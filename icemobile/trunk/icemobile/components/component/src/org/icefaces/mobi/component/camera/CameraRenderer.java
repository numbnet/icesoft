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

package org.icefaces.mobi.component.camera;


import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.Utils;
import org.icefaces.impl.application.AuxUploadResourceHandler;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ValueChangeEvent;

import javax.faces.render.Renderer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class CameraRenderer extends Renderer {
    private static Logger logger = Logger.getLogger(CameraRenderer.class.getName());


    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        Camera camera = (Camera) uiComponent;
        String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
        String clientId = camera.getClientId();
        try {
            if (!camera.isDisabled()) {
                Map<String, Object> map = new HashMap<String, Object>();
                boolean valid =  extractImages(facesContext, map, clientId);
                /* only set map to value if boolean returned from extractImages is true */
                if (valid){
                    if (map !=null){
                       camera.setValue(map);

             //   trigger valueChange and add map as newEvent value old event is NA
                       uiComponent.queueEvent(new ValueChangeEvent(uiComponent,
    		    		    null, map));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param facesContext
     * @param map
     * @param clientId
     * @return   boolean true means validation of the upload passes, false mean it does not.
     * @throws IOException
     *
     * that uploaded this component.
     */
    public boolean extractImages(FacesContext facesContext, Map map, String clientId) throws IOException {
        HttpServletRequest request = (HttpServletRequest)
                facesContext.getExternalContext().getRequest();
        boolean isValid=false;

        String partUploadName = clientId;
        Part part = null;
        try {
            part = request.getPart(partUploadName);
        } catch (ServletException e)  {
            //ignore ServletException here since auxUpload is not multipart
        }
        if (null == part)  {
            Map auxMap = AuxUploadResourceHandler.getAuxRequestMap();
            part = (Part) auxMap.get(partUploadName);
        }
        if (part !=null){
            String contentType = part.getContentType();
            String fileName = java.util.UUID.randomUUID().toString();
            if (part.getSize()<=0){
               isValid=false;
            }else {
               isValid = true;
            }
            if ("image/jpeg".equals(contentType)|| "image/jpg".equals(contentType)) {
                fileName += ".jpg";
            }
            else if ("image/png".equals(contentType)) {
                fileName += ".png";
            }
            else {  /*if not jpeg or png give it filename of oth for other */
                fileName += ".oth";
            }
            Utils.createMapOfFile(map, request, part, fileName, contentType, facesContext);
        }
        return isValid;
    }


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Camera camera = (Camera) uiComponent;
        boolean isEnhanced = EnvUtils.isEnhancedBrowser(facesContext);
        boolean isAuxUpload = EnvUtils.isAuxUploadBrowser(facesContext);

        if (!isEnhanced && !isAuxUpload) {
            writer.startElement(HTML.SPAN_ELEM, uiComponent);
            writer.startElement(HTML.INPUT_ELEM, uiComponent);
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_FILE, null);
            writer.writeAttribute(HTML.ID_ATTR, clientId, null);
            writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
            writer.endElement(HTML.INPUT_ELEM);
            return;
        } 

        // root element
        boolean disabled = camera.isDisabled();
        // span as per MobI-11
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        // button element
        writer.startElement(HTML.BUTTON_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId + "_button", null);
        // write out style for input button, same as default device button.
        Utils.writeConcatenatedStyleClasses(writer,
                "mobi-button mobi-button-default",
                camera.getStyleClass());
        writer.writeAttribute(HTML.STYLE_ATTR, camera.getStyle(), HTML.STYLE_ATTR);

        int width = camera.getMaxwidth();
        int height = camera.getMaxheight();
        //default value of unset in params is Integer.MIN_VALUE
        String script;
        if (isAuxUpload)  {
            script = Utils.getICEmobileSXScript("camera", clientId);
        } else {
            if ( (width != Integer.MIN_VALUE) || 
                    (height != Integer.MIN_VALUE) ) {
                String params = "'" + clientId + "','maxwidth=" + width + 
                        "&maxheight=" + height + "'";
                script = "ice.camera(" + params + ");";
            } else {
                script = "ice.camera( '" + clientId + "' );";
            }
        }
        writer.writeAttribute(HTML.ONCLICK_ATTR, script, null);
        writer.writeText("camera", null);
        writer.endElement(HTML.BUTTON_ELEM);

    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Camera camera = (Camera) uiComponent;
        writer.endElement(HTML.SPAN_ELEM);
    }

}
