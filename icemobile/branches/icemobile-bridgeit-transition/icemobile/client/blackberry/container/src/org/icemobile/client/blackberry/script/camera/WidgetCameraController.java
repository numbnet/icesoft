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
package org.icemobile.client.blackberry.script.camera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import org.icemobile.client.blackberry.ContainerController;
import org.icemobile.client.blackberry.Logger;
import org.icemobile.client.blackberry.utils.FileSelectorPopupScreen;
import org.icemobile.client.blackberry.utils.NameValuePair;
import org.icemobile.client.blackberry.utils.FileUtils;
import org.icemobile.client.blackberry.utils.ImageManipulator;
import org.icemobile.client.blackberry.utils.UploadUtilities;

import net.rim.device.api.amms.control.camera.EnhancedFocusControl;
import net.rim.device.api.io.Base64OutputStream;
import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.script.ScriptableFunction;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.JPEGEncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

/**
 * This Camera controller gains control of the camera directly in a widget in a screen 
 * for placement in the screen hierarchy and receives an image directly from the 
 * camera widget when a photo is taken. 
 * 
 * The image taken is always in landscape mode. The exif orientation of images 
 * taken by the BlackBerry camera is ALWAYS 6, and the way to get vertically 
 * upright pictures on the server is to take photos with the camera horizontal,
 * keypad to the right. 
 *
 */
public class WidgetCameraController extends ScriptableFunction {


    private ContainerController mController;
    private VideoControl mVideoControl;
    private MainScreen mCameraScreen;
    private int maxWidth = 640;
    private int maxHeight = 480;  
    private String mFieldId;
    private int mThumbWidth; 
    private int mThumbHeight;
    private int mCapturedWidth = 640;
    private int mCapturedHeight = 480;   
   
    private Player mPlayer; 

    public WidgetCameraController(ContainerController controller) { 
        mController = controller; 
    }	

