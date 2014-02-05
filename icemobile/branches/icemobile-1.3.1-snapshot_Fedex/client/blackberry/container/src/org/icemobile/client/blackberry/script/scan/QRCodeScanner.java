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

package org.icemobile.client.blackberry.script.scan;

import java.util.Hashtable;

import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import org.icemobile.client.blackberry.ContainerController;
import net.rim.device.api.amms.control.camera.EnhancedFocusControl;
import net.rim.device.api.barcodelib.BitmapLuminanceSource;
import net.rim.device.api.script.ScriptableFunction;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;

import org.icemobile.client.blackberry.Logger;
import org.icemobile.client.blackberry.utils.NameValuePair;
import org.icemobile.client.blackberry.utils.UploadUtilities;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

public class QRCodeScanner extends ScriptableFunction {
    
    private ContainerController mContainer;
    private VideoControl mVideoControl;
    private MainScreen mScanScreen;
    private Player mPlayer;
    
    // maxHeight and maxWidth are markup specified desired sizes and may be any size
    private int maxWidth = 1024; 
    private int maxHeight = 768; 
    private String mFieldId;
    
    // These sizes are sizes of images captured by the camera phone (only 3 sizes supported)
    private int mCapturedWidth = 1024;
    private int mCapturedHeight = 768;
    
//    private int mIndex;
//    private int mLastTotalSize;   
     

    public QRCodeScanner(ContainerController controller) { 
        mContainer = controller;      
    }   

    /**
     * Take a photo. The return value from this call is TRUE if the 
     * invocation went ok. The actual results will be defined later via an asynchronous 
     * callback when the photo is actually captured and written.  
     * 
     */
    public  Object invoke( Object thiz, Object[] args) { 
        
//    	MemoryStats ms = Memory.getRAMStats(); 
//    	
//    	Logger.DEBUG("QRCode scan: " + mIndex++ + ", allocated: " + ms.getAllocated() + ", free: " + ms.getFree() + 
//    			", total size: " + ms.getObjectSize() + ", needed: " + Memory.getMemoryNeeded() ); 
//    	mLastTotalSize = ms.getObjectSize();
    			
    			
    	
        Hashtable params = new Hashtable();
        String supportedEncodings = System.getProperty("video.snapshot.encodings"); 
        if (supportedEncodings.trim().length() == 0) { 
        	Logger.DIALOG("Photo capture not supported"); 
            return Boolean.FALSE;
        }
        
        if (args.length == 2) { 
            mFieldId = (String) args[0];
         
            if (args[1] instanceof Object) { 
                // indicates arguments not being passed 
            } else { 
                params = UploadUtilities.getNameValuePairHash((String)args[1], "=", "&");
            } 
            
        } else { 
        	Logger.ERROR("ice.qrCode - wrong number of arguments");
            return Boolean.FALSE; 
        }     
        
        try { 

            NameValuePair temp; 
            
            temp = (NameValuePair) params.get("maxwidth");
            if (temp != null) {
                maxWidth = Integer.parseInt(temp.getValue() ); 
            }
            temp = (NameValuePair) params.get("maxheight"); 
            if (temp != null) { 
                maxHeight = Integer.parseInt( temp.getValue() );
            }
                
        } catch (Exception e) { 
        	Logger.ERROR("ice.qrCode - size error in js: " + e);
        }
        

        String[] encodings = UploadUtilities.split(supportedEncodings, " "); 
//        for (int idx = 0; idx < encodings.length; idx ++ ) { 
//            Logger.DEBUG("- " + encodings[idx]);
//        }
        int [] encodingResolutions = parseEncodingStrings( encodings );        
        final int [] leastNecessaryResolution = findMinimumNecessary ( encodingResolutions, maxWidth, maxHeight); 

        invokeDeviceCamera(leastNecessaryResolution);
        return Boolean.TRUE;
    }

