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


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


@Component(
        tagName = "videoPlayer",
        componentClass = "org.icefaces.mobi.component.video.VideoPlayer",
        rendererClass = "org.icefaces.mobi.component.video.VideoPlayerRenderer",
        generatedClass = "org.icefaces.mobi.component.video.VideoPlayerBase",
        componentType = "org.icefaces.VideoPlayer",
        rendererType = "org.icefaces.VideoPlayerRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.VideoPlayer",
        tlddoc = "videoPlayer renders a video player to play back videos."
)


@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class VideoPlayerMeta extends UIComponentBaseMeta {

    @Property(defaultValue = "auto", tlddoc = "Determies whether video source is preloaded. Accepted values are auto,none or metadata;  " +
            "where metadata means preload just the metadata, auto leaves the browser to " +
	      "decide preload strategy, and none performs no preloading")
    private String preload;

    @Property(defaultValue = "true", tlddoc = "Determines if the controls are displayed.")
    private boolean controls;

    @Property(tlddoc = "The value of the video resource.  Can be byte[], filename string, or Resource")
    private Object value;

    @Property(defaultValue = "false", tlddoc = "Specifies if the file should be " +
            "played repeatedly.")
    private boolean loop;

    @Property(tlddoc = "Allows webkit supported browsers to play back the video inline rather than a new window.")
    private boolean playsinline;

    @Property(defaultValue = "false",
            tlddoc = org.icefaces.mobi.utils.TLDConstants.DISABLED)
    private boolean disabled;

    @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.TABINDEX)
    private int tabindex;

     @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLE)
     private String style;

     @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLECLASS)
     private String styleClass;

    @Property(tlddoc = "The name used for registering resources.")
    private String name;

    @Property(tlddoc = "The URL of video stream to play.")
    private String url;

    /* TODO    @Property(tlddoc = "not implemented, display a static photo until the video is played") 
       private String poster; */

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "The maximum width of video area.")
    private int width;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "The maximum height of video area.")
    private int height;

    @Property(defaultValue = "session", tlddoc = "Deterimes the scope of the video resource.")
    private String scope;

    @Property( tlddoc = "The contentType or mimeType for the video file.")
    private String type;

    @Property( tlddoc = "The label on a link that launches a system video player.")
    private String linkLabel;
}
