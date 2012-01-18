package org.icefaces.component.layoutcontainer;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;


@Component(
        tagName = "layoutPane",
        componentClass = "org.icefaces.component.layoutcontainer.LayoutPane",
        rendererClass = "org.icefaces.component.layoutcontainer.LayoutPaneRenderer",
        generatedClass = "org.icefaces.component.layoutcontainer.LayoutPaneBase",
        componentType = "org.icefaces.LayoutPane",
        rendererType = "org.icefaces.LayoutPaneRenderer",
        extendsClass    = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.LayoutPane",
        tlddoc = "This mobility component " +
                "renders a panel for the laoutContainer component parent can be either header or footer, "+
                " for double column for Ipad or honeycomb or desktop browser or single column layout"
)

public class LayoutPaneMeta extends UIPanelMeta {

    @Property(tlddoc = "type is either header or footer")
    private String type;

    @Property(tlddoc = "style will be rendered on the root element of this " +
    "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
        "this component.")
    private String styleClass;
}