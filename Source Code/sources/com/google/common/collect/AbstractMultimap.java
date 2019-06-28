package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMultimap<K, V> implements Multimap<K, V> {
    private transient Map<K, Collection<V>> asMap;
    private transient Collection<Entry<K, V>> entries;
    private transient Set<K> keySet;
    private transient Multiset<K> keys;
    private transient Collection<V> values;

    /* renamed from: com.google.common.collect.AbstractMultimap$2 */
    class C03472 extends Entries<K, V> {
        C03472() {
        }

        Multimap<K, V> multimap() {
            return AbstractMultimap.this;
        }

        public Iterator<Entry<K, V>> iterator() {
            return AbstractMultimap.this.entryIterator();
        }
    }

    /* renamed from: com.google.common.collect.AbstractMultimap$1 */
    class C05581 extends EntrySet<K, V> {
        C05581() {
        }

        Multimap<K, V> multimap() {
            return AbstractMultimap.this;
        }

        public Iterator<Entry<K, V>> iterator() {
            return AbstractMultimap.this.entryIterator();
        }
    }

    /* renamed from: com.google.common.collect.AbstractMultimap$3 */
    class C05593 extends KeySet<K, Collection<V>> {
        C05593() {
        }

        Map<K, Collection<V>> map() {
            return AbstractMultimap.this.asMap();
        }
    }

    abstract Map<K, Collection<V>> createAsMap();

    abstract Iterator<Entry<K, V>> entryIterator();

    AbstractMultimap() {
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsValue(@Nullable Object value) {
        for (Collection<V> collection : asMap().values()) {
            if (collection.contains(value)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
        Collection<V> collection = (Collection) asMap().get(key);
        return collection != null && collection.contains(value);
    }

    public boolean remove(@Nullable Object key, @Nullable Object value) {
        Collection<V> collection = (Collection) asMap().get(key);
        return collection != null && collection.remove(value);
    }

    public boolean put(@Nullable K key, @Nullable V value) {
        return get(key).add(value);
    }

    public boolean putAll(@Nullable K key, Iterable<? extends V> values) {
        Preconditions.checkNotNull(values);
        return values.iterator().hasNext() && Iterables.addAll(get(key), values);
    }

    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        boolean changed = false;
        for (Entry<? extends K, ? extends V> entry : multimap.entries()) {
            changed |= put(entry.getKey(), entry.getValue());
        }
        return changed;
    }

    public Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
        Preconditions.checkNotNull(values);
        Collection<V> result = removeAll(key);
        putAll(key, values);
        return result;
    }

    public Collection<Entry<K, V>> entries() {
        Collection<Entry<K, V>> collection = this.entries;
        if (collection != null) {
            return collection;
        }
        collection = createEntries();
        this.entries = collection;
        return collection;
    }

    Collection<Entry<K, V>> createEntries() {
        return this instanceof SetMultimap ? new C05581() : new C03472();
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
        return new C05593();
    }

    public Multiset<K> keys() {
        Multiset<K> multiset = this.keys;
        if (multiset != null) {
            return multiset;
        }
        multiset = createKeys();
        this.keys = multiset;
        return multiset;
    }

    Multiset<K> createKeys() {
        return new Keys(this);
    }

    public Collection<V> values() {
        Collection<V> collection = this.values;
        if (collection != null) {
            return collection;
        }
        collection = createValues();
        this.values = collection;
        return collection;
    }

    Collection<V> createValues() {
        return new Values(this);
    }

    public Map<K, Collection<V>> asMap() {
        Map<K, Collection<V>> map = this.asMap;
        if (map != null) {
            return map;
        }
        map = createAsMap();
        this.asMap = map;
        return map;
    }

    public boolean equals(@Nullable Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Multimap)) {
            return false;
        }
        return asMap().equals(((Multimap) object).asMap());
    }

    public int hashCode() {
        return asMap().hashCode();
    }

    public String toString() {
        return asMap().toString();
    }
}
