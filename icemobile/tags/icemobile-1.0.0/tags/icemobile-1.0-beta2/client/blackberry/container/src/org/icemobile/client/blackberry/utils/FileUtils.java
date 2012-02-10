/*
 * Copyright (c) 2011, ICEsoft Technologies Canada Corp.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */ 
package org.icemobile.client.blackberry.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import org.icemobile.client.blackberry.ICEmobileContainer;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.io.Base64OutputStream;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.JPEGEncodedImage;

/**
 * Eventually (post alpha), the container will support saving the files in user 
 * configurable locations. This class will manage creating unique filenames 
 * for various media files based on user configuration and availability. For 
 * now, just save stuff in the store. 
 */
public class FileUtils {

    private static final String AUDIO_PATH;
    private static final String CAMERA_PATH;
    private static final String VIDEO_PATH;

    static SimpleDateFormat dateTime = new SimpleDateFormat("ddmmyy-HHmmss");

    static { 
    	CAMERA_PATH = System.getProperty("fileconn.dir.photos");
    	VIDEO_PATH = System.getProperty("fileconn.dir.videos");
    	String ap = System.getProperty("fileconn.dir.music");
    	int spos = ap.indexOf("/music/");
    	if (spos > -1 ) { 
    		AUDIO_PATH = ap.substring(0, spos) + "/voicenotes/";
    	} else { 
    		AUDIO_PATH = ap;
    	}
    }
    
    /**
     * get a unique filename for a video. This is based on the 
     *  "'/device/devicepath'/videos/date-time-" + xxxxx where the client provides 
     * the xxxxx. 
     * 
     * @param postfix the concrete filename and extension 
     * @return A unique filename located in the device storage 
     */
    public static String getVideoFileURL(String postfix) { 

        StringBuffer builder = new StringBuffer(VIDEO_PATH);
        Date d = new Date (System.currentTimeMillis());
        builder.append( dateTime.format( d ) ); 
        builder.append("-").append(postfix);

        return builder.toString();

    }

 

    /**
     * get a unique file prefix for the system store for video. This is based on the 
     * root "file:///store/home/user/date-time-" + xxxxx where the client provides 
     * the xxxxx. This methods puts the date and time in the filename, so that a new 
     * directory is not created in each case 
     * 
     * @param postfix the concrete filename and extension 
     * @return A unique filename located in the device storage 
     */
    public static String getPictureFileURL(String postfix) { 

        StringBuffer builder = new StringBuffer(CAMERA_PATH);
        Date d = new Date (System.currentTimeMillis());
        builder.append( dateTime.format( d ) ); 
        builder.append("-").append(postfix);

        return builder.toString();

    }


    /**
     * get a unique file prefix for the system store for video. This is based on the 
     * root "file:///store/home/user/date-time-" + xxxxx where the client provides 
     * the xxxxx. This methods puts the date and time in the filename, so that a new 
     * directory is not created in each case 
     * 
     * @param postfix the concrete filename and extension 
     * @return A unique filename located in the device storage 
     */
    public static String getAudioFileURL(String postfix) { 

        StringBuffer builder = new StringBuffer(AUDIO_PATH);
        Date d = new Date (System.currentTimeMillis());
        builder.append( dateTime.format( d ) ); 
        builder.append("-").append(postfix);

        return builder.toString();

    }

    
    /**
     * fetch the contents of an image resource from the filesystem as base 64 
     * encoded String; 
     * @param filename 
     * @return Base 64 encoded string. 
     */
    public static String getImageResourceBase64 (String resourcename ) { 

        String returnVal = null;
        byte[] capturedThumb;
        Bitmap icon = Bitmap.getBitmapResource( resourcename );
        EncodedImage img = JPEGEncodedImage.encode( icon, 100 );
        capturedThumb = img.getData(); 
        ByteArrayOutputStream bos = new ByteArrayOutputStream ( capturedThumb.length);
        Base64OutputStream b64os = new Base64OutputStream( bos );
        try { 
            b64os.write( capturedThumb );
            b64os.flush();
            
            bos.flush(); 

            returnVal = bos.toString();
            
        } catch (IOException ioe) { 
            ICEmobileContainer.ERROR("Exception reading resource: " + resourcename + ": " + ioe);
        } finally { 
            if (b64os != null) {
                try { 
                    b64os.close();
                } catch (Exception e) {}
            }
            if (bos != null) { 
                try { 
                    bos.close(); 
                } catch (Exception e) {}
            }
        }
        return returnVal;
    } 

}
