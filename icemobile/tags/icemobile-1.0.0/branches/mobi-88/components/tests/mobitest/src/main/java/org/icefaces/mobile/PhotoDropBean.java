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
    private String subject = "subject";
    private String message = "message";
    private int count1 = 0;
    private int count2 = 0;
    private int count3 = 0;
    private int count4 = 0;

    public PhotoDropBean() {
    }

    private String room1Code = "room1";
    private String room2Code = "room2";
    private String room3Code = "room3";
    private String room4Code = "room4";

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
        return "/images/room1.jpg?r=" + count1;
    }

    public String getRoom2Path()  {
        return "/images/room2.jpg?r=" + count2;
    }

    public String getRoom3Path()  {
        return "/images/room3.jpg?r=" + count3;
    }

    public String getRoom4Path()  {
        return "/images/room4.jpg?r=" + count4;
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
        String destFileName = baseDir + File.separator;

        if (uploadedCode.equals(room1Code))  {
            destFileName += "room1.jpg";
            count1++;
        } else if (uploadedCode.equals(room2Code))  {
            destFileName += "room2.jpg";
            count2++;
        } else if (uploadedCode.equals(room3Code))  {
            destFileName += "room3.jpg";
            count3++;
        } else if (uploadedCode.equals(room4Code))  {
            destFileName += "room4.jpg";
            count4++;
        }
        if (null != uploadedFile)  {
            uploadedFile.renameTo(new File(destFileName));
        }

        PushRenderer.render(GROUP);
        return "";
    }

}
