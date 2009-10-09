package org.icefaces.component.tab;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;


@Component(tagName ="tab",
        componentClass ="org.icefaces.component.tab.Tab",
        componentType = "com.icesoft.faces.Tab",
        rendererType ="",
        extendsClass = "javax.faces.component.UICommand", 
        generatedClass = "org.icefaces.component.tab.TabBase")
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