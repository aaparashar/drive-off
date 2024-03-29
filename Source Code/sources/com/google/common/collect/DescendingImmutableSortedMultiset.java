package com.google.common.collect;

import com.google.common.collect.Multiset.Entry;
import javax.annotation.Nullable;

final class DescendingImmutableSortedMultiset<E> extends ImmutableSortedMultiset<E> {
    private final transient ImmutableSortedMultiset<E> forward;

    DescendingImmutableSortedMultiset(ImmutableSortedMultiset<E> forward) {
        this.forward = forward;
    }

    public int count(@Nullable Object element) {
        return this.forward.count(element);
    }

    public Entry<E> firstEntry() {
        return this.forward.lastEntry();
    }

    public Entry<E> lastEntry() {
        return this.forward.firstEntry();
    }

    public int size() {
        return this.forward.size();
    }

    public ImmutableSortedSet<E> elementSet() {
        return this.forward.elementSet().descendingSet();
    }

    ImmutableSet<Entry<E>> createEntrySet() {
        final ImmutableSet<Entry<E>> forwardEntrySet = this.forward.entrySet();
        return new EntrySet() {
            public int size() {
                return forwardEntrySet.size();
            }

            public UnmodifiableIterator<Entry<E>> iterator() {
                return asList().iterator();
            }

            ImmutableList<Entry<E>> createAsList() {
                return forwardEntrySet.asList().reverse();
            }
        };
    }

    public ImmutableSortedMultiset<E> descendingMultiset() {
        return this.forward;
    }

    public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
        return this.forward.tailMultiset((Object) upperBound, boundType).descendingMultiset();
    }

    public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
        return this.forward.headMultiset((Object) lowerBound, boundType).descendingMultiset();
    }

    boolean isPartialView() {
        return this.forward.isPartialView();
    }
}
