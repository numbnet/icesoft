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

package org.icefaces.mobi.component.button;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Field;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.mobi.utils.TLDConstants;

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
        tlddoc = "This component allows the grouping of commandButtons. The buttons "
           + "can be grouped horizontally or vertically."
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class CommandButtonGroupMeta extends UIComponentBaseMeta {

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;

    @Property(defaultValue = "false", tlddoc = "When the commandButtonGroup is disabled, the user can " +
            "view the button group and see which button is selected, but cannot make any changes to the " +
            "button group by selecting another in the group.  Similar to a readonly situation.")
    private boolean disabled;

    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(defaultValue = "horizontal",
            tlddoc = "The layout orientation of the button group, \"vertical\" or \"horizontal\". ")
    private String orientation;

    @Field
    private String selectedId;

    @Field
    private String name;
}
