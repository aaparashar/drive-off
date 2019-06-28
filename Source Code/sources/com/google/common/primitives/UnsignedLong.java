package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.math.BigInteger;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
public final class UnsignedLong extends Number implements Comparable<UnsignedLong>, Serializable {
    public static final UnsignedLong MAX_VALUE = new UnsignedLong(-1);
    public static final UnsignedLong ONE = new UnsignedLong(1);
    private static final long UNSIGNED_MASK = Long.MAX_VALUE;
    public static final UnsignedLong ZERO = new UnsignedLong(0);
    private final long value;

    private UnsignedLong(long value) {
        this.value = value;
    }

    @Deprecated
    @Beta
    public static UnsignedLong asUnsigned(long value) {
        return fromLongBits(value);
    }

    public static UnsignedLong fromLongBits(long bits) {
        return new UnsignedLong(bits);
    }

    public static UnsignedLong valueOf(long value) {
        boolean z;
        if (value >= 0) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "value (%s) is outside the range for an unsigned long value", Long.valueOf(value));
        return fromLongBits(value);
    }

    public static UnsignedLong valueOf(BigInteger value) {
        boolean z;
        Preconditions.checkNotNull(value);
        if (value.signum() < 0 || value.bitLength() > 64) {
            z = false;
        } else {
            z = true;
        }
        Preconditions.checkArgument(z, "value (%s) is outside the range for an unsigned long value", value);
        return fromLongBits(value.longValue());
    }

    public static UnsignedLong valueOf(String string) {
        return valueOf(string, 10);
    }

    public static UnsignedLong valueOf(String string, int radix) {
        return fromLongBits(UnsignedLongs.parseUnsignedLong(string, radix));
    }

    @Deprecated
    @Beta
    public UnsignedLong add(UnsignedLong val) {
        return plus(val);
    }

    public UnsignedLong plus(UnsignedLong val) {
        return fromLongBits(((UnsignedLong) Preconditions.checkNotNull(val)).value + this.value);
    }

    @Deprecated
    @Beta
    public UnsignedLong subtract(UnsignedLong val) {
        return minus(val);
    }

    public UnsignedLong minus(UnsignedLong val) {
        return fromLongBits(this.value - ((UnsignedLong) Preconditions.checkNotNull(val)).value);
    }

    @Deprecated
    @Beta
    public UnsignedLong multiply(UnsignedLong val) {
        return times(val);
    }

    @CheckReturnValue
    public UnsignedLong times(UnsignedLong val) {
        return fromLongBits(((UnsignedLong) Preconditions.checkNotNull(val)).value * this.value);
    }

    @Deprecated
    @Beta
    public UnsignedLong divide(UnsignedLong val) {
        return dividedBy(val);
    }

    @CheckReturnValue
    public UnsignedLong dividedBy(UnsignedLong val) {
        return fromLongBits(UnsignedLongs.divide(this.value, ((UnsignedLong) Preconditions.checkNotNull(val)).value));
    }

    @Deprecated
    @Beta
    public UnsignedLong remainder(UnsignedLong val) {
        return mod(val);
    }

    @CheckReturnValue
    public UnsignedLong mod(UnsignedLong val) {
        return fromLongBits(UnsignedLongs.remainder(this.value, ((UnsignedLong) Preconditions.checkNotNull(val)).value));
    }

    public int intValue() {
        return (int) this.value;
    }

    public long longValue() {
        return this.value;
    }

    public float floatValue() {
        float fValue = (float) (this.value & UNSIGNED_MASK);
        if (this.value < 0) {
            return fValue + 9.223372E18f;
        }
        return fValue;
    }

    public double doubleValue() {
        double dValue = (double) (this.value & UNSIGNED_MASK);
        if (this.value < 0) {
            return dValue + 9.223372036854776E18d;
        }
        return dValue;
    }

    public BigInteger bigIntegerValue() {
        BigInteger bigInt = BigInteger.valueOf(this.value & UNSIGNED_MASK);
        if (this.value < 0) {
            return bigInt.setBit(63);
        }
        return bigInt;
    }

    public int compareTo(UnsignedLong o) {
        Preconditions.checkNotNull(o);
        return UnsignedLongs.compare(this.value, o.value);
    }

    public int hashCode() {
        return Longs.hashCode(this.value);
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof UnsignedLong)) {
            return false;
        }
        if (this.value == ((UnsignedLong) obj).value) {
            return true;
        }
        return false;
    }

    public String toString() {
        return UnsignedLongs.toString(this.value);
    }

    public String toString(int radix) {
        return UnsignedLongs.toString(this.value, radix);
    }
}
