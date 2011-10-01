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

package icefaces.mobile;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.imageio.ImageIO;

import org.icefaces.application.PushRenderer;
import org.icefaces.impl.util.CoreUtils;
import org.icefaces.util.EnvUtils;

/**
 * <p>The CameraBean is used to test camera component.</p>
 */
@ManagedBean(name="mixedBean")
@SessionScoped
public class MixedMediaBean implements Serializable {
	private static final Logger logger =
        Logger.getLogger(MixedMediaBean.class.toString());
    private int idCounter = 0;
    private Map cameraImage = new HashMap<String, Object>();
    private Map audioClip = new HashMap<String, Object>();
    public static final String CAMERA_KEY_FILE = "file";
    private String pathToImageFile = "../images/icefaces.png";
    private String pathToAudioFile = "../audio/sample.mp4";
    // files associated with the current user
    private int width=200;
    private int height=200;
    private byte[] bytes;
    private boolean bytesAvailable = false;
    private String audioContentType="audio/mpeg";
    private boolean soundClipAvailable = false;
  
    
    private static int sampleUploadCount = 1;
	private int numberImages=0;

    public void setCameraImage(Map cameraImage) {
        this.cameraImage = cameraImage;
        File imageFile;
        if (null==cameraImage){
            System.out.println("attribute null for cameraImage");
            imageFile = new File("temp");
        }
        else {
          imageFile = (File) cameraImage.get(CAMERA_KEY_FILE);
        }
  
        FacesContext facesContext = FacesContext.getCurrentInstance();
        BufferedImage image = null;
        try {
            if (imageFile != null) {            	
                 //actually have ability to call file anything
                this.numberImages++;
                this.pathToImageFile = imageFile.getAbsolutePath();
                String relativePath =  cameraImage.get("relativePath").toString();
                this.setPathToImageFile(relativePath);
                
                // try for a little clean up after
                imageFile.deleteOnExit();
              
            } else {

				//     FacesContext facesContext = FacesContext.getCurrentInstance();
                String sampleImagePath = facesContext.getExternalContext()
                        .getRealPath("/images/")
                        .concat(File.separator)
                        .concat("DSC_"+sampleUploadCount+".jpg");
                sampleUploadCount = (sampleUploadCount++ > 11)?0:sampleUploadCount;
                image = ImageIO.read(new File(sampleImagePath));
            }
        } catch (IOException e) {
            logger.log(Level.WARNING,
                    "Error reading sample upload file: ", e);
        }
        // process the image and add the image store.
        if (image != null) {
            // scale the original file into a small thumbNail and the other
            // into a 1 megapixelish sized image.
            this.width = image.getWidth();
            this.height = image.getHeight();
            image.flush();   
        }
    }
    public Map getCameraImage() {
        return cameraImage;
    }  
     
    public String getPathToImageFile() {
		return pathToImageFile;
	}
	public void setPathToImageFile(String pathToFile) {
		this.pathToImageFile = pathToFile;
	}
	public int getNumberImages() {
		return numberImages;
	}
	public void setNumberImages(int numberImages) {
		this.numberImages = numberImages;
	}

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public boolean isBytesAvailable() {
		return bytesAvailable;
	}
	public void setBytesAvailable(boolean bytesAvailable) {
		this.bytesAvailable = bytesAvailable;
	}
	public Map getAudioClip() {
		return audioClip;
	}
	public void setAudioClip(Map clip) {
		this.audioClip = clip;
		File audioFile = (File)clip.get("file");
		if (null!=audioFile){
			String filePath = audioClip.get("relativePath").toString();
			if (null!=filePath){
				this.setPathToAudioFile(filePath);
				this.soundClipAvailable = true;
			}
			String contentType = audioClip.get("contentType").toString();
			this.setAudioContentType(contentType);
		}
		
	}
	public String getPathToAudioFile() {
		return pathToAudioFile;
	}
	public void setPathToAudioFile(String pathToAudioFile) {
		this.pathToAudioFile = pathToAudioFile;
	}
	public String getAudioContentType() {
		return audioContentType;
	}
	public void setAudioContentType(String audioContentType) {
		this.audioContentType = audioContentType;
	}
	public boolean isSoundClipAvailable() {
		return soundClipAvailable;
	}
	public void setSoundClipAvailable(boolean soundClipAvailable) {
		this.soundClipAvailable = soundClipAvailable;
	}
	
    public boolean isEnhancedBrowser()  {
        return EnvUtils.isEnhancedBrowser(FacesContext.getCurrentInstance());
    }
}
