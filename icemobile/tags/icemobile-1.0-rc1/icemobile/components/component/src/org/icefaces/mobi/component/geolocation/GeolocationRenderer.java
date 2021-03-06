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

package org.icefaces.mobi.component.geolocation;

import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.renderkit.CoreRenderer;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GeolocationRenderer extends CoreRenderer {
    private static Logger log = Logger.getLogger(GeolocationRenderer.class.getName());
    private static final String JS_NAME = "geolocation.js";
    private static final String JS_MIN_NAME = "geolocation-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.geolocation";

    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        Geolocation geolocation = (Geolocation) uiComponent;
//        String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
        String clientId = geolocation.getClientId();
        //assuming we only want the decoding done if the Geolocation is the source of the event?
        //leave out for now so can test the decoding with form submission
        //    if (clientId.equals(source)) {
        try {
            if (!geolocation.isDisabled()) {
                //MOBI-11 requirement is to decode the  values
                String nameHidden = clientId + "_field";
                String locationString = String.valueOf(requestParameterMap.get(nameHidden));
                if (null != locationString || !("null".equals(locationString))){
                    String[] params = locationString.split(",\\s*");
                    int numberOfParams = params.length;
                    if (numberOfParams > 1) {
                        String latString = params[0];
                        if (null != latString) {
                            try {
                                Double latitude = Double.parseDouble(latString);
                                geolocation.setLatitude(latitude);
                            } catch (Exception e) {
                                log.log(Level.WARNING, "ERROR  parsing latitude value, defaulting to zero",e);
                                geolocation.setLatitude(0.0);
                            }
                        }
                        String longString = params[1];
                        if (null != longString) {
                            try {
                                Double longitude = Double.parseDouble(longString);
                                geolocation.setLongitude(longitude);
                            } catch (Exception e) {
                                log.log(Level.WARNING, "ERROR  parsing longitude value, defaulting to zero",e);
                                geolocation.setLongitude(0.0);
                            }
                        }
                        decodeBehaviors(facesContext, geolocation);
                    }
                }
            }

        } catch (Exception e) {
            log.log(Level.WARNING, "Error decoding geo-location request paramaters.",e);
        }
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Geolocation locator = (Geolocation) uiComponent;
        ClientBehaviorHolder cbh = (ClientBehaviorHolder)uiComponent;
        boolean hasBehaviors = !cbh.getClientBehaviors().isEmpty();
        // root element
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_locHidden", null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId + "_field", null);
        boolean disabled = locator.isDisabled();
        boolean singleSubmit = locator.isSingleSubmit();
        if (disabled) {
            writer.writeAttribute("disabled", "disabled", null);
        }
        writer.endElement("input");
        if (!disabled ) {
            StringBuilder sb = new StringBuilder(255);
            String fnCall = "document.getElementById(\"" + clientId + "_locHidden\").value=pos.coords.latitude+\",\"+pos.coords.longitude;";
            sb.append(fnCall);
            if ( hasBehaviors){
                  sb.append(this.buildAjaxRequest(facesContext, cbh, "activate"));
            }
            else if (singleSubmit){
                String ssCall = "ice.se(null, '"+clientId+"');";
                sb.append(ssCall);
            }
            String finalScript = "navigator.geolocation.getCurrentPosition(function(pos) { " +  sb.toString() + "} );";
            writer.startElement("script", uiComponent);
            writer.writeAttribute("id", clientId+"_script", "id");
            writer.write(finalScript);
            writer.endElement("script");
        }

        writer.endElement(HTML.SPAN_ELEM);
    }


}
