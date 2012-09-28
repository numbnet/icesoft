package org.icefaces.ace.component.datatable;

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.columngroup.ColumnGroup;
import org.icefaces.ace.component.row.Row;
import org.icefaces.ace.component.tableconfigpanel.TableConfigPanel;
import org.icefaces.ace.context.RequestContext;
import org.icefaces.ace.event.SelectEvent;
import org.icefaces.ace.event.UnselectEvent;
import org.icefaces.ace.model.table.RowState;
import org.icefaces.ace.model.table.RowStateMap;
import org.icefaces.ace.model.table.TreeDataModel;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * User: Nils
 * Date: 12-03-12
 * Time: 11:02 AM
 */
public class DataTableDecoder {
    // --------------------------------------------------------------------- //
    // Selection ----------------------------------------------------------- //
    // --------------------------------------------------------------------- //
    static void decodeSelection(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        String selection = params.get(clientId + "_selection");

        if (table.isSingleSelectionMode()) decodeSingleSelection(table, selection, params.get(clientId + "_deselection"));
        else decodeMultipleSelection(table, selection, params.get(clientId + "_deselection"));
        queueInstantSelectionEvent(context, table, clientId, params);
    }

    static void queueInstantSelectionEvent(FacesContext context, DataTable table, String clientId, Map<String,String> params) {
        if (table.isInstantSelectionRequest(context)) {
            Object model = table.getDataModel();
            TreeDataModel treeModel = null;
            String selection = params.get(clientId + "_instantSelectedRowIndexes");
            String[] indexes = selection.split(",");
            Object[] objs = new Object[indexes.length];

            for (int i = 0; i < indexes.length; i++) {
                String index = indexes[i];
                // If selection occurs with a TreeModel and non-root index
                if (table.hasTreeDataModel() && selection.indexOf('.') > 0) {
                    treeModel = (TreeDataModel) model;
                    int lastSepIndex = index.lastIndexOf('.');
                    treeModel.setRootIndex(index.substring(0, lastSepIndex));
                    index = index.substring(lastSepIndex+1);
                }

                table.setRowIndex(Integer.parseInt(index));
                objs[i] = table.getRowData();
            }
            SelectEvent selectEvent = new SelectEvent(table, objs);
            selectEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
            table.queueEvent(selectEvent);

            if (treeModel != null) treeModel.setRootIndex(null);
        }
        else if (table.isInstantUnselectionRequest(context)) {
            Object model = table.getDataModel();
            TreeDataModel treeModel = null;
            String selection = params.get(clientId + "_instantUnselectedRowIndexes");

            // If unselection occurs with a TreeModel and non-root index
            if (table.hasTreeDataModel() && selection.indexOf('.') > 0) {
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

    static void decodeSingleSelection(DataTable table, String selection, String deselection) {
        RowStateMap stateMap = table.getStateMap();

        if (isValueBlank(selection)) {
            if (deselection != null && !"".equals(deselection))
                stateMap.setAllSelected(false);
        }
        else if (table.isCellSelection()) {
            table.clearCellSelection();
            table.addSelectedCell(selection);
            if (deselection != null && deselection.length() > 0)
                table.removeSelectedCell(deselection);
        }
        else {
            TreeDataModel treeModel = null;
            Object model = (Object) table.getDataModel();

            if (table.hasTreeDataModel()) treeModel = (TreeDataModel) model;

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

    static void decodeMultipleSelection(DataTable table, String selection, String deselection) {
        Object value = table.getDataModel();
        TreeDataModel model = null;
        if (table.hasTreeDataModel()) model = (TreeDataModel) value;
        RowStateMap stateMap = table.getStateMap();

        // Process selections
        if (isValueBlank(selection)) {}
        else if (table.isCellSelection()) {
            table.addSelectedCells(selection.split(","));
        } else {
            String[] rowSelectValues = selection.split(",");

            for (String s : rowSelectValues) {
                // Handle tree case indexes
                if (s.indexOf(".") != -1 && model != null) {
                    int lastSepIndex = s.lastIndexOf('.');
                    model.setRootIndex(s.substring(0, lastSepIndex));
                    s = s.substring(lastSepIndex+1);
                }
                table.setRowIndex(Integer.parseInt(s));

                RowState state = stateMap.get(table.getRowData());
                if (!state.isSelected() && state.isSelectable())
                    state.setSelected(true);

                // Cleanup after tree case indexes
                if (model != null) model.setRootIndex(null);
            }
        }

        // Process deselections
        if (table.isCellSelection()) {
            if (deselection != null && deselection.length() > 0)
                table.removeSelectedCells(deselection.split(","));
        } else {
            String[] rowDeselectValues = new String[0];
            if (deselection != null && !deselection.equals(""))
                rowDeselectValues = deselection.split(",");

            int x = 0;
            for (String s : rowDeselectValues) {
                // Handle tree case indexes
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
        }

        table.setRowIndex(-1);
    }

    // Util ---------------------------------------------------------------- //
    static boolean isValueBlank(String value) {
        if (value == null) return true;
        return value.trim().equals("");
    }

    

    // --------------------------------------------------------------------- //
    // Filter -------------------------------------------------------------- //
    // --------------------------------------------------------------------- //
    static void decodeFilters(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        String filteredId = params.get(clientId + "_filteredColumn");
        Column filteredColumn = null;

        table.setSavedFilterState(new FilterState(table));

        // Ensure this refiltering occurs on the original data
        table.setFirst(0);
        table.setPage(1);

        if (table.isLazy()) {
            // If in lazy case, just save change to filter input. Load method must account for the rest.
            Map<String,Column> filterMap = table.getFilterMap();
            filteredColumn = filterMap.get(filteredId);
            if (filteredColumn != null) filteredColumn.setFilterValue(params.get(filteredId).toLowerCase());

            if (table.isPaginator())
                if (RequestContext.getCurrentInstance() != null)
                    RequestContext.getCurrentInstance().addCallbackParam("totalRecords", table.getRowCount());
        } else {
            Map<String,Column> filterMap = table.getFilterMap();

            // If applying a new filter, save the value to the column
            filteredColumn = filterMap.get(filteredId);

            if (filteredColumn != null)
                filteredColumn.setFilterValue(params.get(filteredId).toLowerCase());

            // Get the value of the global filter
            String globalFilter = params.get(clientId + UINamingContainer.getSeparatorChar(context) + "globalFilter");
            table.setFilterValue(globalFilter);

            // Queue filters for later processing
            table.applyFilters();
        }
    }


    
    // --------------------------------------------------------------------- //
    // Sort ---------------------------------------------------------------- //
    // --------------------------------------------------------------------- //
    static void decodeSortRequest(FacesContext context, DataTable table, String clientId, String sortKeysInput) {
        List<Column> columns = new ArrayList<Column>();
        table.setSavedSortState(new SortState(table));
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        ColumnGroup group = table.getColumnGroup("header");
        Column sortColumn = null;

        // ClientId null if coming from the tableConfigPanel decode.
        if (clientId == null) clientId = table.getClientId(context);
        String[] sortKeys = (sortKeysInput != null) ? sortKeysInput.split(",") : params.get(clientId + "_sortKeys").split(",");
        String[] sortDirs = params.get(clientId + "_sortDirs").split(",");

        // Get header columns from grouped header
        if (group != null) {
            for (UIComponent c : group.getChildren()) {
                if (c instanceof Row) for (UIComponent rc : c.getChildren()) {
                    if (rc instanceof Column) columns.add((Column)rc);
                }
            }
        } else columns = table.getColumns();

        // Reset all priorities, new list incoming
        for (Column c : columns) {
            c.setSortPriority(null);
        }

        if (sortKeys[0].equals("")) {
            return;
        }

        int i = 0;
        for (String sortKey : sortKeys) {
            if (group != null) {
                outer: for (UIComponent child : group.getChildren()) {
                    for (UIComponent headerRowChild : ((Row)child).getChildren()) {
                        if (headerRowChild instanceof Column)
                            if (headerRowChild.getClientId(context).equals(sortKey)) {
                                sortColumn = (Column) headerRowChild;
                                break outer;
                            }
                    }
                }
            } else {
                for (Column column : table.getColumns()) {
                    if (column.getClientId(context).equals(sortKey)) {
                        sortColumn = column;
                        break;
                    }
                }
            }

            sortColumn.setSortPriority(i+1);
            sortColumn.setSortAscending(Boolean.parseBoolean(sortDirs[i]));
            i++;
        }
    }

    // --------------------------------------------------------------------- //
    // Pagination ---------------------------------------------------------- //
    // --------------------------------------------------------------------- //
    static void decodePageRequest(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();

        String rowsParam = params.get(clientId + "_rows");
        String pageParam = params.get(clientId + "_page");

        table.setRows(Integer.valueOf(rowsParam));
        table.setPage(Integer.valueOf(pageParam));
        table.setFirst((table.getPage() - 1) * table.getRows());
    }

    
    
    // --------------------------------------------------------------------- //
    // Column Reorder ------------------------------------------------------ //
    // --------------------------------------------------------------------- //
    static void decodeColumnReorderRequest(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();

        List<Integer> ordering = table.getColumnOrdering();
        String[] columnTargets = params.get(clientId + "_columnReorder").split("-");
        Integer columnIndex = ordering.remove(Integer.parseInt(columnTargets[0]));
        ordering.add(Integer.parseInt(columnTargets[1]), columnIndex);
        // this call just to indicate a change has taken place to col order, and recalc
        table.setColumnOrdering(ordering);
    }



    // --------------------------------------------------------------------- //
    // Table Conf ---------------------------------------------------------- //
    // --------------------------------------------------------------------- //    
    static void decodeTableConfigurationRequest(FacesContext context, DataTable table) {
        TableConfigPanel tableConfigPanel = table.findTableConfigPanel(context);
        decodeColumnConfigurations(context, table, tableConfigPanel);
    }

    static private void decodeColumnConfigurations(FacesContext context, DataTable table, TableConfigPanel panel) {
        int i;
        String clientId = table.getClientId(context);
        List<Column> columns = table.getColumns();
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        boolean visibility = panel.isColumnVisibilityConfigurable();
        boolean ordering = panel.isColumnOrderingConfigurable();
        boolean sizing = false; //panel.isColumnSizingConfigurable();
        boolean name = panel.isColumnNameConfigurable();
        boolean firstCol = panel.getType().equals("first-col") ;
        boolean lastCol = panel.getType().equals("last-col");
        boolean sorting = panel.isColumnSortingConfigurable();

        for (i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);

            if (column.isConfigurable()) {
                boolean disableVisibilityControl = (firstCol && i == 0) || ((lastCol && i == columns.size() - 1));

                String panelId = panel.getClientId();
                if (visibility && !disableVisibilityControl) decodeColumnVisibility(params, column, i, panelId);
                if (sizing) decodeColumnSizing(params, column, i, panelId);
                if (name) decodeColumnName(params, column, i, panelId);
            }
        }

        if (ordering) decodeColumnOrdering(params, table, clientId);
        if (sorting) {
            decodeSortRequest(context, table, clientId,
                    processConfigPanelSortKeys(clientId, params, table));
        }
    }

    static private void decodeColumnName(Map<String, String> params, Column column, int i, String clientId) {
        String text = params.get(clientId + "_head_" + i);
        column.setHeaderText(text);
    }

    static private void decodeColumnOrdering(Map<String, String> params, DataTable table, String clientId) {
        String[] indexes = params.get(clientId + "_colorder").split(",");
        table.setColumnOrdering(indexes);
    }

    static private void decodeColumnSizing(Map<String, String> params, Column column, int i, String clientId) {

    }

    static private void decodeColumnVisibility(Map<String, String> params, Column column, int i, String clientId) {
        String code = params.get(clientId + "_colvis_" + i);
        ValueExpression valueExpression = column.getValueExpression("rendered");
        if (valueExpression != null) {
            if (code == null) valueExpression.setValue(FacesContext.getCurrentInstance().getELContext(),
                    Boolean.FALSE);
            else valueExpression.setValue(FacesContext.getCurrentInstance().getELContext(),
                    Boolean.TRUE);
        } else {
            if (code == null) column.setRendered(false);
            else column.setRendered(true);
        }
    }

    // Util ---------------------------------------------------------------- //
    static private String processConfigPanelSortKeys(String clientId, Map<String, String> params, DataTable table) {
        String[] sortKeys = params.get(clientId + "_sortKeys").split(",");
        List<Column> columns = table.getColumns();
        String newSortKeys = "";

        for (String key : sortKeys) {
            if (key.length() > 0) {
                if (newSortKeys.length() == 0) newSortKeys = columns.get(Integer.parseInt(key)).getClientId();
                else newSortKeys += "," + columns.get(Integer.parseInt(key)).getClientId();
            }
        }

        return newSortKeys;
    }
}
