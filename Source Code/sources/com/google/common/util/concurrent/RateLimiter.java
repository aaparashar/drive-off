package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@Beta
public abstract class RateLimiter {
    double maxPermits;
    private final Object mutex;
    private long nextFreeTicketMicros;
    private final long offsetNanos;
    volatile double stableIntervalMicros;
    double storedPermits;
    private final SleepingTicker ticker;

    private static class Bursty extends RateLimiter {
        Bursty(SleepingTicker ticker) {
            super(ticker);
        }

        void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
            double d = 0.0d;
            double oldMaxPermits = this.maxPermits;
            this.maxPermits = permitsPerSecond;
            if (oldMaxPermits != 0.0d) {
                d = (this.storedPermits * this.maxPermits) / oldMaxPermits;
            }
            this.storedPermits = d;
        }

        long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
            return 0;
        }
    }

    @VisibleForTesting
    static abstract class SleepingTicker extends Ticker {
        static final SleepingTicker SYSTEM_TICKER = new C06511();

        /* renamed from: com.google.common.util.concurrent.RateLimiter$SleepingTicker$1 */
        static class C06511 extends SleepingTicker {
            C06511() {
            }

            public long read() {
                return Ticker.systemTicker().read();
            }

            public void sleepMicrosUninterruptibly(long micros) {
                if (micros > 0) {
                    Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
                }
            }
        }

        abstract void sleepMicrosUninterruptibly(long j);

        SleepingTicker() {
        }
    }

    private static class WarmingUp extends RateLimiter {
        private double halfPermits;
        private double slope;
        final long warmupPeriodMicros;

        WarmingUp(SleepingTicker ticker, long warmupPeriod, TimeUnit timeUnit) {
            super(ticker);
            this.warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);
        }

        void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
            double oldMaxPermits = this.maxPermits;
            this.maxPermits = ((double) this.warmupPeriodMicros) / stableIntervalMicros;
            this.halfPermits = this.maxPermits / 2.0d;
            this.slope = ((stableIntervalMicros * 3.0d) - stableIntervalMicros) / this.halfPermits;
            if (oldMaxPermits == Double.POSITIVE_INFINITY) {
                this.storedPermits = 0.0d;
            } else {
                this.storedPermits = oldMaxPermits == 0.0d ? this.maxPermits : (this.storedPermits * this.maxPermits) / oldMaxPermits;
            }
        }

        long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
            double availablePermitsAboveHalf = storedPermits - this.halfPermits;
            long micros = 0;
            if (availablePermitsAboveHalf > 0.0d) {
                double permitsAboveHalfToTake = Math.min(availablePermitsAboveHalf, permitsToTake);
                micros = (long) (((permitsToTime(availablePermitsAboveHalf) + permitsToTime(availablePermitsAboveHalf - permitsAboveHalfToTake)) * permitsAboveHalfToTake) / 2.0d);
                permitsToTake -= permitsAboveHalfToTake;
            }
            return (long) (((double) micros) + (this.stableIntervalMicros * permitsToTake));
        }

        private double permitsToTime(double permits) {
            return this.stableIntervalMicros + (this.slope * permits);
        }
    }

    abstract void doSetRate(double d, double d2);

    abstract long storedPermitsToWaitTime(double d, double d2);

    public static RateLimiter create(double permitsPerSecond) {
        return create(SleepingTicker.SYSTEM_TICKER, permitsPerSecond);
    }

    @VisibleForTesting
    static RateLimiter create(SleepingTicker ticker, double permitsPerSecond) {
        RateLimiter rateLimiter = new Bursty(ticker);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }

    public static RateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit) {
        return create(SleepingTicker.SYSTEM_TICKER, permitsPerSecond, warmupPeriod, unit);
    }

    @VisibleForTesting
    static RateLimiter create(SleepingTicker ticker, double permitsPerSecond, long warmupPeriod, TimeUnit timeUnit) {
        RateLimiter rateLimiter = new WarmingUp(ticker, warmupPeriod, timeUnit);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }

    @VisibleForTesting
    static RateLimiter createBursty(SleepingTicker ticker, double permitsPerSecond, int maxBurstSize) {
        Bursty rateLimiter = new Bursty(ticker);
        rateLimiter.setRate(permitsPerSecond);
        rateLimiter.maxPermits = (double) maxBurstSize;
        return rateLimiter;
    }

    private RateLimiter(SleepingTicker ticker) {
        this.mutex = new Object();
        this.nextFreeTicketMicros = 0;
        this.ticker = ticker;
        this.offsetNanos = ticker.read();
    }

    public final void setRate(double permitsPerSecond) {
        boolean z = permitsPerSecond > 0.0d && !Double.isNaN(permitsPerSecond);
        Preconditions.checkArgument(z, "rate must be positive");
        synchronized (this.mutex) {
            resync(readSafeMicros());
            double stableIntervalMicros = ((double) TimeUnit.SECONDS.toMicros(1)) / permitsPerSecond;
            this.stableIntervalMicros = stableIntervalMicros;
            doSetRate(permitsPerSecond, stableIntervalMicros);
        }
    }

    public final double getRate() {
        return ((double) TimeUnit.SECONDS.toMicros(1)) / this.stableIntervalMicros;
    }

    public void acquire() {
        acquire(1);
    }

    public void acquire(int permits) {
        long microsToWait;
        checkPermits(permits);
        synchronized (this.mutex) {
            microsToWait = reserveNextTicket((double) permits, readSafeMicros());
        }
        this.ticker.sleepMicrosUninterruptibly(microsToWait);
    }

    public boolean tryAcquire(long timeout, TimeUnit unit) {
        return tryAcquire(1, timeout, unit);
    }

    public boolean tryAcquire(int permits) {
        return tryAcquire(permits, 0, TimeUnit.MICROSECONDS);
    }

    public boolean tryAcquire() {
        return tryAcquire(1, 0, TimeUnit.MICROSECONDS);
    }

    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
        long timeoutMicros = unit.toMicros(timeout);
        checkPermits(permits);
        synchronized (this.mutex) {
            long nowMicros = readSafeMicros();
            if (this.nextFreeTicketMicros > nowMicros + timeoutMicros) {
                return false;
            }
            long microsToWait = reserveNextTicket((double) permits, nowMicros);
            this.ticker.sleepMicrosUninterruptibly(microsToWait);
            return true;
        }
    }

    private static void checkPermits(int permits) {
        Preconditions.checkArgument(permits > 0, "Requested permits must be positive");
    }

    private long reserveNextTicket(double requiredPermits, long nowMicros) {
        resync(nowMicros);
        long microsToNextFreeTicket = this.nextFreeTicketMicros - nowMicros;
        double storedPermitsToSpend = Math.min(requiredPermits, this.storedPermits);
        this.nextFreeTicketMicros += storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend) + ((long) (this.stableIntervalMicros * (requiredPermits - storedPermitsToSpend)));
        this.storedPermits -= storedPermitsToSpend;
        return microsToNextFreeTicket;
    }

    private void resync(long nowMicros) {
        if (nowMicros > this.nextFreeTicketMicros) {
            this.storedPermits = Math.min(this.maxPermits, this.storedPermits + (((double) (nowMicros - this.nextFreeTicketMicros)) / this.stableIntervalMicros));
            this.nextFreeTicketMicros = nowMicros;
        }
    }

    private long readSafeMicros() {
        return TimeUnit.NANOSECONDS.toMicros(this.ticker.read() - this.offsetNanos);
    }

    public String toString() {
        return String.format("RateLimiter[stableRate=%3.1fqps]", new Object[]{Double.valueOf(1000000.0d / this.stableIntervalMicros)});
    }
}
