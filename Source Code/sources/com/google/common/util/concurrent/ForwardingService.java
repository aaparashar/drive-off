package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.collect.ForwardingObject;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.concurrent.Executor;

@Deprecated
@Beta
public abstract class ForwardingService extends ForwardingObject implements Service {
    protected abstract Service delegate();

    protected ForwardingService() {
    }

    public ListenableFuture<State> start() {
        return delegate().start();
    }

    public State state() {
        return delegate().state();
    }

    public ListenableFuture<State> stop() {
        return delegate().stop();
    }

    public State startAndWait() {
        return delegate().startAndWait();
    }

    public State stopAndWait() {
        return delegate().stopAndWait();
    }

    public boolean isRunning() {
        return delegate().isRunning();
    }

    public void addListener(Listener listener, Executor executor) {
        delegate().addListener(listener, executor);
    }

    public Throwable failureCause() {
        return delegate().failureCause();
    }

    protected State standardStartAndWait() {
        return (State) Futures.getUnchecked(start());
    }

    protected State standardStopAndWait() {
        return (State) Futures.getUnchecked(stop());
    }
}
