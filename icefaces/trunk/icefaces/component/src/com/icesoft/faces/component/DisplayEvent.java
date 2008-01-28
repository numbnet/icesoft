package com.icesoft.faces.component;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class DisplayEvent extends FacesEvent{

    private static final long serialVersionUID = 0L;
    UIComponent target = null;
    Object value = null;
    boolean visible = false;
    
    public DisplayEvent(UIComponent component) {
        super(component);
    }

    public DisplayEvent(UIComponent component, UIComponent target) {
        super(component);
        this.target = target;
    }
    
    public DisplayEvent(UIComponent component, UIComponent target, Object value) {
        super(component);
        this.target = target;
        this.value = value;
    }

    public DisplayEvent(UIComponent component, 
                        UIComponent target, 
                        Object value,
                        boolean visible) {
        super(component);
        this.target = target;
        this.value = value;
        this.visible = visible;
    }
    
    public UIComponent getTarget() {
        return target;
    }

    public Object getValue() {
        return value;
    }

    public boolean isVisible() {
        return visible;
    }
    
    public boolean isAppropriateListener(FacesListener listener) {
        // TODO Auto-generated method stub
        return false;
    }


    public void processListener(FacesListener listener) {
        // TODO Auto-generated method stub
    }
}
