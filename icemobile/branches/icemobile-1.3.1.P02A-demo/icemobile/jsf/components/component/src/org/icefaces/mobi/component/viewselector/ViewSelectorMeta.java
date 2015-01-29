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
package org.icefaces.mobi.component.viewselector;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.annotation.Property;

import javax.faces.component.UIComponent;

@Component(
        tagName = "viewSelector",
        componentClass = "org.icefaces.mobi.component.viewselector.ViewSelector",
        rendererClass = "org.icefaces.mobi.component.viewselector.ViewSelectorRenderer",
        generatedClass = "org.icefaces.mobi.component.viewselector.ViewSelectorBase",
        handlerClass = "org.icefaces.mobi.component.viewselector.ViewSelectorHandler",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.ViewSelector",
        rendererType = "org.icefaces.ViewSelectorRenderer",
        componentFamily = "org.icefaces.ViewSelector",
        tlddoc = "View select allows a developer to specify content for a small" +
                "view or a large view.  The component will render the respective " +
                "content depending on which view was detected."
)

public class ViewSelectorMeta {

    @Property(tlddoc = "Default view to load if an appropriate view can not be determined" +
            "by the component. ")
    private String defaultView;

    /**
     * Defines which child component to load for  respective view once detected.
     */
    @Facets
    class FacetsMeta {
        @Facet
        UIComponent small;
        @Facet
        UIComponent large;
    }
}
