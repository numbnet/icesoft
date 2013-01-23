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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.renderkit.CoreRenderer;

/** The gMapOverlay component renderer. */
public class GMapOverlayRenderer extends CoreRenderer {


    /**
     * If our rendered property is true, render the beginning of the current
     * state of this UIComponent to the response contained in the specified
     * FacesContext.
     *
     * @throws IOException - if an input/output error occurs while rendering
     */
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {

        GMapOverlay overlay = (GMapOverlay) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = overlay.getClientId(context);
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        if (overlay.getPoints() != null && overlay.getShape() != null) {
            writer.write(
                    String.format("var wrapper = mobi.gmap.getWrapper('%s');"
                            + "wrapper.removeGOverlay('%s');",
                       overlay.getParent().getClientId(context), clientId));
            writer.write(
                    String.format("wrapper.gOverlay('%s','%s','%s','%s');",
                            clientId, overlay.getShape(),
                            overlay.getPoints(), overlay.getOptions()));
        }
        writer.endElement("script");
    }
}