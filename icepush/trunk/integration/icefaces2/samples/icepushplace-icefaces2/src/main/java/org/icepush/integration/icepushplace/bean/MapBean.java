package org.icepush.integration.icepushplace.bean;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import java.io.Serializable;

@ManagedBean(name="mapBean")
@CustomScoped(value = "#{window}")
public class MapBean implements Serializable {
    private boolean consolePopupVisible = true;
    
    public boolean getConsolePopupVisible() {
        return consolePopupVisible;
    }
    
    public void setConsolePopupVisible(boolean consolePopupVisible) {
        this.consolePopupVisible = consolePopupVisible;
    }
    
    public void toggleConsolePopup(ActionEvent event) {
        consolePopupVisible = !consolePopupVisible;
    }
}
