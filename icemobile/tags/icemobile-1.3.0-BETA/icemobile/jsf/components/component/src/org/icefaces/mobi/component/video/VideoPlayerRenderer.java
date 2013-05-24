/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icefaces.mobi.utils.PassThruAttributeWriter;
import org.icefaces.mobi.renderkit.BaseResourceRenderer;
import org.icefaces.mobi.utils.Utils;
import org.icemobile.renderkit.IResponseWriter;
import org.icemobile.util.ClientDescriptor;
import org.icemobile.renderkit.VideoPlayerCoreRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.logging.Logger;


public class VideoPlayerRenderer extends BaseResourceRenderer {
    private static Logger log = Logger.getLogger(VideoPlayerRenderer.class.getName());


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        IResponseWriter writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        String clientId = uiComponent.getClientId(facesContext);
        VideoPlayer video = (VideoPlayer) uiComponent;
        String mimeType = video.getType();
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
        if (srcAttribute!=null){
            video.setSrcAttribute(srcAttribute);
        }
 /*       writer.startElement(HTML.SPAN_ELEM, uiComponent);
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

        writer.writeAttribute("src", srcAttribute, null);
        writer.endElement("video");
        ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
        if (!client.isIOS() && video.getLinkLabel() != null)  {
            writer.startElement("br", uiComponent);
            writer.endElement("br");
            writer.startElement("a", uiComponent);
            writer.writeAttribute("target", "_blank", null);
            writer.writeAttribute("href", srcAttribute, null);
            writer.writeText(video.getLinkLabel(), null);
            writer.endElement("a");
        } */
        VideoPlayerCoreRenderer renderer = new VideoPlayerCoreRenderer();
        renderer.encodeEnd(video, writer);
    }



}
