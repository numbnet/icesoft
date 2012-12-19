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

package org.icefaces.component.fieldset;


import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;


@Component(
        tagName = "fieldsetRow",
        componentClass = "org.icefaces.component.fieldset.FieldSetRow",
        rendererClass = "org.icefaces.component.fieldset.FieldSetRowRenderer",
        generatedClass = "org.icefaces.component.fieldset.FieldSetRowBase",
        componentType = "org.icefaces.FieldSetRow",
        rendererType = "org.icefaces.FieldSetRowRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.FieldSetRow",
        tlddoc = "This mobility component is used within a fieldsetGroup tag to group unordered items."
)

public class FieldSetRowMeta extends UIComponentBaseMeta {
        	

    @Property(defaultValue="false",
    		  tlddoc = "true means the item is styled as a group header")
    private boolean group;

    @Property(tlddoc = "style will be rendered on the root element of this " +
    "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
        "this component.")
    private String styleClass;

}
