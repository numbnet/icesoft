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


import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;


@Component(
        tagName = "videoPlayer",
        componentClass = "org.icefaces.component.video.VideoPlayer",
        rendererClass = "org.icefaces.component.video.VideoPlayerRenderer",
        generatedClass = "org.icefaces.component.video.VideoPlayerBase",
        componentType = "org.icefaces.VideoPlayer",
        rendererType = "org.icefaces.VideoPlayerRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.VideoPlayer",
        tlddoc = "This mobility component renders a video Player to play back supported codecs for mobility devices"
)

@ResourceDependencies({
})
public class VideoPlayerMeta extends UIComponentBaseMeta {

    @Property(defaultValue = "auto", tlddoc = "whether to preload the file accepted values are auto,none or metadata,  " +
            "where metadata means preload just the metadata and auto leaves the broswer to " +
            "decide whether to preload the entire file")
    private String preload;

    @Property(defaultValue = "true", tlddoc = "whether the controls are used or not")
    private boolean controls;

    @Property(tlddoc = "value can be byte[] or String filename or Resource")
    private Object value;

    @Property(defaultValue = "false", tlddoc = "boolean which specifies whether the file should be " +
            "played repeatedly")
    private boolean loop;

    @Property(tlddoc = "allows webkit supported browsers to play back the video inline rather than a new window")
    private boolean playsinline;

    @Property(defaultValue = "false",
            tlddoc = "When disabled, audio is not activated")
    private boolean disabled;

    @Property(tlddoc = "tabindex of the component")
    private Integer tabindex;

    @Property(tlddoc = "style will be rendered on the root element of this " +
            "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
            "this component.")
    private String styleClass;

    @Property(tlddoc = "name is used for resource registering")
    private String name;

    @Property(tlddoc = "not sure if we are needing this yet or not")
    private String url;

    @Property(tlddoc = "have not implemented the poster yet but this appears as a static photo until the video plays")
    private String poster;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "maximum width of video area")
    private int width;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "maximum height of video area")
    private int height;

    @Property(defaultValue = "session", tlddoc = "support for resource based video which can be registered in various scopes")
    private String scope;

    @Property(defaultValue = "video/mp4", tlddoc = " contentType or mimeType for video file")
    private String type;
}
