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

package org.icefaces.component.video;


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


public class VideoCaptureRenderer extends Renderer {
    private final static Logger logger = Logger.getLogger(VideoCaptureRenderer.class.getName());

    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        VideoCapture video = (VideoCapture) uiComponent;

        String clientId = video.getClientId();
        if (video.isDisabled()) {
            return;
        }

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            extractVideo(facesContext, map, clientId);
            video.setValue(map);
            uiComponent.queueEvent(new ActionEvent(uiComponent));

        } catch (Exception e) {
            logger.warning("Exception decoding video stream: " + e);
        }

    }

    private void extractVideo(FacesContext facesContext, Map map, String clientId) throws IOException {
        HttpServletRequest request = (HttpServletRequest)
                facesContext.getExternalContext().getRequest();
        try {
            for (Part part : request.getParts()) {
                String contentType = part.getContentType();
                String fileName = java.util.UUID.randomUUID().toString();
                boolean isVideo = false;
                if ("video/mp4".equals(contentType)) {
                    fileName = fileName + ".mp4";
                    isVideo = true;
                } else if ("video/mpeg".equals(contentType)) {
                    fileName = fileName + ".mp4";
                    isVideo = true;
                } else if ("video/mov".equals(contentType)) {
                    fileName = fileName + ".mov";
                    isVideo = true;
                } else if ("video/3gpp".equals(contentType)) {
                    fileName = fileName + ".3gp";
                    isVideo = true;
                }

                //need to restrict to parts matching the ID of this component
                if (isVideo) {
                    Utils.createMapOfFile(map, request, part, fileName, contentType, facesContext);
                }
//            	else { //log an error for unknown filetype
//                    logger.info(" No video formatted files captured from multipart upload, part="+part.getContentType());
//            	}
            }
        } catch (ServletException e) {
            logger.warning("ServletException decoding video stream: " + e);
            //ServletException is discarded since it indicates
            //form-encoded rather than multipart
        } catch (Exception ee) {
            logger.warning("Exception decoding video: " + ee);
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        VideoCapture video = (VideoCapture) uiComponent;

        // root element
        boolean disabled = video.isDisabled();
        if (!EnvUtils.isEnhancedBrowser(facesContext)) {
            writer.startElement(HTML.SPAN_ELEM, uiComponent);
            writer.startElement(HTML.INPUT_ELEM, uiComponent);
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_FILE, null);
            writer.writeAttribute(HTML.ID_ATTR, clientId, null);
            writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
            writer.endElement(HTML.INPUT_ELEM);
            return;
        }
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        // button element
        //only pass id in basic mode for development

        writer.startElement("input", uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
        writer.writeAttribute("id", clientId + "_button", null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId + "_button", null);
        writer.writeAttribute("value", "camcorder", null);
        // write out style for input button, same as default device button.
        Utils.writeConcatenatedStyleClasses(writer,
                "mobi-button mobi-button-default",
                video.getStyleClass());
        writer.writeAttribute(HTML.STYLE_ATTR, video.getStyle(), HTML.STYLE_ATTR);
        if (disabled) writer.writeAttribute("disabled", "disabled", null);
        int width = video.getMaxwidth();
        int height = video.getMaxheight();
        int maxtime = video.getMaxtime();
        //default value of unset in params is Integer.MIN_VALUE
        //hopefully Steve can calculate size in android based on maxwidth and
        //maxheight? 
        String params = "'" + clientId + "'";
        //only commonality between iPhone and android is duration or maxTime
        //simplify this scripting when devices have this implemented and is final api
        int unset = Integer.MIN_VALUE;
        int numParams = 0;
        String attributeSeparator = "&";
        if (maxtime != unset || width != unset || height != unset) {
            params += ",'";
        }
        if (maxtime != unset) {
            if (numParams > 0) {
                params += attributeSeparator;
            }
            params += "maxtime=" + maxtime;
            numParams++;
        }
        if (width != Integer.MIN_VALUE) {
            if (numParams > 0) {
                params += attributeSeparator;
            }
            params += "maxwidth=" + width;
            numParams++;
        }
        if (height != Integer.MIN_VALUE) {
            if (numParams > 0) {
                params += attributeSeparator;
            }
            params += "maxheight=" + height;
            numParams++;
        }
        if (numParams > 0) {
            params += "'";
        }
//        logger.info("*******finalScript params="+params);
        String finalScript = "ice.camcorder(" + params + ");";
//	    logger.info("final Script call="+finalScript);
        if (video.isDisabled()) {
            writer.writeAttribute(HTML.DISABLED_ATTR, HTML.DISABLED_ATTR, null);
        }
        writer.writeAttribute(HTML.ONCLICK_ATTR, finalScript, null);
//        			
        writer.endElement("input");

    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
//        String clientId = uiComponent.getClientId(facesContext);
        writer.endElement(HTML.SPAN_ELEM);
    }
}
