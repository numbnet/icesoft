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

import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.PassThruAttributeWriter;
import org.icefaces.mobi.renderkit.BaseResourceRenderer;
import org.icefaces.mobi.utils.Utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.logging.Logger;


public class VideoPlayerRenderer extends BaseResourceRenderer {
    private static Logger log = Logger.getLogger(VideoPlayerRenderer.class.getName());


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        VideoPlayer video = (VideoPlayer) uiComponent;

        // root element
//        boolean disabled = video.isDisabled();

        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        // apply component style class and append any user defined classes.
        StringBuilder styleClass = new StringBuilder(VideoPlayer.VIDEO_CLASS);
        if (video.getStyleClass() != null){
            styleClass.append(" ").append(video.getStyleClass());
        }
        writer.writeAttribute("class", styleClass.toString(), null);
        // apply style class
        PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, video.getSpanPassThruAttributes());

        writer.startElement("video", uiComponent);
        // pass though number attributes for span
        PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, video.getVideoPassThruAttributes());

        if (video.isControls())
            writer.writeAttribute("controls", "controls", null);

        if (video.isLoop()) writer.writeAttribute("loop", "loop", null);
        String mimeType = video.getType();
        if (null != mimeType)  {
            writer.writeAttribute("type", mimeType, null);
        }
        if (video.isPlaysinline()) {
            writer.writeAttribute("webkit-playsinline", "yes", null);
        }
        String scope = video.getScope().toLowerCase().trim();
        if (!scope.equals("flash") && !(scope.equals("window")) && !(scope.equals("application"))
                && (!scope.equals("request")) && (!scope.equals("view"))) {
            scope = "session";
        }
        String name = video.getName();
        if (null == name || name.equals("")) name = "video" + clientId;
        Object videoObject = video.getValue();
        String srcAttribute = "null";
        if (null != videoObject) {
            srcAttribute = processSrcAttribute(facesContext, videoObject, name, mimeType, scope, video.getUrl());
        }
        writer.writeAttribute("src", srcAttribute, null);
        writer.endElement("video");
        if (!Utils.isIOS())  {
            writer.startElement("br", uiComponent);
            writer.endElement("br");
            writer.startElement("a", uiComponent);
            writer.writeAttribute("href", srcAttribute, null);
            writer.writeText(video.getLinkLabel(), null);
            writer.endElement("a");
        }
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.SPAN_ELEM);
    }


}
