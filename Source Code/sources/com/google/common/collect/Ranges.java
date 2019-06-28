package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Deprecated
@Beta
public final class Ranges {
    private Ranges() {
    }

    public static <C extends Comparable<?>> Range<C> open(C lower, C upper) {
        return Range.open(lower, upper);
    }

    public static <C extends Comparable<?>> Range<C> closed(C lower, C upper) {
        return Range.closed(lower, upper);
    }

    public static <C extends Comparable<?>> Range<C> closedOpen(C lower, C upper) {
        return Range.closedOpen(lower, upper);
    }

    public static <C extends Comparable<?>> Range<C> openClosed(C lower, C upper) {
        return Range.openClosed(lower, upper);
    }

    public static <C extends Comparable<?>> Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType) {
        return Range.range(lower, lowerType, upper, upperType);
    }

    public static <C extends Comparable<?>> Range<C> lessThan(C endpoint) {
        return Range.lessThan(endpoint);
    }

    public static <C extends Comparable<?>> Range<C> atMost(C endpoint) {
        return Range.atMost(endpoint);
    }

    public static <C extends Comparable<?>> Range<C> upTo(C endpoint, BoundType boundType) {
        return Range.upTo(endpoint, boundType);
    }

    public static <C extends Comparable<?>> Range<C> greaterThan(C endpoint) {
        return Range.greaterThan(endpoint);
    }

    public static <C extends Comparable<?>> Range<C> atLeast(C endpoint) {
        return Range.atLeast(endpoint);
    }

    public static <C extends Comparable<?>> Range<C> downTo(C endpoint, BoundType boundType) {
        return Range.downTo(endpoint, boundType);
    }

    public static <C extends Comparable<?>> Range<C> all() {
        return Range.all();
    }

    public static <C extends Comparable<?>> Range<C> singleton(C value) {
        return Range.singleton(value);
    }

    public static <C extends Comparable<?>> Range<C> encloseAll(Iterable<C> values) {
        return Range.encloseAll(values);
    }
}
