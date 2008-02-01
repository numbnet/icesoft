package com.icesoft.faces.component;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesListener;
import javax.faces.event.ActionEvent;

public class ContextActionEvent extends ActionEvent {
    UIComponent target;
    Object contextValue;
    
    public ContextActionEvent(UIComponent component, UIComponent target, Object contextValue) {
        super(component);
        this.target = target;
        this.contextValue = contextValue;
    }
    
    public UIComponent getTarget() {
        return target;
    }

    public Object getContextValue() {
        return contextValue;
    }
    
    public boolean isAppropriateListener(FacesListener listener) {
        // TODO Auto-generated method stub
        return false;
    }


    public void processListener(FacesListener listener) {
        // TODO Auto-generated method stub
    }
}
