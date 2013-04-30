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

package org.icemobile.samples.mobileshowcase.view.examples.device.reality;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.faces.application.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.imageio.ImageIO;

import org.icefaces.application.ResourceRegistry;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.samples.mobileshowcase.view.examples.device.DeviceInput;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.Destination;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.Example;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.ExampleResource;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.ExampleResources;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.ResourceType;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;


@Destination(
        title = "example.device.reality.destination.title.short",
        titleExt = "example.device.reality.destination.title.long",
        titleBack = "example.device.reality.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/device/realitylocations-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/device/realitylocations-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "reality-example.xhtml",
                        resource = "/WEB-INF/includes/examples/device/realitylocations-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "RealityBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/device/reality/RealityBean.java")
        }
)
@ManagedBean(name = RealityLocationsBean.BEAN_NAME)
@SessionScoped
public class RealityLocationsBean extends ExampleImpl<RealityLocationsBean> implements
        Serializable {

    private static final Logger logger =
            Logger.getLogger(RealityBean.class.toString());

    public static final String BEAN_NAME = "realityLocationsBean";
    public RealityLocationsBean() {
        super(RealityLocationsBean.class);
    }
}

