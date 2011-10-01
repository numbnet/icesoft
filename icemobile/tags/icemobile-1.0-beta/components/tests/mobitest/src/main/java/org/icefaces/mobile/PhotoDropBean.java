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
import java.util.Map;

import org.icefaces.application.PushRenderer;
import org.icefaces.application.PushMessage;

import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ApplicationScoped;



/**
 * <p>A Stores photo and QR Code state for four "rooms".</p>
 */
@ManagedBean
@ApplicationScoped
public class PhotoDropBean  {
    public static final String GROUP = "photodrop";
    private int count = 0;

    public PhotoDropBean() {
    }

    private String room1Code = "One";
    private String room2Code = "Two";
    private String room3Code = "Three";
    private String room4Code = "Four";
    
    private String room1Name = "room1.jpg";
    private String room2Name = "room2.jpg";
    private String room3Name = "room3.jpg";
    private String room4Name = "room4.jpg";

    public String getRoom1Code() {
        PushRenderer.addCurrentSession(GROUP);
        return room1Code;
    }

    public void setRoom1Code(String code)  {
        this.room1Code = code;
    }

    public String getRoom2Code() {
        return room2Code;
    }

    public void setRoom2Code(String code)  {
        this.room2Code = code; 
    }   
    
    public String getRoom3Code() {
        return room3Code;
    }

    public void setRoom3Code(String code)  {
        this.room3Code = code; 
    }   
    
    public String getRoom4Code() {
        return room4Code;
    }

    public void setRoom4Code(String code)  {
        this.room4Code = code; 
    }   
    
    public String getRoom1Path()  {
        return "/images/" + room1Name;
    }

    public String getRoom2Path()  {
        return "/images/" + room2Name;
    }

    public String getRoom3Path()  {
        return "/images/" + room3Name;
    }

    public String getRoom4Path()  {
        return "/images/" + room4Name;
    }

    private String uploadedCode = "";

    public void setUploadedCode(String code)  {
        this.uploadedCode = code;
    }

    public String getUploadedCode()  {
        return uploadedCode;
    }

    public File uploadedFile = null;

    public void setUploadedPhoto(Map photo)  {
        uploadedFile = (File) photo.get("file");
    }

    public String upload()  {
        String baseDir = FacesContext.getCurrentInstance().getExternalContext()
            .getRealPath("/images/");
        String destDirName = baseDir + File.separator;
        String oldName = "";
        count++;
        String newName = "upload" + String.valueOf(count) + ".jpg";

        if (uploadedCode.equals(room1Code))  {
            oldName = room1Name;
            room1Name = newName;
        } else if (uploadedCode.equals(room2Code))  {
            oldName = room2Name;
            room2Name = newName;
        } else if (uploadedCode.equals(room3Code))  {
            oldName = room3Name;
            room3Name = newName;
        } else if (uploadedCode.equals(room4Code))  {
            oldName = room4Name;
            room4Name = newName;
        }
        if (null != uploadedFile)  {
            uploadedFile.renameTo(new File(destDirName + newName));
            File oldFile = new File(destDirName + oldName);
            oldFile.delete();
        }

        PushRenderer.render(GROUP);
        return "";
    }

}
