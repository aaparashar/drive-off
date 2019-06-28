package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Table.Cell;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
@Beta
public final class ArrayTable<R, C, V> implements Table<R, C, V>, Serializable {
    private static final long serialVersionUID = 0;
    private final V[][] array;
    private transient CellSet cellSet;
    private final ImmutableMap<C, Integer> columnKeyToIndex;
    private final ImmutableList<C> columnList;
    private transient ColumnMap columnMap;
    private final ImmutableMap<R, Integer> rowKeyToIndex;
    private final ImmutableList<R> rowList;
    private transient RowMap rowMap;
    private transient Collection<V> values;

    private class CellSet extends AbstractSet<Cell<R, C, V>> {
        private CellSet() {
        }

        public Iterator<Cell<R, C, V>> iterator() {
            return new AbstractIndexedListIterator<Cell<R, C, V>>(size()) {
                protected Cell<R, C, V> get(final int index) {
                    return new AbstractCell<R, C, V>() {
                        final int columnIndex = (index % ArrayTable.this.columnList.size());
                        final int rowIndex = (index / ArrayTable.this.columnList.size());

                        public R getRowKey() {
                            return ArrayTable.this.rowList.get(this.rowIndex);
                        }

                        public C getColumnKey() {
                            return ArrayTable.this.columnList.get(this.columnIndex);
                        }

                        public V getValue() {
                            return ArrayTable.this.at(this.rowIndex, this.columnIndex);
                        }
                    };
                }
            };
        }

