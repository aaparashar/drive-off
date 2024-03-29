package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.math.BigInteger;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class UnsignedInteger extends Number implements Comparable<UnsignedInteger> {
    public static final UnsignedInteger MAX_VALUE = asUnsigned(-1);
    public static final UnsignedInteger ONE = asUnsigned(1);
    public static final UnsignedInteger ZERO = asUnsigned(0);
    private final int value;

    private UnsignedInteger(int value) {
        this.value = value & -1;
    }

    @Deprecated
    @Beta
    public static UnsignedInteger asUnsigned(int value) {
        return fromIntBits(value);
    }

    public static UnsignedInteger fromIntBits(int bits) {
        return new UnsignedInteger(bits);
    }

    public static UnsignedInteger valueOf(long value) {
        boolean z;
        if ((4294967295L & value) == value) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "value (%s) is outside the range for an unsigned integer value", Long.valueOf(value));
        return fromIntBits((int) value);
    }

    public static UnsignedInteger valueOf(BigInteger value) {
        boolean z;
        Preconditions.checkNotNull(value);
        if (value.signum() < 0 || value.bitLength() > 32) {
            z = false;
        } else {
            z = true;
        }
        Preconditions.checkArgument(z, "value (%s) is outside the range for an unsigned integer value", value);
        return fromIntBits(value.intValue());
    }

    public static UnsignedInteger valueOf(String string) {
        return valueOf(string, 10);
    }

    public static UnsignedInteger valueOf(String string, int radix) {
        return fromIntBits(UnsignedInts.parseUnsignedInt(string, radix));
    }

    @Deprecated
    @Beta
    public UnsignedInteger add(UnsignedInteger val) {
        return plus(val);
    }

    @CheckReturnValue
    public UnsignedInteger plus(UnsignedInteger val) {
        return fromIntBits(((UnsignedInteger) Preconditions.checkNotNull(val)).value + this.value);
    }

    @Deprecated
    @Beta
    public UnsignedInteger subtract(UnsignedInteger val) {
        return minus(val);
    }

    @CheckReturnValue
    public UnsignedInteger minus(UnsignedInteger val) {
        return fromIntBits(this.value - ((UnsignedInteger) Preconditions.checkNotNull(val)).value);
    }

    @GwtIncompatible("Does not truncate correctly")
    @Deprecated
    @Beta
    public UnsignedInteger multiply(UnsignedInteger val) {
        return times(val);
    }

    @CheckReturnValue
    @GwtIncompatible("Does not truncate correctly")
    public UnsignedInteger times(UnsignedInteger val) {
        return fromIntBits(((UnsignedInteger) Preconditions.checkNotNull(val)).value * this.value);
    }

    @Deprecated
    @Beta
    public UnsignedInteger divide(UnsignedInteger val) {
        return dividedBy(val);
    }

    @CheckReturnValue
    public UnsignedInteger dividedBy(UnsignedInteger val) {
        return fromIntBits(UnsignedInts.divide(this.value, ((UnsignedInteger) Preconditions.checkNotNull(val)).value));
    }

    @Deprecated
    @Beta
    public UnsignedInteger remainder(UnsignedInteger val) {
        return mod(val);
    }

    @CheckReturnValue
    public UnsignedInteger mod(UnsignedInteger val) {
        return fromIntBits(UnsignedInts.remainder(this.value, ((UnsignedInteger) Preconditions.checkNotNull(val)).value));
    }

    public int intValue() {
        return this.value;
    }

    public long longValue() {
        return UnsignedInts.toLong(this.value);
    }

    public float floatValue() {
        return (float) longValue();
    }

    public double doubleValue() {
        return (double) longValue();
    }

    public BigInteger bigIntegerValue() {
        return BigInteger.valueOf(longValue());
    }

    public int compareTo(UnsignedInteger other) {
        Preconditions.checkNotNull(other);
        return UnsignedInts.compare(this.value, other.value);
    }

    public int hashCode() {
        return this.value;
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof UnsignedInteger)) {
            return false;
        }
        if (this.value == ((UnsignedInteger) obj).value) {
            return true;
        }
        return false;
    }

    public String toString() {
        return toString(10);
    }

    public String toString(int radix) {
        return UnsignedInts.toString(this.value, radix);
    }
}
