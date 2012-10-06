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
package org.icefaces.mobi.component.audio;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.component.video.VideoPlayer;
import org.icefaces.mobi.renderkit.BaseResourceRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icefaces.mobi.utils.PassThruAttributeWriter;
import org.icemobile.util.ClientDescriptor;


public class AudioRenderer extends BaseResourceRenderer {
    private static Logger log = Logger.getLogger(AudioRenderer.class.getName());


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        
        ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
        
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Audio audio = (Audio) uiComponent;
        boolean disabled = audio.isDisabled();  //haven't done check to see if it's disabled

        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        // apply style class
        PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent,
                audio.getSpanPassThruAttributes());
        // apply component style class and append any user defined classes.
        StringBuilder styleClass = new StringBuilder(VideoPlayer.VIDEO_CLASS);
        if (audio.getStyleClass() != null){
            styleClass.append(" ").append(audio.getStyleClass());
        }
        writer.writeAttribute("class", styleClass.toString(), null);
        String srcAttribute;
        if ( client.isBlackBerryOS()) {
            writer.startElement("audio", uiComponent);
            if (disabled) {
                writer.writeAttribute("disabled", "disabled", null);
            }
            srcAttribute = writeCommonAttributes(writer, audio, facesContext, clientId);
            writer.endElement("audio");
        } else {
            writer.startElement("video", uiComponent);
            srcAttribute = writeCommonAttributes(writer, audio, facesContext, clientId);
            writer.writeAttribute("autoplay", "true", null);
            if (disabled) {
                writer.writeAttribute("disabled", "disabled", null);
            }
            writer.writeAttribute("height", "20", null);
            writer.writeAttribute("width", "100", null);
            writer.endElement("video");
        }
        // write inline image link
        if (!client.isIOS() && audio.getLinkLabel() != null)  {
            writer.startElement("br", uiComponent);
            writer.endElement("br");
            writer.startElement("a", uiComponent);
            writer.writeAttribute("target", "_blank", null);
            writer.writeAttribute("href", srcAttribute, null);
            writer.writeText(audio.getLinkLabel(), null);
            writer.endElement("a");
        }
    }


    private String writeCommonAttributes(ResponseWriter writer, Audio audio, FacesContext facesContext,
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
        return srcAttribute;
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.endElement(HTML.SPAN_ELEM);
    }


}
