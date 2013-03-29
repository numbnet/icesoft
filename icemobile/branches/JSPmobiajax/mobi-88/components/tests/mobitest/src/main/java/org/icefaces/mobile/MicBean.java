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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.FileNameMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Formatter;

import javax.activation.MimetypesFileTypeMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * <p>The MicBean is used to test microphone component.</p>
 */
@ManagedBean(name="micBean")
@ApplicationScoped
public class MicBean implements Serializable {
    
    private int idCounter = 0;
    private Map clip = new HashMap<String, Object>();
    private String clipName = "icemobile.mp4";
    private boolean soundClipAvailable = false;
    private String soundSize="nothing yet";
	private byte[] clipInBytes;
	private String audioContentType = "audio/mpeg";
    private String pathToFile = "../audio/icemobile.mp4";
	private Map clip2 = new HashMap<String, Object>();
	private int maxtime=200;

    // files associated with the current user
    private final List fileList =
            Collections.synchronizedList(new ArrayList());

    public MicBean(){
    	try{   
    	  this.prepIPadClip();
    	}catch (Exception e){
    		System.out.println("could not convert iPadClip to byte array");
    	}
    }   
   
    public void setClip(Map soundInfo) { 
    	this.clip = soundInfo;
        File soundFile = (File) soundInfo.get("file");
        if (null != soundFile)  {
            String commandTemplate = 
                    FacesContext.getCurrentInstance().getExternalContext()
                    .getInitParameter("org.icemobile.audioConvertCommand");
            if (null != commandTemplate)  {
                try {
                    File converted = File.createTempFile("mobile", ".mp4");
                    StringBuilder command = new StringBuilder();
                    Formatter formatter = new Formatter(command);
                    formatter.format(commandTemplate, 
                            soundFile.getAbsolutePath(), 
                            converted.getAbsolutePath());
                    Runtime runtime = Runtime.getRuntime();
                    Process process = runtime.exec(command.toString());
                    process.waitFor();
                    soundFile.delete();
                    converted.renameTo(soundFile);
                    System.out.println("Completed audio conversion for " + 
                            soundFile.getAbsolutePath());
                } catch (Throwable t)  {
                    t.printStackTrace();
                }
            } 
         	 this.setClipName(soundFile.getName());
             System.out.println("SOUNDFILE not empty in backing bean has name="+this.clipName);   
            try {
               String relativePath = clip.get("relativePath").toString();
               System.out.println(" path to file="+relativePath);
               this.setPathToFile(relativePath);
               soundSize = String.valueOf(soundFile.length());
               System.out.println("new soundSize " + soundSize);
               this.soundClipAvailable = true;
               this.audioContentType =(String) soundInfo.get("contentType");        
            }
             catch (Exception e) {
                e.printStackTrace();
            }

        }else{
        	System.out.println("soundfile is empty in backing bean");
        }
        	
    } 

    private String getName(Map soundInfo) {
		if (null != soundInfo.get("file")){
			this.clipName=soundInfo.get("fileName").toString();
		}
		return this.clipName;
	}
    String sampleBite = "sample.mp4";
    
    public String getSampleBite() {
    	FacesContext facesContext = FacesContext.getCurrentInstance();
        String rootPath = facesContext.getExternalContext()
        .getRealPath("/audio/");
        System.out.println("ROOTPATH of audio url is ="+rootPath);
		return rootPath+sampleBite;
	}
	public void setSampleBite(String sampleBite) {
		this.sampleBite = sampleBite;
	}
	
	private String sampleIPad;
	public String getSampleIPad() throws Exception{
		return sampleIPad;
	}
	String androidURL = "../audio/sampleAndroid.mp4";
    public String getAndroidURL()  {
        return androidURL;
    }
    String bberryURL = "../audio/sound.amr";
    public String getBberryURL(){
    	return bberryURL;
    }
	
	String audioURL = "../audio/sample.mp4";
    public String getAudioURL()  {
    	  FacesContext facesContext = FacesContext.getCurrentInstance();
          String rootPath = facesContext.getExternalContext()
                .getRealPath("/audio/");
          System.out.println("ROOTPATH of audio url is ="+rootPath);
        return "../audio/"+this.getClipName();
    }

    public String getClipName() {
		return clipName;
	}


	public boolean isSoundClipAvailable() {
		return soundClipAvailable;
	}

    public String getAudioContentType(){
    	return this.audioContentType;
    }
	public void setSoundClipAvailable(boolean soundClipAvailable) {
		this.soundClipAvailable = soundClipAvailable;
	}


	public void setClipName(String clip) {
		this.clipName = clip;
	}


	private void prepIPadClip() throws FileNotFoundException {
		   File clipFile;
		   FacesContext facesContext = FacesContext.getCurrentInstance(); 
		    String sampleFilePath = facesContext.getExternalContext() 
		    .getRealPath("/audio/") 
		    .concat(File.separator) 
		    .concat("sampleIPad" + ".mp4"); 
		    clipFile = new File(sampleFilePath); 
		    try{
		       System.out.println("\t\t  MIMETYPE of iPadClip="+this.getMimeType(sampleFilePath));
		    }catch (Exception e){
		    	System.out.println("could not get mimetype for iPadClip");
		    }
		   

		//Buffer some bytes. 

		   FileInputStream fis = new FileInputStream(clipFile); 
		   ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		   byte[] buf = new byte[1024]; 
		   try { 
		      for (int readNum; (readNum = fis.read(buf)) != -1;) { 
		        bos.write(buf, 0, readNum); //no doubt here is 0 
		     } 
		   } catch (IOException ex) { 
		       System.out.println("Error writing out stream"); 
		   } 
			this.clipInBytes = bos.toByteArray();
//			this.getContentTypes();
			
		}
	public byte[] getClipInBytes() {
		return clipInBytes;
	}

	public void setClipInBytes(byte[] clipInBytes) {
		this.clipInBytes = clipInBytes;
	}

    public String getMimeType(String fileUrl)
		      throws java.io.IOException
		{
		     String type = new MimetypesFileTypeMap().getContentType(fileUrl);
		     return type;
	    }
    public void getContentTypes(){
    	System.out.println(" ===========MIMETYPES=============");
    	try{
    	System.out.println(" iPad mimetype = "+getContentType("http://10.18.39.112:8080/mobitest/audio/sampleIPad.mp4"));
    	System.out.println(" Android mimetype = "+getContentType("http://10.18.39.112:8080/mobitest/sampleAndroid.mp4"));
    	System.out.println(" resource filetype = "
    			+getContentType( "http://10.18.39.112:8080/mobitest/javax.faces.resource/audioplaybytes.jsf"));
    	}catch (Exception e){
    		
    	}
    }
    //very slow way to get contentType or mimeType of file.  Quicker to use commons file logging or activation jar
    String contentType = "none";
	public String getContentType(String fileext) throws Exception{
		System.out.println("trying to get contentType of fileext="+fileext);
		contentType =  new MimetypesFileTypeMap().getContentType(fileext);
	    System.out.println("\t\t contentType="+contentType);
		return contentType;		
	}

	public String getPathToFile() {
		return pathToFile;
	}

	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}

	public Map getClip2() {
		return clip2;
	}

	public void setClip2(Map clip2) {
		this.clip2 = clip2;
	}

	public int getMaxtime() {
		return maxtime;
	}

	public void setMaxtime(int maxtime) {
		this.maxtime = maxtime;
	}
		

    public String reset()  {
        pathToFile = "../audio/icemobile.mp4";
        return "";
    }		

}
