package com.icesoft.faces.component.ext;

import javax.faces.component.UIPanel;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;

public class HeaderRow extends UIPanel {
    public static final String COMPONENT_TYPE = "com.icesoft.faces.HeaderRow";
    private String style = null;
    private String styleClass = null;
    private String colspan = null;
    private String rowspan = null;
    
    public String getComponentType() {
        return COMPONENT_TYPE;
    }
    
    /**
     * <p>Set the value of the <code>style</code> property.</p>
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * <p>Return the value of the <code>style</code> property.</p>
     */
    public String getStyle() {
        if (style != null) {
            return style;
        }
        ValueBinding vb = getValueBinding("style");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>styleClass</code> property.</p>
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     */
    public String getStyleClass() {
        if (styleClass != null) {
            return styleClass;
        }
        ValueBinding vb = getValueBinding("styleClass");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    } 

    /**
     * <p>Set the value of the <code>colspan</code> property.</p>
     */
    public void setColspan(String colspan) {
        this.colspan = colspan;
    }

    /**
     * <p>Return the value of the <code>colspan</code> property.</p>
     */
    public String getColspan() {
        if (colspan != null) {
            return colspan;
        }
        ValueBinding vb = getValueBinding("colspan");
        return vb != null ? (String) vb.getValue(getFacesContext()) :null;
    }
    
    /**
     * <p>Set the value of the <code>rowspan</code> property.</p>
     */
    public void setRowspan(String rowspan) {
        this.rowspan = rowspan;
    }

    /**
     * <p>Return the value of the <code>rowspan</code> property.</p>
     */
    public String getRowspan() {
        if (rowspan != null) {
            return rowspan;
        }
        ValueBinding vb = getValueBinding("rowspan");
        return vb != null ? (String) vb.getValue(getFacesContext()) :null;
    }    
}
