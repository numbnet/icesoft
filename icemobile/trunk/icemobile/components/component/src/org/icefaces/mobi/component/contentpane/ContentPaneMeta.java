package org.icefaces.mobi.component.contentpane;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Required;

import javax.faces.component.UIComponent;

@Component(
        tagName = "contentPane",
        componentClass = "org.icefaces.mobi.component.contentpane.ContentPane",
        rendererClass = "org.icefaces.mobi.component.contentpane.ContentPaneRenderer",
        generatedClass = "org.icefaces.mobi.component.contentpane.ContentPaneBase",
        componentType = "org.icefaces.ContentPane",
        rendererType = "org.icefaces.ContentPaneRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.ContentPane",
        tlddoc = "This mobility component is a child of several different layout containers.  No client behavior" +
                " goes with this component.  In order to have any client-side behavior, it must be used with one" +
                " of the other layout components.  Children of contentStack, accordion or tabSet, or any other mobility" +
                " component which implements ContentPaneController.  The cacheType determines whether the children" +
                " of this component/panel  are " +
                " a) tobeconstructed = constructed in the server side component tree" +
                " b) constructed = children are constructed and present in server side component tree" +
                " c) client = children of this component are not only constructed but also are rendered" +
                "    on the client --best for static dataa  "
)

public class ContentPaneMeta extends UIPanelMeta{

     @Property(tlddoc="the selectedId must the id of a child panel to be displayed. Only one child may be selected" +
            " at a time.  If this is null, the first child panel will be shown.")
     private String selectedId;

     @Property(defaultValue="false", tlddoc=" makes sense to have the scrollable here as it will need certain style attributes for " +
            " and child panel to scroll within it. If accordionTitle is not null, then this should default to false")
     private boolean scrollable;

     @Property(defaultValue=" ", tlddoc="used when ContentPane is child of Accordion or tabSet")
     private String title;

     @Property(tlddoc = "style will be rendered on the root element of this " +
     "component.")
     private String style;

     @Property(tlddoc = "style class will be rendered on the root element of " +
        "this component.")
     private String styleClass;

     @Property(defaultValue="constructed",
             tlddoc = "cache type.  Options currently include client, constructed and tobeconstructed")
     private String cacheType;

}
