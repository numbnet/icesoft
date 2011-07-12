package org.icefaces.samples.showcase.example.compat.tab;

import java.io.Serializable;

public class TabObject implements Serializable {
    private String label;
    private String content;
    
    public TabObject() {
    }
    
    public TabObject(String label, String content) {
        this.label = label;
        this.content = content;
    }
    
    public String getLabel() { return label; }
    public String getContent() { return content; }
    
    public void setLabel(String label) { this.label = label; }
    public void setContent(String content) { this.content = content; }
}
