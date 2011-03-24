package org.icepush.integration.icepushplace.bean;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;

import java.io.Serializable;

/**
 * Class used to track the previous region a user was in
 * This is necessary to move a user when they change their region and press Update
 */
@ManagedBean(name="personRegion")
@CustomScoped(value = "#{window}")
public class PersonRegion implements Serializable {
    private int oldRegion = -1;
    
    public int getOldRegion() {
        return oldRegion;
    }
    
    public void setOldRegion(int oldRegion) {
        this.oldRegion = oldRegion;
    }
    
    public void changed(ValueChangeEvent event) {
        if ((event != null) && (event.getOldValue() != null)) {
            oldRegion = Integer.parseInt(event.getOldValue().toString());
        }
    }
}
