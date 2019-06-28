package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.common.primitives.Ints;
import javax.annotation.Nullable;

final class RegularImmutableSortedMultiset<E> extends ImmutableSortedMultiset<E> {
    private final transient int[] counts;
    private final transient long[] cumulativeCounts;
    private final transient RegularImmutableSortedSet<E> elementSet;
    private final transient int length;
    private final transient int offset;

    private final class EntrySet extends EntrySet {

        /* renamed from: com.google.common.collect.RegularImmutableSortedMultiset$EntrySet$1 */
        class C06711 extends ImmutableAsList<Entry<E>> {
            C06711() {
            }

            public Entry<E> get(int index) {
                return RegularImmutableSortedMultiset.this.getEntry(index);
            }

            ImmutableCollection<Entry<E>> delegateCollection() {
                return EntrySet.this;
            }
        }

        private EntrySet() {
            super();
        }

        public int size() {
            return RegularImmutableSortedMultiset.this.length;
        }

        public UnmodifiableIterator<Entry<E>> iterator() {
            return asList().iterator();
        }

        ImmutableList<Entry<E>> createAsList() {
            return new C06711();
        }
    }

    RegularImmutableSortedMultiset(RegularImmutableSortedSet<E> elementSet, int[] counts, long[] cumulativeCounts, int offset, int length) {
        this.elementSet = elementSet;
        this.counts = counts;
        this.cumulativeCounts = cumulativeCounts;
        this.offset = offset;
        this.length = length;
    }

    private Entry<E> getEntry(int index) {
        return Multisets.immutableEntry(this.elementSet.asList().get(index), this.counts[this.offset + index]);
    }

    public Entry<E> firstEntry() {
        return getEntry(0);
    }

    public Entry<E> lastEntry() {
        return getEntry(this.length - 1);
    }

    public int count(@Nullable Object element) {
        int index = this.elementSet.indexOf(element);
        return index == -1 ? 0 : this.counts[this.offset + index];
    }

    public int size() {
        return Ints.saturatedCast(this.cumulativeCounts[this.offset + this.length] - this.cumulativeCounts[this.offset]);
    }

    public ImmutableSortedSet<E> elementSet() {
        return this.elementSet;
    }

    public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
        return getSubMultiset(0, this.elementSet.headIndex(upperBound, Preconditions.checkNotNull(boundType) == BoundType.CLOSED));
    }

    public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
        return getSubMultiset(this.elementSet.tailIndex(lowerBound, Preconditions.checkNotNull(boundType) == BoundType.CLOSED), this.length);
    }

    ImmutableSortedMultiset<E> getSubMultiset(int from, int to) {
        Preconditions.checkPositionIndexes(from, to, this.length);
        if (from == to) {
            return ImmutableSortedMultiset.emptyMultiset(comparator());
        }
        return (from == 0 && to == this.length) ? this : new RegularImmutableSortedMultiset((RegularImmutableSortedSet) this.elementSet.getSubSet(from, to), this.counts, this.cumulativeCounts, this.offset + from, to - from);
    }

    ImmutableSet<Entry<E>> createEntrySet() {
        return new EntrySet();
    }

    boolean isPartialView() {
        return this.offset > 0 || this.length < this.counts.length;
    }
}
