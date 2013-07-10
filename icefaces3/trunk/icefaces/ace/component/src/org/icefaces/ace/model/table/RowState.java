/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.ace.model.table;

import javax.faces.component.UIComponent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RowState implements Serializable {
    public enum ExpansionType { PANEL, ROW, NONE }
    ExpansionType expansionType = ExpansionType.PANEL;

    // Type of row, used for per-row-class column rendering
    String type = "default";

    boolean selected = false;
    boolean selectable = true;
    boolean expanded = false;
    boolean expandable = true;
    boolean editable = true;
    boolean visible = true;
    List<String> activeCellEditorIds;
    List<String> selectedColumnIds;


    public RowState() {}

    public ExpansionType getExpansionType() {
        return expansionType;
    }

    public void setExpansionType(ExpansionType expansionType) {
        this.expansionType = expansionType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public List<String> getActiveCellEditorIds() {
        if (activeCellEditorIds == null) activeCellEditorIds = new ArrayList<String>();
        return activeCellEditorIds;
    }

    public List<String> getSelectedColumnIds() {
        if (selectedColumnIds == null) selectedColumnIds = new ArrayList<String>();
        return selectedColumnIds;
    }

    public void setSelectedColumnIds(List<String> selectedColumnIds) {
        this.selectedColumnIds = selectedColumnIds;
    }

    public void addSelectedCell(UIComponent column) {
        if (column != null) {
            String id = column.getId();
            if (!getSelectedColumnIds().contains(id))
                getSelectedColumnIds().add(id);
        }
    }

    public void removedSelectedCell(UIComponent column) {
        if (column != null) {
            getSelectedColumnIds().remove(column.getId());
        }
    }

    public void setActiveCellEditorIds(List<String> activeCellEditorIds) {
        this.activeCellEditorIds = activeCellEditorIds;
    }

    /**
     * Convenience method to take the id of the CellEditor component and add it to the list of active editors
     * in the row state for a given row object.
     *
     * Only call this method while DataTable has a row index set or the editor will be disabled for all rows.
     * @param editor
     */
    public void addActiveCellEditor(UIComponent editor) {
        if (editor != null) {
            String id = editor.getId();
            if (!getActiveCellEditorIds().contains(id))
                getActiveCellEditorIds().add(id);
        }
    }

    /**
     * Convenience method to take the id of the CellEditor component and remove it from the list of active editors
     * in the row state for a given row object.
     *
     * Only call this method while DataTable has a row index set or the editor will be disabled for all rows.
     * @param editor
     */
    public void removeActiveCellEditor(UIComponent editor) {
        if (editor != null) {
            getActiveCellEditorIds().remove(editor.getId());
        }
    }
}
