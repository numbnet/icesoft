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

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.el.MethodExpression;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

@Component(
        tagName         = "gMapMarker",
        componentClass  = "org.icefaces.ace.component.gmap.GMapMarker",
        rendererClass   = "org.icefaces.ace.component.gmap.GMapMarkerRenderer",
        generatedClass  = "org.icefaces.ace.component.gmap.GMapMarkerBase",
        extendsClass    = "javax.faces.component.UIPanel",
        componentType   = "org.icefaces.ace.component.GMapMarker",
        rendererType    = "org.icefaces.ace.component.GMapMarkerRenderer",
		componentFamily = "org.icefaces.ace.component"
        )

@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.css"),
	@ResourceDependency(library="icefaces.ace", name="util/ace-jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="util/ace-components.js")
})

public class GMapMarkerMeta extends UIPanelMeta {
    @Property(tlddoc="The longitude for the marker.")
    private String longitude;

    @Property(tlddoc="The latitude for the marker.")
    private String latitude;

    @Property(tlddoc="The animation that the marker should use. Valid values are bounce, drop, or none")
    private String animation;

    @Property(tlddoc="Additional options to be sent to the marker. Check google maps API for more specifics. Form is attribute:'value'", defaultValue=" ")
    private String options;
}
