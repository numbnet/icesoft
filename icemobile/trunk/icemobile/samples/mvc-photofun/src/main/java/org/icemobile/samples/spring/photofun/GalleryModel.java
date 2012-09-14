package org.icemobile.samples.spring.photofun;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icemobile.samples.spring.photofun.MediaService.TagWeightMap;

public class GalleryModel {
	
	private List<String> filters = new ArrayList<String>();
	private String filterString;
	private List<String> tags = new ArrayList<String>();
	private TagWeightMap tagsMap;
	private List<MediaMessage> filteredMessages = new ArrayList<MediaMessage>();
	private int filteredMessagesCount = 0;
	private static final Log log = LogFactory
			.getLog(GalleryModel.class);
	
	
	public List<String> getFilters() {
		return filters;
	}
	public void setFilters(List<String> filters) {
		this.filters = filters;
	}
	public String getFilterString() {
		return filterString;
	}
	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}
	public List<MediaMessage> getFilteredMessages() {
		return filteredMessages;
	}
	public void setFilteredMessages(List<MediaMessage> filteredMessages) {
		this.filteredMessages = filteredMessages;
	}
	public int getFilteredMessagesCount() {
		return filteredMessagesCount;
	}
	public void setFilteredMessagesCount(int filteredMessagesCount) {
		this.filteredMessagesCount = filteredMessagesCount;
	}
	public List<String> getTags() {
		log.debug("tags******************** " + tags);
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public Map<String, Integer> getTagsMap() {
		return tagsMap;
	}
	public void setTagsMap(TagWeightMap tagsMap) {
		this.tagsMap = tagsMap;
	}
	

}
