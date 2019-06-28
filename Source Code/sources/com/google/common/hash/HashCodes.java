package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedInts;
import java.io.Serializable;

@Beta
public final class HashCodes {

    private static final class BytesHashCode extends HashCode implements Serializable {
        private static final long serialVersionUID = 0;
        final byte[] bytes;

        BytesHashCode(byte[] bytes) {
            this.bytes = (byte[]) Preconditions.checkNotNull(bytes);
        }

        public int bits() {
            return this.bytes.length * 8;
        }

        public byte[] asBytes() {
            return (byte[]) this.bytes.clone();
        }

        public int asInt() {
            boolean z;
            if (this.bytes.length >= 4) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkState(z, "HashCode#asInt() requires >= 4 bytes (it only has %s bytes).", Integer.valueOf(this.bytes.length));
            return (((this.bytes[0] & 255) | ((this.bytes[1] & 255) << 8)) | ((this.bytes[2] & 255) << 16)) | ((this.bytes[3] & 255) << 24);
        }

        public long asLong() {
            boolean z;
            if (this.bytes.length >= 8) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkState(z, "HashCode#asLong() requires >= 8 bytes (it only has %s bytes).", Integer.valueOf(this.bytes.length));
            return ((((((((((long) this.bytes[1]) & 255) << 8) | (((long) this.bytes[0]) & 255)) | ((((long) this.bytes[2]) & 255) << 16)) | ((((long) this.bytes[3]) & 255) << 24)) | ((((long) this.bytes[4]) & 255) << 32)) | ((((long) this.bytes[5]) & 255) << 40)) | ((((long) this.bytes[6]) & 255) << 48)) | ((((long) this.bytes[7]) & 255) << 56);
        }

        public long padToLong() {
            return this.bytes.length < 8 ? UnsignedInts.toLong(asInt()) : asLong();
        }

        public int hashCode() {
            if (this.bytes.length >= 4) {
                return asInt();
            }
            int val = this.bytes[0] & 255;
            for (int i = 1; i < this.bytes.length; i++) {
                val |= (this.bytes[i] & 255) << (i * 8);
            }
            return val;
        }
    }

    private static final class IntHashCode extends HashCode implements Serializable {
        private static final long serialVersionUID = 0;
        final int hash;

        IntHashCode(int hash) {
            this.hash = hash;
        }

        public int bits() {
            return 32;
        }

        public byte[] asBytes() {
            return new byte[]{(byte) this.hash, (byte) (this.hash >> 8), (byte) (this.hash >> 16), (byte) (this.hash >> 24)};
        }

        public int asInt() {
            return this.hash;
        }

        public long asLong() {
            throw new IllegalStateException("this HashCode only has 32 bits; cannot create a long");
        }

        public long padToLong() {
            return UnsignedInts.toLong(this.hash);
        }
    }

    private static final class LongHashCode extends HashCode implements Serializable {
        private static final long serialVersionUID = 0;
        final long hash;

        LongHashCode(long hash) {
            this.hash = hash;
        }

        public int bits() {
            return 64;
        }

        public byte[] asBytes() {
            return new byte[]{(byte) ((int) this.hash), (byte) ((int) (this.hash >> 8)), (byte) ((int) (this.hash >> 16)), (byte) ((int) (this.hash >> 24)), (byte) ((int) (this.hash >> 32)), (byte) ((int) (this.hash >> 40)), (byte) ((int) (this.hash >> 48)), (byte) ((int) (this.hash >> 56))};
        }

        public int asInt() {
            return (int) this.hash;
        }

        public long asLong() {
            return this.hash;
        }

        public long padToLong() {
            return this.hash;
        }
    }

    private HashCodes() {
    }

    public static HashCode fromInt(int hash) {
        return new IntHashCode(hash);
    }

    public static HashCode fromLong(long hash) {
        return new LongHashCode(hash);
    }

    public static HashCode fromBytes(byte[] bytes) {
        boolean z = true;
        if (bytes.length < 1) {
            z = false;
        }
        Preconditions.checkArgument(z, "A HashCode must contain at least 1 byte.");
        return fromBytesNoCopy((byte[]) bytes.clone());
    }

    static HashCode fromBytesNoCopy(byte[] bytes) {
        return new BytesHashCode(bytes);
    }
}
