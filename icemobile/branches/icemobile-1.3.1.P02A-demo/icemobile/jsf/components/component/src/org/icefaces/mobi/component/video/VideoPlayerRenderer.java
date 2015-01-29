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

package org.icefaces.mobi.component.video;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.icefaces.mobi.renderkit.BaseResourceRenderer;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icemobile.renderkit.IResponseWriter;
import org.icemobile.renderkit.VideoPlayerCoreRenderer;


public class VideoPlayerRenderer extends BaseResourceRenderer {
    private static final Logger log = Logger.getLogger(VideoPlayerRenderer.class.getName());


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
        VideoPlayerCoreRenderer renderer = new VideoPlayerCoreRenderer();
        renderer.encodeEnd(video, writer);
    }



}
