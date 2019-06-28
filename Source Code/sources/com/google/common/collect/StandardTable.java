package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.Table.Cell;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
class StandardTable<R, C, V> implements Table<R, C, V>, Serializable {
    private static final long serialVersionUID = 0;
    @GwtTransient
    final Map<R, Map<C, V>> backingMap;
    private transient CellSet cellSet;
    private transient Set<C> columnKeySet;
    private transient ColumnMap columnMap;
    @GwtTransient
    final Supplier<? extends Map<C, V>> factory;
    private transient RowKeySet rowKeySet;
    private transient RowMap rowMap;
    private transient Values values;

    private class CellIterator implements Iterator<Cell<R, C, V>> {
        Iterator<Entry<C, V>> columnIterator;
        Entry<R, Map<C, V>> rowEntry;
        final Iterator<Entry<R, Map<C, V>>> rowIterator;

        private CellIterator() {
            this.rowIterator = StandardTable.this.backingMap.entrySet().iterator();
            this.columnIterator = Iterators.emptyModifiableIterator();
        }

        public boolean hasNext() {
            return this.rowIterator.hasNext() || this.columnIterator.hasNext();
        }

        public Cell<R, C, V> next() {
            if (!this.columnIterator.hasNext()) {
                this.rowEntry = (Entry) this.rowIterator.next();
                this.columnIterator = ((Map) this.rowEntry.getValue()).entrySet().iterator();
            }
            Entry<C, V> columnEntry = (Entry) this.columnIterator.next();
            return Tables.immutableCell(this.rowEntry.getKey(), columnEntry.getKey(), columnEntry.getValue());
        }

        public void remove() {
            this.columnIterator.remove();
            if (((Map) this.rowEntry.getValue()).isEmpty()) {
                this.rowIterator.remove();
            }
        }
    }

    class Row extends AbstractMap<C, V> {
        Map<C, V> backingRowMap;
        Set<Entry<C, V>> entrySet;
        Set<C> keySet;
        final R rowKey;

        /* renamed from: com.google.common.collect.StandardTable$Row$1 */
        class C06211 extends KeySet<C, V> {
            C06211() {
            }

            Map<C, V> map() {
                return Row.this;
            }
        }

        private class RowEntrySet extends EntrySet<C, V> {
            private RowEntrySet() {
            }

            Map<C, V> map() {
                return Row.this;
            }

            public int size() {
                Map<C, V> map = Row.this.backingRowMap();
                return map == null ? 0 : map.size();
            }

            public Iterator<Entry<C, V>> iterator() {
                Map<C, V> map = Row.this.backingRowMap();
                if (map == null) {
                    return Iterators.emptyModifiableIterator();
                }
                final Iterator<Entry<C, V>> iterator = map.entrySet().iterator();
                return new Iterator<Entry<C, V>>() {
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    public Entry<C, V> next() {
                        final Entry<C, V> entry = (Entry) iterator.next();
                        return new ForwardingMapEntry<C, V>() {
                            protected Entry<C, V> delegate() {
                                return entry;
                            }

                            public V setValue(V value) {
                                return super.setValue(Preconditions.checkNotNull(value));
                            }

                            public boolean equals(Object object) {
                                return standardEquals(object);
                            }
                        };
                    }

                    public void remove() {
                        iterator.remove();
                        Row.this.maintainEmptyInvariant();
                    }
                };
            }
        }

        Row(R rowKey) {
            this.rowKey = Preconditions.checkNotNull(rowKey);
        }

        Map<C, V> backingRowMap() {
            if (this.backingRowMap != null && (!this.backingRowMap.isEmpty() || !StandardTable.this.backingMap.containsKey(this.rowKey))) {
                return this.backingRowMap;
            }
            Map<C, V> computeBackingRowMap = computeBackingRowMap();
            this.backingRowMap = computeBackingRowMap;
            return computeBackingRowMap;
        }

        Map<C, V> computeBackingRowMap() {
            return (Map) StandardTable.this.backingMap.get(this.rowKey);
        }

