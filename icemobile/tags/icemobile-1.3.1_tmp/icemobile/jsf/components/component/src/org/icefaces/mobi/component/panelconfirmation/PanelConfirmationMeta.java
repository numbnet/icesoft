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
package org.icefaces.mobi.component.panelconfirmation;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Field;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.mobi.utils.TLDConstants;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "panelConfirmation",
        componentClass = "org.icefaces.mobi.component.panelconfirmation.PanelConfirmation",
        rendererClass = "org.icefaces.mobi.component.panelconfirmation.PanelConfirmationRenderer",
        generatedClass = "org.icefaces.mobi.component.panelconfirmation.PanelConfirmationBase",
        componentType = "org.icefaces.PanelConfirmation",
        rendererType = "org.icefaces.PanelConfirmationRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.PanelConfirmation",
        tlddoc = "This mobility component renders a confirmation panel to be used with any mobi " +
                "commandButton or menuButton component.  See commandButton and menuButton docs " +
                "for the correct attribute to bind this component to the button components. " +
                "This component was modelled after 1.8 panelConfirmation and disabled feature was" +
                "not part of the functionality of that component so not carried over to this one. "
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class PanelConfirmationMeta extends UIPanelMeta {

    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    /* not part of the 1.8 component that this was modelled after Can just not have a valid panelconfirmation id on the button*/
 /*   @Property(tlddoc = TLDConstants.DISABLED)
    private boolean disabled; */

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;

    @Property(defaultValue = "Confirm", tlddoc = "The title of the confirmation panel in an attribute " +
            "to allow internationalization of this component.")
    private String title;

    @Property(tlddoc = " Three distinct types are allowed - acceptOnly, cancelOnly and both. Type acceptOnly means only accept button will " +
            "appear on the confirmation panel popup. The type of " +
            "cancelOnly means only a cancel button appears, and type of both means the confirmation dialog " +
            "will contain both the accept and cancel button.")
    private String type;

    @Property(defaultValue = "Cancel", tlddoc = "The label for the cancel button.")
    private String cancelLabel;

    @Property(defaultValue = "Confirm", tlddoc = "The label for the confirm/accept button.")
    private String acceptLabel;

    @Property(defaultValue = "Confirm?", tlddoc = "The message to appear on the confirmation diablog.")
    private String message;

}
