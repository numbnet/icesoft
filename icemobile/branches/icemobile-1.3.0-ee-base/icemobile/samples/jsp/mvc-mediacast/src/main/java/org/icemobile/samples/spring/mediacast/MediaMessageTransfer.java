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

public class MediaMessageTransfer {
	
	private String id;
	private String description;
	private String photoFileName;
	private long created;
	private boolean hasVideo;
	private boolean hasPhoto;
	private boolean hasAudio;
	
	
	public MediaMessageTransfer(MediaMessage msg){
		id = msg.getId();
		description = msg.getDescription();
		if( msg.getSmallPhoto() != null ){
		    photoFileName = msg.getSmallPhoto().getName();
		    hasPhoto = true;
		}
		hasAudio = msg.getShowAudio();
		hasVideo = msg.getShowVideo();
		created = msg.getCreated();
	}

	@Override
	public String toString() {
		return "MediaMessageTransfer [id=" + id + ", description="
				+ description + ", fileName=" + photoFileName  + ", created=" + created + "]";
	}

	public long getCreated() {
		return created;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String getPhotoFileName() {
		return photoFileName;
	}
	
	public boolean isAudio(){
	    return hasAudio;
	}
	
	public boolean isVideo(){
	    return hasVideo;
	}
	
	public boolean isPhoto(){
	    return hasPhoto;
	}

}
