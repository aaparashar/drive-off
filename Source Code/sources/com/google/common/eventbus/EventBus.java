package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Beta
public class EventBus {
    private static final LoadingCache<Class<?>, Set<Class<?>>> flattenHierarchyCache = CacheBuilder.newBuilder().weakKeys().build(new C04771());
    private final ThreadLocal<Queue<EventWithHandler>> eventsToDispatch;
    private final HandlerFindingStrategy finder;
    private final SetMultimap<Class<?>, EventHandler> handlersByType;
    private final ReadWriteLock handlersByTypeLock;
    private final ThreadLocal<Boolean> isDispatching;
    private final Logger logger;

    /* renamed from: com.google.common.eventbus.EventBus$2 */
    class C02252 extends ThreadLocal<Queue<EventWithHandler>> {
        C02252() {
        }

        protected Queue<EventWithHandler> initialValue() {
            return new LinkedList();
        }
    }

    /* renamed from: com.google.common.eventbus.EventBus$3 */
    class C02263 extends ThreadLocal<Boolean> {
        C02263() {
        }

        protected Boolean initialValue() {
            return Boolean.valueOf(false);
        }
    }

    static class EventWithHandler {
        final Object event;
        final EventHandler handler;

        public EventWithHandler(Object event, EventHandler handler) {
            this.event = Preconditions.checkNotNull(event);
            this.handler = (EventHandler) Preconditions.checkNotNull(handler);
        }
    }

    /* renamed from: com.google.common.eventbus.EventBus$1 */
    static class C04771 extends CacheLoader<Class<?>, Set<Class<?>>> {
        C04771() {
        }

        public Set<Class<?>> load(Class<?> concreteClass) {
            return TypeToken.of((Class) concreteClass).getTypes().rawTypes();
        }
    }

    public EventBus() {
        this("default");
    }

    public EventBus(String identifier) {
        this.handlersByType = HashMultimap.create();
        this.handlersByTypeLock = new ReentrantReadWriteLock();
        this.finder = new AnnotatedHandlerFinder();
        this.eventsToDispatch = new C02252();
        this.isDispatching = new C02263();
        this.logger = Logger.getLogger(EventBus.class.getName() + "." + ((String) Preconditions.checkNotNull(identifier)));
    }

    public void register(Object object) {
        Multimap<Class<?>, EventHandler> methodsInListener = this.finder.findAllHandlers(object);
        this.handlersByTypeLock.writeLock().lock();
        try {
            this.handlersByType.putAll(methodsInListener);
        } finally {
            this.handlersByTypeLock.writeLock().unlock();
        }
    }

    public void unregister(Object object) {
        for (Entry<Class<?>, Collection<EventHandler>> entry : this.finder.findAllHandlers(object).asMap().entrySet()) {
            Class<?> eventType = (Class) entry.getKey();
            Collection<EventHandler> eventMethodsInListener = (Collection) entry.getValue();
            this.handlersByTypeLock.writeLock().lock();
            try {
                Set<EventHandler> currentHandlers = this.handlersByType.get(eventType);
                if (currentHandlers.containsAll(eventMethodsInListener)) {
                    currentHandlers.removeAll(eventMethodsInListener);
                    this.handlersByTypeLock.writeLock().unlock();
                } else {
                    throw new IllegalArgumentException("missing event handler for an annotated method. Is " + object + " registered?");
                }
            } catch (Throwable th) {
                this.handlersByTypeLock.writeLock().unlock();
                throw th;
            }
        }
    }

    public void post(Object event) {
        boolean dispatched = false;
        for (Class<?> eventType : flattenHierarchy(event.getClass())) {
            this.handlersByTypeLock.readLock().lock();
            try {
                Set<EventHandler> wrappers = this.handlersByType.get(eventType);
                if (!wrappers.isEmpty()) {
                    dispatched = true;
                    for (EventHandler wrapper : wrappers) {
                        enqueueEvent(event, wrapper);
                    }
                }
                this.handlersByTypeLock.readLock().unlock();
            } catch (Throwable th) {
                this.handlersByTypeLock.readLock().unlock();
            }
        }
        if (!(dispatched || (event instanceof DeadEvent))) {
            post(new DeadEvent(this, event));
        }
        dispatchQueuedEvents();
    }

    void enqueueEvent(Object event, EventHandler handler) {
        ((Queue) this.eventsToDispatch.get()).offer(new EventWithHandler(event, handler));
    }

    void dispatchQueuedEvents() {
        if (!((Boolean) this.isDispatching.get()).booleanValue()) {
            this.isDispatching.set(Boolean.valueOf(true));
            try {
                Queue<EventWithHandler> events = (Queue) this.eventsToDispatch.get();
                while (true) {
                    EventWithHandler eventWithHandler = (EventWithHandler) events.poll();
                    if (eventWithHandler == null) {
                        break;
                    }
                    dispatch(eventWithHandler.event, eventWithHandler.handler);
                }
            } finally {
                this.isDispatching.remove();
                this.eventsToDispatch.remove();
            }
        }
    }

    void dispatch(Object event, EventHandler wrapper) {
        try {
            wrapper.handleEvent(event);
        } catch (InvocationTargetException e) {
            this.logger.log(Level.SEVERE, "Could not dispatch event: " + event + " to handler " + wrapper, e);
        }
    }

    @VisibleForTesting
    Set<Class<?>> flattenHierarchy(Class<?> concreteClass) {
        try {
            return (Set) flattenHierarchyCache.getUnchecked(concreteClass);
        } catch (UncheckedExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }
}
