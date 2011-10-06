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
 * Code Modification 2: Improved Scrollable DataTable Column Sizing - ICE-7028
 * Contributors: Nils Lundquist
 */
package org.icefaces.ace.component.datatable;

import org.icefaces.ace.component.celleditor.CellEditor;
import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.columngroup.ColumnGroup;
import org.icefaces.ace.component.columns.Columns;
import org.icefaces.ace.component.row.Row;
import org.icefaces.ace.component.tableconfigpanel.TableConfigPanel;
import org.icefaces.ace.context.RequestContext;
import org.icefaces.ace.event.SelectEvent;
import org.icefaces.ace.event.UnselectEvent;
import org.icefaces.ace.model.legacy.Cell;
import org.icefaces.ace.model.table.*;
import org.icefaces.ace.model.MultiplePropertyComparator;
import org.icefaces.ace.model.table.SortCriteria;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.HTML;
import org.icefaces.render.MandatoryResourceComponent;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.*;

@MandatoryResourceComponent("org.icefaces.ace.component.datatable.DataTable")
public class DataTableRenderer extends CoreRenderer {
    @Override
	public void decode(FacesContext context, UIComponent component) {
		DataTable table = (DataTable) component;

        if (table.isFilteringEnabled()) this.decodeFilters(context, table);
        if (table.isSelectionEnabled()) this.decodeSelection(context, table);
        if (table.isTableConfigurationRequest(context)) this.decodeTableConfigurationRequest(context, table);
        else if (table.isPaginationRequest(context)) this.decodePageRequest(context, table);
        else if (table.isSortRequest(context)) this.decodeSortRequest(context, table, null);
        else if (table.isColumnReorderRequest(context)) this.decodeColumnReorderRequest(context, table);
	}

    private void decodeColumnReorderRequest(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();

        List<Integer> ordering = table.getColumnOrdering();
        String[] columnTargets = params.get(clientId + "_columnReorder").split("-");
        Integer columnIndex = ordering.remove(Integer.parseInt(columnTargets[0]));
        ordering.add(Integer.parseInt(columnTargets[1]), columnIndex);
        // this call just to indicate a change has taken place to col order, and recalc
        table.setColumnOrdering(ordering);
    }

    void decodePageRequest(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
		Map<String,String> params = context.getExternalContext().getRequestParameterMap();

		String rowsParam = params.get(clientId + "_rows");
		String pageParam = params.get(clientId + "_page");

		table.setRows(Integer.valueOf(rowsParam));
        table.setPage(Integer.valueOf(pageParam));
        table.setFirst((table.getPage() - 1) * table.getRows());
	}

    void decodeSortRequest(FacesContext context, DataTable table, String clientId) {
        if (clientId == null) clientId = table.getClientId(context);
		Map<String,String> params = context.getExternalContext().getRequestParameterMap();
		String[] sortKeys = params.get(clientId + "_sortKeys").split(",");
		String[] sortDirs = params.get(clientId + "_sortDirs").split(",");
        ColumnGroup group = table.getColumnGroup("header");
        Column sortColumn = null;
        SortCriteria[] criteria = new SortCriteria[sortKeys.length];

        if (sortKeys[0].equals("")) {
            table.setSortCriteria(null);
            return;
        }

        int i = 0;
        for (String sortKey : sortKeys) {
            if (group != null) {
                outer: for (UIComponent child : group.getChildren()) {
                    for (UIComponent headerRowChild : ((Row)child).getChildren())
                        if (((Column)headerRowChild).getClientId(context).equals(sortKey))
                            { sortColumn = (Column) headerRowChild; break outer; }
                }
            } else for (Column column : table.getColumns())
                if (column.getClientId(context).equals(sortKey)) { sortColumn = column; break; }

            criteria[i] = new SortCriteria(resolveField(sortColumn.getValueExpression("sortBy")), Boolean.parseBoolean(sortDirs[i]));
            i++;
        }

        if (table.isLazy()) {
            table.setSortCriteria(criteria);
        } else if (sortColumn != null) {
            Object value = table.getValue();
            if (value instanceof List) {
                List list = (List)value;
                if (list.size() > 0 && list.get(0) instanceof Map.Entry) {
                    Collections.sort(list, new EntryKeyComparatorWrapper(new MultiplePropertyComparator(criteria)));
                } else {
                    Collections.sort(list, new MultiplePropertyComparator(criteria));
                }
            }
        }
	}

    void decodeFilters(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
		Map<String,String> params = context.getExternalContext().getRequestParameterMap();

        if (table.isFilterRequest(context)) {
            table.setFilteredData(null);
            table.setFirst(0);
            table.setPage(1);
        }

        if (table.isLazy()) {
            Map<String,String> filters = new HashMap<String, String>();
            Map<String,Column> filterMap = table.getFilterMap();

            for (String filterName : filterMap.keySet()) {
                Column column = filterMap.get(filterName);
                String filterValue = params.get(filterName).toLowerCase();

                if (!isValueBlank(filterValue)) {
                    String filterField = resolveField(column.getValueExpression("filterBy"));
                    filters.put(filterField, filterValue);
                }
            }
            table.setFilters(filters);

            if (table.isPaginator())
                if (RequestContext.getCurrentInstance() != null)
                    RequestContext.getCurrentInstance().addCallbackParam("totalRecords", table.getRowCount());
        } else {
            Map<String,Column> filterMap = table.getFilterMap();
            List filteredData = new ArrayList();

            String globalFilter = params.get(clientId + UINamingContainer.getSeparatorChar(context) + "globalFilter");
            boolean hasGlobalFilter = !isValueBlank(globalFilter);
            if (hasGlobalFilter) globalFilter = globalFilter.toLowerCase();

            for (int i = 0; i < table.getRowCount(); i++) {
                table.setRowIndex(i);
                boolean localMatch = true;
                boolean globalMatch = false;

                for (String filterName : filterMap.keySet()) {
                    Column column = filterMap.get(filterName);
                    String columnFilter = params.get(filterName).toLowerCase();
                    String columnValue = String.valueOf(column.getValueExpression("filterBy").getValue(context.getELContext()));

                    if (hasGlobalFilter && !globalMatch && columnValue != null && columnValue.toLowerCase().contains(globalFilter))
                            globalMatch = true;

                    if (isValueBlank(columnFilter)) localMatch = true;

                    else if (columnValue == null || !column.getFilterConstraint().applies(columnValue.toLowerCase(), columnFilter)) {
                        localMatch = false;
                        break;
                    }
                }

                boolean matches = localMatch;
                if (hasGlobalFilter) matches = localMatch && globalMatch;
                if (matches) filteredData.add(table.getRowData());
            }

            boolean isAllFiltered = filteredData.size() == table.getRowCount();

            // Returns data to client in JSON format to adjust paginator size.
            if (table.isPaginator()) {
                int totalRecords = isAllFiltered ? table.getRowCount() : filteredData.size();
                // If we are not in a valid request context, no need to worry about updating the paginator
                // This occurs during a 'hijacked partial' table export callback for instance
                if (RequestContext.getCurrentInstance() != null)
                    RequestContext.getCurrentInstance().addCallbackParam("totalRecords", totalRecords);
            }

            //No need to define filtered data if it is same as actual data
            if (!isAllFiltered) table.setFilteredData(filteredData);
            // Reset model when all filters cleared
            table.setRowIndex(-1);  //reset datamodel
        }
	}

    void decodeSelection(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
		Map<String,String> params = context.getExternalContext().getRequestParameterMap();
		String selection = params.get(clientId + "_selection");

        if (table.isSingleSelectionMode()) decodeSingleSelection(table, selection, params.get(clientId + "_deselection"));
		else decodeMultipleSelection(table, selection, params.get(clientId + "_deselection"));
        queueInstantSelectionEvent(context, table, clientId, params);
	}

