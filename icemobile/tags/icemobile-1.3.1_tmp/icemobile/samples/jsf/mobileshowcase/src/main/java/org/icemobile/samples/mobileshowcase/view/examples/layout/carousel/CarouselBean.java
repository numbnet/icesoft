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

package org.icemobile.samples.mobileshowcase.view.examples.layout.carousel;

import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
@Destination(
        title = "example.layout.carousel.destination.title.short",
        titleExt = "example.layout.carousel.destination.title.long",
        titleBack = "example.layout.carousel.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/layout/carousel-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/layout/carousel-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "carousel-example.xhtml",
                        resource = "/WEB-INF/includes/examples/layout/carousel-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "CarouselBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/layout/carousel/CarouselBean.java")
        }
)
@ManagedBean(name = CarouselBean.BEAN_NAME)
@SessionScoped
public class CarouselBean extends ExampleImpl<CarouselBean> implements
        Serializable {

    public static final String BEAN_NAME = "carouselBean";

    private ArrayList<String> images = new ArrayList<String>();
    private int selectedItem =2;

    public CarouselBean() {
        super(CarouselBean.class);

        images.add("flags/Argentina.png");
        images.add("flags/Brazil.png");
        images.add("flags/Canada.png");
        images.add("flags/Denmark.png");
        images.add("flags/France.png");
        images.add("flags/Germany.png");
        images.add("flags/Hong-Kong.png");
        images.add("flags/Italy.png");
        images.add("flags/Japan.png");
        images.add("flags/New-Zealand.png");
        images.add("flags/Romania.png");
        images.add("flags/Spain.png");
        images.add("flags/USA.png");
        images.add("flags/Venezuela.png");
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getSelected() {
        return images.get(selectedItem);
    }
}
