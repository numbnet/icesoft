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

package org.icemobile.samples.spring.photofun;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class MediaMessage implements Serializable {

    private static final Logger logger =
            Logger.getLogger(MediaMessage.class.toString());

    private String title;
    private String id;
    private String description;
    private List<String> tags = new ArrayList<String>(); 
    private double latitude = 0.0;
    private double longitude = 0.0;
    private double altitude = 0.0;
    private double direction = 0.0; //0-359 degrees
    private String uploadMsg;
    private Media photo = null;
    
    public void addPhoto(Media photo) {
        this.photo = photo;
    }
    
    public void clear(){
    	description = null;
    	direction = 0;
    	id = null;
    	latitude = 0;
    	longitude = 0;
    	photo = null;
    	tags = new ArrayList<String>();
    	title = null;
    	
    }

	public MediaMessage clone(){
    	MediaMessage cloned = new MediaMessage();
    	cloned.setDescription(description);
    	cloned.setDirection(direction);
    	cloned.setId(id);
    	cloned.setLatitude(latitude);
    	cloned.setLongitude(longitude);
    	cloned.setPhoto(photo);
    	cloned.setTags(tags);
    	cloned.setTitle(title);
    	return cloned;
    }

	/**
     * Clean up file resources.
     */
    public void dispose(){
        // try and clean up the data[], but only for real photos, we don't
        // want to delete the video and audio icons.
        if (getShowPhoto()){
            //smallPhoto.dispose();
            //mediumPhoto.dispose();
            //largePhoto.dispose(); //TODO
        }
    }
    
    public double getAltitude(){
    	return altitude;
    }
    
    public void setAltitude(double alt){
    	this.altitude = alt;
    }

	public String getDescription() {
		return description;
	}

	public double getDirection() {
		return direction;
	}

    public String getId() {
		return id;
	}

    public double getLatitude() {
		return latitude;
	}

    public double getLongitude() {
		return longitude;
	}
    
    public Media getPhoto() {
        return photo;
    }

    public boolean getShowPhoto()  {
    	 return photo != null;
    }

    public List<String> getTags() {
		return tags;
	}

	public String getTitle() {
		return title;
	}

	public boolean isHasMedia(){
    	return this.photo != null;
    }
	
	public void setDescription(String description) {
		this.description = description;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
    
    public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setPhoto(Media photo) {
		this.photo = photo;
	}
    
    public void setTags(List<String> tags){
		this.tags = tags;
	}
    
    public void setTitle(String title) {
		this.title = title;
	}
    
    public String getUploadMsg() {
		return uploadMsg;
	}

	public void setUploadMsg(String uploadMsg) {
		this.uploadMsg = uploadMsg;
	}

	
    
    public String toString(){
    	return String.format("%s title=%s, description=%s, photo=%s, tags=%s, lattitude=%s, longitude=%s, direction=%s", 
    			super.toString(), 
    			title,
    			description,
    			photo,
    			tags,
    			latitude,
    			longitude,
    			direction);
    }

}
