package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import java.util.Comparator;

@GwtCompatible
public final class SignedBytes {
    public static final byte MAX_POWER_OF_TWO = (byte) 64;

    private enum LexicographicalComparator implements Comparator<byte[]> {
        INSTANCE;

        public int compare(byte[] left, byte[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i = 0; i < minLength; i++) {
                int result = SignedBytes.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }
    }

    private SignedBytes() {
    }

    public static byte checkedCast(long value) {
        boolean z;
        byte result = (byte) ((int) value);
        if (((long) result) == value) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "Out of range: %s", Long.valueOf(value));
        return result;
    }

    public static byte saturatedCast(long value) {
        if (value > 127) {
            return Ascii.DEL;
        }
        if (value < -128) {
            return UnsignedBytes.MAX_POWER_OF_TWO;
        }
        return (byte) ((int) value);
    }

    public static int compare(byte a, byte b) {
        return a - b;
    }

    public static byte min(byte... array) {
        boolean z;
        if (array.length > 0) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z);
        byte min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static byte max(byte... array) {
        boolean z;
        if (array.length > 0) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z);
        byte max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static String join(String separator, byte... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(array.length * 5);
        builder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            builder.append(separator).append(array[i]);
        }
        return builder.toString();
    }

    public static Comparator<byte[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }
}
