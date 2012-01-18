package org.icefaces.component.layoutcontainer;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;


@Component(
        tagName = "layoutContainer",
        componentClass = "org.icefaces.component.layoutcontainer.LayoutContainer",
        rendererClass = "org.icefaces.component.layoutcontainer.LayoutContainerRenderer",
        generatedClass = "org.icefaces.component.layoutcontainer.LayoutContainerBase",
        componentType = "org.icefaces.LayoutContainer",
        rendererType = "org.icefaces.LayoutContainerRenderer",
        extendsClass    = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.LayoutContainer",
        tlddoc = "This mobility component " +
                "layouts a template depending on the device size for single column for small device "+
                "and double column for Ipad or honeycomb or desktop browser"
)

public class LayoutContainerMeta extends UIPanelMeta {

    @Property(tlddoc=" true if single")
    private boolean single;

    @Property(tlddoc = "style will be rendered on the root element of this " +
    "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
        "this component.")
    private String styleClass;
}