package org.icefaces.component.tab;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Field;
import org.icefaces.component.annotation.Property;

import org.icefaces.component.baseMeta.UIPanelMeta;


@Component(tagName ="tabSetController",
        componentClass  = "org.icefaces.component.tab.TabSetController",
        generatedClass  = "org.icefaces.component.tab.TabSetControllerBase",
        extendsClass    = "javax.faces.component.UIPanel", 
        componentType   = "org.icefaces.TabSetController",
        componentFamily = "org.icefaces.TabSetController")
public class TabSetControllerMeta extends UIPanelMeta {
    @Property (tlddoc="id of the tabSet component") 
    private String For;
}
