package org.icefaces.component.tab;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

import org.icefaces.component.baseMeta.UIPanelMeta;


@Component(tagName ="tabSetProxy",
        componentClass  = "org.icefaces.component.tab.TabSetProxy",
        generatedClass  = "org.icefaces.component.tab.TabSetProxyBase",
        extendsClass    = "javax.faces.component.UIPanel", 
        componentType   = "org.icefaces.TabSetProxy",
        componentFamily = "org.icefaces.TabSetProxy")
public class TabSetProxyMeta extends UIPanelMeta {
    @Property (tlddoc="id of the tabSet component") 
    private String For;
}
