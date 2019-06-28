package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;

@Beta
public abstract class AbstractService implements Service {
    private static final Logger logger = Logger.getLogger(AbstractService.class.getName());
    @GuardedBy("lock")
    private final List<ListenerExecutorPair> listeners = Lists.newArrayList();
    private final ReentrantLock lock = new ReentrantLock();
    @GuardedBy("queuedListeners")
    private final Queue<Runnable> queuedListeners = Queues.newConcurrentLinkedQueue();
    private final Transition shutdown = new Transition();
    @GuardedBy("lock")
    private volatile StateSnapshot snapshot = new StateSnapshot(State.NEW);
    private final Transition startup = new Transition();

    private static class ListenerExecutorPair {
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
                AbstractService.logger.log(Level.SEVERE, "Exception while executing listener " + this.listener + " with executor " + this.executor, e);
            }
        }
    }

    @Immutable
    private static final class StateSnapshot {
        @Nullable
        final Throwable failure;
        final boolean shutdownWhenStartupFinishes;
        final State state;

        StateSnapshot(State internalState) {
            this(internalState, false, null);
        }

        StateSnapshot(State internalState, boolean shutdownWhenStartupFinishes, @Nullable Throwable failure) {
            int i;
            boolean z = !shutdownWhenStartupFinishes || internalState == State.STARTING;
            Preconditions.checkArgument(z, "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", internalState);
            if (failure != null) {
                i = 1;
            } else {
                i = 0;
            }
            Preconditions.checkArgument((i ^ (internalState == State.FAILED ? 1 : 0)) == 0, "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", internalState, failure);
            this.state = internalState;
            this.shutdownWhenStartupFinishes = shutdownWhenStartupFinishes;
            this.failure = failure;
        }

        State externalState() {
            if (this.shutdownWhenStartupFinishes && this.state == State.STARTING) {
                return State.STOPPING;
            }
            return this.state;
        }

        Throwable failureCause() {
            boolean z;
            if (this.state == State.FAILED) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkState(z, "failureCause() is only valid if the service has failed, service is %s", this.state);
            return this.failure;
        }
    }

    /* renamed from: com.google.common.util.concurrent.AbstractService$1 */
    class C05211 implements Listener {
        C05211() {
        }

        public void starting() {
        }

        public void running() {
            AbstractService.this.startup.set(State.RUNNING);
        }

        public void stopping(State from) {
            if (from == State.STARTING) {
                AbstractService.this.startup.set(State.STOPPING);
            }
        }

        public void terminated(State from) {
            if (from == State.NEW) {
                AbstractService.this.startup.set(State.TERMINATED);
            }
            AbstractService.this.shutdown.set(State.TERMINATED);
        }

        public void failed(State from, Throwable failure) {
            switch (from) {
                case STARTING:
                    AbstractService.this.startup.setException(failure);
                    AbstractService.this.shutdown.setException(new Exception("Service failed to start.", failure));
                    return;
                case RUNNING:
                    AbstractService.this.shutdown.setException(new Exception("Service failed while running", failure));
                    return;
                case STOPPING:
                    AbstractService.this.shutdown.setException(failure);
                    return;
                default:
                    throw new AssertionError("Unexpected from state: " + from);
            }
        }
    }

    private class Transition extends AbstractFuture<State> {
        private Transition() {
        }

        public State get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
            try {
                return (State) super.get(timeout, unit);
            } catch (TimeoutException e) {
                throw new TimeoutException(AbstractService.this.toString());
            }
        }
    }

    protected abstract void doStart();

    protected abstract void doStop();

    protected AbstractService() {
        addListener(new C05211(), MoreExecutors.sameThreadExecutor());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.common.util.concurrent.ListenableFuture<com.google.common.util.concurrent.Service.State> start() {
        /*
        r3 = this;
        r1 = r3.lock;
        r1.lock();
        r1 = r3.snapshot;	 Catch:{ Throwable -> 0x0027 }
        r1 = r1.state;	 Catch:{ Throwable -> 0x0027 }
        r2 = com.google.common.util.concurrent.Service.State.NEW;	 Catch:{ Throwable -> 0x0027 }
        if (r1 != r2) goto L_0x001c;
    L_0x000d:
        r1 = new com.google.common.util.concurrent.AbstractService$StateSnapshot;	 Catch:{ Throwable -> 0x0027 }
        r2 = com.google.common.util.concurrent.Service.State.STARTING;	 Catch:{ Throwable -> 0x0027 }
        r1.<init>(r2);	 Catch:{ Throwable -> 0x0027 }
        r3.snapshot = r1;	 Catch:{ Throwable -> 0x0027 }
        r3.starting();	 Catch:{ Throwable -> 0x0027 }
        r3.doStart();	 Catch:{ Throwable -> 0x0027 }
    L_0x001c:
        r1 = r3.lock;
        r1.unlock();
        r3.executeListeners();
    L_0x0024:
        r1 = r3.startup;
        return r1;
    L_0x0027:
        r0 = move-exception;
        r3.notifyFailed(r0);	 Catch:{ all -> 0x0034 }
        r1 = r3.lock;
        r1.unlock();
        r3.executeListeners();
        goto L_0x0024;
    L_0x0034:
        r1 = move-exception;
        r2 = r3.lock;
        r2.unlock();
        r3.executeListeners();
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.AbstractService.start():com.google.common.util.concurrent.ListenableFuture<com.google.common.util.concurrent.Service$State>");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.common.util.concurrent.ListenableFuture<com.google.common.util.concurrent.Service.State> stop() {
        /*
        r5 = this;
        r1 = r5.lock;
        r1.lock();
        r1 = com.google.common.util.concurrent.AbstractService.C02657.$SwitchMap$com$google$common$util$concurrent$Service$State;	 Catch:{ Throwable -> 0x0031 }
        r2 = r5.snapshot;	 Catch:{ Throwable -> 0x0031 }
        r2 = r2.state;	 Catch:{ Throwable -> 0x0031 }
        r2 = r2.ordinal();	 Catch:{ Throwable -> 0x0031 }
        r1 = r1[r2];	 Catch:{ Throwable -> 0x0031 }
        switch(r1) {
            case 1: goto L_0x0057;
            case 2: goto L_0x0072;
            case 3: goto L_0x004e;
            case 4: goto L_0x004e;
            case 5: goto L_0x004e;
            case 6: goto L_0x0040;
            default: goto L_0x0014;
        };	 Catch:{ Throwable -> 0x0031 }
    L_0x0014:
        r1 = new java.lang.AssertionError;	 Catch:{ Throwable -> 0x0031 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0031 }
        r2.<init>();	 Catch:{ Throwable -> 0x0031 }
        r3 = "Unexpected state: ";
        r2 = r2.append(r3);	 Catch:{ Throwable -> 0x0031 }
        r3 = r5.snapshot;	 Catch:{ Throwable -> 0x0031 }
        r3 = r3.state;	 Catch:{ Throwable -> 0x0031 }
        r2 = r2.append(r3);	 Catch:{ Throwable -> 0x0031 }
        r2 = r2.toString();	 Catch:{ Throwable -> 0x0031 }
        r1.<init>(r2);	 Catch:{ Throwable -> 0x0031 }
        throw r1;	 Catch:{ Throwable -> 0x0031 }
    L_0x0031:
        r0 = move-exception;
        r5.notifyFailed(r0);	 Catch:{ all -> 0x0068 }
        r1 = r5.lock;
        r1.unlock();
        r5.executeListeners();
    L_0x003d:
        r1 = r5.shutdown;
        return r1;
    L_0x0040:
        r1 = new com.google.common.util.concurrent.AbstractService$StateSnapshot;	 Catch:{ Throwable -> 0x0031 }
        r2 = com.google.common.util.concurrent.Service.State.TERMINATED;	 Catch:{ Throwable -> 0x0031 }
        r1.<init>(r2);	 Catch:{ Throwable -> 0x0031 }
        r5.snapshot = r1;	 Catch:{ Throwable -> 0x0031 }
        r1 = com.google.common.util.concurrent.Service.State.NEW;	 Catch:{ Throwable -> 0x0031 }
        r5.terminated(r1);	 Catch:{ Throwable -> 0x0031 }
    L_0x004e:
        r1 = r5.lock;
        r1.unlock();
        r5.executeListeners();
        goto L_0x003d;
    L_0x0057:
        r1 = new com.google.common.util.concurrent.AbstractService$StateSnapshot;	 Catch:{ Throwable -> 0x0031 }
        r2 = com.google.common.util.concurrent.Service.State.STARTING;	 Catch:{ Throwable -> 0x0031 }
        r3 = 1;
        r4 = 0;
        r1.<init>(r2, r3, r4);	 Catch:{ Throwable -> 0x0031 }
        r5.snapshot = r1;	 Catch:{ Throwable -> 0x0031 }
        r1 = com.google.common.util.concurrent.Service.State.STARTING;	 Catch:{ Throwable -> 0x0031 }
        r5.stopping(r1);	 Catch:{ Throwable -> 0x0031 }
        goto L_0x004e;
    L_0x0068:
        r1 = move-exception;
        r2 = r5.lock;
        r2.unlock();
        r5.executeListeners();
        throw r1;
    L_0x0072:
        r1 = new com.google.common.util.concurrent.AbstractService$StateSnapshot;	 Catch:{ Throwable -> 0x0031 }
        r2 = com.google.common.util.concurrent.Service.State.STOPPING;	 Catch:{ Throwable -> 0x0031 }
        r1.<init>(r2);	 Catch:{ Throwable -> 0x0031 }
        r5.snapshot = r1;	 Catch:{ Throwable -> 0x0031 }
        r1 = com.google.common.util.concurrent.Service.State.RUNNING;	 Catch:{ Throwable -> 0x0031 }
        r5.stopping(r1);	 Catch:{ Throwable -> 0x0031 }
        r5.doStop();	 Catch:{ Throwable -> 0x0031 }
        goto L_0x004e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.AbstractService.stop():com.google.common.util.concurrent.ListenableFuture<com.google.common.util.concurrent.Service$State>");
    }

    public State startAndWait() {
        return (State) Futures.getUnchecked(start());
    }

    public State stopAndWait() {
        return (State) Futures.getUnchecked(stop());
    }

    protected final void notifyStarted() {
        this.lock.lock();
        try {
            if (this.snapshot.state != State.STARTING) {
                IllegalStateException failure = new IllegalStateException("Cannot notifyStarted() when the service is " + this.snapshot.state);
                notifyFailed(failure);
                throw failure;
            }
            if (this.snapshot.shutdownWhenStartupFinishes) {
                this.snapshot = new StateSnapshot(State.STOPPING);
                doStop();
            } else {
                this.snapshot = new StateSnapshot(State.RUNNING);
                running();
            }
            this.lock.unlock();
            executeListeners();
        } catch (Throwable th) {
            this.lock.unlock();
            executeListeners();
        }
    }

    protected final void notifyStopped() {
        this.lock.lock();
        try {
            if (this.snapshot.state == State.STOPPING || this.snapshot.state == State.RUNNING) {
                State previous = this.snapshot.state;
                this.snapshot = new StateSnapshot(State.TERMINATED);
                terminated(previous);
                return;
            }
            IllegalStateException failure = new IllegalStateException("Cannot notifyStopped() when the service is " + this.snapshot.state);
            notifyFailed(failure);
            throw failure;
        } finally {
            this.lock.unlock();
            executeListeners();
        }
    }

    protected final void notifyFailed(Throwable cause) {
        Preconditions.checkNotNull(cause);
        this.lock.lock();
        try {
            switch (this.snapshot.state) {
                case STARTING:
                case RUNNING:
                case STOPPING:
                    State previous = this.snapshot.state;
                    this.snapshot = new StateSnapshot(State.FAILED, false, cause);
                    failed(previous, cause);
                    break;
                case TERMINATED:
                case NEW:
                    throw new IllegalStateException("Failed while in state:" + this.snapshot.state, cause);
                case FAILED:
                    break;
                default:
                    throw new AssertionError("Unexpected state: " + this.snapshot.state);
            }
            this.lock.unlock();
            executeListeners();
        } catch (Throwable th) {
            this.lock.unlock();
            executeListeners();
        }
    }

    public final boolean isRunning() {
        return state() == State.RUNNING;
    }

    public final State state() {
        return this.snapshot.externalState();
    }

    public final Throwable failureCause() {
        return this.snapshot.failureCause();
    }

    public final void addListener(Listener listener, Executor executor) {
        Preconditions.checkNotNull(listener, "listener");
        Preconditions.checkNotNull(executor, "executor");
        this.lock.lock();
        try {
            if (!(this.snapshot.state == State.TERMINATED || this.snapshot.state == State.FAILED)) {
                this.listeners.add(new ListenerExecutorPair(listener, executor));
            }
            this.lock.unlock();
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    public String toString() {
        return getClass().getSimpleName() + " [" + state() + "]";
    }

    private void executeListeners() {
        if (!this.lock.isHeldByCurrentThread()) {
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

    @GuardedBy("lock")
    private void starting() {
        for (final ListenerExecutorPair pair : this.listeners) {
            this.queuedListeners.add(new Runnable() {

                /* renamed from: com.google.common.util.concurrent.AbstractService$2$1 */
                class C02551 implements Runnable {
                    C02551() {
                    }

                    public void run() {
                        pair.listener.starting();
                    }
                }

                public void run() {
                    pair.execute(new C02551());
                }
            });
        }
    }

    @GuardedBy("lock")
    private void running() {
        for (final ListenerExecutorPair pair : this.listeners) {
            this.queuedListeners.add(new Runnable() {

                /* renamed from: com.google.common.util.concurrent.AbstractService$3$1 */
                class C02571 implements Runnable {
                    C02571() {
                    }

                    public void run() {
                        pair.listener.running();
                    }
                }

                public void run() {
                    pair.execute(new C02571());
                }
            });
        }
    }

    @GuardedBy("lock")
    private void stopping(final State from) {
        for (final ListenerExecutorPair pair : this.listeners) {
            this.queuedListeners.add(new Runnable() {

                /* renamed from: com.google.common.util.concurrent.AbstractService$4$1 */
                class C02591 implements Runnable {
                    C02591() {
                    }

                    public void run() {
                        pair.listener.stopping(from);
                    }
                }

                public void run() {
                    pair.execute(new C02591());
                }
            });
        }
    }

    @GuardedBy("lock")
    private void terminated(final State from) {
        for (final ListenerExecutorPair pair : this.listeners) {
            this.queuedListeners.add(new Runnable() {

                /* renamed from: com.google.common.util.concurrent.AbstractService$5$1 */
                class C02611 implements Runnable {
                    C02611() {
                    }

                    public void run() {
                        pair.listener.terminated(from);
                    }
                }

                public void run() {
                    pair.execute(new C02611());
                }
            });
        }
        this.listeners.clear();
    }

    @GuardedBy("lock")
    private void failed(final State from, final Throwable cause) {
        for (final ListenerExecutorPair pair : this.listeners) {
            this.queuedListeners.add(new Runnable() {

                /* renamed from: com.google.common.util.concurrent.AbstractService$6$1 */
                class C02631 implements Runnable {
                    C02631() {
                    }

                    public void run() {
                        pair.listener.failed(from, cause);
                    }
                }

                public void run() {
                    pair.execute(new C02631());
                }
            });
        }
        this.listeners.clear();
    }
}
