package org.icefaces.application.showcase.view.bean.examples.component.dataTable;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.context.effects.Pulsate;

import javax.faces.model.SelectItem;
import java.io.Serializable;

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
 * Date: 12-01-30
 * Time: 11:42 AM
 */

public class DataTableFind implements Serializable {
    public static final String BEAN_NAME = "dataTableFind";

    public String selectedEffectType = "default";
    public String selectedSearchMode = "contains";
    public String searchQuery = "";
    public HtmlDataTable iceTable = null;
    public String[] selectedColumns = new String[]{"id", "firstName", "lastName", "phone"};
    public int lastFoundIndex = -1;
    private boolean caseSensitive;

    public final SelectItem[] SEARCH_MODES = {new SelectItem("startsWith", "Starts With"),
            new SelectItem("endsWith", "Ends With"),
            new SelectItem("contains", "Contains"),
            new SelectItem("exactMatch", "Exact Match")};

    public final SelectItem[] COLUMNS = {new SelectItem("id", "Id"),
            new SelectItem("firstName", "First Name"),
            new SelectItem("lastName", "Last Name"),
            new SelectItem("phone", "Phone")};

    public final SelectItem[] EFFECT_TYPES = {new SelectItem("none", "None"),
            new SelectItem("default", "Default (Highlight)"),
            new SelectItem("pulsate", "Pulsate")};


    public SelectItem[] getSEARCH_MODES() {
        return SEARCH_MODES;
    }

    public String getSelectedSearchMode() {
        return selectedSearchMode;
    }

    public void setSelectedSearchMode(String selectedSearchMode) {
        this.selectedSearchMode = selectedSearchMode;
    }

    public String[] getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(String[] selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    public SelectItem[] getCOLUMNS() {
        return COLUMNS;
    }

    public SelectItem[] getEFFECT_TYPES() {
        return EFFECT_TYPES;
    }

    public String getSelectedEffectType() {
        return selectedEffectType;
    }

    public void setSelectedEffectType(String selectedEffectType) {
        this.selectedEffectType = selectedEffectType;
    }


    public void find(javax.faces.event.ActionEvent e) {
        lastFoundIndex = iceTable.findRow(searchQuery, selectedColumns, lastFoundIndex + 1, selectedSearchMode, caseSensitive);

        if (selectedEffectType.equals("default"))
            iceTable.navigateToRow(lastFoundIndex);
        else if (selectedEffectType.equals("pulsate"))
            iceTable.navigateToRow(lastFoundIndex, new Pulsate());
        else if (selectedEffectType.equals("none"))
            iceTable.navigateToRow(lastFoundIndex, null);
    }

    public void reset(javax.faces.event.ActionEvent e) {
        lastFoundIndex = -1;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public HtmlDataTable getIceTable() {
        return iceTable;
    }

    public void setIceTable(HtmlDataTable iceTable) {
        this.iceTable = iceTable;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
}
