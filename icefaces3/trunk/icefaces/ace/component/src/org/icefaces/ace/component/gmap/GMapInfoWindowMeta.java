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

package org.icefaces.ace.component.gmap;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName         = "gMapInfoWindow",
        componentClass  = "org.icefaces.ace.component.gmap.GMapInfoWindow",
        rendererClass   = "org.icefaces.ace.component.gmap.GMapInfoWindowRenderer",
        generatedClass  = "org.icefaces.ace.component.gmap.GMapInfoWindowBase",
        extendsClass    = "javax.faces.component.UIPanel",
        componentType   = "org.icefaces.ace.component.GMapInfoWindow",
        rendererType    = "org.icefaces.ace.component.GMapInfoWindowRenderer",
		componentFamily = "org.icefaces.ace.component",
        tlddoc = "ace:gMapInfoWindow creates a pop-up window within the parent ace:gMap, which can be used to display either text via the 'content' attribute or " +
                "nested html or jsf tags. If placed within an ace:gMapMarker, the window will 'bind' to the marker, and move along with it."
        )

@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.css"),
	@ResourceDependency(library="icefaces.ace", name="util/ace-jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="util/ace-components.js")
})

public class GMapInfoWindowMeta extends UIPanelMeta {
    @Property(tlddoc="The longitude for the window, will be overridden if a child of a marker.")
    private String longitude;

    @Property(tlddoc="The latitude for the marker, will be overridden if a child of a marker.")
    private String latitude;

    @Property(tlddoc="The text content to be displayed within the info window. " +
            "If this component has child tags(either html or another jsf component), this attribute will not be used, placing the children within the window instead.")
    private String content;

    @Property(tlddoc="Additional options to be sent to the window. Check google maps API for more specifics at " +
            "https://developers.google.com/maps/documentation/javascript/reference#InfoWindowOptions. Form is attribute:'value'.", defaultValue="none")
    private String options;
}
