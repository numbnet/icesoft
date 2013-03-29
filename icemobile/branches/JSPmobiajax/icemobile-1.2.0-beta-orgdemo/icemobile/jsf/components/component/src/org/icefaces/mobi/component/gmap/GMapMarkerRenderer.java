package org.icefaces.mobi.component.gmap;
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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.icefaces.render.MandatoryResourceComponent;

import org.icefaces.mobi.renderkit.CoreRenderer;

@MandatoryResourceComponent(tagName="gMap", value="org.icefaces.mobi.component.gmap.GMap")
public class GMapMarkerRenderer extends CoreRenderer {

	    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
			ResponseWriter writer = context.getResponseWriter();
			GMapMarker marker = (GMapMarker) component;
			String clientId = marker.getClientId(context);
			writer.startElement("span", null);
			writer.writeAttribute("id", clientId + "_marker", null);
			writer.startElement("script", null);
			writer.writeAttribute("type", "text/javascript", null);
			String currentLat = marker.getLatitude();
			String currentLon = marker.getLongitude();
			//create a marker if lat and lon defined on the component itself
			if (currentLat != null &&  currentLon != null
					&& currentLat.length() > 0 && currentLon.length() > 0) {
				//to dynamic support first to remove if any
				StringBuilder sb = new StringBuilder();
				sb.append("var map = mobi.gmap.repo['"+ marker.getParent().getClientId(context) + "'];");
				sb.append("var marker = mobi.gmap.markers['"+clientId+"'];");
				sb.append("if( marker ){");
				sb.append(   "marker.setMap(null);");
				sb.append(   "delete mobi.gmap.markers['"+clientId+"'];");
				sb.append("}");
				sb.append("var ll = new google.maps.LatLng("+currentLat+","+currentLon+");");
				sb.append("marker = new google.maps.Marker({position: ll, map: map});");
				sb.append("mobi.gmap.markers['"+clientId+"'] = marker;");
				writer.write(sb.toString());
			}
			writer.endElement("script");
			writer.endElement("span");
	}
}