package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
class FilteredEntryMultimap<K, V> extends FilteredMultimap<K, V> {
    final Predicate<? super Entry<K, V>> predicate;

    class AsMap extends AbstractMap<K, Collection<V>> {
        private Set<K> keySet;

        /* renamed from: com.google.common.collect.FilteredEntryMultimap$AsMap$3 */
        class C03563 extends Values<K, Collection<V>> {
            C03563() {
            }

            Map<K, Collection<V>> map() {
                return AsMap.this;
            }

            public boolean remove(@Nullable Object o) {
                if (o instanceof Collection) {
                    Collection<?> c = (Collection) o;
                    Iterator<Entry<K, Collection<V>>> entryIterator = FilteredEntryMultimap.this.unfiltered.asMap().entrySet().iterator();
                    while (entryIterator.hasNext()) {
                        Entry<K, Collection<V>> entry = (Entry) entryIterator.next();
                        Collection<V> collection = FilteredEntryMultimap.filterCollection((Collection) entry.getValue(), new ValuePredicate(entry.getKey()));
                        if (!collection.isEmpty() && c.equals(collection)) {
                            if (collection.size() == ((Collection) entry.getValue()).size()) {
                                entryIterator.remove();
                            } else {
                                collection.clear();
                            }
                            return true;
                        }
                    }
                }
                return false;
            }

            public boolean removeAll(Collection<?> c) {
                return FilteredEntryMultimap.this.removeIf(Predicates.compose(Predicates.in(c), Maps.valueFunction()));
            }