    void queueInstantSelectionEvent(FacesContext context, DataTable table, String clientId, Map<String,String> params) {
		if (table.isInstantSelectionRequest(context)) {
            Object model = table.getDataModel();
            TreeDataModel treeModel = null;
            String selection = params.get(clientId + "_instantSelectedRowIndex");

            // If selection occurs with a TreeModel and non-root index
            if (model instanceof TreeDataModel && selection.indexOf('.') > 0) {
                treeModel = (TreeDataModel) model;
                int lastSepIndex = selection.lastIndexOf('.');
                treeModel.setRootIndex(selection.substring(0, lastSepIndex));
                selection = selection.substring(lastSepIndex+1);
            }

            int selectedRowIndex = Integer.parseInt(selection);
            table.setRowIndex(selectedRowIndex);
            SelectEvent selectEvent = new SelectEvent(table, table.getRowData());
            selectEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
            table.queueEvent(selectEvent);
            if (treeModel != null) treeModel.setRootIndex(null);
        }
        else if (table.isInstantUnselectionRequest(context)) {
            Object model = table.getDataModel();
            TreeDataModel treeModel = null;
            String selection = params.get(clientId + "_instantUnselectedRowIndex");

            // If unselection occurs with a TreeModel and non-root index
            if (model instanceof TreeDataModel && selection.indexOf('.') > 0) {
                treeModel = (TreeDataModel) model;
                int lastSepIndex = selection.lastIndexOf('.');
                treeModel.setRootIndex(selection.substring(0, lastSepIndex));
                selection = selection.substring(lastSepIndex+1);
            }

            int unselectedRowIndex = Integer.parseInt(selection);
            table.setRowIndex(unselectedRowIndex);
            UnselectEvent unselectEvent = new UnselectEvent(table, table.getRowData());
            unselectEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
            table.queueEvent(unselectEvent);
            if (treeModel != null) treeModel.setRootIndex(null);
        }
        table.setRowIndex(-1);
	}

    void decodeSingleSelection(DataTable table, String selection, String deselection) {
		RowStateMap stateMap = table.getStateMap();

        // Set the selection to null handling.
        if (isValueBlank(selection)) {
            // Deselect all previous
            if (!deselection.equals("")) stateMap.setAllSelected(false);
        }
        else if (table.isCellSelection()) table.setCellSelection(buildCell(table, selection));
        else {
            TreeDataModel treeModel = null;
            Object model = (Object) table.getDataModel();

            if (model instanceof TreeDataModel) treeModel = (TreeDataModel) model;

            // Tree case handling enhancement
            if (treeModel != null & selection.indexOf('.') > 0) {
                int lastSepIndex = selection.lastIndexOf('.');
                treeModel.setRootIndex(selection.substring(0, lastSepIndex));
                selection = selection.substring(lastSepIndex+1);
            }

            // Deselect all previous
            stateMap.setAllSelected(false);

            // Standard case handling
            int selectedRowIndex = Integer.parseInt(selection);
            table.setRowIndex(selectedRowIndex);
            Object rowData = table.getRowData();
            RowState state = stateMap.get(rowData);
            if (state.isSelectable()) state.setSelected(true);
            if (treeModel != null) treeModel.setRootIndex(null);
            table.setRowIndex(-1);
        }
	}

	void decodeMultipleSelection(DataTable table, String selection, String deselection) {
        Object value = table.getDataModel();
        TreeDataModel model = null;
        if (value instanceof TreeDataModel) model = (TreeDataModel) value;
        RowStateMap stateMap = table.getStateMap();

		if (isValueBlank(selection)) {}
        else if (table.isCellSelection()) {
            String[] cellInfos = selection.split(",");
            Cell[] cells = new Cell[cellInfos.length];

            for (int i = 0; i < cellInfos.length; i++) {
                cells[i] = buildCell(table, cellInfos[i]);
                table.setRowIndex(-1);	//clean
            }

            table.setCellSelection(cells);
        } else {
            String[] rowSelectValues = selection.split(",");

            for (String s : rowSelectValues) {
                if (s.indexOf(".") != -1 && model != null) {
                    int lastSepIndex = s.lastIndexOf('.');
                    model.setRootIndex(s.substring(0, lastSepIndex));
                    s = s.substring(lastSepIndex+1);
                }
                table.setRowIndex(Integer.parseInt(s));

                RowState state = stateMap.get(table.getRowData());
                if (!state.isSelected() && state.isSelectable())
                    state.setSelected(true);

                if (model != null) model.setRootIndex(null);
            }
            table.setRowIndex(-1);

        }
        String[] rowDeselectValues = new String[0];
        if (deselection != null && !deselection.equals(""))
            rowDeselectValues = deselection.split(",");

        int x = 0;
        for (String s : rowDeselectValues) {
            if (s.indexOf(".") != -1 && model != null) {
                int lastSepIndex = s.lastIndexOf('.');
                model.setRootIndex(s.substring(0, lastSepIndex));
                s = s.substring(lastSepIndex+1);
            }

            table.setRowIndex(Integer.parseInt(s));

            RowState state = stateMap.get(table.getRowData());
            if (state.isSelected())
                state.setSelected(false);

            if (model != null) model.setRootIndex(null);
        }
        table.setRowIndex(-1);
	}

    void decodeTableConfigurationRequest(FacesContext context, DataTable table) {
        TableConfigPanel tableConfigPanel = table.getTableConfigPanel(context);
        decodeColumnConfigurations(context, table, tableConfigPanel);
    }

    private void decodeColumnConfigurations(FacesContext context, DataTable table, TableConfigPanel panel) {
        int i;
        String clientId = table.getClientId(context);
        List<Column> columns = table.getColumns();
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        boolean visibility = panel.isColumnVisibilityConfigurable();
        boolean ordering = panel.isColumnOrderingConfigurable();
        boolean sizing = panel.isColumnSizingConfigurable();
        boolean name = panel.isColumnNameConfigurable();
        boolean firstCol = panel.getType().equals("first-col") ;
        boolean lastCol = panel.getType().equals("last-col");
        boolean sorting = panel.isColumnSortingConfigurable();

        for (i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);

            boolean disableVisibilityControl = (firstCol && i == 0) || ((lastCol && i == columns.size() - 1));

            if (visibility && !disableVisibilityControl) decodeColumnVisibility(params, column, i, clientId);
            if (sizing) decodeColumnSizing(params, column, i, clientId);
            if (name) decodeColumnName(params, column, i, clientId);
        }

