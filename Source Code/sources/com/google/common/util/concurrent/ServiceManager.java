package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Monitor.Guard;
import com.google.common.util.concurrent.Service.State;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Beta
public final class ServiceManager {
    private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());
    private final ImmutableMap<Service, ServiceListener> services;
    private final ServiceManagerState state;

    @Beta
    public interface Listener {
        void failure(Service service);

        void healthy();

        void stopped();
    }

    @Immutable
    private static final class ListenerExecutorPair {
        final Executor executor;
        final Listener listener;

        ListenerExecutorPair(Listener listener, Executor executor) {
            this.listener = listener;
            this.executor = executor;
        }

        void execute(Runnable runnable) {
            try {
                this.executor.execute(runnable);
            } catch (Exception e) {
                ServiceManager.logger.log(Level.SEVERE, "Exception while executing listener " + this.listener + " with executor " + this.executor, e);
            }
        }
    }

    private static final class ServiceManagerState {
        final Guard awaitHealthGuard = new Guard(this.monitor) {
            public boolean isSatisfied() {
                return ServiceManagerState.this.unstartedServices == 0 || ServiceManagerState.this.unstoppedServices != ServiceManagerState.this.numberOfServices;
            }
        };
        @GuardedBy("monitor")
        final List<ListenerExecutorPair> listeners = Lists.newArrayList();
        final Monitor monitor = new Monitor();
        final int numberOfServices;
        @GuardedBy("queuedListeners")
        final Queue<Runnable> queuedListeners = Queues.newConcurrentLinkedQueue();
        final Guard stoppedGuard = new Guard(this.monitor) {
            public boolean isSatisfied() {
                return ServiceManagerState.this.unstoppedServices == 0;
            }
        };
        @GuardedBy("monitor")
        int unstartedServices;
        @GuardedBy("monitor")
        int unstoppedServices;

        ServiceManagerState(int numberOfServices) {
            this.numberOfServices = numberOfServices;
            this.unstoppedServices = numberOfServices;
            this.unstartedServices = numberOfServices;
        }

        void addListener(Listener listener, Executor executor) {
            Preconditions.checkNotNull(listener, "listener");
            Preconditions.checkNotNull(executor, "executor");
            this.monitor.enter();
            try {
                if (this.unstartedServices > 0 || this.unstoppedServices > 0) {
                    this.listeners.add(new ListenerExecutorPair(listener, executor));
                }
                this.monitor.leave();
            } catch (Throwable th) {
                this.monitor.leave();
            }
        }

        void awaitHealthy() {
            this.monitor.enter();
            try {
                this.monitor.waitForUninterruptibly(this.awaitHealthGuard);
            } finally {
                this.monitor.leave();
            }
        }

        boolean awaitHealthy(long timeout, TimeUnit unit) {
            this.monitor.enter();
            try {
                if (this.monitor.waitForUninterruptibly(this.awaitHealthGuard, timeout, unit)) {
                    return true;
                }
                this.monitor.leave();
                return false;
            } finally {
                this.monitor.leave();
            }
        }

        void awaitStopped() {
            this.monitor.enter();
            try {
                this.monitor.waitForUninterruptibly(this.stoppedGuard);
            } finally {
                this.monitor.leave();
            }
        }

        boolean awaitStopped(long timeout, TimeUnit unit) {
            this.monitor.enter();
            try {
                boolean waitForUninterruptibly = this.monitor.waitForUninterruptibly(this.stoppedGuard, timeout, unit);
                return waitForUninterruptibly;
            } finally {
                this.monitor.leave();
            }
        }

        @GuardedBy("monitor")
        private void serviceFinishedStarting(Service service, boolean currentlyHealthy) {
            boolean z;
            if (this.unstartedServices > 0) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkState(z, "All services should have already finished starting but %s just finished.", service);
            this.unstartedServices--;
            if (currentlyHealthy && this.unstartedServices == 0 && this.unstoppedServices == this.numberOfServices) {
                for (final ListenerExecutorPair pair : this.listeners) {
                    this.queuedListeners.add(new Runnable() {

                        /* renamed from: com.google.common.util.concurrent.ServiceManager$ServiceManagerState$3$1 */
                        class C02771 implements Runnable {
                            C02771() {
                            }

                            public void run() {
                                pair.listener.healthy();
                            }
                        }

                        public void run() {
                            pair.execute(new C02771());
                        }
                    });
                }
            }
        }

        @GuardedBy("monitor")
        private void serviceTerminated(Service service) {
            serviceStopped(service);
        }

        @GuardedBy("monitor")
        private void serviceFailed(final Service service) {
            for (final ListenerExecutorPair pair : this.listeners) {
                this.queuedListeners.add(new Runnable() {

                    /* renamed from: com.google.common.util.concurrent.ServiceManager$ServiceManagerState$4$1 */
                    class C02791 implements Runnable {
                        C02791() {
                        }

                        public void run() {
                            pair.listener.failure(service);
                        }
                    }

                    public void run() {
                        pair.execute(new C02791());
                    }
                });
            }
            serviceStopped(service);
        }

        @GuardedBy("monitor")
        private void serviceStopped(Service service) {
            boolean z;
            if (this.unstoppedServices > 0) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkState(z, "All services should have already stopped but %s just stopped.", service);
            this.unstoppedServices--;
            if (this.unstoppedServices == 0) {
                if (this.unstartedServices == 0) {
                    z = true;
                } else {
                    z = false;
                }
                Preconditions.checkState(z, "All services are stopped but %d services haven't finished starting", Integer.valueOf(this.unstartedServices));
                for (final ListenerExecutorPair pair : this.listeners) {
                    this.queuedListeners.add(new Runnable() {

                        /* renamed from: com.google.common.util.concurrent.ServiceManager$ServiceManagerState$5$1 */
                        class C02811 implements Runnable {
                            C02811() {
                            }

                            public void run() {
                                pair.listener.stopped();
                            }
                        }

                        public void run() {
                            pair.execute(new C02811());
                        }
                    });
                }
                this.listeners.clear();
            }
        }

        private void executeListeners() {
            Preconditions.checkState(!this.monitor.isOccupiedByCurrentThread(), "It is incorrect to execute listeners with the monitor held.");
            synchronized (this.queuedListeners) {
                while (true) {
                    Runnable listener = (Runnable) this.queuedListeners.poll();
                    if (listener != null) {
                        listener.run();
                    }
                }
            }
        }
    }

    /* renamed from: com.google.common.util.concurrent.ServiceManager$1 */
    class C05291 implements Function<Entry<Service, Long>, Long> {
        C05291() {
        }

        public Long apply(Entry<Service, Long> input) {
            return (Long) input.getValue();
        }
    }

    private static final class ServiceListener implements com.google.common.util.concurrent.Service.Listener {
        final Service service;
        final ServiceManagerState state;
        @GuardedBy("watch")
        final Stopwatch watch = new Stopwatch();

        ServiceListener(Service service, ServiceManagerState state) {
            this.service = service;
            this.state = state;
        }

        public void starting() {
            startTimer();
        }

        public void running() {
            this.state.monitor.enter();
            try {
                finishedStarting(true);
            } finally {
                this.state.monitor.leave();
                this.state.executeListeners();
            }
        }

        public void stopping(State from) {
            if (from == State.STARTING) {
                this.state.monitor.enter();
                try {
                    finishedStarting(false);
                } finally {
                    this.state.monitor.leave();
                    this.state.executeListeners();
                }
            }
        }

        public void terminated(State from) {
            ServiceManager.logger.info("Service " + this.service + " has terminated. Previous state was " + from + " state.");
            this.state.monitor.enter();
            try {
                if (from == State.NEW) {
                    startTimer();
                    finishedStarting(false);
                }
                this.state.serviceTerminated(this.service);
            } finally {
                this.state.monitor.leave();
                this.state.executeListeners();
            }
        }

        public void failed(State from, Throwable failure) {
            ServiceManager.logger.log(Level.SEVERE, "Service " + this.service + " has failed in the " + from + " state.", failure);
            this.state.monitor.enter();
            try {
                if (from == State.STARTING) {
                    finishedStarting(false);
                }
                this.state.serviceFailed(this.service);
            } finally {
                this.state.monitor.leave();
                this.state.executeListeners();
            }
        }

        @GuardedBy("monitor")
        void finishedStarting(boolean currentlyHealthy) {
            synchronized (this.watch) {
                this.watch.stop();
                ServiceManager.logger.log(Level.INFO, "Started " + this.service + " in " + startupTimeMillis() + " ms.");
            }
            this.state.serviceFinishedStarting(this.service, currentlyHealthy);
        }

        void start() {
            startTimer();
            this.service.start();
        }

        void startTimer() {
            synchronized (this.watch) {
                if (!this.watch.isRunning()) {
                    this.watch.start();
                    ServiceManager.logger.log(Level.INFO, "Starting {0}", this.service);
                }
            }
        }

        synchronized long startupTimeMillis() {
            long elapsed;
            synchronized (this.watch) {
                elapsed = this.watch.elapsed(TimeUnit.MILLISECONDS);
            }
            return elapsed;
        }
    }

    public ServiceManager(Iterable<? extends Service> services) {
        ImmutableList<Service> copy = ImmutableList.copyOf((Iterable) services);
        this.state = new ServiceManagerState(copy.size());
        Builder<Service, ServiceListener> builder = ImmutableMap.builder();
        Executor executor = MoreExecutors.sameThreadExecutor();
        Iterator i$ = copy.iterator();
        while (i$.hasNext()) {
            boolean z;
            Service service = (Service) i$.next();
            ServiceListener listener = new ServiceListener(service, this.state);
            service.addListener(listener, executor);
            if (service.state() == State.NEW) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "Can only manage NEW services, %s", service);
            builder.put(service, listener);
        }
        this.services = builder.build();
    }

    @Inject
    ServiceManager(Set<Service> services) {
        this((Iterable) services);
    }

    public void addListener(Listener listener, Executor executor) {
        this.state.addListener(listener, executor);
    }

    public ServiceManager startAsync() {
        Iterator i$ = this.services.entrySet().iterator();
        while (i$.hasNext()) {
            Preconditions.checkState(((Service) ((Entry) i$.next()).getKey()).state() == State.NEW, "Service %s is %s, cannot start it.", (Service) ((Entry) i$.next()).getKey(), ((Service) ((Entry) i$.next()).getKey()).state());
        }
        i$ = this.services.values().iterator();
        while (i$.hasNext()) {
            ((ServiceListener) i$.next()).start();
        }
        return this;
    }

    public void awaitHealthy() {
        this.state.awaitHealthy();
        Preconditions.checkState(isHealthy(), "Expected to be healthy after starting");
    }

    public void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException {
        if (this.state.awaitHealthy(timeout, unit)) {
            Preconditions.checkState(isHealthy(), "Expected to be healthy after starting");
            return;
        }
        throw new TimeoutException("Timeout waiting for the services to become healthy.");
    }

    public ServiceManager stopAsync() {
        Iterator i$ = this.services.keySet().iterator();
        while (i$.hasNext()) {
            ((Service) i$.next()).stop();
        }
        return this;
    }

    public void awaitStopped() {
        this.state.awaitStopped();
    }

    public void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
        if (!this.state.awaitStopped(timeout, unit)) {
            throw new TimeoutException("Timeout waiting for the services to stop.");
        }
    }

    public boolean isHealthy() {
        Iterator i$ = this.services.keySet().iterator();
        while (i$.hasNext()) {
            if (!((Service) i$.next()).isRunning()) {
                return false;
            }
        }
        return true;
    }

    public ImmutableMultimap<State, Service> servicesByState() {
        ImmutableMultimap.Builder<State, Service> builder = ImmutableMultimap.builder();
        Iterator i$ = this.services.keySet().iterator();
        while (i$.hasNext()) {
            Service service = (Service) i$.next();
            builder.put(service.state(), service);
        }
        return builder.build();
    }

    public ImmutableMap<Service, Long> startupTimes() {
        Map<Service, Long> loadTimeMap = Maps.newHashMapWithExpectedSize(this.services.size());
        Iterator i$ = this.services.entrySet().iterator();
        while (i$.hasNext()) {
            Entry<Service, ServiceListener> entry = (Entry) i$.next();
            State state = ((Service) entry.getKey()).state();
            if (!(state == State.NEW || state == State.STARTING)) {
                loadTimeMap.put(entry.getKey(), Long.valueOf(((ServiceListener) entry.getValue()).startupTimeMillis()));
            }
        }
        List<Entry<Service, Long>> servicesByStartTime = Ordering.natural().onResultOf(new C05291()).sortedCopy(loadTimeMap.entrySet());
        Builder<Service, Long> builder = ImmutableMap.builder();
        for (Entry<Service, Long> entry2 : servicesByStartTime) {
            builder.put(entry2);
        }
        return builder.build();
    }

    public String toString() {
        return Objects.toStringHelper(ServiceManager.class).add("services", this.services.keySet()).toString();
    }
}
