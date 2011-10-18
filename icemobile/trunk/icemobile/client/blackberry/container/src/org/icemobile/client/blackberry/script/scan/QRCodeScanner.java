package org.icemobile.client.blackberry.script.scan;

import java.util.Hashtable;

import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import net.rim.device.api.amms.control.camera.EnhancedFocusControl;
import net.rim.device.api.barcodelib.BitmapLuminanceSource;
import net.rim.device.api.script.ScriptableFunction;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

import org.icemobile.client.blackberry.ICEmobileContainer;
import org.icemobile.client.blackberry.utils.NameValuePair;
import org.icemobile.client.blackberry.utils.UploadUtilities;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

public class QRCodeScanner extends ScriptableFunction {
    
    private ICEmobileContainer mContainer;
    private VideoControl mVideoControl;
    private MainScreen mCameraScreen;
    
    // maxHeight and maxWidth are markup specified desired sizes and may be any size
//    private int maxWidth = 640;
    private int maxWidth = 1024; 
//    private int maxWidth = 2592; 
    
//    private int maxHeight = 480;
    private int maxHeight = 768; 
//    private int maxHeight = 1944;
    private String mFieldId;
    private int mThumbWidth; 
    private int mThumbHeight;
    
    // These sizes are sizes of images captured by the camera phone (only 3 sizes supported)
    private int mCapturedWidth = 1024;
    private int mCapturedHeight = 768;
    
   
    private Player mPlayer; 

    public QRCodeScanner(ICEmobileContainer container) { 
        mContainer = container; 
        mCameraScreen = new QRCameraScreen();
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
            ICEmobileContainer.DIALOG("Photo capture not supported"); 
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
            ICEmobileContainer.ERROR("ice.qrCode - wrong number of arguments");
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
            ICEmobileContainer.ERROR("ice.qrCode - size error in js: " + e);
        }
        

        String[] encodings = UploadUtilities.split(supportedEncodings, " "); 
//        for (int idx = 0; idx < encodings.length; idx ++ ) { 
//            ICEmobileContainer.DEBUG("- " + encodings[idx]);
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

