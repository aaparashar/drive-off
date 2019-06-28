package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
class FilteredKeyMultimap<K, V> extends FilteredMultimap<K, V> {
    final Predicate<? super K> keyPredicate;

    /* renamed from: com.google.common.collect.FilteredKeyMultimap$1 */
    class C03581 extends Entries<K, V> {
        C03581() {
        }

        Multimap<K, V> multimap() {
            return FilteredKeyMultimap.this;
        }

        public Iterator<Entry<K, V>> iterator() {
            return FilteredKeyMultimap.this.entryIterator();
        }

        public boolean remove(@Nullable Object o) {
            if (o instanceof Entry) {
                Entry<?, ?> entry = (Entry) o;
                if (FilteredKeyMultimap.this.unfiltered.containsEntry(entry.getKey(), entry.getValue()) && FilteredKeyMultimap.this.keyPredicate.apply(entry.getKey())) {
                    return FilteredKeyMultimap.this.unfiltered.remove(entry.getKey(), entry.getValue());
                }
            }
            return false;
        }

        public boolean removeAll(Collection<?> c) {
            return Iterators.removeIf(FilteredKeyMultimap.this.unfiltered.entries().iterator(), Predicates.and(Predicates.compose(FilteredKeyMultimap.this.keyPredicate, Maps.keyFunction()), Predicates.in(c)));
        }

        public boolean retainAll(Collection<?> c) {
            return Iterators.removeIf(FilteredKeyMultimap.this.unfiltered.entries().iterator(), Predicates.and(Predicates.compose(FilteredKeyMultimap.this.keyPredicate, Maps.keyFunction()), Predicates.not(Predicates.in(c))));
        }
    }

    static class AddRejectingList<K, V> extends ForwardingList<V> {
        final K key;

        AddRejectingList(K key) {
            this.key = key;
        }

        public boolean add(V v) {
            add(0, v);
            return true;
        }

        public boolean addAll(Collection<? extends V> collection) {
            addAll(0, collection);
            return true;
        }

        public void add(int index, V v) {
            Preconditions.checkPositionIndex(index, 0);
            throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
        }

        public boolean addAll(int index, Collection<? extends V> elements) {
            Preconditions.checkNotNull(elements);
            Preconditions.checkPositionIndex(index, 0);
            throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
        }

        protected List<V> delegate() {
            return Collections.emptyList();
        }
    }

    static class AddRejectingSet<K, V> extends ForwardingSet<V> {
        final K key;

        AddRejectingSet(K key) {
            this.key = key;
        }

        public boolean add(V v) {
            throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
        }

        public boolean addAll(Collection<? extends V> collection) {
            Preconditions.checkNotNull(collection);
            throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
        }

        protected Set<V> delegate() {
            return Collections.emptySet();
        }
    }

    FilteredKeyMultimap(Multimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
        super(unfiltered);
        this.keyPredicate = (Predicate) Preconditions.checkNotNull(keyPredicate);
    }

    Predicate<? super Entry<K, V>> entryPredicate() {
        return Predicates.compose(this.keyPredicate, Maps.keyFunction());
    }

    public int size() {
        int size = 0;
        for (Collection<V> collection : asMap().values()) {
            size += collection.size();
        }
        return size;
    }

    public boolean containsKey(@Nullable Object key) {
        if (!this.unfiltered.containsKey(key)) {
            return false;
        }
        return this.keyPredicate.apply(key);
    }

    public Collection<V> removeAll(Object key) {
        return containsKey(key) ? this.unfiltered.removeAll(key) : unmodifiableEmptyCollection();
    }

    Collection<V> unmodifiableEmptyCollection() {
        if (this.unfiltered instanceof SetMultimap) {
            return ImmutableSet.of();
        }
        return ImmutableList.of();
    }

    public void clear() {
        keySet().clear();
    }

    Set<K> createKeySet() {
        return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
    }

    public Collection<V> get(K key) {
        if (this.keyPredicate.apply(key)) {
            return this.unfiltered.get(key);
        }
        if (this.unfiltered instanceof SetMultimap) {
            return new AddRejectingSet(key);
        }
        return new AddRejectingList(key);
    }

    Iterator<Entry<K, V>> entryIterator() {
        return Iterators.filter(this.unfiltered.entries().iterator(), Predicates.compose(this.keyPredicate, Maps.keyFunction()));
    }

    Collection<Entry<K, V>> createEntries() {
        return new C03581();
    }

    Map<K, Collection<V>> createAsMap() {
        return Maps.filterKeys(this.unfiltered.asMap(), this.keyPredicate);
    }

    Multiset<K> createKeys() {
        return Multisets.filter(this.unfiltered.keys(), this.keyPredicate);
    }
}
