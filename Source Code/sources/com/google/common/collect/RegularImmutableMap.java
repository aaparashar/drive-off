package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@GwtCompatible(emulated = true, serializable = true)
final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {
    private static final double MAX_LOAD_FACTOR = 1.2d;
    private static final long serialVersionUID = 0;
    private final transient LinkedEntry<K, V>[] entries;
    private final transient int mask;
    private final transient LinkedEntry<K, V>[] table;

    private interface LinkedEntry<K, V> extends Entry<K, V> {
        @Nullable
        LinkedEntry<K, V> next();
    }

    @Immutable
    private static final class NonTerminalEntry<K, V> extends ImmutableEntry<K, V> implements LinkedEntry<K, V> {
        final LinkedEntry<K, V> next;

        NonTerminalEntry(K key, V value, LinkedEntry<K, V> next) {
            super(key, value);
            this.next = next;
        }

        public LinkedEntry<K, V> next() {
            return this.next;
        }
    }

    @Immutable
    private static final class TerminalEntry<K, V> extends ImmutableEntry<K, V> implements LinkedEntry<K, V> {
        TerminalEntry(K key, V value) {
            super(key, value);
        }

        @Nullable
        public LinkedEntry<K, V> next() {
            return null;
        }
    }

    private class EntrySet extends ImmutableMapEntrySet<K, V> {
        private EntrySet() {
        }

        ImmutableMap<K, V> map() {
            return RegularImmutableMap.this;
        }

        public UnmodifiableIterator<Entry<K, V>> iterator() {
            return asList().iterator();
        }

        ImmutableList<Entry<K, V>> createAsList() {
            return new RegularImmutableAsList((ImmutableCollection) this, RegularImmutableMap.this.entries);
        }
    }

    RegularImmutableMap(Entry<?, ?>... immutableEntries) {
        int size = immutableEntries.length;
        this.entries = createEntryArray(size);
        int tableSize = Hashing.closedTableSize(size, MAX_LOAD_FACTOR);
        this.table = createEntryArray(tableSize);
        this.mask = tableSize - 1;
        for (int entryIndex = 0; entryIndex < size; entryIndex++) {
            Entry<K, V> entry = immutableEntries[entryIndex];
            K key = entry.getKey();
            int tableIndex = Hashing.smear(key.hashCode()) & this.mask;
            LinkedEntry<K, V> existing = this.table[tableIndex];
            LinkedEntry<K, V> linkedEntry = newLinkedEntry(key, entry.getValue(), existing);
            this.table[tableIndex] = linkedEntry;
            this.entries[entryIndex] = linkedEntry;
            while (existing != null) {
                boolean z;
                if (key.equals(existing.getKey())) {
                    z = false;
                } else {
                    z = true;
                }
                Preconditions.checkArgument(z, "duplicate key: %s", key);
                existing = existing.next();
            }
        }
    }

    private LinkedEntry<K, V>[] createEntryArray(int size) {
        return new LinkedEntry[size];
    }

    private static <K, V> LinkedEntry<K, V> newLinkedEntry(K key, V value, @Nullable LinkedEntry<K, V> next) {
        return next == null ? new TerminalEntry(key, value) : new NonTerminalEntry(key, value, next);
    }

    public V get(@Nullable Object key) {
        if (key == null) {
            return null;
        }
        for (LinkedEntry<K, V> entry = this.table[Hashing.smear(key.hashCode()) & this.mask]; entry != null; entry = entry.next()) {
            if (key.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public int size() {
        return this.entries.length;
    }

    boolean isPartialView() {
        return false;
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return new EntrySet();
    }
}
