package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class Hashing {
    private static final int C1 = -862048943;
    private static final int C2 = 461845907;
    static int MAX_TABLE_SIZE = 1073741824;

    private Hashing() {
    }

    static int smear(int hashCode) {
        return C2 * Integer.rotateLeft(C1 * hashCode, 15);
    }

    static int closedTableSize(int expectedEntries, double loadFactor) {
        expectedEntries = Math.max(expectedEntries, 2);
        int tableSize = Integer.highestOneBit(expectedEntries);
        if (((double) expectedEntries) / ((double) tableSize) <= loadFactor) {
            return tableSize;
        }
        tableSize <<= 1;
        return tableSize > 0 ? tableSize : MAX_TABLE_SIZE;
    }

    static boolean needsResizing(int size, int tableSize, double loadFactor) {
        return ((double) size) > ((double) tableSize) * loadFactor && tableSize < MAX_TABLE_SIZE;
    }
}
