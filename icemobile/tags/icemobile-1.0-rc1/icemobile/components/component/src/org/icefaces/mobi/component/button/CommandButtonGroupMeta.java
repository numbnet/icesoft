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

package org.icefaces.mobi.component.button;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "commandButtonGroup",
        componentClass = "org.icefaces.mobi.component.button.CommandButtonGroup",
        rendererClass = "org.icefaces.mobi.component.button.CommandButtonGroupRenderer",
        generatedClass = "org.icefaces.mobi.component.button.CommandButtonGroupBase",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.component.CommandButtonGroup",
        rendererType = "org.icefaces.component.CommandButtonGroupRenderer",
        componentFamily = "org.icefaces.CommandButtonGroup",
        tlddoc = "This mobile component allows the grouping of mobile command " +
                "buttons.  The grouping can be either in the horizontal or " +
                "vertical plain.  The 'selected' attribute on a command button " +
                "can but used to set the selected button in the group."
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class CommandButtonGroupMeta extends UIComponentBaseMeta {

    @Property(tlddoc = "style class of the component, rendered on the div root of the component")
    private String styleClass;

    @Property(tlddoc = "style of the component, rendered on the div root of the component")
    private String style;

    @Property(defaultValue = "horizontal",
            tlddoc = "Change the layout orientation of the button group child to either horizontal or vertical")
    private String orientation;


}
