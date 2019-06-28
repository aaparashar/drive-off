package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
    static final double MAX_LOAD_FACTOR = 1.2d;
    private final transient BiMapEntry<K, V>[] entries;
    private final transient int hashCode;
    private transient ImmutableBiMap<V, K> inverse;
    private final transient BiMapEntry<K, V>[] kToVTable;
    private final transient int mask;
    private final transient BiMapEntry<K, V>[] vToKTable;

    private static class InverseSerializedForm<K, V> implements Serializable {
        private static final long serialVersionUID = 1;
        private final ImmutableBiMap<K, V> forward;

        InverseSerializedForm(ImmutableBiMap<K, V> forward) {
            this.forward = forward;
        }

        Object readResolve() {
            return this.forward.inverse();
        }
    }

    private static class BiMapEntry<K, V> extends ImmutableEntry<K, V> {
        BiMapEntry(K key, V value) {
            super(key, value);
        }

        @Nullable
        BiMapEntry<K, V> getNextInKToVBucket() {
            return null;
        }

        @Nullable
        BiMapEntry<K, V> getNextInVToKBucket() {
            return null;
        }
    }

    private final class Inverse extends ImmutableBiMap<V, K> {

        final class InverseEntrySet extends ImmutableMapEntrySet<V, K> {

            /* renamed from: com.google.common.collect.RegularImmutableBiMap$Inverse$InverseEntrySet$1 */
            class C06681 extends ImmutableAsList<Entry<V, K>> {
                C06681() {
                }

                public Entry<V, K> get(int index) {
                    Entry<K, V> entry = RegularImmutableBiMap.this.entries[index];
                    return Maps.immutableEntry(entry.getValue(), entry.getKey());
                }

                ImmutableCollection<Entry<V, K>> delegateCollection() {
                    return InverseEntrySet.this;
                }
            }

            InverseEntrySet() {
            }

            ImmutableMap<V, K> map() {
                return Inverse.this;
            }

            boolean isHashCodeFast() {
                return true;
            }

            public int hashCode() {
                return RegularImmutableBiMap.this.hashCode;
            }

            public UnmodifiableIterator<Entry<V, K>> iterator() {
                return asList().iterator();
            }

            ImmutableList<Entry<V, K>> createAsList() {
                return new C06681();
            }
        }

        private Inverse() {
        }

        public int size() {
            return inverse().size();
        }

        public ImmutableBiMap<K, V> inverse() {
            return RegularImmutableBiMap.this;
        }

        public K get(@Nullable Object value) {
            if (value == null) {
                return null;
            }
            for (BiMapEntry<K, V> entry = RegularImmutableBiMap.this.vToKTable[Hashing.smear(value.hashCode()) & RegularImmutableBiMap.this.mask]; entry != null; entry = entry.getNextInVToKBucket()) {
                if (value.equals(entry.getValue())) {
                    return entry.getKey();
                }
            }
            return null;
        }

        ImmutableSet<Entry<V, K>> createEntrySet() {
            return new InverseEntrySet();
        }

        boolean isPartialView() {
            return false;
        }

        Object writeReplace() {
            return new InverseSerializedForm(RegularImmutableBiMap.this);
        }
    }

    /* renamed from: com.google.common.collect.RegularImmutableBiMap$1 */
    class C06671 extends ImmutableMapEntrySet<K, V> {
        C06671() {
        }

        ImmutableMap<K, V> map() {
            return RegularImmutableBiMap.this;
        }

        public UnmodifiableIterator<Entry<K, V>> iterator() {
            return asList().iterator();
        }

        ImmutableList<Entry<K, V>> createAsList() {
            return new RegularImmutableAsList((ImmutableCollection) this, RegularImmutableBiMap.this.entries);
        }

        boolean isHashCodeFast() {
            return true;
        }

        public int hashCode() {
            return RegularImmutableBiMap.this.hashCode;
        }
    }

    private static class NonTerminalBiMapEntry<K, V> extends BiMapEntry<K, V> {
        @Nullable
        private final BiMapEntry<K, V> nextInKToVBucket;
        @Nullable
        private final BiMapEntry<K, V> nextInVToKBucket;

        NonTerminalBiMapEntry(K key, V value, @Nullable BiMapEntry<K, V> nextInKToVBucket, @Nullable BiMapEntry<K, V> nextInVToKBucket) {
            super(key, value);
            this.nextInKToVBucket = nextInKToVBucket;
            this.nextInVToKBucket = nextInVToKBucket;
        }

        @Nullable
        BiMapEntry<K, V> getNextInKToVBucket() {
            return this.nextInKToVBucket;
        }

        @Nullable
        BiMapEntry<K, V> getNextInVToKBucket() {
            return this.nextInVToKBucket;
        }
    }

    RegularImmutableBiMap(Collection<? extends Entry<? extends K, ? extends V>> entriesToAdd) {
        int n = entriesToAdd.size();
        int tableSize = Hashing.closedTableSize(n, MAX_LOAD_FACTOR);
        this.mask = tableSize - 1;
        BiMapEntry<K, V>[] kToVTable = createEntryArray(tableSize);
        BiMapEntry<K, V>[] vToKTable = createEntryArray(tableSize);
        BiMapEntry<K, V>[] entries = createEntryArray(n);
        int i = 0;
        int hashCode = 0;
        for (Entry<? extends K, ? extends V> entry : entriesToAdd) {
            K key = Preconditions.checkNotNull(entry.getKey());
            V value = Preconditions.checkNotNull(entry.getValue());
            int keyHash = key.hashCode();
            int valueHash = value.hashCode();
            int keyBucket = Hashing.smear(keyHash) & this.mask;
            int valueBucket = Hashing.smear(valueHash) & this.mask;
            BiMapEntry<K, V> nextInKToVBucket = kToVTable[keyBucket];
            for (BiMapEntry<K, V> kToVEntry = nextInKToVBucket; kToVEntry != null; kToVEntry = kToVEntry.getNextInKToVBucket()) {
                if (key.equals(kToVEntry.getKey())) {
                    throw new IllegalArgumentException("Multiple entries with same key: " + entry + " and " + kToVEntry);
                }
            }
            BiMapEntry<K, V> nextInVToKBucket = vToKTable[valueBucket];
            for (BiMapEntry<K, V> vToKEntry = nextInVToKBucket; vToKEntry != null; vToKEntry = vToKEntry.getNextInVToKBucket()) {
                if (value.equals(vToKEntry.getValue())) {
                    throw new IllegalArgumentException("Multiple entries with same value: " + entry + " and " + vToKEntry);
                }
            }
            BiMapEntry<K, V> newEntry = (nextInKToVBucket == null && nextInVToKBucket == null) ? new BiMapEntry(key, value) : new NonTerminalBiMapEntry(key, value, nextInKToVBucket, nextInVToKBucket);
            kToVTable[keyBucket] = newEntry;
            vToKTable[valueBucket] = newEntry;
            int i2 = i + 1;
            entries[i] = newEntry;
            hashCode += keyHash ^ valueHash;
            i = i2;
        }
        this.kToVTable = kToVTable;
        this.vToKTable = vToKTable;
        this.entries = entries;
        this.hashCode = hashCode;
    }

    private static <K, V> BiMapEntry<K, V>[] createEntryArray(int length) {
        return new BiMapEntry[length];
    }

    @Nullable
    public V get(@Nullable Object key) {
        if (key == null) {
            return null;
        }
        for (BiMapEntry<K, V> entry = this.kToVTable[Hashing.smear(key.hashCode()) & this.mask]; entry != null; entry = entry.getNextInKToVBucket()) {
            if (key.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return new C06671();
    }

    boolean isPartialView() {
        return false;
    }

    public int size() {
        return this.entries.length;
    }

    public ImmutableBiMap<V, K> inverse() {
        ImmutableBiMap<V, K> immutableBiMap = this.inverse;
        if (immutableBiMap != null) {
            return immutableBiMap;
        }
        immutableBiMap = new Inverse();
        this.inverse = immutableBiMap;
        return immutableBiMap;
    }
}
