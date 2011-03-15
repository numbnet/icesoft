package com.icesoft.icefaces.tutorial.component.tabset.placement;

import java.util.List;
import java.util.ArrayList;
import javax.faces.event.ValueChangeEvent;

/**
 * <p>
 * A basic backing bean for a ice:panelTabSet component. The only instance variable
 * needed is a list of tabs which are used to store the label and content of each tab.
 * Technically the tabs could be hardcoded directly on the .jspx page, but this
 * approach allows easier modification and change.
 * </p>
 * <p>
 * This class also supports the switching and storing of the position of the tabs.
 * To do this, a getter and setter of a position String which is updated through the
 * .jspx page. 
 * </p>
 */
public class TabsetBean {
    private static final int NUMBER_OF_TABS = 3;
    
    private List tabs = new ArrayList(NUMBER_OF_TABS);
    private String placement = "top";
    
    public TabsetBean() {
        Tab toAdd;
        for (int i = 0; i < NUMBER_OF_TABS; i++) {
            toAdd = new Tab("Label " + (i+1),
                            "Content " + (i+1));
                            
            tabs.add(toAdd);
        }
    }
    
    public List getTabs() {
        return tabs;
    }
    
    public String getPlacement() {
        return placement;
    }
    
    public void setTabs(List tabs) {
        this.tabs = tabs;
    }
    
    public void setPlacement(String placement) {
        this.placement = placement;
    }
}

