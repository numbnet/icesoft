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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

@Service
public class MediaService implements ServletContextAware {
    
    private static final int CAROUSEL_MAX_INDEX = 15;
    
    private List<MediaMessage> media = Collections.synchronizedList(new ArrayList<MediaMessage>());
    private static final int CAROUSEL_IMG_HEIGHT = 75;
    private String contextPath;
    private TagWeightMap tagsMap = new TagWeightMap();
    private Comparator<MediaMessage> mediaByTimeComparator = new MediaMessageByTimeComparator();
    
    private static final Log log = LogFactory
            .getLog(MediaService.class);
    
    public List<MediaMessage> getMedia(){
        return media;
    }
    
    public List<MediaMessage> getMediaListSortedByTime(){
        List<MediaMessage> list = new ArrayList<MediaMessage>();
        if( media != null && media.size() > 0 ){
            list = new ArrayList<MediaMessage>(media);
            Collections.sort(list, mediaByTimeComparator);
        }
        return list;
    }
    
    public List<MediaMessage> getMediaSortedByTime(int max){
        List<MediaMessage> list = new ArrayList<MediaMessage>();
        if( media != null && media.size() > 0 ){
            list.addAll(media);
            Collections.sort(list, mediaByTimeComparator);
            if( list.isEmpty() && list.size() > max){
                list = list.subList(0,  max);
            }
        }
        return list;
    }
    
    public List<MediaMessage> getMediaCopy(){
        return new ArrayList<MediaMessage>(media);
    }
    
    public void setContextPath(String contextPath){
        this.contextPath = contextPath;
    }
    
    public List<String> getMediaImageMarkup(){
        List<String> imageMarkup = new ArrayList<String>();
        if( media != null ){
            List<MediaMessage> list = getMediaSortedByTime(CAROUSEL_MAX_INDEX);
            if( list != null && list.size() > 0 ){
                for( MediaMessage mediaMsg : getMediaSortedByTime(CAROUSEL_MAX_INDEX) ){
                    File photo = mediaMsg.getSmallPhoto();
                    if( photo != null ){
                        String markup = "<div><a data-id='"+mediaMsg.getId()+"' href='"+contextPath+"/app?page=viewer"
                                + "&id="+mediaMsg.getId()
                                +"' title='"+mediaMsg.getDescription()+"'><img height='"
                                +CAROUSEL_IMG_HEIGHT+"' width='"+CAROUSEL_IMG_HEIGHT +"' src='resources/uploads/"
                                + photo.getName()+"' style='border:none;'/></a></div>";
                        imageMarkup.add(markup);
                    }
                }
            }
            
        }
        return imageMarkup;
    }

    public MediaMessage getMediaMessage(String id){
        MediaMessage result = null;
        if( id != null ){
            for( MediaMessage msg: getMediaCopy()){
                if( msg.getId() != null &&  msg.getId().equals(id)){
                    result = msg;
                    break;
                }
            }
        }
        return result;
    }
    
    public void removeMessage(String id){
        synchronized(media) {
            Iterator<MediaMessage> iter = media.iterator();
            while( iter.hasNext() ){
                MediaMessage msg = iter.next();
                if( msg.getId() != null && msg.getId().equals(id)){
                    iter.remove();
                    msg.dispose();
                    break;
                }
            }
        }
    }

    public void setServletContext(ServletContext context) {
        this.contextPath = context.getContextPath();
    }

    public TagWeightMap getTagsMap() {
        return tagsMap;
    }

    public List<String> getTags(){
        return new ArrayList<String>(tagsMap.keySet());
    }
    
    public void addMedia(MediaMessage msg){
        if( msg != null ){
            MediaMessage cloned = msg.clone();
            boolean mediaAdded = media.add(cloned);
            log.debug("media added="+mediaAdded);
            log.debug("addMedia: tags="+msg.getTags());
            if( msg.getTags().size() > 0 ){
                for( String tag : msg.getTags() ){
                    tagsMap.put(tag);
                    log.debug("tag: " + tag + ", count=" + tagsMap.get(tag));
                }
            }
        }       
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
        
        public void put(String tag){
            Integer count = super.get(tag);
            if( count != null ){
                super.put(tag, Integer.valueOf(count.intValue()+1));
            }
            else{
                super.put(tag, Integer.valueOf(1));
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
            log.debug(String.format("calculate weight for %s min=%s, max=%s, count=%s, weight=%s", tag, minCount, maxCount, tagCount, weight));
            
            return weight;
        }
    }
    
    
    /* sorted descending */
    class MediaMessageByTimeComparator implements Comparator<MediaMessage>{

        public int compare(MediaMessage msg1, MediaMessage msg2) {
            
            try{
            
                if( msg1 == null && msg2 == null ){
                    return 0;
                }
                if( msg1 != null && msg2 == null ){
                    return -1;
                }
                if( msg1 == null && msg2 != null ){
                    return 1;
                }
                return Long.valueOf(msg2.getCreated())
                        .compareTo(Long.valueOf(msg1.getCreated()));
            }
            catch(Exception e){
                e.printStackTrace();
                log.fatal("problem in comparator");
                return 0;
            }
        }
        
    }


}
