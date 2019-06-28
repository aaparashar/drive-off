package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
class RegularImmutableList<E> extends ImmutableList<E> {
    private final transient Object[] array;
    private final transient int offset;
    private final transient int size;

    RegularImmutableList(Object[] array, int offset, int size) {
        this.offset = offset;
        this.size = size;
        this.array = array;
    }

    RegularImmutableList(Object[] array) {
        this(array, 0, array.length);
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return false;
    }

    boolean isPartialView() {
        return (this.offset == 0 && this.size == this.array.length) ? false : true;
    }

    public Object[] toArray() {
        Object[] newArray = new Object[size()];
        System.arraycopy(this.array, this.offset, newArray, 0, this.size);
        return newArray;
    }

    public <T> T[] toArray(T[] other) {
        if (other.length < this.size) {
            other = ObjectArrays.newArray((Object[]) other, this.size);
        } else if (other.length > this.size) {
            other[this.size] = null;
        }
        System.arraycopy(this.array, this.offset, other, 0, this.size);
        return other;
    }

    public E get(int index) {
        Preconditions.checkElementIndex(index, this.size);
        return this.array[this.offset + index];
    }

    ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
        return new RegularImmutableList(this.array, this.offset + fromIndex, toIndex - fromIndex);
    }

    public UnmodifiableListIterator<E> listIterator(int index) {
        return Iterators.forArray(this.array, this.offset, this.size, index);
    }

    public boolean equals(@Nullable Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof List)) {
            return false;
        }
        List<?> that = (List) object;
        if (size() != that.size()) {
            return false;
        }
        int index = this.offset;
        int index2;
        if (object instanceof RegularImmutableList) {
            RegularImmutableList<?> other = (RegularImmutableList) object;
            int i = other.offset;
            while (i < other.offset + other.size) {
                index2 = index + 1;
                if (!this.array[index].equals(other.array[i])) {
                    return false;
                }
                i++;
                index = index2;
            }
            return true;
        }
        for (Object element : that) {
            index2 = index + 1;
            if (!this.array[index].equals(element)) {
                return false;
            }
            index = index2;
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = Collections2.newStringBuilderForCollection(size()).append('[').append(this.array[this.offset]);
        for (int i = this.offset + 1; i < this.offset + this.size; i++) {
            sb.append(", ").append(this.array[i]);
        }
        return sb.append(']').toString();
    }
}