        public int size() {
            return ArrayTable.this.size();
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Cell)) {
                return false;
            }
            Cell<?, ?, ?> cell = (Cell) obj;
            Integer rowIndex = (Integer) ArrayTable.this.rowKeyToIndex.get(cell.getRowKey());
            Integer columnIndex = (Integer) ArrayTable.this.columnKeyToIndex.get(cell.getColumnKey());
            if (rowIndex == null || columnIndex == null || !Objects.equal(ArrayTable.this.at(rowIndex.intValue(), columnIndex.intValue()), cell.getValue())) {
                return false;
            }
            return true;
        }
    }

    private class Values extends AbstractCollection<V> {
        private Values() {
        }

        public Iterator<V> iterator() {
            return new TransformedIterator<Cell<R, C, V>, V>(ArrayTable.this.cellSet().iterator()) {
                V transform(Cell<R, C, V> cell) {
                    return cell.getValue();
                }
            };
        }

        public int size() {
            return ArrayTable.this.size();
        }
    }

    private static abstract class ArrayMap<K, V> extends ImprovedAbstractMap<K, V> {
        private final ImmutableMap<K, Integer> keyIndex;

        /* renamed from: com.google.common.collect.ArrayTable$ArrayMap$1 */
        class C05611 extends EntrySet<K, V> {
            C05611() {
            }

            Map<K, V> map() {
                return ArrayMap.this;
            }

            public Iterator<Entry<K, V>> iterator() {
                return new AbstractIndexedListIterator<Entry<K, V>>(size()) {
                    protected Entry<K, V> get(final int index) {
                        return new AbstractMapEntry<K, V>() {
                            public K getKey() {
                                return ArrayMap.this.getKey(index);
                            }

                            public V getValue() {
                                return ArrayMap.this.getValue(index);
                            }

                            public V setValue(V value) {
                                return ArrayMap.this.setValue(index, value);
                            }
                        };
                    }
                };
            }
        }

        abstract String getKeyRole();

        @Nullable
        abstract V getValue(int i);

        @Nullable
        abstract V setValue(int i, V v);

        private ArrayMap(ImmutableMap<K, Integer> keyIndex) {
            this.keyIndex = keyIndex;
        }

        public Set<K> keySet() {
            return this.keyIndex.keySet();
        }

        K getKey(int index) {
            return this.keyIndex.keySet().asList().get(index);
        }

        public int size() {
            return this.keyIndex.size();
        }

        public boolean isEmpty() {
            return this.keyIndex.isEmpty();
        }

        protected Set<Entry<K, V>> createEntrySet() {
            return new C05611();
        }

        public boolean containsKey(@Nullable Object key) {
            return this.keyIndex.containsKey(key);
        }

        public V get(@Nullable Object key) {
            Integer index = (Integer) this.keyIndex.get(key);
            if (index == null) {
                return null;
            }
            return getValue(index.intValue());
        }

        public V put(K key, V value) {
            Integer index = (Integer) this.keyIndex.get(key);
            if (index != null) {
                return setValue(index.intValue(), value);
            }
            throw new IllegalArgumentException(getKeyRole() + " " + key + " not in " + this.keyIndex.keySet());
        }

        public V remove(Object key) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }
    }

    private class Column extends ArrayMap<R, V> {
        final int columnIndex;

        Column(int columnIndex) {
            super(ArrayTable.this.rowKeyToIndex);
            this.columnIndex = columnIndex;
        }

        String getKeyRole() {
            return "Row";
        }

        V getValue(int index) {
            return ArrayTable.this.at(index, this.columnIndex);
        }

        V setValue(int index, V newValue) {
            return ArrayTable.this.set(index, this.columnIndex, newValue);
        }
    }

    private class ColumnMap extends ArrayMap<C, Map<R, V>> {
        private ColumnMap() {
            super(ArrayTable.this.columnKeyToIndex);
        }

        String getKeyRole() {
            return "Column";
        }

        Map<R, V> getValue(int index) {
            return new Column(index);
        }

        Map<R, V> setValue(int index, Map<R, V> map) {
            throw new UnsupportedOperationException();
        }

        public Map<R, V> put(C c, Map<R, V> map) {
            throw new UnsupportedOperationException();
        }
    }

    private class Row extends ArrayMap<C, V> {
        final int rowIndex;

        Row(int rowIndex) {
            super(ArrayTable.this.columnKeyToIndex);
            this.rowIndex = rowIndex;
        }

        String getKeyRole() {
            return "Column";
        }

        V getValue(int index) {
            return ArrayTable.this.at(this.rowIndex, index);
        }

        V setValue(int index, V newValue) {
            return ArrayTable.this.set(this.rowIndex, index, newValue);
        }
    }

    private class RowMap extends ArrayMap<R, Map<C, V>> {
        private RowMap() {
            super(ArrayTable.this.rowKeyToIndex);
        }

        String getKeyRole() {
            return "Row";
        }

        Map<C, V> getValue(int index) {
            return new Row(index);
        }

        Map<C, V> setValue(int index, Map<C, V> map) {
            throw new UnsupportedOperationException();
        }

        public Map<C, V> put(R r, Map<C, V> map) {
            throw new UnsupportedOperationException();
        }
    }

    public static <R, C, V> ArrayTable<R, C, V> create(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
        return new ArrayTable(rowKeys, columnKeys);
    }

    public static <R, C, V> ArrayTable<R, C, V> create(Table<R, C, V> table) {
        return new ArrayTable((Table) table);
    }

    public static <R, C, V> ArrayTable<R, C, V> create(ArrayTable<R, C, V> table) {
        return new ArrayTable((ArrayTable) table);
    }

    private ArrayTable(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
        boolean z;
        boolean z2 = true;
        this.rowList = ImmutableList.copyOf((Iterable) rowKeys);
        this.columnList = ImmutableList.copyOf((Iterable) columnKeys);
        if (this.rowList.isEmpty()) {
            z = false;
        } else {
            z = true;
        }
        Preconditions.checkArgument(z);
        if (this.columnList.isEmpty()) {
            z2 = false;
        }
        Preconditions.checkArgument(z2);
        this.rowKeyToIndex = index(this.rowList);
        this.columnKeyToIndex = index(this.columnList);
        this.array = (Object[][]) ((Object[][]) Array.newInstance(Object.class, new int[]{this.rowList.size(), this.columnList.size()}));
        eraseAll();
    }

    private static <E> ImmutableMap<E, Integer> index(List<E> list) {
        Builder<E, Integer> columnBuilder = ImmutableMap.builder();
        for (int i = 0; i < list.size(); i++) {
            columnBuilder.put(list.get(i), Integer.valueOf(i));
        }
        return columnBuilder.build();
    }

    private ArrayTable(Table<R, C, V> table) {
        this(table.rowKeySet(), table.columnKeySet());
        putAll(table);
    }

    private ArrayTable(ArrayTable<R, C, V> table) {
        this.rowList = table.rowList;
        this.columnList = table.columnList;
        this.rowKeyToIndex = table.rowKeyToIndex;
        this.columnKeyToIndex = table.columnKeyToIndex;
        Object[][] copy = (Object[][]) ((Object[][]) Array.newInstance(Object.class, new int[]{this.rowList.size(), this.columnList.size()}));
        this.array = copy;
        eraseAll();
        for (int i = 0; i < this.rowList.size(); i++) {
            System.arraycopy(table.array[i], 0, copy[i], 0, table.array[i].length);
        }
    }

    public ImmutableList<R> rowKeyList() {
        return this.rowList;
    }

    public ImmutableList<C> columnKeyList() {
        return this.columnList;
    }

    public V at(int rowIndex, int columnIndex) {
        Preconditions.checkElementIndex(rowIndex, this.rowList.size());
        Preconditions.checkElementIndex(columnIndex, this.columnList.size());
        return this.array[rowIndex][columnIndex];
    }

    public V set(int rowIndex, int columnIndex, @Nullable V value) {
        Preconditions.checkElementIndex(rowIndex, this.rowList.size());
        Preconditions.checkElementIndex(columnIndex, this.columnList.size());
        V oldValue = this.array[rowIndex][columnIndex];
        this.array[rowIndex][columnIndex] = value;
        return oldValue;
    }

    @GwtIncompatible("reflection")
    public V[][] toArray(Class<V> valueClass) {
        Object[][] copy = (Object[][]) ((Object[][]) Array.newInstance(valueClass, new int[]{this.rowList.size(), this.columnList.size()}));
        for (int i = 0; i < this.rowList.size(); i++) {
            System.arraycopy(this.array[i], 0, copy[i], 0, this.array[i].length);
        }
        return copy;
    }

    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }

    public void eraseAll() {
        for (V[] row : this.array) {
            Arrays.fill(row, null);
        }
    }

    public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
        return containsRow(rowKey) && containsColumn(columnKey);
    }

    public boolean containsColumn(@Nullable Object columnKey) {
        return this.columnKeyToIndex.containsKey(columnKey);
    }

    public boolean containsRow(@Nullable Object rowKey) {
        return this.rowKeyToIndex.containsKey(rowKey);
    }

    public boolean containsValue(@Nullable Object value) {
        for (V[] arr$ : this.array) {
            for (V element : r0[r4]) {
                if (Objects.equal(value, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
        Integer rowIndex = (Integer) this.rowKeyToIndex.get(rowKey);
        Integer columnIndex = (Integer) this.columnKeyToIndex.get(columnKey);
        return (rowIndex == null || columnIndex == null) ? null : at(rowIndex.intValue(), columnIndex.intValue());
    }

    public boolean isEmpty() {
        return false;
    }

    public V put(R rowKey, C columnKey, @Nullable V value) {
        boolean z;
        Preconditions.checkNotNull(rowKey);
        Preconditions.checkNotNull(columnKey);
        Integer rowIndex = (Integer) this.rowKeyToIndex.get(rowKey);
        if (rowIndex != null) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "Row %s not in %s", rowKey, this.rowList);
        Integer columnIndex = (Integer) this.columnKeyToIndex.get(columnKey);
        if (columnIndex != null) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "Column %s not in %s", columnKey, this.columnList);
        return set(rowIndex.intValue(), columnIndex.intValue(), value);
    }

    public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
        for (Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
            put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }
    }

    @Deprecated
    public V remove(Object rowKey, Object columnKey) {
        throw new UnsupportedOperationException();
    }

    public V erase(@Nullable Object rowKey, @Nullable Object columnKey) {
        Integer rowIndex = (Integer) this.rowKeyToIndex.get(rowKey);
        Integer columnIndex = (Integer) this.columnKeyToIndex.get(columnKey);
        if (rowIndex == null || columnIndex == null) {
            return null;
        }
        return set(rowIndex.intValue(), columnIndex.intValue(), null);
    }

    public int size() {
        return this.rowList.size() * this.columnList.size();
    }

    public boolean equals(@Nullable Object obj) {
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

    public Set<Cell<R, C, V>> cellSet() {
        Set<Cell<R, C, V>> set = this.cellSet;
        if (set != null) {
            return set;
        }
        Set cellSet = new CellSet();
        this.cellSet = cellSet;
        return cellSet;
    }

    public Map<R, V> column(C columnKey) {
        Preconditions.checkNotNull(columnKey);
        Integer columnIndex = (Integer) this.columnKeyToIndex.get(columnKey);
        return columnIndex == null ? ImmutableMap.of() : new Column(columnIndex.intValue());
    }

    public ImmutableSet<C> columnKeySet() {
        return this.columnKeyToIndex.keySet();
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

    public Map<C, V> row(R rowKey) {
        Preconditions.checkNotNull(rowKey);
        Integer rowIndex = (Integer) this.rowKeyToIndex.get(rowKey);
        return rowIndex == null ? ImmutableMap.of() : new Row(rowIndex.intValue());
    }

    public ImmutableSet<R> rowKeySet() {
        return this.rowKeyToIndex.keySet();
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

    public Collection<V> values() {
        Collection<V> collection = this.values;
        if (collection != null) {
            return collection;
        }
        collection = new Values();
        this.values = collection;
        return collection;
    }
}
