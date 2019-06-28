package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.security.MessageDigest;
import javax.annotation.Nullable;

@Beta
public abstract class HashCode {
    private static final char[] hexDigits = "0123456789abcdef".toCharArray();

    public abstract byte[] asBytes();

    public abstract int asInt();

    public abstract long asLong();

    public abstract int bits();

    public abstract long padToLong();

    HashCode() {
    }

    public int writeBytesTo(byte[] dest, int offset, int maxLength) {
        maxLength = Ints.min(maxLength, asBytes().length);
        Preconditions.checkPositionIndexes(offset, offset + maxLength, dest.length);
        System.arraycopy(hash, 0, dest, offset, maxLength);
        return maxLength;
    }

    public boolean equals(@Nullable Object object) {
        if (!(object instanceof HashCode)) {
            return false;
        }
        return MessageDigest.isEqual(asBytes(), ((HashCode) object).asBytes());
    }

    public int hashCode() {
        return asInt();
    }

    public String toString() {
        byte[] bytes = asBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(hexDigits[(b >> 4) & 15]).append(hexDigits[b & 15]);
        }
        return sb.toString();
    }
}
