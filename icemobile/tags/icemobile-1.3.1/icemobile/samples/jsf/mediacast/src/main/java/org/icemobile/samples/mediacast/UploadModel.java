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

package org.icemobile.samples.mediacast;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
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
    private String selectedMediaInput = null;

    private Map<String, Object> videoUploadMap = new HashMap<String, Object>();
    private Map<String, Object> audioUploadMap = new HashMap<String, Object>();
    private Map<String, Object> photoUploadMap = new HashMap<String, Object>();
    
    // uploaded file
    private File cameraFile;
    private File audioFile;
    private File videoFile;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private double direction = -1.0;
    
    // select a photo to view in detail.
    private transient MediaMessage currentMediaMessage = new MediaMessage();

    private String tags;

    private String uploadFeedbackMessage;
    
    private String selectedLocation;
    
    private String url;

    public String getSelectedLocation() {
		return selectedLocation;
	}

	public void setSelectedLocation(String selectedLocation) {
		this.selectedLocation = selectedLocation;
	}

	public void setAudioUploadMap(Map<String, Object> map) {
    	audioUploadMap = map;
        audioFile = (File) audioUploadMap.get(MediaController.MEDIA_FILE_KEY);
        if (audioFile != null) {
        	logger.fine("Retrieved Audio File");
            // try for a little clean up after
            audioFile.deleteOnExit();
        }
    }
	
	public Map<String, Object> getAudioUploadMap(){
		return audioUploadMap;
	}

    public void setVideoUploadMap(Map<String, Object> map) {
    	videoUploadMap = map;
        videoFile = (File) videoUploadMap.get(MediaController.MEDIA_FILE_KEY);
        if (videoFile != null) {
            logger.fine(this + " Retrieved Video File");
            // try for a little clean up after
            videoFile.deleteOnExit();
        }
    }
    
    public Map<String, Object> getVideoUploadMap(){
    	return videoUploadMap;
    }

    public void setPhotoUploadMap(Map<String, Object> map) {
        photoUploadMap = map;
        File imageFile = (File) photoUploadMap.get(MediaController.MEDIA_FILE_KEY);

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
    
    public Map<String, Object> getPhotoUploadMap(){
    	return photoUploadMap;
    }

    public MediaMessage getCurrentMediaMessage() {
        return currentMediaMessage;
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

    public boolean isShowCamera() {
        return MediaMessage.MEDIA_TYPE_PHOTO.equals(selectedMediaInput);
    }

    public boolean isShowCamcorder() {
        return MediaMessage.MEDIA_TYPE_VIDEO.equals(selectedMediaInput);
    }

    public boolean isShowMicrophone() {
        return MediaMessage.MEDIA_TYPE_AUDIO.equals(selectedMediaInput);
    }

    public String getUploadFeedbackMessage() {
        return uploadFeedbackMessage;
    }

    public void setUploadFeedbackMessage(String message) {
        this.uploadFeedbackMessage = message;
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
    
    public double getDirection(){
    	return direction;
    }
    
    public void setDirection(double direction){
    	this.direction = direction;
    }
    
    public String getTags(){
    	return this.tags;
    }
    
    public void setTags(String tags){
    	this.tags = tags;
    }
    
    public String toString(){
    	return String.format("UploadModel: lat=%s, long=%s, direction=%s, video file=%s, audio=%s, camera=%s",
    			this.getLatitude(), this.getLongitude(), this.direction, this.videoFile != null ? videoFile.getName() : "null", 
    			audioFile != null ? audioFile.getName() : "null", cameraFile != null ? cameraFile.getName() : "null");
    }
    
    public void clearCurrentMediaMessage(){
    	this.currentMediaMessage = new MediaMessage();
    	this.audioFile = null;
    	this.cameraFile = null;
    	this.direction = -1.0;
    	this.latitude = 0.0;
    	this.longitude = 0.0;
    	this.photoUploadMap = new HashMap<String, Object>();;
    	this.videoUploadMap = new HashMap<String, Object>();;
    	this.audioUploadMap = new HashMap<String, Object>();;
    	this.selectedLocation = null;
    	this.tags = null;
    	this.uploadFeedbackMessage = null;
    	this.videoFile = null;
    	this.selectedMediaInput = null;
    	this.url = null;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
