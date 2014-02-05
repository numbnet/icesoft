package org.icefaces.mobile.layout;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.icefaces.mobi.utils.MobiJSFUtils;

@ManagedBean
@SessionScoped
public class ContentStackMenu4 {
    
    private String currentPane;

    public String getCurrentPane() {
        return currentPane;
    }

    public void setCurrentPane(String currentPane) {
        this.currentPane = currentPane;
    }
    
    public void goToMenu(ActionEvent evt){
        currentPane = "menu";
        System.out.println("goToMenu()");
    }
}
