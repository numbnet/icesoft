package org.icefaces.component.event;

import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;

public class PropertyChangeEvent extends ValueChangeEvent {
    private static final long serialVersionUID = 1L;
    
    /*
     * Name of the property
     */
    private String name; 
    
    public PropertyChangeEvent(UIComponent component, Object oldValue,
            Object newValue, String name) {
        super(component, oldValue, newValue);
        this.name = name;
    }

    /**
     * <p>Return the value of the <code>name</code> property.</p>
     */
    public String getName() {
        return name;
    }
}
