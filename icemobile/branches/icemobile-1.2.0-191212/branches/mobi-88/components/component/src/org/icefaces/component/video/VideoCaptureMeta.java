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

import java.util.Map;


@Component(
        tagName = "camcorder",
        componentClass = "org.icefaces.component.video.VideoCapture",
        rendererClass = "org.icefaces.component.video.VideoCaptureRenderer",
        generatedClass = "org.icefaces.component.video.VideoCaptureBase",
        componentType = "org.icefaces.VideoCapture",
        rendererType = "org.icefaces.VideoCaptureRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.VideoCapture",
        tlddoc = "This mobility component captures a video file via a mobile device video capture" +
                " using the ICEfaces mobility container, and stores this file on the server. "
)


public class VideoCaptureMeta extends UIComponentBaseMeta {

    @Property(tlddoc = "as per specs the video clip information is stored in a Map")
    private Map<String, Object> value;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "length of audio clip in seconds")
    private int maxtime;

    @Property(defaultValue = "false",
            tlddoc = "When disabled, files are not selectable for upload.")
    private boolean disabled;

    @Property(tlddoc = "tabindex of the component")
    private int tabindex;

    @Property(tlddoc = "style will be rendered on the root element of this " +
            "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
            "this component.")
    private String styleClass;


    @Property(defaultValue = "2097152", tlddoc = "find out from Ted what this represents")
    private int maxK;

    @Property(defaultValue = "Integer.MIN_VALUE",
            tlddoc = "int value of maxwidth for video size in container and passed as a parameter ")
    private int maxwidth;

    @Property(defaultValue = "Integer.MIN_VALUE",
            tlddoc = "int value of maxheight passed as a parameter to device")
    private int maxheight;

    @Property(defaultValue = "mp4", tlddoc = "format of video")
    private String format;

    @Property(defaultValue = "mp4", tlddoc = "encoding of video")
    private String encoding;

}
