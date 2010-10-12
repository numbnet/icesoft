package org.icefaces.component.tab;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Field;
import org.icefaces.component.annotation.Property;

import org.icefaces.component.baseMeta.UIPanelMeta;

@Component(tagName ="tabSetController",
        componentClass ="org.icefaces.component.tab.TabSetController",
        componentType = "com.icesoft.faces.TabSetController",
        extendsClass = "javax.faces.component.UIPanel", 
        generatedClass = "org.icefaces.component.tab.TabSetControllerBase",
        componentFamily="com.icesoft.faces.TabSet"
        )
public class TabSetControllerMeta extends UIPanelMeta {
    @Property (tlddoc="id of the tabset component") 
    private String For;
    
}
