package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
class RegularImmutableAsList<E> extends ImmutableAsList<E> {
    private final ImmutableCollection<E> delegate;
    private final ImmutableList<? extends E> delegateList;

    RegularImmutableAsList(ImmutableCollection<E> delegate, ImmutableList<? extends E> delegateList) {
        this.delegate = delegate;
        this.delegateList = delegateList;
    }

    RegularImmutableAsList(ImmutableCollection<E> delegate, Object[] array) {
        this((ImmutableCollection) delegate, ImmutableList.asImmutableList(array));
    }

    ImmutableCollection<E> delegateCollection() {
        return this.delegate;
    }

    ImmutableList<? extends E> delegateList() {
        return this.delegateList;
    }

    public UnmodifiableListIterator<E> listIterator(int index) {
        return this.delegateList.listIterator(index);
    }

    public Object[] toArray() {
        return this.delegateList.toArray();
    }

    public <T> T[] toArray(T[] other) {
        return this.delegateList.toArray(other);
    }

    public int indexOf(Object object) {
        return this.delegateList.indexOf(object);
    }

    public int lastIndexOf(Object object) {
        return this.delegateList.lastIndexOf(object);
    }

    public boolean equals(Object obj) {
        return this.delegateList.equals(obj);
    }

    public int hashCode() {
        return this.delegateList.hashCode();
    }

    public E get(int index) {
        return this.delegateList.get(index);
    }
}
