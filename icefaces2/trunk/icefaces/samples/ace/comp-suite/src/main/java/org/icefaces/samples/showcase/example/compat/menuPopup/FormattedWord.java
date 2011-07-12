package org.icefaces.samples.showcase.example.compat.menuPopup;

import java.io.Serializable;

public class FormattedWord implements Serializable {
    private String text;
    private String style;
    
    public FormattedWord(String text) {
        this(text, null);
    }
    
    public FormattedWord(String text, String style) {
        this.text = text;
        this.style = style;
    }
    
    public String getText() { return text; }
    public String getStyle() { return style; }
    
    public void setText(String text) { this.text = text; }
    public void setStyle(String style) { this.style = style; }
    
    public String toString() {
        return text;
    }
}
