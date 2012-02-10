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

package org.icemobile.samples.mobileshowcase.view.examples.media.audio;

/**
 *
 */

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
        title = "example.media.audio.destination.title.short",
        titleExt = "example.media.audio.destination.title.long",
        titleBack = "example.media.audio.destination.title.back",
        contentPath = "/WEB-INF/includes/examples/media/audio.xhtml"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/media/audio-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/media/audio-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "audio-example.xhtml",
                        resource = "/WEB-INF/includes/examples/media/audio-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "AudioBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/media/audio/AudioBean.java")
        }
)

@ManagedBean(name = AudioBean.BEAN_NAME)
@ApplicationScoped
public class AudioBean extends ExampleImpl<AudioBean> implements
        Serializable {

    public static final String BEAN_NAME = "audioBean";

    private static final Logger logger =
            Logger.getLogger(AudioBean.class.toString());

    private byte[] audioByteData;
    private Resource audioResourceData;

    public AudioBean() {
        super(AudioBean.class);
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
                .getRealPath("/resources/audio/audio_clip.mp3");
        File audioFile = new File(sampleAudioPath);

        // create new resource object that the graphicImageComponent can use.
        try {
            audioByteData = DeviceInput.createByteArray(audioFile);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Error loading audio from byte[].", ex);
        }
        // create a new resource object that the graphicImageComponent can use.
        try {
            audioResourceData = DeviceInput.createResourceObject(audioFile,
                    UUID.randomUUID().toString(), "audio/mpeg");
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Error loading audio from Resource.", ex);
        }
    }

    public byte[] getAudioByteData() {
        return audioByteData;
    }

    public Resource getAudioResourceData() {
        return audioResourceData;
    }
}
