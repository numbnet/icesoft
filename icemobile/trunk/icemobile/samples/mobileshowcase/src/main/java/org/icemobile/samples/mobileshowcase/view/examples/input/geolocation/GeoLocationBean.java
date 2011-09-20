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

package org.icemobile.samples.mobileshowcase.view.examples.input.geolocation;


import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@Destination(
        title = "example.input.geolocation.destination.title.short",
        titleExt = "example.input.geolocation.destination.title.long",
        titleBack = "example.input.geolocation.destination.title.back",
        contentPath = "/WEB-INF/includes/examples/input/geolocation.xhtml"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/input/geolocation-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/input/geolocation-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "geolocation-example.xhtml",
                        resource = "/WEB-INF/includes/examples/input/geolocation-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "GeoLocationBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/input/geolocation/GeoLocationBean.java")
        }
)

@ManagedBean(name = GeoLocationBean.BEAN_NAME)
@SessionScoped
public class GeoLocationBean extends ExampleImpl<GeoLocationBean> implements
        Serializable {

    public static final String BEAN_NAME = "geoLocationBean";

    private double latitude = 0.0;
    private double longitude = 0.0;

    public GeoLocationBean() {
        super(GeoLocationBean.class);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLongitudeRead() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

     public double getLatitudeRead() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    // lat and long values are read only, no value writing takes place.
    public void setLongitudeRead(double longitude) {
    }
    public void setLatitudeRead(double latitude) {
    }
}