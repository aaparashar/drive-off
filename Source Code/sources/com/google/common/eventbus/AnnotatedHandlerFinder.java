package com.google.common.eventbus;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

class AnnotatedHandlerFinder implements HandlerFindingStrategy {
    private static final LoadingCache<Class<?>, ImmutableList<Method>> handlerMethodsCache = CacheBuilder.newBuilder().weakKeys().build(new C04761());

    /* renamed from: com.google.common.eventbus.AnnotatedHandlerFinder$1 */
    static class C04761 extends CacheLoader<Class<?>, ImmutableList<Method>> {
        C04761() {
        }

        public ImmutableList<Method> load(Class<?> concreteClass) throws Exception {
            return AnnotatedHandlerFinder.getAnnotatedMethodsInternal(concreteClass);
        }
    }

    AnnotatedHandlerFinder() {
    }

    public Multimap<Class<?>, EventHandler> findAllHandlers(Object listener) {
        Multimap<Class<?>, EventHandler> methodsInListener = HashMultimap.create();
        Iterator i$ = getAnnotatedMethods(listener.getClass()).iterator();
        while (i$.hasNext()) {
            Method method = (Method) i$.next();
            methodsInListener.put(method.getParameterTypes()[0], makeHandler(listener, method));
        }
        return methodsInListener;
    }

    private static ImmutableList<Method> getAnnotatedMethods(Class<?> clazz) {
        try {
            return (ImmutableList) handlerMethodsCache.getUnchecked(clazz);
        } catch (UncheckedExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    private static ImmutableList<Method> getAnnotatedMethodsInternal(Class<?> clazz) {
        Set<? extends Class<?>> supers = TypeToken.of((Class) clazz).getTypes().rawTypes();
        Builder<Method> result = ImmutableList.builder();
        for (Object method : clazz.getMethods()) {
            for (Class<?> c : supers) {
                try {
                    if (c.getMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Subscribe.class)) {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        if (parameterTypes.length != 1) {
                            throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation, but requires " + parameterTypes.length + " arguments.  Event handler methods must require a single argument.");
                        }
                        Class<?> eventType = parameterTypes[0];
                        result.add(method);
                    }
                } catch (NoSuchMethodException e) {
                }
            }
        }
        return result.build();
    }

    private static EventHandler makeHandler(Object listener, Method method) {
        if (methodIsDeclaredThreadSafe(method)) {
            return new EventHandler(listener, method);
        }
        return new SynchronizedEventHandler(listener, method);
    }

    private static boolean methodIsDeclaredThreadSafe(Method method) {
        return method.getAnnotation(AllowConcurrentEvents.class) != null;
    }
}