    /**
     * Build the screen and apply the camera widgets therein. 
     * @param leastNecessaryResolution
     */
    private void invokeDeviceCamera(final int[] leastNecessaryResolution) {
        UiApplication.getUiApplication().invokeLater( new Runnable() {
            public void run() { 
            	
                try { 
                	
                	
                    // If there is a smaller size detected that can satisfy the needs, use it, or just 
                    // default to the devices default size. 
                    if (leastNecessaryResolution[0] != 0) { 
                        mCapturedWidth = leastNecessaryResolution[0]; 
                        mCapturedHeight = leastNecessaryResolution[1];
                        mPlayer = javax.microedition.media.Manager.createPlayer( 
                                "capture://video?encoding=jpeg&width=" + Integer.toString(mCapturedWidth) + 
                                "&height=" + Integer.toString(mCapturedHeight) );
                    } else { 
                        mPlayer = javax.microedition.media.Manager.createPlayer( "capture://video?encoding=jpeg" );
                    }
                    mPlayer.realize();
                    mVideoControl = (VideoControl) mPlayer.getControl("VideoControl");

                    if (mVideoControl != null) { 

                        Field videoField = (Field) mVideoControl.initDisplayMode
                        	(VideoControl.USE_GUI_PRIMITIVE, "net.rim.device.api.ui.Field");
                        mVideoControl.setDisplayFullScreen(true);
                        mVideoControl.setVisible(true);

                        mPlayer.start();

                        EnhancedFocusControl efc =
                            (EnhancedFocusControl) mPlayer.getControl("net.rim.device.api.amms.control.camera.EnhancedFocusControl");
                        if (efc.isAutoFocusSupported() && !efc.isAutoFocusLocked()) { 
//                            efc.startAutoFocus();
                        } 
                        if(videoField != null) {        
                        	mScanScreen = new QRCameraScreen();                            
                            
                            synchronized (Application.getEventLock()) { 
                            	mScanScreen.add( videoField );
                            	UiApplication.getUiApplication().pushScreen( mScanScreen );
                            }
                        } else { 
                        	Logger.ERROR("Camera widget not found");
                        }
                    }
                }
                catch(Exception e) {        
                	Logger.ERROR("ice.qrCode - Exception in image processing: " + e.toString());
                }

            }});
    }
    
    

    class QRCameraScreen extends MainScreen { 
        protected boolean invokeAction(int action) { 

            boolean handled = super.invokeAction(action); 
            if(!handled) { 

                if(action == ACTION_INVOKE) { 

                    UiApplication.getUiApplication().invokeLater( new Runnable() {
                        public void run() {

                            try {                              
                                
                                long startTime = System.currentTimeMillis();
                                // Get the orientation at photo time, not when processing! 
                                byte[] rawImage = mVideoControl.getSnapshot(null);
                                synchronized (Application.getEventLock()) { 
                                	UiApplication.getUiApplication().popScreen( mScanScreen );
                                } 
                                Logger.TIME(startTime, "ice.qrCode capturing scan bytes" );   
                                
                                startTime = System.currentTimeMillis();
                                Bitmap qrcodeBitmap = Bitmap.
                                        createBitmapFromBytes(rawImage, 
                                        0, 
                                        rawImage.length,
                                        1 );
                                Logger.TIME(startTime, "ice.qrCode creating bitmap" );   
                                
                                startTime = System.currentTimeMillis();
                                Result[] results = processQRCodeBitmap(qrcodeBitmap);
                                if (results.length == 0) { 
                                	Logger.DIALOG("ice.qrCode found no codes in scan.. Please try again. ");                                               
                                } else { 
                                	Logger.TIME(startTime, "ice.qrCode processing qrcode" );  
                                    mContainer.insertQRCodeScript( mFieldId, results[0].getText());
                                }                                 
                                
//                                MemoryStats ms = Memory.getRAMStats(); 
//                                Logger.DEBUG("ice.qrCode - done, delta consumed: " + (ms.getObjectSize() - mLastTotalSize)) ;
                                
                            } catch(Exception e) {
                            	Logger.ERROR("ice.qrCode - exception: " + e);
//
                            } finally {
                                cleanup();                                
                            }
                        } 
                    });
                }
            }           
            return handled;                
        }
        
