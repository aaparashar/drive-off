package com.google.common.math;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Booleans;
import java.math.BigInteger;
import java.math.RoundingMode;

public final class DoubleMath {
    private static final double LN_2 = Math.log(2.0d);
    @VisibleForTesting
    static final int MAX_FACTORIAL = 170;
    private static final double MAX_INT_AS_DOUBLE = 2.147483647E9d;
    private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18d;
    private static final double MIN_INT_AS_DOUBLE = -2.147483648E9d;
    private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18d;
    @VisibleForTesting
    static final double[] everySixteenthFactorial = new double[]{1.0d, 2.0922789888E13d, 2.631308369336935E35d, 1.2413915592536073E61d, 1.2688693218588417E89d, 7.156945704626381E118d, 9.916779348709496E149d, 1.974506857221074E182d, 3.856204823625804E215d, 5.5502938327393044E249d, 4.7147236359920616E284d};

    /* renamed from: com.google.common.math.DoubleMath$1 */
    static /* synthetic */ class C02411 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];

        static {
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    static double roundIntermediate(double x, RoundingMode mode) {
        if (DoubleUtils.isFinite(x)) {
            double z;
            switch (C02411.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
                case 1:
                    MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(x));
                    return x;
                case 2:
                    if (x >= LN_2 || isMathematicalInteger(x)) {
                        return x;
                    }
                    return x - 1.0d;
                case 3:
                    if (x <= LN_2 || isMathematicalInteger(x)) {
                        return x;
                    }
                    return x + 1.0d;
                case 4:
                    return x;
                case 5:
                    if (isMathematicalInteger(x)) {
                        return x;
                    }
                    return x + Math.copySign(1.0d, x);
                case 6:
                    return Math.rint(x);
                case 7:
                    z = Math.rint(x);
                    return Math.abs(x - z) == 0.5d ? x + Math.copySign(0.5d, x) : z;
                case 8:
                    z = Math.rint(x);
                    if (Math.abs(x - z) != 0.5d) {
                        return z;
                    }
                    return x;
                default:
                    throw new AssertionError();
            }
        }
        throw new ArithmeticException("input is infinite or NaN");
    }

    public static int roundToInt(double x, RoundingMode mode) {
        int i;
        int i2 = 1;
        double z = roundIntermediate(x, mode);
        if (z > -2.147483649E9d) {
            i = 1;
        } else {
            i = 0;
        }
        if (z >= 2.147483648E9d) {
            i2 = 0;
        }
        MathPreconditions.checkInRange(i2 & i);
        return (int) z;
    }

    public static long roundToLong(double x, RoundingMode mode) {
        int i;
        int i2 = 1;
        double z = roundIntermediate(x, mode);
        if (MIN_LONG_AS_DOUBLE - z < 1.0d) {
            i = 1;
        } else {
            i = 0;
        }
        if (z >= MAX_LONG_AS_DOUBLE_PLUS_ONE) {
            i2 = 0;
        }
        MathPreconditions.checkInRange(i2 & i);
        return (long) z;
    }

    public static BigInteger roundToBigInteger(double x, RoundingMode mode) {
        int i = 1;
        x = roundIntermediate(x, mode);
        int i2 = MIN_LONG_AS_DOUBLE - x < 1.0d ? 1 : 0;
        if (x >= MAX_LONG_AS_DOUBLE_PLUS_ONE) {
            i = 0;
        }
        if ((i & i2) != 0) {
            return BigInteger.valueOf((long) x);
        }
        BigInteger result = BigInteger.valueOf(DoubleUtils.getSignificand(x)).shiftLeft(Math.getExponent(x) - 52);
        return x < LN_2 ? result.negate() : result;
    }

    public static boolean isPowerOfTwo(double x) {
        return x > LN_2 && DoubleUtils.isFinite(x) && LongMath.isPowerOfTwo(DoubleUtils.getSignificand(x));
    }

    public static double log2(double x) {
        return Math.log(x) / LN_2;
    }

    public static int log2(double x, RoundingMode mode) {
        int i = 1;
        boolean z = x > LN_2 && DoubleUtils.isFinite(x);
        Preconditions.checkArgument(z, "x must be positive and finite");
        int exponent = Math.getExponent(x);
        if (!DoubleUtils.isNormal(x)) {
            return log2(4.503599627370496E15d * x, mode) - 52;
        }
        boolean increment;
        int i2;
        switch (C02411.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
                break;
            case 2:
                break;
            case 3:
                if (isPowerOfTwo(x)) {
                    increment = false;
                } else {
                    increment = true;
                }
                break;
            case 4:
                if (exponent < 0) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                if (isPowerOfTwo(x)) {
                    i = 0;
                }
                increment = i2 & i;
                break;
            case 5:
                if (exponent >= 0) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                if (isPowerOfTwo(x)) {
                    i = 0;
                }
                increment = i2 & i;
                break;
            case 6:
            case 7:
            case 8:
                double xScaled = DoubleUtils.scaleNormalize(x);
                if (xScaled * xScaled > 2.0d) {
                    increment = true;
                } else {
                    increment = false;
                }
                break;
            default:
                throw new AssertionError();
        }
        increment = false;
        if (increment) {
            return exponent + 1;
        }
        return exponent;
    }

    public static boolean isMathematicalInteger(double x) {
        return DoubleUtils.isFinite(x) && (x == LN_2 || 52 - Long.numberOfTrailingZeros(DoubleUtils.getSignificand(x)) <= Math.getExponent(x));
    }

    public static double factorial(int n) {
        MathPreconditions.checkNonNegative("n", n);
        if (n > MAX_FACTORIAL) {
            return Double.POSITIVE_INFINITY;
        }
        double accum = 1.0d;
        for (int i = (n & -16) + 1; i <= n; i++) {
            accum *= (double) i;
        }
        return everySixteenthFactorial[n >> 4] * accum;
    }

    public static boolean fuzzyEquals(double a, double b, double tolerance) {
        MathPreconditions.checkNonNegative("tolerance", tolerance);
        return Math.copySign(a - b, 1.0d) <= tolerance || a == b || !(a == a || b == b);
    }

    public static int fuzzyCompare(double a, double b, double tolerance) {
        if (fuzzyEquals(a, b, tolerance)) {
            return 0;
        }
        if (a < b) {
            return -1;
        }
        if (a > b) {
            return 1;
        }
        return Booleans.compare(Double.isNaN(a), Double.isNaN(b));
    }

    private DoubleMath() {
    }
}
