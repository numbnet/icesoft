/*
* Original Code developed and contributed by Prime Technology.
* Subsequent Code Modifications Copyright 2011 ICEsoft Technologies Canada Corp. (c)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
*
* Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
*
* Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
* Contributors: ICEsoft Technologies Canada Corp. (c)
*
* Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
* Contributors: ______________________
* Contributors: ______________________
*/

package org.icefaces.ace.component.datatable;

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.columngroup.ColumnGroup;
import org.icefaces.ace.component.rowexpander.RowExpander;
import org.icefaces.ace.component.rowpanelexpander.RowPanelExpander;
import org.icefaces.ace.component.tableconfigpanel.TableConfigPanel;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.*;
import org.icefaces.ace.model.table.SortCriteria;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UINamingContainer;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PhaseId;
import javax.faces.model.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@ResourceDependencies({
        @ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.css"),
        @ResourceDependency(library="icefaces.ace", name="paginator/paginator.css"),
        @ResourceDependency(library="icefaces.ace", name="datatable/datatable.css"),
        @ResourceDependency(library="icefaces.ace", name="jquery/jquery.js"),
        @ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.js"),
        @ResourceDependency(library="icefaces.ace", name="yui/utilities/utilities.js"),
        @ResourceDependency(library="icefaces.ace", name="paginator/paginator.js"),
        @ResourceDependency(library="icefaces.ace", name="core/core.js"),
        @ResourceDependency(library="icefaces.ace", name="datatable/datatable.js")
})
public class DataTable extends DataTableBase {
    private static Logger log = Logger.getLogger(DataTable.class.getName());



    private Map<String, Column> filterMap;
    private List filteredData;

    // Saved like pseudo meta-class field; required because including another ace component in an ace meta is uncompilable
    private TableConfigPanel panel;
    private Object[] values;

    @Override
    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (values == null) {
            values = new Object[2];
        }
        values[0] = super.saveState(context);
        values[1] = panel;
        return (values);
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (state == null) {
            return;
        }
        values = (Object[]) state;
        super.restoreState(context, values[0]);
        panel = (TableConfigPanel) values[1];
    }

    public List<Column> getColumns() {
        ArrayList<Column> columns = new ArrayList<Column>();
        List<Integer> columnOrdering = getColumnOrdering();

        ArrayList<Column> unordered = new ArrayList<Column>();
        Stack childStack = new Stack<UIComponent>();
        childStack.add(this);
        while (!childStack.empty()) {
            for (UIComponent child : ((UIComponent)childStack.pop()).getChildren()) {
                if (!(child instanceof ColumnGroup) && !(child instanceof Column)) {
                    if (child.getChildren().size() > 0) childStack.add(child);
                } else if (child instanceof Column) unordered.add((Column) child);
            }
        }

        // Allow the ordering to grow beyond the current set of columns,
        // to allow persistence of order during column swaps.
        while (columnOrdering.size() < unordered.size()) columnOrdering.add(columnOrdering.size());

        for (Integer i : columnOrdering)
            if (i < unordered.size()) columns.add(unordered.get(i));

        return columns;
    }

    public List<Integer> getColumnOrdering() {
        List<Integer> superOrder = super.getColumnOrdering();
        if (superOrder == null || superOrder.size() == 0) {
            ArrayList<Column> columns = new ArrayList<Column>();
            Stack childStack = new Stack<UIComponent>();
            childStack.add(this);
            while (!childStack.empty()) {
                for (UIComponent child : ((UIComponent)childStack.pop()).getChildren()) {
                    if (!(child instanceof ColumnGroup) && !(child instanceof Column)) {
                        if (child.getChildren().size() > 0) childStack.add(child);
                    } else if (child instanceof Column) columns.add((Column) child);
                }
            }
            ArrayList<Integer> ordering = new ArrayList<Integer>();
            int i=0;
            for (Object o : columns) ordering.add(i++);
            setColumnOrdering(ordering);
            return ordering;
        } else return superOrder;
    }

    public Map<String,Column> getFilterMap() {
        if (filterMap == null) {
            filterMap = new HashMap<String,Column>();
            ColumnGroup group = getColumnGroup("header");
            if (group != null) {
                for (UIComponent child : group.getChildren())
                    if (child.isRendered()) for (UIComponent grandchild : child.getChildren())
                        if (grandchild.isRendered() && grandchild.getValueExpression("filterBy") != null)
                            filterMap.put(grandchild.getClientId(FacesContext.getCurrentInstance()) + "_filter", (Column)grandchild);
            } else
                for (Column column : getColumns())
                    if (column.getValueExpression("filterBy") != null)
                        filterMap.put(column.getClientId(FacesContext.getCurrentInstance()) + "_filter", column);
        }
        return filterMap;
    }

    public ColumnGroup getColumnGroup(String target) {
        for (UIComponent child : this.getChildren())
            if (child instanceof ColumnGroup) {
                ColumnGroup colGroup = (ColumnGroup) child;
                if (target.equals(colGroup.getType())) return colGroup;
            }
        return null;
    }

