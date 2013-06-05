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
package org.icemobile.client.blackberry.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import org.icemobile.client.blackberry.Logger;

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
        	Logger.ERROR("Exception reading resource: " + resourcename + ": " + ioe);
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
