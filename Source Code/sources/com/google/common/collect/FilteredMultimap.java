package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Map.Entry;

@GwtCompatible
abstract class FilteredMultimap<K, V> extends AbstractMultimap<K, V> {
    final Multimap<K, V> unfiltered;

    abstract Predicate<? super Entry<K, V>> entryPredicate();

    FilteredMultimap(Multimap<K, V> unfiltered) {
        this.unfiltered = (Multimap) Preconditions.checkNotNull(unfiltered);
    }
}
