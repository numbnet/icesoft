package org.icefaces.ace.component.gmap;
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

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;

import org.icefaces.ace.component.ajax.AjaxBehavior;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;

@MandatoryResourceComponent(tagName="gMap", value="org.icefaces.ace.component.gmap.GMap")
public class GMapMarkerRenderer extends CoreRenderer {


	    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
			ResponseWriter writer = context.getResponseWriter();
			GMapMarker marker = (GMapMarker) component;
			String clientId = marker.getClientId(context);
			writer.startElement("span", null);
			writer.writeAttribute("id", clientId + "_marker", null);
			writer.startElement("script", null);
			writer.writeAttribute("type", "text/javascript", null);
			writer.write("ice.ace.jq(function() {");
			String oldLatitude = "";
			String oldLongitude = "";
			String currentLat = marker.getLatitude();
			String currentLon = marker.getLongitude();
			//create a marker if lat and lon defined on the component itself
			if (currentLat != null &&  currentLon != null
					&& currentLat.length() > 0 && currentLon.length() > 0) {
				if (!currentLat.equals(oldLatitude) ||
						!currentLon.equals(oldLongitude)) {
					//to dynamic support first to remove if any
					writer.write("ice.ace.gMap.removeMarker('"+ marker.getParent().getClientId(context)+"', '"+ clientId +"');");
					if (marker.getOptions() != null)
						writer.write("ice.ace.gMap.addMarker('" + marker.getParent().getClientId(context) + "', '" + clientId +
							"', '"+ currentLat + "', '" + currentLon + "', \"" + marker.getOptions() + "\");");
					else
						writer.write("ice.ace.gMap.addMarker('" + marker.getParent().getClientId(context) + "', '" + clientId +
							"', '"+ currentLat + "', '" + currentLon + "');");
				}
				oldLatitude = currentLat;
				oldLongitude = currentLon;
			}
			writer.write("});");
			writer.endElement("script");
			writer.endElement("span");
	}
}