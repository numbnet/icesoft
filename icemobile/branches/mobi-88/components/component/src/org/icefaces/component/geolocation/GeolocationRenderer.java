/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icefaces.component.geolocation;

import org.icefaces.component.utils.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;


public class GeolocationRenderer extends Renderer {
    private static Logger log = Logger.getLogger(GeolocationRenderer.class.getName());

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
                //MOBI-11 requirement is to decode the height and width values
                String idHidden = clientId + "_locHidden";
                String nameHidden = clientId + "_field";
                String locationString = String.valueOf(requestParameterMap.get(nameHidden));
                if (null != locationString || !(locationString.equals("null"))) {
                    String[] params = locationString.split(",\\s*");
                    int numberOfParams = params.length;
//	           		    	for (int i=0; i<params.length; i++){
//	           		    		System.out.println(" \t param ["+i+"]="+params[i]);
//	           		    	}
                    if (numberOfParams > 1) {
                        String latString = params[0];
                        if (null != latString) {
//                            log.info("\t\t latString=" + latString);
                            try {
                                Double latitude = Double.parseDouble(latString);
//                                log.info("\t\t latitude=" + latitude);
                                geolocation.setLatitude(latitude);
                            } catch (Exception e) {
                                log.warning(" Exception thrown, setting latitude to zero e=");
                                geolocation.setLatitude(0.0);
                            }
                        }
                        String longString = params[1];
                        if (null != longString) {
//                            log.info("longString=" + longString);
                            try {
                                Double longitude = Double.parseDouble(longString);
                                geolocation.setLongitude(longitude);
                            } catch (Exception e) {
                                log.warning(" Exception thrown, setting longitutde to zero e=");
                                geolocation.setLongitude(0.0);
                            }
                        }
                    }
                } else log.finer(" params is empty!!");
            } else log.finer(" no hidden value set!!");
            //unparse the location string


            //	 	    do we need to queue an event or a valueChangeEvent?
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Geolocation Geolocation = (Geolocation) uiComponent;
        // root element
//        boolean disabled = Geolocation.isDisabled();

        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_locHidden", null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId + "_field", null);
        writer.endElement("input");
        String fnCall = "document.getElementById(\"" + clientId + "_locHidden\").value=pos.coords.latitude+\",\"+pos.coords.longitude;";
        String finalScript = "navigator.geolocation.getCurrentPosition(function(pos) { " + fnCall + "} );";
        //   System.out.println(" \t final script="+finalScript);
        writer.startElement("script", uiComponent);
        writer.write(finalScript);
        writer.endElement("script");
        //       ScriptWriter.insertScript(facesContext, uiComponent,finalScript);

    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
//        String clientId = uiComponent.getClientId(facesContext);
//        Geolocation Geolocation = (Geolocation) uiComponent;

        writer.endElement(HTML.SPAN_ELEM);
    }


}
