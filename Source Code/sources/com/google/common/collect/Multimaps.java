package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableListMultimap.Builder;
import com.google.common.collect.Maps.EntryTransformer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Multimaps {

    static abstract class Entries<K, V> extends AbstractCollection<Entry<K, V>> {
        abstract Multimap<K, V> multimap();

        Entries() {
        }

        public int size() {
            return multimap().size();
        }

        public boolean contains(@Nullable Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry<?, ?> entry = (Entry) o;
            return multimap().containsEntry(entry.getKey(), entry.getValue());
        }

        public boolean remove(@Nullable Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry<?, ?> entry = (Entry) o;
            return multimap().remove(entry.getKey(), entry.getValue());
        }

        public void clear() {
            multimap().clear();
        }
    }

    static class Values<K, V> extends AbstractCollection<V> {
        final Multimap<K, V> multimap;

        Values(Multimap<K, V> multimap) {
            this.multimap = multimap;
        }

        public Iterator<V> iterator() {
            return Maps.valueIterator(this.multimap.entries().iterator());
        }

        public int size() {
            return this.multimap.size();
        }

        public boolean contains(@Nullable Object o) {
            return this.multimap.containsValue(o);
        }

        public void clear() {
            this.multimap.clear();
        }
    }

    static abstract class AsMap<K, V> extends ImprovedAbstractMap<K, Collection<V>> {

        class EntrySet extends EntrySet<K, Collection<V>> {
            EntrySet() {
            }

            Map<K, Collection<V>> map() {
                return AsMap.this;
            }

            public Iterator<Entry<K, Collection<V>>> iterator() {
                return AsMap.this.entryIterator();
            }

            public boolean remove(Object o) {
                if (!contains(o)) {
                    return false;
                }
                AsMap.this.removeValuesForKey(((Entry) o).getKey());
                return true;
            }
        }

        abstract Iterator<Entry<K, Collection<V>>> entryIterator();

        abstract Multimap<K, V> multimap();

        public abstract int size();

        AsMap() {
        }

        protected Set<Entry<K, Collection<V>>> createEntrySet() {
            return new EntrySet();
        }

        void removeValuesForKey(Object key) {
            multimap().removeAll(key);
        }

        public Collection<V> get(Object key) {
            return containsKey(key) ? multimap().get(key) : null;
        }

        public Collection<V> remove(Object key) {
            return containsKey(key) ? multimap().removeAll(key) : null;
        }

        public Set<K> keySet() {
            return multimap().keySet();
        }

        public boolean isEmpty() {
            return multimap().isEmpty();
        }

        public boolean containsKey(Object key) {
            return multimap().containsKey(key);
        }

        public void clear() {
            multimap().clear();
        }
    }

    static abstract class EntrySet<K, V> extends Entries<K, V> implements Set<Entry<K, V>> {
        EntrySet() {
        }

        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }

        public boolean equals(@Nullable Object obj) {
            return Sets.equalsImpl(this, obj);
        }
    }

    static final class ValueFunction<K, V1, V2> implements Function<V1, V2> {
        private final K key;
        private final EntryTransformer<? super K, ? super V1, V2> transformer;

        ValueFunction(K key, EntryTransformer<? super K, ? super V1, V2> transformer) {
            this.key = key;
            this.transformer = transformer;
        }

        public V2 apply(@Nullable V1 value) {
            return this.transformer.transformEntry(this.key, value);
        }
    }

    static class Keys<K, V> extends AbstractMultiset<K> {
        final Multimap<K, V> multimap;

        class KeysEntrySet extends EntrySet<K> {
            KeysEntrySet() {
            }

            Multiset<K> multiset() {
                return Keys.this;
            }

            public Iterator<Multiset.Entry<K>> iterator() {
                return Keys.this.entryIterator();
            }

            public int size() {
                return Keys.this.distinctElements();
            }

            public boolean isEmpty() {
                return Keys.this.multimap.isEmpty();
            }

            public boolean contains(@Nullable Object o) {
                if (!(o instanceof Multiset.Entry)) {
                    return false;
                }
                Multiset.Entry<?> entry = (Multiset.Entry) o;
                Collection<V> collection = (Collection) Keys.this.multimap.asMap().get(entry.getElement());
                if (collection == null || collection.size() != entry.getCount()) {
                    return false;
                }
                return true;
            }

            public boolean remove(@Nullable Object o) {
                if (o instanceof Multiset.Entry) {
                    Multiset.Entry<?> entry = (Multiset.Entry) o;
                    Collection<V> collection = (Collection) Keys.this.multimap.asMap().get(entry.getElement());
                    if (collection != null && collection.size() == entry.getCount()) {
                        collection.clear();
                        return true;
                    }
                }
                return false;
            }
        }

        Keys(Multimap<K, V> multimap) {
            this.multimap = multimap;
        }

        Iterator<Multiset.Entry<K>> entryIterator() {
            return new TransformedIterator<Entry<K, Collection<V>>, Multiset.Entry<K>>(this.multimap.asMap().entrySet().iterator()) {
                Multiset.Entry<K> transform(final Entry<K, Collection<V>> backingEntry) {
                    return new AbstractEntry<K>() {
                        public K getElement() {
                            return backingEntry.getKey();
                        }

                        public int getCount() {
                            return ((Collection) backingEntry.getValue()).size();
                        }
                    };
                }
            };
        }

        int distinctElements() {
            return this.multimap.asMap().size();
        }

        Set<Multiset.Entry<K>> createEntrySet() {
            return new KeysEntrySet();
        }

        public boolean contains(@Nullable Object element) {
            return this.multimap.containsKey(element);
        }

        public Iterator<K> iterator() {
            return Maps.keyIterator(this.multimap.entries().iterator());
        }

        public int count(@Nullable Object element) {
            Collection<V> values = (Collection) Maps.safeGet(this.multimap.asMap(), element);
            return values == null ? 0 : values.size();
        }

        public int remove(@Nullable Object element, int occurrences) {
            Preconditions.checkArgument(occurrences >= 0);
            if (occurrences == 0) {
                return count(element);
            }
            Collection<V> values = (Collection) Maps.safeGet(this.multimap.asMap(), element);
            if (values == null) {
                return 0;
            }
            int oldCount = values.size();
            if (occurrences >= oldCount) {
                values.clear();
            } else {
                Iterator<V> iterator = values.iterator();
                for (int i = 0; i < occurrences; i++) {
                    iterator.next();
                    iterator.remove();
                }
            }
            return oldCount;
        }

        public void clear() {
            this.multimap.clear();
        }

        public Set<K> elementSet() {
            return this.multimap.keySet();
        }
    }

    private static class MapMultimap<K, V> implements SetMultimap<K, V>, Serializable {
        private static final MapJoiner JOINER = Joiner.on("], ").withKeyValueSeparator("=[").useForNull("null");
        private static final long serialVersionUID = 7845222491160860175L;
        transient Map<K, Collection<V>> asMap;
        final Map<K, V> map;

        class AsMap extends ImprovedAbstractMap<K, Collection<V>> {
            AsMap() {
            }

            protected Set<Entry<K, Collection<V>>> createEntrySet() {
                return new AsMapEntries();
            }

            public boolean containsKey(Object key) {
                return MapMultimap.this.map.containsKey(key);
            }

            public Collection<V> get(Object key) {
                Collection<V> collection = MapMultimap.this.get(key);
                return collection.isEmpty() ? null : collection;
            }

            public Collection<V> remove(Object key) {
                Collection<V> collection = MapMultimap.this.removeAll(key);
                return collection.isEmpty() ? null : collection;
            }
        }

        class AsMapEntries extends ImprovedAbstractSet<Entry<K, Collection<V>>> {
            AsMapEntries() {
            }

            public int size() {
                return MapMultimap.this.map.size();
            }

            public Iterator<Entry<K, Collection<V>>> iterator() {
                return new TransformedIterator<K, Entry<K, Collection<V>>>(MapMultimap.this.map.keySet().iterator()) {
                    Entry<K, Collection<V>> transform(final K key) {
                        return new AbstractMapEntry<K, Collection<V>>() {
                            public K getKey() {
                                return key;
                            }

                            public Collection<V> getValue() {
                                return MapMultimap.this.get(key);
                            }
                        };
                    }
                };
            }

            public boolean contains(Object o) {
                boolean z = true;
                if (!(o instanceof Entry)) {
                    return false;
                }
                Entry<?, ?> entry = (Entry) o;
                if (!(entry.getValue() instanceof Set)) {
                    return false;
                }
                Set<?> set = (Set) entry.getValue();
                if (!(set.size() == 1 && MapMultimap.this.containsEntry(entry.getKey(), set.iterator().next()))) {
                    z = false;
                }
                return z;
            }

            public boolean remove(Object o) {
                boolean z = true;
                if (!(o instanceof Entry)) {
                    return false;
                }
                Entry<?, ?> entry = (Entry) o;
                if (!(entry.getValue() instanceof Set)) {
                    return false;
                }
                Set<?> set = (Set) entry.getValue();
                if (!(set.size() == 1 && MapMultimap.this.map.entrySet().remove(Maps.immutableEntry(entry.getKey(), set.iterator().next())))) {
                    z = false;
                }
                return z;
            }
        }

        MapMultimap(Map<K, V> map) {
            this.map = (Map) Preconditions.checkNotNull(map);
        }

        public int size() {
            return this.map.size();
        }

        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        public boolean containsKey(Object key) {
            return this.map.containsKey(key);
        }

        public boolean containsValue(Object value) {
            return this.map.containsValue(value);
        }

        public boolean containsEntry(Object key, Object value) {
            return this.map.entrySet().contains(Maps.immutableEntry(key, value));
        }

        public Set<V> get(final K key) {
            return new ImprovedAbstractSet<V>() {

                /* renamed from: com.google.common.collect.Multimaps$MapMultimap$1$1 */
                class C02041 implements Iterator<V> {
                    /* renamed from: i */
                    int f2i;

                    C02041() {
                    }

                    public boolean hasNext() {
                        return this.f2i == 0 && MapMultimap.this.map.containsKey(key);
                    }

                    public V next() {
                        if (hasNext()) {
                            this.f2i++;
                            return MapMultimap.this.map.get(key);
                        }
                        throw new NoSuchElementException();
                    }

                    public void remove() {
                        boolean z = true;
                        if (this.f2i != 1) {
                            z = false;
                        }
                        Preconditions.checkState(z);
                        this.f2i = -1;
                        MapMultimap.this.map.remove(key);
                    }
                }

                public Iterator<V> iterator() {
                    return new C02041();
                }

                public int size() {
                    return MapMultimap.this.map.containsKey(key) ? 1 : 0;
                }
            };
        }

        public boolean put(K k, V v) {
            throw new UnsupportedOperationException();
        }

        public boolean putAll(K k, Iterable<? extends V> iterable) {
            throw new UnsupportedOperationException();
        }

        public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
            throw new UnsupportedOperationException();
        }

        public Set<V> replaceValues(K k, Iterable<? extends V> iterable) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object key, Object value) {
            return this.map.entrySet().remove(Maps.immutableEntry(key, value));
        }

        public Set<V> removeAll(Object key) {
            Set<V> values = new HashSet(2);
            if (this.map.containsKey(key)) {
                values.add(this.map.remove(key));
            }
            return values;
        }

        public void clear() {
            this.map.clear();
        }

        public Set<K> keySet() {
            return this.map.keySet();
        }

        public Multiset<K> keys() {
            return new Keys(this);
        }

        public Collection<V> values() {
            return this.map.values();
        }

        public Set<Entry<K, V>> entries() {
            return this.map.entrySet();
        }

        public Map<K, Collection<V>> asMap() {
            Map<K, Collection<V>> result = this.asMap;
            if (result != null) {
                return result;
            }
            result = new AsMap();
            this.asMap = result;
            return result;
        }

        public boolean equals(@Nullable Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof Multimap)) {
                return false;
            }
            Multimap<?, ?> that = (Multimap) object;
            if (size() == that.size() && asMap().equals(that.asMap())) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.map.hashCode();
        }

        public String toString() {
            if (this.map.isEmpty()) {
                return "{}";
            }
            StringBuilder builder = Collections2.newStringBuilderForCollection(this.map.size()).append('{');
            JOINER.appendTo(builder, this.map);
            return builder.append("]}").toString();
        }
    }

    private static class TransformedEntriesMultimap<K, V1, V2> extends AbstractMultimap<K, V2> {
        final Multimap<K, V1> fromMultimap;
        final EntryTransformer<? super K, ? super V1, V2> transformer;

        /* renamed from: com.google.common.collect.Multimaps$TransformedEntriesMultimap$1 */
        class C04331 implements EntryTransformer<K, Collection<V1>, Collection<V2>> {
            C04331() {
            }

            public Collection<V2> transformEntry(K key, Collection<V1> value) {
                return TransformedEntriesMultimap.this.transform(key, value);
            }
        }

        /* renamed from: com.google.common.collect.Multimaps$TransformedEntriesMultimap$2 */
        class C04352 implements Function<Entry<K, V1>, Entry<K, V2>> {
            C04352() {
            }

            public Entry<K, V2> apply(final Entry<K, V1> entry) {
                return new AbstractMapEntry<K, V2>() {
                    public K getKey() {
                        return entry.getKey();
                    }

                    public V2 getValue() {
                        return TransformedEntriesMultimap.this.transformer.transformEntry(entry.getKey(), entry.getValue());
                    }
                };
            }
        }

        /* renamed from: com.google.common.collect.Multimaps$TransformedEntriesMultimap$3 */
        class C04363 implements Function<Entry<K, V1>, V2> {
            C04363() {
            }

            public V2 apply(Entry<K, V1> entry) {
                return TransformedEntriesMultimap.this.transformer.transformEntry(entry.getKey(), entry.getValue());
            }
        }

        TransformedEntriesMultimap(Multimap<K, V1> fromMultimap, EntryTransformer<? super K, ? super V1, V2> transformer) {
            this.fromMultimap = (Multimap) Preconditions.checkNotNull(fromMultimap);
            this.transformer = (EntryTransformer) Preconditions.checkNotNull(transformer);
        }

        Collection<V2> transform(K key, Collection<V1> values) {
            Function<V1, V2> function = new ValueFunction(key, this.transformer);
            if (values instanceof List) {
                return Lists.transform((List) values, function);
            }
            return Collections2.transform(values, function);
        }

        Map<K, Collection<V2>> createAsMap() {
            return Maps.transformEntries(this.fromMultimap.asMap(), new C04331());
        }

        public void clear() {
            this.fromMultimap.clear();
        }

        public boolean containsEntry(Object key, Object value) {
            return get(key).contains(value);
        }

        public boolean containsKey(Object key) {
            return this.fromMultimap.containsKey(key);
        }

        public boolean containsValue(Object value) {
            return values().contains(value);
        }

        Iterator<Entry<K, V2>> entryIterator() {
            return Iterators.transform(this.fromMultimap.entries().iterator(), new C04352());
        }

        public Collection<V2> get(K key) {
            return transform(key, this.fromMultimap.get(key));
        }

        public boolean isEmpty() {
            return this.fromMultimap.isEmpty();
        }

        public Set<K> keySet() {
            return this.fromMultimap.keySet();
        }

        public Multiset<K> keys() {
            return this.fromMultimap.keys();
        }

        public boolean put(K k, V2 v2) {
            throw new UnsupportedOperationException();
        }

        public boolean putAll(K k, Iterable<? extends V2> iterable) {
            throw new UnsupportedOperationException();
        }

        public boolean putAll(Multimap<? extends K, ? extends V2> multimap) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object key, Object value) {
            return get(key).remove(value);
        }

        public Collection<V2> removeAll(Object key) {
            return transform(key, this.fromMultimap.removeAll(key));
        }

        public Collection<V2> replaceValues(K k, Iterable<? extends V2> iterable) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return this.fromMultimap.size();
        }

        Collection<V2> createValues() {
            return Collections2.transform(this.fromMultimap.entries(), new C04363());
        }
    }

    private static class UnmodifiableAsMapValues<V> extends ForwardingCollection<Collection<V>> {
        final Collection<Collection<V>> delegate;

        UnmodifiableAsMapValues(Collection<Collection<V>> delegate) {
            this.delegate = Collections.unmodifiableCollection(delegate);
        }

        protected Collection<Collection<V>> delegate() {
            return this.delegate;
        }

        public Iterator<Collection<V>> iterator() {
            final Iterator<Collection<V>> iterator = this.delegate.iterator();
            return new UnmodifiableIterator<Collection<V>>() {
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                public Collection<V> next() {
                    return Multimaps.unmodifiableValueCollection((Collection) iterator.next());
                }
            };
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] array) {
            return standardToArray(array);
        }

        public boolean contains(Object o) {
            return standardContains(o);
        }

        public boolean containsAll(Collection<?> c) {
            return standardContainsAll(c);
        }
    }

    private static class UnmodifiableMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable {
        private static final long serialVersionUID = 0;
        final Multimap<K, V> delegate;
        transient Collection<Entry<K, V>> entries;
        transient Set<K> keySet;
        transient Multiset<K> keys;
        transient Map<K, Collection<V>> map;
        transient Collection<V> values;

        UnmodifiableMultimap(Multimap<K, V> delegate) {
            this.delegate = (Multimap) Preconditions.checkNotNull(delegate);
        }

        protected Multimap<K, V> delegate() {
            return this.delegate;
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public Map<K, Collection<V>> asMap() {
            Map<K, Collection<V>> result = this.map;
            if (result != null) {
                return result;
            }
            final Map<K, Collection<V>> unmodifiableMap = Collections.unmodifiableMap(this.delegate.asMap());
            result = new ForwardingMap<K, Collection<V>>() {
                Collection<Collection<V>> asMapValues;
                Set<Entry<K, Collection<V>>> entrySet;

                protected Map<K, Collection<V>> delegate() {
                    return unmodifiableMap;
                }

                public Set<Entry<K, Collection<V>>> entrySet() {
                    Set<Entry<K, Collection<V>>> set = this.entrySet;
                    if (set != null) {
                        return set;
                    }
                    set = Multimaps.unmodifiableAsMapEntries(unmodifiableMap.entrySet());
                    this.entrySet = set;
                    return set;
                }

                public Collection<V> get(Object key) {
                    Collection<V> collection = (Collection) unmodifiableMap.get(key);
                    return collection == null ? null : Multimaps.unmodifiableValueCollection(collection);
                }

                public Collection<Collection<V>> values() {
                    Collection<Collection<V>> collection = this.asMapValues;
                    if (collection != null) {
                        return collection;
                    }
                    collection = new UnmodifiableAsMapValues(unmodifiableMap.values());
                    this.asMapValues = collection;
                    return collection;
                }

                public boolean containsValue(Object o) {
                    return values().contains(o);
                }
            };
            this.map = result;
            return result;
        }

        public Collection<Entry<K, V>> entries() {
            Collection<Entry<K, V>> result = this.entries;
            if (result != null) {
                return result;
            }
            result = Multimaps.unmodifiableEntries(this.delegate.entries());
            this.entries = result;
            return result;
        }

        public Collection<V> get(K key) {
            return Multimaps.unmodifiableValueCollection(this.delegate.get(key));
        }

        public Multiset<K> keys() {
            Multiset<K> result = this.keys;
            if (result != null) {
                return result;
            }
            result = Multisets.unmodifiableMultiset(this.delegate.keys());
            this.keys = result;
            return result;
        }

        public Set<K> keySet() {
            Set<K> result = this.keySet;
            if (result != null) {
                return result;
            }
            result = Collections.unmodifiableSet(this.delegate.keySet());
            this.keySet = result;
            return result;
        }

        public boolean put(K k, V v) {
            throw new UnsupportedOperationException();
        }

        public boolean putAll(K k, Iterable<? extends V> iterable) {
            throw new UnsupportedOperationException();
        }

        public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object key, Object value) {
            throw new UnsupportedOperationException();
        }

        public Collection<V> removeAll(Object key) {
            throw new UnsupportedOperationException();
        }

        public Collection<V> replaceValues(K k, Iterable<? extends V> iterable) {
            throw new UnsupportedOperationException();
        }

        public Collection<V> values() {
            Collection<V> result = this.values;
            if (result != null) {
                return result;
            }
            result = Collections.unmodifiableCollection(this.delegate.values());
            this.values = result;
            return result;
        }
    }

    private static class CustomMultimap<K, V> extends AbstractMapBasedMultimap<K, V> {
        @GwtIncompatible("java serialization not supported")
        private static final long serialVersionUID = 0;
        transient Supplier<? extends Collection<V>> factory;

        CustomMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
            super(map);
            this.factory = (Supplier) Preconditions.checkNotNull(factory);
        }

        protected Collection<V> createCollection() {
            return (Collection) this.factory.get();
        }

        @GwtIncompatible("java.io.ObjectOutputStream")
        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(backingMap());
        }

        @GwtIncompatible("java.io.ObjectInputStream")
        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (Supplier) stream.readObject();
            setMap((Map) stream.readObject());
        }
    }

    private static final class TransformedEntriesListMultimap<K, V1, V2> extends TransformedEntriesMultimap<K, V1, V2> implements ListMultimap<K, V2> {
        TransformedEntriesListMultimap(ListMultimap<K, V1> fromMultimap, EntryTransformer<? super K, ? super V1, V2> transformer) {
            super(fromMultimap, transformer);
        }

        List<V2> transform(final K key, Collection<V1> values) {
            return Lists.transform((List) values, new Function<V1, V2>() {
                public V2 apply(V1 value) {
                    return TransformedEntriesListMultimap.this.transformer.transformEntry(key, value);
                }
            });
        }

        public List<V2> get(K key) {
            return transform((Object) key, this.fromMultimap.get(key));
        }

        public List<V2> removeAll(Object key) {
            return transform(key, this.fromMultimap.removeAll(key));
        }

        public List<V2> replaceValues(K k, Iterable<? extends V2> iterable) {
            throw new UnsupportedOperationException();
        }
    }

    static class UnmodifiableAsMapEntries<K, V> extends ForwardingSet<Entry<K, Collection<V>>> {
        private final Set<Entry<K, Collection<V>>> delegate;

        UnmodifiableAsMapEntries(Set<Entry<K, Collection<V>>> delegate) {
            this.delegate = delegate;
        }

        protected Set<Entry<K, Collection<V>>> delegate() {
            return this.delegate;
        }

        public Iterator<Entry<K, Collection<V>>> iterator() {
            final Iterator<Entry<K, Collection<V>>> iterator = this.delegate.iterator();
            return new ForwardingIterator<Entry<K, Collection<V>>>() {
                protected Iterator<Entry<K, Collection<V>>> delegate() {
                    return iterator;
                }

                public Entry<K, Collection<V>> next() {
                    return Multimaps.unmodifiableAsMapEntry((Entry) iterator.next());
                }
            };
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] array) {
            return standardToArray(array);
        }

        public boolean contains(Object o) {
            return Maps.containsEntryImpl(delegate(), o);
        }

        public boolean containsAll(Collection<?> c) {
            return standardContainsAll(c);
        }

        public boolean equals(@Nullable Object object) {
            return standardEquals(object);
        }
    }

    private static class UnmodifiableListMultimap<K, V> extends UnmodifiableMultimap<K, V> implements ListMultimap<K, V> {
        private static final long serialVersionUID = 0;

        UnmodifiableListMultimap(ListMultimap<K, V> delegate) {
            super(delegate);
        }

        public ListMultimap<K, V> delegate() {
            return (ListMultimap) super.delegate();
        }

        public List<V> get(K key) {
            return Collections.unmodifiableList(delegate().get(key));
        }

        public List<V> removeAll(Object key) {
            throw new UnsupportedOperationException();
        }

        public List<V> replaceValues(K k, Iterable<? extends V> iterable) {
            throw new UnsupportedOperationException();
        }
    }

    private static class UnmodifiableSetMultimap<K, V> extends UnmodifiableMultimap<K, V> implements SetMultimap<K, V> {
        private static final long serialVersionUID = 0;

        UnmodifiableSetMultimap(SetMultimap<K, V> delegate) {
            super(delegate);
        }

        public SetMultimap<K, V> delegate() {
            return (SetMultimap) super.delegate();
        }

        public Set<V> get(K key) {
            return Collections.unmodifiableSet(delegate().get(key));
        }

        public Set<Entry<K, V>> entries() {
            return Maps.unmodifiableEntrySet(delegate().entries());
        }

        public Set<V> removeAll(Object key) {
            throw new UnsupportedOperationException();
        }

        public Set<V> replaceValues(K k, Iterable<? extends V> iterable) {
            throw new UnsupportedOperationException();
        }
    }

    private static class CustomListMultimap<K, V> extends AbstractListMultimap<K, V> {
        @GwtIncompatible("java serialization not supported")
        private static final long serialVersionUID = 0;
        transient Supplier<? extends List<V>> factory;

        CustomListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
            super(map);
            this.factory = (Supplier) Preconditions.checkNotNull(factory);
        }

        protected List<V> createCollection() {
            return (List) this.factory.get();
        }

        @GwtIncompatible("java.io.ObjectOutputStream")
        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(backingMap());
        }

        @GwtIncompatible("java.io.ObjectInputStream")
        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (Supplier) stream.readObject();
            setMap((Map) stream.readObject());
        }
    }

    private static class CustomSetMultimap<K, V> extends AbstractSetMultimap<K, V> {
        @GwtIncompatible("not needed in emulated source")
        private static final long serialVersionUID = 0;
        transient Supplier<? extends Set<V>> factory;

        CustomSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
            super(map);
            this.factory = (Supplier) Preconditions.checkNotNull(factory);
        }

        protected Set<V> createCollection() {
            return (Set) this.factory.get();
        }

        @GwtIncompatible("java.io.ObjectOutputStream")
        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(backingMap());
        }

        @GwtIncompatible("java.io.ObjectInputStream")
        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (Supplier) stream.readObject();
            setMap((Map) stream.readObject());
        }
    }

    private static class UnmodifiableSortedSetMultimap<K, V> extends UnmodifiableSetMultimap<K, V> implements SortedSetMultimap<K, V> {
        private static final long serialVersionUID = 0;

        UnmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
            super(delegate);
        }

        public SortedSetMultimap<K, V> delegate() {
            return (SortedSetMultimap) super.delegate();
        }

        public SortedSet<V> get(K key) {
            return Collections.unmodifiableSortedSet(delegate().get(key));
        }

        public SortedSet<V> removeAll(Object key) {
            throw new UnsupportedOperationException();
        }

        public SortedSet<V> replaceValues(K k, Iterable<? extends V> iterable) {
            throw new UnsupportedOperationException();
        }

        public Comparator<? super V> valueComparator() {
            return delegate().valueComparator();
        }
    }

    private static class CustomSortedSetMultimap<K, V> extends AbstractSortedSetMultimap<K, V> {
        @GwtIncompatible("not needed in emulated source")
        private static final long serialVersionUID = 0;
        transient Supplier<? extends SortedSet<V>> factory;
        transient Comparator<? super V> valueComparator;

        CustomSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
            super(map);
            this.factory = (Supplier) Preconditions.checkNotNull(factory);
            this.valueComparator = ((SortedSet) factory.get()).comparator();
        }

        protected SortedSet<V> createCollection() {
            return (SortedSet) this.factory.get();
        }

        public Comparator<? super V> valueComparator() {
            return this.valueComparator;
        }

        @GwtIncompatible("java.io.ObjectOutputStream")
        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(backingMap());
        }

        @GwtIncompatible("java.io.ObjectInputStream")
        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (Supplier) stream.readObject();
            this.valueComparator = ((SortedSet) this.factory.get()).comparator();
            setMap((Map) stream.readObject());
        }
    }

    private Multimaps() {
    }

    public static <K, V> Multimap<K, V> newMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
        return new CustomMultimap(map, factory);
    }

    public static <K, V> ListMultimap<K, V> newListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
        return new CustomListMultimap(map, factory);
    }

    public static <K, V> SetMultimap<K, V> newSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
        return new CustomSetMultimap(map, factory);
    }

    public static <K, V> SortedSetMultimap<K, V> newSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
        return new CustomSortedSetMultimap(map, factory);
    }

    public static <K, V, M extends Multimap<K, V>> M invertFrom(Multimap<? extends V, ? extends K> source, M dest) {
        Preconditions.checkNotNull(dest);
        for (Entry<? extends V, ? extends K> entry : source.entries()) {
            dest.put(entry.getValue(), entry.getKey());
        }
        return dest;
    }

    public static <K, V> Multimap<K, V> synchronizedMultimap(Multimap<K, V> multimap) {
        return Synchronized.multimap(multimap, null);
    }

    public static <K, V> Multimap<K, V> unmodifiableMultimap(Multimap<K, V> delegate) {
        return ((delegate instanceof UnmodifiableMultimap) || (delegate instanceof ImmutableMultimap)) ? delegate : new UnmodifiableMultimap(delegate);
    }

    @Deprecated
    public static <K, V> Multimap<K, V> unmodifiableMultimap(ImmutableMultimap<K, V> delegate) {
        return (Multimap) Preconditions.checkNotNull(delegate);
    }

    public static <K, V> SetMultimap<K, V> synchronizedSetMultimap(SetMultimap<K, V> multimap) {
        return Synchronized.setMultimap(multimap, null);
    }

    public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(SetMultimap<K, V> delegate) {
        return ((delegate instanceof UnmodifiableSetMultimap) || (delegate instanceof ImmutableSetMultimap)) ? delegate : new UnmodifiableSetMultimap(delegate);
    }

    @Deprecated
    public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(ImmutableSetMultimap<K, V> delegate) {
        return (SetMultimap) Preconditions.checkNotNull(delegate);
    }

    public static <K, V> SortedSetMultimap<K, V> synchronizedSortedSetMultimap(SortedSetMultimap<K, V> multimap) {
        return Synchronized.sortedSetMultimap(multimap, null);
    }

    public static <K, V> SortedSetMultimap<K, V> unmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
        return delegate instanceof UnmodifiableSortedSetMultimap ? delegate : new UnmodifiableSortedSetMultimap(delegate);
    }

    public static <K, V> ListMultimap<K, V> synchronizedListMultimap(ListMultimap<K, V> multimap) {
        return Synchronized.listMultimap(multimap, null);
    }

    public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ListMultimap<K, V> delegate) {
        return ((delegate instanceof UnmodifiableListMultimap) || (delegate instanceof ImmutableListMultimap)) ? delegate : new UnmodifiableListMultimap(delegate);
    }

    @Deprecated
    public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ImmutableListMultimap<K, V> delegate) {
        return (ListMultimap) Preconditions.checkNotNull(delegate);
    }

    private static <V> Collection<V> unmodifiableValueCollection(Collection<V> collection) {
        if (collection instanceof SortedSet) {
            return Collections.unmodifiableSortedSet((SortedSet) collection);
        }
        if (collection instanceof Set) {
            return Collections.unmodifiableSet((Set) collection);
        }
        if (collection instanceof List) {
            return Collections.unmodifiableList((List) collection);
        }
        return Collections.unmodifiableCollection(collection);
    }

    private static <K, V> Entry<K, Collection<V>> unmodifiableAsMapEntry(final Entry<K, Collection<V>> entry) {
        Preconditions.checkNotNull(entry);
        return new AbstractMapEntry<K, Collection<V>>() {
            public K getKey() {
                return entry.getKey();
            }

            public Collection<V> getValue() {
                return Multimaps.unmodifiableValueCollection((Collection) entry.getValue());
            }
        };
    }

    private static <K, V> Collection<Entry<K, V>> unmodifiableEntries(Collection<Entry<K, V>> entries) {
        if (entries instanceof Set) {
            return Maps.unmodifiableEntrySet((Set) entries);
        }
        return new UnmodifiableEntries(Collections.unmodifiableCollection(entries));
    }

    private static <K, V> Set<Entry<K, Collection<V>>> unmodifiableAsMapEntries(Set<Entry<K, Collection<V>>> asMapEntries) {
        return new UnmodifiableAsMapEntries(Collections.unmodifiableSet(asMapEntries));
    }

    public static <K, V> SetMultimap<K, V> forMap(Map<K, V> map) {
        return new MapMultimap(map);
    }

    public static <K, V1, V2> Multimap<K, V2> transformValues(Multimap<K, V1> fromMultimap, final Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        return transformEntries((Multimap) fromMultimap, new EntryTransformer<K, V1, V2>() {
            public V2 transformEntry(K k, V1 value) {
                return function.apply(value);
            }
        });
    }

    public static <K, V1, V2> Multimap<K, V2> transformEntries(Multimap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
        return new TransformedEntriesMultimap(fromMap, transformer);
    }

    public static <K, V1, V2> ListMultimap<K, V2> transformValues(ListMultimap<K, V1> fromMultimap, final Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        return transformEntries((ListMultimap) fromMultimap, new EntryTransformer<K, V1, V2>() {
            public V2 transformEntry(K k, V1 value) {
                return function.apply(value);
            }
        });
    }

    public static <K, V1, V2> ListMultimap<K, V2> transformEntries(ListMultimap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
        return new TransformedEntriesListMultimap(fromMap, transformer);
    }

    public static <K, V> ImmutableListMultimap<K, V> index(Iterable<V> values, Function<? super V, K> keyFunction) {
        return index(values.iterator(), (Function) keyFunction);
    }

    public static <K, V> ImmutableListMultimap<K, V> index(Iterator<V> values, Function<? super V, K> keyFunction) {
        Preconditions.checkNotNull(keyFunction);
        Builder<K, V> builder = ImmutableListMultimap.builder();
        while (values.hasNext()) {
            Object value = values.next();
            Preconditions.checkNotNull(value, values);
            builder.put(keyFunction.apply(value), value);
        }
        return builder.build();
    }

    @GwtIncompatible("untested")
    public static <K, V> Multimap<K, V> filterKeys(Multimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
        if (unfiltered instanceof FilteredKeyMultimap) {
            FilteredKeyMultimap<K, V> prev = (FilteredKeyMultimap) unfiltered;
            return new FilteredKeyMultimap(prev.unfiltered, Predicates.and(prev.keyPredicate, keyPredicate));
        } else if (!(unfiltered instanceof FilteredMultimap)) {
            return new FilteredKeyMultimap(unfiltered, keyPredicate);
        } else {
            FilteredMultimap<K, V> prev2 = (FilteredMultimap) unfiltered;
            return new FilteredEntryMultimap(prev2.unfiltered, Predicates.and(prev2.entryPredicate(), Predicates.compose(keyPredicate, Maps.keyFunction())));
        }
    }

    @GwtIncompatible("untested")
    public static <K, V> Multimap<K, V> filterValues(Multimap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
        return filterEntries(unfiltered, Predicates.compose(valuePredicate, Maps.valueFunction()));
    }

    @GwtIncompatible("untested")
    public static <K, V> Multimap<K, V> filterEntries(Multimap<K, V> unfiltered, Predicate<? super Entry<K, V>> entryPredicate) {
        Preconditions.checkNotNull(entryPredicate);
        return unfiltered instanceof FilteredMultimap ? filterFiltered((FilteredMultimap) unfiltered, entryPredicate) : new FilteredEntryMultimap((Multimap) Preconditions.checkNotNull(unfiltered), entryPredicate);
    }

    private static <K, V> Multimap<K, V> filterFiltered(FilteredMultimap<K, V> multimap, Predicate<? super Entry<K, V>> entryPredicate) {
        return new FilteredEntryMultimap(multimap.unfiltered, Predicates.and(multimap.entryPredicate(), entryPredicate));
    }
}