                mContainer.pushScreen(mCameraScreen);
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
                            mCameraScreen.add(videoField);
                        }
                    }
                }
                catch(Exception e) {        
                    ICEmobileContainer.ERROR("ice.shootPhoto - Exception in image processing: " + e.toString());
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
                                synchronized (ICEmobileContainer.getEventLock()) { 
                                    mContainer.popScreen( mCameraScreen );
                                } 
                                ICEmobileContainer.TIME(startTime, "ice.qrCode capturing scan bytes" );   
                                
                                startTime = System.currentTimeMillis();
                                Bitmap qrcodeBitmap = Bitmap.
                                        createBitmapFromBytes(rawImage, 
                                        0, 
                                        rawImage.length,
                                        1 );
                                ICEmobileContainer.TIME(startTime, "ice.qrCode creating bitmap" );   
                                
                                startTime = System.currentTimeMillis();
                                Result[] results = processQRCodeBitmap(qrcodeBitmap);
                                if (results != null) { 
                                    if (results.length != 1) { 
                                        ICEmobileContainer.DIALOG("QRCode scan attempted to process: " + 
                                                results.length + " codes");
                                    }
                                    mContainer.insertQRCodeScript( mFieldId, results[0].getText());
                                } else { 
                                    ICEmobileContainer.DIALOG("Error scanning code, please try again");
                                }
                                
                                ICEmobileContainer.TIME(startTime, "ice.qrCode processing qrcode" );  
                                ICEmobileContainer.DEBUG("ice.qrCode - done");

                            } catch(Exception e) {
                                ICEmobileContainer.ERROR("ice.qrCode - exception: " + e);
//
                            } finally {
                                synchronized (ICEmobileContainer.getEventLock()) { 
                                    mCameraScreen.deleteAll();
                                } 
                                
                            }
                        } 
                    });
                }
            }           
            return handled;                
        }

        private Result[] processQRCodeBitmap(Bitmap qrcodeBitmap)  {
            
            Result[] qrCodes = null;
            try { 
                BitmapLuminanceSource bls = new BitmapLuminanceSource(qrcodeBitmap);
                ICEmobileContainer.DEBUG("ice.qrCode - 1 of 4 BitmapLuminance obtained: width: " + 
                        bls.getWidth() + ", height: " + bls.getHeight() +  
                        ", canCrop: " + bls.isCropSupported() + ", canRotate: " + bls.isRotateSupported());
                
                Binarizer lbb = new GlobalHistogramBinarizer( bls ); 
                ICEmobileContainer.DEBUG("ice.qrCode - 2 of 4 Binarizer obtained"); 
                BinaryBitmap bb = new BinaryBitmap( lbb ); 
                ICEmobileContainer.DEBUG("ice.qrCode - 3 of 4 BinaryBitmap obtained"); 
                MultipleBarcodeReader mbr = new QRCodeMultiReader();
             
                qrCodes = mbr.decodeMultiple(bb);
                ICEmobileContainer.DEBUG("ice.qrCode - 4 of 4 BarcodeResult read, length: " + qrCodes.length); 
                for (int idx = 0; idx < qrCodes.length; idx ++ ) { 
                    ICEmobileContainer.DEBUG("ice.qrCode - found: " + qrCodes[idx].getText());                                   
                }
                
            } catch (Exception e) { 
                ICEmobileContainer.DEBUG("ice.qrCode - exception processing image: " + e);     
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
    
    
//    /** 
//     * Currently unused. Could be used to select a file from the filesystem. 
//     */
//    private void invokePhotoGallery( ) { 
//        
//        
//        String cameraPath = "file:///SDCard/BlackBerry/"; 
//        if (cameraPath.startsWith("file:///")) { 
//            cameraPath = cameraPath.substring("file:///".length() );
//        }
//        FileSelectorPopupScreen fsp = new FileSelectorPopupScreen(cameraPath, 
//                new String[] { ".jpg" } );
//        fsp.pickFile();
//        String chosenImage = fsp.getFile(); 
//        if (chosenImage == null) { 
//            return; 
//        }
//        
//        // So now I have an opened filename. Need to read, insert as thumbnail, and 
//        processExistingFile(chosenImage);
//        
//        
//    }
    
    
//    /**
//     * Handle the case where the user has chosen a file from the file system. 
//     * @param filename name of the chosen file
//     */
//    private void processExistingFile( String filename ) { 
//        
//        FileConnection fc = null; 
//        try { 
//            fc = (FileConnection) Connector.open( filename );
//            
//            InputStream is = fc.openInputStream(); 
//            byte[] data = IOUtilities.streamToBytes(is);               
//            
//        } catch (Exception e) { 
//            if (fc != null)  { 
//                try { 
//                    fc.close(); 
//                } catch (Exception io) {}
//            }
//        }        
//        mContainer.insertHiddenFilenameScript(mFieldId, filename );         
//    }
//    
    
 
    
//    /**
//     * If necessary, persist the raw data to a file
//     * 
//     * @param rawImage
//     * @param localFilename
//     */
//    private void persistImage (byte rawImage[], String localFilename) { 
//        
//        FileConnection fconn = null;
//        try { 
//
//            ICEmobileContainer.DEBUG("ice.shootPhoto - capture to: " + localFilename);
//            fconn = (FileConnection) Connector.open(localFilename);
//
//            if (!fconn.exists()) {
//                ICEmobileContainer.DEBUG("ice.shootPhoto - Must create file");
//                fconn.create(); 
//
//                OutputStream os = fconn.openOutputStream();
//                os.write( rawImage );
//                os.close();
//
//            } else { 
//                ICEmobileContainer.ERROR("ice.shootPhoto file already exists: " + localFilename);  
//            }
//
//        } catch(Exception e) {
//
//            ICEmobileContainer.ERROR("ice.shootPhoto - persist exception: " + e);
//
//        } finally {
//            try { 
//                if (fconn != null) { 
//                    fconn.close();
//                }
//            } catch (Exception e) { } 
//        }       
//    }
    
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