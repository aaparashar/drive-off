package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableList<E> extends ImmutableCollection<E> implements List<E>, RandomAccess {

    private static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final Object[] elements;

        SerializedForm(Object[] elements) {
            this.elements = elements;
        }

        Object readResolve() {
            return ImmutableList.copyOf(this.elements);
        }
    }

    public static final class Builder<E> extends com.google.common.collect.ImmutableCollection.Builder<E> {
        private Object[] contents;
        private int size;

        public Builder() {
            this(4);
        }

        Builder(int capacity) {
            this.contents = new Object[capacity];
            this.size = 0;
        }

        Builder<E> ensureCapacity(int minCapacity) {
            if (this.contents.length < minCapacity) {
                this.contents = ObjectArrays.arraysCopyOf(this.contents, com.google.common.collect.ImmutableCollection.Builder.expandedCapacity(this.contents.length, minCapacity));
            }
            return this;
        }

        public Builder<E> add(E element) {
            Preconditions.checkNotNull(element);
            ensureCapacity(this.size + 1);
            Object[] objArr = this.contents;
            int i = this.size;
            this.size = i + 1;
            objArr[i] = element;
            return this;
        }

        public Builder<E> addAll(Iterable<? extends E> elements) {
            if (elements instanceof Collection) {
                ensureCapacity(this.size + ((Collection) elements).size());
            }
            super.addAll((Iterable) elements);
            return this;
        }

        public Builder<E> add(E... elements) {
            for (int i = 0; i < elements.length; i++) {
                ObjectArrays.checkElementNotNull(elements[i], i);
            }
            ensureCapacity(this.size + elements.length);
            System.arraycopy(elements, 0, this.contents, this.size, elements.length);
            this.size += elements.length;
            return this;
        }

        public Builder<E> addAll(Iterator<? extends E> elements) {
            super.addAll((Iterator) elements);
            return this;
        }

        public ImmutableList<E> build() {
            switch (this.size) {
                case 0:
                    return ImmutableList.of();
                case 1:
                    return ImmutableList.of(this.contents[0]);
                default:
                    if (this.size == this.contents.length) {
                        return new RegularImmutableList(this.contents);
                    }
                    return new RegularImmutableList(ObjectArrays.arraysCopyOf(this.contents, this.size));
            }
        }
    }

    private static class ReverseImmutableList<E> extends ImmutableList<E> {
        private final transient ImmutableList<E> forwardList;
        private final transient int size;

        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        public /* bridge */ /* synthetic */ ListIterator listIterator() {
            return super.listIterator();
        }

        ReverseImmutableList(ImmutableList<E> backingList) {
            this.forwardList = backingList;
            this.size = backingList.size();
        }

        private int reverseIndex(int index) {
            return (this.size - 1) - index;
        }

        private int reversePosition(int index) {
            return this.size - index;
        }

        public ImmutableList<E> reverse() {
            return this.forwardList;
        }

        public boolean contains(@Nullable Object object) {
            return this.forwardList.contains(object);
        }

        public boolean containsAll(Collection<?> targets) {
            return this.forwardList.containsAll(targets);
        }

        public int indexOf(@Nullable Object object) {
            int index = this.forwardList.lastIndexOf(object);
            return index >= 0 ? reverseIndex(index) : -1;
        }

        public int lastIndexOf(@Nullable Object object) {
            int index = this.forwardList.indexOf(object);
            return index >= 0 ? reverseIndex(index) : -1;
        }

        public ImmutableList<E> subList(int fromIndex, int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, this.size);
            return this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)).reverse();
        }

        public E get(int index) {
            Preconditions.checkElementIndex(index, this.size);
            return this.forwardList.get(reverseIndex(index));
        }

        public UnmodifiableListIterator<E> listIterator(int index) {
            Preconditions.checkPositionIndex(index, this.size);
            final UnmodifiableListIterator<E> forward = this.forwardList.listIterator(reversePosition(index));
            return new UnmodifiableListIterator<E>() {
                public boolean hasNext() {
                    return forward.hasPrevious();
                }

                public boolean hasPrevious() {
                    return forward.hasNext();
                }

                public E next() {
                    return forward.previous();
                }

                public int nextIndex() {
                    return ReverseImmutableList.this.reverseIndex(forward.previousIndex());
                }

                public E previous() {
                    return forward.next();
                }

                public int previousIndex() {
                    return ReverseImmutableList.this.reverseIndex(forward.nextIndex());
                }
            };
        }

        public int size() {
            return this.size;
        }

        public boolean isEmpty() {
            return this.forwardList.isEmpty();
        }

        boolean isPartialView() {
            return this.forwardList.isPartialView();
        }
    }

    class SubList extends ImmutableList<E> {
        final transient int length;
        final transient int offset;

        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        public /* bridge */ /* synthetic */ ListIterator listIterator() {
            return super.listIterator();
        }

        public /* bridge */ /* synthetic */ ListIterator listIterator(int x0) {
            return super.listIterator(x0);
        }

        SubList(int offset, int length) {
            this.offset = offset;
            this.length = length;
        }

        public int size() {
            return this.length;
        }

        public E get(int index) {
            Preconditions.checkElementIndex(index, this.length);
            return ImmutableList.this.get(this.offset + index);
        }

        public ImmutableList<E> subList(int fromIndex, int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, this.length);
            return ImmutableList.this.subList(this.offset + fromIndex, this.offset + toIndex);
        }

        boolean isPartialView() {
            return true;
        }
    }

    public static <E> ImmutableList<E> of() {
        return EmptyImmutableList.INSTANCE;
    }

    public static <E> ImmutableList<E> of(E element) {
        return new SingletonImmutableList(element);
    }

    public static <E> ImmutableList<E> of(E e1, E e2) {
        return construct(e1, e2);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3) {
        return construct(e1, e2, e3);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4) {
        return construct(e1, e2, e3, e4);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5) {
        return construct(e1, e2, e3, e4, e5);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
        return construct(e1, e2, e3, e4, e5, e6);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
        return construct(e1, e2, e3, e4, e5, e6, e7);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
        return construct(e1, e2, e3, e4, e5, e6, e7, e8);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
        return construct(e1, e2, e3, e4, e5, e6, e7, e8, e9);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
        return construct(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11) {
        return construct(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11, E e12, E... others) {
        Object[] array = new Object[(others.length + 12)];
        array[0] = e1;
        array[1] = e2;
        array[2] = e3;
        array[3] = e4;
        array[4] = e5;
        array[5] = e6;
        array[6] = e7;
        array[7] = e8;
        array[8] = e9;
        array[9] = e10;
        array[10] = e11;
        array[11] = e12;
        System.arraycopy(others, 0, array, 12, others.length);
        return construct(array);
    }

    public static <E> ImmutableList<E> copyOf(Iterable<? extends E> elements) {
        Preconditions.checkNotNull(elements);
        return elements instanceof Collection ? copyOf(Collections2.cast(elements)) : copyOf(elements.iterator());
    }

    public static <E> ImmutableList<E> copyOf(Collection<? extends E> elements) {
        if (!(elements instanceof ImmutableCollection)) {
            return copyFromCollection(elements);
        }
        ImmutableList<E> list = ((ImmutableCollection) elements).asList();
        if (list.isPartialView()) {
            return copyFromCollection(list);
        }
        return list;
    }

    public static <E> ImmutableList<E> copyOf(Iterator<? extends E> elements) {
        if (!elements.hasNext()) {
            return of();
        }
        Object first = elements.next();
        if (elements.hasNext()) {
            return new Builder().add(first).addAll((Iterator) elements).build();
        }
        return of(first);
    }

    public static <E> ImmutableList<E> copyOf(E[] elements) {
        switch (elements.length) {
            case 0:
                return of();
            case 1:
                return new SingletonImmutableList(elements[0]);
            default:
                return construct((Object[]) elements.clone());
        }
    }

    static <E> ImmutableList<E> asImmutableList(Object[] elements) {
        switch (elements.length) {
            case 0:
                return of();
            case 1:
                return new SingletonImmutableList(elements[0]);
            default:
                return construct(elements);
        }
    }

    private static <E> ImmutableList<E> copyFromCollection(Collection<? extends E> collection) {
        return asImmutableList(collection.toArray());
    }

    private static <E> ImmutableList<E> construct(Object... elements) {
        for (int i = 0; i < elements.length; i++) {
            ObjectArrays.checkElementNotNull(elements[i], i);
        }
        return new RegularImmutableList(elements);
    }

    ImmutableList() {
    }

    public UnmodifiableIterator<E> iterator() {
        return listIterator();
    }

    public UnmodifiableListIterator<E> listIterator() {
        return listIterator(0);
    }

    public UnmodifiableListIterator<E> listIterator(int index) {
        return new AbstractIndexedListIterator<E>(size(), index) {
            protected E get(int index) {
                return ImmutableList.this.get(index);
            }
        };
    }

    public int indexOf(@Nullable Object object) {
        return object == null ? -1 : Lists.indexOfImpl(this, object);
    }

    public int lastIndexOf(@Nullable Object object) {
        return object == null ? -1 : Lists.lastIndexOfImpl(this, object);
    }

    public boolean contains(@Nullable Object object) {
        return indexOf(object) >= 0;
    }

    public ImmutableList<E> subList(int fromIndex, int toIndex) {
        Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
        switch (toIndex - fromIndex) {
            case 0:
                return of();
            case 1:
                return of(get(fromIndex));
            default:
                return subListUnchecked(fromIndex, toIndex);
        }
    }

    ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
        return new SubList(fromIndex, toIndex - fromIndex);
    }

    @Deprecated
    public final boolean addAll(int index, Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final E set(int index, E e) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void add(int index, E e) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final E remove(int index) {
        throw new UnsupportedOperationException();
    }

    public ImmutableList<E> asList() {
        return this;
    }

    public ImmutableList<E> reverse() {
        return new ReverseImmutableList(this);
    }

    public boolean equals(Object obj) {
        return Lists.equalsImpl(this, obj);
    }

    public int hashCode() {
        return Lists.hashCodeImpl(this);
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    Object writeReplace() {
        return new SerializedForm(toArray());
    }

    public static <E> Builder<E> builder() {
        return new Builder();
    }
}
