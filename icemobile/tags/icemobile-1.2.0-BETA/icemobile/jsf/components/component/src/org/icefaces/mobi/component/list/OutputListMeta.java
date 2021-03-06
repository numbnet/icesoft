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

package org.icefaces.mobi.component.list;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


@Component(
        tagName = "outputList",
        componentClass = "org.icefaces.mobi.component.list.OutputList",
        rendererClass = "org.icefaces.mobi.component.list.OutputListRenderer",
        generatedClass = "org.icefaces.mobi.component.list.OutputListBase",
        componentType = "org.icefaces.OutputList",
        rendererType = "org.icefaces.OutputListRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.OutputList",
        tlddoc = "This mobility component " +
                "represents a grouping of unordered list items.  The child component "+
                "of this component should only be outputListItem. "
)


@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class OutputListMeta extends UIComponentBaseMeta {
	
    @Property(defaultValue="false",
    		  tlddoc = "true enables padding around list group")
    private boolean inset;

    @Property(tlddoc = "style will be rendered on the root element of this " +
    "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
        "this component.")
    private String styleClass;

}
