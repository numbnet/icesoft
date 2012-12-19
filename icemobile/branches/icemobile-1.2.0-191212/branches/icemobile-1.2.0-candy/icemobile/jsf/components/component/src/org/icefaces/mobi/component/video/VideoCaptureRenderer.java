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

package org.icefaces.mobi.component.video;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.icefaces.mobi.renderkit.BaseInputResourceRenderer;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.utils.MobiJSFUtils;

import org.icemobile.renderkit.DeviceCoreRenderer;



public class VideoCaptureRenderer extends BaseInputResourceRenderer {
    private final static Logger logger = Logger.getLogger(VideoCaptureRenderer.class.getName());

    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        VideoCapture camcorder = (VideoCapture) uiComponent;
        String clientId = camcorder.getClientId();
        if (!camcorder.isDisabled()) {
           try {
              Map<String, Object> map = new HashMap<String, Object>();
              boolean isValid = extractVideo(facesContext, map, clientId);
              if (isValid){
                 if (map !=null){
                    camcorder.setValue(map);
             //     trigger valueChange and add map as newEvent value old event is NA
                    uiComponent.queueEvent(new ValueChangeEvent(uiComponent,
    		    		    null, map));
                }
              }

           } catch (Exception e) {
              e.printStackTrace();
           }
        }
    }

    public boolean extractVideo(FacesContext facesContext, Map map, String clientId) throws IOException {
        return MobiJSFUtils.decodeComponentFile(facesContext, clientId, map);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        VideoCapture camcorder = (VideoCapture) uiComponent;
        String oldLabel = camcorder.getButtonLabel();
        if (MobiJSFUtils.uploadInProgress(camcorder))  {
            camcorder.setButtonLabel(camcorder.getCaptureMessageLabel()) ;
        } 
        DeviceCoreRenderer renderer = new DeviceCoreRenderer();
        ResponseWriterWrapper writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        renderer.encode(camcorder, writer, false);
        camcorder.setButtonLabel(oldLabel);

 /*       boolean isEnhanced =  cd.isICEmobileContainer()  || cd.isSXRegistered() ;
        boolean isAuxUpload = EnvUtils.isAuxUploadBrowser(facesContext);
        // root element
        boolean disabled = camcorder.isDisabled();
        if (!isEnhanced || camcorder.isUseNative()) {
            writer.startElement(HTML.SPAN_ELEM, uiComponent);
            writer.startElement(HTML.INPUT_ELEM, uiComponent);
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_FILE, null);
            writer.writeAttribute(HTML.ID_ATTR, clientId, null);
            writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
            writer.writeAttribute("accept", "camcorder/*", null);
            writer.endElement(HTML.INPUT_ELEM);
            writer.endElement(HTML.SPAN_ELEM);
            return;
        }
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        // button element
        writer.startElement("input", uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
        writer.writeAttribute("id", clientId + "_button", null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId + "_button", null);
        writer.writeAttribute("value", "camcorder", null);
        // write out style for input button, same as default device button.
        JSFUtils.writeConcatenatedStyleClasses(writer,
                "mobi-button",
                video.getStyleClass());
        writer.writeAttribute(HTML.STYLE_ATTR, video.getStyle(), HTML.STYLE_ATTR);
        if (disabled) writer.writeAttribute("disabled", "disabled", null);
        int width = video.getMaxwidth();
        int height = video.getMaxheight();
        int maxtime = video.getMaxtime();

        String script;
        if (isAuxUpload)  {
            script = MobiJSFUtils.getICEmobileSXScript("camcorder", uiComponent);
        } else {
            //default value of unset in params is Integer.MIN_VALUE
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
            script = "ice.camcorder(" + params + ");";
        }
        if (video.isDisabled()) {
            writer.writeAttribute(HTML.DISABLED_ATTR, HTML.DISABLED_ATTR, null);
        }
        writer.writeAttribute(HTML.ONCLICK_ATTR, script, null);
        writer.endElement("input");
        writer.endElement(HTML.SPAN_ELEM);  */
    }
}
