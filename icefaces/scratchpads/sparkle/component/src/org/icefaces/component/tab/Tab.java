package org.icefaces.component.tab;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

public class Tab extends UICommand{
    public static final String COMPONENT_TYPE = "com.icesoft.faces.Tab";
    public static final String RENDERER_TYPE = "com.icesoft.faces.TabRenderer";
    private String label;
    private String dataSrc;
    private Boolean cacheData;      
    private Boolean disabled;    
    public Tab() {
        super();
        setRendererType(null);
    }
    
    public UIComponent getHeadFacet() {
        return (UIComponent) getFacet("header");
    }

    public UIComponent getBodyFacet() {
        return (UIComponent) getFacet("body");
    }
    
    public UIComponent getFooterFacet() {
        return (UIComponent) getFacet("footer");
    }

    public UIComponent getLabelFacet() {
        return (UIComponent) getFacet("label");
    }
    
    public String getLabel() {
        if (label != null) {
            return label;
        }
        ValueBinding vb = getValueBinding("label");
        return vb != null ? ((String) vb.getValue(getFacesContext())) : null;
    }  

    public void setLabel(String label) {
        this.label = label;
    }
    
    public void setDataSrc(String dataSrc) {
        this.dataSrc = dataSrc;
    }

    public String getDataSrc() {
        if (dataSrc != null) {
            return dataSrc;
        }
        ValueBinding vb = getValueBinding("dataSrc");
        return vb != null ? ((String) vb.getValue(getFacesContext())) : null;
    }  
    
    public void setCacheData(boolean cacheData) {
        this.cacheData = new Boolean(cacheData);
    }
    
    public boolean isCacheData() {
        if (cacheData != null) {
            return cacheData.booleanValue();
        }
        ValueBinding vb = getValueBinding("cacheData");
        return vb != null ? ((Boolean) vb.getValue(getFacesContext())).booleanValue() : true;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = new Boolean(disabled);
    }
    
    public boolean isDisabled() {
        if (disabled != null) {
            return disabled.booleanValue();
        }
        ValueBinding vb = getValueBinding("disabled");
        return vb != null ? ((Boolean) vb.getValue(getFacesContext())).booleanValue() : false;
    }    
}