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
package org.icefaces.component.audio;

import org.icefaces.component.utils.BaseResourceRenderer;
import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.PassThruAttributeWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;


public class AudioRenderer extends BaseResourceRenderer {
    private static Logger log = Logger.getLogger(AudioRenderer.class.getName());


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Audio audio = (Audio) uiComponent;
        Map contextMap = facesContext.getAttributes();
        String deviceType = "iphone";  //default
        if (null != contextMap.get("device")) {
            deviceType = contextMap.get("device").toString();
        }
        boolean disabled = audio.isDisabled();  //haven't done check to see if it's disabled

        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        if (!deviceType.equals("bberry")) {
            writer.startElement("audio", uiComponent);
            if (disabled) {
                writer.writeAttribute("disabled", "disabled", null);
            }
            writeCommonAttributes(writer, audio, facesContext, clientId);
            writer.endElement("audio");
        } else {
            writer.startElement("video", uiComponent);
            writeCommonAttributes(writer, audio, facesContext, clientId);
            writer.writeAttribute("autoplay", "true", null);
            if (disabled) {
                writer.writeAttribute("disabled", "disabled", null);
            }
            writer.writeAttribute("height", "20", null);
            writer.writeAttribute("width", "100", null);
            writer.endElement("video");
        }
    }


    private void writeCommonAttributes(ResponseWriter writer, Audio audio, FacesContext facesContext,
                                       String clientId)
            throws IOException {
        PassThruAttributeWriter.renderNonBooleanAttributes(writer, audio, audio.getAttributesNames());
        if (audio.isControls())
            writer.writeAttribute("controls", "controls", null);
        String mimeType = audio.getType();
        if (null == mimeType)
            mimeType = "audio/mpeg"; //do we want a default type this seems to work best???
        writer.writeAttribute("type", mimeType, null);
        String scope = audio.getScope().toLowerCase().trim();
        if (!scope.equals("flash") && !(scope.equals("window")) && !(scope.equals("application"))
                && (!scope.equals("request")) && (!scope.equals("view"))) {
            scope = "session";
        }
        String name = audio.getName();
        if (null == name || name.equals("")) name = "audio" + clientId;
        Object audioObject = audio.getValue();
        String srcAttribute = "null";
        if (null != audioObject) {
            srcAttribute = processSrcAttribute(facesContext, audioObject, name, mimeType, scope, audio.getUrl());
        }
        writer.writeAttribute("src", srcAttribute, null);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.endElement(HTML.SPAN_ELEM);
    }


}
