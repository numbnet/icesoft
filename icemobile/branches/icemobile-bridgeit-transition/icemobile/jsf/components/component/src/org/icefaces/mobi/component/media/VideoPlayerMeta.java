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

package org.icefaces.mobi.component.media;


import org.icefaces.ace.meta.annotation.*;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


@Component(
        tagName = "videoPlayer",
        componentClass = "org.icefaces.mobi.component.media.VideoPlayer",
        rendererClass = "org.icefaces.mobi.component.media.VideoPlayerRenderer",
        generatedClass = "org.icefaces.mobi.component.media.VideoPlayerBase",
        componentType = "org.icefaces.VideoPlayer",
        rendererType = "org.icefaces.VideoPlayerRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.VideoPlayer",
        tlddoc = "videoPlayer renders a video player to play back videos. " +
                "The component can be bound to a variety of source types. "
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class VideoPlayerMeta extends BaseMediaPlayerMeta {


    @Property(tlddoc = "Allows webkit supported browsers to play back the video inline rather than a new window.")
    private boolean playsinline;

    @Property(tlddoc = "display a static photo until the video is played")
    private String poster;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "The maximum width of video area.")
    private int width;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "The maximum height of video area.")
    private int height;
 }

