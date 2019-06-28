package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public final class LinkedHashMultimap<K, V> extends AbstractSetMultimap<K, V> {
    private static final int DEFAULT_KEY_CAPACITY = 16;
    private static final int DEFAULT_VALUE_SET_CAPACITY = 2;
    @VisibleForTesting
    static final double VALUE_SET_LOAD_FACTOR = 1.0d;
    @GwtIncompatible("java serialization not supported")
    private static final long serialVersionUID = 1;
    private transient ValueEntry<K, V> multimapHeaderEntry;
    @VisibleForTesting
    transient int valueSetCapacity = 2;

    /* renamed from: com.google.common.collect.LinkedHashMultimap$1 */
    class C01941 implements Iterator<Entry<K, V>> {
        ValueEntry<K, V> nextEntry = LinkedHashMultimap.this.multimapHeaderEntry.successorInMultimap;
        ValueEntry<K, V> toRemove;

        C01941() {
        }

        public boolean hasNext() {
            return this.nextEntry != LinkedHashMultimap.this.multimapHeaderEntry;
        }

        public Entry<K, V> next() {
            if (hasNext()) {
                ValueEntry<K, V> result = this.nextEntry;
                this.toRemove = result;
                this.nextEntry = this.nextEntry.successorInMultimap;
                return result;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            Iterators.checkRemove(this.toRemove != null);
            LinkedHashMultimap.this.remove(this.toRemove.getKey(), this.toRemove.getValue());
            this.toRemove = null;
        }
    }

    private interface ValueSetLink<K, V> {
        ValueSetLink<K, V> getPredecessorInValueSet();

        ValueSetLink<K, V> getSuccessorInValueSet();

        void setPredecessorInValueSet(ValueSetLink<K, V> valueSetLink);

        void setSuccessorInValueSet(ValueSetLink<K, V> valueSetLink);
    }

    @VisibleForTesting
    static final class ValueEntry<K, V> extends AbstractMapEntry<K, V> implements ValueSetLink<K, V> {
        final K key;
        @Nullable
        ValueEntry<K, V> nextInValueSetHashRow;
        ValueEntry<K, V> predecessorInMultimap;
        ValueSetLink<K, V> predecessorInValueSet;
        ValueEntry<K, V> successorInMultimap;
        ValueSetLink<K, V> successorInValueSet;
        final V value;
        final int valueHash;

        ValueEntry(@Nullable K key, @Nullable V value, int valueHash, @Nullable ValueEntry<K, V> nextInValueSetHashRow) {
            this.key = key;
            this.value = value;
            this.valueHash = valueHash;
            this.nextInValueSetHashRow = nextInValueSetHashRow;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public ValueSetLink<K, V> getPredecessorInValueSet() {
            return this.predecessorInValueSet;
        }

        public ValueSetLink<K, V> getSuccessorInValueSet() {
            return this.successorInValueSet;
        }

        public void setPredecessorInValueSet(ValueSetLink<K, V> entry) {
            this.predecessorInValueSet = entry;
        }

        public void setSuccessorInValueSet(ValueSetLink<K, V> entry) {
            this.successorInValueSet = entry;
        }

        public ValueEntry<K, V> getPredecessorInMultimap() {
            return this.predecessorInMultimap;
        }

        public ValueEntry<K, V> getSuccessorInMultimap() {
            return this.successorInMultimap;
        }

        public void setSuccessorInMultimap(ValueEntry<K, V> multimapSuccessor) {
            this.successorInMultimap = multimapSuccessor;
        }

        public void setPredecessorInMultimap(ValueEntry<K, V> multimapPredecessor) {
            this.predecessorInMultimap = multimapPredecessor;
        }
    }

    @VisibleForTesting
    final class ValueSet extends ImprovedAbstractSet<V> implements ValueSetLink<K, V> {
        private ValueSetLink<K, V> firstEntry;
        @VisibleForTesting
        ValueEntry<K, V>[] hashTable;
        private final K key;
        private ValueSetLink<K, V> lastEntry;
        private int modCount = 0;
        private int size = 0;

        /* renamed from: com.google.common.collect.LinkedHashMultimap$ValueSet$1 */
        class C01951 implements Iterator<V> {
            int expectedModCount = ValueSet.this.modCount;
            ValueSetLink<K, V> nextEntry = ValueSet.this.firstEntry;
            ValueEntry<K, V> toRemove;

            C01951() {
            }

            private void checkForComodification() {
                if (ValueSet.this.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }

            public boolean hasNext() {
                checkForComodification();
                return this.nextEntry != ValueSet.this;
            }

            public V next() {
                if (hasNext()) {
                    ValueEntry<K, V> entry = this.nextEntry;
                    V result = entry.getValue();
                    this.toRemove = entry;
                    this.nextEntry = entry.getSuccessorInValueSet();
                    return result;
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                boolean z;
                checkForComodification();
                if (this.toRemove != null) {
                    z = true;
                } else {
                    z = false;
                }
                Iterators.checkRemove(z);
                Object o = this.toRemove.getValue();
                int row = Hashing.smear(o == null ? 0 : o.hashCode()) & (ValueSet.this.hashTable.length - 1);
                ValueEntry<K, V> prev = null;
                for (ValueEntry<K, V> entry = ValueSet.this.hashTable[row]; entry != null; entry = entry.nextInValueSetHashRow) {
                    if (entry == this.toRemove) {
                        if (prev == null) {
                            ValueSet.this.hashTable[row] = entry.nextInValueSetHashRow;
                        } else {
                            prev.nextInValueSetHashRow = entry.nextInValueSetHashRow;
                        }
                        LinkedHashMultimap.deleteFromValueSet(this.toRemove);
                        LinkedHashMultimap.deleteFromMultimap(this.toRemove);
                        ValueSet.this.size = ValueSet.this.size - 1;
                        this.expectedModCount = ValueSet.access$104(ValueSet.this);
                        this.toRemove = null;
                    }
                    prev = entry;
                }
                this.toRemove = null;
            }
        }

        static /* synthetic */ int access$104(ValueSet x0) {
            int i = x0.modCount + 1;
            x0.modCount = i;
            return i;
        }

        ValueSet(K key, int expectedValues) {
            this.key = key;
            this.firstEntry = this;
            this.lastEntry = this;
            this.hashTable = new ValueEntry[Hashing.closedTableSize(expectedValues, LinkedHashMultimap.VALUE_SET_LOAD_FACTOR)];
        }

        public ValueSetLink<K, V> getPredecessorInValueSet() {
            return this.lastEntry;
        }

        public ValueSetLink<K, V> getSuccessorInValueSet() {
            return this.firstEntry;
        }

        public void setPredecessorInValueSet(ValueSetLink<K, V> entry) {
            this.lastEntry = entry;
        }

        public void setSuccessorInValueSet(ValueSetLink<K, V> entry) {
            this.firstEntry = entry;
        }

        public Iterator<V> iterator() {
            return new C01951();
        }

        public int size() {
            return this.size;
        }

        public boolean contains(@Nullable Object o) {
            int hash = o == null ? 0 : o.hashCode();
            ValueEntry<K, V> entry = this.hashTable[Hashing.smear(hash) & (this.hashTable.length - 1)];
            while (entry != null) {
                if (hash == entry.valueHash && Objects.equal(o, entry.getValue())) {
                    return true;
                }
                entry = entry.nextInValueSetHashRow;
            }
            return false;
        }

        public boolean add(@Nullable V value) {
            int hash = value == null ? 0 : value.hashCode();
            int row = Hashing.smear(hash) & (this.hashTable.length - 1);
            ValueEntry<K, V> rowHead = this.hashTable[row];
            ValueEntry<K, V> entry = rowHead;
            while (entry != null) {
                if (hash == entry.valueHash && Objects.equal(value, entry.getValue())) {
                    return false;
                }
                entry = entry.nextInValueSetHashRow;
            }
            ValueEntry<K, V> newEntry = new ValueEntry(this.key, value, hash, rowHead);
            LinkedHashMultimap.succeedsInValueSet(this.lastEntry, newEntry);
            LinkedHashMultimap.succeedsInValueSet(newEntry, this);
            LinkedHashMultimap.succeedsInMultimap(LinkedHashMultimap.this.multimapHeaderEntry.getPredecessorInMultimap(), newEntry);
            LinkedHashMultimap.succeedsInMultimap(newEntry, LinkedHashMultimap.this.multimapHeaderEntry);
            this.hashTable[row] = newEntry;
            this.size++;
            this.modCount++;
            rehashIfNecessary();
            return true;
        }

        private void rehashIfNecessary() {
            if (Hashing.needsResizing(this.size, this.hashTable.length, LinkedHashMultimap.VALUE_SET_LOAD_FACTOR)) {
                ValueEntry<K, V>[] hashTable = new ValueEntry[(this.hashTable.length * 2)];
                this.hashTable = hashTable;
                int mask = hashTable.length - 1;
                for (ValueEntry<K, V> entry = this.firstEntry; entry != this; entry = entry.getSuccessorInValueSet()) {
                    ValueEntry<K, V> valueEntry = entry;
                    int row = Hashing.smear(valueEntry.valueHash) & mask;
                    valueEntry.nextInValueSetHashRow = hashTable[row];
                    hashTable[row] = valueEntry;
                }
            }
        }

        public boolean remove(@Nullable Object o) {
            int hash = o == null ? 0 : o.hashCode();
            int row = Hashing.smear(hash) & (this.hashTable.length - 1);
            ValueEntry<K, V> prev = null;
            ValueEntry<K, V> entry = this.hashTable[row];
            while (entry != null) {
                if (hash == entry.valueHash && Objects.equal(o, entry.getValue())) {
                    if (prev == null) {
                        this.hashTable[row] = entry.nextInValueSetHashRow;
                    } else {
                        prev.nextInValueSetHashRow = entry.nextInValueSetHashRow;
                    }
                    LinkedHashMultimap.deleteFromValueSet(entry);
                    LinkedHashMultimap.deleteFromMultimap(entry);
                    this.size--;
                    this.modCount++;
                    return true;
                }
                prev = entry;
                entry = entry.nextInValueSetHashRow;
            }
            return false;
        }

        public void clear() {
            Arrays.fill(this.hashTable, null);
            this.size = 0;
            for (ValueEntry<K, V> entry = this.firstEntry; entry != this; entry = entry.getSuccessorInValueSet()) {
                LinkedHashMultimap.deleteFromMultimap(entry);
            }
            LinkedHashMultimap.succeedsInValueSet(this, this);
            this.modCount++;
        }
    }

    public /* bridge */ /* synthetic */ Map asMap() {
        return super.asMap();
    }

    public /* bridge */ /* synthetic */ boolean containsEntry(Object x0, Object x1) {
        return super.containsEntry(x0, x1);
    }

    public /* bridge */ /* synthetic */ boolean containsKey(Object x0) {
        return super.containsKey(x0);
    }

    public /* bridge */ /* synthetic */ boolean containsValue(Object x0) {
        return super.containsValue(x0);
    }

    public /* bridge */ /* synthetic */ boolean equals(Object x0) {
        return super.equals(x0);
    }

    public /* bridge */ /* synthetic */ Set get(Object x0) {
        return super.get(x0);
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    public /* bridge */ /* synthetic */ Set keySet() {
        return super.keySet();
    }

    public /* bridge */ /* synthetic */ Multiset keys() {
        return super.keys();
    }

    public /* bridge */ /* synthetic */ boolean put(Object x0, Object x1) {
        return super.put(x0, x1);
    }

    public /* bridge */ /* synthetic */ boolean putAll(Multimap x0) {
        return super.putAll(x0);
    }

    public /* bridge */ /* synthetic */ boolean putAll(Object x0, Iterable x1) {
        return super.putAll(x0, x1);
    }

    public /* bridge */ /* synthetic */ boolean remove(Object x0, Object x1) {
        return super.remove(x0, x1);
    }

    public /* bridge */ /* synthetic */ Set removeAll(Object x0) {
        return super.removeAll(x0);
    }

    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public static <K, V> LinkedHashMultimap<K, V> create() {
        return new LinkedHashMultimap(16, 2);
    }

    public static <K, V> LinkedHashMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) {
        return new LinkedHashMultimap(Maps.capacity(expectedKeys), Maps.capacity(expectedValuesPerKey));
    }

    public static <K, V> LinkedHashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
        LinkedHashMultimap<K, V> result = create(multimap.keySet().size(), 2);
        result.putAll(multimap);
        return result;
    }

    private static <K, V> void succeedsInValueSet(ValueSetLink<K, V> pred, ValueSetLink<K, V> succ) {
        pred.setSuccessorInValueSet(succ);
        succ.setPredecessorInValueSet(pred);
    }

    private static <K, V> void succeedsInMultimap(ValueEntry<K, V> pred, ValueEntry<K, V> succ) {
        pred.setSuccessorInMultimap(succ);
        succ.setPredecessorInMultimap(pred);
    }

    private static <K, V> void deleteFromValueSet(ValueSetLink<K, V> entry) {
        succeedsInValueSet(entry.getPredecessorInValueSet(), entry.getSuccessorInValueSet());
    }

    private static <K, V> void deleteFromMultimap(ValueEntry<K, V> entry) {
        succeedsInMultimap(entry.getPredecessorInMultimap(), entry.getSuccessorInMultimap());
    }

    private LinkedHashMultimap(int keyCapacity, int valueSetCapacity) {
        super(new LinkedHashMap(keyCapacity));
        Preconditions.checkArgument(valueSetCapacity >= 0, "expectedValuesPerKey must be >= 0 but was %s", Integer.valueOf(valueSetCapacity));
        this.valueSetCapacity = valueSetCapacity;
        this.multimapHeaderEntry = new ValueEntry(null, null, 0, null);
        succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
    }

    Set<V> createCollection() {
        return new LinkedHashSet(this.valueSetCapacity);
    }

    Collection<V> createCollection(K key) {
        return new ValueSet(key, this.valueSetCapacity);
    }

    public Set<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
        return super.replaceValues((Object) key, (Iterable) values);
    }

    public Set<Entry<K, V>> entries() {
        return super.entries();
    }

    public Collection<V> values() {
        return super.values();
    }

    Iterator<Entry<K, V>> entryIterator() {
        return new C01941();
    }

    public void clear() {
        super.clear();
        succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(this.valueSetCapacity);
        stream.writeInt(keySet().size());
        for (K key : keySet()) {
            stream.writeObject(key);
        }
        stream.writeInt(size());
        for (Entry<K, V> entry : entries()) {
            stream.writeObject(entry.getKey());
            stream.writeObject(entry.getValue());
        }
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        int i;
        stream.defaultReadObject();
        this.multimapHeaderEntry = new ValueEntry(null, null, 0, null);
        succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
        this.valueSetCapacity = stream.readInt();
        int distinctKeys = stream.readInt();
        Map<K, Collection<V>> map = new LinkedHashMap(Maps.capacity(distinctKeys));
        for (i = 0; i < distinctKeys; i++) {
            K key = stream.readObject();
            map.put(key, createCollection(key));
        }
        int entries = stream.readInt();
        for (i = 0; i < entries; i++) {
            key = stream.readObject();
            ((Collection) map.get(key)).add(stream.readObject());
        }
        setMap(map);
    }
}