//    public String getColumnSelectionMode() {
//        String selectionMode;
//        for (Column column : getColumns())
//            if ((selectionMode = column.getSelectionMode()) != null)
//                return selectionMode;
//        return null;
//    }

    @Override
    public RowStateMap getStateMap() {
        if (internalStateMap != null) return internalStateMap;
        RowStateMap stateMap = super.getStateMap();
        if (stateMap == null) {
            stateMap = new RowStateMap();
            internalStateMap = stateMap;
        }
        return stateMap;
    }

    @Override
    protected DataModel getDataModel() {
        Object value = this.getValue();
        Integer hash = (value != null) ? value.hashCode() : null;

        // If value hash hash not changed, return the model state managed the superclass
        // If the value has not been set, let the superclass manage it
        // We manage the tree data model, as setDataModel doesn't appear to save our reference
        if (treeModel != null) {
            if (hash == null || hash.equals(valueHashCode)) return treeModel;
        }
        else if (hash == null || hash.equals(valueHashCode)) return super.getDataModel();

        // Save hash code of value to prevent tree-case handling except on value change.
        valueHashCode = hash;

        // If the value is a list of map entries, interpret it as a tree model
        if (value != null && value instanceof List) {
            List list = (List)value;
            if (list.size() > 0 && list.get(0) instanceof Map.Entry) {
                TreeDataModel model = new TreeDataModel(list);
                // This automated handling may fall short for some cases.
                // If the application is adding expandable rows as children
                // to expandable rows, during a partial render, their addition
                // wouldn't cause a change in hashCode, which wouldn't trigger
                // this convenience handling.
                if (this.getPanelExpansion() == null)
                    getStateMap().setExpandableByTreeModel(model);
                treeModel = model;
                return model;
            }
        }

        // Use standard javax data models for all other cases.
        return super.getDataModel();
    }

    // Public proxy
    public DataModel getModel() { return getDataModel(); }

    @Override
    public void broadcast(javax.faces.event.FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        FacesContext context = FacesContext.getCurrentInstance();
        String outcome = null;
        MethodExpression me = null;

        if      (event instanceof org.icefaces.ace.event.SelectEvent)   me = getRowSelectListener();
        else if (event instanceof org.icefaces.ace.event.UnselectEvent) me = getRowUnselectListener();
        else if (event instanceof org.icefaces.ace.event.RowEditEvent)  me = getRowEditListener();

        if (me != null) outcome = (String) me.invoke(context.getELContext(), new Object[] {event});

        if (outcome != null) {
            NavigationHandler navHandler = context.getApplication().getNavigationHandler();
            navHandler.handleNavigation(context, null, outcome);
            context.renderResponse();
        }
    }


    public void loadLazyData() {
        LazyDataModel model = (LazyDataModel) getDataModel();

        // TODO: Why is get rows passed here and the load method?
        model.setPageSize(getRows());
        List<?> data = model.load(getFirst(), getRows(), getSortCriteria(), getFilters());

        model.setWrappedData(data);
    }

    public void clearLazyCache() {
        LazyDataModel model = (LazyDataModel) getDataModel();
        model.setWrappedData(null);
    }

    public SortCriteria[] getSortCriteria() {
        return (SortCriteria[]) getStateHelper().eval("sortCriteria", null);
    }
    public void setSortCriteria(SortCriteria[] sortCriteria) {
        getStateHelper().put("sortCriteria", sortCriteria);
    }

    public Map<String,String> getFilters() {
        return (Map<String,String>) getStateHelper().eval("filters", new HashMap<String,String>());
    }
    public void setFilters(Map<String,String> filters) {
        getStateHelper().put("filters", filters);
    }

    public void setFilteredData(List list) { this.filteredData = list; }
    public List getFilteredData() { return this.filteredData; }
    public void enableFiltering() { getStateHelper().put("filtering", true); }

    public void setTableConfigPanel(TableConfigPanel panel) {
        this.panel = panel;
        configPanelId = panel.getClientId();
    }
    public TableConfigPanel getTableConfigPanel(FacesContext context) {
        if (panel == null & configPanelId != null) {
            panel = (TableConfigPanel)context.getViewRoot().findComponent(configPanelId);
        }
        return panel;
    }

    public void resetValue() { setValue(null); }
    public void resetPagination() { setFirst(0); setPage(1); }
    public void reset() { resetValue(); resetPagination(); }
    
    public void calculatePage() {
        int rows = this.getRows();
        int currentPage = this.getPage();
        int numberOfPages = (int) Math.ceil(this.getRowCount() * 1d / rows);

        // If paging to beyond the last page.
        if (currentPage > numberOfPages && numberOfPages > 0) {
            this.setPage(numberOfPages);
        } else if (currentPage < 1) {
            this.setPage(1);
        }

        this.setFirst((this.getPage()-1) * rows);
    }

    private boolean isIdPrefixedParamSet(String param, FacesContext x) {
        return x.getExternalContext().getRequestParameterMap().containsKey(this.getClientId(x) + param);
    }
    public boolean hasFooterColumn(List<Column> columns) {
        for (Column column : columns)
            if (column.getFacet("footer") != null || column.getFooterText() != null)
                return true;
        return false;
    }
    public boolean isSelectionEnabled() {
        return this.isRowSelectionEnabled();
    }
    public boolean isRowSelectionEnabled() { return this.getSelectionMode() != null; }
    //public boolean isColumnSelectionEnabled() { return getColumnSelectionMode() != null; }
    public boolean isFilteringEnabled() {
        Object value = getStateHelper().get("filtering");
        return value == null ? false : true;
    }
    public boolean isCellSelection() {
        String selectionMode = this.getSelectionMode();
        if (selectionMode != null) return selectionMode.indexOf("cell") != -1;
        else return false;
    }
    public boolean isSingleSelectionMode() {
        String selectionMode = this.getSelectionMode();
        //String columnSelectionMode = this.getColumnSelectionMode();
        if (selectionMode != null)
            return selectionMode.equalsIgnoreCase("single") || selectionMode.equalsIgnoreCase("singlecell");
        //else if (columnSelectionMode != null)
            //return columnSelectionMode.equalsIgnoreCase("single");
        else return false;
    }
    public boolean isPaginationRequest(FacesContext x)         { return isIdPrefixedParamSet("_paging", x); }
    public boolean isTableConfigurationRequest(FacesContext x) { return isIdPrefixedParamSet("_tableconf", x); }
    public boolean isColumnReorderRequest(FacesContext x)      { return isIdPrefixedParamSet("_columnReorder", x); }
    public boolean isSortRequest(FacesContext x)               { return isIdPrefixedParamSet("_sorting", x); }
    public boolean isFilterRequest(FacesContext x)             { return isIdPrefixedParamSet("_filtering", x); }
    public boolean isGlobalFilterRequest(FacesContext x)       { return isIdPrefixedParamSet("_globalFilter", x); }
    public boolean isInstantSelectionRequest(FacesContext x)   { return isIdPrefixedParamSet("_instantSelectedRowIndex", x); }
    public boolean isInstantUnselectionRequest(FacesContext x) { return isIdPrefixedParamSet("_instantUnselectedRowIndex", x); }
    public boolean isRowPanelExpansionRequest(FacesContext x)  { return isIdPrefixedParamSet("_rowPanelExpansion", x); }
    public boolean isRowExpansionRequest(FacesContext x)       { return isIdPrefixedParamSet("_rowExpansion", x); }
    public boolean isRowEditRequest(FacesContext x)            { return isIdPrefixedParamSet("_rowEdit", x); }
    public boolean isScrollingRequest(FacesContext x)          { return isIdPrefixedParamSet("_scrolling", x); }
    public boolean isDataManipulationRequest(FacesContext x)   { return isPaginationRequest(x) || isSortRequest(x) || isFilterRequest(x); }


    public RowPanelExpander getPanelExpansion() {
        for (UIComponent kid : getChildren())
            if (kid instanceof RowPanelExpander) return (RowPanelExpander) kid;
        return null;
    }
    public RowExpander getRowExpansion() {
        for (UIComponent kid : getChildren())
            if (kid instanceof RowExpander) return (RowExpander) kid;
        return null;
    }

    @Override
    public Object getValue() { return getFilteredData() != null ? getFilteredData() : super.getValue(); }


    public static final String CONTAINER_CLASS = "ui-datatable ui-widget";
    public static final String COLUMN_HEADER_CLASS = "ui-widget-header";
    public static final String COLUMN_HEADER_CONTAINER_CLASS = "ui-header-column";
    public static final String COLUMN_FOOTER_CLASS = "ui-widget-header";
    public static final String COLUMN_FOOTER_CONTAINER_CLASS = "ui-footer-column";
    public static final String DATA_CLASS = "ui-datatable-data ui-widget-content";
    public static final String EMPTY_DATA_CLASS = "ui-datatable-data-empty";
    public static final String ROW_CLASS = "";
    public static final String HEADER_CLASS = "ui-datatable-header ui-widget-header ui-corner-tl ui-corner-tr";
    public static final String HEADER_RIGHT_CLASS = "ui-header-right";
    public static final String FOOTER_CLASS = "ui-datatable-footer ui-widget-header ui-corner-bl ui-corner-br";
    public static final String HEAD_TEXT_CLASS = "ui-header-text";
    public static final String SORTABLE_COLUMN_CLASS = "ui-sortable-column";
    public static final String SORTABLE_COLUMN_CONTROL_CLASS = "ui-sortable-control";
    public static final String SORTABLE_COLUMN_ICON_CONTAINER = "ui-sortable-column-icon";
    public static final String SORTABLE_COLUMN_ICON_UP_CLASS = "ui-icon ui-icon-triangle-1-n";
    public static final String SORTABLE_COLUMN_ICON_DOWN_CLASS = "ui-icon ui-icon-triangle-1-s";
    public static final String SORTABLE_COLUMN_ORDER_CLASS = "ui-sortable-column-order";
    public static final String COLUMN_FILTER_CLASS = "ui-column-filter";
    public static final String UNSELECTABLE_ROW_CLASS = "ui-unselectable";
    public static final String REORDERABLE_COL_CLASS = "ui-reorderable-col";
    public static final String EXPANDED_ROW_CLASS = "ui-expanded-row";
    public static final String EXPANDED_ROW_CONTENT_CLASS = "ui-expanded-row-content";
    public static final String ROW_PANEL_TOGGLER_CLASS = "ui-row-panel-toggler";
    public static final String ROW_TOGGLER_CLASS = "ui-row-toggler";
    public static final String EDITABLE_COLUMN_CLASS = "ui-editable-column";
    public static final String CELL_EDITOR_CLASS = "ui-cell-editor";
    public static final String CELL_EDITOR_INPUT_CLASS = "ui-cell-editor-input";
    public static final String CELL_EDITOR_OUTPUT_CLASS = "ui-cell-editor-output";
    public static final String ROW_EDITOR_COLUMN_CLASS = "ui-row-editor-column";
    public static final String ROW_EDITOR_CLASS = "ui-row-editor";
    public static final String SELECTION_COLUMN_CLASS = "ui-selection-column";
    public static final String EVEN_ROW_CLASS = "ui-datatable-even";
    public static final String ODD_ROW_CLASS = "ui-datatable-odd";
    public static final String SCROLLABLE_X_CLASS = "ui-datatable-scroll-x";
    public static final String SCROLLABLE_CONTAINER_CLASS = "ui-datatable-scrollable";
    public static final String SCROLLABLE_HEADER_CLASS = "ui-datatable-scrollable-header";
    public static final String SCROLLABLE_BODY_CLASS = "ui-datatable-scrollable-body";
    public static final String SCROLLABLE_FOOTER_CLASS = "ui-datatable-scrollable-footer";
    public static final String COLUMN_RESIZER_CLASS = "ui-column-resizer";

    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        boolean ret = false;
        if (this.isVisitable(context)) {
            boolean visitRows = requiresRowIteration(context);

            int savedIndex = -1;
            if (visitRows) {
                savedIndex = getRowIndex();
                setRowIndex(-1);
            }

            this.pushComponentToEL(FacesContext.getCurrentInstance(), this);
            try {
                VisitResult result = context.invokeVisitCallback(this, callback);
                if (result.equals(VisitResult.COMPLETE)) return true;
                if (doVisitChildren(context, visitRows) & result == VisitResult.ACCEPT) {
                    if (visitFacets(context, callback, visitRows)) return true;
                    if (visitColumnsAndColumnFacets(context, callback, visitRows)) return true;
                    if (visitRowsAndExpandedRows(context, callback, visitRows)) return true;
                }
            } finally {
                this.popComponentFromEL(FacesContext.getCurrentInstance());
                this.setRowIndex(savedIndex);
                if (visitRows) setRowIndex(savedIndex);
            }
        }
        return ret;
    }

    private boolean requiresRowIteration(VisitContext ctx) {
        try { // Use JSF 2.1 hints if available
            return !ctx.getHints().contains(VisitHint.SKIP_ITERATION);
        } catch (NoSuchFieldError e) {
            FacesContext fctx = FacesContext.getCurrentInstance();
            return (!PhaseId.RESTORE_VIEW.equals(fctx.getCurrentPhaseId()));
        }
    }

    private boolean visitFacets(VisitContext context, VisitCallback callback, boolean visitRows) {
        if (visitRows) setRowIndex(-1);
        if (getFacetCount() > 0) {
            for (UIComponent facet : getFacets().values())
                if (facet.visitTree(context, callback)) return true;
        }
        return false;
    }

    // Visit each UIColumn and any facets it may have defined exactly once
    private boolean visitColumnsAndColumnFacets(VisitContext context, VisitCallback callback, boolean visitRows) {
        if (visitRows) setRowIndex(-1);
        if (getChildCount() > 0) {
            for (UIComponent column : getChildren()) {
                if (column instanceof UIColumn) {
                    VisitResult result = context.invokeVisitCallback(column, callback); // visit the column directly
                    if (result == VisitResult.COMPLETE) return true;
                    if (column.getFacetCount() > 0) {
                        for (UIComponent columnFacet : column.getFacets().values()) {
                            if (columnFacet.visitTree(context, callback)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void processDecodes(FacesContext context) {
        // Required to prevent input component processing on sort, filter, tableconf and pagination initiated submits.
        if (isDataManipulationRequest(context) || isTableConfigurationRequest(context)) {
            this.decode(context);
            context.renderResponse();
        } else super.processDecodes(context);
    }

    private boolean visitRowsAndExpandedRows(VisitContext context, VisitCallback callback, boolean visitRows) {
        int rows = 0;
        int offset = 0;
        int first = getFirst();
        RowStateMap stateMap = this.getStateMap();
        if (visitRows) {
            rows = getRows();
            // If a indeterminate number of rows are shown, visit all rows.
            if (rows == 0) rows = getRowCount();
        }

        while (offset < rows) {
            this.setRowIndex(first + offset);
            if (isRowAvailable()) {
                // Check for tree case
                Object model = this.getDataModel();
                if (model instanceof TreeDataModel) {
                    String currentRootId = "";
                    TreeDataModel dataModel = ((TreeDataModel)model);
                    // Handle row and loop down the tree if expanded.
                    try {
                        do {
                            if (log.isLoggable(Level.FINEST)) log.finest("Visiting Row Id: " + dataModel.getRowIndex());
                            // Decodes row/node in tree case.
                            for (Column c : getColumns()) if (c.visitTree(context, callback)) return true;
                            // Handle recursive case
                            RowState currentModel = stateMap.get(dataModel.getRowData());
                            // If this row is expanded and has children, set it as the root & keep looping.
                            if (currentModel != null && currentModel.isExpanded() && dataModel.getCurrentRowChildCount() > 0) {
                                currentRootId =  currentRootId.equals("") ? (this.getRowIndex()+"") : (currentRootId + "." + getRowIndex());
                                dataModel.setRootIndex(currentRootId);
                                this.setRowIndex(0);
                            } else if (dataModel.getRowIndex() < dataModel.getRowCount()-1) {
                                this.setRowIndex(dataModel.getRowIndex() + 1);
                            } else if (!currentRootId.equals("")) {
                                // changing currrent node id to reflect pop
                                this.setRowIndex(dataModel.pop() + 1);
                                currentRootId = (currentRootId.lastIndexOf('.') != -1)  ? currentRootId.substring(0,currentRootId.lastIndexOf('.')) : "";
                                if (log.isLoggable(Level.FINEST)) log.finest("Popping Root: " + currentRootId);
                            }
                            // Break out of expansion recursion to continue root node
                            if (currentRootId.equals("")) break;
                        } while (true);
                    } finally { dataModel.setRootIndex(null); }
                    // Decode row in plain model case.
                } else for (Column c : getColumns()) if (c.visitTree(context, callback)) return true;
            } else return false;
            offset++;
        }
        return false;
    }
    private boolean doVisitChildren(VisitContext context, boolean visitRows) {
        if (visitRows) setRowIndex(-1);
        Collection<String> idsToVisit = context.getSubtreeIdsToVisit(this);
        assert(idsToVisit != null);
        // non-empty collection means we need to visit our children.
        return (!idsToVisit.isEmpty());
    }

    Boolean tableWrapper;
    public Boolean isTreeRootNode() {
        if (tableWrapper == null) tableWrapper = (getDataModel() instanceof TreeDataModel);
        return tableWrapper;
    }
    // Overridden from custom UIData impl because build process requires UIData, at
    // a point where TreeDataModel will not have the dependencies required for compilation.
    @Override
    public String getContainerClientId(FacesContext context) {
        String containerClientId = getStandardContainerClientId(context);

        int rowIndex;
        String rowId = "";
        if (isTreeRootNode()) {
            TreeDataModel rootModel = (TreeDataModel)getDataModel();
            rowIndex = getRowIndex();
            if (rootModel.isRootIndexSet()) rowId = rootModel.getRootIndex() + "." + rowIndex;
            else rowId += rowIndex;
            } else {
            rowIndex = getRowIndex();
            rowId += rowIndex;
            }

        //TODO: raise reliability.
        if (rowIndex == -1) return containerClientId;

        StringBuilder bld = getBuilder();
        return bld.append(containerClientId).append(UINamingContainer.getSeparatorChar(context)).append(rowId).toString();
    }

    public void setColumnOrdering(String[] indexes) {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        int i;
        for (String index : indexes)
            ints.add(Integer.parseInt(index));

        setColumnOrdering(ints);
    }

    public boolean hasHeaders() {
        for (UIComponent c : getChildren()) {
            if (c instanceof Column && ((c.getFacet("header") != null) || (((Column)c).getHeaderText() != null))) return true;
            else if (c instanceof ColumnGroup && ((ColumnGroup)c).getType().equals("header")) return true;
        }

        return false;
    }

}
