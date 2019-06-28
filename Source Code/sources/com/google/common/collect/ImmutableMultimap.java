package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public abstract class ImmutableMultimap<K, V> extends AbstractMultimap<K, V> implements Serializable {
    private static final long serialVersionUID = 0;
    final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
    final transient int size;

    public static class Builder<K, V> {
        Multimap<K, V> builderMultimap = new BuilderMultimap();
        Comparator<? super K> keyComparator;
        Comparator<? super V> valueComparator;

        /* renamed from: com.google.common.collect.ImmutableMultimap$Builder$1 */
        class C03681 implements Function<Entry<K, Collection<V>>, K> {
            C03681() {
            }

            public K apply(Entry<K, Collection<V>> entry) {
                return entry.getKey();
            }
        }

        public Builder<K, V> put(K key, V value) {
            this.builderMultimap.put(Preconditions.checkNotNull(key), Preconditions.checkNotNull(value));
            return this;
        }

        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            this.builderMultimap.put(Preconditions.checkNotNull(entry.getKey()), Preconditions.checkNotNull(entry.getValue()));
            return this;
        }

        public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
            Collection<V> valueList = this.builderMultimap.get(Preconditions.checkNotNull(key));
            for (V value : values) {
                valueList.add(Preconditions.checkNotNull(value));
            }
            return this;
        }

        public Builder<K, V> putAll(K key, V... values) {
            return putAll((Object) key, Arrays.asList(values));
        }

        public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
            for (Entry<? extends K, ? extends Collection<? extends V>> entry : multimap.asMap().entrySet()) {
                putAll(entry.getKey(), (Iterable) entry.getValue());
            }
            return this;
        }

        public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
            this.keyComparator = (Comparator) Preconditions.checkNotNull(keyComparator);
            return this;
        }

        public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
            this.valueComparator = (Comparator) Preconditions.checkNotNull(valueComparator);
            return this;
        }

        public ImmutableMultimap<K, V> build() {
            if (this.valueComparator != null) {
                for (Collection<V> values : this.builderMultimap.asMap().values()) {
                    Collections.sort((List) values, this.valueComparator);
                }
            }
            if (this.keyComparator != null) {
                Multimap<K, V> sortedCopy = new BuilderMultimap();
                List<Entry<K, Collection<V>>> entries = Lists.newArrayList(this.builderMultimap.asMap().entrySet());
                Collections.sort(entries, Ordering.from(this.keyComparator).onResultOf(new C03681()));
                for (Entry<K, Collection<V>> entry : entries) {
                    sortedCopy.putAll(entry.getKey(), (Iterable) entry.getValue());
                }
                this.builderMultimap = sortedCopy;
            }
            return ImmutableMultimap.copyOf(this.builderMultimap);
        }
    }

    @GwtIncompatible("java serialization is not supported")
    static class FieldSettersHolder {
        static final FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
        static final FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");

        FieldSettersHolder() {
        }
    }

    private static class EntryCollection<K, V> extends ImmutableCollection<Entry<K, V>> {
        private static final long serialVersionUID = 0;
        final ImmutableMultimap<K, V> multimap;

        EntryCollection(ImmutableMultimap<K, V> multimap) {
            this.multimap = multimap;
        }

        public UnmodifiableIterator<Entry<K, V>> iterator() {
            return this.multimap.entryIterator();
        }

        boolean isPartialView() {
            return this.multimap.isPartialView();
        }

        public int size() {
            return this.multimap.size();
        }

        public boolean contains(Object object) {
            if (!(object instanceof Entry)) {
                return false;
            }
            Entry<?, ?> entry = (Entry) object;
            return this.multimap.containsEntry(entry.getKey(), entry.getValue());
        }
    }

    private static class Values<V> extends ImmutableCollection<V> {
        private static final long serialVersionUID = 0;
        final ImmutableMultimap<?, V> multimap;

        Values(ImmutableMultimap<?, V> multimap) {
            this.multimap = multimap;
        }

        public UnmodifiableIterator<V> iterator() {
            return Maps.valueIterator(this.multimap.entries().iterator());
        }

        public int size() {
            return this.multimap.size();
        }

        boolean isPartialView() {
            return true;
        }
    }

    class Keys extends ImmutableMultiset<K> {

        private class KeysEntrySet extends EntrySet {
            private KeysEntrySet() {
                super();
            }

            public int size() {
                return ImmutableMultimap.this.keySet().size();
            }

            public UnmodifiableIterator<Multiset.Entry<K>> iterator() {
                return asList().iterator();
            }

            ImmutableList<Multiset.Entry<K>> createAsList() {
                final ImmutableList<? extends Entry<K, ? extends Collection<V>>> mapEntries = ImmutableMultimap.this.map.entrySet().asList();
                return new ImmutableAsList<Multiset.Entry<K>>() {
                    public Multiset.Entry<K> get(int index) {
                        Entry<K, ? extends Collection<V>> entry = (Entry) mapEntries.get(index);
                        return Multisets.immutableEntry(entry.getKey(), ((Collection) entry.getValue()).size());
                    }

                    ImmutableCollection<Multiset.Entry<K>> delegateCollection() {
                        return KeysEntrySet.this;
                    }
                };
            }
        }

        Keys() {
        }

        public boolean contains(@Nullable Object object) {
            return ImmutableMultimap.this.containsKey(object);
        }

        public int count(@Nullable Object element) {
            Collection<V> values = (Collection) ImmutableMultimap.this.map.get(element);
            return values == null ? 0 : values.size();
        }

        public Set<K> elementSet() {
            return ImmutableMultimap.this.keySet();
        }

        public int size() {
            return ImmutableMultimap.this.size();
        }

        ImmutableSet<Multiset.Entry<K>> createEntrySet() {
            return new KeysEntrySet();
        }

        boolean isPartialView() {
            return true;
        }
    }

    private static class BuilderMultimap<K, V> extends AbstractMapBasedMultimap<K, V> {
        private static final long serialVersionUID = 0;

        BuilderMultimap() {
            super(new LinkedHashMap());
        }

        Collection<V> createCollection() {
            return Lists.newArrayList();
        }
    }

    public abstract ImmutableCollection<V> get(K k);

    public abstract ImmutableMultimap<V, K> inverse();

    public /* bridge */ /* synthetic */ boolean containsEntry(Object x0, Object x1) {
        return super.containsEntry(x0, x1);
    }

    public /* bridge */ /* synthetic */ boolean containsValue(Object x0) {
        return super.containsValue(x0);
    }

    public /* bridge */ /* synthetic */ boolean equals(Object x0) {
        return super.equals(x0);
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public static <K, V> ImmutableMultimap<K, V> of() {
        return ImmutableListMultimap.of();
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1) {
        return ImmutableListMultimap.of(k1, v1);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2) {
        return ImmutableListMultimap.of(k1, v1, k2, v2);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
        if (multimap instanceof ImmutableMultimap) {
            ImmutableMultimap<K, V> kvMultimap = (ImmutableMultimap) multimap;
            if (!kvMultimap.isPartialView()) {
                return kvMultimap;
            }
        }
        return ImmutableListMultimap.copyOf(multimap);
    }

    ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> map, int size) {
        this.map = map;
        this.size = size;
    }

    @Deprecated
    public ImmutableCollection<V> removeAll(Object key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public ImmutableCollection<V> replaceValues(K k, Iterable<? extends V> iterable) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public boolean put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public boolean putAll(K k, Iterable<? extends V> iterable) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    boolean isPartialView() {
        return this.map.isPartialView();
    }

    public boolean containsKey(@Nullable Object key) {
        return this.map.containsKey(key);
    }

    public int size() {
        return this.size;
    }

    public ImmutableSet<K> keySet() {
        return this.map.keySet();
    }

    public ImmutableMap<K, Collection<V>> asMap() {
        return this.map;
    }

    Map<K, Collection<V>> createAsMap() {
        throw new AssertionError("should never be called");
    }

    public ImmutableCollection<Entry<K, V>> entries() {
        return (ImmutableCollection) super.entries();
    }

    ImmutableCollection<Entry<K, V>> createEntries() {
        return new EntryCollection(this);
    }

    UnmodifiableIterator<Entry<K, V>> entryIterator() {
        final Iterator<? extends Entry<K, ? extends ImmutableCollection<V>>> mapIterator = this.map.entrySet().iterator();
        return new UnmodifiableIterator<Entry<K, V>>() {
            K key;
            Iterator<V> valueIterator;

            public boolean hasNext() {
                return (this.key != null && this.valueIterator.hasNext()) || mapIterator.hasNext();
            }

            public Entry<K, V> next() {
                if (this.key == null || !this.valueIterator.hasNext()) {
                    Entry<K, ? extends ImmutableCollection<V>> entry = (Entry) mapIterator.next();
                    this.key = entry.getKey();
                    this.valueIterator = ((ImmutableCollection) entry.getValue()).iterator();
                }
                return Maps.immutableEntry(this.key, this.valueIterator.next());
            }
        };
    }

    public ImmutableMultiset<K> keys() {
        return (ImmutableMultiset) super.keys();
    }

    ImmutableMultiset<K> createKeys() {
        return new Keys();
    }

    public ImmutableCollection<V> values() {
        return (ImmutableCollection) super.values();
    }

    ImmutableCollection<V> createValues() {
        return new Values(this);
    }
}
