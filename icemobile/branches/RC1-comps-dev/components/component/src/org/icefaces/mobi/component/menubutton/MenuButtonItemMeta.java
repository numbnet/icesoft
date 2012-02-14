package org.icefaces.mobi.component.menubutton;
import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UICommandMeta;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@ClientBehaviorHolder(events = {
	@ClientEvent(name="click", javadoc="Fired when a menuButtonItem is selected or clicked",
            tlddoc="Fired when commandButton is clicked", defaultRender="@all",
            defaultExecute="@all")}, defaultEvent="click")
@Component(
        tagName = "menuButtonItem",
        componentClass = "org.icefaces.mobi.component.menubutton.MenuButtonItem",
        rendererClass = "org.icefaces.mobi.component.menubutton.MenuButtonItemRenderer",
        generatedClass = "org.icefaces.mobi.component.menubutton.MenuButtonItemBase",
        extendsClass = "javax.faces.component.UICommand",
        componentType = "org.icefaces.component.MenuButtonItem",
        rendererType = "org.icefaces.component.MenuButtonItemRenderer",
        componentFamily = "org.icefaces.MenuButtonItem",
        tlddoc = "This component fires an actionListener from a menu button. " +
                "It has the same functionality of a regular jsf command menubutton "
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class MenuButtonItemMeta extends UICommandMeta {
      @Property(defaultValue = "false",
            tlddoc = "When singleSubmit is true, triggering an action on this component will submit" +
                    " and execute this component only. Equivalent to <f:ajax execute='@this' render='@all'>." +
                    " When singleSubmit is false, triggering an action on this component will submit and execute " +
                    " the full form that this component is contained within." +
                    " The default value is false.")
      private boolean singleSubmit;

        @Property(defaultValue = "false",
                tlddoc = "disabled property. If true no input may be submitted via this" +
                        "component.  Is required by aria specs")
        private boolean disabled;

        @Property(tlddoc = "tabindex of the component")
        private Integer tabindex;

        @Property(tlddoc="value of menuButtonItem")
        private String value;

        @Property(tlddoc="label of menutButtonItem")
        private String label;

        @Property(defaultValue="false", tlddoc="true if this item has been selected")
        private boolean selected;

        @Property(tlddoc = "style class of the component, rendered on the div root of the component")
        private String styleClass;

        @Property(tlddoc = "style of the component, rendered on the div root of the component")
        private String style;

        @Property(defaultValue = "false", tlddoc = "immediate as per jsf specs")
        private boolean immediate;

        @Property(tlddoc="id of panelConfirmation to be used with this component")
        private String panelConfirmation;

        @Property(tlddoc="id of blocking submitNotification panel which blocks any further access to page until process is complete")
        private String submitNofification;

}
