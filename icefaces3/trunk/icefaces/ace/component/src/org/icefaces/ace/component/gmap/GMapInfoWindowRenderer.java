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

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@MandatoryResourceComponent(tagName="gMap", value="org.icefaces.ace.component.gmap.GMap")
public class GMapInfoWindowRenderer extends CoreRenderer {


	    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
			ResponseWriter writer = context.getResponseWriter();
			GMapInfoWindow infoWindow = (GMapInfoWindow) component;
			String clientId = infoWindow.getClientId(context);
			String mapId;
            String markerId="none";
            writer.startElement("span", null);
			writer.writeAttribute("id", clientId + "_marker", null);
			writer.startElement("script", null);
			writer.writeAttribute("type", "text/javascript", null);
			writer.write("ice.ace.jq(function() {");

            if("GMapMarker".equals(infoWindow.getParent().getClass().getSimpleName()))
            {
                markerId=infoWindow.getParent().getClientId(context);
                mapId=infoWindow.getParent().getParent().getClientId(context);
            }
            else
                mapId=infoWindow.getParent().getClientId(context);
            if(infoWindow.getChildCount() == 0)
            writer.write("ice.ace.gMap.addGWindow('"+ mapId +"', '"+ clientId +"','" + infoWindow.getContent() + "', new google.maps.LatLng("+ infoWindow.getLatitude() +
                    "," + infoWindow.getLongitude() + "), \"" + infoWindow.getOptions() + "\", '" + markerId + "');");
            else
            {
                List list = infoWindow.getChildren();
                String content="";
                for(int i=0;i<list.size();i++){
                    content+=list.get(i).toString();
                }
                content=content.trim();
                content=content.replaceAll("\"","\\\\\"");
                content=content.replaceAll("[\n\t]","");
                System.out.println(content);
                writer.write("ice.ace.gMap.addGWindow('" + mapId + "', '" + clientId + "',\"" + content + "\", new google.maps.LatLng(" + infoWindow.getLatitude() +
                        "," + infoWindow.getLongitude() + "), \"" + infoWindow.getOptions() + "\", '" + markerId + "');");
            }

			writer.write("});");
			writer.endElement("script");
			writer.endElement("span");
	}

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

    public boolean getRendersChildren() {
        return true;
    }
}