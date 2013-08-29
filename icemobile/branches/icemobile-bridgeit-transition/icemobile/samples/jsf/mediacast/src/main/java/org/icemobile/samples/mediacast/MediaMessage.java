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

package org.icemobile.samples.mediacast;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MediaMessage implements Serializable {

    private static final Logger logger =
            Logger.getLogger(MediaMessage.class.toString());

    private String title;
    private String description;
    private List<String> tags = new ArrayList<String>(); 
    private String url;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private double direction = (360 * Math.random()); //0-359 degrees

    
    private Media smallPhoto = null;
    private Media mediumPhoto = null;
    private Media largePhoto = null;
    private Media videoMedia = null;
    private Media audioMedia = null;
    private Media videoThumbnailSmall = null;
    private Media videoThumbnailMed = null;
    private File photoFile;
    
    public static final String MEDIA_TYPE_PHOTO = "Photo";
    public static final String MEDIA_TYPE_VIDEO = "Video";
    public static final String MEDIA_TYPE_AUDIO = "Audio";
    
    public Media getSmallPhoto() {
        return smallPhoto;
    }

    public void addSmallPhoto(Media photo) {
        smallPhoto = photo;
    }

    public Media getMediumPhoto() {
        return mediumPhoto;
    }

    public void addMediumPhoto(Media photo) {
        mediumPhoto = photo;
    }

    public void setPhotoFile(File cameraFile){
        photoFile = cameraFile;
    }

    public Media getLargePhoto() {
        return largePhoto;
    }

    public void addLargePhoto(Media photo) {
        largePhoto = photo;
    }

    public void setVideoMedia(Media video)  {
        this.videoMedia = video;
    }
    
    public void setAudioMedia(Media audio)  {
        this.audioMedia = audio;
    }

    public Media getVideo()  {
        return videoMedia;
    }

    public Media getAudio()  {
        return audioMedia;
    }
    
    public boolean getShowAudio()  {
        return audioMedia != null;
    }

    public boolean getShowVideo()  {
    	 return videoMedia != null;
    }

    public boolean getShowPhoto()  {
    	 return photoFile != null || largePhoto != null;
    }

    /* PB where was this used?
    public String getMediaURL()  {
        ExternalContext externalContext = FacesContext
                .getCurrentInstance().getExternalContext();
        String rootPath = externalContext.getRealPath("/");
        String absolutePath = mediaFile.getAbsolutePath();
        String urlPath = absolutePath.substring(rootPath.length() - 1);
        return urlPath;
    }*/

    public Media getIcon()  {
        Media iconMedia = null;
        iconMedia = getMediumPhoto();
        if (null != iconMedia)  {
            return iconMedia;
        }
        iconMedia = getVideoThumbnail();
        if (null != iconMedia)  {
            return iconMedia;
        }
        if (null != getAudio())  {
            return MediaHelper.SOUND_ICON;
        }
        return MediaHelper.BROKEN_ICON;
    }

    public String getIconURL()  {
        return getIcon().getData().getURL().toString();
    }

    public double getLat()  {
        return latitude;
    }
    
    public void setLat(double lat){
    	this.latitude = lat;
    }

    public double getLon()  {
        return longitude;
    }
    
    public void setLon(double lon){
    	this.longitude = lon;
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getTags() {
		return tags;
	}
	
	public void setTags(List<String> tags){
		this.tags = tags;
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	/**
     * Clean up file resources.
     */
    public void dispose(){
        if (photoFile != null){
            boolean success = photoFile.delete();
            if (!success && logger.isLoggable(Level.FINE)){
                logger.fine("Could not dispose of photo file" + photoFile.getAbsolutePath());
            }
        }
        // try and clean up the data[], but only for real photos, we don't
        // want to delete the video and audio icons.
        if (getShowPhoto()){
            smallPhoto.dispose();
            mediumPhoto.dispose();
            largePhoto.dispose();
        }
    }
    
    public boolean isHasMedia(){
    	return this.audioMedia != null || this.photoFile != null || this.videoMedia != null;
    }
    
    public void setVideoThumbnailSmall(Media thumb){
    	this.videoThumbnailSmall = thumb;
    }
    
    public Media getVideoThumbnailSmall(){
    	return this.videoThumbnailSmall;
    }
    
    public void setVideoThumbnailMed(Media thumb){
    	this.videoThumbnailMed = thumb;
    }
    
    public Media getVideoThumbnail(){
    	return this.videoThumbnailMed;
    }
    
    public String toString(){
    	return String.format("%s audioMedia=%s, description=%s, direction=%s, " +
    			"largePhoto=%s, lat=%s, lon=%s, mediumPhoto=%s, photoFile=%s, smallPhoto%s, " +
    			"tags=%s, title=%s, url=%s, videoMedia=%s, videoThumbnailMed=%s, " +
    			"videoThumbnailSmall=%s", this.getClass().getSimpleName(), audioMedia,
    			description, direction, largePhoto, latitude, longitude, mediumPhoto, photoFile,
    			smallPhoto, tags, title, url, videoMedia, videoThumbnailMed, videoThumbnailSmall);
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
