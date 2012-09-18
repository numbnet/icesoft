package org.icemobile.samples.spring.mediacast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

@Service
public class MediaService implements ServletContextAware {
	
	private List<MediaMessage> media = new ArrayList<MediaMessage>();
	private Set<MediaMessage> mediaByVotes = Collections.synchronizedSet(new HashSet<MediaMessage>());
	private static final int CAROUSEL_IMG_HEIGHT = 48;
	private static final String CAROUSEL_ITEM_MARKUP = 
			"<div style='overflow:hidden;height:48px;'><img height='"+CAROUSEL_IMG_HEIGHT+"' src='%1$s/resources/uploads/%2$s' style='border:none;' title='%3$s'></div><a class='view-play-icon' href='%1$s/media/%4$s' ><img src='%1$s/resources/images/view-icon.png' style='border:none;'></a>";
	private String contextPath;
	private TagWeightMap tagsMap = new TagWeightMap();
	private static final String CONTEST_CAROUSEL_ITEM_MARKUP = 
			"<div style='overflow:hidden;height:48px;'><img height='"+CAROUSEL_IMG_HEIGHT+"' src='%1$s/resources/uploads/%2$s' style='border:none;' title='%3$s'></div><a class='view-play-icon' href='%1$s/contest-uploads/%4$s' ><img src='%1$s/resources/images/view-icon.png' style='border:none;'></a>";
	private Comparator<MediaMessage> mediaByVotesComparator = new MediaMessageByVotesComparator();
	
	private static final Log log = LogFactory
			.getLog(MediaService.class);
	
	public List<MediaMessage> getMedia(){
		return media;
	}
	
	public List<MediaMessage> getMediaSortedByVotes(){
		List<MediaMessage> list = new ArrayList<MediaMessage>(mediaByVotes);
		Collections.sort(list, mediaByVotesComparator);
		return list;
	}
	
	
	public void setContextPath(String contextPath){
		this.contextPath = contextPath;
	}
	
	public List<String> getMediaImageMarkup(){
    	List<String> imageMarkup = new ArrayList<String>();
    	if( media != null ){
	    	for( MediaMessage mediaMsg : media ){
	    		imageMarkup.add(String.format(CAROUSEL_ITEM_MARKUP, contextPath, mediaMsg.getPhoto().getFileName(), mediaMsg.getTitle(), mediaMsg.getId()));
	    	}
    	}
    	return imageMarkup;
    }
	
	public List<String> getContestMediaImageMarkup(){
    	List<String> imageMarkup = new ArrayList<String>();
    	if( mediaByVotes != null ){
	    	for( MediaMessage mediaMsg : getMediaSortedByVotes() ){
	    		imageMarkup.add(String.format(CONTEST_CAROUSEL_ITEM_MARKUP, contextPath, mediaMsg.getPhoto().getFileName(), mediaMsg.getTitle(), mediaMsg.getId()));
	    	}
    	}
    	return imageMarkup;
    }

	public MediaMessage getMediaMessage(String id){
		MediaMessage result = null;
		for( MediaMessage msg: media){
			if( msg.getId().equals(id)){
				result = msg;
				break;
			}
		}
		return result;
	}
	
	public void removeMessage(String id){
		for( MediaMessage msg: media){
			if( msg.getId().equals(id)){
				media.remove(msg);
				if( msg.getPhoto() != null ){
					msg.getPhoto().dispose();
				}
				if( msg.getVideo() != null ){
					msg.getVideo().dispose();
				}
				if( msg.getAudio() != null ){
					msg.getAudio().dispose();
				}
				break;
			}
		}
		Iterator<MediaMessage> iter = mediaByVotes.iterator();
		while( iter.hasNext() ){
			MediaMessage msg = iter.next();
			if( msg.getId().equals(id)){
				iter.remove();
				if( msg.getPhoto() != null ){
					msg.getPhoto().dispose();
				}
				if( msg.getVideo() != null ){
					msg.getVideo().dispose();
				}
				if( msg.getAudio() != null ){
					msg.getAudio().dispose();
				}
				break;
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
			media.add(0,cloned);
			log.debug("added to media");
			boolean mediaByVotesAdded = mediaByVotes.add(cloned);
			log.debug("added to mediaByVotes");
			log.debug("media by votes added="+mediaByVotesAdded);
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
	class MediaMessageByVotesComparator implements Comparator<MediaMessage>{

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
				return Integer.valueOf(msg2.getVotes().size())
						.compareTo(msg1.getVotes().size());
			}
			catch(Exception e){
				e.printStackTrace();
				log.fatal("problem in comparator");
				return 0;
			}
		}
		
	}


}
