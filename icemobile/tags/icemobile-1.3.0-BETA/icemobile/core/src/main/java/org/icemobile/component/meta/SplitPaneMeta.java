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
package org.icemobile.component.meta;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.annotation.JSP;
import org.icefaces.ace.meta.annotation.Property;
import org.icemobile.component.baseMeta.PanelMeta;

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
                " Eventually will also be resizable.")
@JSP(tagName                        = "splitPane",
     tagClass                       = "org.icemobile.jsp.tags.layout.SplitPaneTag",
     generatedTagClass              = "org.icemobile.jsp.tags.layout.SplitPaneBaseTag",
     generatedInterfaceClass        = "org.icemobile.component.ISplitPane",
     generatedInterfaceExtendsClass = "org.icemobile.component.IMobiComponent",
     tlddoc = "SplitPane must be used with 2 children that are " +
              "fragment.  The first will represent the left and the " +
              "second the right side, as only vertical split is currently " +
              "supported.  The user may determine the width of the first or " +
              "left column with an integer % value with the remainder " +
              "being assigned to the right column or inner div. both " +
              "inner children will be scrollable by default unless " +
              "scrollable attribute is set to false.")
@ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
public class SplitPaneMeta extends PanelMeta {
    @Property(defaultValue="true",
             tlddoc="Determines if the contents of this panel are both scrollable.")
    private boolean scrollable;

    @Property(defaultValue="25",
            tlddoc="An integer value representing the % of the total width assigned to the left pane.")
    private int columnDivider;

    @Facets
    class FacetsMeta {
        @Facet
        UIComponent left;
        @Facet
        UIComponent right;
    }

 /*   @Property(defaultValue="none",
              tlddoc = " TODO resize needs to be further defined, but initially will be 2 adjacent panes " +
             "sharing a common border which can move.  Will also provide a button to collapse left pane" )
    private String resizable;  */
}
