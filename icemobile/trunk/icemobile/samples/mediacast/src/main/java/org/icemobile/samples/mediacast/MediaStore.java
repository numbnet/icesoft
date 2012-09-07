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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * Simple in memory image cache which stores an uploaded image and a scaled
 * thumbnail version of it.
 */
@ManagedBean(name = MediaStore.BEAN_NAME, eager = true)
@ApplicationScoped
public class MediaStore implements Serializable {
	
	private static final Logger log =
            Logger.getLogger(MediaStore.class.toString());

    public static final String BEAN_NAME = "mediaStore";

    private static final int MAX_CACHE_SIZE = 10;

    private List<MediaMessage> media = new CopyOnWriteArrayList<MediaMessage>();
    
    private Map<String,Integer> tags = new HashMap<String,Integer>();
    
    
    public List<MediaMessage> getMedia() {
        return media;
    }
    
    /**
     * Add a new MediaMessage set to the the store.
     *
     * @param mediaMessage MediaMessage to add to store.
     */
    public void addMedia(MediaMessage mediaMessage) {
    	media.add(0,mediaMessage);
    	if( mediaMessage.getTags().size() > 0 ){
    		for( String tag : mediaMessage.getTags() ){
    			Integer count = tags.get(tag);
    			if( count != null ){
    				tags.put(tag, Integer.valueOf(count.intValue()+1));
    			}
    			else{
    				tags.put(tag, Integer.valueOf(1));
    			}
    		}
    	}
        // keep the list of upload small. we don't want to break the bank!
        if (media.size() > MAX_CACHE_SIZE) {
            MediaMessage message = media.get(media.size()-1);
            removeMedia(message);
        }
    }

    /**
     * Removes the specified message from the media stack.
     *
     * @param mediaMessage media message to remove.
     */
    public void removeMedia(MediaMessage mediaMessage){
    	media.remove(mediaMessage);
    	for( String tag : mediaMessage.getTags() ){
    		Integer count = tags.get(tag);
    		if(Integer.valueOf(1).equals(count)){
    			tags.remove(tag);
    		}
    		else{
    			tags.put(tag, Integer.valueOf(count.intValue()-1));
    		}
    	}
    }
    
    public Map<String, Integer> getTags(){
    	return tags;
    }
    
    public List<String> getTagSet(){
    	return new ArrayList<String>(tags.keySet());
    }

}
