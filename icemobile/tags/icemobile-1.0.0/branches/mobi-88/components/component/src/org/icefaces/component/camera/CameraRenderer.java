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

package org.icefaces.component.camera;


import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.Utils;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
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

    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        Camera camera = (Camera) uiComponent;
        String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
        String clientId = camera.getClientId();
        try {
            if (!camera.isDisabled()) {
                Map<String, Object> map = new HashMap<String, Object>();
                extractImages(facesContext, map, clientId);
                camera.setValue(map);
                uiComponent.queueEvent(new ActionEvent(uiComponent));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void extractImages(FacesContext facesContext, Map map, String clientId) throws IOException {
        HttpServletRequest request = (HttpServletRequest)
                facesContext.getExternalContext().getRequest();
        try {
            for (Part part : request.getParts()) {
                //  	logger.info("********part name="+part.getName()+" clientId="+clientId);
                String contentType = part.getContentType();
                String fileName = java.util.UUID.randomUUID().toString();
                if ("image/jpeg".equals(contentType)) {
                    fileName += ".jpg";
                    Utils.createMapOfFile(map, request, part, fileName, contentType, facesContext);
                }
                if ("image/png".equals(contentType)) {
                    fileName += ".png";
                    Utils.createMapOfFile(map, request, part, fileName, contentType, facesContext);
                }
            }
            /**   This is how the code should read so that multiple media component uploads can be on same form
             *           Part part=  request.getPart(clientId);
             if (null!=part){
             String contentType = part.getContentType();
             logger.info("part " + part.getName() + " " + contentType);
             String fileName=java.util.UUID.randomUUID().toString();
             if ("image/jpeg".equals(contentType))  {
             fileName += ".jpg";
             Utils.createMap(map, request, part, fileName, contentType, facesContext);
             }
             if ("image/png".equals(contentType)){
             fileName += ".png";
             Utils.createMap(map, request, part, fileName, contentType, facesContext);
             }
             }
             */

        } catch (ServletException e) {
            //ServletException is discarded since it indicates
            //form-encoded rather than multipart
        }
    }


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Camera camera = (Camera) uiComponent;
        if (!EnvUtils.isEnhancedBrowser(facesContext)) {
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
        if (width != Integer.MIN_VALUE || height != Integer.MIN_VALUE) {
            String params = "'" + clientId + "','maxwidth=" + width + "&maxheight=" + height + "'";
            String finalScript = "ice.camera(" + params + ");";
//            logger.info("final Script call="+finalScript);
            writer.writeAttribute(HTML.ONCLICK_ATTR, finalScript, null);
        } else {
            writer.writeAttribute(HTML.ONCLICK_ATTR, "ice.camera( '" + clientId + "' );", null);
        }
        writer.writeText("camera", null);
        writer.endElement(HTML.BUTTON_ELEM);
        //no more hidden fields


    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Camera camera = (Camera) uiComponent;
        writer.endElement(HTML.SPAN_ELEM);
    }

}
