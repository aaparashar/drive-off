package com.google.common.reflect;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

class TypeResolver {
    private final ImmutableMap<TypeVariable<?>, Type> typeTable;

    private static final class TypeMappingIntrospector {
        private static final WildcardCapturer wildcardCapturer = new WildcardCapturer();
        private final Set<Type> introspectedTypes = Sets.newHashSet();
        private final Map<TypeVariable<?>, Type> mappings = Maps.newHashMap();

        private TypeMappingIntrospector() {
        }

        static ImmutableMap<TypeVariable<?>, Type> getTypeMappings(Type contextType) {
            TypeMappingIntrospector introspector = new TypeMappingIntrospector();
            introspector.introspect(wildcardCapturer.capture(contextType));
            return ImmutableMap.copyOf(introspector.mappings);
        }

        private void introspect(Type type) {
            if (!this.introspectedTypes.add(type)) {
                return;
            }
            if (type instanceof ParameterizedType) {
                introspectParameterizedType((ParameterizedType) type);
            } else if (type instanceof Class) {
                introspectClass((Class) type);
            } else if (type instanceof TypeVariable) {
                for (Type bound : ((TypeVariable) type).getBounds()) {
                    introspect(bound);
                }
            } else if (type instanceof WildcardType) {
                for (Type bound2 : ((WildcardType) type).getUpperBounds()) {
                    introspect(bound2);
                }
            }
        }

        private void introspectClass(Class<?> clazz) {
            introspect(clazz.getGenericSuperclass());
            for (Type interfaceType : clazz.getGenericInterfaces()) {
                introspect(interfaceType);
            }
        }

        private void introspectParameterizedType(ParameterizedType parameterizedType) {
            Class<?> rawClass = (Class) parameterizedType.getRawType();
            TypeVariable<?>[] vars = rawClass.getTypeParameters();
            Type[] typeArgs = parameterizedType.getActualTypeArguments();
            Preconditions.checkState(vars.length == typeArgs.length);
            for (int i = 0; i < vars.length; i++) {
                map(vars[i], typeArgs[i]);
            }
            introspectClass(rawClass);
            introspect(parameterizedType.getOwnerType());
        }

        private void map(TypeVariable<?> var, Type arg) {
            if (!this.mappings.containsKey(var)) {
                Type t = arg;
                while (t != null) {
                    if (var.equals(t)) {
                        Type x = arg;
                        while (x != null) {
                            x = (Type) this.mappings.remove(x);
                        }
                        return;
                    }
                    t = (Type) this.mappings.get(t);
                }
                this.mappings.put(var, arg);
            }
        }
    }

    private static final class WildcardCapturer {
        private final AtomicInteger id;

        private WildcardCapturer() {
            this.id = new AtomicInteger();
        }

