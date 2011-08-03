/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icemobile.samples.mediacast;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * UploadModes stores files uploaded via the new mobile components before
 * they are added to the ImageStore.  The bean also adds the currently
 * session to the "mobi" render group so that it can receive push updates when
 * a new file is uploaded by other users.
 */
@ManagedBean(name = UploadModel.BEAN_NAME)
@SessionScoped
public class UploadModel implements Serializable {

    private static final Logger logger =
            Logger.getLogger(UploadModel.class.toString());

    public static final String BEAN_NAME = "uploadModel";

    // media input mode.
    private String selectedMediaInput = "";

    private Map mediaMap;
    // uploaded file
    private File cameraFile;
    private File audioFile;
    private File videoFile;

    // select a photo to view in detail.
    private MediaMessage selectedPhoto;

    // placeholder for new message comment
    private String photoComment;

    public UploadModel() {
        mediaMap = new HashMap<String, Object>();
    }

    public void setAudio(Map audio) {
        audioFile = (File) audio.get(MediaController.MEDIA_FILE_KEY);
        if (audioFile != null) {
            logger.info("Retrieved Audio File");
            // try for a little clean up after
            audioFile.deleteOnExit();
        }
    }

    public void setVideo(Map video) {
        videoFile = (File) video.get(MediaController.MEDIA_FILE_KEY);

        if (videoFile != null) {
            logger.info(this + " Retrieved Video File");
            // try for a little clean up after
            videoFile.deleteOnExit();
        }
    }

    /**
     * Accept the uploaded Image file and any metadata
     *
     * @param mediaMap mediaMap map
     */
    public void setMediaMap(Map mediaMap) {
        this.mediaMap = mediaMap;
        File imageFile = (File) mediaMap.get(MediaController.MEDIA_FILE_KEY);

        if (imageFile != null) {
            logger.info("Retrieved Camera Image adding to ImageStore");
            // try for a little clean up after
            imageFile.deleteOnExit();
        }
        cameraFile = imageFile;
    }

    public Map getMediaMap() {
        return mediaMap;
    }

    public MediaMessage getSelectedPhoto() {
        return selectedPhoto;
    }

    public void setSelectedPhoto(MediaMessage selectedPhoto) {
        this.selectedPhoto = selectedPhoto;
    }

    public String getComment() {
        return photoComment;
    }

    public void setComment(String photoComment) {
        this.photoComment = photoComment;
    }

    public File getCameraFile() {
        return cameraFile;
    }

    public void setCameraFile(File cameraFile) {
        this.cameraFile = cameraFile;
    }

    public File getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(File videoFile) {
        this.videoFile = videoFile;
    }

    public File getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(File audioFile) {
        this.audioFile = audioFile;
    }

    public String getSelectedMediaInput() {
        return selectedMediaInput;
    }

    public void setSelectedMediaInput(String selectedMediaInput) {
        this.selectedMediaInput = selectedMediaInput;
    }

    public boolean getShowInputSelect() {
        return "".equals(selectedMediaInput);
    }

    public boolean getShowCamera() {
        return MediaMessage.MEDIA_TYPE_PHOTO.equals(selectedMediaInput);
    }

    public boolean getShowCamcorder() {
        return MediaMessage.MEDIA_TYPE_VIDEO.equals(selectedMediaInput);
    }

    public boolean getShowMicrophone() {
        return MediaMessage.MEDIA_TYPE_AUDIO.equals(selectedMediaInput);
    }

}
