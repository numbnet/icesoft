package org.icemobile.samples.mediacast;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class MediaView {
	
	private MediaMessage media;

	public MediaMessage getMedia() {
		return media;
	}

	public void setMedia(MediaMessage media) {
		this.media = media;
	}

}
