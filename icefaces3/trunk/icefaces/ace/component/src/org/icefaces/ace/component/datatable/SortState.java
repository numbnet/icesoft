package org.icefaces.ace.component.datatable;

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.columngroup.ColumnGroup;
import org.icefaces.ace.component.row.Row;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.HashMap;
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
 * Date: 2012-09-20
 * Time: 12:16 PM
 */
public class SortState {
    Map<Column, ColumnState> stateMap = new HashMap<Column, ColumnState>();

    class ColumnState implements Serializable {
        Integer priority;
        Boolean ascending;

        ColumnState(Integer sortPriority, Boolean ascending) {
            this.priority = sortPriority;
            this.ascending = ascending;
        }
    }

    public SortState() {}

    public SortState(DataTable table) {
        List<Column> columnList = table.getColumns(true);
        for (Column column : columnList)
            saveState(column);
    }

    public SortState(FacesContext context, DataTable table) {
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        ColumnGroup group = table.getColumnGroup("header");
        Column sortColumn = null;
        String clientId = table.getClientId(context);
        String[] sortKeys = params.get(clientId + "_sortKeys").split(",");
        String[] sortDirs = params.get(clientId + "_sortDirs").split(",");
        List<Column> columns = table.getColumns(true);

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

            saveState(sortColumn, i+1, Boolean.parseBoolean(sortDirs[i]));
            i++;
        }
    }

    public void saveState(Column column) {
        stateMap.put(column, new ColumnState(column.getSortPriority(), column.isSortAscending()));
    }

    public void saveState(Column column, Integer priority, Boolean ascending) {
        stateMap.put(column, new ColumnState(priority, ascending));
    }

    private void restoreState(Column column) {
        ColumnState state = stateMap.get(column);
        if (state != null) {
            column.setSortPriority(state.priority);
            column.setSortAscending(state.ascending);
        }
    }

    public void restoreState(DataTable table) {
        List<Column> columnList = table.getColumns(true);

        for (Column column : columnList) {
            column.setSortPriority(null);
            restoreState(column);
        }
    }
}
