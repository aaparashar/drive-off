package com.google.api.client.http;

import com.google.common.base.Preconditions;
import java.io.IOException;

public class ExponentialBackOffPolicy implements BackOffPolicy {
    public static final int DEFAULT_INITIAL_INTERVAL_MILLIS = 500;
    public static final int DEFAULT_MAX_ELAPSED_TIME_MILLIS = 900000;
    public static final int DEFAULT_MAX_INTERVAL_MILLIS = 60000;
    public static final double DEFAULT_MULTIPLIER = 1.5d;
    public static final double DEFAULT_RANDOMIZATION_FACTOR = 0.5d;
    private int currentIntervalMillis;
    private final int initialIntervalMillis;
    private final int maxElapsedTimeMillis;
    private final int maxIntervalMillis;
    private final double multiplier;
    private final double randomizationFactor;
    long startTimeNanos;

    public static class Builder {
        private int initialIntervalMillis = 500;
        private int maxElapsedTimeMillis = ExponentialBackOffPolicy.DEFAULT_MAX_ELAPSED_TIME_MILLIS;
        private int maxIntervalMillis = ExponentialBackOffPolicy.DEFAULT_MAX_INTERVAL_MILLIS;
        private double multiplier = 1.5d;
        private double randomizationFactor = 0.5d;

        protected Builder() {
        }

        public ExponentialBackOffPolicy build() {
            return new ExponentialBackOffPolicy(this.initialIntervalMillis, this.randomizationFactor, this.multiplier, this.maxIntervalMillis, this.maxElapsedTimeMillis);
        }

        public final int getInitialIntervalMillis() {
            return this.initialIntervalMillis;
        }

        public Builder setInitialIntervalMillis(int initialIntervalMillis) {
            this.initialIntervalMillis = initialIntervalMillis;
            return this;
        }

        public final double getRandomizationFactor() {
            return this.randomizationFactor;
        }

        public Builder setRandomizationFactor(double randomizationFactor) {
            this.randomizationFactor = randomizationFactor;
            return this;
        }

        public final double getMultiplier() {
            return this.multiplier;
        }

        public Builder setMultiplier(double multiplier) {
            this.multiplier = multiplier;
            return this;
        }

        public final int getMaxIntervalMillis() {
            return this.maxIntervalMillis;
        }

        public Builder setMaxIntervalMillis(int maxIntervalMillis) {
            this.maxIntervalMillis = maxIntervalMillis;
            return this;
        }

        public final int getMaxElapsedTimeMillis() {
            return this.maxElapsedTimeMillis;
        }

        public Builder setMaxElapsedTimeMillis(int maxElapsedTimeMillis) {
            this.maxElapsedTimeMillis = maxElapsedTimeMillis;
            return this;
        }
    }

    public ExponentialBackOffPolicy() {
        this(500, 0.5d, 1.5d, DEFAULT_MAX_INTERVAL_MILLIS, DEFAULT_MAX_ELAPSED_TIME_MILLIS);
    }

    ExponentialBackOffPolicy(int initialIntervalMillis, double randomizationFactor, double multiplier, int maxIntervalMillis, int maxElapsedTimeMillis) {
        boolean z;
        boolean z2 = true;
        Preconditions.checkArgument(initialIntervalMillis > 0);
        if (0.0d > randomizationFactor || randomizationFactor >= 1.0d) {
            z = false;
        } else {
            z = true;
        }
        Preconditions.checkArgument(z);
        if (multiplier >= 1.0d) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z);
        if (maxIntervalMillis >= initialIntervalMillis) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z);
        if (maxElapsedTimeMillis <= 0) {
            z2 = false;
        }
        Preconditions.checkArgument(z2);
        this.initialIntervalMillis = initialIntervalMillis;
        this.randomizationFactor = randomizationFactor;
        this.multiplier = multiplier;
        this.maxIntervalMillis = maxIntervalMillis;
        this.maxElapsedTimeMillis = maxElapsedTimeMillis;
        reset();
    }

    public boolean isBackOffRequired(int statusCode) {
        switch (statusCode) {
            case 500:
            case 503:
                return true;
            default:
                return false;
        }
    }

    public final void reset() {
        this.currentIntervalMillis = this.initialIntervalMillis;
        this.startTimeNanos = System.nanoTime();
    }

    public long getNextBackOffMillis() throws IOException {
        if (getElapsedTimeMillis() > ((long) this.maxElapsedTimeMillis)) {
            return -1;
        }
        int randomizedInterval = getRandomValueFromInterval(this.randomizationFactor, Math.random(), this.currentIntervalMillis);
        incrementCurrentInterval();
        return (long) randomizedInterval;
    }

    static int getRandomValueFromInterval(double randomizationFactor, double random, int currentIntervalMillis) {
        double delta = randomizationFactor * ((double) currentIntervalMillis);
        double minInterval = ((double) currentIntervalMillis) - delta;
        return (int) (((((((double) currentIntervalMillis) + delta) - minInterval) + 1.0d) * random) + minInterval);
    }

    public final int getInitialIntervalMillis() {
        return this.initialIntervalMillis;
    }

    public final double getRandomizationFactor() {
        return this.randomizationFactor;
    }

    public final int getCurrentIntervalMillis() {
        return this.currentIntervalMillis;
    }

    public final double getMultiplier() {
        return this.multiplier;
    }

    public final int getMaxIntervalMillis() {
        return this.maxIntervalMillis;
    }

    public final int getMaxElapsedTimeMillis() {
        return this.maxElapsedTimeMillis;
    }

    public final long getElapsedTimeMillis() {
        return (System.nanoTime() - this.startTimeNanos) / 1000000;
    }

    private void incrementCurrentInterval() {
        if (((double) this.currentIntervalMillis) >= ((double) this.maxIntervalMillis) / this.multiplier) {
            this.currentIntervalMillis = this.maxIntervalMillis;
        } else {
            this.currentIntervalMillis = (int) (((double) this.currentIntervalMillis) * this.multiplier);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
