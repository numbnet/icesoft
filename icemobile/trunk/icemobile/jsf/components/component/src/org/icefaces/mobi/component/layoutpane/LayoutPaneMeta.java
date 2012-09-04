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
package org.icefaces.mobi.component.layoutpane;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
@Component(
        tagName = "layoutPane",
        componentClass = "org.icefaces.mobi.component.layoutpane.LayoutPane",
        rendererClass = "org.icefaces.mobi.component.layoutpane.LayoutPaneRenderer",
        generatedClass = "org.icefaces.mobi.component.layoutpane.LayoutPaneBase",
        componentType = "org.icefaces.LayoutPane",
        rendererType = "org.icefaces.LayoutPaneRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.LayoutPane",
        tlddoc = "This mobility component renders a div that can be defined for  " +
                "page layout.  It can be scrollable, have width or height. Can be used as a single" +
                " panel layout with facet of single, or if two, side by side, panels, then defined" +
                " as a left and right facet together. " +
                ". Eventually will also be resizable."
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class LayoutPaneMeta extends UIPanelMeta {
    @Property(defaultValue="false",
             tlddoc=" determines if the content of this panel is scrollable.")
    private boolean scrollable;

    @Property(defaultValue="-1",
            tlddoc=" valid css integer width % for left pane when left and right facet used This " +
            " option not used for single Facet as it is assumed 100% for that panel for width " +
            " For example a value of 30 will mean that the left facet gets 30% and the right facet 70%." +
            " If default Value is used, both panes will not be modified for width by this component")
    private int width;


    @Facets
    class FacetsMeta {
        @Facet
        UIComponent left;
        @Facet
        UIComponent right;
        @Facet
        UIComponent single;
    }

    @Property(tlddoc = "style will be rendered on a root element of this component")
    private String style;

 /*   @Property(defaultValue="none",
              tlddoc = " TODO resize needs to be further defined, but initially will be 2 adjacent panes " +
             "sharing a common border which can move.  May also do a collapse of one of the panes?" )
    private String resize; */

}
