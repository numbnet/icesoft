package org.icefaces.mobi.component.submitnotification;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;

@Component(
        tagName = "submitNotification",
        componentClass = "org.icefaces.mobi.component.submitnotification.SubmitNotification",
        rendererClass = "org.icefaces.mobi.component.submitnotification.SubmitNotificationRenderer",
        generatedClass = "org.icefaces.mobi.component.submitnotification.SubmitNotificationBase",
        componentType = "org.icefaces.SubmitNotification",
        rendererType = "org.icefaces.SubmitNotificationRenderer",
        extendsClass    = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.SubmitNotification",
        tlddoc = "This mobility component " +
                "renders a panel to be used with any mobi commandButton which blocks any other submission for the duration "+
                " the process triggered by the button until the update is complete")
public class SubmitNotificationMeta extends UIPanelMeta {

    @Property(tlddoc = "tabindex of the component")
    private int tabindex;

    @Property(tlddoc = "style will be rendered on the root element of this " +
            "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
            "this component.")
    private String styleClass;

}
