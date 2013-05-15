/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.logging.Logger;

@ManagedBean(name="imageBean")
@SessionScoped
public class ImageBean implements Serializable {
	private static final Logger logger =
        Logger.getLogger(ImageBean.class.toString());	
	int cameraSampleUploadCount=1;

	private int height;
	private int width;
	private byte[] fileInBytes;
	
	public ImageBean(){

	}
	@PostConstruct
	public void prepFile() {

        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String sampleImagePath = facesContext.getExternalContext()
                      .getRealPath("/images/DSC_2.JPG");
            File imageFile = new File(sampleImagePath);
            imageFile = new File(sampleImagePath);;
           //Buffer some bytes.

           FileInputStream fis = new FileInputStream(imageFile);
           ByteArrayOutputStream bos = new ByteArrayOutputStream();
           byte[] buf = new byte[1024];
	       for (int readNum; (readNum = fis.read(buf)) != -1;) {
	            bos.write(buf, 0, readNum); //no doubt here is 0
                this.fileInBytes= bos.toByteArray();
	       }
	    } catch (Exception ex) {
	       logger.warning("Error writing out stream");
           ex.printStackTrace();
	    }

	 }

	public int getCameraSampleUploadCount() {
		return cameraSampleUploadCount;
	}

	public void setCameraSampleUploadCount(int cameraSampleUploadCount) {
		this.cameraSampleUploadCount = cameraSampleUploadCount;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public byte[] getFileInBytes() {
		return fileInBytes;
	}

	public void setFileInBytes(byte[] fileInBytes) {
		this.fileInBytes = fileInBytes;
	}

}
