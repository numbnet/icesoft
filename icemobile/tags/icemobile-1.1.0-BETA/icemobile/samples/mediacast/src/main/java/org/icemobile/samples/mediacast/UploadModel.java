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

package org.icemobile.samples.mediacast;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * UploadModel stores files uploaded via the new mobile components before
 * they are added to the ImageStore.  The bean also adds the current
 * session to the "mobi" render group so that it can receive push updates when
 * a new file is uploaded by other users.
 */
@ManagedBean(name = UploadModel.BEAN_NAME)
@SessionScoped
public class UploadModel implements Serializable {

    private static final Logger logger =
            Logger.getLogger(UploadModel.class.toString());

    public static final String BEAN_NAME = "uploadModel";

    // media input mode.
    private String selectedMediaInput = "";

    private Map mediaMap;
    // uploaded file
    private File cameraFile;
    private File audioFile;
    private File videoFile;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private double direction = -1.0;

    // select a photo to view in detail.
    private transient MediaMessage selectedPhoto;

    private String title;
    private String description;

    // upload error message
    private String uploadErrorMessage;
    
    private String selectedLocation;

    public String getSelectedLocation() {
		return selectedLocation;
	}

	public void setSelectedLocation(String selectedLocation) {
		this.selectedLocation = selectedLocation;
	}

	public UploadModel() {
        mediaMap = new HashMap<String, Object>();
    }

    public void setAudio(Map audio) {
        audioFile = (File) audio.get(MediaController.MEDIA_FILE_KEY);
        mediaMap = audio;
        if (audioFile != null) {
            logger.fine("Retrieved Audio File");
            // try for a little clean up after
            audioFile.deleteOnExit();
        }
    }

    public void setVideo(Map video) {
        videoFile = (File) video.get(MediaController.MEDIA_FILE_KEY);
        mediaMap = video;
        if (videoFile != null) {
            logger.fine(this + " Retrieved Video File");
            // try for a little clean up after
            videoFile.deleteOnExit();
        }
    }

    /**
     * Accept the uploaded Image file and any metadata
     *
     * @param mediaMap mediaMap map
     */
    public void setMediaMap(Map mediaMap) {
        this.mediaMap = mediaMap;
        File imageFile = (File) mediaMap.get(MediaController.MEDIA_FILE_KEY);

        if (imageFile != null) {
            logger.fine("Retrieved Camera Image adding to ImageStore");
            // try for a little clean up after
            imageFile.deleteOnExit();
        }
        else{
        	logger.warning("image file is null");
        }
        cameraFile = imageFile;
    }

    public Map getMediaMap() {
        return mediaMap;
    }

    public MediaMessage getSelectedPhoto() {
        return selectedPhoto;
    }

    public void setSelectedPhoto(MediaMessage selectedPhoto) {
        this.selectedPhoto = selectedPhoto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getCameraFile() {
        return cameraFile;
    }

    public void setCameraFile(File cameraFile) {
        this.cameraFile = cameraFile;
    }

    public File getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(File videoFile) {
        this.videoFile = videoFile;
    }

    public File getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(File audioFile) {
        this.audioFile = audioFile;
    }

    public String getSelectedMediaInput() {
        return selectedMediaInput;
    }

    public void setSelectedMediaInput(String selectedMediaInput) {
        this.selectedMediaInput = selectedMediaInput;
    }

    public boolean getShowCamera() {
        return MediaMessage.MEDIA_TYPE_PHOTO.equals(selectedMediaInput);
    }

    public boolean getShowCamcorder() {
        return MediaMessage.MEDIA_TYPE_VIDEO.equals(selectedMediaInput);
    }

    public boolean getShowMicrophone() {
        return MediaMessage.MEDIA_TYPE_AUDIO.equals(selectedMediaInput);
    }

    public String getUploadErrorMessage() {
        return uploadErrorMessage;
    }

    public void setUploadErrorMessage(String uploadErrorMessage) {
        this.uploadErrorMessage = uploadErrorMessage;
    }
    
    public double getLatitude()  {
        return latitude;
    }

    public void setLatitude(double latitude)  {
        this.latitude = latitude;
    }

    public double getLongitude()  {
        return longitude;
    }

    public void setLongitude(double longitude)  {
        this.longitude = longitude;
    }
    
    public String getDescription(){
    	return this.description;
    }
    
    public void setDescription(String description){
    	this.description = description;
    }
    
    public double getDirection(){
    	return direction;
    }
    
    public void setDirection(double direction){
    	this.direction = direction;
    }
    
    public String toString(){
    	return String.format("UploadModel: title=%s, description=%s, lat=%s, long=%s, direction=%s, video file=%s, audio=%s, camera=%s",
    			this.getTitle(), this.getDescription(), this.getLatitude(), this.getLongitude(), this.direction, this.videoFile != null ? videoFile.getName() : "null", 
    			audioFile != null ? audioFile.getName() : "null", cameraFile != null ? cameraFile.getName() : "null");
    }
}
