/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobi.component.gmap;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.renderkit.CoreRenderer;

/**
 * gMap Renderer.
 *
 *
 */
public class GMapRenderer extends CoreRenderer {

    /**
     * If our rendered property is true, render the beginning of the current
     * state of this UIComponent to the response contained in the specified
     * FacesContext.
     *
     * @param context The JSF Context
     * @param component The JSF Component reference
     *
     * @throws IOException - if an input/output error occurs while rendering
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        GMap gmap = (GMap) component;

        writer.startElement("div", component);
        writer.writeAttribute("id", clientId + "_wrp", null);
        writer.writeAttribute("style", "height: 100%;", null);
        
        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("style", "height: 100%;", null);
        writer.endElement("div");

        writeJavascriptFile(context, component, "gmap.js", "gmap.js",
                "org.icefaces.component.gmap");

        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write(String.format("var wrapper = mobi.gmap.create('%s');",
                clientId));
        if ((gmap.isLocateAddress() || !gmap.isIntialized())
                && (gmap.getAddress() != null && gmap.getAddress().length() > 2)) {
            writer.write(String.format("wrapper.locateAddress('%s');",
                    gmap.getAddress()));
        } else {
            writer.write(String
                    .format("wrapper.getMap().setCenter(new google.maps.LatLng(%s,%s));",
                            gmap.getLatitude(), gmap.getLongitude()));
        }
        writer.write(String.format("wrapper.getMap().setZoom(%s);",
                gmap.getZoomLevel()));
        writer.write(String.format("wrapper.setMapType('%s');", gmap.getType()
                .toUpperCase()));
        if (gmap.getOptions() != null && gmap.getOptions().length() > 1) {
            writer.write(String.format("wrapper.addOptions('%s');",
                    gmap.getOptions()));
        }
        writer.endElement("script");
    }

    /**
     * If our rendered property is true, render the children of this UIComponent
     * to the response contained in the specified
     * FacesContext.
     *
     * @param context The JSF Context
     * @param component The JSF Component reference
     *
     * @throws IOException - if an input/output error occurs while rendering
     */
    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException();
        }
        if (component.getChildCount() == 0) {
            return;
        }
        Iterator<UIComponent> kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.encodeBegin(context);
            if (kid.getRendersChildren()) {
                kid.encodeChildren(context);
            }
            kid.encodeEnd(context);
        }

    }
    
    /**
     * If our rendered property is true, render the end of the current
     * state of this UIComponent to the response contained in the specified
     * FacesContext.
     *
     * @param context The JSF Context
     * @param component The JSF Component reference
     *
     * @throws IOException - if an input/output error occurs while rendering
     */
    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        GMap gmap = (GMap) component;
        writer.endElement("div");
        gmap.setIntialized(true);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

}
