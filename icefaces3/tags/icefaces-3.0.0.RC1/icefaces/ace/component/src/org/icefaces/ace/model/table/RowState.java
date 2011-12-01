package org.icefaces.ace.model.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RowState implements Serializable{
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
}
