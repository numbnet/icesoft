/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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
import org.icefaces.ace.meta.baseMeta.UISeriesBaseMeta;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.model.SelectItem;
import javax.el.MethodExpression;
import java.util.List;

@ClientBehaviorHolder(events = {
	@ClientEvent(name="click", javadoc="Fired when a menubutton is clicked",
            tlddoc="Fired when commandButton is clicked", defaultRender="@all",
            defaultExecute="@all")}, defaultEvent="close")
@Component(
        tagName = "menuButton",
        componentClass = "org.icefaces.mobi.component.menubutton.MenuButton",
        rendererClass = "org.icefaces.mobi.component.menubutton.MenuButtonRenderer",
        generatedClass = "org.icefaces.mobi.component.menubutton.MenuButtonBase",
        extendsClass = "org.icefaces.impl.component.UISeriesBase",
        componentType = "org.icefaces.component.MenuButton",
        rendererType = "org.icefaces.component.MenuButtonRenderer",
        componentFamily = "org.icefaces.MenuButton",
        tlddoc = "This component renders a select menu button with a collection of menuButtonItems " +
                "upon selection of a menuButtonItem, an actionListener will be queued. Children may" +
                " only be menuButtonItem, and a collection may be specified by the value attribute " +
                "with the var attribute as per UISeries implementation.  Otherwise, several " +
                "menuButtonItem children can be designated as children of this component."
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class MenuButtonMeta extends UISeriesBaseMeta{

    @Property(tlddoc = "style will be rendered on the root element of this " +
            "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
            "this component.")
    private String styleClass;

    @Property(defaultValue = "false",
            tlddoc = "disabled property. If true no input may be submitted via this" +
                    "component.  Is required by aria specs")
    private boolean disabled;

    @Property(defaultValue="Menu", tlddoc="Label of the menu button")
    private String buttonLabel;

    @Property(defaultValue="Select", tlddoc="first item in list which cannot be selected but helpful to users " +
            " to understand how to use the component and they must select an option in the list.")
    private String selectTitle;

    @Property(defaultValue = "false", tlddoc = "The default value of this attribute is false. If true then value change event will happen in APPLY_REQUEST_VALUES phase and if the value of this attribute is false then event change will happen in INVOKE_APPLICATION phase")
    private boolean immediate;
    /** other possible attributes include vertical, scroll increment, circular, numbershown, currentIndex */
}
