package org.icefaces.component.scrollpane;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;


@Component(
        tagName = "scrollPane",
        componentClass = "org.icefaces.component.scrollpane.ScrollPane",
        rendererClass = "org.icefaces.component.scrollpane.ScrollPaneRenderer",
        generatedClass = "org.icefaces.component.scrollpane.ScrollPaneBase",
        componentType = "org.icefaces.ScrollPane",
        rendererType = "org.icefaces.ScrollPaneRenderer",
        extendsClass    = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.ScrollPane",
        tlddoc = "This mobility component " +
                "represents a panel which will scrollpane in all mobile devices and desktop browsers"
)

public class ScrollPaneMeta extends UIPanelMeta {
    @Property (defaultValue = "content",
            tlddoc=" valid types are content, menu and single")
    private String type;

    @Property(tlddoc = "style will be rendered on the root element of this " +
    "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
        "this component.")
    private String styleClass;
}
