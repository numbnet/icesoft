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


@Component(
        tagName = "fieldsetGroup",
        componentClass = "org.icefaces.component.fieldset.FieldSetGroup",
        rendererClass = "org.icefaces.component.fieldset.FieldSetGroupRenderer",
        generatedClass = "org.icefaces.component.fieldset.FieldSetGroupBase",
        componentType = "org.icefaces.FieldSetGroup",
        rendererType = "org.icefaces.FieldSetGroupRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.FieldSetGroup",
        tlddoc = "This mobility component " +
                "represents a grouping of unordered list items.  The child component "+
                "of this component should only be fieldsetRow. "
)

public class FieldSetGroupMeta extends UIComponentBaseMeta {
	
    @Property(defaultValue="true",
    		  tlddoc = "true enables padding around list group")
    private boolean inset;
 
    @Property(tlddoc = "style will be rendered on the root element of this " +
    "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
        "this component.")
    private String styleClass;

}