        void maintainEmptyInvariant() {
            if (backingRowMap() != null && this.backingRowMap.isEmpty()) {
                StandardTable.this.backingMap.remove(this.rowKey);
                this.backingRowMap = null;
            }
        }

        public boolean containsKey(Object key) {
            Map<C, V> backingRowMap = backingRowMap();
            return (key == null || backingRowMap == null || !Maps.safeContainsKey(backingRowMap, key)) ? false : true;
        }

        public V get(Object key) {
            Map<C, V> backingRowMap = backingRowMap();
            return (key == null || backingRowMap == null) ? null : Maps.safeGet(backingRowMap, key);
        }

        public V put(C key, V value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
            if (this.backingRowMap == null || this.backingRowMap.isEmpty()) {
                return StandardTable.this.put(this.rowKey, key, value);
            }
            return this.backingRowMap.put(key, value);
        }

        public V remove(Object key) {
            Map<C, V> backingRowMap = backingRowMap();
            if (backingRowMap == null) {
                return null;
            }
            V result = Maps.safeRemove(backingRowMap, key);
            maintainEmptyInvariant();
            return result;
        }

        public void clear() {
            Map<C, V> backingRowMap = backingRowMap();
            if (backingRowMap != null) {
                backingRowMap.clear();
            }
            maintainEmptyInvariant();
        }

        public Set<C> keySet() {
            Set<C> set = this.keySet;
            if (set != null) {
                return set;
            }
            set = new C06211();
            this.keySet = set;
            return set;
        }

        public Set<Entry<C, V>> entrySet() {
            Set<Entry<C, V>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = new RowEntrySet();
            this.entrySet = set;
            return set;
        }
    }

    private abstract class TableCollection<T> extends AbstractCollection<T> {
        private TableCollection() {
        }

        public boolean isEmpty() {
            return StandardTable.this.backingMap.isEmpty();
        }

        public void clear() {
            StandardTable.this.backingMap.clear();
        }
    }

    private abstract class TableSet<T> extends AbstractSet<T> {
        private TableSet() {
        }

        public boolean isEmpty() {
            return StandardTable.this.backingMap.isEmpty();
        }

        public void clear() {
            StandardTable.this.backingMap.clear();
        }
    }

    private class CellSet extends TableSet<Cell<R, C, V>> {
        private CellSet() {
            super();
        }

        public Iterator<Cell<R, C, V>> iterator() {
            return new CellIterator();
        }

        public int size() {
            return StandardTable.this.size();
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Cell)) {
                return false;
            }
            Cell<?, ?, ?> cell = (Cell) obj;
            return StandardTable.this.containsMapping(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }

        public boolean remove(Object obj) {
            if (!(obj instanceof Cell)) {
                return false;
            }
            Cell<?, ?, ?> cell = (Cell) obj;
            return StandardTable.this.removeMapping(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }
    }

    private class Column extends ImprovedAbstractMap<R, V> {
        final C columnKey;
        com.google.common.collect.StandardTable$Column.Values columnValues;
        com.google.common.collect.StandardTable$Column.KeySet keySet;

        class Values extends AbstractCollection<V> {
            Values() {
            }

            public Iterator<V> iterator() {
                return Maps.valueIterator(Column.this.entrySet().iterator());
            }

            public int size() {
                return Column.this.entrySet().size();
            }

            public boolean isEmpty() {
                return !StandardTable.this.containsColumn(Column.this.columnKey);
            }

            public void clear() {
                Column.this.entrySet().clear();
            }

            public boolean remove(Object obj) {
                if (obj == null) {
                    return false;
                }
                Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
                while (iterator.hasNext()) {
                    Map<C, V> map = (Map) iterator.next();
                    if (map.entrySet().remove(new ImmutableEntry(Column.this.columnKey, obj))) {
                        if (map.isEmpty()) {
                            iterator.remove();
                        }
                        return true;
                    }
                }
                return false;
            }

            public boolean removeAll(final Collection<?> c) {
                Preconditions.checkNotNull(c);
                return Column.this.removePredicate(new Predicate<Entry<R, V>>() {
                    public boolean apply(Entry<R, V> entry) {
                        return c.contains(entry.getValue());
                    }
                });
            }

