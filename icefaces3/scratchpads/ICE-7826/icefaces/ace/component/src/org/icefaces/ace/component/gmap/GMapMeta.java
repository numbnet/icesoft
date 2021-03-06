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
        tagName         = "gMap",
        componentClass  = "org.icefaces.ace.component.gmap.GMap",
        rendererClass   = "org.icefaces.ace.component.gmap.GMapRenderer",
        generatedClass  = "org.icefaces.ace.component.gmap.GMapBase",
        extendsClass    = "javax.faces.component.UIPanel",
        componentType   = "org.icefaces.ace.component.GMap",
        rendererType    = "org.icefaces.ace.component.GMapRenderer",
		componentFamily = "org.icefaces.ace.component",
		tlddoc = "A version 3.0 API google map interface."
        )

@ResourceDependencies({
	@ResourceDependency(name = "icefaces.ace/gmap/api.js"),
	@ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.css"),
	@ResourceDependency(library="icefaces.ace", name="util/ace-jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="util/ace-components.js")
})

public class GMapMeta extends UIPanelMeta {

	@Property(tlddoc="The starting longitude for the map. Will be overridden if an address is provided.", defaultValue="-114.08538937568665")
	private String longitude;
	
	@Property(tlddoc="The starting latitude for the map. Will be overridden if an address is provided.", defaultValue="51.06757388616548")
	private String latitude;
	
	@Property(tlddoc="Starting zoom of the map element.", defaultValue="5")
	private int zoomLevel;
	
	@Property(tlddoc="Additional options to be sent to the map. Check google maps API for more specifics. Form is attribute:'value'")
	private String options;
	
	@Property(tlddoc="Whether the map should be locating the specified address. Default is false.", defaultValue="false")
	private boolean locateAddress;
	
	@Property(tlddoc="Specifies whether the map has been initialized or not.", defaultValue="false")
	private boolean intialized;
	
	@Property(tlddoc="Address to locate.")
	private String address;
	
	@Property(tlddoc="Map type to display by default. Possible values are HYBRID, ROADMAP, SATELLITE and TERRAIN, case insensitive", defaultValue="ROADMAP")
	private String type;
}
