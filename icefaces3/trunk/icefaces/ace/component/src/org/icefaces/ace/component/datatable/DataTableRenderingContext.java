package org.icefaces.ace.component.datatable;

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.model.table.RowStateMap;

import java.util.List;
import java.util.Map;

public class DataTableRenderingContext {
    private DataTable table;

    private String paginatorPosition;
    private boolean paginator;
    private boolean scrollable;
    private boolean staticHeaders;
    private RowStateMap stateMap;
    private String rowIndexVar;
    private Integer rows;
    private Integer firstRowIndex;
    private Integer pagPose;
    private Map<Object, List<String>> rowToSelectedFieldsMap;
    private List<Column> columns;
    private String rowStateVar;
    private String rowStyleClass;
    private String selectionMode;
    private boolean resizableColumns;
    private Integer scrollHeight;
    private String var;
    private boolean firstColumn;
    private boolean lastColumn;
    private boolean inHeaderSubrows;
    private boolean reorderableColumns;
    private boolean columnSortable;
    private boolean columnFilterable;
    private int tabIndex;

    public DataTableRenderingContext(DataTable table) {
        this.table = table;

        paginatorPosition = table.getPaginatorPosition();
        paginator = table.isPaginator();
        scrollable = table.isScrollable();
        staticHeaders = table.isStaticHeaders();
        stateMap = table.getStateMap();
        rowIndexVar = table.getRowIndexVar();
        rows = table.getRows();
        firstRowIndex = table.getFirst();
        pagPose = table.getPage();
        rowToSelectedFieldsMap = table.getRowToSelectedFieldsMap();
        columns = table.getColumns();
        rowStateVar = table.getRowStateVar();
        rowStyleClass = table.getRowStyleClass();
        selectionMode = table.getSelectionMode();
        resizableColumns = table.isResizableColumns();
        scrollHeight = table.getScrollHeight();
        reorderableColumns = table.isReorderableColumns();
        var = table.getVar();
        tabIndex = table.getTabIndex();
    }

    public DataTable getTable() {
        return table;
    }

    public String getPaginatorPosition() {
        return paginatorPosition;
    }

    public boolean isPaginator() {
        return paginator;
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public boolean isStaticHeaders() {
        return staticHeaders &&  isScrollable();
    }

    public RowStateMap getStateMap() {
        return stateMap;
    }

    public String getRowIndexVar() {
        return rowIndexVar;
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getFirstRowIndex() {
        return firstRowIndex;
    }

    public Integer getPagPose() {
        return pagPose;
    }

    public Map<Object, List<String>> getRowToSelectedFieldsMap() {
        return rowToSelectedFieldsMap;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public String getRowStateVar() {
        return rowStateVar;
    }

    public String getRowStyleClass() {
        return rowStyleClass;
    }

    public String getSelectionMode() {
        return selectionMode;
    }

    public boolean isResizableColumns() {
        return resizableColumns;
    }

    public Integer getScrollHeight() {
        return scrollHeight;
    }

    public String getVar() {
        return var;
    }

    public void setFirstColumn(boolean firstColumn) {
        this.firstColumn = firstColumn;
    }

    public boolean isFirstColumn() {
        return firstColumn;
    }

    public void setLastColumn(boolean b) {
        this.lastColumn = b;
    }

    public boolean isLastColumn() {
        return lastColumn;
    }

    public void setInHeaderSubrows(boolean headerSubrows) {
        this.inHeaderSubrows = headerSubrows;
    }

    public boolean isInHeaderSubrows() {
        return inHeaderSubrows;
    }

    public boolean isReorderableColumns() {
        return reorderableColumns;
    }

    public void setReorderableColumns(boolean reorderableColumns) {
        this.reorderableColumns = reorderableColumns;
    }

    public void setColumnSortable(boolean columnSortable) {
        this.columnSortable = columnSortable;
    }

    public boolean isColumnSortable() {
        return columnSortable;
    }

    public void setColumnFilterable(boolean columnFilterable) {
        this.columnFilterable = columnFilterable;
    }

    public boolean isColumnFilterable() {
        return columnFilterable;
    }

    public int getTabIndex() {
        if (tabIndex < 1) return 0;
        return tabIndex++;
    }
}
