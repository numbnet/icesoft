/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
package org.icefaces.mobi.component.augmentedreality;

import static org.icefaces.mobi.utils.HTML.ID_ATTR;
import static org.icefaces.mobi.utils.HTML.SPAN_ELEM;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.impl.application.AuxUploadResourceHandler;
import org.icefaces.impl.application.AuxUploadSetup;
import org.icefaces.mobi.renderkit.BaseInputRenderer;
import org.icefaces.mobi.renderkit.RenderUtils;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.util.BridgeItCommand;
import org.icemobile.util.CSSUtils;
import org.icemobile.util.ClientDescriptor;


public class AugmentedRealityRenderer extends BaseInputRenderer  {
    private static final String LOC_LABEL = "locationLabel";
    private static final String LOC_LAT = "locationLat";
    private static final String LOC_LON = "locationLon";
    private static final String LOC_ALT = "locationAlt";
    private static final String LOC_DIR = "locationDir";
    private static final String LOC_ICON = "locationIcon";
    private static final String MARK_MODEL = "markerModel";
    private static final String MARK_LABEL = "markerLabel";

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
             throws IOException {
         
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        AugmentedReality ag = (AugmentedReality)uiComponent;
        ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
        
        writer.startElement(SPAN_ELEM, null);
        writer.writeAttribute(ID_ATTR, clientId + "_wpr", null);
        RenderUtils.writeStyle(uiComponent, writer);
        RenderUtils.writeStyleClassAndBase(uiComponent, writer, "mobi-wrapper");
        if( client.isBridgeItSupportedPlatform(BridgeItCommand.AUGMENTEDREALITY) ){
            RenderUtils.startButtonElem(uiComponent, writer);
             
            RenderUtils.writeDisabled(uiComponent, writer);
            writer.writeAttribute("class",CSSUtils.STYLECLASS_BUTTON, null);
            RenderUtils.writeTabIndex(uiComponent, writer);
             
            String buttonValue=ag.getButtonLabel();
            if (MobiJSFUtils.uploadInProgress(ag))  {
                buttonValue = ag.getCaptureMessageLabel();
            } 
    
            String locationsString = "";
            for (UIComponent child : ag.getChildren())  {
                if (child instanceof AugmentedRealityLocations) {
                    AugmentedRealityLocations locations = 
                            (AugmentedRealityLocations) child;
                    locationsString = iterateLocations(facesContext, locations, ag.getUrlBase());
                }
                if (child instanceof AugmentedRealityMarkers) {
                    AugmentedRealityMarkers markers =
                            (AugmentedRealityMarkers) child;
                    locationsString += iterateMarkers(facesContext, markers);
                    locationsString += "'v':'vuforia'";
                }
            }
    
            String script = "bridgeit.augmentedReality( '" + clientId + "', '', "
                + "{postURL:'" + AuxUploadSetup.getInstance().getUploadURL() + "',"
                + "cookies:{'JSESSIONID':'" + MobiJSFUtils.getSessionIdCookie(facesContext) +  "'},"
                + " locations:{" + locationsString + "}});";
            writer.writeAttribute(HTML.ONCLICK_ATTR, script, null);
            writer.startElement(HTML.SPAN_ELEM, uiComponent);
            writer.writeText(buttonValue, null);
            writer.endElement(HTML.SPAN_ELEM);
            
            writer.endElement(HTML.BUTTON_ELEM);
        }
        else{
            UIComponent fallbackFacet = uiComponent.getFacet("fallback");
            if( fallbackFacet != null ){
                fallbackFacet.encodeAll(facesContext);
            }
        }
        writer.endElement(SPAN_ELEM);
     }

    @Override
    public void decode(FacesContext facesContext, UIComponent component) {
        Map parameterMap = facesContext.getExternalContext()
                .getRequestParameterMap();
        AugmentedReality aug = (AugmentedReality) component;
        String clientId = component.getClientId();
        Object submitted = parameterMap.get(clientId);
        if (null == submitted)  {
            Map auxMap = AuxUploadResourceHandler.getAuxRequestMap();
            submitted = auxMap.get(clientId);
        }
        if (null != submitted) {
            String submittedString = String.valueOf(submitted);
            if (submittedString != null){
                Object convertedValue = this.getConvertedValue(facesContext, component, submittedString);
                this.setSubmittedValue(aug, convertedValue);
            }

        }
    }


    String iterateLocations(FacesContext facesContext, 
            AugmentedRealityLocations locations, String urlBase)  {
        String var = locations.getVar();
        if (null == var) {
            //cannot iterate without a var
            return null;
        }
        StringBuilder result = new StringBuilder();
        
        Map<String, Object> requestMap =
              facesContext.getExternalContext().getRequestMap();
        Collection items = (Collection) locations.getValue();
        //an optimization will be to find the longest common prefix of
        //the itemIcon values and set urlBase to that
        for (Object item : items)  {
            Object oldVar = requestMap.put(var, item);
            Map<String,Object> attrs = locations.getAttributes();
            String itemLabel = (String) attrs.get(LOC_LABEL);
            result.append("'");
            result.append(itemLabel);
            result.append("'");
            result.append(":");
            result.append("'");
            result.append(attrs.get(LOC_LAT)).append(",");
            result.append(attrs.get(LOC_LON)).append(",");

            Object itemAlt = attrs.get(LOC_ALT);
            if (null != itemAlt)  {
                result.append(itemAlt);
            }
            result.append(",");
            Object itemDir = attrs.get(LOC_DIR);
            if (null != itemDir)  {
                result.append(itemDir);
            }
            result.append(",");
            String itemIcon = (String) attrs.get(LOC_ICON);
            if (null != itemIcon)  {
                result.append(itemIcon);
            }
            result.append("'");
            result.append(",");
            requestMap.put(var, oldVar);
        }
        String finalResult = result.toString();
        return finalResult.length() > 0 ? finalResult.substring(0,finalResult.length()-1) : ""; // remove last comma
    }

    String iterateMarkers(FacesContext facesContext,
            AugmentedRealityMarkers markers)  {
        String var = markers.getVar();
        if (null == var) {
            //cannot iterate without a var
            return null;
        }
        StringBuilder result = new StringBuilder();
        
        Map<String, Object> requestMap =
              facesContext.getExternalContext().getRequestMap();
        Collection items = (Collection) markers.getValue();
        int index = 0;
        for (Object item : items)  {
            Object oldVar = requestMap.put(var, item);
            Map<String,Object> attrs = markers.getAttributes();
            String markerLabel = (String) attrs.get(MARK_LABEL);
            String itemID = "_" + index;
            result.append("'");
            result.append(markerLabel + itemID);
            result.append("'");
            result.append(":");

            String markerModel = (String) attrs.get(MARK_MODEL);
            if (null != markerModel)  {
                result.append("'");
                result.append(markerModel);
                result.append("'");
            }
            result.append(",");
            requestMap.put(var, oldVar);
            index++;
        }
        return result.toString();
   }

}
