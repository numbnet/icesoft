package com.icesoft.icefaces.tutorial.component.tabset.basic;

import java.util.List;
import java.util.ArrayList;

/**
 * <p>
 * A basic backing bean for a ice:panelTabSet component. The only instance variable
 * needed is a list of tabs which are used to store the label and content of each tab.
 * Technically the tabs could be hardcoded directly on the .jspx page, but this
 * approach allows easier modification and change.
 * </p>
 */
public class TabsetBean {
    private static final int NUMBER_OF_TABS = 3; // simple to change to add more default tabs
    
    private List tabs = new ArrayList(NUMBER_OF_TABS);
    
    public TabsetBean() {
        // Loop and add default tabs with generic labels and content
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
    
    public void setTabs(List tabs) {
        this.tabs = tabs;
    }
}

