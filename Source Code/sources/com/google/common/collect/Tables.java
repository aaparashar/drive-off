package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Table.Cell;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
public final class Tables {
    private static final Function<? extends Map<?, ?>, ? extends Map<?, ?>> UNMODIFIABLE_WRAPPER = new C04641();

    /* renamed from: com.google.common.collect.Tables$1 */
    static class C04641 implements Function<Map<Object, Object>, Map<Object, Object>> {
        C04641() {
        }

        public Map<Object, Object> apply(Map<Object, Object> input) {
            return Collections.unmodifiableMap(input);
        }
    }

    static abstract class AbstractCell<R, C, V> implements Cell<R, C, V> {
        AbstractCell() {
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Cell)) {
                return false;
            }
            Cell<?, ?, ?> other = (Cell) obj;
            if (Objects.equal(getRowKey(), other.getRowKey()) && Objects.equal(getColumnKey(), other.getColumnKey()) && Objects.equal(getValue(), other.getValue())) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(getRowKey(), getColumnKey(), getValue());
        }

        public String toString() {
            return "(" + getRowKey() + "," + getColumnKey() + ")=" + getValue();
        }
    }

    private static class TransformedTable<R, C, V1, V2> implements Table<R, C, V2> {
        CellSet cellSet;
        Map<C, Map<R, V2>> columnMap;
        final Table<R, C, V1> fromTable;
        final Function<? super V1, V2> function;
        Map<R, Map<C, V2>> rowMap;
        Collection<V2> values;

        /* renamed from: com.google.common.collect.Tables$TransformedTable$1 */
        class C04651 implements Function<Cell<R, C, V1>, Cell<R, C, V2>> {
            C04651() {
            }

            public Cell<R, C, V2> apply(Cell<R, C, V1> cell) {
                return Tables.immutableCell(cell.getRowKey(), cell.getColumnKey(), TransformedTable.this.function.apply(cell.getValue()));
            }
        }

        /* renamed from: com.google.common.collect.Tables$TransformedTable$2 */
        class C04662 implements Function<Map<C, V1>, Map<C, V2>> {
            C04662() {
            }

            public Map<C, V2> apply(Map<C, V1> row) {
                return Maps.transformValues((Map) row, TransformedTable.this.function);
            }
        }

        /* renamed from: com.google.common.collect.Tables$TransformedTable$3 */
        class C04673 implements Function<Map<R, V1>, Map<R, V2>> {
            C04673() {
            }

            public Map<R, V2> apply(Map<R, V1> column) {
                return Maps.transformValues((Map) column, TransformedTable.this.function);
            }
        }

        class CellSet extends TransformedCollection<Cell<R, C, V1>, Cell<R, C, V2>> implements Set<Cell<R, C, V2>> {
            CellSet() {
                super(TransformedTable.this.fromTable.cellSet(), TransformedTable.this.cellFunction());
            }

            public boolean equals(Object obj) {
                return Sets.equalsImpl(this, obj);
            }

            public int hashCode() {
                return Sets.hashCodeImpl(this);
            }

            public boolean contains(Object obj) {
                if (!(obj instanceof Cell)) {
                    return false;
                }
                Cell<?, ?, ?> cell = (Cell) obj;
                if (!Objects.equal(cell.getValue(), TransformedTable.this.get(cell.getRowKey(), cell.getColumnKey()))) {
                    return false;
                }
                if (cell.getValue() != null || TransformedTable.this.fromTable.contains(cell.getRowKey(), cell.getColumnKey())) {
                    return true;
                }
                return false;
            }

            public boolean remove(Object obj) {
                if (!contains(obj)) {
                    return false;
                }
                Cell<?, ?, ?> cell = (Cell) obj;
                TransformedTable.this.fromTable.remove(cell.getRowKey(), cell.getColumnKey());
                return true;
            }
        }

        TransformedTable(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
            this.fromTable = (Table) Preconditions.checkNotNull(fromTable);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        public boolean contains(Object rowKey, Object columnKey) {
            return this.fromTable.contains(rowKey, columnKey);
        }

        public boolean containsRow(Object rowKey) {
            return this.fromTable.containsRow(rowKey);
        }

        public boolean containsColumn(Object columnKey) {
            return this.fromTable.containsColumn(columnKey);
        }

        public boolean containsValue(Object value) {
            return values().contains(value);
        }

        public V2 get(Object rowKey, Object columnKey) {
            return contains(rowKey, columnKey) ? this.function.apply(this.fromTable.get(rowKey, columnKey)) : null;
        }

        public boolean isEmpty() {
            return this.fromTable.isEmpty();
        }

        public int size() {
            return this.fromTable.size();
        }

        public void clear() {
            this.fromTable.clear();
        }

        public V2 put(R r, C c, V2 v2) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Table<? extends R, ? extends C, ? extends V2> table) {
            throw new UnsupportedOperationException();
        }

        public V2 remove(Object rowKey, Object columnKey) {
            return contains(rowKey, columnKey) ? this.function.apply(this.fromTable.remove(rowKey, columnKey)) : null;
        }

        public Map<C, V2> row(R rowKey) {
            return Maps.transformValues(this.fromTable.row(rowKey), this.function);
        }

        public Map<R, V2> column(C columnKey) {
            return Maps.transformValues(this.fromTable.column(columnKey), this.function);
        }

        Function<Cell<R, C, V1>, Cell<R, C, V2>> cellFunction() {
            return new C04651();
        }

        public Set<Cell<R, C, V2>> cellSet() {
            if (this.cellSet != null) {
                return this.cellSet;
            }
            Set cellSet = new CellSet();
            this.cellSet = cellSet;
            return cellSet;
        }

        public Set<R> rowKeySet() {
            return this.fromTable.rowKeySet();
        }

        public Set<C> columnKeySet() {
            return this.fromTable.columnKeySet();
        }

        public Collection<V2> values() {
            if (this.values != null) {
                return this.values;
            }
            Collection<V2> transform = Collections2.transform(this.fromTable.values(), this.function);
            this.values = transform;
            return transform;
        }

        Map<R, Map<C, V2>> createRowMap() {
            return Maps.transformValues(this.fromTable.rowMap(), new C04662());
        }

        public Map<R, Map<C, V2>> rowMap() {
            if (this.rowMap != null) {
                return this.rowMap;
            }
            Map<R, Map<C, V2>> createRowMap = createRowMap();
            this.rowMap = createRowMap;
            return createRowMap;
        }

        Map<C, Map<R, V2>> createColumnMap() {
            return Maps.transformValues(this.fromTable.columnMap(), new C04673());
        }

        public Map<C, Map<R, V2>> columnMap() {
            if (this.columnMap != null) {
                return this.columnMap;
            }
            Map<C, Map<R, V2>> createColumnMap = createColumnMap();
            this.columnMap = createColumnMap;
            return createColumnMap;
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
    }

    private static class TransposeTable<C, R, V> implements Table<C, R, V> {
        private static final Function<Cell<?, ?, ?>, Cell<?, ?, ?>> TRANSPOSE_CELL = new C04681();
        CellSet cellSet;
        final Table<R, C, V> original;

        /* renamed from: com.google.common.collect.Tables$TransposeTable$1 */
        static class C04681 implements Function<Cell<?, ?, ?>, Cell<?, ?, ?>> {
            C04681() {
            }

            public Cell<?, ?, ?> apply(Cell<?, ?, ?> cell) {
                return Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
            }
        }

        class CellSet extends TransformedCollection<Cell<R, C, V>, Cell<C, R, V>> implements Set<Cell<C, R, V>> {
            CellSet() {
                super(TransposeTable.this.original.cellSet(), TransposeTable.TRANSPOSE_CELL);
            }

            public boolean equals(Object obj) {
                if (obj == this) {
                    return true;
                }
                if (!(obj instanceof Set)) {
                    return false;
                }
                Set<?> os = (Set) obj;
                if (os.size() == size()) {
                    return containsAll(os);
                }
                return false;
            }

            public int hashCode() {
                return Sets.hashCodeImpl(this);
            }

            public boolean contains(Object obj) {
                if (!(obj instanceof Cell)) {
                    return false;
                }
                Cell<?, ?, ?> cell = (Cell) obj;
                return TransposeTable.this.original.cellSet().contains(Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue()));
            }

            public boolean remove(Object obj) {
                if (!(obj instanceof Cell)) {
                    return false;
                }
                Cell<?, ?, ?> cell = (Cell) obj;
                return TransposeTable.this.original.cellSet().remove(Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue()));
            }
        }

        TransposeTable(Table<R, C, V> original) {
            this.original = (Table) Preconditions.checkNotNull(original);
        }

        public void clear() {
            this.original.clear();
        }

        public Map<C, V> column(R columnKey) {
            return this.original.row(columnKey);
        }

        public Set<R> columnKeySet() {
            return this.original.rowKeySet();
        }

        public Map<R, Map<C, V>> columnMap() {
            return this.original.rowMap();
        }

        public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
            return this.original.contains(columnKey, rowKey);
        }

        public boolean containsColumn(@Nullable Object columnKey) {
            return this.original.containsRow(columnKey);
        }

        public boolean containsRow(@Nullable Object rowKey) {
            return this.original.containsColumn(rowKey);
        }

        public boolean containsValue(@Nullable Object value) {
            return this.original.containsValue(value);
        }

        public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
            return this.original.get(columnKey, rowKey);
        }

        public boolean isEmpty() {
            return this.original.isEmpty();
        }

        public V put(C rowKey, R columnKey, V value) {
            return this.original.put(columnKey, rowKey, value);
        }

        public void putAll(Table<? extends C, ? extends R, ? extends V> table) {
            this.original.putAll(Tables.transpose(table));
        }

        public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
            return this.original.remove(columnKey, rowKey);
        }

        public Map<R, V> row(C rowKey) {
            return this.original.column(rowKey);
        }

        public Set<C> rowKeySet() {
            return this.original.columnKeySet();
        }

        public Map<C, Map<R, V>> rowMap() {
            return this.original.columnMap();
        }

        public int size() {
            return this.original.size();
        }

        public Collection<V> values() {
            return this.original.values();
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

        public Set<Cell<C, R, V>> cellSet() {
            Set<Cell<C, R, V>> set = this.cellSet;
            if (set != null) {
                return set;
            }
            Set cellSet = new CellSet();
            this.cellSet = cellSet;
            return cellSet;
        }
    }

    static final class ImmutableCell<R, C, V> extends AbstractCell<R, C, V> implements Serializable {
        private static final long serialVersionUID = 0;
        private final C columnKey;
        private final R rowKey;
        private final V value;

        ImmutableCell(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
            this.rowKey = rowKey;
            this.columnKey = columnKey;
            this.value = value;
        }

        public R getRowKey() {
            return this.rowKey;
        }

        public C getColumnKey() {
            return this.columnKey;
        }

        public V getValue() {
            return this.value;
        }
    }

    private static class UnmodifiableTable<R, C, V> extends ForwardingTable<R, C, V> implements Serializable {
        private static final long serialVersionUID = 0;
        final Table<? extends R, ? extends C, ? extends V> delegate;

        UnmodifiableTable(Table<? extends R, ? extends C, ? extends V> delegate) {
            this.delegate = (Table) Preconditions.checkNotNull(delegate);
        }

        protected Table<R, C, V> delegate() {
            return this.delegate;
        }

        public Set<Cell<R, C, V>> cellSet() {
            return Collections.unmodifiableSet(super.cellSet());
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public Map<R, V> column(@Nullable C columnKey) {
            return Collections.unmodifiableMap(super.column(columnKey));
        }

        public Set<C> columnKeySet() {
            return Collections.unmodifiableSet(super.columnKeySet());
        }

        public Map<C, Map<R, V>> columnMap() {
            return Collections.unmodifiableMap(Maps.transformValues(super.columnMap(), Tables.unmodifiableWrapper()));
        }

        public V put(@Nullable R r, @Nullable C c, @Nullable V v) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
            throw new UnsupportedOperationException();
        }

        public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
            throw new UnsupportedOperationException();
        }

        public Map<C, V> row(@Nullable R rowKey) {
            return Collections.unmodifiableMap(super.row(rowKey));
        }

        public Set<R> rowKeySet() {
            return Collections.unmodifiableSet(super.rowKeySet());
        }

        public Map<R, Map<C, V>> rowMap() {
            return Collections.unmodifiableMap(Maps.transformValues(super.rowMap(), Tables.unmodifiableWrapper()));
        }

        public Collection<V> values() {
            return Collections.unmodifiableCollection(super.values());
        }
    }

    static final class UnmodifiableRowSortedMap<R, C, V> extends UnmodifiableTable<R, C, V> implements RowSortedTable<R, C, V> {
        private static final long serialVersionUID = 0;

        public UnmodifiableRowSortedMap(RowSortedTable<R, ? extends C, ? extends V> delegate) {
            super(delegate);
        }

        protected RowSortedTable<R, C, V> delegate() {
            return (RowSortedTable) super.delegate();
        }

        public SortedMap<R, Map<C, V>> rowMap() {
            return Collections.unmodifiableSortedMap(Maps.transformValues(delegate().rowMap(), Tables.unmodifiableWrapper()));
        }

        public SortedSet<R> rowKeySet() {
            return Collections.unmodifiableSortedSet(delegate().rowKeySet());
        }
    }

    private Tables() {
    }

    public static <R, C, V> Cell<R, C, V> immutableCell(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
        return new ImmutableCell(rowKey, columnKey, value);
    }

    public static <R, C, V> Table<C, R, V> transpose(Table<R, C, V> table) {
        return table instanceof TransposeTable ? ((TransposeTable) table).original : new TransposeTable(table);
    }

    @Beta
    public static <R, C, V> Table<R, C, V> newCustomTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
        Preconditions.checkArgument(backingMap.isEmpty());
        Preconditions.checkNotNull(factory);
        return new StandardTable(backingMap, factory);
    }

    @Beta
    public static <R, C, V1, V2> Table<R, C, V2> transformValues(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
        return new TransformedTable(fromTable, function);
    }

    public static <R, C, V> Table<R, C, V> unmodifiableTable(Table<? extends R, ? extends C, ? extends V> table) {
        return new UnmodifiableTable(table);
    }

    @Beta
    public static <R, C, V> RowSortedTable<R, C, V> unmodifiableRowSortedTable(RowSortedTable<R, ? extends C, ? extends V> table) {
        return new UnmodifiableRowSortedMap(table);
    }

    private static <K, V> Function<Map<K, V>, Map<K, V>> unmodifiableWrapper() {
        return UNMODIFIABLE_WRAPPER;
    }
}
