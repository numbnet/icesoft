package org.icefaces.ace.component.datatable;

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.context.RequestContext;

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
public class FilterState {
    Map<Column, String> valueMap = new HashMap<Column, String>();

    public FilterState() {}

    /* Create comprehensive current filter state */
    public FilterState(DataTable table) {
        List<Column> columnList = table.getColumns(true);
        for (Column column : columnList)
            saveState(column);
    }

    /* Create delta state from incoming filter input */
    public FilterState(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        String filteredId = params.get(clientId + "_filteredColumn");
        Column filteredColumn = null;

        Map<String,Column> filterMap = table.getFilterMap();

        // If applying a new filter, save the value to the column
        filteredColumn = filterMap.get(filteredId);

        if (filteredColumn != null)
            saveState(filteredColumn, params.get(filteredId).toLowerCase());
    }

    public void saveState(Column column) {
        valueMap.put(column, column.getFilterValue());
    }

    public void saveState(Column column, String value) {
        valueMap.put(column, value);
    }

    private void restoreState(Column column) {
        String val = valueMap.get(column);
        if (val != null)
            column.setFilterValue(val);
    }

    public void restoreState(DataTable table) {
        List<Column> columnList = table.getColumns(true);
        for (Column column : columnList)
            restoreState(column);
    }
}
