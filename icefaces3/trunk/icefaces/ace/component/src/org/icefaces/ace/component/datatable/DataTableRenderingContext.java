package org.icefaces.ace.component.datatable;

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.model.table.RowStateMap;

import java.util.List;
import java.util.Map;

public class DataTableRenderingContext {
    private DataTable table;

    private String paginatorPosition;
    private Boolean paginator;
    private Boolean scrollable;
    private Boolean staticHeaders;
    private RowStateMap stateMap;
    private String rowIndexVar;
    private Integer rows;
    private Integer first;
    private Integer pagPose;
    private Map<Object, List<String>> rowToSelectedFieldsMap;
    private List<Column> columns;
    private String rowStateVar;
    private String rowStyleClass;
    private String selectionMode;
    private Boolean resizableColumns;
    private Integer scrollHeight;
    private String var;

    public DataTableRenderingContext(DataTable table) {
        this.table = table;

        paginatorPosition = table.getPaginatorPosition();
        paginator = table.isPaginator();
        scrollable = table.isScrollable();
        staticHeaders = table.isStaticHeaders();
        stateMap = table.getStateMap();
        rowIndexVar = table.getRowIndexVar();
        rows = table.getRows();
        first = table.getFirst();
        pagPose = table.getPage();
        rowToSelectedFieldsMap = table.getRowToSelectedFieldsMap();
        columns = table.getColumns();
        rowStateVar = table.getRowStateVar();
        rowStyleClass = table.getRowStyleClass();
        selectionMode = table.getSelectionMode();
        resizableColumns = table.isResizableColumns();
        scrollHeight = table.getScrollHeight();
        var = table.getVar();
    }

    public DataTable getTable() {
        return table;
    }

    public String getPaginatorPosition() {
        return paginatorPosition;
    }

    public Boolean getPaginator() {
        return paginator;
    }

    public Boolean getScrollable() {
        return scrollable;
    }

    public Boolean getStaticHeaders() {
        return staticHeaders &&  getScrollable();
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

    public Integer getFirst() {
        return first;
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

    public Boolean getResizableColumns() {
        return resizableColumns;
    }

    public Integer getScrollHeight() {
        return scrollHeight;
    }

    public String getVar() {
        return var;
    }
}
