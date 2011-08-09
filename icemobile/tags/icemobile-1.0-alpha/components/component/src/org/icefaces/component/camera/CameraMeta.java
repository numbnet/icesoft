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

package org.icefaces.component.camera;


import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;
import java.util.Map;

@Component(
        tagName = "camera",
        componentClass = "org.icefaces.component.camera.Camera",
        rendererClass = "org.icefaces.component.camera.CameraRenderer",
        generatedClass = "org.icefaces.component.camera.CameraBase",
        componentType = "org.icefaces.Camera",
        rendererType = "org.icefaces.CameraRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.Camera",
        tlddoc = "This mobility component captures an image via a mobile device camera" +
                " and stores it on the mobile device. "
)

@ResourceDependencies({
})
public class CameraMeta extends UIComponentBaseMeta {

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "maxium allowed width of image")
    private int maxwidth;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "maximum allowed height of image")
    private int maxheight;

    @Property(defaultValue = "false",
            tlddoc = "When disabled, files are not selectable for upload.")
    private boolean disabled;

    @Property(defaultValue = "0", tlddoc = "tabindex of the component")
    private int tabindex;

    @Property(tlddoc = "style will be rendered on the root element of this " +
            "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
            "this component.")
    private String styleClass;

    @Property(tlddoc = "as per specs the image information is stored in a Map")
    private Map<String, Object> value;

//does this component require singleSubmit???

}