            public boolean retainAll(Collection<?> c) {
                return FilteredEntryMultimap.this.removeIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.valueFunction()));
            }
        }

        /* renamed from: com.google.common.collect.FilteredEntryMultimap$AsMap$1 */
        class C05671 extends KeySet<K, Collection<V>> {
            C05671() {
            }

            Map<K, Collection<V>> map() {
                return AsMap.this;
            }

            public boolean removeAll(Collection<?> c) {
                return FilteredEntryMultimap.this.removeIf(Predicates.compose(Predicates.in(c), Maps.keyFunction()));
            }

            public boolean retainAll(Collection<?> c) {
                return FilteredEntryMultimap.this.removeIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.keyFunction()));
            }

            public boolean remove(@Nullable Object o) {
                return AsMap.this.remove(o) != null;
            }
        }

        /* renamed from: com.google.common.collect.FilteredEntryMultimap$AsMap$2 */
        class C05692 extends EntrySet<K, Collection<V>> {

            /* renamed from: com.google.common.collect.FilteredEntryMultimap$AsMap$2$1 */
            class C05681 extends AbstractIterator<Entry<K, Collection<V>>> {
                final Iterator<Entry<K, Collection<V>>> backingIterator = FilteredEntryMultimap.this.unfiltered.asMap().entrySet().iterator();

                C05681() {
                }

                protected Entry<K, Collection<V>> computeNext() {
                    while (this.backingIterator.hasNext()) {
                        Entry<K, Collection<V>> entry = (Entry) this.backingIterator.next();
                        K key = entry.getKey();
                        Collection<V> collection = FilteredEntryMultimap.filterCollection((Collection) entry.getValue(), new ValuePredicate(key));
                        if (!collection.isEmpty()) {
                            return Maps.immutableEntry(key, collection);
                        }
                    }
                    return (Entry) endOfData();
                }
            }

            C05692() {
            }

            Map<K, Collection<V>> map() {
                return AsMap.this;
            }

            public Iterator<Entry<K, Collection<V>>> iterator() {
                return new C05681();
            }

            public boolean removeAll(Collection<?> c) {
                return FilteredEntryMultimap.this.removeIf(Predicates.in(c));
            }

            public boolean retainAll(Collection<?> c) {
                return FilteredEntryMultimap.this.removeIf(Predicates.not(Predicates.in(c)));
            }

            public int size() {
                return Iterators.size(iterator());
            }
        }

        AsMap() {
        }

        public boolean containsKey(@Nullable Object key) {
            return get(key) != null;
        }

        public void clear() {
            FilteredEntryMultimap.this.clear();
        }

        public Collection<V> get(@Nullable Object key) {
            Collection<V> result = (Collection) FilteredEntryMultimap.this.unfiltered.asMap().get(key);
            if (result == null) {
                return null;
            }
            result = FilteredEntryMultimap.filterCollection(result, new ValuePredicate(key));
            if (result.isEmpty()) {
                return null;
            }
            return result;
        }

        public Collection<V> remove(@Nullable Object key) {
            Collection<V> collection = (Collection) FilteredEntryMultimap.this.unfiltered.asMap().get(key);
            if (collection == null) {
                return null;
            }
            K k = key;
            List<V> result = Lists.newArrayList();
            Iterator<V> itr = collection.iterator();
            while (itr.hasNext()) {
                V v = itr.next();
                if (FilteredEntryMultimap.this.satisfies(k, v)) {
                    itr.remove();
                    result.add(v);
                }
            }
            if (result.isEmpty()) {
                return null;
            }
            if (FilteredEntryMultimap.this.unfiltered instanceof SetMultimap) {
                return Collections.unmodifiableSet(Sets.newLinkedHashSet(result));
            }
            return Collections.unmodifiableList(result);
        }

        public Set<K> keySet() {
            Set<K> set = this.keySet;
            if (set != null) {
                return set;
            }
            set = new C05671();
            this.keySet = set;
            return set;
        }

        public Set<Entry<K, Collection<V>>> entrySet() {
            return new C05692();
        }

        public Collection<Collection<V>> values() {
            return new C03563();
        }
    }

    final class ValuePredicate implements Predicate<V> {
        private final K key;

        ValuePredicate(K key) {
            this.key = key;
        }

        public boolean apply(@Nullable V value) {
            return FilteredEntryMultimap.this.satisfies(this.key, value);
        }
    }

    class Keys extends Keys<K, V> {

        /* renamed from: com.google.common.collect.FilteredEntryMultimap$Keys$1 */
        class C05701 extends EntrySet<K> {
            C05701() {
            }

            Multiset<K> multiset() {
                return Keys.this;
            }

            public Iterator<Multiset.Entry<K>> iterator() {
                return Keys.this.entryIterator();
            }

            public int size() {
                return FilteredEntryMultimap.this.keySet().size();
            }

            private boolean removeIf(final Predicate<? super Multiset.Entry<K>> predicate) {
                return FilteredEntryMultimap.this.removeIf(new Predicate<Entry<K, Collection<V>>>() {
                    public boolean apply(Entry<K, Collection<V>> entry) {
                        return predicate.apply(Multisets.immutableEntry(entry.getKey(), ((Collection) entry.getValue()).size()));
                    }
                });
            }

            public boolean removeAll(Collection<?> c) {
                return removeIf(Predicates.in(c));
            }

            public boolean retainAll(Collection<?> c) {
                return removeIf(Predicates.not(Predicates.in(c)));
            }
        }

        Keys() {
            super(FilteredEntryMultimap.this);
        }

        public int remove(@Nullable Object key, int occurrences) {
            Multisets.checkNonnegative(occurrences, "occurrences");
            if (occurrences == 0) {
                return count(key);
            }
            Collection<V> collection = (Collection) FilteredEntryMultimap.this.unfiltered.asMap().get(key);
            if (collection == null) {
                return 0;
            }
            K k = key;
            int oldCount = 0;
            Iterator<V> itr = collection.iterator();
            while (itr.hasNext()) {
                if (FilteredEntryMultimap.this.satisfies(k, itr.next())) {
                    oldCount++;
                    if (oldCount <= occurrences) {
                        itr.remove();
                    }
                }
            }
            return oldCount;
        }

        public Set<Multiset.Entry<K>> entrySet() {
            return new C05701();
        }
    }

    FilteredEntryMultimap(Multimap<K, V> unfiltered, Predicate<? super Entry<K, V>> predicate) {
        super(unfiltered);
        this.predicate = (Predicate) Preconditions.checkNotNull(predicate);
    }

    Predicate<? super Entry<K, V>> entryPredicate() {
        return this.predicate;
    }

    public int size() {
        return entries().size();
    }

    private boolean satisfies(K key, V value) {
        return this.predicate.apply(Maps.immutableEntry(key, value));
    }

    static <E> Collection<E> filterCollection(Collection<E> collection, Predicate<? super E> predicate) {
        if (collection instanceof Set) {
            return Sets.filter((Set) collection, (Predicate) predicate);
        }
        return Collections2.filter(collection, predicate);
    }

    public boolean containsKey(@Nullable Object key) {
        return asMap().get(key) != null;
    }

    public Collection<V> removeAll(@Nullable Object key) {
        return (Collection) Objects.firstNonNull(asMap().remove(key), unmodifiableEmptyCollection());
    }

    Collection<V> unmodifiableEmptyCollection() {
        return this.unfiltered instanceof SetMultimap ? Collections.emptySet() : Collections.emptyList();
    }

    public void clear() {
        entries().clear();
    }

    public Collection<V> get(K key) {
        return filterCollection(this.unfiltered.get(key), new ValuePredicate(key));
    }

    Collection<Entry<K, V>> createEntries() {
        return filterCollection(this.unfiltered.entries(), this.predicate);
    }

    Iterator<Entry<K, V>> entryIterator() {
        throw new AssertionError("should never be called");
    }

    Map<K, Collection<V>> createAsMap() {
        return new AsMap();
    }

    public Set<K> keySet() {
        return asMap().keySet();
    }

    boolean removeIf(Predicate<? super Entry<K, Collection<V>>> predicate) {
        Iterator<Entry<K, Collection<V>>> entryIterator = this.unfiltered.asMap().entrySet().iterator();
        boolean changed = false;
        while (entryIterator.hasNext()) {
            Entry<K, Collection<V>> entry = (Entry) entryIterator.next();
            K key = entry.getKey();
            Collection<V> collection = filterCollection((Collection) entry.getValue(), new ValuePredicate(key));
            if (!collection.isEmpty() && predicate.apply(Maps.immutableEntry(key, collection))) {
                if (collection.size() == ((Collection) entry.getValue()).size()) {
                    entryIterator.remove();
                } else {
                    collection.clear();
                }
                changed = true;
            }
        }
        return changed;
    }

    Multiset<K> createKeys() {
        return new Keys();
    }
}
