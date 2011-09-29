/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.mobile;



import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
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

import javax.activation.MimetypesFileTypeMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.imageio.ImageIO;

import org.icefaces.application.PushRenderer;
import org.icefaces.impl.util.CoreUtils;
import org.icefaces.util.EnvUtils;

/**
 * <p>The CameraBean is used to test camera component.</p>
 */
@ManagedBean(name="cameraBean")
@ApplicationScoped
public class CameraBean implements Serializable {
	private static final Logger logger =
        Logger.getLogger(CameraBean.class.toString());
    private int idCounter = 0;
    private Map cameraImage = new HashMap<String, Object>();
    public static final String CAMERA_KEY_FILE = "file";
    private String pathToFile = "/images/icefaces.png";
    // files associated with the current user
    private final List fileList =
            Collections.synchronizedList(new ArrayList());
    private int width=200;
    private int height=200;
    private byte[] bytes;
    private boolean bytesAvailable = false;
    private String messageFromAL = " ValueChangenListener fired for camera";
  
    
    private static int sampleUploadCount = 1;
    int fileId = -1;
	private int numberImages=0;

    /**
     * <p>Used with f:setPropertyActionListener to set current fileId.</p>
     *
     * @param fileId
     */
    public void setIdToDelete(int fileId)  {
        this.fileId = fileId;
    }
    public void setCameraImage(Map cameraImage) {
        this.cameraImage = cameraImage;
        File imageFile;
        if (null==cameraImage){
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
                this.pathToFile = imageFile.getAbsolutePath();
                String relativePath =  cameraImage.get("relativePath").toString();
                this.setPathToFile(relativePath);
                logger.info("Real PATH TO FILE+"+this.getPathToFile());
             //   logger.info("Retrieved Camera Image adding to ImageStore");
                
                // try for a little clean up after
                imageFile.deleteOnExit();
              
            } else {
                logger.info("Camera Image Retrieval failed, loading test file from jar");
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
            e.printStackTrace();
        }
        // process the image and add the image store.
        if (image != null) {
            // scale the original file into a small thumbNail and the other
            // into a 1 megapixelish sized image.
            this.width = image.getWidth();
            this.height = image.getHeight();
            image.flush();
        }
        else{
            System.out.println("image is null!!!!");
        }
    }

    public Map getCameraImage() {
        return cameraImage;
    }

    public List getFileList() {
        return fileList;
    }
    
    public String getPathToFile() {
		return pathToFile;
	}
	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}
	public int getNumberImages() {
		return numberImages;
	}
	public void setNumberImages(int numberImages) {
		this.numberImages = numberImages;
	}
	public boolean isEmptyFileList() {
        return fileList.isEmpty();
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
	
    public boolean isEnhancedBrowser()  {
        return EnvUtils.isEnhancedBrowser(FacesContext.getCurrentInstance());
    }

    public void methodOne(ValueChangeEvent event){
        // use this method to check filetype and can possibly remove the file if not correct filetype
         if (event!=null){
            Map compMap = (HashMap)event.getNewValue();
            File fname = (File)compMap.get(CAMERA_KEY_FILE);
            String filePath = fname.getAbsolutePath();
            MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
            String mimeType = mimeTypesMap.getContentType(filePath);
            if (mimeType.equals("image/jpeg") || mimeType.equals("image/png")){
                 messageFromAL="valid image uploaded of jpg or png";
            } else {
               messageFromAL = "invalid upload so can delete without user being able to access";
            }
         }
        logger.info("ValueChangeListener  event is null");
    }

    public String getMessageFromAL() {
        return messageFromAL;
    }

    public void setMessageFromAL(String messageFromAL) {
        this.messageFromAL = messageFromAL;
    }
    //should have predestroy method to get rid of files???

}