            public boolean retainAll(final Collection<?> c) {
                Preconditions.checkNotNull(c);
                return Column.this.removePredicate(new Predicate<Entry<R, V>>() {
                    public boolean apply(Entry<R, V> entry) {
                        return !c.contains(entry.getValue());
                    }
                });
            }
        }

        class EntrySet extends ImprovedAbstractSet<Entry<R, V>> {
            EntrySet() {
            }

            public Iterator<Entry<R, V>> iterator() {
                return new EntrySetIterator();
            }

            public int size() {
                int size = 0;
                for (Map<C, V> map : StandardTable.this.backingMap.values()) {
                    if (map.containsKey(Column.this.columnKey)) {
                        size++;
                    }
                }
                return size;
            }

            public boolean isEmpty() {
                return !StandardTable.this.containsColumn(Column.this.columnKey);
            }

            public void clear() {
                Column.this.removePredicate(Predicates.alwaysTrue());
            }

            public boolean contains(Object o) {
                if (!(o instanceof Entry)) {
                    return false;
                }
                Entry<?, ?> entry = (Entry) o;
                return StandardTable.this.containsMapping(entry.getKey(), Column.this.columnKey, entry.getValue());
            }

            public boolean remove(Object obj) {
                if (!(obj instanceof Entry)) {
                    return false;
                }
                Entry<?, ?> entry = (Entry) obj;
                return StandardTable.this.removeMapping(entry.getKey(), Column.this.columnKey, entry.getValue());
            }

            public boolean retainAll(Collection<?> c) {
                return Column.this.removePredicate(Predicates.not(Predicates.in(c)));
            }
        }

        class KeySet extends ImprovedAbstractSet<R> {
            KeySet() {
            }

            public Iterator<R> iterator() {
                return Maps.keyIterator(Column.this.entrySet().iterator());
            }

            public int size() {
                return Column.this.entrySet().size();
            }

            public boolean isEmpty() {
                return !StandardTable.this.containsColumn(Column.this.columnKey);
            }

            public boolean contains(Object obj) {
                return StandardTable.this.contains(obj, Column.this.columnKey);
            }

            public boolean remove(Object obj) {
                return StandardTable.this.remove(obj, Column.this.columnKey) != null;
            }

            public void clear() {
                Column.this.entrySet().clear();
            }

            public boolean retainAll(final Collection<?> c) {
                Preconditions.checkNotNull(c);
                return Column.this.removePredicate(new Predicate<Entry<R, V>>() {
                    public boolean apply(Entry<R, V> entry) {
                        return !c.contains(entry.getKey());
                    }
                });
            }
        }

        class EntrySetIterator extends AbstractIterator<Entry<R, V>> {
            final Iterator<Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();

            EntrySetIterator() {
            }

            protected Entry<R, V> computeNext() {
                while (this.iterator.hasNext()) {
                    final Entry<R, Map<C, V>> entry = (Entry) this.iterator.next();
                    if (((Map) entry.getValue()).containsKey(Column.this.columnKey)) {
                        return new AbstractMapEntry<R, V>() {
                            public R getKey() {
                                return entry.getKey();
                            }

                            public V getValue() {
                                return ((Map) entry.getValue()).get(Column.this.columnKey);
                            }

                            public V setValue(V value) {
                                return ((Map) entry.getValue()).put(Column.this.columnKey, Preconditions.checkNotNull(value));
                            }
                        };
                    }
                }
                return (Entry) endOfData();
            }
        }

        Column(C columnKey) {
            this.columnKey = Preconditions.checkNotNull(columnKey);
        }

        public V put(R key, V value) {
            return StandardTable.this.put(key, this.columnKey, value);
        }

        public V get(Object key) {
            return StandardTable.this.get(key, this.columnKey);
        }

        public boolean containsKey(Object key) {
            return StandardTable.this.contains(key, this.columnKey);
        }

        public V remove(Object key) {
            return StandardTable.this.remove(key, this.columnKey);
        }

        public Set<Entry<R, V>> createEntrySet() {
            return new EntrySet();
        }

