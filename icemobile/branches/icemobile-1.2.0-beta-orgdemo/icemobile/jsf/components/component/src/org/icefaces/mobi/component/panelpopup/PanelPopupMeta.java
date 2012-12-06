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
package org.icefaces.mobi.component.panelpopup;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;

@Component(
        tagName = "panelPopup",
        componentClass = "org.icefaces.mobi.component.panelpopup.PanelPopup",
        rendererClass = "org.icefaces.mobi.component.panelpopup.PanelPopupRenderer",
        generatedClass = "org.icefaces.mobi.component.panelpopup.PanelPopupBase",
        componentType = "org.icefaces.PanelPopup",
        rendererType = "org.icefaces.PanelPopupRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.PanelPopup",
        tlddoc = "This mobility component " +
                "renders a confirmation panel to be used with any mobi commandButton or menuButton"
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class PanelPopupMeta extends UIPanelMeta {
    @Property(tlddoc = "style will be rendered on a root element of this component")
    private String style;

    @Property(tlddoc = "style class will be rendered on a root element of this component")
    private String styleClass;

    @Property(tlddoc = "Header text. Will not display if a label facet is used. But may be used instead" +
            " of a label facet")
    private String headerText;

    @Property(defaultValue = "false", tlddoc = "means there is need to expose the js api for devloper. " +
            " to access the ability to open and close the popup")
    private boolean clientSide;

    @Property(tlddoc = "visible can be used to toggle visibility on the server, rendered should not be used that way, setting rendered=false on a visible modal dialog will not remove the modality layer, visible=false must be set first (or client-side JS function called)", defaultValue = "false")
    private boolean visible;

    @Property(tlddoc = "if ture, does not allow the popup to be visible)", defaultValue = "false")
    private boolean disabled;

    @Property(defaultValue = "true", tlddoc = "autoCenter of panel")
    private boolean autoCenter;

    @Property(tlddoc = "Width of the element in pixels. Default (not specified or value <= 0) is auto. ", defaultValue = "Integer.MIN_VALUE")
    private int width;

    @Property(tlddoc = "Height of the element in pixels. Default (not specified or value <= 0) is auto.", defaultValue = "Integer.MIN_VALUE")
    private int height;

 /*   @Property(defaultValue="true", tlddoc=" when true, the popup will center on it's surrounding form. WHen false, it will" +
            " center on the window.  Only used if autoCenter is true")
    private boolean centerOnForm;  */
    @Facets
    class FacetsMeta {
        @Facet
        UIComponent label;
    }

}
