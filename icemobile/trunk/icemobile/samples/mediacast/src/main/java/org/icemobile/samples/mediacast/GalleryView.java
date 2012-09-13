package org.icemobile.samples.mediacast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang3.StringUtils;

@ManagedBean
@SessionScoped
public class GalleryView {
	
	@ManagedProperty(value="#{mediaStore}")
	private MediaStore mediaStore;
	
	private List<String> filters = new ArrayList<String>();
	private String filterString;
	
	private List<MediaMessage> filteredMessages = new ArrayList<MediaMessage>();
	private int filteredMessagesCount = 0;
	
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
	
	public String getFilterString(){
		return filterString;
	}
	
	public void setFilterString(String filterString){
		this.filterString = filterString;
		filters = Arrays.asList(StringUtils.split(filterString));
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
	
	public boolean tagInCurrentFilters(String tag){
		return filters.contains(tag);
	}
	
	public void toggleFilter(ActionEvent e){
		String tag = (String)e.getComponent().getAttributes().get("tag");
		filters = new ArrayList<String>(filters);
		if( filters.contains(tag)){
			filters.remove(tag);
		}
		else{
			filters.add(tag);
		}
	}

}
