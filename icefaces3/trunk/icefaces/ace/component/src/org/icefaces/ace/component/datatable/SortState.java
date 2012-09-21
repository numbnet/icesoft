package org.icefaces.ace.component.datatable;

import org.icefaces.ace.component.column.Column;

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

    class ColumnState {
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

    public void saveState(Column column) {
        stateMap.put(column, new ColumnState(column.getSortPriority(), column.isSortAscending()));
    }

    private void restoreState(Column column) {
        ColumnState state = stateMap.get(column);
        column.setSortPriority(state.priority);
        column.setSortAscending(state.ascending);
    }

    public void restoreState(DataTable table) {
        List<Column> columnList = table.getColumns(true);
        for (Column column : columnList)
            restoreState(column);
    }
}
