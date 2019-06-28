package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

@Beta
public class AsyncEventBus extends EventBus {
    private final ConcurrentLinkedQueue<EventWithHandler> eventsToDispatch = new ConcurrentLinkedQueue();
    private final Executor executor;

    public AsyncEventBus(String identifier, Executor executor) {
        super(identifier);
        this.executor = (Executor) Preconditions.checkNotNull(executor);
    }

    public AsyncEventBus(Executor executor) {
        this.executor = (Executor) Preconditions.checkNotNull(executor);
    }

    void enqueueEvent(Object event, EventHandler handler) {
        this.eventsToDispatch.offer(new EventWithHandler(event, handler));
    }

    protected void dispatchQueuedEvents() {
        while (true) {
            EventWithHandler eventWithHandler = (EventWithHandler) this.eventsToDispatch.poll();
            if (eventWithHandler != null) {
                dispatch(eventWithHandler.event, eventWithHandler.handler);
            } else {
                return;
            }
        }
    }

    void dispatch(final Object event, final EventHandler handler) {
        Preconditions.checkNotNull(event);
        Preconditions.checkNotNull(handler);
        this.executor.execute(new Runnable() {
            public void run() {
                super.dispatch(event, handler);
            }
        });
    }
}
