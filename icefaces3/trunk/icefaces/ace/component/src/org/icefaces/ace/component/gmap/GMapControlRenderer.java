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

@MandatoryResourceComponent(tagName = "gMap", value = "org.icefaces.ace.component.gmap.GMap")
public class GMapControlRenderer extends CoreRenderer {


    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        GMapControl control = (GMapControl) component;
        String clientId = control.getClientId(context);
        writer.startElement("span", null);
        writer.writeAttribute("id", clientId + "_control", null);
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("ice.ace.jq(function() {");
        if (control.isDisabled())
            writer.write("ice.ace.gMap.removeControl('" + control.getParent().getClientId(context) + "', '" + control.getName() + "');");
        else {
            writer.write("ice.ace.gMap.addControl('" + control.getParent().getClientId(context) + "', '" + control.getName() +
                    "', '" + control.getPosition() + "', '" + control.getControlStyle() + "');");
        }
        writer.write("});");
        writer.endElement("script");
        writer.endElement("span");
    }
}