        private void cleanup() { 
        	synchronized ( Application.getEventLock()) {
        		
        		if (mScanScreen != null) { 
        			mScanScreen.deleteAll();
                    mScanScreen = null;
        		}
        		mVideoControl = null; 
        		if (mPlayer != null) { 
        			mPlayer.close(); 
        			mPlayer = null; 
        		}
        	}     	
        }

        private Result[] processQRCodeBitmap(Bitmap qrcodeBitmap)  {
            
            Result[] qrCodes = new Result[0];
            try { 
                BitmapLuminanceSource bls = new BitmapLuminanceSource(qrcodeBitmap);
                Logger.DEBUG("ice.qrCode - 1 of 4 BitmapLuminance obtained: width: " + 
                        bls.getWidth() + ", height: " + bls.getHeight() +  
                        ", canCrop: " + bls.isCropSupported() + ", canRotate: " + bls.isRotateSupported());
                
                Binarizer lbb = new GlobalHistogramBinarizer( bls ); 
                Logger.DEBUG("ice.qrCode - 2 of 4 Binarizer obtained"); 
                BinaryBitmap bb = new BinaryBitmap( lbb ); 
                Logger.DEBUG("ice.qrCode - 3 of 4 BinaryBitmap obtained"); 
                MultipleBarcodeReader mbr = new QRCodeMultiReader();
             
                qrCodes = mbr.decodeMultiple(bb);
                Logger.DEBUG("ice.qrCode - 4 of 4 BarcodeResult read, length: " + qrCodes.length); 
                for (int idx = 0; idx < qrCodes.length; idx ++ ) { 
                    Logger.DEBUG("ice.qrCode - found: " + qrCodes[idx].getText());                                   
                }
                
            } catch (Exception e) { 
                Logger.DEBUG("ice.qrCode - exception processing image: " + e);     
            }
            return qrCodes; 
        }       
    }
    
    /**
     * Takes an array of strings with the following format: 
     *   encoding=jpeg&width=2592&height=1944 
     *   encoding=jpeg&width=1200&height=900 
     *   
     *  And returns an array of integers representing the supported widths and heights. 
     *  If a pair of integers is empty, the width/height fields were not found in that
     *  encoding. 
     *  
     * @param encodingArray Array of supported video encodings. 
     * @return filled in array of converted encoding widths and heights. 
     */
    private int[] parseEncodingStrings(String [] encodingArray) { 
        
        int[] returnVal = new int[ encodingArray.length*2];
        Hashtable encTable;
        NameValuePair temp; 
        for (int idx = 0; idx < encodingArray.length; idx ++ ) {
            encTable = UploadUtilities.getNameValuePairHash(encodingArray[idx], "=", "&"); 
            
            temp = (NameValuePair)encTable.get("width"); 
            if (temp != null) { 
                returnVal[idx*2] = Integer.parseInt( temp.getValue() ); 
            }
            
            temp = (NameValuePair)encTable.get("height"); 
            if (temp != null) { 
                returnVal[idx*2+1] = Integer.parseInt( temp.getValue() );
            }
        }
        return returnVal;
    }
    

    
    /**
     * From the devices supported encodings, find the smallest choice that 
     * is larger than both maxWidth and maxHeight. We will downscale the 
     * image to the exact sizing later but it's faster to work with the 
     * smallest possible images.  
     * 
     * @param supportedEncodings
     * @param maxWidth the maximum desired width
     * @param maxHeight the maximum desired height
     * @return Size 2 int [] with val[0] = width, val[1] = height 
     */
    private int[] findMinimumNecessary(int[] supportedEncodings, int maxWidth, int maxHeight) {
        
        int returnVal[] = new int[2];
        int encodingCount = supportedEncodings.length/2;
        for (int idx = 0; idx < encodingCount; idx ++) { 
            if (supportedEncodings[idx*2] >= maxWidth && supportedEncodings[idx*2+1] >= maxHeight) { 
                returnVal[0] = supportedEncodings[idx*2]; 
                returnVal[1] = supportedEncodings[idx*2+1];
            }
        } 
        return returnVal;
    }
}