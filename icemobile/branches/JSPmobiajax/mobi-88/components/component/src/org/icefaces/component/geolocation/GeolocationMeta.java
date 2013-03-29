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

@Component(
        tagName = "geolocation",
        componentClass = "org.icefaces.component.geolocation.Geolocation",
        rendererClass = "org.icefaces.component.geolocation.GeolocationRenderer",
        generatedClass = "org.icefaces.component.geolocation.GeolocationBase",
        componentType = "org.icefaces.Geolocation",
        rendererType = "org.icefaces.GeolocationRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.Geolocation",
        tlddoc = "This mobility component captures an geolocation object" +
                " of longitude and latitude, heading, speed and altitude via" +
                " html5 navigator api"
)

@ResourceDependencies({
})
public class GeolocationMeta extends UIComponentBaseMeta {

    @Property(tlddoc = "latitude of mobile device in decimal degrees")
    private Double latitude;

    @Property(tlddoc = "longitude of mobile device in decimal degrees")
    private Double longitude;

    @Property(tlddoc = "altitude of mobile device in decimal degrees")
    private Double altitude;

    @Property(tlddoc = "direction of travel mobile device in degrees" +
            " where 0<=heading<360 degrees counting clockwise relative to true north")
    private Double heading;

    @Property(tlddoc = "current ground speed of mobile device in meters per second")
    private Double speed;

    @Property(defaultValue = "0", tlddoc = "code of error message where 1 is PERMISSION_DENIED, 2 is POSIITON_UNAVAILABLE" +
            " 3 is TIMEOUT")
    private int messageCode;

    @Property(defaultValue = "false",
            tlddoc = "When disabled, geolocation is not activated")
    private boolean disabled;

    @Property(tlddoc = "tabindex of the component")
    private Integer tabindex;

    @Property(tlddoc = "style will be rendered on the root element of this " +
            "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
            "this component.")
    private String styleClass;

    @Property(defaultValue = "false",
            tlddoc = "When singleSubmit is true, changing the value of this component" +
                    " will submit and execute this component only. Equivalent to " +
                    " execute=\"@this\" render=\"@all\" of the f ajax tag. " +
                    "When singleSubmit is false, no submit occurs. The value is simply  " +
                    "stored in the hidden field. The default value is false.")
    private boolean singleSubmit;

}
