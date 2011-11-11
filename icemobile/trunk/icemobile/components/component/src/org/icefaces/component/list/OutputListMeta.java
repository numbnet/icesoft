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

package org.icefaces.component.list;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIDataMeta;


@Component(
        tagName = "outputList",
        componentClass = "org.icefaces.component.list.OutputList",
        rendererClass = "org.icefaces.component.list.OutputListRenderer",
        generatedClass = "org.icefaces.component.list.OutputListBase",
        componentType = "org.icefaces.OutputList",
        rendererType = "org.icefaces.OutputListRenderer",
        extendsClass = "javax.faces.component.UIData",
        componentFamily = "org.icefaces.OutputList",
        tlddoc = "This mobility component " +
                "represents a grouping of unordered list items.  The child component "+
                "of this component should only be outputListItem. "
)


public class OutputListMeta extends UIDataMeta {
	
    @Property(defaultValue="false",
    		  tlddoc = "true enables padding around list group")
    private boolean inset;

    @Property(defaultValue="default", tlddoc = "type is default or thumb  " )
    private String listType;

    @Property(tlddoc = "style will be rendered on the root element of this " +
    "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
        "this component.")
    private String styleClass;

    @Property(defaultValue="default", tlddoc = "This attribute is only required when a list is used in the var attribute." +
            " Otherwise if using the OutputListItem component, use the type attribute then.  Itemtype is default or thumb  " )
    private String itemType;

    @Property(tlddoc = "style class will be rendered on each list item for " +
        "this component.")
    private String itemStyleClass;
}
