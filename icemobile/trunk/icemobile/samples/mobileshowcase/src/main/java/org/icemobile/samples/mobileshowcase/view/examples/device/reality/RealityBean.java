/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

import org.icemobile.samples.mobileshowcase.util.FacesUtils;
import org.icemobile.samples.mobileshowcase.view.examples.device.DeviceInput;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.application.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;


@Destination(
        title = "example.device.reality.destination.title.short",
        titleExt = "example.device.reality.destination.title.long",
        titleBack = "example.device.reality.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/device/reality-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/device/reality-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "reality-example.xhtml",
                        resource = "/WEB-INF/includes/examples/device/reality-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "RealityBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/device/reality/RealityBean.java")
        }
)
@ManagedBean(name = RealityBean.BEAN_NAME)
@SessionScoped
public class RealityBean extends ExampleImpl<RealityBean> implements
        Serializable {

    private static final Logger logger =
            Logger.getLogger(RealityBean.class.toString());

    public static final String BEAN_NAME = "realityBean";

    public static final String FILE_KEY = "file";
    public static final String RELATIVE_PATH_KEY = "relativePath";
    public static final String CONTENT_TYPE_KEY = "contentType";

    private Map cameraImage = new HashMap();
    private File cameraFile;
    // uploaded video will be stored as a resource.
    private Resource outputResource;
    private String label;
    private double latitude = 0.0;
    private double longitude = 0.0;
    HashMap<String,RealityMessage> messages = new HashMap();

    // upload error message
    private String uploadMessage;

    public RealityBean() {
        super(RealityBean.class);
    }

    public void processUploadedImage(ActionEvent event) {
        outputResource = null;
        if (cameraImage != null &&
                cameraImage.get("contentType") != null &&
                ((String)cameraImage.get("contentType")).startsWith("image")) {
            // clean up previously upload file
            if (cameraFile != null){
                disposeResources();
            }
            cameraFile = (File)cameraImage.get(FILE_KEY);
            if (cameraFile != null) {
                // copy the bytes into the resource object.
                try {
                    outputResource = DeviceInput.createResourceObject(
                        cameraFile, UUID.randomUUID().toString(),
                        (String) cameraImage.get(CONTENT_TYPE_KEY));
                } catch (IOException ex) {
                    logger.warning("Error setting up video resource object");
                }
                uploadMessage = "Upload was successful";
            }
        }else{
            // create error message for users.
            uploadMessage = "The uploaded image file could not be correctly processed.";
        }
        // a null/empty object is used in the page to hide the audio
        // component.
        RealityMessage message = new RealityMessage();
        message.setTitle(label);
        message.setLocation(latitude + "," + longitude);
        message.setFileName(URLEncoder.encode(outputResource.getRequestPath()));
        messages.put(label, message);
    }

    @PreDestroy
    public void disposeResources(){
        boolean success = cameraFile.delete();
        if (!success && logger.isLoggable(Level.FINE)){
            logger.fine("Could not dispose of media file" + cameraFile.getAbsolutePath());
        }
    }

    public void setCameraImage(Map cameraImage) {

        this.cameraImage = cameraImage;

    }

    public Map getCameraImage() {
        return cameraImage;
    }

    public Resource getOutputResource() {
        return outputResource;
    }

    public String getUploadMessage() {
        return uploadMessage;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getParams() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        StringBuilder sb = new StringBuilder();
        String url = externalContext.getRequestScheme() + "://" +
            externalContext.getRequestServerName() + ":" + 
            externalContext.getRequestServerPort();
        sb.append("ub=" + URLEncoder.encode(url));
        for (RealityMessage message : messages.values())  {
            String location = message.getPacked() + ",," + 
                    message.getFileName();
            sb.append("&" + location);
        }

        return sb.toString();
    }
}

class RealityMessage {

    private String title;
    private String location;
    private String selection;
    private String fileName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPacked()  {
        return URLEncoder.encode(title) + "=" + location;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("properties ");
        sb.append("title=");
        sb.append("'").append(title).append("', ");
        sb.append("location=");
        sb.append("'").append(location).append("', ");
        return sb.toString();
    }
}
