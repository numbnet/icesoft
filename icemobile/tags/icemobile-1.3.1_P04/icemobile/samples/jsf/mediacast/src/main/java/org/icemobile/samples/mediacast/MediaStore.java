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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
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
    
    private TagWeightMap tagWeightMap = new TagWeightMap();
    
    
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
    			tagWeightMap.add(tag);
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
    		tagWeightMap.remove(tag);
    	}
    }
    
    public TagWeightMap getTagWeightMap(){
    	return tagWeightMap;
    }
    
    public List<String> getTags(){
    	return new ArrayList<String>(tagWeightMap.keySet());
    }
    
	@SuppressWarnings("serial")
	public class TagWeightMap extends HashMap<String,Integer>{
		
		private static final int MAX_FONT_SIZE = 22;
		private static final int MIN_FONT_SIZE = 9;
		private int maxCount = 0;
		private int minCount = 0;

		@Override
		public Integer get(Object key) {
			return calculateWeight((String)key);
		}
		
		public void add(String tag){
			Integer count = super.get(tag);
			if( count != null ){
				super.put(tag, Integer.valueOf(count.intValue()+1));
			}
			else{
				super.put(tag, Integer.valueOf(1));
			}
		}
		
		public void remove(String tag){
			Integer count = super.get(tag);
    		if(Integer.valueOf(1).equals(count)){
    			super.remove(tag);
    		}
    		else{
    			super.put(tag, Integer.valueOf(count.intValue()-1));
    		}
		}
		
		//see http://en.wikipedia.org/wiki/Tag_cloud
		private int calculateWeight(String tag){
			int weight = 0;
			Integer tagCountI = super.get(tag);
			int tagCount = tagCountI == null ? 0 : tagCountI.intValue();
			if( this.size() > 0 ){
				minCount = maxCount = tagCount;
				for( String key : this.keySet() ){
					int count = super.get(key).intValue();
					if( count > maxCount ){
						maxCount = count;
					}
					else if( count < minCount ){
						minCount = count;
					}
				}
				if( tagCount > minCount ){
					weight = (MAX_FONT_SIZE * (tagCount - minCount))/(maxCount - minCount);
				}
				else{
					weight = MIN_FONT_SIZE;
				}
				
			}
			else{
				maxCount = 0;
				minCount = 0;
			}
			
			return weight;
		}
	}


}
