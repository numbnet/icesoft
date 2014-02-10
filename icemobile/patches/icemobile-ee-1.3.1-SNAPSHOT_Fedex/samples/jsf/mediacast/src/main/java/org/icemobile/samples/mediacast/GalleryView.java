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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class GalleryView implements Serializable {
	
	@ManagedProperty(value="#{mediaStore}")
	private MediaStore mediaStore;
	
	private List<String> filters = new ArrayList<String>();
	
	private List<MediaMessage> filteredMessages = new ArrayList<MediaMessage>();
	private int filteredMessagesCount = 0;
	
	private Map<String,String> tagInCurrentFilters = new HashMap<String,String>();
	
	private static final Logger log =
            Logger.getLogger(GalleryView.class.toString());
	
	@PostConstruct
	public void init(){
		filteredMessages = new ArrayList<MediaMessage>(mediaStore.getMedia());
		filteredMessagesCount = filteredMessages.size();
	}
	
	public void setMediaStore(MediaStore store){
		this.mediaStore = store;
	}
	
	private void filterMessages(){
		filteredMessages.clear();
		if( filters.isEmpty()){
			filteredMessages = new ArrayList<MediaMessage>(mediaStore.getMedia());
		}
		else{
			Iterator<MediaMessage> iter = mediaStore.getMedia().iterator();
			while( iter.hasNext() ){
				MediaMessage msg = iter.next();
				for( String tag : msg.getTags() ){
					if( filters.contains(tag)){
						filteredMessages.add(msg);
						break;
					}
				}
			}
		}
		filteredMessagesCount = filteredMessages.size();
	}
	
	public List<MediaMessage> getFilteredMessages(){
		filterMessages();
		return filteredMessages;
	}
	
	public int getFilteredMessagesSize(){
		return filteredMessagesCount;
	}
	
	public void toggleFilter(ActionEvent e){
		String tag = (String)e.getComponent().getAttributes().get("tag");
		filters = new ArrayList<String>(filters);
		if( filters.contains(tag)){
			filters.remove(tag);
			tagInCurrentFilters.remove(tag);
		}
		else{
			filters.add(tag);
			tagInCurrentFilters.put(tag,tag);
		}
	}
	
	public Map<String,String> getTagInCurrentFilters(){
		return tagInCurrentFilters;
	}

}
