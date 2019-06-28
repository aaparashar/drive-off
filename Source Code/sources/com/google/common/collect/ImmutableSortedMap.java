package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.SortedMap;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V> implements NavigableMap<K, V> {
    private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new EmptyImmutableSortedMap(NATURAL_ORDER);
    private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
    private static final long serialVersionUID = 0;
    private transient ImmutableSortedMap<K, V> descendingMap;

    public static class Builder<K, V> extends com.google.common.collect.ImmutableMap.Builder<K, V> {
        private final Comparator<? super K> comparator;

        public Builder(Comparator<? super K> comparator) {
            this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
        }

        public Builder<K, V> put(K key, V value) {
            this.entries.add(ImmutableMap.entryOf(key, value));
            return this;
        }

        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            super.put(entry);
            return this;
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public ImmutableSortedMap<K, V> build() {
            ImmutableSortedMap.sortEntries(this.entries, this.comparator);
            ImmutableSortedMap.validateEntries(this.entries, this.comparator);
            return ImmutableSortedMap.fromSortedEntries(this.comparator, this.entries);
        }
    }

    private static class SerializedForm extends SerializedForm {
        private static final long serialVersionUID = 0;
        private final Comparator<Object> comparator;

        SerializedForm(ImmutableSortedMap<?, ?> sortedMap) {
            super(sortedMap);
            this.comparator = sortedMap.comparator();
        }

        Object readResolve() {
            return createMap(new Builder(this.comparator));
        }
    }

    abstract ImmutableSortedMap<K, V> createDescendingMap();

    public abstract ImmutableSortedMap<K, V> headMap(K k, boolean z);

    public abstract ImmutableSortedSet<K> keySet();

    public abstract ImmutableSortedMap<K, V> tailMap(K k, boolean z);

    public abstract ImmutableCollection<V> values();

    static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
        if (Ordering.natural().equals(comparator)) {
            return of();
        }
        return new EmptyImmutableSortedMap(comparator);
    }

    static <K, V> ImmutableSortedMap<K, V> fromSortedEntries(Comparator<? super K> comparator, Collection<? extends Entry<? extends K, ? extends V>> entries) {
        if (entries.isEmpty()) {
            return emptyMap(comparator);
        }
        com.google.common.collect.ImmutableList.Builder<K> keyBuilder = ImmutableList.builder();
        com.google.common.collect.ImmutableList.Builder<V> valueBuilder = ImmutableList.builder();
        for (Entry<? extends K, ? extends V> entry : entries) {
            keyBuilder.add(entry.getKey());
            valueBuilder.add(entry.getValue());
        }
        return new RegularImmutableSortedMap(new RegularImmutableSortedSet(keyBuilder.build(), comparator), valueBuilder.build());
    }

    static <K, V> ImmutableSortedMap<K, V> from(ImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
        if (keySet.isEmpty()) {
            return emptyMap(keySet.comparator());
        }
        return new RegularImmutableSortedMap((RegularImmutableSortedSet) keySet, valueList);
    }

    public static <K, V> ImmutableSortedMap<K, V> of() {
        return NATURAL_EMPTY_MAP;
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1) {
        return from(ImmutableSortedSet.of(k1), ImmutableList.of(v1));
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2) {
        return new Builder(Ordering.natural()).put((Object) k1, (Object) v1).put((Object) k2, (Object) v2).build();
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return new Builder(Ordering.natural()).put((Object) k1, (Object) v1).put((Object) k2, (Object) v2).put((Object) k3, (Object) v3).build();
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return new Builder(Ordering.natural()).put((Object) k1, (Object) v1).put((Object) k2, (Object) v2).put((Object) k3, (Object) v3).put((Object) k4, (Object) v4).build();
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return new Builder(Ordering.natural()).put((Object) k1, (Object) v1).put((Object) k2, (Object) v2).put((Object) k3, (Object) v3).put((Object) k4, (Object) v4).put((Object) k5, (Object) v5).build();
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        return copyOfInternal(map, Ordering.natural());
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        return copyOfInternal(map, (Comparator) Preconditions.checkNotNull(comparator));
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> map) {
        Comparator<? super K> comparator = map.comparator();
        if (comparator == null) {
            comparator = NATURAL_ORDER;
        }
        return copyOfInternal(map, comparator);
    }

    private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        boolean sameComparator = false;
        if (map instanceof SortedMap) {
            Comparator<?> comparator2 = ((SortedMap) map).comparator();
            if (comparator2 != null) {
                sameComparator = comparator.equals(comparator2);
            } else if (comparator == NATURAL_ORDER) {
                sameComparator = true;
            } else {
                sameComparator = false;
            }
        }
        if (sameComparator && (map instanceof ImmutableSortedMap)) {
            ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap) map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        }
        Entry[] entries = (Entry[]) map.entrySet().toArray(new Entry[0]);
        for (int i = 0; i < entries.length; i++) {
            Entry<K, V> entry = entries[i];
            entries[i] = ImmutableMap.entryOf(entry.getKey(), entry.getValue());
        }
        List<Entry<K, V>> list = Arrays.asList(entries);
        if (!sameComparator) {
            sortEntries(list, comparator);
            validateEntries(list, comparator);
        }
        return fromSortedEntries(comparator, list);
    }

    private static <K, V> void sortEntries(List<Entry<K, V>> entries, final Comparator<? super K> comparator) {
        Collections.sort(entries, new Comparator<Entry<K, V>>() {
            public int compare(Entry<K, V> entry1, Entry<K, V> entry2) {
                return comparator.compare(entry1.getKey(), entry2.getKey());
            }
        });
    }

    private static <K, V> void validateEntries(List<Entry<K, V>> entries, Comparator<? super K> comparator) {
        for (int i = 1; i < entries.size(); i++) {
            if (comparator.compare(((Entry) entries.get(i - 1)).getKey(), ((Entry) entries.get(i)).getKey()) == 0) {
                throw new IllegalArgumentException("Duplicate keys in mappings " + entries.get(i - 1) + " and " + entries.get(i));
            }
        }
    }

    public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() {
        return new Builder(Ordering.natural());
    }

    public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) {
        return new Builder(comparator);
    }

    public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() {
        return new Builder(Ordering.natural().reverse());
    }

    ImmutableSortedMap() {
    }

    ImmutableSortedMap(ImmutableSortedMap<K, V> descendingMap) {
        this.descendingMap = descendingMap;
    }

    public int size() {
        return values().size();
    }

    public boolean containsValue(@Nullable Object value) {
        return values().contains(value);
    }

    boolean isPartialView() {
        return keySet().isPartialView() || values().isPartialView();
    }

    public ImmutableSet<Entry<K, V>> entrySet() {
        return super.entrySet();
    }

    public Comparator<? super K> comparator() {
        return keySet().comparator();
    }

    public K firstKey() {
        return keySet().first();
    }

    public K lastKey() {
        return keySet().last();
    }

    public ImmutableSortedMap<K, V> headMap(K toKey) {
        return headMap((Object) toKey, false);
    }

    public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) {
        return subMap((Object) fromKey, true, (Object) toKey, false);
    }

    public ImmutableSortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        boolean z;
        Preconditions.checkNotNull(fromKey);
        Preconditions.checkNotNull(toKey);
        if (comparator().compare(fromKey, toKey) <= 0) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "expected fromKey <= toKey but %s > %s", fromKey, toKey);
        return headMap((Object) toKey, toInclusive).tailMap((Object) fromKey, fromInclusive);
    }

    public ImmutableSortedMap<K, V> tailMap(K fromKey) {
        return tailMap((Object) fromKey, true);
    }

    public Entry<K, V> lowerEntry(K key) {
        return headMap((Object) key, false).lastEntry();
    }

    public K lowerKey(K key) {
        return Maps.keyOrNull(lowerEntry(key));
    }

    public Entry<K, V> floorEntry(K key) {
        return headMap((Object) key, true).lastEntry();
    }

    public K floorKey(K key) {
        return Maps.keyOrNull(floorEntry(key));
    }

    public Entry<K, V> ceilingEntry(K key) {
        return tailMap((Object) key, true).firstEntry();
    }

    public K ceilingKey(K key) {
        return Maps.keyOrNull(ceilingEntry(key));
    }

    public Entry<K, V> higherEntry(K key) {
        return tailMap((Object) key, false).firstEntry();
    }

    public K higherKey(K key) {
        return Maps.keyOrNull(higherEntry(key));
    }

    public Entry<K, V> firstEntry() {
        return isEmpty() ? null : (Entry) entrySet().asList().get(0);
    }

    public Entry<K, V> lastEntry() {
        return isEmpty() ? null : (Entry) entrySet().asList().get(size() - 1);
    }

    @Deprecated
    public final Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    public ImmutableSortedMap<K, V> descendingMap() {
        ImmutableSortedMap<K, V> result = this.descendingMap;
        if (result != null) {
            return result;
        }
        result = createDescendingMap();
        this.descendingMap = result;
        return result;
    }

    public ImmutableSortedSet<K> navigableKeySet() {
        return keySet();
    }

    public ImmutableSortedSet<K> descendingKeySet() {
        return keySet().descendingSet();
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
