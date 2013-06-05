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

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.icefaces.application.PushRenderer;

@ManagedBean
@SessionScoped
public class MediaView implements Serializable{
	
	private MediaMessage media;
	
	@ManagedProperty(value="#{mediaStore}")
	private MediaStore mediaStore;
	
	private String currentTab = "upload";
	
	@PostConstruct
	public void init(){
	    PushRenderer.addCurrentSession(MediaController.RENDER_GROUP);
	}

	public MediaMessage getMedia() {
		if( media == null ){
			if( mediaStore.getMedia() != null && mediaStore.getMedia().size() > 0 ){
				media = mediaStore.getMedia().get(0);
			}
		}
		return media;
	}

	public void setMedia(MediaMessage media) {
		this.media = media;
	}

	public MediaStore getMediaStore() {
		return mediaStore;
	}

	public void setMediaStore(MediaStore mediaStore) {
		this.mediaStore = mediaStore;
	}
	
    public String getCurrentTab() {
        return currentTab;
    }
    
    public void setCurrentTab(String currentTab) {
        this.currentTab = currentTab;
    }


}
