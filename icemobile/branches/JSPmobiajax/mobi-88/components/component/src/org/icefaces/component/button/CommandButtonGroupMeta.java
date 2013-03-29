/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icefaces.component.button;


import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.baseMeta.UIComponentBaseMeta;

@Component(
        tagName = "commandButtonGroup",
        componentClass = "org.icefaces.component.button.CommandButtonGroup",
        rendererClass = "org.icefaces.component.button.CommandButtonGroupRenderer",
        generatedClass = "org.icefaces.component.button.CommandButtonGroupBase",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.component.CommandButtonGroup",
        rendererType = "org.icefaces.component.CommandButtonGroupRenderer",
        componentFamily = "org.icefaces.CommandButtonGroup",
        tlddoc = "This mobile component allows the grouping of mobile command " +
                "buttons.  The grouping can be either in the horizontal or " +
                "vertical plain.  The 'selected' attribute on a command button " +
                "can but used to set the selected button in the group."
)

public class CommandButtonGroupMeta extends UIComponentBaseMeta {

    @Property(tlddoc = "style class of the component, rendered on the div root of the component")
    private String styleClass;

    @Property(tlddoc = "style of the component, rendered on the div root of the component")
    private String style;

    @Property(defaultValue = "horizontal",
            tlddoc = "Change the layout orientation of the button group child to either horizontal or vertical")
    private String orientation;


}
