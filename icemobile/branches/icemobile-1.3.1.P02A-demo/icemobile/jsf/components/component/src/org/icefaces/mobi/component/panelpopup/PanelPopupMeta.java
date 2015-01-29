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
package org.icefaces.mobi.component.panelpopup;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.mobi.utils.TLDConstants;

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
                "renders a confirmation panel to be used with any mobi commandButton or menuButton if" +
                "clientSide is false.  If clientSide is true, then use of the clientSide api is available. See " +
                "wiki docs for api for clientSide \"true\" or sample apps." +
                "A facet with name of label is available which show styled facet heading while allowing " +
                "developers to add images or other markup rather than simple text as does the headerText attribute.  " +
                "If a label facet is present, the headerText attribute is ignored. "
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class PanelPopupMeta extends UIPanelMeta {
    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;

    @Property(tlddoc = " The header text to be rendered on the popup. Header test will not display if a label " +
            "facet is specififed, but may be used instead of a label Facet." )
    private String headerText;

    @Property(defaultValue = "false", tlddoc = "Used when there is need to expose the javascript " +
            "api to the devloper. The contents of the popup will be always rendered so the developer " +
            "can  open and close the popup with appropriate javascript function call.  See wiki or examples apps.")
    private boolean clientSide;

    @Property(tlddoc = "Used to toggle visibility from the server. " +
            "Note that setting rendered=false on a visible modal dialog will not remove the modality layer, " +
            "visible \"false\"  must be set first (or client-side JS function called).", defaultValue = "false")
    private boolean visible;

    @Property(tlddoc = "When \"true\" the popup is allowed to become visible.)", defaultValue = "false")
    private boolean disabled;

    @Property(defaultValue = "true", tlddoc = "Invokes a calculations to be done to autoCenter the panel in the view.")
    private boolean autoCenter;

    @Property(tlddoc = "Width of the element in pixels. If not specified, will just be whatever contents determine. ", defaultValue = "Integer.MIN_VALUE")
    private int width;

    @Property(tlddoc = "Height of the element in pixels. If not specified, will just be whatever contents determine.", defaultValue = "Integer.MIN_VALUE")
    private int height;

 /*   @Property(defaultValue="true", tlddoc=" when true, the popup will center on it's surrounding form. WHen false, it will" +
            " center on the window.  Only used if autoCenter is true")
    private boolean centerOnForm;  */

    class FacetsMeta {
        @Facet
        UIComponent label;
    }

}
