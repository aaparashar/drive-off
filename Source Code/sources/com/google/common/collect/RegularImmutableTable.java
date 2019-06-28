package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Table.Cell;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@GwtCompatible
abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {
    private transient ImmutableSet<Cell<R, C, V>> cellSet;
    private transient ImmutableCollection<V> values;

    private static abstract class ImmutableArrayMap<K, V> extends ImmutableMap<K, V> {
        private final int size;

        /* renamed from: com.google.common.collect.RegularImmutableTable$ImmutableArrayMap$1 */
        class C06741 extends ImmutableMapEntrySet<K, V> {
            C06741() {
            }

            ImmutableMap<K, V> map() {
                return ImmutableArrayMap.this;
            }

            public UnmodifiableIterator<Entry<K, V>> iterator() {
                return new AbstractIndexedListIterator<Entry<K, V>>(size()) {
                    protected Entry<K, V> get(int index) {
                        return Maps.immutableEntry(ImmutableArrayMap.this.getKey(index), ImmutableArrayMap.this.getValue(index));
                    }
                };
            }
        }

        /* renamed from: com.google.common.collect.RegularImmutableTable$ImmutableArrayMap$2 */
        class C06752 extends ImmutableMapEntrySet<K, V> {

            /* renamed from: com.google.common.collect.RegularImmutableTable$ImmutableArrayMap$2$1 */
            class C06181 extends AbstractIterator<Entry<K, V>> {
                private int index = -1;
                private final int maxIndex = ImmutableArrayMap.this.keyToIndex().size();

                C06181() {
                }

                protected Entry<K, V> computeNext() {
                    this.index++;
                    while (this.index < this.maxIndex) {
                        V value = ImmutableArrayMap.this.getValue(this.index);
                        if (value != null) {
                            return Maps.immutableEntry(ImmutableArrayMap.this.getKey(this.index), value);
                        }
                        this.index++;
                    }
                    return (Entry) endOfData();
                }
            }

            C06752() {
            }

            ImmutableMap<K, V> map() {
                return ImmutableArrayMap.this;
            }

            public UnmodifiableIterator<Entry<K, V>> iterator() {
                return new C06181();
            }
        }

        @Nullable
        abstract V getValue(int i);

        abstract ImmutableMap<K, Integer> keyToIndex();

        ImmutableArrayMap(int size) {
            this.size = size;
        }

        private boolean isFull() {
            return this.size == keyToIndex().size();
        }

        K getKey(int index) {
            return keyToIndex().keySet().asList().get(index);
        }

        ImmutableSet<K> createKeySet() {
            return isFull() ? keyToIndex().keySet() : super.createKeySet();
        }

        public int size() {
            return this.size;
        }

        public V get(@Nullable Object key) {
            Integer keyIndex = (Integer) keyToIndex().get(key);
            return keyIndex == null ? null : getValue(keyIndex.intValue());
        }

        ImmutableSet<Entry<K, V>> createEntrySet() {
            return isFull() ? new C06741() : new C06752();
        }
    }

    abstract class CellSet extends ImmutableSet<Cell<R, C, V>> {
        CellSet() {
        }

        public int size() {
            return RegularImmutableTable.this.size();
        }

        public boolean contains(@Nullable Object object) {
            if (!(object instanceof Cell)) {
                return false;
            }
            Cell<?, ?, ?> cell = (Cell) object;
            Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
            if (value == null || !value.equals(cell.getValue())) {
                return false;
            }
            return true;
        }

        boolean isPartialView() {
            return false;
        }
    }

    @VisibleForTesting
    @Immutable
    static final class DenseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
        private final int[] columnCounts = new int[this.columnKeyToIndex.size()];
        private final ImmutableMap<C, Integer> columnKeyToIndex;
        private final ImmutableMap<C, Map<R, V>> columnMap;
        private final int[] iterationOrderColumn;
        private final int[] iterationOrderRow;
        private final int[] rowCounts = new int[this.rowKeyToIndex.size()];
        private final ImmutableMap<R, Integer> rowKeyToIndex;
        private final ImmutableMap<R, Map<C, V>> rowMap;
        private final V[][] values;

        /* renamed from: com.google.common.collect.RegularImmutableTable$DenseImmutableTable$1 */
        class C06171 extends ImmutableList<V> {
            C06171() {
            }

            public int size() {
                return DenseImmutableTable.this.iterationOrderRow.length;
            }

            public V get(int index) {
                return DenseImmutableTable.this.values[DenseImmutableTable.this.iterationOrderRow[index]][DenseImmutableTable.this.iterationOrderColumn[index]];
            }

            boolean isPartialView() {
                return true;
            }
        }

        private final class Column extends ImmutableArrayMap<R, V> {
            private final int columnIndex;

            Column(int columnIndex) {
                super(DenseImmutableTable.this.columnCounts[columnIndex]);
                this.columnIndex = columnIndex;
            }

            ImmutableMap<R, Integer> keyToIndex() {
                return DenseImmutableTable.this.rowKeyToIndex;
            }

            V getValue(int keyIndex) {
                return DenseImmutableTable.this.values[keyIndex][this.columnIndex];
            }

            boolean isPartialView() {
                return true;
            }
        }

        private final class ColumnMap extends ImmutableArrayMap<C, Map<R, V>> {
            private ColumnMap() {
                super(DenseImmutableTable.this.columnCounts.length);
            }

            ImmutableMap<C, Integer> keyToIndex() {
                return DenseImmutableTable.this.columnKeyToIndex;
            }

            Map<R, V> getValue(int keyIndex) {
                return new Column(keyIndex);
            }

            boolean isPartialView() {
                return false;
            }
        }

        private final class Row extends ImmutableArrayMap<C, V> {
            private final int rowIndex;

            Row(int rowIndex) {
                super(DenseImmutableTable.this.rowCounts[rowIndex]);
                this.rowIndex = rowIndex;
            }

            ImmutableMap<C, Integer> keyToIndex() {
                return DenseImmutableTable.this.columnKeyToIndex;
            }

            V getValue(int keyIndex) {
                return DenseImmutableTable.this.values[this.rowIndex][keyIndex];
            }

            boolean isPartialView() {
                return true;
            }
        }

        private final class RowMap extends ImmutableArrayMap<R, Map<C, V>> {
            private RowMap() {
                super(DenseImmutableTable.this.rowCounts.length);
            }

            ImmutableMap<R, Integer> keyToIndex() {
                return DenseImmutableTable.this.rowKeyToIndex;
            }

            Map<C, V> getValue(int keyIndex) {
                return new Row(keyIndex);
            }

            boolean isPartialView() {
                return false;
            }
        }

        class DenseCellSet extends CellSet {

            /* renamed from: com.google.common.collect.RegularImmutableTable$DenseImmutableTable$DenseCellSet$1 */
            class C06721 extends ImmutableAsList<Cell<R, C, V>> {
                C06721() {
                }

                public Cell<R, C, V> get(int index) {
                    int rowIndex = DenseImmutableTable.this.iterationOrderRow[index];
                    int columnIndex = DenseImmutableTable.this.iterationOrderColumn[index];
                    return Tables.immutableCell(DenseImmutableTable.this.rowKeySet().asList().get(rowIndex), DenseImmutableTable.this.columnKeySet().asList().get(columnIndex), DenseImmutableTable.this.values[rowIndex][columnIndex]);
                }

                ImmutableCollection<Cell<R, C, V>> delegateCollection() {
                    return DenseCellSet.this;
                }
            }

            DenseCellSet() {
                super();
            }

            public UnmodifiableIterator<Cell<R, C, V>> iterator() {
                return asList().iterator();
            }

            ImmutableList<Cell<R, C, V>> createAsList() {
                return new C06721();
            }
        }

        public /* bridge */ /* synthetic */ Set cellSet() {
            return super.cellSet();
        }

        public /* bridge */ /* synthetic */ Collection values() {
            return super.values();
        }

        private static <E> ImmutableMap<E, Integer> makeIndex(ImmutableSet<E> set) {
            Builder<E, Integer> indexBuilder = ImmutableMap.builder();
            int i = 0;
            Iterator i$ = set.iterator();
            while (i$.hasNext()) {
                indexBuilder.put(i$.next(), Integer.valueOf(i));
                i++;
            }
            return indexBuilder.build();
        }

        DenseImmutableTable(ImmutableList<Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
            super();
            this.values = (Object[][]) ((Object[][]) Array.newInstance(Object.class, new int[]{rowSpace.size(), columnSpace.size()}));
            this.rowKeyToIndex = makeIndex(rowSpace);
            this.columnKeyToIndex = makeIndex(columnSpace);
            int[] iterationOrderRow = new int[cellList.size()];
            int[] iterationOrderColumn = new int[cellList.size()];
            for (int i = 0; i < cellList.size(); i++) {
                Cell<R, C, V> cell = (Cell) cellList.get(i);
                R rowKey = cell.getRowKey();
                C columnKey = cell.getColumnKey();
                int rowIndex = ((Integer) this.rowKeyToIndex.get(rowKey)).intValue();
                int columnIndex = ((Integer) this.columnKeyToIndex.get(columnKey)).intValue();
                Preconditions.checkArgument(this.values[rowIndex][columnIndex] == null, "duplicate key: (%s, %s)", rowKey, columnKey);
                this.values[rowIndex][columnIndex] = cell.getValue();
                int[] iArr = this.rowCounts;
                iArr[rowIndex] = iArr[rowIndex] + 1;
                iArr = this.columnCounts;
                iArr[columnIndex] = iArr[columnIndex] + 1;
                iterationOrderRow[i] = rowIndex;
                iterationOrderColumn[i] = columnIndex;
            }
            this.iterationOrderRow = iterationOrderRow;
            this.iterationOrderColumn = iterationOrderColumn;
            this.rowMap = new RowMap();
            this.columnMap = new ColumnMap();
        }

        public ImmutableMap<R, V> column(C columnKey) {
            Integer columnIndex = (Integer) this.columnKeyToIndex.get(Preconditions.checkNotNull(columnKey));
            if (columnIndex == null) {
                return ImmutableMap.of();
            }
            return new Column(columnIndex.intValue());
        }

        public ImmutableSet<C> columnKeySet() {
            return this.columnKeyToIndex.keySet();
        }

        public ImmutableMap<C, Map<R, V>> columnMap() {
            return this.columnMap;
        }

        public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
            return get(rowKey, columnKey) != null;
        }

        public boolean containsColumn(@Nullable Object columnKey) {
            return this.columnKeyToIndex.containsKey(columnKey);
        }

        public boolean containsRow(@Nullable Object rowKey) {
            return this.rowKeyToIndex.containsKey(rowKey);
        }

        public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
            Integer rowIndex = (Integer) this.rowKeyToIndex.get(rowKey);
            Integer columnIndex = (Integer) this.columnKeyToIndex.get(columnKey);
            return (rowIndex == null || columnIndex == null) ? null : this.values[rowIndex.intValue()][columnIndex.intValue()];
        }

        public ImmutableMap<C, V> row(R rowKey) {
            Preconditions.checkNotNull(rowKey);
            Integer rowIndex = (Integer) this.rowKeyToIndex.get(rowKey);
            if (rowIndex == null) {
                return ImmutableMap.of();
            }
            return new Row(rowIndex.intValue());
        }

        public ImmutableSet<R> rowKeySet() {
            return this.rowKeyToIndex.keySet();
        }

        public ImmutableMap<R, Map<C, V>> rowMap() {
            return this.rowMap;
        }

        ImmutableCollection<V> createValues() {
            return new C06171();
        }

        public int size() {
            return this.iterationOrderRow.length;
        }

        ImmutableSet<Cell<R, C, V>> createCellSet() {
            return new DenseCellSet();
        }
    }

    @VisibleForTesting
    @Immutable
    static final class SparseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
        private final ImmutableMap<C, Map<R, V>> columnMap;
        private final int[] iterationOrderColumn;
        private final int[] iterationOrderRow;
        private final ImmutableMap<R, Map<C, V>> rowMap;

        /* renamed from: com.google.common.collect.RegularImmutableTable$SparseImmutableTable$1 */
        class C06191 extends ImmutableList<V> {
            C06191() {
            }

            public int size() {
                return SparseImmutableTable.this.iterationOrderRow.length;
            }

            public V get(int index) {
                ImmutableMap<C, V> row = (ImmutableMap) SparseImmutableTable.this.rowMap.values().asList().get(SparseImmutableTable.this.iterationOrderRow[index]);
                return row.values().asList().get(SparseImmutableTable.this.iterationOrderColumn[index]);
            }

            boolean isPartialView() {
                return true;
            }
        }

        class SparseCellSet extends CellSet {

            /* renamed from: com.google.common.collect.RegularImmutableTable$SparseImmutableTable$SparseCellSet$1 */
            class C06761 extends ImmutableAsList<Cell<R, C, V>> {
                C06761() {
                }

                public Cell<R, C, V> get(int index) {
                    Entry<R, Map<C, V>> rowEntry = (Entry) SparseImmutableTable.this.rowMap.entrySet().asList().get(SparseImmutableTable.this.iterationOrderRow[index]);
                    ImmutableMap<C, V> row = (ImmutableMap) rowEntry.getValue();
                    Entry<C, V> colEntry = (Entry) row.entrySet().asList().get(SparseImmutableTable.this.iterationOrderColumn[index]);
                    return Tables.immutableCell(rowEntry.getKey(), colEntry.getKey(), colEntry.getValue());
                }

                ImmutableCollection<Cell<R, C, V>> delegateCollection() {
                    return SparseCellSet.this;
                }
            }

            SparseCellSet() {
                super();
            }

            public UnmodifiableIterator<Cell<R, C, V>> iterator() {
                return asList().iterator();
            }

            ImmutableList<Cell<R, C, V>> createAsList() {
                return new C06761();
            }
        }

        public /* bridge */ /* synthetic */ Set cellSet() {
            return super.cellSet();
        }

        public /* bridge */ /* synthetic */ Collection values() {
            return super.values();
        }

        SparseImmutableTable(ImmutableList<Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
            super();
            Map<R, Integer> rowIndex = Maps.newHashMap();
            Map<R, Map<C, V>> rows = Maps.newLinkedHashMap();
            Iterator i$ = rowSpace.iterator();
            while (i$.hasNext()) {
                R row = i$.next();
                rowIndex.put(row, Integer.valueOf(rows.size()));
                rows.put(row, new LinkedHashMap());
            }
            Map<C, Map<R, V>> columns = Maps.newLinkedHashMap();
            i$ = columnSpace.iterator();
            while (i$.hasNext()) {
                columns.put(i$.next(), new LinkedHashMap());
            }
            int[] iterationOrderRow = new int[cellList.size()];
            int[] iterationOrderColumn = new int[cellList.size()];
            for (int i = 0; i < cellList.size(); i++) {
                Cell<R, C, V> cell = (Cell) cellList.get(i);
                R rowKey = cell.getRowKey();
                C columnKey = cell.getColumnKey();
                V value = cell.getValue();
                iterationOrderRow[i] = ((Integer) rowIndex.get(rowKey)).intValue();
                Map<C, V> thisRow = (Map) rows.get(rowKey);
                iterationOrderColumn[i] = thisRow.size();
                V oldValue = thisRow.put(columnKey, value);
                if (oldValue != null) {
                    throw new IllegalArgumentException("Duplicate value for row=" + rowKey + ", column=" + columnKey + ": " + value + ", " + oldValue);
                }
                ((Map) columns.get(columnKey)).put(rowKey, value);
            }
            this.iterationOrderRow = iterationOrderRow;
            this.iterationOrderColumn = iterationOrderColumn;
            Builder<R, Map<C, V>> rowBuilder = ImmutableMap.builder();
            for (Entry<R, Map<C, V>> row2 : rows.entrySet()) {
                rowBuilder.put(row2.getKey(), ImmutableMap.copyOf((Map) row2.getValue()));
            }
            this.rowMap = rowBuilder.build();
            Builder<C, Map<R, V>> columnBuilder = ImmutableMap.builder();
            for (Entry<C, Map<R, V>> col : columns.entrySet()) {
                columnBuilder.put(col.getKey(), ImmutableMap.copyOf((Map) col.getValue()));
            }
            this.columnMap = columnBuilder.build();
        }

        public ImmutableMap<R, V> column(C columnKey) {
            Preconditions.checkNotNull(columnKey);
            return (ImmutableMap) Objects.firstNonNull((ImmutableMap) this.columnMap.get(columnKey), ImmutableMap.of());
        }

        public ImmutableSet<C> columnKeySet() {
            return this.columnMap.keySet();
        }

        public ImmutableMap<C, Map<R, V>> columnMap() {
            return this.columnMap;
        }

        public ImmutableMap<C, V> row(R rowKey) {
            Preconditions.checkNotNull(rowKey);
            return (ImmutableMap) Objects.firstNonNull((ImmutableMap) this.rowMap.get(rowKey), ImmutableMap.of());
        }

        public ImmutableSet<R> rowKeySet() {
            return this.rowMap.keySet();
        }

        public ImmutableMap<R, Map<C, V>> rowMap() {
            return this.rowMap;
        }

        public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
            Map<C, V> row = (Map) this.rowMap.get(rowKey);
            return row != null && row.containsKey(columnKey);
        }

        public boolean containsColumn(@Nullable Object columnKey) {
            return this.columnMap.containsKey(columnKey);
        }

        public boolean containsRow(@Nullable Object rowKey) {
            return this.rowMap.containsKey(rowKey);
        }

        public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
            Map<C, V> row = (Map) this.rowMap.get(rowKey);
            return row == null ? null : row.get(columnKey);
        }

        ImmutableCollection<V> createValues() {
            return new C06191();
        }

        public int size() {
            return this.iterationOrderRow.length;
        }

        ImmutableSet<Cell<R, C, V>> createCellSet() {
            return new SparseCellSet();
        }
    }

    abstract ImmutableSet<Cell<R, C, V>> createCellSet();

    abstract ImmutableCollection<V> createValues();

    public abstract int size();

    private RegularImmutableTable() {
    }

    public final ImmutableCollection<V> values() {
        ImmutableCollection<V> immutableCollection = this.values;
        if (immutableCollection != null) {
            return immutableCollection;
        }
        immutableCollection = createValues();
        this.values = immutableCollection;
        return immutableCollection;
    }

    public final boolean containsValue(@Nullable Object value) {
        return values().contains(value);
    }

    public final ImmutableSet<Cell<R, C, V>> cellSet() {
        ImmutableSet<Cell<R, C, V>> immutableSet = this.cellSet;
        if (immutableSet != null) {
            return immutableSet;
        }
        immutableSet = createCellSet();
        this.cellSet = immutableSet;
        return immutableSet;
    }

    public final boolean isEmpty() {
        return false;
    }

    static final <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Cell<R, C, V>> cells, @Nullable final Comparator<? super R> rowComparator, @Nullable final Comparator<? super C> columnComparator) {
        Preconditions.checkNotNull(cells);
        if (!(rowComparator == null && columnComparator == null)) {
            Collections.sort(cells, new Comparator<Cell<R, C, V>>() {
                public int compare(Cell<R, C, V> cell1, Cell<R, C, V> cell2) {
                    int i = 0;
                    int rowCompare = rowComparator == null ? 0 : rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
                    if (rowCompare != 0) {
                        return rowCompare;
                    }
                    if (columnComparator != null) {
                        i = columnComparator.compare(cell1.getColumnKey(), cell2.getColumnKey());
                    }
                    return i;
                }
            });
        }
        return forCellsInternal(cells, rowComparator, columnComparator);
    }

    static final <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Cell<R, C, V>> cells) {
        return forCellsInternal(cells, null, null);
    }

    private static final <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Cell<R, C, V>> cells, @Nullable Comparator<? super R> rowComparator, @Nullable Comparator<? super C> columnComparator) {
        ImmutableSet.Builder<R> rowSpaceBuilder = ImmutableSet.builder();
        ImmutableSet.Builder<C> columnSpaceBuilder = ImmutableSet.builder();
        ImmutableList<Cell<R, C, V>> cellList = ImmutableList.copyOf((Iterable) cells);
        Iterator i$ = cellList.iterator();
        while (i$.hasNext()) {
            Cell<R, C, V> cell = (Cell) i$.next();
            rowSpaceBuilder.add(cell.getRowKey());
            columnSpaceBuilder.add(cell.getColumnKey());
        }
        ImmutableSet<R> rowSpace = rowSpaceBuilder.build();
        if (rowComparator != null) {
            Collection rowList = Lists.newArrayList((Iterable) rowSpace);
            Collections.sort(rowList, rowComparator);
            rowSpace = ImmutableSet.copyOf(rowList);
        }
        ImmutableSet<C> columnSpace = columnSpaceBuilder.build();
        if (columnComparator != null) {
            Collection columnList = Lists.newArrayList((Iterable) columnSpace);
            Collections.sort(columnList, columnComparator);
            columnSpace = ImmutableSet.copyOf(columnList);
        }
        return cellList.size() > (rowSpace.size() * columnSpace.size()) / 2 ? new DenseImmutableTable(cellList, rowSpace, columnSpace) : new SparseImmutableTable(cellList, rowSpace, columnSpace);
    }
}
