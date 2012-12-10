package org.icemobile.samples.spring.mediacast;

import java.io.File;

public class MediaMessageTransfer {
	
	private String id;
	private String description;
	private String fileName;
	private long created;
	
	public MediaMessageTransfer(MediaMessage msg){
		id = msg.getId();
		description = msg.getDescription();
		if( msg.getSmallPhoto() != null ){
			fileName = msg.getSmallPhoto().getName();
		}
		created = msg.getCreated();
	}

	@Override
	public String toString() {
		return "MediaMessageTransfer [id=" + id + ", description="
				+ description + ", fileName=" + fileName  + ", created=" + created + "]";
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

	public String getFileName() {
		return fileName;
	}

}
