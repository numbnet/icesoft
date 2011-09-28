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


import org.icefaces.component.utils.BaseInputResourceRenderer;
import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.Utils;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CameraRenderer extends BaseInputResourceRenderer {
    private static Logger logger = Logger.getLogger(CameraRenderer.class.getName());


    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Camera camera = (Camera) uiComponent;
        String clientId = camera.getClientId();
        try {
            if (!camera.isDisabled()) {
                Map<String, Object> map = new HashMap<String, Object>();
                boolean valid = extractImages(facesContext, map, clientId);
                /* only set map to value if boolean returned from extractImages is true */
                if (valid) {
                    this.setSubmittedValue(uiComponent, map);
                    Integer old = Integer.MAX_VALUE;
                    Integer selected = Integer.MIN_VALUE;
                    //   just trigger valueChange for now as validation may include
                    //   only queueing this if certain attributes change to valid values.
                    uiComponent.queueEvent(new ValueChangeEvent(uiComponent,
                            old, selected));
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error extracting image data from request.",e);
        }
    }

    private boolean extractImages(FacesContext facesContext, Map map, String clientId) throws IOException {
        HttpServletRequest request = (HttpServletRequest)
                facesContext.getExternalContext().getRequest();
        boolean isValid = false;

        try {
            //if it's a container upload then the name of part if <clientId>-file
            //if desktop browser it's just the clientId
            String partUploadName = clientId;
            if (EnvUtils.isEnhancedBrowser(facesContext)) {
                partUploadName += "-file";
            }
            Part part = request.getPart(partUploadName);
            if (part != null) {
                String contentType = part.getContentType();
                String fileName = java.util.UUID.randomUUID().toString();
                isValid = part.getSize() > 0;
                if ("image/jpeg".equals(contentType) || "image/jpg".equals(contentType)) {
                    fileName += ".jpg";
                } else if ("image/png".equals(contentType)) {
                    fileName += ".png";
                } else {  /*if not jpeg or png give it filename of oth for other */
                    fileName += ".oth";
                }
                Utils.createMapOfFile(map, request, part, fileName, contentType, facesContext);
            }
            return isValid;
        } catch (ServletException e) {
            //ServletException is discarded since it indicates
            //form-encoded rather than multipart
            return isValid;
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
            writer.writeAttribute(HTML.ONCLICK_ATTR, finalScript, null);
        } else {
            writer.writeAttribute(HTML.ONCLICK_ATTR, "ice.camera( '" + clientId + "' );", null);
        }
        writer.writeText("camera", null);
        writer.endElement(HTML.BUTTON_ELEM);

    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.SPAN_ELEM);
    }

}
