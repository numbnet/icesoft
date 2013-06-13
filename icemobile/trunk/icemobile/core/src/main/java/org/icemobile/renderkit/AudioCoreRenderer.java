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

package org.icemobile.renderkit;

import org.icemobile.component.IAudio;
import org.icemobile.util.ClientDescriptor;

import java.io.IOException;
import java.util.logging.Logger;

import static org.icemobile.util.HTML.*;

public class AudioCoreRenderer extends BaseCoreRenderer{
    public static final String AUDIO_CLASS = "mobi-audio";
    private static final Logger logger =
            Logger.getLogger(AudioCoreRenderer.class.toString());

    public void encodeEnd(IAudio audio, IResponseWriter writer)
            throws IOException {

        String clientId = audio.getClientId();
        ClientDescriptor client = audio.getClient();

        writer.startElement(SPAN_ELEM, audio);
        writer.writeAttribute(ID_ATTR, clientId);
        writeStandardLayoutAttributes(writer, audio, AUDIO_CLASS );
        // apply component style class and append any user defined classes.

        writer.startElement("audio", audio);

        if (audio.isLoop()){
            writer.writeAttribute("loop", "loop");
        }
        if (audio.isAutoPlay()){
            writer.writeAttribute("autoplay", "autoplay");
        }
         //   srcAttribute = writeCommonAttributes(writer, audio, facesContext, clientId);
        String preloadVal = "auto";
        if (!isStringAttributeEmpty(audio.getPreload())){
            String preload = audio.getPreload();
            if (preload.toLowerCase().trim().equals("none")){
                preloadVal = "none";
            }
            if (preload.toLowerCase().trim().equals("metadata")){
                preloadVal="metadata";
            }
        }
        writer.writeAttribute("preload", preloadVal);

        if (audio.isControls()){
            writer.writeAttribute("controls", "controls");
        }
        if (audio.isMuted()){
            writer.writeAttribute("muted", "muted");
        }
        String mimeType = audio.getType();
        if (!isStringAttributeEmpty(mimeType)){
          //  mimeType = "audio/mpeg"; //do we want a default type this seems to work best???
            writer.writeAttribute("type", mimeType);
        }
        String srcAttribute = audio.getSrcAttribute();
        if (null == srcAttribute){
            srcAttribute = audio.getUrl();
        }
        if (audio.isMuted()) {
            writer.writeAttribute("muted", "muted");
        }
        writer.writeAttribute("src", srcAttribute);
        writer.endElement("audio");
        // write inline image link
  //      logger.info("iOS ="+client.isIOS() +"linkLabel="+audio.getLinkLabel());
        if (!client.isIOS() && audio.getLinkLabel() != null)  {
            writer.startElement("br", audio);
            writer.endElement("br");
            writer.startElement("a", audio);
            writer.writeAttribute("target", "_blank");
            writer.writeAttribute("href", srcAttribute);
            writer.writeText(audio.getLinkLabel());
            writer.endElement("a");
        }
        writer.endElement(SPAN_ELEM);
    }

}
