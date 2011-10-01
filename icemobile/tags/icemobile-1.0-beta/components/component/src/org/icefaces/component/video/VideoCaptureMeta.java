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
import org.icefaces.component.annotation.Expression;
import org.icefaces.component.annotation.Implementation;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.baseMeta.UIComponentBaseMeta;


import javax.el.MethodExpression;
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

    @Property(defaultValue = "Integer.MIN_VALUE",
            tlddoc = "int value of maxwidth for video size in container and passed as a parameter ")
    private int maxwidth;

    @Property(defaultValue = "Integer.MIN_VALUE",
            tlddoc = "int value of maxheight passed as a parameter to device")
    private int maxheight;

    @Property(tlddoc = "as per specs the image information is stored in a Map")
    private Map<String, Object> value;

    @Property(defaultValue="false", tlddoc="The default value of this attribute is false. If true then value change event will happen in APPLY_REQUEST_VALUES phase and if the value of this attribute is false then event change will happen in INVOKE_APPLICATION phase")
    private boolean immediate;

    @Property(expression= Expression.METHOD_EXPRESSION, methodExpressionArgument="javax.faces.event.ValueChangeEvent",
    	    tlddoc = "MethodExpression representing a value change listener method that will be notified when a file has " +
    	            "been uploaded with a valid file size > 0. The expression must evaluate to a public method that takes a " +
    	            "ValueChangeEvent  parameter, with a return type of void, or to a public method that takes no arguments " +
    	            "with a return type of void. ")
    private MethodExpression valueChangeListener;

}