        public Collection<V> values() {
            Collection<V> collection = this.columnValues;
            if (collection != null) {
                return collection;
            }
            Collection values = new Values();
            this.columnValues = values;
            return values;
        }

        boolean removePredicate(Predicate<? super Entry<R, V>> predicate) {
            boolean changed = false;
            Iterator<Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<R, Map<C, V>> entry = (Entry) iterator.next();
                Map<C, V> map = (Map) entry.getValue();
                V value = map.get(this.columnKey);
                if (value != null && predicate.apply(new ImmutableEntry(entry.getKey(), value))) {
                    map.remove(this.columnKey);
                    changed = true;
                    if (map.isEmpty()) {
                        iterator.remove();
                    }
                }
            }
            return changed;
        }

        public Set<R> keySet() {
            Set<R> set = this.keySet;
            if (set != null) {
                return set;
            }
            Set keySet = new KeySet();
            this.keySet = keySet;
            return keySet;
        }
    }

    private class ColumnKeySet extends TableSet<C> {
        private ColumnKeySet() {
            super();
        }

        public Iterator<C> iterator() {
            return StandardTable.this.createColumnKeyIterator();
        }

        public int size() {
            return Iterators.size(iterator());
        }

        public boolean remove(Object obj) {
            if (obj == null) {
                return false;
            }
            boolean changed = false;
            Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
            while (iterator.hasNext()) {
                Map<C, V> map = (Map) iterator.next();
                if (map.keySet().remove(obj)) {
                    changed = true;
                    if (map.isEmpty()) {
                        iterator.remove();
                    }
                }
            }
            return changed;
        }

        public boolean removeAll(Collection<?> c) {
            Preconditions.checkNotNull(c);
            boolean changed = false;
            Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
            while (iterator.hasNext()) {
                Map<C, V> map = (Map) iterator.next();
                if (Iterators.removeAll(map.keySet().iterator(), c)) {
                    changed = true;
                    if (map.isEmpty()) {
                        iterator.remove();
                    }
                }
            }
            return changed;
        }

        public boolean retainAll(Collection<?> c) {
            Preconditions.checkNotNull(c);
            boolean changed = false;
            Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
            while (iterator.hasNext()) {
                Map<C, V> map = (Map) iterator.next();
                if (map.keySet().retainAll(c)) {
                    changed = true;
                    if (map.isEmpty()) {
                        iterator.remove();
                    }
                }
            }
            return changed;
        }

        public boolean contains(Object obj) {
            if (obj == null) {
                return false;
            }
            for (Map<C, V> map : StandardTable.this.backingMap.values()) {
                if (map.containsKey(obj)) {
                    return true;
                }
            }
            return false;
        }
    }

    private class ColumnMap extends ImprovedAbstractMap<C, Map<R, V>> {
        com.google.common.collect.StandardTable$ColumnMap.ColumnMapValues columnMapValues;

        class ColumnMapEntrySet extends TableSet<Entry<C, Map<R, V>>> {
            ColumnMapEntrySet() {
                super();
            }

            public Iterator<Entry<C, Map<R, V>>> iterator() {
                return new TransformedIterator<C, Entry<C, Map<R, V>>>(StandardTable.this.columnKeySet().iterator()) {
                    Entry<C, Map<R, V>> transform(C columnKey) {
                        return new ImmutableEntry(columnKey, StandardTable.this.column(columnKey));
                    }
                };
            }

            public int size() {
                return StandardTable.this.columnKeySet().size();
            }

            public boolean contains(Object obj) {
                if (obj instanceof Entry) {
                    Entry<?, ?> entry = (Entry) obj;
                    if (StandardTable.this.containsColumn(entry.getKey())) {
                        return ColumnMap.this.get(entry.getKey()).equals(entry.getValue());
                    }
                }
                return false;
            }

            public boolean remove(Object obj) {
                if (!contains(obj)) {
                    return false;
                }
                StandardTable.this.removeColumn(((Entry) obj).getKey());
                return true;
            }

            public boolean removeAll(Collection<?> c) {
                boolean changed = false;
                for (Object obj : c) {
                    changed |= remove(obj);
                }
                return changed;
            }

            public boolean retainAll(Collection<?> c) {
                boolean changed = false;
                Iterator i$ = Lists.newArrayList(StandardTable.this.columnKeySet().iterator()).iterator();
                while (i$.hasNext()) {
                    C columnKey = i$.next();
                    if (!c.contains(new ImmutableEntry(columnKey, StandardTable.this.column(columnKey)))) {
                        StandardTable.this.removeColumn(columnKey);
                        changed = true;
                    }
                }
                return changed;
            }
        }

        private class ColumnMapValues extends TableCollection<Map<R, V>> {
            private ColumnMapValues() {
                super();
            }

            public Iterator<Map<R, V>> iterator() {
                return Maps.valueIterator(ColumnMap.this.entrySet().iterator());
            }

            public boolean remove(Object obj) {
                for (Entry<C, Map<R, V>> entry : ColumnMap.this.entrySet()) {
                    if (((Map) entry.getValue()).equals(obj)) {
                        StandardTable.this.removeColumn(entry.getKey());
                        return true;
                    }
                }
                return false;
            }

            public boolean removeAll(Collection<?> c) {
                Preconditions.checkNotNull(c);
                boolean changed = false;
                Iterator i$ = Lists.newArrayList(StandardTable.this.columnKeySet().iterator()).iterator();
                while (i$.hasNext()) {
                    C columnKey = i$.next();
                    if (c.contains(StandardTable.this.column(columnKey))) {
                        StandardTable.this.removeColumn(columnKey);
                        changed = true;
                    }
                }
                return changed;
            }

            public boolean retainAll(Collection<?> c) {
                Preconditions.checkNotNull(c);
                boolean changed = false;
                Iterator i$ = Lists.newArrayList(StandardTable.this.columnKeySet().iterator()).iterator();
                while (i$.hasNext()) {
                    C columnKey = i$.next();
                    if (!c.contains(StandardTable.this.column(columnKey))) {
                        StandardTable.this.removeColumn(columnKey);
                        changed = true;
                    }
                }
                return changed;
            }

            public int size() {
                return StandardTable.this.columnKeySet().size();
            }
        }

        private ColumnMap() {
        }

        public Map<R, V> get(Object key) {
            return StandardTable.this.containsColumn(key) ? StandardTable.this.column(key) : null;
        }

        public boolean containsKey(Object key) {
            return StandardTable.this.containsColumn(key);
        }

        public Map<R, V> remove(Object key) {
            return StandardTable.this.containsColumn(key) ? StandardTable.this.removeColumn(key) : null;
        }

        public Set<Entry<C, Map<R, V>>> createEntrySet() {
            return new ColumnMapEntrySet();
        }

        public Set<C> keySet() {
            return StandardTable.this.columnKeySet();
        }

        public Collection<Map<R, V>> values() {
            Collection<Map<R, V>> collection = this.columnMapValues;
            if (collection != null) {
                return collection;
            }
            Collection columnMapValues = new ColumnMapValues();
            this.columnMapValues = columnMapValues;
            return columnMapValues;
        }
    }

    class RowKeySet extends TableSet<R> {
        RowKeySet() {
            super();
        }

        public Iterator<R> iterator() {
            return Maps.keyIterator(StandardTable.this.rowMap().entrySet().iterator());
        }

        public int size() {
            return StandardTable.this.backingMap.size();
        }

        public boolean contains(Object obj) {
            return StandardTable.this.containsRow(obj);
        }

        public boolean remove(Object obj) {
            return (obj == null || StandardTable.this.backingMap.remove(obj) == null) ? false : true;
        }
    }

    class RowMap extends ImprovedAbstractMap<R, Map<C, V>> {

        class EntrySet extends TableSet<Entry<R, Map<C, V>>> {
            EntrySet() {
                super();
            }

            public Iterator<Entry<R, Map<C, V>>> iterator() {
                return new TransformedIterator<R, Entry<R, Map<C, V>>>(StandardTable.this.backingMap.keySet().iterator()) {
                    Entry<R, Map<C, V>> transform(R rowKey) {
                        return new ImmutableEntry(rowKey, StandardTable.this.row(rowKey));
                    }
                };
            }

            public int size() {
                return StandardTable.this.backingMap.size();
            }

            public boolean contains(Object obj) {
                if (!(obj instanceof Entry)) {
                    return false;
                }
                Entry<?, ?> entry = (Entry) obj;
                if (entry.getKey() != null && (entry.getValue() instanceof Map) && Collections2.safeContains(StandardTable.this.backingMap.entrySet(), entry)) {
                    return true;
                }
                return false;
            }

            public boolean remove(Object obj) {
                if (!(obj instanceof Entry)) {
                    return false;
                }
                Entry<?, ?> entry = (Entry) obj;
                if (entry.getKey() != null && (entry.getValue() instanceof Map) && StandardTable.this.backingMap.entrySet().remove(entry)) {
                    return true;
                }
                return false;
            }
        }

        RowMap() {
        }

        public boolean containsKey(Object key) {
            return StandardTable.this.containsRow(key);
        }

        public Map<C, V> get(Object key) {
            return StandardTable.this.containsRow(key) ? StandardTable.this.row(key) : null;
        }

        public Set<R> keySet() {
            return StandardTable.this.rowKeySet();
        }

        public Map<C, V> remove(Object key) {
            return key == null ? null : (Map) StandardTable.this.backingMap.remove(key);
        }

        protected Set<Entry<R, Map<C, V>>> createEntrySet() {
            return new EntrySet();
        }
    }

    private class Values extends TableCollection<V> {
        private Values() {
            super();
        }

        public Iterator<V> iterator() {
            return new TransformedIterator<Cell<R, C, V>, V>(StandardTable.this.cellSet().iterator()) {
                V transform(Cell<R, C, V> cell) {
                    return cell.getValue();
                }
            };
        }

        public int size() {
            return StandardTable.this.size();
        }
    }

    private class ColumnKeyIterator extends AbstractIterator<C> {
        Iterator<Entry<C, V>> entryIterator;
        final Iterator<Map<C, V>> mapIterator;
        final Map<C, V> seen;

        private ColumnKeyIterator() {
            this.seen = (Map) StandardTable.this.factory.get();
            this.mapIterator = StandardTable.this.backingMap.values().iterator();
            this.entryIterator = Iterators.emptyIterator();
        }

        protected C computeNext() {
            while (true) {
                if (this.entryIterator.hasNext()) {
                    Entry<C, V> entry = (Entry) this.entryIterator.next();
                    if (!this.seen.containsKey(entry.getKey())) {
                        this.seen.put(entry.getKey(), entry.getValue());
                        return entry.getKey();
                    }
                } else if (!this.mapIterator.hasNext()) {
                    return endOfData();
                } else {
                    this.entryIterator = ((Map) this.mapIterator.next()).entrySet().iterator();
                }
            }
        }
    }

    StandardTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
        this.backingMap = backingMap;
        this.factory = factory;
    }

    public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
        if (rowKey == null || columnKey == null) {
            return false;
        }
        Map<C, V> map = (Map) Maps.safeGet(this.backingMap, rowKey);
        if (map == null || !Maps.safeContainsKey(map, columnKey)) {
            return false;
        }
        return true;
    }

    public boolean containsColumn(@Nullable Object columnKey) {
        if (columnKey == null) {
            return false;
        }
        for (Map<C, V> map : this.backingMap.values()) {
            if (Maps.safeContainsKey(map, columnKey)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsRow(@Nullable Object rowKey) {
        return rowKey != null && Maps.safeContainsKey(this.backingMap, rowKey);
    }

    public boolean containsValue(@Nullable Object value) {
        if (value == null) {
            return false;
        }
        for (Map<C, V> map : this.backingMap.values()) {
            if (map.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
        if (rowKey == null || columnKey == null) {
            return null;
        }
        Map<C, V> map = (Map) Maps.safeGet(this.backingMap, rowKey);
        if (map != null) {
            return Maps.safeGet(map, columnKey);
        }
        return null;
    }

    public boolean isEmpty() {
        return this.backingMap.isEmpty();
    }

    public int size() {
        int size = 0;
        for (Map<C, V> map : this.backingMap.values()) {
            size += map.size();
        }
        return size;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Table)) {
            return false;
        }
        return cellSet().equals(((Table) obj).cellSet());
    }

    public int hashCode() {
        return cellSet().hashCode();
    }

    public String toString() {
        return rowMap().toString();
    }

    public void clear() {
        this.backingMap.clear();
    }

    private Map<C, V> getOrCreate(R rowKey) {
        Map<C, V> map = (Map) this.backingMap.get(rowKey);
        if (map != null) {
            return map;
        }
        map = (Map) this.factory.get();
        this.backingMap.put(rowKey, map);
        return map;
    }

    public V put(R rowKey, C columnKey, V value) {
        Preconditions.checkNotNull(rowKey);
        Preconditions.checkNotNull(columnKey);
        Preconditions.checkNotNull(value);
        return getOrCreate(rowKey).put(columnKey, value);
    }

    public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
        for (Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
            put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }
    }

    public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
        V v = null;
        if (!(rowKey == null || columnKey == null)) {
            Map<C, V> map = (Map) Maps.safeGet(this.backingMap, rowKey);
            if (map != null) {
                v = map.remove(columnKey);
                if (map.isEmpty()) {
                    this.backingMap.remove(rowKey);
                }
            }
        }
        return v;
    }

    private Map<R, V> removeColumn(Object column) {
        Map<R, V> output = new LinkedHashMap();
        Iterator<Entry<R, Map<C, V>>> iterator = this.backingMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<R, Map<C, V>> entry = (Entry) iterator.next();
            V value = ((Map) entry.getValue()).remove(column);
            if (value != null) {
                output.put(entry.getKey(), value);
                if (((Map) entry.getValue()).isEmpty()) {
                    iterator.remove();
                }
            }
        }
        return output;
    }

    private boolean containsMapping(Object rowKey, Object columnKey, Object value) {
        return value != null && value.equals(get(rowKey, columnKey));
    }

    private boolean removeMapping(Object rowKey, Object columnKey, Object value) {
        if (!containsMapping(rowKey, columnKey, value)) {
            return false;
        }
        remove(rowKey, columnKey);
        return true;
    }

    public Set<Cell<R, C, V>> cellSet() {
        Set<Cell<R, C, V>> set = this.cellSet;
        if (set != null) {
            return set;
        }
        Set cellSet = new CellSet();
        this.cellSet = cellSet;
        return cellSet;
    }

    public Map<C, V> row(R rowKey) {
        return new Row(rowKey);
    }

    public Map<R, V> column(C columnKey) {
        return new Column(columnKey);
    }

    public Set<R> rowKeySet() {
        Set<R> set = this.rowKeySet;
        if (set != null) {
            return set;
        }
        Set rowKeySet = new RowKeySet();
        this.rowKeySet = rowKeySet;
        return rowKeySet;
    }

    public Set<C> columnKeySet() {
        Set<C> set = this.columnKeySet;
        if (set != null) {
            return set;
        }
        set = new ColumnKeySet();
        this.columnKeySet = set;
        return set;
    }

    Iterator<C> createColumnKeyIterator() {
        return new ColumnKeyIterator();
    }

    public Collection<V> values() {
        Collection<V> collection = this.values;
        if (collection != null) {
            return collection;
        }
        Collection values = new Values();
        this.values = values;
        return values;
    }

    public Map<R, Map<C, V>> rowMap() {
        Map<R, Map<C, V>> map = this.rowMap;
        if (map != null) {
            return map;
        }
        Map rowMap = new RowMap();
        this.rowMap = rowMap;
        return rowMap;
    }

    public Map<C, Map<R, V>> columnMap() {
        Map<C, Map<R, V>> map = this.columnMap;
        if (map != null) {
            return map;
        }
        Map columnMap = new ColumnMap();
        this.columnMap = columnMap;
        return columnMap;
    }
}
