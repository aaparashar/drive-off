package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.MapDifference.ValueDifference;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Maps {
    static final MapJoiner STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");

    private static abstract class AbstractFilteredMap<K, V> extends AbstractMap<K, V> {
        final Predicate<? super Entry<K, V>> predicate;
        final Map<K, V> unfiltered;
        Collection<V> values;

        class Values extends AbstractCollection<V> {
            Values() {
            }

            public Iterator<V> iterator() {
                final Iterator<Entry<K, V>> entryIterator = AbstractFilteredMap.this.entrySet().iterator();
                return new UnmodifiableIterator<V>() {
                    public boolean hasNext() {
                        return entryIterator.hasNext();
                    }

                    public V next() {
                        return ((Entry) entryIterator.next()).getValue();
                    }
                };
            }

            public int size() {
                return AbstractFilteredMap.this.entrySet().size();
            }

            public void clear() {
                AbstractFilteredMap.this.entrySet().clear();
            }

            public boolean isEmpty() {
                return AbstractFilteredMap.this.entrySet().isEmpty();
            }

            public boolean remove(Object o) {
                Iterator<Entry<K, V>> iterator = AbstractFilteredMap.this.unfiltered.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<K, V> entry = (Entry) iterator.next();
                    if (Objects.equal(o, entry.getValue()) && AbstractFilteredMap.this.predicate.apply(entry)) {
                        iterator.remove();
                        return true;
                    }
                }
                return false;
            }

            public boolean removeAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                boolean changed = false;
                Iterator<Entry<K, V>> iterator = AbstractFilteredMap.this.unfiltered.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<K, V> entry = (Entry) iterator.next();
                    if (collection.contains(entry.getValue()) && AbstractFilteredMap.this.predicate.apply(entry)) {
                        iterator.remove();
                        changed = true;
                    }
                }
                return changed;
            }

            public boolean retainAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                boolean changed = false;
                Iterator<Entry<K, V>> iterator = AbstractFilteredMap.this.unfiltered.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<K, V> entry = (Entry) iterator.next();
                    if (!collection.contains(entry.getValue()) && AbstractFilteredMap.this.predicate.apply(entry)) {
                        iterator.remove();
                        changed = true;
                    }
                }
                return changed;
            }

            public Object[] toArray() {
                return Lists.newArrayList(iterator()).toArray();
            }

            public <T> T[] toArray(T[] array) {
                return Lists.newArrayList(iterator()).toArray(array);
            }
        }

        AbstractFilteredMap(Map<K, V> unfiltered, Predicate<? super Entry<K, V>> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }

        boolean apply(Object key, V value) {
            return this.predicate.apply(Maps.immutableEntry(key, value));
        }

        public V put(K key, V value) {
            Preconditions.checkArgument(apply(key, value));
            return this.unfiltered.put(key, value);
        }

        public void putAll(Map<? extends K, ? extends V> map) {
            for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
                Preconditions.checkArgument(apply(entry.getKey(), entry.getValue()));
            }
            this.unfiltered.putAll(map);
        }

        public boolean containsKey(Object key) {
            return this.unfiltered.containsKey(key) && apply(key, this.unfiltered.get(key));
        }

        public V get(Object key) {
            V value = this.unfiltered.get(key);
            return (value == null || !apply(key, value)) ? null : value;
        }

        public boolean isEmpty() {
            return entrySet().isEmpty();
        }

        public V remove(Object key) {
            return containsKey(key) ? this.unfiltered.remove(key) : null;
        }

        public Collection<V> values() {
            Collection<V> collection = this.values;
            if (collection != null) {
                return collection;
            }
            collection = new Values();
            this.values = collection;
            return collection;
        }
    }

    public interface EntryTransformer<K, V1, V2> {
        V2 transformEntry(@Nullable K k, @Nullable V1 v1);
    }

    @GwtCompatible
    static abstract class ImprovedAbstractMap<K, V> extends AbstractMap<K, V> {
        private Set<Entry<K, V>> entrySet;
        private Set<K> keySet;
        private Collection<V> values;

        /* renamed from: com.google.common.collect.Maps$ImprovedAbstractMap$2 */
        class C04212 extends Values<K, V> {
            C04212() {
            }

            Map<K, V> map() {
                return ImprovedAbstractMap.this;
            }
        }

        /* renamed from: com.google.common.collect.Maps$ImprovedAbstractMap$1 */
        class C06011 extends KeySet<K, V> {
            C06011() {
            }

            Map<K, V> map() {
                return ImprovedAbstractMap.this;
            }
        }

        protected abstract Set<Entry<K, V>> createEntrySet();

        ImprovedAbstractMap() {
        }

        public Set<Entry<K, V>> entrySet() {
            Set<Entry<K, V>> result = this.entrySet;
            if (result != null) {
                return result;
            }
            result = createEntrySet();
            this.entrySet = result;
            return result;
        }

        public Set<K> keySet() {
            Set<K> set = this.keySet;
            if (set != null) {
                return set;
            }
            set = new C06011();
            this.keySet = set;
            return set;
        }

        public Collection<V> values() {
            Collection<V> collection = this.values;
            if (collection != null) {
                return collection;
            }
            collection = new C04212();
            this.values = collection;
            return collection;
        }
    }

    static class TransformedEntriesMap<K, V1, V2> extends AbstractMap<K, V2> {
        Set<Entry<K, V2>> entrySet;
        final Map<K, V1> fromMap;
        final EntryTransformer<? super K, ? super V1, V2> transformer;
        Collection<V2> values;

        /* renamed from: com.google.common.collect.Maps$TransformedEntriesMap$2 */
        class C04242 extends Values<K, V2> {
            C04242() {
            }

            Map<K, V2> map() {
                return TransformedEntriesMap.this;
            }
        }

        /* renamed from: com.google.common.collect.Maps$TransformedEntriesMap$1 */
        class C06021 extends EntrySet<K, V2> {
            C06021() {
            }

            Map<K, V2> map() {
                return TransformedEntriesMap.this;
            }

            public Iterator<Entry<K, V2>> iterator() {
                return new TransformedIterator<Entry<K, V1>, Entry<K, V2>>(TransformedEntriesMap.this.fromMap.entrySet().iterator()) {
                    Entry<K, V2> transform(final Entry<K, V1> entry) {
                        return new AbstractMapEntry<K, V2>() {
                            public K getKey() {
                                return entry.getKey();
                            }

                            public V2 getValue() {
                                return TransformedEntriesMap.this.transformer.transformEntry(entry.getKey(), entry.getValue());
                            }
                        };
                    }
                };
            }
        }

        TransformedEntriesMap(Map<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
            this.fromMap = (Map) Preconditions.checkNotNull(fromMap);
            this.transformer = (EntryTransformer) Preconditions.checkNotNull(transformer);
        }

        public int size() {
            return this.fromMap.size();
        }

        public boolean containsKey(Object key) {
            return this.fromMap.containsKey(key);
        }

        public V2 get(Object key) {
            V1 value = this.fromMap.get(key);
            return (value != null || this.fromMap.containsKey(key)) ? this.transformer.transformEntry(key, value) : null;
        }

        public V2 remove(Object key) {
            return this.fromMap.containsKey(key) ? this.transformer.transformEntry(key, this.fromMap.remove(key)) : null;
        }

        public void clear() {
            this.fromMap.clear();
        }

        public Set<K> keySet() {
            return this.fromMap.keySet();
        }

        public Set<Entry<K, V2>> entrySet() {
            Set<Entry<K, V2>> result = this.entrySet;
            if (result != null) {
                return result;
            }
            result = new C06021();
            this.entrySet = result;
            return result;
        }

        public Collection<V2> values() {
            Collection<V2> collection = this.values;
            if (collection != null) {
                return collection;
            }
            collection = new C04242();
            this.values = collection;
            return collection;
        }
    }

    static abstract class Values<K, V> extends AbstractCollection<V> {
        abstract Map<K, V> map();

        Values() {
        }

        public Iterator<V> iterator() {
            return Maps.valueIterator(map().entrySet().iterator());
        }

        public boolean remove(Object o) {
            try {
                return super.remove(o);
            } catch (UnsupportedOperationException e) {
                for (Entry<K, V> entry : map().entrySet()) {
                    if (Objects.equal(o, entry.getValue())) {
                        map().remove(entry.getKey());
                        return true;
                    }
                }
                return false;
            }
        }

        public boolean removeAll(Collection<?> c) {
            try {
                return super.removeAll((Collection) Preconditions.checkNotNull(c));
            } catch (UnsupportedOperationException e) {
                Set<K> toRemove = Sets.newHashSet();
                for (Entry<K, V> entry : map().entrySet()) {
                    if (c.contains(entry.getValue())) {
                        toRemove.add(entry.getKey());
                    }
                }
                return map().keySet().removeAll(toRemove);
            }
        }

        public boolean retainAll(Collection<?> c) {
            try {
                return super.retainAll((Collection) Preconditions.checkNotNull(c));
            } catch (UnsupportedOperationException e) {
                Set<K> toRetain = Sets.newHashSet();
                for (Entry<K, V> entry : map().entrySet()) {
                    if (c.contains(entry.getValue())) {
                        toRetain.add(entry.getKey());
                    }
                }
                return map().keySet().retainAll(toRetain);
            }
        }

        public int size() {
            return map().size();
        }

        public boolean isEmpty() {
            return map().isEmpty();
        }

        public boolean contains(@Nullable Object o) {
            return map().containsValue(o);
        }

        public void clear() {
            map().clear();
        }
    }

    private static class AsMapView<K, V> extends ImprovedAbstractMap<K, V> {
        final Function<? super K, V> function;
        private final Set<K> set;

        /* renamed from: com.google.common.collect.Maps$AsMapView$1 */
        class C05961 extends EntrySet<K, V> {
            C05961() {
            }

            Map<K, V> map() {
                return AsMapView.this;
            }

            public Iterator<Entry<K, V>> iterator() {
                return Maps.asSetEntryIterator(AsMapView.this.backingSet(), AsMapView.this.function);
            }
        }

        Set<K> backingSet() {
            return this.set;
        }

        AsMapView(Set<K> set, Function<? super K, V> function) {
            this.set = (Set) Preconditions.checkNotNull(set);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        public Set<K> keySet() {
            return Maps.removeOnlySet(backingSet());
        }

        public Collection<V> values() {
            return Collections2.transform(this.set, this.function);
        }

        public int size() {
            return backingSet().size();
        }

        public boolean containsKey(@Nullable Object key) {
            return backingSet().contains(key);
        }

        public V get(@Nullable Object key) {
            if (!backingSet().contains(key)) {
                return null;
            }
            return this.function.apply(key);
        }

        public V remove(@Nullable Object key) {
            if (!backingSet().remove(key)) {
                return null;
            }
            return this.function.apply(key);
        }

        public void clear() {
            backingSet().clear();
        }

        protected Set<Entry<K, V>> createEntrySet() {
            return new C05961();
        }
    }

    private enum EntryFunction implements Function<Entry, Object> {
        KEY {
            @Nullable
            public Object apply(Entry entry) {
                return entry.getKey();
            }
        },
        VALUE {
            @Nullable
            public Object apply(Entry entry) {
                return entry.getValue();
            }
        }
    }

    static abstract class EntrySet<K, V> extends ImprovedAbstractSet<Entry<K, V>> {
        abstract Map<K, V> map();

        EntrySet() {
        }

        public int size() {
            return map().size();
        }

        public void clear() {
            map().clear();
        }

        public boolean contains(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry<?, ?> entry = (Entry) o;
            Object key = entry.getKey();
            V value = map().get(key);
            if (!Objects.equal(value, entry.getValue())) {
                return false;
            }
            if (value != null || map().containsKey(key)) {
                return true;
            }
            return false;
        }

        public boolean isEmpty() {
            return map().isEmpty();
        }

        public boolean remove(Object o) {
            if (!contains(o)) {
                return false;
            }
            return map().keySet().remove(((Entry) o).getKey());
        }

        public boolean removeAll(Collection<?> c) {
            boolean removeAll;
            try {
                removeAll = super.removeAll((Collection) Preconditions.checkNotNull(c));
            } catch (UnsupportedOperationException e) {
                removeAll = true;
                for (Object o : c) {
                    removeAll |= remove(o);
                }
            }
            return removeAll;
        }

        public boolean retainAll(Collection<?> c) {
            try {
                return super.retainAll((Collection) Preconditions.checkNotNull(c));
            } catch (UnsupportedOperationException e) {
                Set<Object> keys = Sets.newHashSetWithExpectedSize(c.size());
                for (Entry<?, ?> o : c) {
                    if (contains(o)) {
                        keys.add(o.getKey());
                    }
                }
                return map().keySet().retainAll(keys);
            }
        }
    }

    static class FilteredEntryMap<K, V> extends AbstractFilteredMap<K, V> {
        Set<Entry<K, V>> entrySet;
        final Set<Entry<K, V>> filteredEntrySet;
        Set<K> keySet;

        private class KeySet extends ImprovedAbstractSet<K> {
            private KeySet() {
            }

            public Iterator<K> iterator() {
                final Iterator<Entry<K, V>> iterator = FilteredEntryMap.this.filteredEntrySet.iterator();
                return new UnmodifiableIterator<K>() {
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    public K next() {
                        return ((Entry) iterator.next()).getKey();
                    }
                };
            }

            public int size() {
                return FilteredEntryMap.this.filteredEntrySet.size();
            }

            public void clear() {
                FilteredEntryMap.this.filteredEntrySet.clear();
            }

            public boolean contains(Object o) {
                return FilteredEntryMap.this.containsKey(o);
            }

            public boolean remove(Object o) {
                if (!FilteredEntryMap.this.containsKey(o)) {
                    return false;
                }
                FilteredEntryMap.this.unfiltered.remove(o);
                return true;
            }

            public boolean retainAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                boolean changed = false;
                Iterator<Entry<K, V>> iterator = FilteredEntryMap.this.unfiltered.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<K, V> entry = (Entry) iterator.next();
                    if (FilteredEntryMap.this.predicate.apply(entry) && !collection.contains(entry.getKey())) {
                        iterator.remove();
                        changed = true;
                    }
                }
                return changed;
            }

            public Object[] toArray() {
                return Lists.newArrayList(iterator()).toArray();
            }

            public <T> T[] toArray(T[] array) {
                return Lists.newArrayList(iterator()).toArray(array);
            }
        }

        private class EntrySet extends ForwardingSet<Entry<K, V>> {
            private EntrySet() {
            }

            protected Set<Entry<K, V>> delegate() {
                return FilteredEntryMap.this.filteredEntrySet;
            }

            public Iterator<Entry<K, V>> iterator() {
                final Iterator<Entry<K, V>> iterator = FilteredEntryMap.this.filteredEntrySet.iterator();
                return new UnmodifiableIterator<Entry<K, V>>() {
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    public Entry<K, V> next() {
                        final Entry<K, V> entry = (Entry) iterator.next();
                        return new ForwardingMapEntry<K, V>() {
                            protected Entry<K, V> delegate() {
                                return entry;
                            }

                            public V setValue(V value) {
                                Preconditions.checkArgument(FilteredEntryMap.this.apply(entry.getKey(), value));
                                return super.setValue(value);
                            }
                        };
                    }
                };
            }
        }

        FilteredEntryMap(Map<K, V> unfiltered, Predicate<? super Entry<K, V>> entryPredicate) {
            super(unfiltered, entryPredicate);
            this.filteredEntrySet = Sets.filter(unfiltered.entrySet(), this.predicate);
        }

        public Set<Entry<K, V>> entrySet() {
            Set<Entry<K, V>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = new EntrySet();
            this.entrySet = set;
            return set;
        }

        public Set<K> keySet() {
            Set<K> set = this.keySet;
            if (set != null) {
                return set;
            }
            set = createKeySet();
            this.keySet = set;
            return set;
        }

        Set<K> createKeySet() {
            return new KeySet();
        }
    }

    private static class FilteredKeyMap<K, V> extends AbstractFilteredMap<K, V> {
        Set<Entry<K, V>> entrySet;
        Predicate<? super K> keyPredicate;
        Set<K> keySet;

        FilteredKeyMap(Map<K, V> unfiltered, Predicate<? super K> keyPredicate, Predicate<Entry<K, V>> entryPredicate) {
            super(unfiltered, entryPredicate);
            this.keyPredicate = keyPredicate;
        }

        public Set<Entry<K, V>> entrySet() {
            Set<Entry<K, V>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = Sets.filter(this.unfiltered.entrySet(), this.predicate);
            this.entrySet = set;
            return set;
        }

        public Set<K> keySet() {
            Set<K> set = this.keySet;
            if (set != null) {
                return set;
            }
            set = Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
            this.keySet = set;
            return set;
        }

        public boolean containsKey(Object key) {
            return this.unfiltered.containsKey(key) && this.keyPredicate.apply(key);
        }
    }

    private static final class KeyPredicate<K, V> implements Predicate<Entry<K, V>> {
        private final Predicate<? super K> keyPredicate;

        KeyPredicate(Predicate<? super K> keyPredicate) {
            this.keyPredicate = (Predicate) Preconditions.checkNotNull(keyPredicate);
        }

        public boolean apply(Entry<K, V> input) {
            return this.keyPredicate.apply(input.getKey());
        }
    }

    static abstract class KeySet<K, V> extends ImprovedAbstractSet<K> {
        abstract Map<K, V> map();

        KeySet() {
        }

        public Iterator<K> iterator() {
            return Maps.keyIterator(map().entrySet().iterator());
        }

        public int size() {
            return map().size();
        }

        public boolean isEmpty() {
            return map().isEmpty();
        }

        public boolean contains(Object o) {
            return map().containsKey(o);
        }

        public boolean remove(Object o) {
            if (!contains(o)) {
                return false;
            }
            map().remove(o);
            return true;
        }

        public void clear() {
            map().clear();
        }
    }

    static class MapDifferenceImpl<K, V> implements MapDifference<K, V> {
        final boolean areEqual;
        final Map<K, ValueDifference<V>> differences;
        final Map<K, V> onBoth;
        final Map<K, V> onlyOnLeft;
        final Map<K, V> onlyOnRight;

        MapDifferenceImpl(boolean areEqual, Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, ValueDifference<V>> differences) {
            this.areEqual = areEqual;
            this.onlyOnLeft = onlyOnLeft;
            this.onlyOnRight = onlyOnRight;
            this.onBoth = onBoth;
            this.differences = differences;
        }

        public boolean areEqual() {
            return this.areEqual;
        }

        public Map<K, V> entriesOnlyOnLeft() {
            return this.onlyOnLeft;
        }

        public Map<K, V> entriesOnlyOnRight() {
            return this.onlyOnRight;
        }

        public Map<K, V> entriesInCommon() {
            return this.onBoth;
        }

        public Map<K, ValueDifference<V>> entriesDiffering() {
            return this.differences;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof MapDifference)) {
                return false;
            }
            MapDifference<?, ?> other = (MapDifference) object;
            if (entriesOnlyOnLeft().equals(other.entriesOnlyOnLeft()) && entriesOnlyOnRight().equals(other.entriesOnlyOnRight()) && entriesInCommon().equals(other.entriesInCommon()) && entriesDiffering().equals(other.entriesDiffering())) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(entriesOnlyOnLeft(), entriesOnlyOnRight(), entriesInCommon(), entriesDiffering());
        }

        public String toString() {
            if (this.areEqual) {
                return "equal";
            }
            StringBuilder result = new StringBuilder("not equal");
            if (!this.onlyOnLeft.isEmpty()) {
                result.append(": only on left=").append(this.onlyOnLeft);
            }
            if (!this.onlyOnRight.isEmpty()) {
                result.append(": only on right=").append(this.onlyOnRight);
            }
            if (!this.differences.isEmpty()) {
                result.append(": value differences=").append(this.differences);
            }
            return result.toString();
        }
    }

    @GwtIncompatible("NavigableMap")
    private static final class NavigableAsMapView<K, V> extends AbstractNavigableMap<K, V> {
        private final Function<? super K, V> function;
        private final NavigableSet<K> set;

        NavigableAsMapView(NavigableSet<K> ks, Function<? super K, V> vFunction) {
            this.set = (NavigableSet) Preconditions.checkNotNull(ks);
            this.function = (Function) Preconditions.checkNotNull(vFunction);
        }

        public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            return Maps.asMap(this.set.subSet(fromKey, fromInclusive, toKey, toInclusive), this.function);
        }

        public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
            return Maps.asMap(this.set.headSet(toKey, inclusive), this.function);
        }

        public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
            return Maps.asMap(this.set.tailSet(fromKey, inclusive), this.function);
        }

        public Comparator<? super K> comparator() {
            return this.set.comparator();
        }

        @Nullable
        public V get(@Nullable Object key) {
            if (!this.set.contains(key)) {
                return null;
            }
            return this.function.apply(key);
        }

        public void clear() {
            this.set.clear();
        }

        Iterator<Entry<K, V>> entryIterator() {
            return Maps.asSetEntryIterator(this.set, this.function);
        }

        Iterator<Entry<K, V>> descendingEntryIterator() {
            return descendingMap().entrySet().iterator();
        }

        public NavigableSet<K> navigableKeySet() {
            return Maps.removeOnlyNavigableSet(this.set);
        }

        public int size() {
            return this.set.size();
        }

        public NavigableMap<K, V> descendingMap() {
            return Maps.asMap(this.set.descendingSet(), this.function);
        }
    }

    static class TransformedEntriesSortedMap<K, V1, V2> extends TransformedEntriesMap<K, V1, V2> implements SortedMap<K, V2> {
        protected SortedMap<K, V1> fromMap() {
            return (SortedMap) this.fromMap;
        }

        TransformedEntriesSortedMap(SortedMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
            super(fromMap, transformer);
        }

        public Comparator<? super K> comparator() {
            return fromMap().comparator();
        }

        public K firstKey() {
            return fromMap().firstKey();
        }

        public SortedMap<K, V2> headMap(K toKey) {
            return Maps.transformEntries(fromMap().headMap(toKey), this.transformer);
        }

        public K lastKey() {
            return fromMap().lastKey();
        }

        public SortedMap<K, V2> subMap(K fromKey, K toKey) {
            return Maps.transformEntries(fromMap().subMap(fromKey, toKey), this.transformer);
        }

        public SortedMap<K, V2> tailMap(K fromKey) {
            return Maps.transformEntries(fromMap().tailMap(fromKey), this.transformer);
        }
    }

    static class ValueDifferenceImpl<V> implements ValueDifference<V> {
        private final V left;
        private final V right;

        static <V> ValueDifference<V> create(@Nullable V left, @Nullable V right) {
            return new ValueDifferenceImpl(left, right);
        }

        private ValueDifferenceImpl(@Nullable V left, @Nullable V right) {
            this.left = left;
            this.right = right;
        }

        public V leftValue() {
            return this.left;
        }

        public V rightValue() {
            return this.right;
        }

        public boolean equals(@Nullable Object object) {
            if (!(object instanceof ValueDifference)) {
                return false;
            }
            ValueDifference<?> that = (ValueDifference) object;
            if (Objects.equal(this.left, that.leftValue()) && Objects.equal(this.right, that.rightValue())) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(this.left, this.right);
        }

        public String toString() {
            return "(" + this.left + ", " + this.right + ")";
        }
    }

    private static final class ValuePredicate<K, V> implements Predicate<Entry<K, V>> {
        private final Predicate<? super V> valuePredicate;

        ValuePredicate(Predicate<? super V> valuePredicate) {
            this.valuePredicate = (Predicate) Preconditions.checkNotNull(valuePredicate);
        }

        public boolean apply(Entry<K, V> input) {
            return this.valuePredicate.apply(input.getValue());
        }
    }

    @GwtIncompatible("NavigableMap")
    static abstract class DescendingMap<K, V> extends ForwardingMap<K, V> implements NavigableMap<K, V> {
        private transient Comparator<? super K> comparator;
        private transient Set<Entry<K, V>> entrySet;
        private transient NavigableSet<K> navigableKeySet;

        /* renamed from: com.google.common.collect.Maps$DescendingMap$2 */
        class C04172 extends Values<K, V> {
            C04172() {
            }

            Map<K, V> map() {
                return DescendingMap.this;
            }
        }

        /* renamed from: com.google.common.collect.Maps$DescendingMap$1 */
        class C05971 extends EntrySet<K, V> {
            C05971() {
            }

            Map<K, V> map() {
                return DescendingMap.this;
            }

            public Iterator<Entry<K, V>> iterator() {
                return DescendingMap.this.entryIterator();
            }
        }

        abstract Iterator<Entry<K, V>> entryIterator();

        abstract NavigableMap<K, V> forward();

        DescendingMap() {
        }

        protected final Map<K, V> delegate() {
            return forward();
        }

        public Comparator<? super K> comparator() {
            Comparator<? super K> result = this.comparator;
            if (result != null) {
                return result;
            }
            Comparator<? super K> forwardCmp = forward().comparator();
            if (forwardCmp == null) {
                forwardCmp = Ordering.natural();
            }
            result = reverse(forwardCmp);
            this.comparator = result;
            return result;
        }

        private static <T> Ordering<T> reverse(Comparator<T> forward) {
            return Ordering.from((Comparator) forward).reverse();
        }

        public K firstKey() {
            return forward().lastKey();
        }

        public K lastKey() {
            return forward().firstKey();
        }

        public Entry<K, V> lowerEntry(K key) {
            return forward().higherEntry(key);
        }

        public K lowerKey(K key) {
            return forward().higherKey(key);
        }

        public Entry<K, V> floorEntry(K key) {
            return forward().ceilingEntry(key);
        }

        public K floorKey(K key) {
            return forward().ceilingKey(key);
        }

        public Entry<K, V> ceilingEntry(K key) {
            return forward().floorEntry(key);
        }

        public K ceilingKey(K key) {
            return forward().floorKey(key);
        }

        public Entry<K, V> higherEntry(K key) {
            return forward().lowerEntry(key);
        }

        public K higherKey(K key) {
            return forward().lowerKey(key);
        }

        public Entry<K, V> firstEntry() {
            return forward().lastEntry();
        }

        public Entry<K, V> lastEntry() {
            return forward().firstEntry();
        }

        public Entry<K, V> pollFirstEntry() {
            return forward().pollLastEntry();
        }

        public Entry<K, V> pollLastEntry() {
            return forward().pollFirstEntry();
        }

        public NavigableMap<K, V> descendingMap() {
            return forward();
        }

        public Set<Entry<K, V>> entrySet() {
            Set<Entry<K, V>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = createEntrySet();
            this.entrySet = set;
            return set;
        }

        Set<Entry<K, V>> createEntrySet() {
            return new C05971();
        }

        public Set<K> keySet() {
            return navigableKeySet();
        }

        public NavigableSet<K> navigableKeySet() {
            NavigableSet<K> navigableSet = this.navigableKeySet;
            if (navigableSet != null) {
                return navigableSet;
            }
            navigableSet = new NavigableKeySet(this);
            this.navigableKeySet = navigableSet;
            return navigableSet;
        }

        public NavigableSet<K> descendingKeySet() {
            return forward().navigableKeySet();
        }

        public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            return forward().subMap(toKey, toInclusive, fromKey, fromInclusive).descendingMap();
        }

        public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
            return forward().tailMap(toKey, inclusive).descendingMap();
        }

        public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
            return forward().headMap(fromKey, inclusive).descendingMap();
        }

        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            return subMap(fromKey, true, toKey, false);
        }

        public SortedMap<K, V> headMap(K toKey) {
            return headMap(toKey, false);
        }

        public SortedMap<K, V> tailMap(K fromKey) {
            return tailMap(fromKey, true);
        }

        public Collection<V> values() {
            return new C04172();
        }
    }

    static final class FilteredEntryBiMap<K, V> extends FilteredEntryMap<K, V> implements BiMap<K, V> {
        private final BiMap<V, K> inverse;

        private static <K, V> Predicate<Entry<V, K>> inversePredicate(final Predicate<? super Entry<K, V>> forwardPredicate) {
            return new Predicate<Entry<V, K>>() {
                public boolean apply(Entry<V, K> input) {
                    return forwardPredicate.apply(Maps.immutableEntry(input.getValue(), input.getKey()));
                }
            };
        }

        FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Entry<K, V>> predicate) {
            super(delegate, predicate);
            this.inverse = new FilteredEntryBiMap(delegate.inverse(), inversePredicate(predicate), this);
        }

        private FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Entry<K, V>> predicate, BiMap<V, K> inverse) {
            super(delegate, predicate);
            this.inverse = inverse;
        }

        BiMap<K, V> unfiltered() {
            return (BiMap) this.unfiltered;
        }

        public V forcePut(@Nullable K key, @Nullable V value) {
            Preconditions.checkArgument(this.predicate.apply(Maps.immutableEntry(key, value)));
            return unfiltered().forcePut(key, value);
        }

        public BiMap<V, K> inverse() {
            return this.inverse;
        }

        public Set<V> values() {
            return this.inverse.keySet();
        }
    }

    private static class FilteredEntrySortedMap<K, V> extends FilteredEntryMap<K, V> implements SortedMap<K, V> {
        FilteredEntrySortedMap(SortedMap<K, V> unfiltered, Predicate<? super Entry<K, V>> entryPredicate) {
            super(unfiltered, entryPredicate);
        }

        SortedMap<K, V> sortedMap() {
            return (SortedMap) this.unfiltered;
        }

        public Comparator<? super K> comparator() {
            return sortedMap().comparator();
        }

        public K firstKey() {
            return keySet().iterator().next();
        }

        public K lastKey() {
            SortedMap<K, V> headMap = sortedMap();
            while (true) {
                K key = headMap.lastKey();
                if (apply(key, this.unfiltered.get(key))) {
                    return key;
                }
                headMap = sortedMap().headMap(key);
            }
        }

        public SortedMap<K, V> headMap(K toKey) {
            return new FilteredEntrySortedMap(sortedMap().headMap(toKey), this.predicate);
        }

        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            return new FilteredEntrySortedMap(sortedMap().subMap(fromKey, toKey), this.predicate);
        }

        public SortedMap<K, V> tailMap(K fromKey) {
            return new FilteredEntrySortedMap(sortedMap().tailMap(fromKey), this.predicate);
        }
    }

    @GwtIncompatible("NavigableMap")
    static class NavigableKeySet<K, V> extends KeySet<K, V> implements NavigableSet<K> {
        private final NavigableMap<K, V> map;

        NavigableKeySet(NavigableMap<K, V> map) {
            this.map = (NavigableMap) Preconditions.checkNotNull(map);
        }

        NavigableMap<K, V> map() {
            return this.map;
        }

        public Comparator<? super K> comparator() {
            return map().comparator();
        }

        public K first() {
            return map().firstKey();
        }

        public K last() {
            return map().lastKey();
        }

        public K lower(K e) {
            return map().lowerKey(e);
        }

        public K floor(K e) {
            return map().floorKey(e);
        }

        public K ceiling(K e) {
            return map().ceilingKey(e);
        }

        public K higher(K e) {
            return map().higherKey(e);
        }

        public K pollFirst() {
            return Maps.keyOrNull(map().pollFirstEntry());
        }

        public K pollLast() {
            return Maps.keyOrNull(map().pollLastEntry());
        }

        public NavigableSet<K> descendingSet() {
            return map().descendingKeySet();
        }

        public Iterator<K> descendingIterator() {
            return descendingSet().iterator();
        }

        public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) {
            return map().subMap(fromElement, fromInclusive, toElement, toInclusive).navigableKeySet();
        }

        public NavigableSet<K> headSet(K toElement, boolean inclusive) {
            return map().headMap(toElement, inclusive).navigableKeySet();
        }

        public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
            return map().tailMap(fromElement, inclusive).navigableKeySet();
        }

        public SortedSet<K> subSet(K fromElement, K toElement) {
            return subSet(fromElement, true, toElement, false);
        }

        public SortedSet<K> headSet(K toElement) {
            return headSet(toElement, false);
        }

        public SortedSet<K> tailSet(K fromElement) {
            return tailSet(fromElement, true);
        }
    }

    private static class SortedAsMapView<K, V> extends AsMapView<K, V> implements SortedMap<K, V> {
        SortedAsMapView(SortedSet<K> set, Function<? super K, V> function) {
            super(set, function);
        }

        SortedSet<K> backingSet() {
            return (SortedSet) super.backingSet();
        }

        public Comparator<? super K> comparator() {
            return backingSet().comparator();
        }

        public Set<K> keySet() {
            return Maps.removeOnlySortedSet(backingSet());
        }

        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            return Maps.asMap(backingSet().subSet(fromKey, toKey), this.function);
        }

        public SortedMap<K, V> headMap(K toKey) {
            return Maps.asMap(backingSet().headSet(toKey), this.function);
        }

        public SortedMap<K, V> tailMap(K fromKey) {
            return Maps.asMap(backingSet().tailSet(fromKey), this.function);
        }

        public K firstKey() {
            return backingSet().first();
        }

        public K lastKey() {
            return backingSet().last();
        }
    }

    static class SortedMapDifferenceImpl<K, V> extends MapDifferenceImpl<K, V> implements SortedMapDifference<K, V> {
        SortedMapDifferenceImpl(boolean areEqual, SortedMap<K, V> onlyOnLeft, SortedMap<K, V> onlyOnRight, SortedMap<K, V> onBoth, SortedMap<K, ValueDifference<V>> differences) {
            super(areEqual, onlyOnLeft, onlyOnRight, onBoth, differences);
        }

        public SortedMap<K, ValueDifference<V>> entriesDiffering() {
            return (SortedMap) super.entriesDiffering();
        }

        public SortedMap<K, V> entriesInCommon() {
            return (SortedMap) super.entriesInCommon();
        }

        public SortedMap<K, V> entriesOnlyOnLeft() {
            return (SortedMap) super.entriesOnlyOnLeft();
        }

        public SortedMap<K, V> entriesOnlyOnRight() {
            return (SortedMap) super.entriesOnlyOnRight();
        }
    }

    @GwtIncompatible("NavigableMap")
    private static class TransformedEntriesNavigableMap<K, V1, V2> extends TransformedEntriesSortedMap<K, V1, V2> implements NavigableMap<K, V2> {
        TransformedEntriesNavigableMap(NavigableMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
            super(fromMap, transformer);
        }

        public Entry<K, V2> ceilingEntry(K key) {
            return transformEntry(fromMap().ceilingEntry(key));
        }

        public K ceilingKey(K key) {
            return fromMap().ceilingKey(key);
        }

        public NavigableSet<K> descendingKeySet() {
            return fromMap().descendingKeySet();
        }

        public NavigableMap<K, V2> descendingMap() {
            return Maps.transformEntries(fromMap().descendingMap(), this.transformer);
        }

        public Entry<K, V2> firstEntry() {
            return transformEntry(fromMap().firstEntry());
        }

        public Entry<K, V2> floorEntry(K key) {
            return transformEntry(fromMap().floorEntry(key));
        }

        public K floorKey(K key) {
            return fromMap().floorKey(key);
        }

        public NavigableMap<K, V2> headMap(K toKey) {
            return headMap(toKey, false);
        }

        public NavigableMap<K, V2> headMap(K toKey, boolean inclusive) {
            return Maps.transformEntries(fromMap().headMap(toKey, inclusive), this.transformer);
        }

        public Entry<K, V2> higherEntry(K key) {
            return transformEntry(fromMap().higherEntry(key));
        }

        public K higherKey(K key) {
            return fromMap().higherKey(key);
        }

        public Entry<K, V2> lastEntry() {
            return transformEntry(fromMap().lastEntry());
        }

        public Entry<K, V2> lowerEntry(K key) {
            return transformEntry(fromMap().lowerEntry(key));
        }

        public K lowerKey(K key) {
            return fromMap().lowerKey(key);
        }

        public NavigableSet<K> navigableKeySet() {
            return fromMap().navigableKeySet();
        }

        public Entry<K, V2> pollFirstEntry() {
            return transformEntry(fromMap().pollFirstEntry());
        }

        public Entry<K, V2> pollLastEntry() {
            return transformEntry(fromMap().pollLastEntry());
        }

        public NavigableMap<K, V2> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            return Maps.transformEntries(fromMap().subMap(fromKey, fromInclusive, toKey, toInclusive), this.transformer);
        }

        public NavigableMap<K, V2> subMap(K fromKey, K toKey) {
            return subMap(fromKey, true, toKey, false);
        }

        public NavigableMap<K, V2> tailMap(K fromKey) {
            return tailMap(fromKey, true);
        }

        public NavigableMap<K, V2> tailMap(K fromKey, boolean inclusive) {
            return Maps.transformEntries(fromMap().tailMap(fromKey, inclusive), this.transformer);
        }

        private Entry<K, V2> transformEntry(Entry<K, V1> entry) {
            if (entry == null) {
                return null;
            }
            K key = entry.getKey();
            return Maps.immutableEntry(key, this.transformer.transformEntry(key, entry.getValue()));
        }

        protected NavigableMap<K, V1> fromMap() {
            return (NavigableMap) super.fromMap();
        }
    }

    private static class UnmodifiableBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        final BiMap<? extends K, ? extends V> delegate;
        BiMap<V, K> inverse;
        final Map<K, V> unmodifiableMap;
        transient Set<V> values;

        UnmodifiableBiMap(BiMap<? extends K, ? extends V> delegate, @Nullable BiMap<V, K> inverse) {
            this.unmodifiableMap = Collections.unmodifiableMap(delegate);
            this.delegate = delegate;
            this.inverse = inverse;
        }

        protected Map<K, V> delegate() {
            return this.unmodifiableMap;
        }

        public V forcePut(K k, V v) {
            throw new UnsupportedOperationException();
        }

        public BiMap<V, K> inverse() {
            BiMap<V, K> biMap = this.inverse;
            if (biMap != null) {
                return biMap;
            }
            biMap = new UnmodifiableBiMap(this.delegate.inverse(), this);
            this.inverse = biMap;
            return biMap;
        }

        public Set<V> values() {
            Set<V> set = this.values;
            if (set != null) {
                return set;
            }
            set = Collections.unmodifiableSet(this.delegate.values());
            this.values = set;
            return set;
        }
    }

    static class UnmodifiableEntries<K, V> extends ForwardingCollection<Entry<K, V>> {
        private final Collection<Entry<K, V>> entries;

        UnmodifiableEntries(Collection<Entry<K, V>> entries) {
            this.entries = entries;
        }

        protected Collection<Entry<K, V>> delegate() {
            return this.entries;
        }

        public Iterator<Entry<K, V>> iterator() {
            final Iterator<Entry<K, V>> delegate = super.iterator();
            return new ForwardingIterator<Entry<K, V>>() {
                public Entry<K, V> next() {
                    return Maps.unmodifiableEntry((Entry) super.next());
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }

                protected Iterator<Entry<K, V>> delegate() {
                    return delegate;
                }
            };
        }

        public boolean add(Entry<K, V> entry) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends Entry<K, V>> collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object object) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] array) {
            return standardToArray(array);
        }
    }

    @GwtIncompatible("NavigableMap")
    private static class FilteredEntryNavigableMap<K, V> extends FilteredEntrySortedMap<K, V> implements NavigableMap<K, V> {
        FilteredEntryNavigableMap(NavigableMap<K, V> unfiltered, Predicate<? super Entry<K, V>> entryPredicate) {
            super(unfiltered, entryPredicate);
        }

        NavigableMap<K, V> sortedMap() {
            return (NavigableMap) super.sortedMap();
        }

        public Entry<K, V> lowerEntry(K key) {
            return headMap(key, false).lastEntry();
        }

        public K lowerKey(K key) {
            return Maps.keyOrNull(lowerEntry(key));
        }

        public Entry<K, V> floorEntry(K key) {
            return headMap(key, true).lastEntry();
        }

        public K floorKey(K key) {
            return Maps.keyOrNull(floorEntry(key));
        }

        public Entry<K, V> ceilingEntry(K key) {
            return tailMap(key, true).firstEntry();
        }

        public K ceilingKey(K key) {
            return Maps.keyOrNull(ceilingEntry(key));
        }

        public Entry<K, V> higherEntry(K key) {
            return tailMap(key, false).firstEntry();
        }

        public K higherKey(K key) {
            return Maps.keyOrNull(higherEntry(key));
        }

        public Entry<K, V> firstEntry() {
            return (Entry) Iterables.getFirst(entrySet(), null);
        }

        public Entry<K, V> lastEntry() {
            return (Entry) Iterables.getFirst(descendingMap().entrySet(), null);
        }

        public Entry<K, V> pollFirstEntry() {
            return pollFirstSatisfyingEntry(sortedMap().entrySet().iterator());
        }

        public Entry<K, V> pollLastEntry() {
            return pollFirstSatisfyingEntry(sortedMap().descendingMap().entrySet().iterator());
        }

        @Nullable
        Entry<K, V> pollFirstSatisfyingEntry(Iterator<Entry<K, V>> entryIterator) {
            while (entryIterator.hasNext()) {
                Entry<K, V> entry = (Entry) entryIterator.next();
                if (this.predicate.apply(entry)) {
                    entryIterator.remove();
                    return entry;
                }
            }
            return null;
        }

        public NavigableMap<K, V> descendingMap() {
            return Maps.filterEntries(sortedMap().descendingMap(), this.predicate);
        }

        public NavigableSet<K> keySet() {
            return (NavigableSet) super.keySet();
        }

        NavigableSet<K> createKeySet() {
            return new NavigableKeySet<K, V>(this) {
                public boolean removeAll(Collection<?> c) {
                    boolean changed = false;
                    Iterator<Entry<K, V>> entryIterator = FilteredEntryNavigableMap.this.sortedMap().entrySet().iterator();
                    while (entryIterator.hasNext()) {
                        Entry<K, V> entry = (Entry) entryIterator.next();
                        if (c.contains(entry.getKey()) && FilteredEntryNavigableMap.this.predicate.apply(entry)) {
                            entryIterator.remove();
                            changed = true;
                        }
                    }
                    return changed;
                }

                public boolean retainAll(Collection<?> c) {
                    boolean changed = false;
                    Iterator<Entry<K, V>> entryIterator = FilteredEntryNavigableMap.this.sortedMap().entrySet().iterator();
                    while (entryIterator.hasNext()) {
                        Entry<K, V> entry = (Entry) entryIterator.next();
                        if (!c.contains(entry.getKey()) && FilteredEntryNavigableMap.this.predicate.apply(entry)) {
                            entryIterator.remove();
                            changed = true;
                        }
                    }
                    return changed;
                }
            };
        }

        public NavigableSet<K> navigableKeySet() {
            return keySet();
        }

        public NavigableSet<K> descendingKeySet() {
            return descendingMap().navigableKeySet();
        }

        public NavigableMap<K, V> subMap(K fromKey, K toKey) {
            return subMap(fromKey, true, toKey, false);
        }

        public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            return Maps.filterEntries(sortedMap().subMap(fromKey, fromInclusive, toKey, toInclusive), this.predicate);
        }

        public NavigableMap<K, V> headMap(K toKey) {
            return headMap(toKey, false);
        }

        public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
            return Maps.filterEntries(sortedMap().headMap(toKey, inclusive), this.predicate);
        }

        public NavigableMap<K, V> tailMap(K fromKey) {
            return tailMap(fromKey, true);
        }

        public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
            return Maps.filterEntries(sortedMap().tailMap(fromKey, inclusive), this.predicate);
        }
    }

    static class UnmodifiableEntrySet<K, V> extends UnmodifiableEntries<K, V> implements Set<Entry<K, V>> {
        UnmodifiableEntrySet(Set<Entry<K, V>> entries) {
            super(entries);
        }

        public boolean equals(@Nullable Object object) {
            return Sets.equalsImpl(this, object);
        }

        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    @GwtIncompatible("NavigableMap")
    static class UnmodifiableNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V>, Serializable {
        private final NavigableMap<K, V> delegate;
        private transient UnmodifiableNavigableMap<K, V> descendingMap;

        UnmodifiableNavigableMap(NavigableMap<K, V> delegate) {
            this.delegate = delegate;
        }

        protected SortedMap<K, V> delegate() {
            return Collections.unmodifiableSortedMap(this.delegate);
        }

        public Entry<K, V> lowerEntry(K key) {
            return Maps.unmodifiableOrNull(this.delegate.lowerEntry(key));
        }

        public K lowerKey(K key) {
            return this.delegate.lowerKey(key);
        }

        public Entry<K, V> floorEntry(K key) {
            return Maps.unmodifiableOrNull(this.delegate.floorEntry(key));
        }

        public K floorKey(K key) {
            return this.delegate.floorKey(key);
        }

        public Entry<K, V> ceilingEntry(K key) {
            return Maps.unmodifiableOrNull(this.delegate.ceilingEntry(key));
        }

        public K ceilingKey(K key) {
            return this.delegate.ceilingKey(key);
        }

        public Entry<K, V> higherEntry(K key) {
            return Maps.unmodifiableOrNull(this.delegate.higherEntry(key));
        }

        public K higherKey(K key) {
            return this.delegate.higherKey(key);
        }

        public Entry<K, V> firstEntry() {
            return Maps.unmodifiableOrNull(this.delegate.firstEntry());
        }

        public Entry<K, V> lastEntry() {
            return Maps.unmodifiableOrNull(this.delegate.lastEntry());
        }

        public final Entry<K, V> pollFirstEntry() {
            throw new UnsupportedOperationException();
        }

        public final Entry<K, V> pollLastEntry() {
            throw new UnsupportedOperationException();
        }

        public NavigableMap<K, V> descendingMap() {
            UnmodifiableNavigableMap<K, V> result = this.descendingMap;
            if (result != null) {
                return result;
            }
            result = new UnmodifiableNavigableMap(this.delegate.descendingMap());
            this.descendingMap = result;
            result.descendingMap = this;
            return result;
        }

        public Set<K> keySet() {
            return navigableKeySet();
        }

        public NavigableSet<K> navigableKeySet() {
            return Sets.unmodifiableNavigableSet(this.delegate.navigableKeySet());
        }

        public NavigableSet<K> descendingKeySet() {
            return Sets.unmodifiableNavigableSet(this.delegate.descendingKeySet());
        }

        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            return subMap(fromKey, true, toKey, false);
        }

        public SortedMap<K, V> headMap(K toKey) {
            return headMap(toKey, false);
        }

        public SortedMap<K, V> tailMap(K fromKey) {
            return tailMap(fromKey, true);
        }

        public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            return Maps.unmodifiableNavigableMap(this.delegate.subMap(fromKey, fromInclusive, toKey, toInclusive));
        }

        public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
            return Maps.unmodifiableNavigableMap(this.delegate.headMap(toKey, inclusive));
        }

        public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
            return Maps.unmodifiableNavigableMap(this.delegate.tailMap(fromKey, inclusive));
        }
    }

    private Maps() {
    }

    static <K> Function<Entry<K, ?>, K> keyFunction() {
        return EntryFunction.KEY;
    }

    static <V> Function<Entry<?, V>, V> valueFunction() {
        return EntryFunction.VALUE;
    }

    @GwtCompatible(serializable = true)
    @Beta
    public static <K extends Enum<K>, V> ImmutableMap<K, V> immutableEnumMap(Map<K, V> map) {
        if (map instanceof ImmutableEnumMap) {
            return (ImmutableEnumMap) map;
        }
        if (map.isEmpty()) {
            return ImmutableMap.of();
        }
        for (Entry<K, V> entry : map.entrySet()) {
            Preconditions.checkNotNull(entry.getKey());
            Preconditions.checkNotNull(entry.getValue());
        }
        return ImmutableEnumMap.asImmutable(new EnumMap(map));
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap();
    }

    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
        return new HashMap(capacity(expectedSize));
    }

    static int capacity(int expectedSize) {
        if (expectedSize < 3) {
            Preconditions.checkArgument(expectedSize >= 0);
            return expectedSize + 1;
        } else if (expectedSize < 1073741824) {
            return (expectedSize / 3) + expectedSize;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
        return new HashMap(map);
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) {
        return new LinkedHashMap(map);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
        return new MapMaker().makeMap();
    }

    public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap();
    }

    public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> map) {
        return new TreeMap(map);
    }

    public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@Nullable Comparator<C> comparator) {
        return new TreeMap(comparator);
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) {
        return new EnumMap((Class) Preconditions.checkNotNull(type));
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, ? extends V> map) {
        return new EnumMap(map);
    }

    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return new IdentityHashMap();
    }

    public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right) {
        if (left instanceof SortedMap) {
            return difference((SortedMap) left, (Map) right);
        }
        return difference(left, right, Equivalence.equals());
    }

    @Beta
    public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence) {
        Preconditions.checkNotNull(valueEquivalence);
        Map<K, V> onlyOnLeft = newHashMap();
        Map<K, V> onlyOnRight = new HashMap(right);
        Map<K, V> onBoth = newHashMap();
        Map<K, ValueDifference<V>> differences = newHashMap();
        boolean eq = true;
        for (Entry<? extends K, ? extends V> entry : left.entrySet()) {
            K leftKey = entry.getKey();
            V leftValue = entry.getValue();
            if (right.containsKey(leftKey)) {
                V rightValue = onlyOnRight.remove(leftKey);
                if (valueEquivalence.equivalent(leftValue, rightValue)) {
                    onBoth.put(leftKey, leftValue);
                } else {
                    eq = false;
                    differences.put(leftKey, ValueDifferenceImpl.create(leftValue, rightValue));
                }
            } else {
                eq = false;
                onlyOnLeft.put(leftKey, leftValue);
            }
        }
        boolean areEqual = eq && onlyOnRight.isEmpty();
        return mapDifference(areEqual, onlyOnLeft, onlyOnRight, onBoth, differences);
    }

    private static <K, V> MapDifference<K, V> mapDifference(boolean areEqual, Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, ValueDifference<V>> differences) {
        return new MapDifferenceImpl(areEqual, Collections.unmodifiableMap(onlyOnLeft), Collections.unmodifiableMap(onlyOnRight), Collections.unmodifiableMap(onBoth), Collections.unmodifiableMap(differences));
    }

    public static <K, V> SortedMapDifference<K, V> difference(SortedMap<K, ? extends V> left, Map<? extends K, ? extends V> right) {
        Preconditions.checkNotNull(left);
        Preconditions.checkNotNull(right);
        Comparator comparator = orNaturalOrder(left.comparator());
        SortedMap<K, V> onlyOnLeft = newTreeMap(comparator);
        SortedMap<K, V> onlyOnRight = newTreeMap(comparator);
        onlyOnRight.putAll(right);
        SortedMap<K, V> onBoth = newTreeMap(comparator);
        SortedMap<K, ValueDifference<V>> differences = newTreeMap(comparator);
        boolean eq = true;
        for (Entry<? extends K, ? extends V> entry : left.entrySet()) {
            K leftKey = entry.getKey();
            V leftValue = entry.getValue();
            if (right.containsKey(leftKey)) {
                V rightValue = onlyOnRight.remove(leftKey);
                if (Objects.equal(leftValue, rightValue)) {
                    onBoth.put(leftKey, leftValue);
                } else {
                    eq = false;
                    differences.put(leftKey, ValueDifferenceImpl.create(leftValue, rightValue));
                }
            } else {
                eq = false;
                onlyOnLeft.put(leftKey, leftValue);
            }
        }
        boolean areEqual = eq && onlyOnRight.isEmpty();
        return sortedMapDifference(areEqual, onlyOnLeft, onlyOnRight, onBoth, differences);
    }

    private static <K, V> SortedMapDifference<K, V> sortedMapDifference(boolean areEqual, SortedMap<K, V> onlyOnLeft, SortedMap<K, V> onlyOnRight, SortedMap<K, V> onBoth, SortedMap<K, ValueDifference<V>> differences) {
        return new SortedMapDifferenceImpl(areEqual, Collections.unmodifiableSortedMap(onlyOnLeft), Collections.unmodifiableSortedMap(onlyOnRight), Collections.unmodifiableSortedMap(onBoth), Collections.unmodifiableSortedMap(differences));
    }

    static <E> Comparator<? super E> orNaturalOrder(@Nullable Comparator<? super E> comparator) {
        return comparator != null ? comparator : Ordering.natural();
    }

    @Beta
    public static <K, V> Map<K, V> asMap(Set<K> set, Function<? super K, V> function) {
        if (set instanceof SortedSet) {
            return asMap((SortedSet) set, (Function) function);
        }
        return new AsMapView(set, function);
    }

    @Beta
    public static <K, V> SortedMap<K, V> asMap(SortedSet<K> set, Function<? super K, V> function) {
        return Platform.mapsAsMapSortedSet(set, function);
    }

    static <K, V> SortedMap<K, V> asMapSortedIgnoreNavigable(SortedSet<K> set, Function<? super K, V> function) {
        return new SortedAsMapView(set, function);
    }

    @GwtIncompatible("NavigableMap")
    @Beta
    public static <K, V> NavigableMap<K, V> asMap(NavigableSet<K> set, Function<? super K, V> function) {
        return new NavigableAsMapView(set, function);
    }

    private static <K, V> Iterator<Entry<K, V>> asSetEntryIterator(Set<K> set, final Function<? super K, V> function) {
        return new TransformedIterator<K, Entry<K, V>>(set.iterator()) {
            Entry<K, V> transform(K key) {
                return Maps.immutableEntry(key, function.apply(key));
            }
        };
    }

    private static <E> Set<E> removeOnlySet(final Set<E> set) {
        return new ForwardingSet<E>() {
            protected Set<E> delegate() {
                return set;
            }

            public boolean add(E e) {
                throw new UnsupportedOperationException();
            }

            public boolean addAll(Collection<? extends E> collection) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static <E> SortedSet<E> removeOnlySortedSet(final SortedSet<E> set) {
        return new ForwardingSortedSet<E>() {
            protected SortedSet<E> delegate() {
                return set;
            }

            public boolean add(E e) {
                throw new UnsupportedOperationException();
            }

            public boolean addAll(Collection<? extends E> collection) {
                throw new UnsupportedOperationException();
            }

            public SortedSet<E> headSet(E toElement) {
                return Maps.removeOnlySortedSet(super.headSet(toElement));
            }

            public SortedSet<E> subSet(E fromElement, E toElement) {
                return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement));
            }

            public SortedSet<E> tailSet(E fromElement) {
                return Maps.removeOnlySortedSet(super.tailSet(fromElement));
            }
        };
    }

    @GwtIncompatible("NavigableSet")
    private static <E> NavigableSet<E> removeOnlyNavigableSet(final NavigableSet<E> set) {
        return new ForwardingNavigableSet<E>() {
            protected NavigableSet<E> delegate() {
                return set;
            }

            public boolean add(E e) {
                throw new UnsupportedOperationException();
            }

            public boolean addAll(Collection<? extends E> collection) {
                throw new UnsupportedOperationException();
            }

            public SortedSet<E> headSet(E toElement) {
                return Maps.removeOnlySortedSet(super.headSet(toElement));
            }

            public SortedSet<E> subSet(E fromElement, E toElement) {
                return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement));
            }

            public SortedSet<E> tailSet(E fromElement) {
                return Maps.removeOnlySortedSet(super.tailSet(fromElement));
            }

            public NavigableSet<E> headSet(E toElement, boolean inclusive) {
                return Maps.removeOnlyNavigableSet(super.headSet(toElement, inclusive));
            }

            public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
                return Maps.removeOnlyNavigableSet(super.tailSet(fromElement, inclusive));
            }

            public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
                return Maps.removeOnlyNavigableSet(super.subSet(fromElement, fromInclusive, toElement, toInclusive));
            }

            public NavigableSet<E> descendingSet() {
                return Maps.removeOnlyNavigableSet(super.descendingSet());
            }
        };
    }

    @Beta
    public static <K, V> ImmutableMap<K, V> toMap(Iterable<K> keys, Function<? super K, V> valueFunction) {
        return toMap(keys.iterator(), (Function) valueFunction);
    }

    @Beta
    public static <K, V> ImmutableMap<K, V> toMap(Iterator<K> keys, Function<? super K, V> valueFunction) {
        Preconditions.checkNotNull(valueFunction);
        Map<K, V> builder = newLinkedHashMap();
        while (keys.hasNext()) {
            K key = keys.next();
            builder.put(key, valueFunction.apply(key));
        }
        return ImmutableMap.copyOf(builder);
    }

    public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> values, Function<? super V, K> keyFunction) {
        return uniqueIndex(values.iterator(), (Function) keyFunction);
    }

    public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterator<V> values, Function<? super V, K> keyFunction) {
        Preconditions.checkNotNull(keyFunction);
        Builder<K, V> builder = ImmutableMap.builder();
        while (values.hasNext()) {
            V value = values.next();
            builder.put(keyFunction.apply(value), value);
        }
        return builder.build();
    }

    @GwtIncompatible("java.util.Properties")
    public static ImmutableMap<String, String> fromProperties(Properties properties) {
        Builder<String, String> builder = ImmutableMap.builder();
        Enumeration<?> e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            builder.put(key, properties.getProperty(key));
        }
        return builder.build();
    }

    @GwtCompatible(serializable = true)
    public static <K, V> Entry<K, V> immutableEntry(@Nullable K key, @Nullable V value) {
        return new ImmutableEntry(key, value);
    }

    static <K, V> Set<Entry<K, V>> unmodifiableEntrySet(Set<Entry<K, V>> entrySet) {
        return new UnmodifiableEntrySet(Collections.unmodifiableSet(entrySet));
    }

    static <K, V> Entry<K, V> unmodifiableEntry(final Entry<K, V> entry) {
        Preconditions.checkNotNull(entry);
        return new AbstractMapEntry<K, V>() {
            public K getKey() {
                return entry.getKey();
            }

            public V getValue() {
                return entry.getValue();
            }
        };
    }

    public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> bimap) {
        return Synchronized.biMap(bimap, null);
    }

    public static <K, V> BiMap<K, V> unmodifiableBiMap(BiMap<? extends K, ? extends V> bimap) {
        return new UnmodifiableBiMap(bimap, null);
    }

    public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> fromMap, Function<? super V1, V2> function) {
        return transformEntries((Map) fromMap, asEntryTransformer(function));
    }

    public static <K, V1, V2> SortedMap<K, V2> transformValues(SortedMap<K, V1> fromMap, Function<? super V1, V2> function) {
        return transformEntries((SortedMap) fromMap, asEntryTransformer(function));
    }

    @GwtIncompatible("NavigableMap")
    public static <K, V1, V2> NavigableMap<K, V2> transformValues(NavigableMap<K, V1> fromMap, Function<? super V1, V2> function) {
        return transformEntries((NavigableMap) fromMap, asEntryTransformer(function));
    }

    private static <K, V1, V2> EntryTransformer<K, V1, V2> asEntryTransformer(final Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        return new EntryTransformer<K, V1, V2>() {
            public V2 transformEntry(K k, V1 value) {
                return function.apply(value);
            }
        };
    }

    public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
        if (fromMap instanceof SortedMap) {
            return transformEntries((SortedMap) fromMap, (EntryTransformer) transformer);
        }
        return new TransformedEntriesMap(fromMap, transformer);
    }

    public static <K, V1, V2> SortedMap<K, V2> transformEntries(SortedMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
        return Platform.mapsTransformEntriesSortedMap(fromMap, transformer);
    }

    @GwtIncompatible("NavigableMap")
    public static <K, V1, V2> NavigableMap<K, V2> transformEntries(NavigableMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
        return new TransformedEntriesNavigableMap(fromMap, transformer);
    }

    static <K, V1, V2> SortedMap<K, V2> transformEntriesIgnoreNavigable(SortedMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
        return new TransformedEntriesSortedMap(fromMap, transformer);
    }

    public static <K, V> Map<K, V> filterKeys(Map<K, V> unfiltered, Predicate<? super K> keyPredicate) {
        if (unfiltered instanceof SortedMap) {
            return filterKeys((SortedMap) unfiltered, (Predicate) keyPredicate);
        }
        if (unfiltered instanceof BiMap) {
            return filterKeys((BiMap) unfiltered, (Predicate) keyPredicate);
        }
        Preconditions.checkNotNull(keyPredicate);
        Predicate entryPredicate = new KeyPredicate(keyPredicate);
        return unfiltered instanceof AbstractFilteredMap ? filterFiltered((AbstractFilteredMap) unfiltered, entryPredicate) : new FilteredKeyMap((Map) Preconditions.checkNotNull(unfiltered), keyPredicate, entryPredicate);
    }

    public static <K, V> SortedMap<K, V> filterKeys(SortedMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
        return filterEntries((SortedMap) unfiltered, new KeyPredicate(keyPredicate));
    }

    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> filterKeys(NavigableMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
        return filterEntries((NavigableMap) unfiltered, new KeyPredicate(keyPredicate));
    }

    public static <K, V> BiMap<K, V> filterKeys(BiMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
        Preconditions.checkNotNull(keyPredicate);
        return filterEntries((BiMap) unfiltered, new KeyPredicate(keyPredicate));
    }

    public static <K, V> Map<K, V> filterValues(Map<K, V> unfiltered, Predicate<? super V> valuePredicate) {
        if (unfiltered instanceof SortedMap) {
            return filterValues((SortedMap) unfiltered, (Predicate) valuePredicate);
        }
        if (unfiltered instanceof BiMap) {
            return filterValues((BiMap) unfiltered, (Predicate) valuePredicate);
        }
        return filterEntries((Map) unfiltered, new ValuePredicate(valuePredicate));
    }

    public static <K, V> SortedMap<K, V> filterValues(SortedMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
        return filterEntries((SortedMap) unfiltered, new ValuePredicate(valuePredicate));
    }

    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> filterValues(NavigableMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
        return filterEntries((NavigableMap) unfiltered, new ValuePredicate(valuePredicate));
    }

    public static <K, V> BiMap<K, V> filterValues(BiMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
        return filterEntries((BiMap) unfiltered, new ValuePredicate(valuePredicate));
    }

    public static <K, V> Map<K, V> filterEntries(Map<K, V> unfiltered, Predicate<? super Entry<K, V>> entryPredicate) {
        if (unfiltered instanceof SortedMap) {
            return filterEntries((SortedMap) unfiltered, (Predicate) entryPredicate);
        }
        if (unfiltered instanceof BiMap) {
            return filterEntries((BiMap) unfiltered, (Predicate) entryPredicate);
        }
        Preconditions.checkNotNull(entryPredicate);
        return unfiltered instanceof AbstractFilteredMap ? filterFiltered((AbstractFilteredMap) unfiltered, (Predicate) entryPredicate) : new FilteredEntryMap((Map) Preconditions.checkNotNull(unfiltered), entryPredicate);
    }

    public static <K, V> SortedMap<K, V> filterEntries(SortedMap<K, V> unfiltered, Predicate<? super Entry<K, V>> entryPredicate) {
        return Platform.mapsFilterSortedMap(unfiltered, entryPredicate);
    }

    static <K, V> SortedMap<K, V> filterSortedIgnoreNavigable(SortedMap<K, V> unfiltered, Predicate<? super Entry<K, V>> entryPredicate) {
        Preconditions.checkNotNull(entryPredicate);
        return unfiltered instanceof FilteredEntrySortedMap ? filterFiltered((FilteredEntrySortedMap) unfiltered, (Predicate) entryPredicate) : new FilteredEntrySortedMap((SortedMap) Preconditions.checkNotNull(unfiltered), entryPredicate);
    }

    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> filterEntries(NavigableMap<K, V> unfiltered, Predicate<? super Entry<K, V>> entryPredicate) {
        Preconditions.checkNotNull(entryPredicate);
        return unfiltered instanceof FilteredEntryNavigableMap ? filterFiltered((FilteredEntryNavigableMap) unfiltered, (Predicate) entryPredicate) : new FilteredEntryNavigableMap((NavigableMap) Preconditions.checkNotNull(unfiltered), entryPredicate);
    }

    public static <K, V> BiMap<K, V> filterEntries(BiMap<K, V> unfiltered, Predicate<? super Entry<K, V>> entryPredicate) {
        Preconditions.checkNotNull(unfiltered);
        Preconditions.checkNotNull(entryPredicate);
        return unfiltered instanceof FilteredEntryBiMap ? filterFiltered((FilteredEntryBiMap) unfiltered, (Predicate) entryPredicate) : new FilteredEntryBiMap(unfiltered, entryPredicate);
    }

    private static <K, V> Map<K, V> filterFiltered(AbstractFilteredMap<K, V> map, Predicate<? super Entry<K, V>> entryPredicate) {
        return new FilteredEntryMap(map.unfiltered, Predicates.and(map.predicate, entryPredicate));
    }

    private static <K, V> SortedMap<K, V> filterFiltered(FilteredEntrySortedMap<K, V> map, Predicate<? super Entry<K, V>> entryPredicate) {
        return new FilteredEntrySortedMap(map.sortedMap(), Predicates.and(map.predicate, entryPredicate));
    }

    @GwtIncompatible("NavigableMap")
    private static <K, V> NavigableMap<K, V> filterFiltered(FilteredEntryNavigableMap<K, V> map, Predicate<? super Entry<K, V>> entryPredicate) {
        return new FilteredEntryNavigableMap(map.sortedMap(), Predicates.and(map.predicate, entryPredicate));
    }

    private static <K, V> BiMap<K, V> filterFiltered(FilteredEntryBiMap<K, V> map, Predicate<? super Entry<K, V>> entryPredicate) {
        return new FilteredEntryBiMap(map.unfiltered(), Predicates.and(map.predicate, entryPredicate));
    }

    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(NavigableMap<K, V> map) {
        Preconditions.checkNotNull(map);
        return map instanceof UnmodifiableNavigableMap ? map : new UnmodifiableNavigableMap(map);
    }

    @Nullable
    private static <K, V> Entry<K, V> unmodifiableOrNull(@Nullable Entry<K, V> entry) {
        return entry == null ? null : unmodifiableEntry(entry);
    }

    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(NavigableMap<K, V> navigableMap) {
        return Synchronized.navigableMap(navigableMap);
    }

    static <V> V safeGet(Map<?, V> map, Object key) {
        V v = null;
        Preconditions.checkNotNull(map);
        try {
            v = map.get(key);
        } catch (ClassCastException e) {
        } catch (NullPointerException e2) {
        }
        return v;
    }

    static boolean safeContainsKey(Map<?, ?> map, Object key) {
        boolean z = false;
        Preconditions.checkNotNull(map);
        try {
            z = map.containsKey(key);
        } catch (ClassCastException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    static <V> V safeRemove(Map<?, V> map, Object key) {
        V v = null;
        Preconditions.checkNotNull(map);
        try {
            v = map.remove(key);
        } catch (ClassCastException e) {
        } catch (NullPointerException e2) {
        }
        return v;
    }

    static <K, V> boolean containsEntryImpl(Collection<Entry<K, V>> c, Object o) {
        if (o instanceof Entry) {
            return c.contains(unmodifiableEntry((Entry) o));
        }
        return false;
    }

    static <K, V> boolean removeEntryImpl(Collection<Entry<K, V>> c, Object o) {
        if (o instanceof Entry) {
            return c.remove(unmodifiableEntry((Entry) o));
        }
        return false;
    }

    static boolean equalsImpl(Map<?, ?> map, Object object) {
        if (map == object) {
            return true;
        }
        if (!(object instanceof Map)) {
            return false;
        }
        return map.entrySet().equals(((Map) object).entrySet());
    }

    static String toStringImpl(Map<?, ?> map) {
        StringBuilder sb = Collections2.newStringBuilderForCollection(map.size()).append('{');
        STANDARD_JOINER.appendTo(sb, (Map) map);
        return sb.append('}').toString();
    }

    static <K, V> void putAllImpl(Map<K, V> self, Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            self.put(entry.getKey(), entry.getValue());
        }
    }

    static boolean containsKeyImpl(Map<?, ?> map, @Nullable Object key) {
        return Iterators.contains(keyIterator(map.entrySet().iterator()), key);
    }

    static boolean containsValueImpl(Map<?, ?> map, @Nullable Object value) {
        return Iterators.contains(valueIterator(map.entrySet().iterator()), value);
    }

    static <K, V> Iterator<K> keyIterator(Iterator<Entry<K, V>> entryIterator) {
        return new TransformedIterator<Entry<K, V>, K>(entryIterator) {
            K transform(Entry<K, V> entry) {
                return entry.getKey();
            }
        };
    }

    @Nullable
    static <K> K keyOrNull(@Nullable Entry<K, ?> entry) {
        return entry == null ? null : entry.getKey();
    }

    @Nullable
    static <V> V valueOrNull(@Nullable Entry<?, V> entry) {
        return entry == null ? null : entry.getValue();
    }

    static <K, V> Iterator<V> valueIterator(Iterator<Entry<K, V>> entryIterator) {
        return new TransformedIterator<Entry<K, V>, V>(entryIterator) {
            V transform(Entry<K, V> entry) {
                return entry.getValue();
            }
        };
    }

    static <K, V> UnmodifiableIterator<V> valueIterator(final UnmodifiableIterator<Entry<K, V>> entryIterator) {
        return new UnmodifiableIterator<V>() {
            public boolean hasNext() {
                return entryIterator.hasNext();
            }

            public V next() {
                return ((Entry) entryIterator.next()).getValue();
            }
        };
    }
}
