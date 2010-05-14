package org.icefaces.component.tab;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Field;
import org.icefaces.component.annotation.Property;

@Component(tagName ="tabSetController",
        componentClass ="org.icefaces.component.tab.TabSetController",
        componentType = "com.icesoft.faces.TabSetController",
        extendsClass = "javax.faces.component.UIPanel", 
        generatedClass = "org.icefaces.component.tab.TabSetControllerBase",
        componentFamily="com.icesoft.faces.TabSet"
        )
public class TabSetControllerMeta {
    @Property  
    private String For;
    
}