    /**
     * Take a photo. The return value from this call is TRUE if the 
     * invocation went ok. The actual results will be defined later via an asynchronous 
     * callback when the photo is actually captured and written.  
     * 
     */
    public  Object invoke( Object thiz, Object[] args) { 
        Hashtable params = new Hashtable();
        String supportedEncodings = System.getProperty("video.snapshot.encodings"); 
        if (supportedEncodings.trim().length() == 0) { 
            Dialog.alert("Photo capture not supported"); 
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
        	Logger.ERROR("ice.shootPhoto - wrong number of arguments");
            return Boolean.FALSE; 
        }     
        
        try { 

            mThumbWidth = mThumbHeight = 64; 
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
        	Logger.ERROR("ice.shootPhoto - size error in js: " + e);
        }
        

        String[] encodings = UploadUtilities.split(supportedEncodings, " "); 
        for (int idx = 0; idx < encodings.length; idx ++ ) { 
            Logger.DEBUG("- " + encodings[idx]);
        }
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
    						//efc.startAutoFocus();
    					} 
    					if(videoField != null) {	
    						synchronized (UiApplication.getEventLock()) { 
    							mCameraScreen = new CameraScreen();                      
    							UiApplication.getUiApplication().pushScreen(mCameraScreen);
    							mCameraScreen.add(videoField);
    						}
    					}
    				}
    			}
    			catch(Exception e) {		
    				Logger.ERROR("ice.shootPhoto - Exception in image processing: " + e.toString());
    			}

    		}});
    }
    
    /** 
     * Currently unused. Could be used to select a file from the filesystem. 
     */
    private void invokePhotoGallery( ) { 
    	
    	
    	String cameraPath = "file:///SDCard/BlackBerry/"; 
    	if (cameraPath.startsWith("file:///")) { 
    		cameraPath = cameraPath.substring("file:///".length() );
    	}
    	FileSelectorPopupScreen fsp = new FileSelectorPopupScreen(cameraPath, 
    			new String[] { ".jpg" } );
    	fsp.pickFile();
    	String chosenImage = fsp.getFile(); 
    	if (chosenImage == null) { 
    		return; 
    	}
    	
    	// So now I have an opened filename. Need to read, insert as thumbnail, and 
    	processExistingFile(chosenImage);
    	
    	
    }

    class CameraScreen extends MainScreen { 
        protected boolean invokeAction(int action) { 

            boolean handled = super.invokeAction(action); 
            if(!handled) { 

                if(action == ACTION_INVOKE) { 

                	UiApplication.getUiApplication().invokeLater( new Runnable() {
                		public void run() {
                			String localFilename = null;

                			try {
                	            long startTime = System.currentTimeMillis();
                				// Get the orientation at photo time, not when processing! 
                				int capturedOrientation = Display.getOrientation(); 
                				byte[] rawImage = mVideoControl.getSnapshot(null);
                				synchronized (UiApplication.getEventLock()) { 
                                    UiApplication.getUiApplication().popScreen( mCameraScreen );
                                } 
                				Logger.TIME(startTime, "ice.shootPhoto capturing bytes" );
                				processThumbnail( rawImage, capturedOrientation ); 
                				
                				Logger.DEBUG("ice.shootPhoto - capture size: " + rawImage.length);
                				localFilename = FileUtils.getPictureFileURL("image.jpg");

                				if (mCapturedWidth != maxWidth || mCapturedHeight != maxHeight) { 
                					
                					Bitmap original = Bitmap.createBitmapFromBytes(rawImage, 0, rawImage.length, 1);
                					Bitmap uploadBitmap = new Bitmap(maxWidth, maxHeight); 
                					original.scaleInto(uploadBitmap, Bitmap.FILTER_BILINEAR, Bitmap.SCALE_TO_FILL);
                					JPEGEncodedImage u = JPEGEncodedImage.encode( uploadBitmap, 100 );
                					rawImage = u.getData();
                				}
                				startTime = System.currentTimeMillis(); 
                				persistImage(rawImage, localFilename); 
                				Logger.TIME(startTime, "ice.shootPhoto persisting image" );
                				mController.insertHiddenFilenameScript(mFieldId, localFilename ); 
                				Logger.DEBUG("ice.shootPhoto - done");
                			} catch(Exception e) {//
                				Logger.ERROR("ice.shootPhoto - exception: " + e);
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
    }
    
    private void cleanup() { 
    	synchronized ( Application.getEventLock()) {
    		
    		if (mCameraScreen != null) { 
    			mCameraScreen.deleteAll();
                mCameraScreen = null;
    		}
    		mVideoControl = null; 
    		if (mPlayer != null) { 
    			mPlayer.close(); 
    			mPlayer = null; 
    		}
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
     * Handle the case where the user has chosen a file from the file system. 
     * @param filename name of the chosen file
     */
    private void processExistingFile( String filename ) { 
    	
    	FileConnection fc = null; 
    	try { 
    		fc = (FileConnection) Connector.open( filename );
    		
    		InputStream is = fc.openInputStream(); 
    		byte[] data = IOUtilities.streamToBytes(is); 
    		
    		processThumbnail( data, Display.ORIENTATION_LANDSCAPE );     		
    		
    	} catch (Exception e) { 
     		if (fc != null)  { 
     			try { 
     				fc.close(); 
     			} catch (Exception io) {}
     		}
    	}
    	
    	mController.insertHiddenFilenameScript(mFieldId, filename ); 
    	
    }
    
    
    /**
     * Take a set of raw bytes and construct, scale, and insert a thumbnail from them. 
     * 
     * @param rawImage
     */
    private void processThumbnail( byte[] rawImage, int capturedOrientation )  { 
    	
    	Bitmap original = Bitmap.createBitmapFromBytes(rawImage, 0, rawImage.length, 1);		
		Bitmap thumbBitmap = new Bitmap( mThumbWidth, mThumbHeight); 
		original.scaleInto( thumbBitmap, Bitmap.FILTER_BILINEAR, Bitmap.SCALE_TO_FILL);                               

		/**
		 * If the phone is being held like a phone, rotate the thumbnail
		 * 90 degrees clockwise to get it to be upright. Can't do this with
		 * the image itself as the ImageManipulator can't create a
		 * Graphics object big enough to do the work.
		 */
		
		Logger.DEBUG("ice.shootPhoto - camera orientation = " + capturedOrientation);
		if (capturedOrientation == Display.ORIENTATION_PORTRAIT) {
			thumbBitmap = ImageManipulator.rotate( thumbBitmap, -90);
		}

		JPEGEncodedImage thumb = JPEGEncodedImage.encode( thumbBitmap, 70 );
//		MetaDataControl mdc = ((JPEGEncodedImage) thumb ).getMetaData();
////		String[] keys = mdc.getKeys();
//		Logger.DEBUG("ice.shootPhoto - Orientation of rotated thumb = " +
//				mdc.getKeyValue("orientation"));

		byte thumbnailData[] = thumb.getData();

		if (mCapturedWidth != maxWidth || mCapturedHeight != maxHeight) { 
			Bitmap uploadBitmap = new Bitmap(maxWidth, maxHeight); 
			original.scaleInto(uploadBitmap, Bitmap.FILTER_BILINEAR, Bitmap.SCALE_TO_FILL);
			JPEGEncodedImage u = JPEGEncodedImage.encode( uploadBitmap, 100 );
			rawImage = u.getData();
		}

		long startTime = System.currentTimeMillis(); 
		ByteArrayOutputStream bos = new ByteArrayOutputStream (thumbnailData.length);
		Base64OutputStream b64os = new Base64OutputStream( bos );
		
		try { 
			b64os.write( thumbnailData );	
			//							
			b64os.flush();
			b64os.close();				
			bos.flush();
			bos.close();

			Logger.TIME(startTime, "ice.shootPhoto writing thumb" );
			mController.insertThumbnail(mFieldId, bos.toString() );
			
		} catch (IOException ioe) { 
			Logger.ERROR("ice.shootPhoto - Exception writing thumbnail: " + ioe);
		}    	
    }
    
    
    /**
     * If necessary, persist the raw data to a file
     * 
     * @param rawImage
     * @param localFilename
     */
    private void persistImage (byte rawImage[], String localFilename) { 
    	
    	FileConnection fconn = null;
    	try { 

    		Logger.DEBUG("ice.shootPhoto - capture to: " + localFilename);
    		fconn = (FileConnection) Connector.open(localFilename);

    		if (!fconn.exists()) {
    			Logger.DEBUG("ice.shootPhoto - Must create file");
    			fconn.create(); 

    			OutputStream os = fconn.openOutputStream();
    			os.write( rawImage );
    			os.close();

    		} else { 
    			Logger.ERROR("ice.shootPhoto file already exists: " + localFilename);  
    		}

    	} catch(Exception e) {

    		Logger.ERROR("ice.shootPhoto - persist exception: " + e);

    	} finally {
    		try { 
    			if (fconn != null) { 
    				fconn.close();
    			}
    		} catch (Exception e) { } 
    	}    	
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
