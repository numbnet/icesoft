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

package org.icemobile.renderkit;

import org.icemobile.component.IVideoPlayer;
import org.icemobile.util.ClientDescriptor;
import static org.icemobile.util.HTML.*;
import java.io.IOException;
import java.util.logging.Logger;


public class VideoPlayerCoreRenderer extends BaseCoreRenderer{
    public static final String VIDEO_CLASS = "mobi-video";
    private static final Logger logger =
            Logger.getLogger(VideoPlayerCoreRenderer.class.toString());

    public void encodeEnd(IVideoPlayer video, IResponseWriter writer)
            throws IOException {

        String clientId = video.getClientId();
        ClientDescriptor client = video.getClient();

        writer.startElement(SPAN_ELEM, video);
        writer.writeAttribute(ID_ATTR, clientId);
        // apply style class
        writeStandardLayoutAttributes(writer, video, VIDEO_CLASS );
        writer.startElement("video", video);
        if (video.isLoop()){
            writer.writeAttribute("loop", "loop");
        }
         //   srcAttribute = writeCommonAttributes(writer, video, facesContext, clientId);
        String preloadVal = "auto";
        if (isStringAttributeEmpty(video.getPreload())){
            String preload = video.getPreload();
            if (preload.toLowerCase().trim().equals("none")){
                preloadVal = "none";
            }
            if (preload.toLowerCase().trim().equals("metadata")){
                preloadVal="metadata";
            }
        }
        if (video.isPlaysinline()) {
            writer.writeAttribute("webkit-playsinline", "yes");
        }
        writer.writeAttribute("preload", preloadVal);
        if (video.getHeight() > 0) {
            writer.writeAttribute("height", String.valueOf(video.getHeight()));
        }
        if (video.getWidth() > 0) {
            writer.writeAttribute("width", String.valueOf(video.getWidth()));
        }
        if (video.isControls()){
            writer.writeAttribute("controls", "controls");
        }
        if (!isStringAttributeEmpty(video.getType())) {
            writer.writeAttribute("type", video.getType());
        }
        if (video.isMuted()){
            writer.writeAttribute("muted", "muted");
        }
        String srcAttribute = video.getSrcAttribute();
        if (srcAttribute==null){
            srcAttribute = video.getUrl();
        }
        if (!isStringAttributeEmpty(video.getPoster())){
            writer.writeAttribute("poster", video.getPoster());
        }
        writer.writeAttribute("src", srcAttribute);
        writer.endElement("video");
        // write inline image link
        if (!client.isIOS() && video.getLinkLabel() != null)  {
            writer.startElement("br", video);
            writer.endElement("br");
            writer.startElement("a", video);
            writer.writeAttribute("target", "_blank");
            writer.writeAttribute("href", srcAttribute);
            writer.writeText(video.getLinkLabel());
            writer.endElement("a");
        }
        writer.endElement(SPAN_ELEM);
    }

}
