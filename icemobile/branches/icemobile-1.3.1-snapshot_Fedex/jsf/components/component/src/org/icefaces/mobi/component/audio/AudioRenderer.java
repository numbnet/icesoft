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
package org.icefaces.mobi.component.audio;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.component.video.VideoPlayer;
import org.icefaces.mobi.renderkit.BaseResourceRenderer;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icefaces.mobi.utils.PassThruAttributeWriter;
import org.icemobile.renderkit.AudioCoreRenderer;
import org.icemobile.renderkit.IResponseWriter;
import org.icemobile.component.IAudio;
import org.icemobile.util.ClientDescriptor;


public class AudioRenderer extends BaseResourceRenderer {
    private static final Logger log = Logger.getLogger(AudioRenderer.class.getName());


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        IResponseWriter writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        Audio audio = (Audio) uiComponent;
        String clientId = audio.getClientId();
        Object audioObject = audio.getValue();
        String scope = audio.getScope().toLowerCase().trim();
        if (!scope.equals("flash") && !(scope.equals("window")) && !(scope.equals("application"))
                && (!scope.equals("request")) && (!scope.equals("view"))) {
            scope = "session";
        }
        String name = audio.getName();
        if (null == name || name.equals("")) name = "audio" + clientId;
        String mimeType = audio.getType();
        if (null == mimeType){
            mimeType = "audio/mpeg"; //do we want a default type this seems to work best???
        }
        String srcAttribute = null;
        if (null != audioObject) {
            srcAttribute = processSrcAttribute(facesContext, audioObject, name, mimeType,
                    scope, audio.getUrl());
            if (srcAttribute!=null){
                audio.setSrcAttribute(srcAttribute);
            }
        }
        //IAudio icomp = IAudio(uiComponent);
        AudioCoreRenderer renderer = new AudioCoreRenderer();
        renderer.encodeEnd(audio, writer);
    }


}
