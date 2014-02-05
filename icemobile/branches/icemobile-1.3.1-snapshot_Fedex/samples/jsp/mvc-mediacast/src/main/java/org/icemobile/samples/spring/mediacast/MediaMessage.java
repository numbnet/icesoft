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

package org.icemobile.samples.spring.mediacast;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MediaMessage implements Serializable{

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
	private File video = null;
	private File audio = null;
	private File smallPhoto = null;
    private File largePhoto = null;
    private long created;
    
	
	public static final String MEDIA_TYPE_PHOTO = "Photo";
	public static final String MEDIA_TYPE_VIDEO = "Video";
	public static final String MEDIA_TYPE_AUDIO = "Audio";

	public void clear(){
		audio = null;
		description = null;
		direction = 0;
		id = null;
		latitude = 0;
		longitude = 0;
		tags = new ArrayList<String>();
		title = null;
		video = null;
		created = 0;
		smallPhoto = null;
		largePhoto = null;
	}

	public MediaMessage clone(){
		MediaMessage cloned = new MediaMessage();
		cloned.setAudio(audio);
		cloned.setDescription(description);
		cloned.setDirection(direction);
		cloned.setId(id);
		cloned.setLatitude(latitude);
		cloned.setLongitude(longitude);
		cloned.setSmallPhoto(smallPhoto);
		cloned.setLargePhoto(largePhoto);
		cloned.setTags(tags);
		cloned.setTitle(title);
		cloned.setVideo(video);
		cloned.setCreated(created);
		cloned.setLargePhoto(largePhoto);
		return cloned;
	}

	/**
	 * Clean up file resources.
	 */
	 public void dispose(){
		if( smallPhoto != null ){
			smallPhoto.delete();
		}
		if( largePhoto != null ){
			largePhoto.delete();
		}
			
	 }

	 public double getAltitude(){
		 return altitude;
	 }

	 public void setAltitude(double alt){
		 this.altitude = alt;
	 }

	 public File getAudio()  {
		 return audio;
	 }

	 public void setAudio(File media){
		 this.audio = media;
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

	 public boolean getShowAudio()  {
		 return audio != null;
	 }

	 public boolean getShowVideo()  {
		 return video != null;
	 }

	 public List<String> getTags() {
		 return tags;
	 }

	 public String getTitle() {
		 return title;
	 }

	 public File getVideo()  {
		 return video;
	 }

	 public void setVideo(File media){
		 this.video = media;
	 }

	 public boolean isHasMedia(){
		 return this.audio != null || this.smallPhoto != null || this.largePhoto != null || this.video != null;
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

	 @Override
	public String toString() {
		return "MediaMessage [title=" + title + ", id="
				+ id + ", description=" + description + ", tags=" + tags
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", altitude=" + altitude + ", direction=" + direction
				+ ", uploadMsg=" + uploadMsg + ", video="
				+ video + ", audio=" + audio + ", smallPhoto=" + smallPhoto
				+ ", largePhoto=" + largePhoto + ", created=" + created + "]";
	}

	public long getCreated() {
		 return created;
	 }

	 public void setCreated(long created) {
		 this.created = created;
	 }

	public File getSmallPhoto() {
		return smallPhoto;
	}

	public void setSmallPhoto(File smallPhoto) {
		this.smallPhoto = smallPhoto;
	}

	public File getLargePhoto() {
		return largePhoto;
	}

	public void setLargePhoto(File largePhoto) {
		this.largePhoto = largePhoto;
	}

}
