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

package org.icefaces.mobile;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Formatter;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.util.EnvUtils;

/**
 * <p>The VideoBean is used to test camcorder component.</p>
 */
@ManagedBean(name="videoBean")
@ApplicationScoped
public class VideoBean implements Serializable {
    private static final Logger logger =
        Logger.getLogger(VideoBean.class.toString());
    private int idCounter = 0;
    private Map clip = new HashMap<String, Object>();
    private String clipName = "icefaces.mp4";
    private boolean videoClipAvailable = false;
    private String videoSize="nothing yet";
    private String contentType = "";
    private String pathToFile ="icefaces.mp4";
    private Map clip2 = new HashMap<String, Object>();
        
    public VideoBean(){

    }
   
    public void setClip(Map videoInfo) { 
    	this.clip = videoInfo;
        File videoFile = (File) videoInfo.get("file");
        if (null != videoFile)  {
            String commandTemplate = 
                    FacesContext.getCurrentInstance().getExternalContext()
                    .getInitParameter("org.icemobile.videoConvertCommand");
            if (null != commandTemplate)  {
                try {
                    File converted = File.createTempFile("video", ".mp4");
                    StringBuilder command = new StringBuilder();
                    Formatter formatter = new Formatter(command);
                    formatter.format(commandTemplate, 
                            videoFile.getAbsolutePath(), 
                            converted.getAbsolutePath());
                    Runtime runtime = Runtime.getRuntime();
                    Process process = runtime.exec(command.toString());
                    process.waitFor();
                    videoFile.delete();
                    File videoDir = videoFile.getParentFile();
                    File newVideo = new File(videoDir, converted.getName());
                    converted.renameTo(newVideo);
                    videoFile = newVideo;
                    logger.info("Completed video conversion for " +
                            videoFile.getAbsolutePath());
                } catch (Throwable t)  {
                    t.printStackTrace();
                }
            } 
            this.setClipName(videoFile.getName());
            System.out.println("VIDEO FILE: " + this.clipName);   
            try {
                ExternalContext externalContext = FacesContext
                        .getCurrentInstance().getExternalContext();
                String rootPath = externalContext.getRealPath("/");
                String absolutePath = videoFile.getAbsolutePath();
                String urlPath = absolutePath.substring(rootPath.length() - 1);
                setPathToFile(urlPath);
                videoSize = String.valueOf(videoFile.length());
                this.videoClipAvailable = true;
                this.contentType =(String) videoInfo.get("contentType");
            }
             catch (Exception e) {
                e.printStackTrace();
            }

        }else{
        	logger.warning("soundfile is empty in backing bean");
        }
        	
    } 
 
    private String getName(Map videoInfo) {
    	this.clipName = "nothing yet";
    	File thisFile = (File)videoInfo.get("file");
		if (null != thisFile){
			this.clipName=thisFile.getName();
		}
		return this.clipName;
	}

    String videoURL = "../video/video.mp4";
    public String getVideoURL()  {
    	  FacesContext facesContext = FacesContext.getCurrentInstance();
          String rootPath = facesContext.getExternalContext()
                .getRealPath("/video/");
        return "../video/"+this.getClipName();
    }

    public String getClipName() {
		return clipName;
	}


	public boolean isVideoClipAvailable() {
		return videoClipAvailable;
	}


	public void setSoundClipAvailable(boolean soundClipAvailable) {
		this.videoClipAvailable = soundClipAvailable;
	}


	public void setClipName(String clipName) {
		this.clipName = clipName;
	}


	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
 
	public String getPathToFile(){
		return this.pathToFile;
	}
	public void setPathToFile(String ptf){
		this.pathToFile= ptf;
	}
    public Map getClip(){
        return this.clip;
    }

	public Map getClip2() {
		return clip2;
	}

	public void setClip2(Map clip2) {
		this.clip2 = clip2;
	}


    public boolean isEnhancedBrowser()  {
        return EnvUtils.isEnhancedBrowser(FacesContext.getCurrentInstance());
    }

    public String reset()  {
        pathToFile = "icefaces.mp4";
        return "";
    }

    private String messageFromAL = "ValueChangeListener -";

     public void methodOne(ValueChangeEvent event){
        //going to use this method to check filetype and remove the file if not correct filetype
          if (event!=null){
             Map compMap = (HashMap)event.getNewValue();
             File fname = (File)compMap.get("file");
             String filePath = fname.getAbsolutePath();
             MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
             String mimeType = mimeTypesMap.getContentType(filePath);
             this.setContentType(mimeType);
             if (mimeType.equals("video/mpeg") || mimeType.equals("video/mov") ||
                 mimeType.equals("video/3gpp") || mimeType.equals("video/mp4")){
                 messageFromAL="valid video uploaded of mpeg or move or amr or 3gpp or mp4";
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
}
