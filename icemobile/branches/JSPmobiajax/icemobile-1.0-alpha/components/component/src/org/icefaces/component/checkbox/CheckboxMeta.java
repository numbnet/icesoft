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

package org.icefaces.component.checkbox;


import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.baseMeta.UISelectBooleanMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "checkbox",
        componentClass = "org.icefaces.component.checkbox.Checkbox",
        rendererClass = "org.icefaces.component.checkbox.CheckboxRenderer",
        componentType = "org.icefaces.Checkbox",
        rendererType = "org.icefaces.CheckboxRenderer",
        extendsClass = "javax.faces.component.UISelectBoolean",
        generatedClass = "org.icefaces.component.checkbox.CheckboxBase",
        componentFamily = "org.icefaces.Checkbox",
        tlddoc = "This component allows entry of a button which " +
                "supports browsers that see checkbox as true or false, " +
                "yes or no, on or off.  LabelPosition property allows label " +
                "to be placed on the button-in case of sam style, or to the left " +
                "of the button - in the case of rime style."
)

@ResourceDependencies({
        @ResourceDependency(name = "checkboxbutton.js", library = "org.icefaces.component.checkbox"),
        @ResourceDependency(name = "checkboxbutton.css", library = "org.icefaces.component.checkbox")
})

public class CheckboxMeta extends UISelectBooleanMeta {

    @Property(tlddoc = "A label to be printed either on the buttton or to the left of it " +
            " according to labelPosition parameter")
    private String label;

/*    @Property(defaultValue="left",
     tlddoc="Default is left for rime theme. Other possibility is \"on\" " +
             "for sam skin.")
private String labelPosition; */

    @Property(defaultValue = "false",
            tlddoc = "When singleSubmit is true, changing the value of this component" +
                    " will submit and execute this component only. Equivalent to " +
                    " execute=\"@this\" render=\"@all\" of the f ajax tag. " +
                    "When singleSubmit is false, no submit occurs. " +
                    "The default value is false.")
    private boolean singleSubmit;

    @Property(tlddoc = "style of the component, rendered on the root div of the component")
    private String style;

    @Property(tlddoc = "style class of the component, rendered on the root div of the component.")
    private String styleClass;

    @Property(defaultValue = "0", tlddoc = "tabindex of the component")
    private int tabindex;

    @Property(defaultValue = "false",
            tlddoc = "disabled property. If true no input may be submitted via this" +
                    " component.  Is required by aria")
    private boolean disabled;
}
