package org.icefaces.mobi.component.contentnavbar;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;

@Component(
        tagName = "contentNavBar",
        componentClass = "org.icefaces.mobi.component.contentnavbar.ContentNavBar",
        rendererClass = "org.icefaces.mobi.component.contentnavbar.ContentNavBarRenderer",
        generatedClass = "org.icefaces.mobi.component.contentnavbar.ContentNavBarBase",

        componentType = "org.icefaces.ContentNavBar",
        rendererType = "org.icefaces.ContentNavBarRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.ContentNavBar",
        tlddoc = "This mobility component renders a navigation tool bar for the contentPane" +
                " component that will allow children of contentMenuItem, and title.  A facet may be provided" +
                " in future if required.  Must be a child of the contentPane. "
)

public class ContentNavBarMeta {
    @Property(tlddoc = "style will be rendered on a root element of this component")
    private String style;

    @Property(tlddoc = "style class will be rendered on a root element of this component")
    private String styleClass;

    @Property(tlddoc = "if menuLabel is not null, then a menu button will be rendered on the tool bar." +
            " A menu button will be styled and placed on this toolbar and a target is expected of the id" +
            " for the menu's enclosing contentPane or home page contentPane to navigate through the stack.")
    private String menuButtonLabel;

    @Property(tlddoc = " required if using a menuButtonLabel or for home page navigation.  This requires the" +
            " id of the contentPane that you wish to navigate to.")
    private String menuButtonTargetId;
}
