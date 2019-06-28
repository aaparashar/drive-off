package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;

@GwtCompatible(emulated = true, serializable = true)
final class RegularImmutableSet<E> extends ArrayImmutableSet<E> {
    private final transient int hashCode;
    private final transient int mask;
    @VisibleForTesting
    final transient Object[] table;

    RegularImmutableSet(Object[] elements, int hashCode, Object[] table, int mask) {
        super(elements);
        this.table = table;
        this.mask = mask;
        this.hashCode = hashCode;
    }

    public boolean contains(Object target) {
        if (target == null) {
            return false;
        }
        int i = Hashing.smear(target.hashCode());
        while (true) {
            Object candidate = this.table[this.mask & i];
            if (candidate == null) {
                return false;
            }
            if (candidate.equals(target)) {
                return true;
            }
            i++;
        }
    }

    public int hashCode() {
        return this.hashCode;
    }

    boolean isHashCodeFast() {
        return true;
    }
}