        Type capture(Type type) {
            Preconditions.checkNotNull(type);
            if ((type instanceof Class) || (type instanceof TypeVariable)) {
                return type;
            }
            if (type instanceof GenericArrayType) {
                return Types.newArrayType(capture(((GenericArrayType) type).getGenericComponentType()));
            }
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                return Types.newParameterizedTypeWithOwner(captureNullable(parameterizedType.getOwnerType()), (Class) parameterizedType.getRawType(), capture(parameterizedType.getActualTypeArguments()));
            } else if (type instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) type;
                if (wildcardType.getLowerBounds().length != 0) {
                    return type;
                }
                return Types.newTypeVariable(WildcardCapturer.class, "capture#" + this.id.incrementAndGet() + "-of ? extends " + Joiner.on('&').join(wildcardType.getUpperBounds()), wildcardType.getUpperBounds());
            } else {
                throw new AssertionError("must have been one of the known types");
            }
        }

        private Type captureNullable(@Nullable Type type) {
            if (type == null) {
                return null;
            }
            return capture(type);
        }

        private Type[] capture(Type[] types) {
            Type[] result = new Type[types.length];
            for (int i = 0; i < types.length; i++) {
                result[i] = capture(types[i]);
            }
            return result;
        }
    }

    public TypeResolver() {
        this.typeTable = ImmutableMap.of();
    }

    private TypeResolver(ImmutableMap<TypeVariable<?>, Type> typeTable) {
        this.typeTable = typeTable;
    }

    static TypeResolver accordingTo(Type type) {
        return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(type));
    }

    public final TypeResolver where(Type formal, Type actual) {
        Map<TypeVariable<?>, Type> mappings = Maps.newHashMap();
        populateTypeMappings(mappings, (Type) Preconditions.checkNotNull(formal), (Type) Preconditions.checkNotNull(actual));
        return where(mappings);
    }

    final TypeResolver where(Map<? extends TypeVariable<?>, ? extends Type> mappings) {
        Builder<TypeVariable<?>, Type> builder = ImmutableMap.builder();
        builder.putAll(this.typeTable);
        for (Entry<? extends TypeVariable<?>, ? extends Type> mapping : mappings.entrySet()) {
            boolean z;
            TypeVariable<?> variable = (TypeVariable) mapping.getKey();
            Type type = (Type) mapping.getValue();
            if (variable.equals(type)) {
                z = false;
            } else {
                z = true;
            }
            Preconditions.checkArgument(z, "Type variable %s bound to itself", variable);
            builder.put(variable, type);
        }
        return new TypeResolver(builder.build());
    }

    private static void populateTypeMappings(Map<TypeVariable<?>, Type> mappings, Type from, Type to) {
        if (!from.equals(to)) {
            if (from instanceof TypeVariable) {
                mappings.put((TypeVariable) from, to);
            } else if (from instanceof GenericArrayType) {
                populateTypeMappings(mappings, ((GenericArrayType) from).getGenericComponentType(), (Type) checkNonNullArgument(Types.getComponentType(to), "%s is not an array type.", to));
            } else if (from instanceof ParameterizedType) {
                ParameterizedType fromParameterizedType = (ParameterizedType) from;
                ParameterizedType toParameterizedType = (ParameterizedType) expectArgument(ParameterizedType.class, to);
                Preconditions.checkArgument(fromParameterizedType.getRawType().equals(toParameterizedType.getRawType()), "Inconsistent raw type: %s vs. %s", from, to);
                Type[] fromArgs = fromParameterizedType.getActualTypeArguments();
                Type[] toArgs = toParameterizedType.getActualTypeArguments();
                Preconditions.checkArgument(fromArgs.length == toArgs.length);
                for (i = 0; i < fromArgs.length; i++) {
                    populateTypeMappings(mappings, fromArgs[i], toArgs[i]);
                }
            } else if (from instanceof WildcardType) {
                WildcardType fromWildcardType = (WildcardType) from;
                WildcardType toWildcardType = (WildcardType) expectArgument(WildcardType.class, to);
                Type[] fromUpperBounds = fromWildcardType.getUpperBounds();
                Type[] toUpperBounds = toWildcardType.getUpperBounds();
                Type[] fromLowerBounds = fromWildcardType.getLowerBounds();
                Type[] toLowerBounds = toWildcardType.getLowerBounds();
                boolean z = fromUpperBounds.length == toUpperBounds.length && fromLowerBounds.length == toLowerBounds.length;
                Preconditions.checkArgument(z, "Incompatible type: %s vs. %s", from, to);
                for (i = 0; i < fromUpperBounds.length; i++) {
                    populateTypeMappings(mappings, fromUpperBounds[i], toUpperBounds[i]);
                }
                for (i = 0; i < fromLowerBounds.length; i++) {
                    populateTypeMappings(mappings, fromLowerBounds[i], toLowerBounds[i]);
                }
            } else {
                throw new IllegalArgumentException("No type mapping from " + from);
            }
        }
    }

    public final Type resolveType(Type type) {
        Preconditions.checkNotNull(type);
        if (type instanceof TypeVariable) {
            return resolveTypeVariable((TypeVariable) type);
        }
        if (type instanceof ParameterizedType) {
            return resolveParameterizedType((ParameterizedType) type);
        }
        if (type instanceof GenericArrayType) {
            return resolveGenericArrayType((GenericArrayType) type);
        }
        if (!(type instanceof WildcardType)) {
            return type;
        }
        WildcardType wildcardType = (WildcardType) type;
        return new WildcardTypeImpl(resolveTypes(wildcardType.getLowerBounds()), resolveTypes(wildcardType.getUpperBounds()));
    }

    private Type[] resolveTypes(Type[] types) {
        Type[] result = new Type[types.length];
        for (int i = 0; i < types.length; i++) {
            result[i] = resolveType(types[i]);
        }
        return result;
    }

    private Type resolveGenericArrayType(GenericArrayType type) {
        return Types.newArrayType(resolveType(type.getGenericComponentType()));
    }

    private Type resolveTypeVariable(final TypeVariable<?> var) {
        final TypeResolver unguarded = this;
        return resolveTypeVariable(var, new TypeResolver(this.typeTable) {
            Type resolveTypeVariable(TypeVariable<?> intermediateVar, TypeResolver guardedResolver) {
                return intermediateVar.getGenericDeclaration().equals(var.getGenericDeclaration()) ? intermediateVar : unguarded.resolveTypeVariable(intermediateVar, guardedResolver);
            }
        });
    }

    Type resolveTypeVariable(TypeVariable<?> var, TypeResolver guardedResolver) {
        Preconditions.checkNotNull(guardedResolver);
        Type type = (Type) this.typeTable.get(var);
        if (type != null) {
            return guardedResolver.resolveType(type);
        }
        Type[] bounds = var.getBounds();
        if (bounds.length == 0) {
            return var;
        }
        return Types.newTypeVariable(var.getGenericDeclaration(), var.getName(), guardedResolver.resolveTypes(bounds));
    }

    private ParameterizedType resolveParameterizedType(ParameterizedType type) {
        Type owner = type.getOwnerType();
        Type resolvedOwner = owner == null ? null : resolveType(owner);
        Type resolvedRawType = resolveType(type.getRawType());
        Type[] vars = type.getActualTypeArguments();
        Type[] resolvedArgs = new Type[vars.length];
        for (int i = 0; i < vars.length; i++) {
            resolvedArgs[i] = resolveType(vars[i]);
        }
        return Types.newParameterizedTypeWithOwner(resolvedOwner, (Class) resolvedRawType, resolvedArgs);
    }

    private static <T> T checkNonNullArgument(T arg, String format, Object... messageParams) {
        Preconditions.checkArgument(arg != null, format, messageParams);
        return arg;
    }

    private static <T> T expectArgument(Class<T> type, Object arg) {
        try {
            return type.cast(arg);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(arg + " is not a " + type.getSimpleName());
        }
    }
}
