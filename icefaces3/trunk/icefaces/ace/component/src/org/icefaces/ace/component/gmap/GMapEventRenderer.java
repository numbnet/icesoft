package org.icefaces.ace.component.gmap;

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

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

@MandatoryResourceComponent(tagName = "gMap", value = "org.icefaces.ace.component.gmap.GMap")
public class GMapEventRenderer extends CoreRenderer {


    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        GMapEvent gMapEvent = (GMapEvent) component;
        writer.startElement("span", null);
        writer.writeAttribute("id", clientId + "_layer", null);
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("ice.ace.jq(function() {");
        String mapContext = gMapEvent.getParent().toString();
        Boolean done = false;
        UIComponent testComponent = component.getParent();
        while (!done) {
            if (testComponent.getRendererType().contains("GMapRenderer")) {
                mapContext = testComponent.getClientId(context);
                done = true;
            } else if (testComponent.toString().contains("UIOutput")) {
                System.out.print("Component not nested in map");
                done = true;
            } else
                testComponent = testComponent.getParent();
        }
        writer.write("ice.ace.gMap.addEvent('" + mapContext + "','" + gMapEvent.getParent().getClientId(context) + "', '" + clientId + "','" + gMapEvent.getParent().toString() + "','" + gMapEvent.getEventType() + "','" + gMapEvent.getRendererType() + "',\"" + gMapEvent.getScript() + "\");");
        writer.write("});");
        writer.endElement("script");
        writer.endElement("span");

    }
}