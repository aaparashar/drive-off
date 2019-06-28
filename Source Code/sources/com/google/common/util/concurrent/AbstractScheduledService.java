package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@Beta
public abstract class AbstractScheduledService implements Service {
    private static final Logger logger = Logger.getLogger(AbstractScheduledService.class.getName());
    private final AbstractService delegate = new C06471();

    /* renamed from: com.google.common.util.concurrent.AbstractScheduledService$2 */
    class C02542 implements ThreadFactory {
        C02542() {
        }

        public Thread newThread(Runnable runnable) {
            return MoreExecutors.newThread(AbstractScheduledService.this.serviceName(), runnable);
        }
    }

    public static abstract class Scheduler {
        abstract Future<?> schedule(AbstractService abstractService, ScheduledExecutorService scheduledExecutorService, Runnable runnable);

        public static Scheduler newFixedDelaySchedule(long initialDelay, long delay, TimeUnit unit) {
            final long j = initialDelay;
            final long j2 = delay;
            final TimeUnit timeUnit = unit;
            return new Scheduler() {
                public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task) {
                    return executor.scheduleWithFixedDelay(task, j, j2, timeUnit);
                }
            };
        }

        public static Scheduler newFixedRateSchedule(long initialDelay, long period, TimeUnit unit) {
            final long j = initialDelay;
            final long j2 = period;
            final TimeUnit timeUnit = unit;
            return new Scheduler() {
                public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task) {
                    return executor.scheduleAtFixedRate(task, j, j2, timeUnit);
                }
            };
        }

        private Scheduler() {
        }
    }

    @Beta
    public static abstract class CustomScheduler extends Scheduler {

        @Beta
        protected static final class Schedule {
            private final long delay;
            private final TimeUnit unit;

            public Schedule(long delay, TimeUnit unit) {
                this.delay = delay;
                this.unit = (TimeUnit) Preconditions.checkNotNull(unit);
            }
        }

        private class ReschedulableCallable extends ForwardingFuture<Void> implements Callable<Void> {
            @GuardedBy("lock")
            private Future<Void> currentFuture;
            private final ScheduledExecutorService executor;
            private final ReentrantLock lock = new ReentrantLock();
            private final AbstractService service;
            private final Runnable wrappedRunnable;

            ReschedulableCallable(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
                this.wrappedRunnable = runnable;
                this.executor = executor;
                this.service = service;
            }

            public Void call() throws Exception {
                this.wrappedRunnable.run();
                reschedule();
                return null;
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void reschedule() {
                /*
                r6 = this;
                r2 = r6.lock;
                r2.lock();
                r2 = r6.currentFuture;	 Catch:{ Throwable -> 0x002d }
                if (r2 == 0) goto L_0x0011;
            L_0x0009:
                r2 = r6.currentFuture;	 Catch:{ Throwable -> 0x002d }
                r2 = r2.isCancelled();	 Catch:{ Throwable -> 0x002d }
                if (r2 != 0) goto L_0x0027;
            L_0x0011:
                r2 = com.google.common.util.concurrent.AbstractScheduledService.CustomScheduler.this;	 Catch:{ Throwable -> 0x002d }
                r1 = r2.getNextSchedule();	 Catch:{ Throwable -> 0x002d }
                r2 = r6.executor;	 Catch:{ Throwable -> 0x002d }
                r4 = r1.delay;	 Catch:{ Throwable -> 0x002d }
                r3 = r1.unit;	 Catch:{ Throwable -> 0x002d }
                r2 = r2.schedule(r6, r4, r3);	 Catch:{ Throwable -> 0x002d }
                r6.currentFuture = r2;	 Catch:{ Throwable -> 0x002d }
            L_0x0027:
                r2 = r6.lock;
                r2.unlock();
            L_0x002c:
                return;
            L_0x002d:
                r0 = move-exception;
                r2 = r6.service;	 Catch:{ all -> 0x0039 }
                r2.notifyFailed(r0);	 Catch:{ all -> 0x0039 }
                r2 = r6.lock;
                r2.unlock();
                goto L_0x002c;
            L_0x0039:
                r2 = move-exception;
                r3 = r6.lock;
                r3.unlock();
                throw r2;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.AbstractScheduledService.CustomScheduler.ReschedulableCallable.reschedule():void");
            }

            public boolean cancel(boolean mayInterruptIfRunning) {
                this.lock.lock();
                try {
                    boolean cancel = this.currentFuture.cancel(mayInterruptIfRunning);
                    return cancel;
                } finally {
                    this.lock.unlock();
                }
            }

            protected Future<Void> delegate() {
                throw new UnsupportedOperationException("Only cancel is supported by this future");
            }
        }

        protected abstract Schedule getNextSchedule() throws Exception;

        public CustomScheduler() {
            super();
        }

        final Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
            ReschedulableCallable task = new ReschedulableCallable(service, executor, runnable);
            task.reschedule();
            return task;
        }
    }

    /* renamed from: com.google.common.util.concurrent.AbstractScheduledService$1 */
    class C06471 extends AbstractService {
        private volatile ScheduledExecutorService executorService;
        private final ReentrantLock lock = new ReentrantLock();
        private volatile Future<?> runningTask;
        private final Runnable task = new C02511();

        /* renamed from: com.google.common.util.concurrent.AbstractScheduledService$1$1 */
        class C02511 implements Runnable {
            C02511() {
            }

            public void run() {
                C06471.this.lock.lock();
                try {
                    AbstractScheduledService.this.runOneIteration();
                    C06471.this.lock.unlock();
                } catch (Throwable th) {
                    C06471.this.lock.unlock();
                }
            }
        }

        /* renamed from: com.google.common.util.concurrent.AbstractScheduledService$1$2 */
        class C02522 implements Runnable {
            C02522() {
            }

            public void run() {
                C06471.this.lock.lock();
                try {
                    AbstractScheduledService.this.startUp();
                    C06471.this.runningTask = AbstractScheduledService.this.scheduler().schedule(AbstractScheduledService.this.delegate, C06471.this.executorService, C06471.this.task);
                    C06471.this.notifyStarted();
                    C06471.this.lock.unlock();
                } catch (Throwable th) {
                    C06471.this.lock.unlock();
                }
            }
        }

        /* renamed from: com.google.common.util.concurrent.AbstractScheduledService$1$3 */
        class C02533 implements Runnable {
            C02533() {
            }

            public void run() {
                try {
                    C06471.this.lock.lock();
                    if (C06471.this.state() != State.STOPPING) {
                        C06471.this.lock.unlock();
                        return;
                    }
                    AbstractScheduledService.this.shutDown();
                    C06471.this.lock.unlock();
                    C06471.this.notifyStopped();
                } catch (Throwable t) {
                    C06471.this.notifyFailed(t);
                    RuntimeException propagate = Throwables.propagate(t);
                }
            }
        }

        C06471() {
        }

        protected final void doStart() {
            this.executorService = AbstractScheduledService.this.executor();
            this.executorService.execute(new C02522());
        }

        protected final void doStop() {
            this.runningTask.cancel(false);
            this.executorService.execute(new C02533());
        }
    }

    protected abstract void runOneIteration() throws Exception;

    protected abstract Scheduler scheduler();

    protected AbstractScheduledService() {
    }

    protected void startUp() throws Exception {
    }

    protected void shutDown() throws Exception {
    }

    protected ScheduledExecutorService executor() {
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new C02542());
        addListener(new Listener() {
            public void starting() {
            }

            public void running() {
            }

            public void stopping(State from) {
            }

            public void terminated(State from) {
                executor.shutdown();
            }

            public void failed(State from, Throwable failure) {
                executor.shutdown();
            }
        }, MoreExecutors.sameThreadExecutor());
        return executor;
    }

    protected String serviceName() {
        return getClass().getSimpleName();
    }

    public String toString() {
        return serviceName() + " [" + state() + "]";
    }

    public final ListenableFuture<State> start() {
        return this.delegate.start();
    }

    public final State startAndWait() {
        return this.delegate.startAndWait();
    }

    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    public final State state() {
        return this.delegate.state();
    }

    public final ListenableFuture<State> stop() {
        return this.delegate.stop();
    }

    public final State stopAndWait() {
        return this.delegate.stopAndWait();
    }

    public final void addListener(Listener listener, Executor executor) {
        this.delegate.addListener(listener, executor);
    }

    public final Throwable failureCause() {
        return this.delegate.failureCause();
    }
}
