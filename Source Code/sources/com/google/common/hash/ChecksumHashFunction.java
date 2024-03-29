package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.zip.Checksum;

final class ChecksumHashFunction extends AbstractStreamingHashFunction implements Serializable {
    private static final long serialVersionUID = 0;
    private final int bits;
    private final Supplier<? extends Checksum> checksumSupplier;
    private final String toString;

    private final class ChecksumHasher extends AbstractByteHasher {
        private final Checksum checksum;

        private ChecksumHasher(Checksum checksum) {
            this.checksum = (Checksum) Preconditions.checkNotNull(checksum);
        }

        protected void update(byte b) {
            this.checksum.update(b);
        }

        protected void update(byte[] bytes, int off, int len) {
            this.checksum.update(bytes, off, len);
        }

        public HashCode hash() {
            long value = this.checksum.getValue();
            if (ChecksumHashFunction.this.bits == 32) {
                return HashCodes.fromInt((int) value);
            }
            return HashCodes.fromLong(value);
        }
    }

    ChecksumHashFunction(Supplier<? extends Checksum> checksumSupplier, int bits, String toString) {
        boolean z;
        this.checksumSupplier = (Supplier) Preconditions.checkNotNull(checksumSupplier);
        if (bits == 32 || bits == 64) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "bits (%s) must be either 32 or 64", Integer.valueOf(bits));
        this.bits = bits;
        this.toString = (String) Preconditions.checkNotNull(toString);
    }

    public int bits() {
        return this.bits;
    }

    public Hasher newHasher() {
        return new ChecksumHasher((Checksum) this.checksumSupplier.get());
    }

    public String toString() {
        return this.toString;
    }
}