        if (sorting) decodeSortRequest(context, table, clientId);
        if (ordering) decodeColumnOrdering(params, table, clientId);
    }

    private void decodeColumnName(Map<String, String> params, Column column, int i, String clientId) {
        String text = params.get(clientId + "_head_" + i);
        column.setHeaderText(text);
    }

    private void decodeColumnOrdering(Map<String, String> params, DataTable table, String clientId) {
        String[] indexes = params.get(clientId + "_colorder").split(",");
        table.setColumnOrdering(indexes);
    }

    private void decodeColumnSizing(Map<String, String> params, Column column, int i, String clientId) {

    }

    private void decodeColumnVisibility(Map<String, String> params, Column column, int i, String clientId) {
        String code = params.get(clientId + "_colvis_" + i);
        if (code == null) column.setRendered(false);
        else column.setRendered(true);
    }


    public boolean isValueBlank(String value) {
		if (value == null) return true;
		return value.trim().equals("");
	}

    @Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException{
		DataTable table = (DataTable) component;
        String clientId = table.getClientId(context);

        if (table.isPaginator()) table.calculatePage();

        if (table.isDataManipulationRequest(context)) encodeTableBody(context, table, table.getColumns());
        else if (table.isRowExpansionRequest(context)) {
            String expandedRowId = context.getExternalContext().getRequestParameterMap().get(clientId + "_expandedRowId");
            if (expandedRowId != null) encodeRowExpansion(context, table, table.getColumns(), context.getResponseWriter());
            else encodeRowContraction(context, table);
        }
        else if (table.isRowPanelExpansionRequest(context)) {
            String expandedRowId = context.getExternalContext().getRequestParameterMap().get(clientId + "_expandedRowId");
            if (expandedRowId != null) encodeRowPanelExpansion(context, table);
            else encodeRowPanelContraction(context, table);
        }
        else if (table.isRowEditRequest(context)) encodeEditedRow(context, table);
        else if (table.isScrollingRequest(context)) encodeLiveRows(context, table);
        // else if (table.isInstantSelectionRequest(context) || table.isInstantUnselectionRequest(context)) {}
        else {
            encodeEntierty(context, table);
        }
	}

	protected void encodeScript(FacesContext context, DataTable table) throws IOException{
        ResponseWriter writer = context.getResponseWriter();
		String clientId = table.getClientId(context);

		writer.startElement("script", table);
		writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
        writer.write(this.resolveWidgetVar(table) + " = new ice.ace.DataTable('" + clientId + "',{");

        UIComponent form = ComponentUtils.findParentForm(context, table);
        if (form == null) throw new FacesException("DataTable : \"" + clientId + "\" must be inside a form element.");

        writer.write("formId:'" + form.getClientId(context) + "'");

        if (table.isPaginator()) encodePaginatorConfig(context, table);

        if (table.isRowSelectionEnabled()) encodeSelectionConfig(context, table);

        //if (table.isColumnSelectionEnabled()) writer.write(",columnSelectionMode:'" + table.getColumnSelectionMode() + "'");

        //Panel expansion
        if (table.getPanelExpansion() != null) {
            writer.write(",panelExpansion:true");
            if (table.getOnExpandStart() != null) writer.write(",onExpandStart:function(row) {" + table.getOnExpandStart() + "}");
        }

        //Row expansion
        if (table.getRowExpansion() != null) {
            writer.write(",rowExpansion:true");
            if (table.getOnExpandStart() != null) writer.write(",onExpandStart:function(row) {" + table.getOnExpandStart() + "}");
        }

        //Scrolling
        if (table.isScrollable()) {
            writer.write(",scrollable:true");
            writer.write(",liveScroll:" + table.isLiveScroll());
            writer.write(",scrollStep:" + table.getRows());
            writer.write(",scrollLimit:" + table.getRowCount());

            if (table.getHeight() != Integer.MIN_VALUE) writer.write(",height:" + table.getHeight());
        }

        if (table.getOnRowEditUpdate() != null) writer.write(",onRowEditUpdate:'" + ComponentUtils.findClientIds(context, form, table.getOnRowEditUpdate()) + "'");
        if (table.isResizableColumns())  writer.write(",resizableColumns:true");
        if (table.isReorderableColumns())  writer.write(",reorderableColumns:true");
        if (table.isSingleSort())  writer.write(",singleSort:true");

        writer.write("});");
		writer.endElement("script");
	}

	protected void encodeEntierty(FacesContext context, DataTable table) throws IOException{
		ResponseWriter writer = context.getResponseWriter();
		String clientId = table.getClientId(context);
        boolean scrollable = table.isScrollable();

        String containerClass = scrollable ? DataTable.CONTAINER_CLASS + " " + DataTable.SCROLLABLE_CONTAINER_CLASS : DataTable.CONTAINER_CLASS;
        containerClass = table.getStyleClass() != null
                            ? containerClass + " " + table.getStyleClass()
                            : containerClass;

        String style = null;
        
        boolean hasPaginator = table.isPaginator();
        String paginatorPosition = table.getPaginatorPosition();

        writer.startElement(HTML.DIV_ELEM, table);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        writer.writeAttribute(HTML.CLASS_ATTR, containerClass, "styleClass");

        if ((style = table.getStyle()) != null) writer.writeAttribute(HTML.STYLE_ELEM, style, HTML.STYLE_ELEM);

        encodeFacet(context, table, table.getHeader(), DataTable.HEADER_CLASS);

        if (hasPaginator && !paginatorPosition.equalsIgnoreCase("bottom")) encodePaginatorMarkup(context, table, "top");

        if (scrollable) encodeScrollableTable(context, table);
        else encodeRegularTable(context, table);

        if (hasPaginator && !paginatorPosition.equalsIgnoreCase("top")) encodePaginatorMarkup(context, table, "bottom");

        encodeFacet(context, table, table.getFooter(), DataTable.FOOTER_CLASS);

        if (table.isSelectionEnabled()) encodeSelectionAndDeselectionHolder(context, table);

        // Moved script here to reinit script following tbl update.
        encodeScript(context, table);

        writer.endElement(HTML.DIV_ELEM);
	}

    protected void encodeUtilityChildren(FacesContext context, DataTable table) throws IOException {
        // Run the encode routines of children who rely on them to initialize
        for (UIComponent child : table.getChildren()) {
            if (child instanceof TableConfigPanel) child.encodeAll(context);
        }
    }

    protected void encodeRegularTable(FacesContext context, DataTable table) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        List<Column> columns = table.getColumns();
        encodeUtilityChildren(context, table);
        writer.startElement(HTML.TABLE_ELEM, null);
        if (table.hasHeaders()) encodeTableHead(context, table, columns);
        encodeTableBody(context, table, columns);
        encodeTableFoot(context, table, columns);
        writer.endElement(HTML.TABLE_ELEM);
    }
    protected void encodeScrollableTable(FacesContext context, DataTable table) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        List<Column> columns = table.getColumns();
        encodeUtilityChildren(context, table);

        if (table.hasHeaders()) {
            writer.startElement(HTML.DIV_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, DataTable.SCROLLABLE_HEADER_CLASS, null);
            writer.startElement(HTML.TABLE_ELEM, null);
            encodeTableHead(context, table, columns);
            writer.endElement(HTML.TABLE_ELEM);
            writer.endElement(HTML.DIV_ELEM);
        }

        writer.startElement(HTML.DIV_ELEM, null);
        String scrollClass = DataTable.SCROLLABLE_X_CLASS + " " + DataTable.SCROLLABLE_BODY_CLASS;
        writer.writeAttribute(HTML.CLASS_ATTR, scrollClass, null);
        writer.writeAttribute(HTML.STYLE_ELEM, "height:" + table.getHeight() + "px", null);
        writer.startElement(HTML.TABLE_ELEM, null);
        encodeTableBody(context, table, columns);
        writer.endElement(HTML.TABLE_ELEM);
        writer.endElement(HTML.DIV_ELEM);

        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTable.SCROLLABLE_FOOTER_CLASS, null);
        writer.startElement(HTML.TABLE_ELEM, null);
        encodeTableFoot(context, table, columns);
        writer.endElement(HTML.TABLE_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    private void writeConfigPanelLaunchButton(ResponseWriter writer, UIComponent component, boolean first) throws IOException {
        String jsId = this.resolveWidgetVar(component);
        String clientId = component.getClientId();

        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "ui-tableconf-button", null);
        writer.writeAttribute(HTML.STYLE_ELEM, (first) ? "left:0;" : "right:0;", null);
        writer.startElement(HTML.ANCHOR_ELEM, null);

        String style = "display:inline-block; padding:2px 4px 4px 2px; margin:0px 10px; text-align:left;";
        writer.writeAttribute(HTML.STYLE_ELEM, style, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "ui-state-default ui-corner-all", null);
        writer.writeAttribute(HTML.HREF_ATTR, "#", null);
        writer.writeAttribute(HTML.ONCLICK_ATTR, "$(ice.ace.escapeClientId('"+ clientId +"_tableconf')).toggle()", null);
        writer.writeAttribute( HTML.ID_ATTR, clientId +"_tableconf_launch", null);
        writer.startElement(HTML.SPAN_ELEM, null);

        writer.writeAttribute(HTML.CLASS_ATTR, "ui-icon ui-icon-gear", null);

        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.ANCHOR_ELEM);
        writer.endElement(HTML.SPAN_ELEM);

        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
        writer.writeText("$(function() {\n" + "\t$(ice.ace.escapeClientId('" + clientId + "_tableconf_launch')).hover(function(event){$(event.currentTarget).toggleClass('ui-state-hover'); event.stopPropagation(); }).click(function(event){$(event.currentTarget).toggleClass('ui-state-active'); var panel = $(ice.ace.escapeClientId('" + clientId + "_tableconf')); if (panel.is(':not(:visible)')) " + jsId + "_tableconf.submitTableConfig(event.currentTarget); event.stopPropagation(); });\n" + "});", null);
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    protected void encodeColumnHeader(FacesContext context, DataTable table, List columnSiblings, Column column, boolean first, boolean last) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = column.getClientId(context);
		boolean isSortable = column.getValueExpression("sortBy") != null;
        boolean hasFilter = column.getValueExpression("filterBy") != null;
        String selectionMode = column.getSelectionMode();
        int rightHeaderPadding = 0;
        int leftHeaderPadding = 0;

        boolean isCurrStacked = isCurrColumnStacked(columnSiblings, column);
        boolean isNextStacked = isNextColumnStacked(columnSiblings, column);

        if (!isCurrStacked) {
            String style = column.getStyle();
            String styleClass = column.getStyleClass();
            String columnClass = DataTable.COLUMN_HEADER_CLASS;

            columnClass = isSortable ? columnClass + " " + DataTable.SORTABLE_COLUMN_CLASS : columnClass;
            columnClass = selectionMode != null ? columnClass + " " + DataTable.SELECTION_COLUMN_CLASS : columnClass;
            columnClass = table.isReorderableColumns() ? columnClass + " " + DataTable.REORDERABLE_COL_CLASS : columnClass;
            columnClass = styleClass != null ? columnClass + " " + styleClass : columnClass;

            writer.startElement("th", null);
            writer.writeAttribute(HTML.CLASS_ATTR, columnClass, null);

            if (style != null) writer.writeAttribute(HTML.STYLE_ELEM, style, null);
            if (column.getRowspan() != 1) writer.writeAttribute("rowspan", column.getRowspan(), null);
            if (column.getColspan() != 1) writer.writeAttribute("colspan", column.getColspan(), null);
        }
        else {
            writer.startElement("hr", null);
            writer.endElement("hr");
        }

        //Container
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTable.COLUMN_HEADER_CONTAINER_CLASS, null);
        writer.startElement(HTML.DIV_ELEM, null);

        //Configurable first-col controls
        boolean shouldWriteConPanelLaunchPanel = false;
        if (first) {
            TableConfigPanel panel = table.getTableConfigPanel(context);
            if (panel != null && panel.getType().equals("first-col")) {
                leftHeaderPadding += 45;
                shouldWriteConPanelLaunchPanel = true;
            }
        }
        // Add styling for last-col control container
        if (last) {
            TableConfigPanel panel = table.getTableConfigPanel(context);
            if (panel != null && panel.getType().equals("last-col"))
                rightHeaderPadding += 45;
        }

        if (isSortable) rightHeaderPadding += 35;

        String paddingStyle = "";
        if (rightHeaderPadding > 0) paddingStyle += "padding-right:" + rightHeaderPadding + "px;";
        if (leftHeaderPadding > 0) paddingStyle += "padding-left:" + leftHeaderPadding + "px;";
        if (!paddingStyle.equals("")) writer.writeAttribute(HTML.STYLE_ATTR, paddingStyle, null);

        if (shouldWriteConPanelLaunchPanel) {
            writeConfigPanelLaunchButton(writer, table, first);
        }

        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTable.HEAD_TEXT_CLASS, null);

        //Header content
        UIComponent header = column.getFacet("header");
        String headerText = column.getHeaderText();

        if (selectionMode != null && selectionMode.equalsIgnoreCase("multiple")) {
            writer.startElement(HTML.INPUT_ELEM, header);
            writer.writeAttribute(HTML.TYPE_ATTR, "checkbox", null);
            writer.writeAttribute(HTML.NAME_ATTR, clientId + "_checkAll", null);
            writer.writeAttribute("onclick", this.resolveWidgetVar(table) + ".toggleCheckAll(this)", null);
            writer.endElement(HTML.INPUT_ELEM);
        } else {
            if (header != null) header.encodeAll(context);
            else if (headerText != null) writer.write(headerText);
        }

        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.DIV_ELEM);

        //Filter
        if (hasFilter) {
            table.enableFiltering();
            encodeFilter(context, table, column);
        }

        if (isSortable || isLastColConfPanel(context, table))
            writeHeaderRightSideControls(writer, context, table, isSortable, last);

        writer.endElement(HTML.DIV_ELEM);
        
        if (!isNextStacked) {
            writer.endElement("th");
        }
    }

    private boolean isLastColConfPanel(FacesContext context, DataTable table) {
        TableConfigPanel panel = table.getTableConfigPanel(context);
        return (panel != null && panel.getType().equals("last-col"));
    }

    private void writeHeaderRightSideControls(ResponseWriter writer, FacesContext context, DataTable table, boolean sortable, boolean last) throws IOException {
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTable.HEADER_RIGHT_CLASS, null);

        //Sort icon
        if (sortable) writeSortControl(writer, context, table);

        //Configurable last-col controls
        if (last && isLastColConfPanel(context, table))
            writeConfigPanelLaunchButton(writer, table, false);

        writer.endElement(HTML.SPAN_ELEM);
    }

    private void writeSortControl(ResponseWriter writer, FacesContext context, DataTable table) throws IOException {
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTable.SORTABLE_COLUMN_CONTROL_CLASS, null);

        // Write carats
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTable.SORTABLE_COLUMN_ICON_CONTAINER, null);

        writer.startElement(HTML.ANCHOR_ELEM, null);
        writer.writeAttribute(HTML.TABINDEX_ATTR, 0, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTable.SORTABLE_COLUMN_ICON_UP_CLASS, null);
        writer.endElement(HTML.ANCHOR_ELEM);

        writer.startElement(HTML.ANCHOR_ELEM, null);
        writer.writeAttribute(HTML.TABINDEX_ATTR, 0, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTable.SORTABLE_COLUMN_ICON_DOWN_CLASS, null);
        writer.endElement(HTML.ANCHOR_ELEM);

        writer.endElement(HTML.SPAN_ELEM);


        // Write Sort Order Integer
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTable.SORTABLE_COLUMN_ORDER_CLASS, null);
        if (table.isSingleSort()) writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);
        else writer.write("&#160;");
        writer.endElement(HTML.SPAN_ELEM);

        writer.endElement(HTML.SPAN_ELEM);
    }

    protected void encodeColumnsHeader(FacesContext context, DataTable table, Columns columns) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String columnVar = columns.getVar();

        for (Object column : (Collection) columns.getValue()) {
            context.getExternalContext().getRequestMap().put(columnVar, column);
            UIComponent header = columns.getFacet("header");

            writer.startElement("th", null);
            writer.writeAttribute(HTML.CLASS_ATTR, DataTable.COLUMN_HEADER_CLASS, null);

            if (header != null) header.encodeAll(context);

            writer.endElement("th");
        }

        context.getExternalContext().getRequestMap().remove(columnVar);
    }

    protected void encodeFilter(FacesContext context, DataTable table, Column column) throws IOException {
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        ResponseWriter writer = context.getResponseWriter();

        String widgetVar = this.resolveWidgetVar(table);
        String filterId = column.getClientId(context) + "_filter";
        String filterFunction = widgetVar + ".filter()";
        String filterStyleClass = column.getFilterStyleClass();
        filterStyleClass = filterStyleClass == null ? DataTable.COLUMN_FILTER_CLASS : DataTable.COLUMN_FILTER_CLASS + " " + filterStyleClass;

        if (column.getValueExpression("filterOptions") == null) {
            String filterEvent = "on" + column.getFilterEvent();
            String filterValue = params.containsKey(filterId) ? params.get(filterId) : "";

            writer.startElement(HTML.INPUT_ELEM, null);
            writer.writeAttribute(HTML.ID_ATTR, filterId, null);
            writer.writeAttribute(HTML.NAME_ATTR, filterId, null);
            writer.writeAttribute(HTML.CLASS_ATTR, filterStyleClass, null);
            writer.writeAttribute("size", "1", null); // Webkit requires none zero/null size value to use CSS width correctly.
            writer.writeAttribute("value", filterValue , null);
            writer.writeAttribute(filterEvent, filterFunction , null);

            if (column.getFilterStyle() != null)
                writer.writeAttribute(HTML.STYLE_ELEM, column.getFilterStyle(), null);

            writer.endElement(HTML.INPUT_ELEM);
        }
        else {
            writer.startElement("select", null);
            writer.writeAttribute(HTML.ID_ATTR, filterId, null);
            writer.writeAttribute(HTML.NAME_ATTR, filterId, null);
            writer.writeAttribute(HTML.CLASS_ATTR, filterStyleClass, null);
            writer.writeAttribute("onchange", filterFunction, null);

            SelectItem[] itemsArray = (SelectItem[]) getFilterOptions(column);

            for (SelectItem item : itemsArray) {
                writer.startElement("option", null);
                writer.writeAttribute("value", item.getValue(), null);
                writer.write(item.getLabel());
                writer.endElement("option");
            }

            writer.endElement("select");
        }

    }
    protected SelectItem[] getFilterOptions(Column column) {
        Object options = column.getFilterOptions();
        if (options instanceof SelectItem[]) return (SelectItem[]) options;
        else if (options instanceof Collection<?>) return ((Collection<SelectItem>) column.getFilterOptions()).toArray(new SelectItem[] {});
        else throw new FacesException("Filter options for column " + column.getClientId() + " should be a SelectItem array or collection");
    }

    protected void encodeColumnFooter(FacesContext context, DataTable table, List columnSiblings, Column column) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        boolean isCurrStacked = isCurrColumnStacked(columnSiblings, column);
        boolean isNextStacked = isNextColumnStacked(columnSiblings, column);

        if (!isCurrStacked) {
            String style = column.getStyle();
            String styleClass = column.getStyleClass();
            String footerClass = styleClass != null ? DataTable.COLUMN_FOOTER_CLASS + " " + styleClass : DataTable.COLUMN_FOOTER_CLASS;

            writer.startElement(HTML.TD_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, footerClass, null);
            if (style != null) writer.writeAttribute(HTML.STYLE_ELEM, style, null);
            if (column.getRowspan() != 1) writer.writeAttribute("rowspan", column.getRowspan(), null);
            if (column.getColspan() != 1) writer.writeAttribute("colspan", column.getColspan(), null);
        }
        else {
            writer.startElement("hr", null);
            writer.endElement("hr");
        }

        //Container
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTable.COLUMN_FOOTER_CONTAINER_CLASS, null);

        //Footer content
        UIComponent facet = column.getFacet("footer");
        String text = column.getFooterText();
        if (facet != null) {
            facet.encodeAll(context);
        } else if (text != null) {
            writer.write(text);
        }

        writer.endElement(HTML.DIV_ELEM);
        
        if (!isNextStacked) {
            writer.endElement(HTML.TD_ELEM);
        }
    }


    protected void encodeTableHead(FacesContext context, DataTable table, List<Column> columns) throws IOException {
        List headContainer = columns;
        ResponseWriter writer = context.getResponseWriter();
        ColumnGroup group = table.getColumnGroup("header");
        if (group != null) headContainer = group.getChildren();

        writer.startElement("thead", null);
        writer.startElement(HTML.TR_ELEM, null);

        // For each row of a col group, or child of a datatable
        boolean firstHeadElement = true;
        Iterator<UIComponent> headElementIterator = headContainer.iterator();
        do {
            UIComponent headerElem = headElementIterator.next();
            List<UIComponent> headerRowChildren = new ArrayList<UIComponent>();
            int i = 0;
            boolean subRows = false;

            // If its a row, get the row children, else add the column as a pseduo child, if not column, break.
            if (headerElem.isRendered())
                if (headerElem instanceof Row) {
                    Row headerRow = (Row) headerElem;
                    headerRowChildren = headerRow.getChildren();
                } else headerRowChildren.add(headerElem);

            if (headerRowChildren.size() > 1) subRows = true;

            // If the element was a row of a col-group render another row for a subrow of the header
            if (subRows) writer.startElement(HTML.TR_ELEM, null);

            // Either loop through row children or render the single column/columns
            Iterator<UIComponent> componentIterator = headerRowChildren.iterator();
            boolean firstComponent = true;
            if (componentIterator.hasNext())
            do {
                UIComponent headerRowChild = componentIterator.next();
                if (headerRowChild.isRendered() && headerRowChild instanceof Column)
                    encodeColumnHeader(context, table, headContainer, (Column) headerRowChild,
                            (firstComponent && firstHeadElement),
                            (!headElementIterator.hasNext() && !componentIterator.hasNext()));
                else if (headerRowChild instanceof Columns)
                    encodeColumnsHeader(context, table, (Columns) headerRowChild);
                firstComponent = false;
            } while (componentIterator.hasNext());
            if (subRows) writer.endElement(HTML.TR_ELEM);
            firstHeadElement = false;
        } while (headElementIterator.hasNext());
        writer.endElement(HTML.TR_ELEM);
        writer.endElement("thead");
    }


    protected void encodeTableBody(FacesContext context, DataTable table, List<Column> columns) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String rowIndexVar = table.getRowIndexVar();
        String clientId = table.getClientId(context);
        //String columnSelectionMode = table.getColumnSelectionMode();

        int rows = table.getRows();
		int first = table.getFirst();
        int rowCount = table.getRowCount();
        int rowCountToRender = rows == 0 ? rowCount : rows;
        boolean hasData = rowCount > 0;

        if (table.isLazy()) table.loadLazyData();

        String tbodyClass = hasData ? DataTable.DATA_CLASS : DataTable.EMPTY_DATA_CLASS;

        writer.startElement("tbody", null);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_data", null);
        writer.writeAttribute(HTML.CLASS_ATTR, tbodyClass, null);

        if (hasData) {
            //TODO: refactor the location of this preselection handling
            //if (selectionMode != null && selection != null) handlePreselection(table, selectionMode, selection);
            for (int i = first; i < (first + rowCountToRender); i++) encodeRow(context, table, columns, clientId, i, null, rowIndexVar);
        } else encodeEmptyMessage(table, writer, columns);

        writer.endElement("tbody");
		table.setRowIndex(-1);
		if (rowIndexVar != null) context.getExternalContext().getRequestMap().remove(rowIndexVar);
    }

    private void encodeEmptyMessage(DataTable table, ResponseWriter writer, List<Column> columns) throws IOException {
        String emptyMessage = table.getEmptyMessage();
        if (emptyMessage != null) {
            writer.startElement(HTML.TR_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, DataTable.ROW_CLASS, null);

            writer.startElement(HTML.TD_ELEM, null);
            writer.writeAttribute("colspan", columns.size(), null);
            writer.write(emptyMessage);
            writer.endElement(HTML.TD_ELEM);

            writer.endElement(HTML.TR_ELEM);
        }
    }


    protected void encodeRow(FacesContext context, DataTable table, List<Column> columns, String clientId, int rowIndex, String parentIndex, String rowIndexVar) throws IOException {
        table.setRowIndex(rowIndex);
        if (!table.isRowAvailable()) return;
        if (rowIndexVar != null) context.getExternalContext().getRequestMap().put(rowIndexVar, rowIndex);

        Object dataModel = table.getDataModel();

        RowState rowState = table.getStateMap().get(table.getRowData());
        boolean selected = rowState.isSelected();
        boolean unselectable = !rowState.isSelectable();
        boolean expanded = rowState.isExpanded();
        boolean visible = rowState.isVisible();
        context.getExternalContext().getRequestMap().put(table.getRowStateVar(), rowState);

        if (visible) {
            ResponseWriter writer = context.getResponseWriter();
            String userRowStyleClass = table.getRowStyleClass();
            String expandedClass = expanded ? DataTable.EXPANDED_ROW_CLASS : "";
            String unselectableClass = unselectable ? DataTable.UNSELECTABLE_ROW_CLASS : "";
            String rowStyleClass = rowIndex % 2 == 0 ? DataTable.ROW_CLASS + " " + DataTable.EVEN_ROW_CLASS : DataTable.ROW_CLASS + " " + DataTable.ODD_ROW_CLASS;

            if (selected && table.getSelectionMode() != null) rowStyleClass = rowStyleClass + " ui-selected ui-state-highlight";
            if (userRowStyleClass != null) rowStyleClass = rowStyleClass + " " + userRowStyleClass;

            writer.startElement(HTML.TR_ELEM, null);
            parentIndex = (parentIndex != null) ? parentIndex + "." : "";
            writer.writeAttribute(HTML.ID_ATTR, clientId + "_row_" + parentIndex + rowIndex, null);
            writer.writeAttribute(HTML.CLASS_ATTR, rowStyleClass + " " + expandedClass + " " + unselectableClass, null);

            for (Column kid : columns) {
                if (kid.isRendered()) {
                    encodeRegularCell(context, table, columns, kid, clientId, selected, (rowIndex == 0));
                }
            }
    //        for (UIComponent kid : table.getChildren())
    //            if (kid.isRendered()) {
    //                if (kid instanceof Column) encodeRegularCell(context, table, (Column) kid, clientId, selected, (rowIndex == 0));
    //                else if (kid instanceof Columns) encodeDynamicCell(context, table, (Columns) kid);
    //            }

            if (rowIndexVar != null) context.getExternalContext().getRequestMap().put(rowIndexVar, rowIndex);
            writer.endElement(HTML.TR_ELEM);

            if (expanded) {
                context.getExternalContext().getRequestMap().put(clientId + "_expandedRowId", ""+rowIndex);
                boolean isPanel = table.getPanelExpansion() != null;
                boolean isRow = table.getRowExpansion() != null;

                if (isPanel && isRow) {
                    if (rowState.getExpansionType() == RowState.ExpansionType.ROW)
                        encodeRowExpansion(context, table, columns, writer);
                    else if (rowState.getExpansionType() == RowState.ExpansionType.PANEL)
                        encodeRowPanelExpansion(context, table);
                } else if (isPanel) {
                    encodeRowPanelExpansion(context, table);
                } else if (isRow) {
                    encodeRowExpansion(context, table, columns, writer);
                }

                table.setRowIndex(rowIndex); // Row index will have come back different from row expansion.
            }
        }
    }


    protected void encodeRegularCell(FacesContext context, DataTable table, List columnSiblings, Column column, String clientId, boolean selected, boolean resizable) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        boolean isCurrStacked = isCurrColumnStacked(columnSiblings, column);
        boolean isNextStacked = isNextColumnStacked(columnSiblings, column);

        if (!isCurrStacked) {
            writer.startElement(HTML.TD_ELEM, null);
            if (resizable) writer.startElement(HTML.DIV_ELEM, null);
            if (column.getStyle() != null) writer.writeAttribute(HTML.STYLE_ELEM, column.getStyle(), null);
        }
        else {
            writer.startElement("hr", null);
            writer.endElement("hr");
        }

        if (column.getSelectionMode() != null) {
            String columnStyleClass = column.getStyleClass();
            columnStyleClass = columnStyleClass == null ? DataTable.SELECTION_COLUMN_CLASS : DataTable.SELECTION_COLUMN_CLASS + " " + columnStyleClass;
            writer.writeAttribute(HTML.CLASS_ATTR, columnStyleClass, null);
            encodeColumnSelection(context, table, clientId, column, selected);
        } else {
            CellEditor editor = column.getCellEditor();
            String columnStyleClass = column.getStyleClass();
            if (editor != null) columnStyleClass = columnStyleClass == null ? DataTable.EDITABLE_COLUMN_CLASS : DataTable.EDITABLE_COLUMN_CLASS + " " + columnStyleClass;
            if (columnStyleClass != null) writer.writeAttribute(HTML.CLASS_ATTR, columnStyleClass, null);
            column.encodeAll(context);
        }
        if (!isNextStacked) {
            if (resizable) writer.endElement(HTML.DIV_ELEM);
            writer.endElement(HTML.TD_ELEM);
        }
    }


    protected void encodeDynamicCell(FacesContext context, DataTable table, Columns columns) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String columnVar = columns.getVar();
        String columnIndexVar = columns.getColumnIndexVar();
        int colIndex = 0;

        for (Object column : (Collection) columns.getValue()) {
            context.getExternalContext().getRequestMap().put(columnVar, column);
            context.getExternalContext().getRequestMap().put(columnIndexVar, colIndex);
            UIComponent header = columns.getFacet("header");

            writer.startElement(HTML.TD_ELEM, null);
            writer.startElement(HTML.DIV_ELEM, null);
            columns.encodeAll(context);
            writer.endElement(HTML.DIV_ELEM);
            writer.endElement(HTML.TD_ELEM);

            colIndex++;
        }

        context.getExternalContext().getRequestMap().remove(columnVar);
        context.getExternalContext().getRequestMap().remove(columnIndexVar);
    }


    protected void encodeTableFoot(FacesContext context, DataTable table, List<Column> columns) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        ColumnGroup group = table.getColumnGroup("footer");
        boolean shouldRender = table.hasFooterColumn(columns) || group != null;

        if (!shouldRender) return;

        writer.startElement("tfoot", null);

        if (group != null) {
            for (UIComponent child : group.getChildren()) {
                if (child.isRendered() && child instanceof Row) {
                    Row footerRow = (Row) child;
                    writer.startElement(HTML.TR_ELEM, null);

                    List<UIComponent> footerRowChildren = footerRow.getChildren();
                    for (UIComponent footerRowChild : footerRowChildren)
                        if (footerRowChild.isRendered() && footerRowChild instanceof Column)
                            encodeColumnFooter(context, table, footerRowChildren, (Column) footerRowChild);

                    writer.endElement(HTML.TR_ELEM);
                }
            }
        } else {
            writer.startElement(HTML.TR_ELEM, null);
            for (Column column : columns) {
                encodeColumnFooter(context, table, columns, column);
            }
            writer.endElement(HTML.TR_ELEM);
        }
        writer.endElement("tfoot");
    }

    protected void encodeFacet(FacesContext context, DataTable table, UIComponent facet, String styleClass) throws IOException {
        if (facet == null) return;
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);

        facet.encodeAll(context);
        writer.endElement(HTML.DIV_ELEM);
    }

    protected void encodePaginatorConfig(FacesContext context, DataTable table) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = table.getClientId(context);
        String paginatorPosition = table.getPaginatorPosition();
        String paginatorContainers = null;
        if (paginatorPosition.equalsIgnoreCase("both"))
             paginatorContainers = "'" + clientId + "_paginatortop','" + clientId + "_paginatorbottom'";
        else paginatorContainers = "'" + clientId + "_paginator" + paginatorPosition + "'";

        writer.write(",paginator:new YAHOO.widget.Paginator({");
        writer.write("rowsPerPage:" + table.getRows());
        writer.write(",totalRecords:" + table.getRowCount());
        writer.write(",initialPage:" + table.getPage());
        writer.write(",containers:[" + paginatorContainers + "]");

        if (table.getPageLinks() != 10) writer.write(",pageLinks:" + table.getPageLinks());
        if (table.getPaginatorTemplate() != null) writer.write(",template:'" + table.getPaginatorTemplate() + "'");
        if (table.getRowsPerPageTemplate() != null) writer.write(",rowsPerPageOptions : [" + table.getRowsPerPageTemplate() + "]");
        if (table.getCurrentPageReportTemplate() != null)writer.write(",pageReportTemplate:'" + table.getCurrentPageReportTemplate() + "'");
        if (!table.isPaginatorAlwaysVisible()) writer.write(",alwaysVisible:false");

        writer.write("})");
    }

    protected void encodePaginatorMarkup(FacesContext context, DataTable table, String position) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = table.getClientId(context);

        String styleClass = "ui-paginator ui-paginator-" + position + " ui-widget-header";

        if (!position.equals("top") && table.getFooter() == null)
            styleClass = styleClass + " ui-corner-bl ui-corner-br";
        else if (!position.equals("bottom") && table.getHeader() == null)
            styleClass = styleClass + " ui-corner-tl ui-corner-tr";

        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_paginator" + position, null);
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
        writer.endElement(HTML.DIV_ELEM);
    }

    protected void encodeSelectionConfig(FacesContext context, DataTable table) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.write(",selectionMode:'" + table.getSelectionMode() + "'");

        if (table.isDblClickSelect()) writer.write(",dblclickSelect:true");

        String onRowSelectUpdate = table.getOnRowSelectUpdate() != null ? table.getOnRowSelectUpdate() : table.getUpdate();
        String onRowUnselectUpdate = table.getOnRowUnselectUpdate() != null ? table.getOnRowUnselectUpdate() : table.getUpdate();

        if (table.getRowSelectListener() != null || onRowSelectUpdate != null) {
            writer.write(",instantSelect:true");

            if (onRowSelectUpdate != null)
                writer.write(",onRowSelectUpdate:'" + ComponentUtils.findClientIds(context, table.getParent(), onRowSelectUpdate) + "'");

            if (onRowUnselectUpdate != null)
                writer.write(",onRowUnselectUpdate:'" + ComponentUtils.findClientIds(context, table.getParent(), onRowUnselectUpdate) + "'");

            if (table.getOnSelectStart() != null) writer.write(",onRowSelectStart:function() {" + table.getOnSelectStart() + "}");
            if (table.getOnSelectComplete() != null) writer.write(",onRowSelectComplete:function(xhr, status, args) {" + table.getOnSelectComplete() + "}");
            if (table.getOnRowSelectStart() != null) writer.write(",onRowSelectStart:function() {" + table.getOnRowSelectStart() + "}");
            if (table.getOnRowSelectComplete() != null) writer.write(",onRowSelectComplete:function(xhr, status, args) {" + table.getOnRowSelectComplete() + "}");
        }

        if (table.getRowUnselectListener() != null) {
            writer.write(",instantUnselect:true");
            if (onRowUnselectUpdate != null) writer.write(",onRowUnselectUpdate:'" + ComponentUtils.findClientIds(context, table.getParent(), onRowUnselectUpdate) + "'");
        }
    }

    protected void encodeSelectionAndDeselectionHolder(FacesContext context, DataTable table) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
        String id = table.getClientId(context) + "_selection";

		writer.startElement(HTML.INPUT_ELEM, null);
		writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
		writer.writeAttribute(HTML.ID_ATTR, id, null);
		writer.writeAttribute(HTML.NAME_ATTR, id, null);
        writer.endElement(HTML.INPUT_ELEM);


        id = table.getClientId(context) + "_deselection";
        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.ID_ATTR, id, null);
        writer.writeAttribute(HTML.NAME_ATTR, id, null);
        writer.endElement(HTML.INPUT_ELEM);
	}

    private void encodeRowPanelContraction(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
        String expandedRowId = context.getExternalContext().getRequestParameterMap().get(clientId + "_contractedRowId");

        DataModel model = table.getDataModel();
        if (!(model instanceof TreeDataModel)) {
            model.setRowIndex(Integer.parseInt(expandedRowId));
            table.getStateMap().get(model.getRowData()).setExpanded(false);
            model.setRowIndex(-1);
        } else {
            TreeDataModel rootModel = (TreeDataModel)model;
            rootModel.setRootIndex(expandedRowId);
            table.getStateMap().get(rootModel.getRootData()).setExpanded(false);
            rootModel.setRootIndex(null);
        }
    }

    private void encodeRowContraction(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
        String expandedRowId = context.getExternalContext().getRequestParameterMap().get(clientId + "_contractedRowId");

        Object model = table.getDataModel();
        if (!(model instanceof TreeDataModel)) throw new FacesException("DataTable : \"" + clientId + "\" must be bound to an instance of TreeDataModel when using sub-row expansion.");

        TreeDataModel rootModel = (TreeDataModel)model;
        rootModel.setRootIndex(expandedRowId);
        table.getStateMap().get(rootModel.getRootData()).setExpanded(false);
        rootModel.setRootIndex(null);
    }

    private void encodeRowExpansion(FacesContext context, DataTable table, List<Column> columns, ResponseWriter writer) throws IOException {
        String rowVar = table.getVar();
        String rowIndexVar = table.getRowIndexVar();
        String clientId = table.getClientId(context);

        boolean renderForFadeIn = true;
        String expandedRowId = context.getExternalContext().getRequestParameterMap().get(clientId + "_expandedRowId");
        if (expandedRowId == null) {
            expandedRowId = (String) context.getExternalContext().getRequestMap().get(clientId + "_expandedRowId");
            // Null expandedRowId in getRequestParameterMap implies we are in a non-partial context, and do not want to fadeIn the rows.
            renderForFadeIn = false;
        }

        Object model = table.getDataModel();

        if (!(model instanceof TreeDataModel)) throw new FacesException("DataTable : \"" + clientId + "\" must be bound to an instance of TreeDataModel when using sub-row expansion.");
        TreeDataModel rootModel = (TreeDataModel)model;
        rootModel.setRootIndex(expandedRowId);
        table.getStateMap().get(rootModel.getRootData()).setExpanded(true);
        table.setRowIndex(0);

        if (rootModel.getRowCount() > 0)
        while (rootModel.getRowIndex() < rootModel.getRowCount()) {
            if (rowVar != null) context.getExternalContext().getRequestMap().put(rowVar, rootModel.getRowData());
            if (rowIndexVar != null) context.getExternalContext().getRequestMap().put(rowIndexVar, rootModel.getRowIndex());

            RowState rowState = table.getStateMap().get(rootModel.getRowData());
            boolean selected = rowState.isSelected();
            boolean expanded = rowState.isExpanded();
            boolean unselectable = !rowState.isSelectable();
            boolean visible = rowState.isVisible();
            context.getExternalContext().getRequestMap().put(table.getRowStateVar(), rowState);

            String expandedClass = expanded ? DataTable.EXPANDED_ROW_CLASS : "";
            String alternatingClass = (rootModel.getRowIndex() % 2 == 0) ? DataTable.EVEN_ROW_CLASS : DataTable.ODD_ROW_CLASS;
            String selectionClass = (selected && table.getSelectionMode() != null) ? "ui-selected ui-state-highlight" : "";
            String unselectableClass = unselectable ? DataTable.UNSELECTABLE_ROW_CLASS : "";

            if (visible) {
                writer.startElement(HTML.TR_ELEM, null);
                writer.writeAttribute(HTML.ID_ATTR, clientId + "_row_" + expandedRowId + "." + rootModel.getRowIndex(), null);
                writer.writeAttribute(HTML.CLASS_ATTR, DataTable.ROW_CLASS + " " + alternatingClass + " " + selectionClass + " " + expandedClass + " " + unselectableClass, null);
                if (renderForFadeIn) writer.writeAttribute(HTML.STYLE_ELEM,"display:none;", null);

                for (Column kid : columns) {
                    if (kid.isRendered()) {
                        encodeRegularCell(context, table, columns, kid, clientId, selected, false);
                    }
                }
    //            for (UIComponent kid : table.getChildren())
    //                if (kid.isRendered())
    //                    if (kid instanceof Column) encodeRegularCell(context, table, (Column) kid, clientId, false, false);
    //                    else if (kid instanceof Columns) encodeDynamicCell(context, table, (Columns) kid);

                writer.endElement(HTML.TR_ELEM);

                if (expanded) {
                    int rowIndex = rootModel.getRowIndex();
                    context.getExternalContext().getRequestMap().put(clientId + "_expandedRowId", expandedRowId+"."+rowIndex);
                    encodeRowExpansion(context, table, columns, writer);
                    rootModel.setRootIndex(expandedRowId);
                    rootModel.setRowIndex(rowIndex); // Row index will have come back different from row expansion.
                    context.getExternalContext().getRequestMap().put(clientId + "_expandedRowId", expandedRowId);
                }
            }

            rootModel.setRowIndex(rootModel.getRowIndex() + 1);
            if (rowIndexVar != null) context.getExternalContext().getRequestMap().remove(rowIndexVar);
            if (rowVar != null) context.getExternalContext().getRequestMap().remove(rowVar);
        }

        // Refactor the location of this to enable more efficient deep tree renders.
        rootModel.setRootIndex(null);
        table.setRowIndex(-1);
    }

    protected void encodeRowPanelExpansion(FacesContext context, DataTable table) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        String clientId = table.getClientId(context);
        boolean fadeIn = true;

        String expandedRowId = params.get(clientId + "_expandedRowId");
        if (expandedRowId == null) {
            expandedRowId = (String) context.getExternalContext().getRequestMap().get(clientId + "_expandedRowId");
            fadeIn = false;
        }

        table.setRowIndex(Integer.parseInt(expandedRowId));

        table.getStateMap().get(table.getRowData()).setExpanded(true);

        writer.startElement(HTML.TR_ELEM, null);
        if (fadeIn) writer.writeAttribute(HTML.STYLE_ELEM, "display:none", null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTable.EXPANDED_ROW_CONTENT_CLASS + " ui-widget-content " + DataTable.UNSELECTABLE_ROW_CLASS , null);

        writer.startElement(HTML.TD_ELEM, null);
        writer.writeAttribute("colspan", table.getColumns().size(), null);
        table.getPanelExpansion().encodeAll(context);

        writer.endElement(HTML.TD_ELEM);
        writer.endElement(HTML.TR_ELEM);
        table.setRowIndex(-1);
    }

    protected void encodeColumnSelection(FacesContext context, DataTable table, String clientId, Column column, boolean selected) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String selectionMode = column.getSelectionMode();
        String name = clientId + "_selection";
        writer.startElement(HTML.INPUT_ELEM, null);

        if (selectionMode.equalsIgnoreCase("single")) {
            writer.writeAttribute(HTML.TYPE_ATTR, "radio", null);
            writer.writeAttribute(HTML.NAME_ATTR, name + "_radio", null);
        } else if (selectionMode.equalsIgnoreCase("multiple")) {
            writer.writeAttribute(HTML.TYPE_ATTR, "checkbox", null);
            writer.writeAttribute(HTML.NAME_ATTR, name + "_checkbox", null);
        } else { throw new FacesException("Invalid column selection mode:" + selectionMode); }

        if (selected) writer.writeAttribute("checked", "checked", null);
        writer.endElement(HTML.INPUT_ELEM);
    }

    protected void encodeEditedRow(FacesContext context, DataTable table) throws IOException {
        Object model = table.getDataModel();
        TreeDataModel treeModel;
        if (model instanceof TreeDataModel) {
            treeModel = (TreeDataModel) model;
            String editedId = context.getExternalContext().getRequestParameterMap().get(table.getClientId(context) + "_editedRowId");
            String rootIndex = null;
            if (editedId.indexOf('.') > 0) {
                int lastSepIndex = editedId.lastIndexOf('.');
                rootIndex = editedId.substring(0, lastSepIndex);
                treeModel.setRootIndex(rootIndex);
                editedId = editedId.substring(lastSepIndex+1);
            }
            treeModel.setRowIndex(Integer.parseInt(editedId));
            encodeRow(context, table, table.getColumns(), table.getClientId(context), Integer.parseInt(editedId), rootIndex, table.getRowIndexVar());
            treeModel.setRootIndex(null);
        } else {
            int editedRowId = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get(table.getClientId(context) + "_editedRowId"));
            table.setRowIndex(editedRowId);
            encodeRow(context, table, table.getColumns(), table.getClientId(context), editedRowId, null, table.getRowIndexVar());
        }
    }

    protected void encodeLiveRows(FacesContext context, DataTable table) throws IOException {
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        int scrollOffset = Integer.parseInt(params.get(table.getClientId(context) + "_scrollOffset"));
        String clientId = table.getClientId(context);
        String rowIndexVar = table.getRowIndexVar();

        for (int i = scrollOffset; i < (scrollOffset + table.getRows()); i++)
            encodeRow(context, table, table.getColumns(), clientId, i, null, rowIndexVar);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {}
    @Override
    public boolean getRendersChildren() { return true; }


    private boolean isNextColumnStacked(List comps, Column currCol) {
        int index = comps.indexOf(currCol);
        if (index >= 0) {
            if ((index + 1) < comps.size()) {
                UIComponent next = (UIComponent) comps.get(index + 1);
                if (next instanceof Column) {
                    Column nextCol = (Column) next;
                    return isCurrColumnStacked(comps, nextCol);
                }
            }
        }
        return false;
    }

    private boolean isCurrColumnStacked(List comps, Column currCol) {
        // The first column can not be stacked, only subsequent ones can be
        // stacked under it
        int index = comps.indexOf(currCol);
        if (index == 0) {
            return false;
        }
        return currCol.isStacked();
    }

    // Get object name from value expression string.
    String resolveField(ValueExpression expression) {
        String expressionString = expression.getExpressionString();
        expressionString = expressionString.substring(2, expressionString.length() - 1);
        return expressionString.substring(expressionString.indexOf(".") + 1);
    }

    // Get instance of cell data model
    Cell buildCell(DataTable dataTable, String value) {
		String[] cellInfo = value.split("#");

        int rowIndex = Integer.parseInt(cellInfo[0]);
		UIColumn column = dataTable.getColumns().get(Integer.parseInt(cellInfo[1]));

		dataTable.setRowIndex(rowIndex);
		Object rowData = dataTable.getRowData();

		Object cellValue = null;
		UIComponent columnChild = column.getChildren().get(0);
		if (columnChild instanceof ValueHolder) cellValue = ((ValueHolder) columnChild).getValue();
		return new Cell(rowData, column.getId(), cellValue);
	}

    private class EntryKeyComparatorWrapper<T> implements Comparator {
        Comparator<T> comparator;

        public EntryKeyComparatorWrapper(Comparator<T> comparator) {
            this.comparator = comparator;
        }

        public int compare(Object o1, Object o2) {
            return comparator.compare(((Map.Entry<T,Object>)o1).getKey(), ((Map.Entry<T,Object>)o2).getKey());
        }
    }
}
