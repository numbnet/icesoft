package org.icemobile.samples.mediacast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.commons.lang3.StringUtils;

@ManagedBean
@SessionScoped
public class GalleryView {
	
	@ManagedProperty(value="#{mediaStore}")
	private MediaStore mediaStore;
	
	private List<String> filters = new ArrayList<String>();
	private String filterString;
	
	private List<MediaMessage> filteredMessages = new ArrayList<MediaMessage>();
	
	@PostConstruct
	public void init(){
		filteredMessages = new ArrayList<MediaMessage>(mediaStore.getMedia());
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
		
	}
	
	public List<MediaMessage> getFilteredMessages(){
		filterMessages();
		return filteredMessages;
	}

}
