package com.icesoft.icefaces.tutorial.component.tabset.style;

/**
 * <p>
 * A basic class representing a Tab object, which basically means a label and content string
 * By using a seperate class to represent a tab, new elements and components can be added
 * into a tab and reflected here without major reworking of the code.
 * </p>
 * <p>
 * The class provides standard getters and setters as well as friendly constructors for
 * providing various degrees of detail.
 * </p>
 *
 */
public class Tab
{
    private String label;
    private String content;
    
    public Tab() {
        label = "Default Label";
        content = "Default Content";
    }
    
    public Tab(String label) {
        this.label = label;
        content = "Default Content";
    }
    
    public Tab(String label, String content) {
        this.label = label;
        this.content = content;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
}

