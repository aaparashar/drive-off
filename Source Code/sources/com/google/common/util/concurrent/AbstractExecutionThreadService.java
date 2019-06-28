package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

@Beta
public abstract class AbstractExecutionThreadService implements Service {
    private static final Logger logger = Logger.getLogger(AbstractExecutionThreadService.class.getName());
    private final Service delegate = new C06451();

    /* renamed from: com.google.common.util.concurrent.AbstractExecutionThreadService$2 */
    class C02472 implements Executor {
        C02472() {
        }

        public void execute(Runnable command) {
            MoreExecutors.newThread(AbstractExecutionThreadService.this.serviceName(), command).start();
        }
    }

    /* renamed from: com.google.common.util.concurrent.AbstractExecutionThreadService$1 */
    class C06451 extends AbstractService {

        /* renamed from: com.google.common.util.concurrent.AbstractExecutionThreadService$1$1 */
        class C02461 implements Runnable {
            C02461() {
            }

            public void run() {
                try {
                    AbstractExecutionThreadService.this.startUp();
                    C06451.this.notifyStarted();
                    if (C06451.this.isRunning()) {
                        AbstractExecutionThreadService.this.run();
                    }
                    AbstractExecutionThreadService.this.shutDown();
                    C06451.this.notifyStopped();
                } catch (Throwable t) {
                    C06451.this.notifyFailed(t);
                    RuntimeException propagate = Throwables.propagate(t);
                }
            }
        }

        C06451() {
        }

        protected final void doStart() {
            AbstractExecutionThreadService.this.executor().execute(new C02461());
        }

        protected void doStop() {
            AbstractExecutionThreadService.this.triggerShutdown();
        }
    }

    protected abstract void run() throws Exception;

    protected AbstractExecutionThreadService() {
    }

    protected void startUp() throws Exception {
    }

    protected void shutDown() throws Exception {
    }

    protected void triggerShutdown() {
    }

    protected Executor executor() {
        return new C02472();
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
