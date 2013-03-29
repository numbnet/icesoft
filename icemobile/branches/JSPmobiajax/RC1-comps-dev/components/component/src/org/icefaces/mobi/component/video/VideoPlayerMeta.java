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
        tlddoc = "This mobility component renders a video Player to play back supported codecs for mobility devices"
)


@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class VideoPlayerMeta extends UIComponentBaseMeta {

    @Property(defaultValue = "auto", tlddoc = "whether to preload the file accepted values are auto,none or metadata;  " +
            "where metadata means preload just the metadata and auto leaves the browser to " +
            "decide whether to preload the entire file")
    private String preload;

    @Property(defaultValue = "true", tlddoc = "whether the controls are displayed or not")
    private boolean controls;

    @Property(tlddoc = "value can be byte[] or String filename or Resource")
    private Object value;

    @Property(defaultValue = "false", tlddoc = "boolean which specifies whether the file should be " +
            "played repeatedly")
    private boolean loop;

    @Property(tlddoc = "allows webkit supported browsers to play back the video inline rather than a new window")
    private boolean playsinline;

    @Property(defaultValue = "false",
            tlddoc = "boolean, when disabled, audio is not activated")
    private boolean disabled;

    @Property(tlddoc = "tabindex of the component")
    private int tabindex;

    @Property(tlddoc = "style will be rendered on the root element of this " +
            "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
            "this component.")
    private String styleClass;

    @Property(tlddoc = "name is used for resource registering")
    private String name;

    @Property(tlddoc = "url of video stream to play")
    private String url;

    @Property(tlddoc = "not implemented, display a static photo until the video is played")
    private String poster;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "maximum width of video area")
    private int width;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "maximum height of video area")
    private int height;

    @Property(defaultValue = "session", tlddoc = "support for resource based video which can be registered in various scopes")
    private String scope;

    @Property( tlddoc = "contentType or mimeType for video file")
    private String type;

    @Property( tlddoc = "Label for link that launches a system video player")
    private String linkLabel;
}
