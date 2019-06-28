package com.google.common.primitives;

import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;

final class AndroidInteger {
    @CheckForNull
    static Integer tryParse(String string) {
        return tryParse(string, 10);
    }

    @CheckForNull
    static Integer tryParse(String string, int radix) {
        boolean z;
        Preconditions.checkNotNull(string);
        if (radix >= 2) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "Invalid radix %s, min radix is %s", Integer.valueOf(radix), Integer.valueOf(2));
        if (radix <= 36) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "Invalid radix %s, max radix is %s", Integer.valueOf(radix), Integer.valueOf(36));
        int length = string.length();
        int i = 0;
        if (length == 0) {
            return null;
        }
        boolean negative;
        if (string.charAt(0) == '-') {
            negative = true;
        } else {
            negative = false;
        }
        if (negative) {
            i = 0 + 1;
            if (i == length) {
                return null;
            }
        }
        return tryParse(string, i, radix, negative);
    }

    @CheckForNull
    private static Integer tryParse(String string, int offset, int radix, boolean negative) {
        int max = Integer.MIN_VALUE / radix;
        int result = 0;
        int length = string.length();
        int offset2 = offset;
        while (offset2 < length) {
            offset = offset2 + 1;
            int digit = Character.digit(string.charAt(offset2), radix);
            if (digit == -1 || max > result) {
                return null;
            }
            int next = (result * radix) - digit;
            if (next > result) {
                return null;
            }
            result = next;
            offset2 = offset;
        }
        if (!negative) {
            result = -result;
            if (result < 0) {
                offset = offset2;
                return null;
            }
        }
        if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
            offset = offset2;
            return null;
        }
        offset = offset2;
        return Integer.valueOf(result);
    }

    private AndroidInteger() {
    }
}
