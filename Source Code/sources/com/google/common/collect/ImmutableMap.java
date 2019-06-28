package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {
    private transient ImmutableSet<Entry<K, V>> entrySet;
    private transient ImmutableSet<K> keySet;
    private transient ImmutableSetMultimap<K, V> multimapView;
    private transient ImmutableCollection<V> values;

    public static class Builder<K, V> {
        final ArrayList<Entry<K, V>> entries = Lists.newArrayList();

        public Builder<K, V> put(K key, V value) {
            this.entries.add(ImmutableMap.entryOf(key, value));
            return this;
        }

        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            K key = entry.getKey();
            V value = entry.getValue();
            if (entry instanceof ImmutableEntry) {
                Preconditions.checkNotNull(key);
                Preconditions.checkNotNull(value);
                this.entries.add(entry);
            } else {
                this.entries.add(ImmutableMap.entryOf(key, value));
            }
            return this;
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            this.entries.ensureCapacity(this.entries.size() + map.size());
            for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public ImmutableMap<K, V> build() {
            return fromEntryList(this.entries);
        }

        private static <K, V> ImmutableMap<K, V> fromEntryList(List<Entry<K, V>> entries) {
            switch (entries.size()) {
                case 0:
                    return ImmutableMap.of();
                case 1:
                    return new SingletonImmutableBiMap((Entry) Iterables.getOnlyElement(entries));
                default:
                    return new RegularImmutableMap((Entry[]) entries.toArray(new Entry[entries.size()]));
            }
        }
    }

    static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        private final Object[] keys;
        private final Object[] values;

        SerializedForm(ImmutableMap<?, ?> map) {
            this.keys = new Object[map.size()];
            this.values = new Object[map.size()];
            int i = 0;
            Iterator i$ = map.entrySet().iterator();
            while (i$.hasNext()) {
                Entry<?, ?> entry = (Entry) i$.next();
                this.keys[i] = entry.getKey();
                this.values[i] = entry.getValue();
                i++;
            }
        }

        Object readResolve() {
            return createMap(new Builder());
        }

        Object createMap(Builder<Object, Object> builder) {
            for (int i = 0; i < this.keys.length; i++) {
                builder.put(this.keys[i], this.values[i]);
            }
            return builder.build();
        }
    }

    abstract ImmutableSet<Entry<K, V>> createEntrySet();

    public abstract V get(@Nullable Object obj);

    abstract boolean isPartialView();

    public static <K, V> ImmutableMap<K, V> of() {
        return ImmutableBiMap.of();
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1) {
        return ImmutableBiMap.of(k1, v1);
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) {
        return new RegularImmutableMap(entryOf(k1, v1), entryOf(k2, v2));
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return new RegularImmutableMap(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3));
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return new RegularImmutableMap(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4));
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return new RegularImmutableMap(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5));
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    static <K, V> Entry<K, V> entryOf(K key, V value) {
        Preconditions.checkNotNull(key, "null key in entry: null=%s", value);
        Preconditions.checkNotNull(value, "null value in entry: %s=null", key);
        return Maps.immutableEntry(key, value);
    }

    public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        if ((map instanceof ImmutableMap) && !(map instanceof ImmutableSortedMap)) {
            ImmutableMap<K, V> kvMap = (ImmutableMap) map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        } else if (map instanceof EnumMap) {
            EnumMap<?, ?> enumMap = (EnumMap) map;
            for (Entry<?, ?> entry : enumMap.entrySet()) {
                Preconditions.checkNotNull(entry.getKey());
                Preconditions.checkNotNull(entry.getValue());
            }
            return ImmutableEnumMap.asImmutable(new EnumMap(enumMap));
        }
        Entry[] entries = (Entry[]) map.entrySet().toArray(new Entry[0]);
        switch (entries.length) {
            case 0:
                return of();
            case 1:
                return new SingletonImmutableBiMap(entryOf(entries[0].getKey(), entries[0].getValue()));
            default:
                for (int i = 0; i < entries.length; i++) {
                    entries[i] = entryOf(entries[i].getKey(), entries[i].getValue());
                }
                return new RegularImmutableMap(entries);
        }
    }

    ImmutableMap() {
    }

    @Deprecated
    public final V put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final V remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(@Nullable Object key) {
        return get(key) != null;
    }

    public boolean containsValue(@Nullable Object value) {
        return value != null && Maps.containsValueImpl(this, value);
    }

    public ImmutableSet<Entry<K, V>> entrySet() {
        ImmutableSet<Entry<K, V>> immutableSet = this.entrySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        immutableSet = createEntrySet();
        this.entrySet = immutableSet;
        return immutableSet;
    }

    public ImmutableSet<K> keySet() {
        ImmutableSet<K> immutableSet = this.keySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        immutableSet = createKeySet();
        this.keySet = immutableSet;
        return immutableSet;
    }

    ImmutableSet<K> createKeySet() {
        return new ImmutableMapKeySet(this);
    }

    public ImmutableCollection<V> values() {
        ImmutableCollection<V> immutableCollection = this.values;
        if (immutableCollection != null) {
            return immutableCollection;
        }
        immutableCollection = new ImmutableMapValues(this);
        this.values = immutableCollection;
        return immutableCollection;
    }

    @Beta
    public ImmutableSetMultimap<K, V> asMultimap() {
        ImmutableSetMultimap<K, V> immutableSetMultimap = this.multimapView;
        if (immutableSetMultimap != null) {
            return immutableSetMultimap;
        }
        immutableSetMultimap = createMultimapView();
        this.multimapView = immutableSetMultimap;
        return immutableSetMultimap;
    }

    private ImmutableSetMultimap<K, V> createMultimapView() {
        ImmutableMap<K, ImmutableSet<V>> map = viewMapValuesAsSingletonSets();
        return new ImmutableSetMultimap(map, map.size(), null);
    }

    private ImmutableMap<K, ImmutableSet<V>> viewMapValuesAsSingletonSets() {
        return new ImmutableMap<K, ImmutableSet<V>>() {

            /* renamed from: com.google.common.collect.ImmutableMap$1MapViewOfValuesAsSingletonSets$1 */
            class C06611 extends ImmutableMapEntrySet<K, ImmutableSet<V>> {
                C06611() {
                }

                ImmutableMap<K, ImmutableSet<V>> map() {
                    return AnonymousClass1MapViewOfValuesAsSingletonSets.this;
                }

                public UnmodifiableIterator<Entry<K, ImmutableSet<V>>> iterator() {
                    final Iterator<Entry<K, V>> backingIterator = ImmutableMap.this.entrySet().iterator();
                    return new UnmodifiableIterator<Entry<K, ImmutableSet<V>>>() {
                        public boolean hasNext() {
                            return backingIterator.hasNext();
                        }

                        public Entry<K, ImmutableSet<V>> next() {
                            final Entry<K, V> backingEntry = (Entry) backingIterator.next();
                            return new AbstractMapEntry<K, ImmutableSet<V>>() {
                                public K getKey() {
                                    return backingEntry.getKey();
                                }

                                public ImmutableSet<V> getValue() {
                                    return ImmutableSet.of(backingEntry.getValue());
                                }
                            };
                        }
                    };
                }
            }

            public /* bridge */ /* synthetic */ Set entrySet() {
                return super.entrySet();
            }

            public /* bridge */ /* synthetic */ Set keySet() {
                return super.keySet();
            }

            public /* bridge */ /* synthetic */ Collection values() {
                return super.values();
            }

            public int size() {
                return ImmutableMap.this.size();
            }

            public boolean containsKey(@Nullable Object key) {
                return ImmutableMap.this.containsKey(key);
            }

            public ImmutableSet<V> get(@Nullable Object key) {
                V outerValue = ImmutableMap.this.get(key);
                return outerValue == null ? null : ImmutableSet.of(outerValue);
            }

            boolean isPartialView() {
                return false;
            }

            ImmutableSet<Entry<K, ImmutableSet<V>>> createEntrySet() {
                return new C06611();
            }
        };
    }

    public boolean equals(@Nullable Object object) {
        return Maps.equalsImpl(this, object);
    }

    public int hashCode() {
        return entrySet().hashCode();
    }

    public String toString() {
        return Maps.toStringImpl(this);
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
