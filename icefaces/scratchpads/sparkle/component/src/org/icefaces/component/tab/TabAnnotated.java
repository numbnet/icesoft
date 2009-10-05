package org.icefaces.component.tab;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;


@Component(component_class="org.icefaces.component.tab.Tab",
        tag_name="tab"
        )
public class TabAnnotated extends UICommand{
    public static final String COMPONENT_TYPE = "com.icesoft.faces.Tab";
    public static final String RENDERER_TYPE = null;
    @Property   
    protected String label;
    @Property   
    protected String dataSrc;
    @Property   
    protected Boolean cacheData;
    @Property   
    protected Boolean disabled;    
    @Property (inherit=true)
    protected String id;    
    @Property (inherit=true)
    protected boolean rendered; 
    
    
    public TabAnnotated() {
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
    
  
}