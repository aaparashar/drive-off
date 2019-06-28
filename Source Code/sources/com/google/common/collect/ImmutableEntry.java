package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
class ImmutableEntry<K, V> extends AbstractMapEntry<K, V> implements Serializable {
    private static final long serialVersionUID = 0;
    private final K key;
    private final V value;

    ImmutableEntry(@Nullable K key, @Nullable V value) {
        this.key = key;
        this.value = value;
    }

    @Nullable
    public K getKey() {
        return this.key;
    }

    @Nullable
    public V getValue() {
        return this.value;
    }

    public final V setValue(V v) {
        throw new UnsupportedOperationException();
    }
}
