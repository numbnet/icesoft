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
import org.icefaces.ace.meta.baseMeta.UIDataMeta;
import org.icefaces.ace.meta.baseMeta.UISeriesBaseMeta;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "outputListItems",
        componentClass = "org.icefaces.mobi.component.list.OutputListItems",
        rendererClass = "org.icefaces.mobi.component.list.OutputListItemsRenderer",
        generatedClass = "org.icefaces.mobi.component.list.OutputListItemsBase",
        componentType = "org.icefaces.OutputListItems",
        rendererType = "org.icefaces.OutputListItemsRenderer",
        extendsClass = "org.icefaces.impl.component.UISeriesBase",
        componentFamily = "org.icefaces.OutputListItems",
        tlddoc = "This mobility component is used within an outputlist tag to group lists of items."
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class OutputListItemsMeta extends UISeriesBaseMeta {

    @Property(defaultValue="false",
    		  tlddoc = "true enables padding around list group")
    private boolean inset;

    @Property(defaultValue="default", tlddoc = " default or thumb  " )
    private String type;

    @Property(tlddoc = "style will be rendered on the root element of this " +
     "component.")
     private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
        "this component.")
    private String styleClass;

}

