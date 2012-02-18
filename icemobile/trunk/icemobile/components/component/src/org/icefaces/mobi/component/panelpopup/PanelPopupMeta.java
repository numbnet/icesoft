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
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import javax.faces.component.UIComponent;

@Component(
        tagName = "panelPopup",
        componentClass = "org.icefaces.mobi.component.panelpopup.PanelPopup",
        rendererClass = "org.icefaces.mobi.component.panelpopup.PanelPopupRenderer",
        generatedClass = "org.icefaces.mobi.component.panelpopup.PanelPopupBase",
        componentType = "org.icefaces.PanelPopup",
        rendererType = "org.icefaces.PanelPopupRenderer",
        extendsClass    = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.PanelPopup",
        tlddoc = "This mobility component " +
                "renders a confirmation panel to be used with any mobi commandButton or menuButton"
)
public class PanelPopupMeta extends UIPanelMeta {
    @Property(tlddoc = "style will be rendered on a root element of this component")
    private String style;

    @Property(tlddoc = "style class will be rendered on a root element of this component")
    private String styleClass;

 	@Property(tlddoc="Header text")
	private String header;

    @Property(defaultValue="false", tlddoc="markup is rendered and only displayed when visible is true.  Otherwise, markup is only rendered when visible if false")
    private boolean clientSide;

 	@Property(tlddoc="visible can be used to toggle visibility on the server, rendered should not be used that way, setting rendered=false on a visible modal dialog will not remove the modality layer, visible=false must be set first (or client-side JS function called)", defaultValue="false")
	private boolean visible;

    @Property(tlddoc="Makes the dialog resizable. Should be false if width or height is auto, or resizing may hehave erratically.", defaultValue="true")
	private boolean resizable;

    @Property(defaultValue="true", tlddoc="autocenter of panel")
    private boolean autoCenter;

    @Property(defaultValue="open", tlddoc="text for client Side open button, so no server round trips")
    private String openButtonLabel;

	@Property(tlddoc="Width of the element in pixels. Default (not specified or value <= 0) is auto. If auto, resizable should be false, or resizing may hehave erratically. If auto, IE7 may not size or position properly.", defaultValue="Integer.MIN_VALUE")
	private int width;

	@Property(tlddoc="Height of the element in pixels. Default (not specified or value <= 0) is auto. If auto, resizable should be false, or resizing may hehave erratically. If auto, IE7 may not size or position properly.", defaultValue="Integer.MIN_VALUE")
	private int height;
/**
	@Property(tlddoc="zindex property to control overlapping with other elements", defaultValue="1000")
	private int zindex;  **/

	@Property(tlddoc="Minimum width of a resizable dialog", defaultValue="150")
	private int minWidth;

	@Property(tlddoc="Minimum height of resizable dialog", defaultValue="0")
	private int minHeight;

    @Facets
    class FacetsMeta{
        @Facet
        UIComponent label;
    }

}
