package org.icemobile.samples.mediacast;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class MediaView {
	
	private MediaMessage media;
	
	@ManagedProperty(value="#{mediaStore}")
	private MediaStore mediaStore;

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

}
