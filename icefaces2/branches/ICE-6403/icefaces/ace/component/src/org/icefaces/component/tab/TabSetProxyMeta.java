package org.icefaces.component.tab;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

import org.icefaces.component.baseMeta.UIPanelMeta;


@Component(tagName ="tabSetProxy",
        componentClass  = "org.icefaces.component.tab.TabSetProxy",
        generatedClass  = "org.icefaces.component.tab.TabSetProxyBase",
        extendsClass    = "javax.faces.component.UIPanel", 
        componentType   = "org.icefaces.TabSetProxy",
        componentFamily = "org.icefaces.TabSetProxy",
        tlddoc = "The TabSetProxy component is used in conjunction with a " +
            "server-side TabSet component that is not inside of a form. " +
            "The TabSetProxy will then instead be placed inside of a form, " +
            "to handle the server communication on behalf of the TabSet.")
public class TabSetProxyMeta extends UIPanelMeta {
    @Property (tlddoc="id of the tabSet component") 
    private String For;
}
