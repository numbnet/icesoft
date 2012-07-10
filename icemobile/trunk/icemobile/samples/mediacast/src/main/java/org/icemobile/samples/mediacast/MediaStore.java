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

import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.bean.ManagedBean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.net.URLEncoder;

/**
 * Simple in memory image cache which stores an uploaded image and a scaled
 * thumbnail version of it.
 */
@ManagedBean(name = MediaStore.BEAN_NAME, eager = true)
@ApplicationScoped
public class MediaStore implements Serializable {
	
	private static final Logger logger =
            Logger.getLogger(MediaStore.class.toString());

    public static final String BEAN_NAME = "mediaStore";

    private static final int MAX_CACHE_SIZE = 10;
    private static final int MAX_CAROUSEL_SIZE = 5;

    private LinkedList<MediaMessage> mediaStack;
    private LinkedList<MediaMessage> mediaCarouselStack;

    public MediaStore() {
        mediaStack = new LinkedList<MediaMessage>();
        mediaCarouselStack = new LinkedList<MediaMessage>();
    }

    public List<MediaMessage> getMediaStack() {
        return mediaStack;
    }

    public List<MediaMessage> getCarouselStack() {
        return mediaCarouselStack;
    }

    public int getCarouselStackCount() {
        return mediaCarouselStack.size();
    }

    public String getArParams() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        StringBuilder sb = new StringBuilder();
        String url = externalContext.getRequestScheme() + "://" +
            externalContext.getRequestServerName() + ":" + 
            externalContext.getRequestServerPort();
        //base URL must be extracted for jsf resources
//        sb.append("ub=" + URLEncoder.encode(url));
        for (MediaMessage message : mediaStack)  {
            String imageURL = null;
			try {
				imageURL = URLEncoder.encode(
				        message.getMediumPhoto().getData().getURL().toString(),"UTF-8" );
				String location = message.getMessageAsUrlParam() + "," + imageURL;
	            sb.append("&" + location);
			} catch (UnsupportedEncodingException e) {
				logger.warning("image url could not be encoded: " + e.getMessage());
			}
        }

        return sb.toString();
    }

    /**
     * Add a new image set to the the store.
     *
     * @param photoMessage photo image to add to store.
     */
    public void addMedia(MediaMessage photoMessage) {
        mediaStack.addFirst(photoMessage);
        // keep the list of upload small. we don't want to break the bank!
        if (mediaStack.size() > MAX_CACHE_SIZE) {
            MediaMessage message = mediaStack.removeLast();
            message.dispose();
        }
        mediaCarouselStack.addFirst(mediaStack.peek());
        if (mediaCarouselStack.size() > MAX_CAROUSEL_SIZE) {
            mediaCarouselStack.removeLast();
            // shared object reference with media stack so no cleanup necessary.
        }
    }

    /**
     * Removes the specified message from the media and carousel stacks.
     *
     * @param mediaMessage media message to remove.
     */
    public void removeMedia(MediaMessage mediaMessage){
        mediaCarouselStack.remove(mediaMessage);
        mediaStack.remove(mediaMessage);
    }

}
