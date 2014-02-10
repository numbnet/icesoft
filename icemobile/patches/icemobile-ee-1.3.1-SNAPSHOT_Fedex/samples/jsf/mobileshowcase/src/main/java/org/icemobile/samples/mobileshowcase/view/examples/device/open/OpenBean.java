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

package org.icemobile.samples.mobileshowcase.view.examples.device.open;

import org.icemobile.samples.mobileshowcase.util.FacesUtils;
import org.icemobile.samples.mobileshowcase.view.examples.device.DeviceInput;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.annotation.PreDestroy;
import javax.faces.application.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Open Bean demonstrates how a mobile device can download and
 * open a file in a matching App on the device.
 */
@Destination(
        title = "example.device.open.destination.title.short",
        titleExt = "example.device.open.destination.title.long",
        titleBack = "example.device.open.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/device/open-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/device/open-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "open-example.xhtml",
                        resource = "/WEB-INF/includes/examples/device/open-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "MicrophoneBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/device/open/OpenBean.java")
        }
)
@ManagedBean(name = OpenBean.BEAN_NAME)
@SessionScoped
public class OpenBean extends ExampleImpl<OpenBean> implements
        Serializable {

    private static final Logger logger =
            Logger.getLogger(OpenBean.class.toString());

    public static final String BEAN_NAME = "openBean";

    public OpenBean() {
        super(OpenBean.class);
    }

}
