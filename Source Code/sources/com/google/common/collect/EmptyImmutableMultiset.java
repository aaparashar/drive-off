package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Multiset.Entry;
import java.util.Collection;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class EmptyImmutableMultiset extends ImmutableMultiset<Object> {
    static final EmptyImmutableMultiset INSTANCE = new EmptyImmutableMultiset();
    private static final long serialVersionUID = 0;

    EmptyImmutableMultiset() {
    }

    public int count(@Nullable Object element) {
        return 0;
    }

    public boolean contains(@Nullable Object object) {
        return false;
    }

    public boolean containsAll(Collection<?> targets) {
        return targets.isEmpty();
    }

    public UnmodifiableIterator<Object> iterator() {
        return Iterators.emptyIterator();
    }

    public boolean equals(@Nullable Object object) {
        if (object instanceof Multiset) {
            return ((Multiset) object).isEmpty();
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public ImmutableSet<Object> elementSet() {
        return ImmutableSet.of();
    }

    public ImmutableSet<Entry<Object>> entrySet() {
        return ImmutableSet.of();
    }

    ImmutableSet<Entry<Object>> createEntrySet() {
        throw new AssertionError("should never be called");
    }

    public int size() {
        return 0;
    }

    boolean isPartialView() {
        return false;
    }

    public Object[] toArray() {
        return ObjectArrays.EMPTY_ARRAY;
    }

    public <T> T[] toArray(T[] other) {
        return asList().toArray(other);
    }

    public ImmutableList<Object> asList() {
        return ImmutableList.of();
    }

    Object readResolve() {
        return INSTANCE;
    }
}
