/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icefaces.mobi.component.menubutton;
import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UICommandMeta;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;
import org.icefaces.mobi.utils.TLDConstants;

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
                "It has the same functionality of a regular JSF UICommand. It may " +
                "have a panelConfirmation and/or a submitNotification component attribute specified " +
                "to allow the developer to confirm a submit and to disable the page showing" +
                "the disable while the submit is happening."
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class MenuButtonItemMeta extends UICommandMeta {
    @Property(defaultValue = "false",
            tlddoc = TLDConstants.SINGLESUBMIT)
    private boolean singleSubmit;

    @Property(defaultValue = "false",
                tlddoc = TLDConstants.DISABLED)
    private boolean disabled;
 /*
      @Property(tlddoc = TLDConstants.TABINDEX)
      private Integer tabindex; */

    @Property(tlddoc="The value of menuButtonItem to be submitted to Server.")
    private String value;

    @Property(tlddoc="The label of menutButtonItem in the select list.")
    private String label;

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;

    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(defaultValue = "false", tlddoc = TLDConstants.IMMEDIATE_INPUT)
    private boolean immediate;

    @Property(tlddoc="The id of a panelConfirmation component in the same view to be used with this component." +
                "In order to find the panelConfirmation component referenced by this id, it is best to have the " +
                "panelConfirmation component and menuButton in the same form or naming container.")
    private String panelConfirmation;

    @Property(tlddoc="The id of blocking submitNotification panel which blocks any further access to page " +
                "until process is complete.  In order to successfully find the submitNotification component " +
                "referenced by this id, it is advisable to have the submitNotification component and the " +
                "menuButton in the same form or naming container.")
    private String submitNotification;

    @Field
    private String name;

    @Field
    private String submitNotificationId;

    @Field
    private String panelConfirmationId;

    @Field
    private String behaviors;
}
