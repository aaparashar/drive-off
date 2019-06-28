package com.google.common.collect;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

abstract class AbstractRangeSet<C extends Comparable> implements RangeSet<C> {
    AbstractRangeSet() {
    }

    public boolean contains(C value) {
        return rangeContaining(value) != null;
    }

    public Range<C> rangeContaining(C value) {
        Preconditions.checkNotNull(value);
        for (Range<C> range : asRanges()) {
            if (range.contains(value)) {
                return range;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return asRanges().isEmpty();
    }

    public void add(Range<C> range) {
        throw new UnsupportedOperationException();
    }

    public void remove(Range<C> range) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        remove(Range.all());
    }

    public boolean enclosesAll(RangeSet<C> other) {
        for (Range<C> range : other.asRanges()) {
            if (!encloses(range)) {
                return false;
            }
        }
        return true;
    }

    public void addAll(RangeSet<C> other) {
        for (Range<C> range : other.asRanges()) {
            add(range);
        }
    }

    public void removeAll(RangeSet<C> other) {
        for (Range<C> range : other.asRanges()) {
            remove(range);
        }
    }

    public boolean encloses(Range<C> otherRange) {
        for (Range<C> range : asRanges()) {
            if (range.encloses(otherRange)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof RangeSet)) {
            return false;
        }
        return asRanges().equals(((RangeSet) obj).asRanges());
    }

    public final int hashCode() {
        return asRanges().hashCode();
    }

    public final String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('{');
        for (Range<C> range : asRanges()) {
            builder.append(range);
        }
        builder.append('}');
        return builder.toString();
    }
}
