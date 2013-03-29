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

package org.icemobile.samples.mobileshowcase.view.examples.media.image;


import org.icemobile.samples.mobileshowcase.view.examples.device.DeviceInput;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.application.Resource;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ImageBean shows how to use the new  <mobi:graphicImage /> component to
 * load an image from a byte[] or image stream.  The intent of the component
 * and example is to show how a image resource can be loaded from a dataSource
 * other then the server file system.
 */
@Destination(
        title = "example.media.image.destination.title.short",
        titleExt = "example.media.image.destination.title.long",
        titleBack = "example.media.image.destination.title.back",
        contentPath = "/WEB-INF/includes/examples/media/image.xhtml"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/media/image-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/media/image-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "image-example.xhtml",
                        resource = "/WEB-INF/includes/examples/media/image-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "ImageBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/media/image/ImageBean.java")
        }
)

@ManagedBean(name = ImageBean.BEAN_NAME)
@ApplicationScoped
public class ImageBean extends ExampleImpl<ImageBean> implements
        Serializable {

    public static final String BEAN_NAME = "imageBean";

    private static final Logger logger =
            Logger.getLogger(ImageBean.class.toString());

    private byte[] imageByteData;
    private Resource imageResourceData;

    public ImageBean() {
        super(ImageBean.class);
    }

    /**
     * Populates the byte[] and Resource object from a file stored on the server.
     */
    @PostConstruct
    public void readyImageData() {
        // read annotations meta data as we override @PostConsturct.
        initMetaData();

        // load the sample image file
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String sampleImagePath = facesContext.getExternalContext()
                .getRealPath("/resources/images/icemobile_large.png");
        File imageFile = new File(sampleImagePath);

        // create new resource object that the graphicImageComponent can use.
        try {
            imageByteData = DeviceInput.createByteArray(imageFile);
        } catch (IOException ex) {
            logger.log(Level.WARNING,
                    "Error loading graphicImage image from byte[].", ex);
        }
        // create a new resource object that the graphicImageComponent can use.
        try {
            imageResourceData = DeviceInput.createResourceObject(imageFile,
                    UUID.randomUUID().toString(), "image/png");
        } catch (IOException ex) {
            logger.log(Level.WARNING,
                    "Error loading graphicImage image from Resource.", ex);
        }
    }

    public byte[] getImageByteData() {
        return imageByteData;
    }

    public Resource getImageResourceData() {
        return imageResourceData;
    }
}