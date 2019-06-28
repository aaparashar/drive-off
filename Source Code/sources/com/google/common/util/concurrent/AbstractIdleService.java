package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.concurrent.Executor;

@Beta
public abstract class AbstractIdleService implements Service {
    private final Service delegate = new C06461();

    /* renamed from: com.google.common.util.concurrent.AbstractIdleService$1 */
    class C06461 extends AbstractService {

        /* renamed from: com.google.common.util.concurrent.AbstractIdleService$1$1 */
        class C02481 implements Runnable {
            C02481() {
            }

            public void run() {
                try {
                    AbstractIdleService.this.startUp();
                    C06461.this.notifyStarted();
                } catch (Throwable t) {
                    C06461.this.notifyFailed(t);
                    RuntimeException propagate = Throwables.propagate(t);
                }
            }
        }

        /* renamed from: com.google.common.util.concurrent.AbstractIdleService$1$2 */
        class C02492 implements Runnable {
            C02492() {
            }

            public void run() {
                try {
                    AbstractIdleService.this.shutDown();
                    C06461.this.notifyStopped();
                } catch (Throwable t) {
                    C06461.this.notifyFailed(t);
                    RuntimeException propagate = Throwables.propagate(t);
                }
            }
        }

        C06461() {
        }

        protected final void doStart() {
            AbstractIdleService.this.executor().execute(new C02481());
        }

        protected final void doStop() {
            AbstractIdleService.this.executor().execute(new C02492());
        }
    }

    protected abstract void shutDown() throws Exception;

    protected abstract void startUp() throws Exception;

    protected AbstractIdleService() {
    }

    protected Executor executor() {
        final State state = state();
        return new Executor() {
            public void execute(Runnable command) {
                MoreExecutors.newThread(AbstractIdleService.this.serviceName() + " " + state, command).start();
            }
        };
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

    protected String serviceName() {
        return getClass().getSimpleName();
    }
}
