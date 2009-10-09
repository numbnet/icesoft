package org.icefaces.component.tab;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;


@Component(tag_name="tab",
        component_class="org.icefaces.component.tab.Tab",
        component_type = "com.icesoft.faces.Tab",
        renderer_type="",
        extends_class = "javax.faces.component.UICommand", 
        generated_class = "org.icefaces.component.tab.TabBase")
public class TabMeta {
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
}