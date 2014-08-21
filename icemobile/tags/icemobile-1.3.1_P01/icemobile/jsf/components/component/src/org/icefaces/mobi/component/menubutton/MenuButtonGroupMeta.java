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


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.mobi.utils.TLDConstants;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


@Component(
        tagName = "menuButtonGroup",
        componentClass = "org.icefaces.mobi.component.menubutton.MenuButtonGroup",
        rendererClass = "org.icefaces.mobi.component.menubutton.MenuButtonGroupRenderer",
        generatedClass = "org.icefaces.mobi.component.menubutton.MenuButtonGroupBase",
        componentType = "org.icefaces.MenuButtonGroup",
        rendererType = "org.icefaces.MenuButtonGroupRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.MenuButtonGroup",
        tlddoc = "This component renders an optgroup element to order a collection of menuButtonItems as " +
                "children.This component cannot be selected but may be disabled which " +
                "disabled all its children from being selected. Parent must be a MenuButton component. "
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class MenuButtonGroupMeta extends UIComponentBaseMeta {

    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(defaultValue = "false",
            tlddoc = TLDConstants.DISABLED)
    private boolean disabled;

    @Property( tlddoc="The label on the menu option group to categorize menuButtonItems")
    private String label;

}
