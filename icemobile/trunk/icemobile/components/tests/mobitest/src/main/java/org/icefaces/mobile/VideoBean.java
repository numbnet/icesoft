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

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Formatter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.util.EnvUtils;

/**
 * <p>The MicBean is used to test camera component.</p>
 */
@ManagedBean(name="videoBean")
@ApplicationScoped
public class VideoBean implements Serializable {
    
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
                    System.out.println("Completed video conversion for " + 
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
             //  System.out.println("new soundSize " + videoSize);
               this.videoClipAvailable = true;
               this.contentType =(String) videoInfo.get("contentType");        
            }
             catch (Exception e) {
                e.printStackTrace();
            }

        }else{
        	System.out.println("soundfile is empty in backing bean");
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
       //   System.out.println("ROOTPATH="+rootPath);
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

    private String messageFromAL = " ValueChangenListener fired ";

    public void methodOne(ValueChangeEvent event){
        this.messageFromAL =" uploaded file ="+this.getClipName()+" sucessfully";
    }

    public String getMessageFromAL() {
        return messageFromAL;
    }

    public void setMessageFromAL(String messageFromAL) {
        this.messageFromAL = messageFromAL;
    }
}
