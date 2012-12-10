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
package org.icefaces.mobi.component.splitpane;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;

@Component(
        tagName = "splitPane",
        componentClass = "org.icefaces.mobi.component.splitpane.SplitPane",
        rendererClass = "org.icefaces.mobi.component.splitpane.SplitPaneRenderer",
        generatedClass = "org.icefaces.mobi.component.splitpane.SplitPaneBase",
        componentType = "org.icefaces.SplitPane",
        rendererType = "org.icefaces.SplitPaneRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.SplitPane",
        tlddoc = "splitPane renders a div with two children that can be defined for  " +
                "page layout.  It can be scrollable, have columnDivider at certain location. " +
                " Requires a left and right facet for two, side by side, panels." +
                " Eventually will also be resizable."
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class SplitPaneMeta extends UIPanelMeta {
    @Property(defaultValue="true",
             tlddoc="Determines if the content of this panel is scrollable.")
    private boolean scrollable;

    @Property(defaultValue="25",
            tlddoc="The % of the total width assigned to the left pane.")
    private int columnDivider;

    @Facets
    class FacetsMeta {
        @Facet
        UIComponent left;
        @Facet
        UIComponent right;
    }

    @Property(tlddoc = "Style class of the container element.")
    private String styleClass;

    @Property(tlddoc = "style will be rendered on a root element of this component")
    private String style;

 /*   @Property(defaultValue="none",
              tlddoc = " TODO resize needs to be further defined, but initially will be 2 adjacent panes " +
             "sharing a common border which can move.  May also do a collapse of one of the panes?" )
    private String resizable;  */

}
