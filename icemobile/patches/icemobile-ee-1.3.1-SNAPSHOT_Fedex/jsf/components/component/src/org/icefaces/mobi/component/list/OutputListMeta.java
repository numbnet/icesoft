/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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
        tlddoc = "outputList defines a grouping of unordered list items.  The child component "+
                "of this component must be outputListItem components. "
)


@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class OutputListMeta extends UIComponentBaseMeta {
	
    @Property(defaultValue="false",
    		  tlddoc = "Determines if inset padding is applied around list group.")
    private boolean inset;

     @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLE)
     private String style;

     @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLECLASS)
     private String styleClass;
     
     @Property(tlddoc = "JavaScript to be evaluated when the browser goes online. Two arguments are passed through to the context: the online or offline 'event' argument, and the 'elem' argument for the root element of the component. ")
     private String ononline;
     
     @Property(tlddoc = "JavaScript to be evaluated when the browser goes offline. Two arguments are passed through to the context: the online or offline 'event' argument, and the 'elem' argument for the root element of the component.")
     private String onoffline;

}
