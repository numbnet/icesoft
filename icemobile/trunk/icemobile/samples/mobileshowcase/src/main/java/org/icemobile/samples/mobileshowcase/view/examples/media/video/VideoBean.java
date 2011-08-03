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

package org.icemobile.samples.mobileshowcase.view.examples.media.video;


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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.UUID;

@Destination(
        title = "example.media.video.destination.title.short",
        titleExt = "example.media.video.destination.title.long",
        titleBack = "example.media.video.destination.title.back",
        contentPath = "/WEB-INF/includes/examples/media/video.xhtml"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/media/video-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/media/video-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "video-example.xhtml",
                        resource = "/WEB-INF/includes/examples/media/video-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "VideoBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/media/video/VideoBean.java")
        }
)

@ManagedBean(name = VideoBean.BEAN_NAME)
@ApplicationScoped
public class VideoBean extends ExampleImpl<VideoBean> implements
        Serializable {

    public static final String BEAN_NAME = "videoBean";

    private static final Logger logger =
            Logger.getLogger(VideoBean.class.toString());

    private byte[] videoByteData;
    private Resource videoResourceData;

    public VideoBean() {
        super(VideoBean.class);
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
        String sampleAudioPath = facesContext.getExternalContext()
                .getRealPath("/resources/video/icesoft_mobile_push.mp4");
        File videoFile = new File(sampleAudioPath);

        // create new resource object that the graphicImageComponent can use.
        try {
            videoByteData = DeviceInput.createByteArray(videoFile);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Error loading video from byte[].", ex);
        }
        // create a new resource object that the graphicImageComponent can use.
        try {
            videoResourceData = DeviceInput.createResourceObject(videoFile,
                    UUID.randomUUID().toString(), "video/mp4");
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Error loading video from Resource.", ex);
        }
    }

    public byte[] getVideoByteData() {
        return videoByteData;
    }

    public Resource getVideoResourceData() {
        return videoResourceData;
    }
}
