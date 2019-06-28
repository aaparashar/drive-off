package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Multiset.Entry;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
class RegularImmutableMultiset<E> extends ImmutableMultiset<E> {
    private final transient ImmutableMap<E, Integer> map;
    private final transient int size;

    private class EntrySet extends EntrySet {
        private EntrySet() {
            super();
        }

        public int size() {
            return RegularImmutableMultiset.this.map.size();
        }

        public UnmodifiableIterator<Entry<E>> iterator() {
            return asList().iterator();
        }

        ImmutableList<Entry<E>> createAsList() {
            final ImmutableList<Map.Entry<E, Integer>> entryList = RegularImmutableMultiset.this.map.entrySet().asList();
            return new ImmutableAsList<Entry<E>>() {
                public Entry<E> get(int index) {
                    return RegularImmutableMultiset.entryFromMapEntry((Map.Entry) entryList.get(index));
                }

                ImmutableCollection<Entry<E>> delegateCollection() {
                    return EntrySet.this;
                }
            };
        }
    }

    RegularImmutableMultiset(ImmutableMap<E, Integer> map, int size) {
        this.map = map;
        this.size = size;
    }

    boolean isPartialView() {
        return this.map.isPartialView();
    }

    public int count(@Nullable Object element) {
        Integer value = (Integer) this.map.get(element);
        return value == null ? 0 : value.intValue();
    }

    public int size() {
        return this.size;
    }

    public boolean contains(@Nullable Object element) {
        return this.map.containsKey(element);
    }

    public ImmutableSet<E> elementSet() {
        return this.map.keySet();
    }

    private static <E> Entry<E> entryFromMapEntry(Map.Entry<E, Integer> entry) {
        return Multisets.immutableEntry(entry.getKey(), ((Integer) entry.getValue()).intValue());
    }

    ImmutableSet<Entry<E>> createEntrySet() {
        return new EntrySet();
    }

    public int hashCode() {
        return this.map.hashCode();
    }
}
