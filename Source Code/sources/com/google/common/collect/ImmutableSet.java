package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableSet<E> extends ImmutableCollection<E> implements Set<E> {
    private static final int CUTOFF = ((int) Math.floor(7.516192768E8d));
    private static final double DESIRED_LOAD_FACTOR = 0.7d;
    static final int MAX_TABLE_SIZE = 1073741824;

    private static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final Object[] elements;

        SerializedForm(Object[] elements) {
            this.elements = elements;
        }

        Object readResolve() {
            return ImmutableSet.copyOf(this.elements);
        }
    }

    public static class Builder<E> extends com.google.common.collect.ImmutableCollection.Builder<E> {
        Object[] contents;
        int size;

        public Builder() {
            this(4);
        }

        Builder(int capacity) {
            boolean z;
            if (capacity >= 0) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "capacity must be >= 0 but was %s", Integer.valueOf(capacity));
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
            ensureCapacity(this.size + 1);
            Object[] objArr = this.contents;
            int i = this.size;
            this.size = i + 1;
            objArr[i] = Preconditions.checkNotNull(element);
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

        public Builder<E> addAll(Iterable<? extends E> elements) {
            if (elements instanceof Collection) {
                ensureCapacity(this.size + ((Collection) elements).size());
            }
            super.addAll((Iterable) elements);
            return this;
        }

        public Builder<E> addAll(Iterator<? extends E> elements) {
            super.addAll((Iterator) elements);
            return this;
        }

        public ImmutableSet<E> build() {
            ImmutableSet<E> result = ImmutableSet.construct(this.size, this.contents);
            this.size = result.size();
            return result;
        }
    }

    static abstract class ArrayImmutableSet<E> extends ImmutableSet<E> {
        final transient Object[] elements;

        ArrayImmutableSet(Object[] elements) {
            this.elements = elements;
        }

        public int size() {
            return this.elements.length;
        }

        public boolean isEmpty() {
            return false;
        }

        public UnmodifiableIterator<E> iterator() {
            return asList().iterator();
        }

        public Object[] toArray() {
            return asList().toArray();
        }

        public <T> T[] toArray(T[] array) {
            return asList().toArray(array);
        }

        public boolean containsAll(Collection<?> targets) {
            if (targets == this) {
                return true;
            }
            if (!(targets instanceof ArrayImmutableSet)) {
                return super.containsAll(targets);
            }
            if (targets.size() > size()) {
                return false;
            }
            for (Object target : ((ArrayImmutableSet) targets).elements) {
                if (!contains(target)) {
                    return false;
                }
            }
            return true;
        }

        boolean isPartialView() {
            return false;
        }

        ImmutableList<E> createAsList() {
            return new RegularImmutableAsList((ImmutableCollection) this, this.elements);
        }
    }

    public abstract UnmodifiableIterator<E> iterator();

    public static <E> ImmutableSet<E> of() {
        return EmptyImmutableSet.INSTANCE;
    }

    public static <E> ImmutableSet<E> of(E element) {
        return new SingletonImmutableSet(element);
    }

    public static <E> ImmutableSet<E> of(E e1, E e2) {
        return construct(2, e1, e2);
    }

    public static <E> ImmutableSet<E> of(E e1, E e2, E e3) {
        return construct(3, e1, e2, e3);
    }

    public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4) {
        return construct(4, e1, e2, e3, e4);
    }

    public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5) {
        return construct(5, e1, e2, e3, e4, e5);
    }

    public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
        Object[] elements = new Object[(others.length + 6)];
        elements[0] = e1;
        elements[1] = e2;
        elements[2] = e3;
        elements[3] = e4;
        elements[4] = e5;
        elements[5] = e6;
        System.arraycopy(others, 0, elements, 6, others.length);
        return construct(elements.length, elements);
    }

    private static <E> ImmutableSet<E> construct(int n, Object... elements) {
        switch (n) {
            case 0:
                return of();
            case 1:
                return of(elements[0]);
            default:
                int tableSize = chooseTableSize(n);
                Object[] table = new Object[tableSize];
                int mask = tableSize - 1;
                int hashCode = 0;
                int i = 0;
                int uniques = 0;
                while (i < n) {
                    Object element = ObjectArrays.checkElementNotNull(elements[i], i);
                    int hash = element.hashCode();
                    int j = Hashing.smear(hash);
                    while (true) {
                        int uniques2;
                        int index = j & mask;
                        Object value = table[index];
                        if (value == null) {
                            uniques2 = uniques + 1;
                            elements[uniques] = element;
                            table[index] = element;
                            hashCode += hash;
                        } else if (value.equals(element)) {
                            uniques2 = uniques;
                        } else {
                            j++;
                        }
                        i++;
                        uniques = uniques2;
                    }
                }
                Arrays.fill(elements, uniques, n, null);
                if (uniques == 1) {
                    return new SingletonImmutableSet(elements[0], hashCode);
                }
                if (tableSize != chooseTableSize(uniques)) {
                    return construct(uniques, elements);
                }
                Object[] uniqueElements;
                if (uniques < elements.length) {
                    uniqueElements = ObjectArrays.arraysCopyOf(elements, uniques);
                } else {
                    uniqueElements = elements;
                }
                return new RegularImmutableSet(uniqueElements, hashCode, table, mask);
        }
    }

    @VisibleForTesting
    static int chooseTableSize(int setSize) {
        int i = 1073741824;
        if (setSize < CUTOFF) {
            i = Integer.highestOneBit(setSize - 1) << 1;
            while (((double) i) * DESIRED_LOAD_FACTOR < ((double) setSize)) {
                i <<= 1;
            }
        } else {
            Preconditions.checkArgument(setSize < 1073741824, "collection too large");
        }
        return i;
    }

    public static <E> ImmutableSet<E> copyOf(E[] elements) {
        switch (elements.length) {
            case 0:
                return of();
            case 1:
                return of(elements[0]);
            default:
                return construct(elements.length, (Object[]) elements.clone());
        }
    }

    public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> elements) {
        return elements instanceof Collection ? copyOf(Collections2.cast(elements)) : copyOf(elements.iterator());
    }

    public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> elements) {
        if (!elements.hasNext()) {
            return of();
        }
        Object first = elements.next();
        if (elements.hasNext()) {
            return new Builder().add(first).addAll((Iterator) elements).build();
        }
        return of(first);
    }

    public static <E> ImmutableSet<E> copyOf(Collection<? extends E> elements) {
        if ((elements instanceof ImmutableSet) && !(elements instanceof ImmutableSortedSet)) {
            ImmutableSet<E> set = (ImmutableSet) elements;
            if (!set.isPartialView()) {
                return set;
            }
        } else if (elements instanceof EnumSet) {
            return ImmutableEnumSet.asImmutable(EnumSet.copyOf((EnumSet) elements));
        }
        return copyFromCollection(elements);
    }

    private static <E> ImmutableSet<E> copyFromCollection(Collection<? extends E> collection) {
        Object[] elements = collection.toArray();
        switch (elements.length) {
            case 0:
                return of();
            case 1:
                return of(elements[0]);
            default:
                return construct(elements.length, elements);
        }
    }

    ImmutableSet() {
    }

    boolean isHashCodeFast() {
        return false;
    }

    public boolean equals(@Nullable Object object) {
        if (object == this) {
            return true;
        }
        if ((object instanceof ImmutableSet) && isHashCodeFast() && ((ImmutableSet) object).isHashCodeFast() && hashCode() != object.hashCode()) {
            return false;
        }
        return Sets.equalsImpl(this, object);
    }

    public int hashCode() {
        return Sets.hashCodeImpl(this);
    }

    Object writeReplace() {
        return new SerializedForm(toArray());
    }

    public static <E> Builder<E> builder() {
        return new Builder();
    }
}
