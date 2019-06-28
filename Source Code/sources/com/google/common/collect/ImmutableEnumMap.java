package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class ImmutableEnumMap<K extends Enum<K>, V> extends ImmutableMap<K, V> {
    private final transient EnumMap<K, V> delegate;

    private static class EnumSerializedForm<K extends Enum<K>, V> implements Serializable {
        private static final long serialVersionUID = 0;
        final EnumMap<K, V> delegate;

        EnumSerializedForm(EnumMap<K, V> delegate) {
            this.delegate = delegate;
        }

        Object readResolve() {
            return new ImmutableEnumMap(this.delegate);
        }
    }

    /* renamed from: com.google.common.collect.ImmutableEnumMap$1 */
    class C05721 extends ImmutableSet<K> {
        C05721() {
        }

        public boolean contains(Object object) {
            return ImmutableEnumMap.this.delegate.containsKey(object);
        }

        public int size() {
            return ImmutableEnumMap.this.size();
        }

        public UnmodifiableIterator<K> iterator() {
            return Iterators.unmodifiableIterator(ImmutableEnumMap.this.delegate.keySet().iterator());
        }

        boolean isPartialView() {
            return true;
        }
    }

    /* renamed from: com.google.common.collect.ImmutableEnumMap$2 */
    class C06592 extends ImmutableMapEntrySet<K, V> {

        /* renamed from: com.google.common.collect.ImmutableEnumMap$2$1 */
        class C03641 extends UnmodifiableIterator<Entry<K, V>> {
            private final Iterator<Entry<K, V>> backingIterator = ImmutableEnumMap.this.delegate.entrySet().iterator();

            C03641() {
            }

            public boolean hasNext() {
                return this.backingIterator.hasNext();
            }

            public Entry<K, V> next() {
                Entry<K, V> entry = (Entry) this.backingIterator.next();
                return Maps.immutableEntry(entry.getKey(), entry.getValue());
            }
        }

        C06592() {
        }

        ImmutableMap<K, V> map() {
            return ImmutableEnumMap.this;
        }

        public UnmodifiableIterator<Entry<K, V>> iterator() {
            return new C03641();
        }
    }

    static <K extends Enum<K>, V> ImmutableMap<K, V> asImmutable(EnumMap<K, V> map) {
        switch (map.size()) {
            case 0:
                return ImmutableMap.of();
            case 1:
                Entry<K, V> entry = (Entry) Iterables.getOnlyElement(map.entrySet());
                return ImmutableMap.of(entry.getKey(), entry.getValue());
            default:
                return new ImmutableEnumMap(map);
        }
    }

    private ImmutableEnumMap(EnumMap<K, V> delegate) {
        this.delegate = delegate;
        Preconditions.checkArgument(!delegate.isEmpty());
    }

    ImmutableSet<K> createKeySet() {
        return new C05721();
    }

    public int size() {
        return this.delegate.size();
    }

    public boolean containsKey(@Nullable Object key) {
        return this.delegate.containsKey(key);
    }

    public V get(Object key) {
        return this.delegate.get(key);
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return new C06592();
    }

    boolean isPartialView() {
        return false;
    }

    Object writeReplace() {
        return new EnumSerializedForm(this.delegate);
    }
}
