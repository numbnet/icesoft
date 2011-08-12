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
import org.icefaces.component.baseMeta.UICommandMeta;

@Component(
        tagName = "commandButton",
        componentClass = "org.icefaces.component.button.CommandButton",
        rendererClass = "org.icefaces.component.button.CommandButtonRenderer",
        generatedClass = "org.icefaces.component.button.CommandButtonBase",
        extendsClass = "javax.faces.component.UICommand",
        componentType = "org.icefaces.component.CommandButton",
        rendererType = "org.icefaces.component.CommandButtonRenderer",
        componentFamily = "org.icefaces.CommandButton",
        tlddoc = "This component allows entry of a complete form or just itself. " +
                "It has athe same functionality of a regular jsf command button " +
                "but without having to add extra attributes other than determining singleSubmit " +
                "to be true or false"
)


public class CommandButtonMeta extends UICommandMeta {


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

    @Property(tlddoc = "style class of the component, rendered on the div root of the component")
    private String styleClass;

    @Property(tlddoc = "style of the component, rendered on the div root of the component")
    private String style;

    @Property(defaultValue = "default", tlddoc = "Four types of buttons are allowed default, important, back and attention")
    private String type;
}
