package com.icesoft.faces.component.ext;

import javax.faces.event.ActionEvent;
import javax.faces.event.FacesListener;
import javax.faces.component.UIComponent;

public class ClickActionEvent extends ActionEvent {

    private RowSelectorEvent rowSelectorEvent = null;
    
    public ClickActionEvent(UIComponent component) {
        super(component);
    }
    
    public boolean isAppropriateListener(FacesListener listener) {
        return false;
    }
    
    public void processListener(FacesListener listener) {
    }
    
    public RowSelectorEvent getRowSelectorEvent() {
        return rowSelectorEvent;
    }
    
    public void setRowSelectorEvent(RowSelectorEvent rowSelectorEvent) {
        this.rowSelectorEvent = rowSelectorEvent;
    }
}
