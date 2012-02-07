package org.icefaces.mobi.component.panelconfirmation;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;

@Component(
        tagName = "panelConfirmation",
        componentClass = "org.icefaces.mobi.component.panelconfirmation.PanelConfirmation",
        rendererClass = "org.icefaces.mobi.component.panelconfirmation.PanelConfirmationRenderer",
        generatedClass = "org.icefaces.mobi.component.panelconfirmation.PanelConfirmationBase",
        componentType = "org.icefaces.PanelConfirmation",
        rendererType = "org.icefaces.PanelConfirmationRenderer",
        extendsClass    = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.PanelConfirmation",
        tlddoc = "This mobility component " +
                "renders a confirmation panel to be used with any mobi commandButton or menuButton"
)

@ClientBehaviorHolder(events = {
	@ClientEvent(name="accept", javadoc="...", tlddoc="...", defaultRender="@this", defaultExecute="@all")
}, defaultEvent="accept")

public class PanelConfirmationMeta extends UIPanelMeta{

    @Property(tlddoc = "style will be rendered on a root element of this component")
    private String style;

    @Property(tlddoc = "style class will be rendered on a root element of this component")
    private String styleClass;

    @Property(defaultValue="Confirm", tlddoc="title of confirmation panel in an attribute so el can be used to internationalize")
    private String title;

    @Property(tlddoc=" acceptOnly means only accept button, confirmOnly means only confirm button, both has both")
    private String type;

    @Property(defaultValue="Cancel", tlddoc="title of cancel button")
    private String cancelLabel;

    @Property(defaultValue="true", tlddoc="autocenter of panel")
    private boolean autoCenter;

    @Property(defaultValue="Confirm", tlddoc="title of confirm/accept button")
    private String acceptLabel;

    @Property(defaultValue="Confirm?", tlddoc="Message")
    private String message;


}
