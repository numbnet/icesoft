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
package org.icemobile.client.blackberry.script.camera;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.RecordControl;
import javax.microedition.media.control.VideoControl;

import org.icemobile.client.blackberry.ContainerController;
import org.icemobile.client.blackberry.Logger;
import org.icemobile.client.blackberry.utils.FileUtils;
import org.icemobile.client.blackberry.utils.NameValuePair;
import org.icemobile.client.blackberry.utils.UploadUtilities;

import net.rim.device.api.script.ScriptableFunction;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;


/**
 * This class is the scriptExtension for video recording 
 *
 */
public class VideoController extends ScriptableFunction {

    private ContainerController mController;

    private VideoRecordingScreen videoScreen;
    private String mEncodedTick;
    

    public VideoController(ContainerController controller ) { 
        mController = controller; 
        mEncodedTick = FileUtils.getImageResourceBase64("tick.png");       
        
    }	

    /**
     * Capture some video. The return value from this call is TRUE if the 
     * invocation went ok. The actual results will be defined later via an asynchronous 
     * callback. 
     * 
     * @return true if video control launched ok, false if not. 
     */
    public  Object invoke( Object thiz, Object[] args) { 

        
        String fieldId = null;
        Hashtable params = new Hashtable();
        if (args.length == 2) { 
            fieldId = (String) args[0];
         
            if (args[1] instanceof Object) { 
                // 'undefined' object indicates arguments not being passed 
            } else { 
                params = UploadUtilities.getNameValuePairHash((String)args[1], "=", "&");
            } 
            
        } else { 
        	Logger.ERROR("ice.video - wrong number of arguments");
            return Boolean.FALSE; 
        }     
        
        NameValuePair temp; 
        
        int maxTime = 0;
        temp = (NameValuePair) params.get("maxtime");
        if (temp != null) {
            maxTime = Integer.parseInt( temp.getValue() ); 
        }
        
        videoScreen = new VideoRecordingScreen(mController, fieldId, maxTime, mEncodedTick );
        synchronized (UiApplication.getEventLock()) { 
        	UiApplication.getUiApplication().pushScreen(videoScreen);			
        }
        videoScreen.startRecording();
        return Boolean.TRUE;
    }	
}

class VideoRecordingScreen extends MainScreen { 

    private VideoRecordingThread mRecordingThread;
    private ContainerController mController; 
    private String mFieldId;
    private Thread mTerminatorThread;
    private int mMaxTime;
    private String mIcon;

    public VideoRecordingScreen(ContainerController container, String fieldId, int maxDuration, String icon ) { 

        mController = container;		
        mFieldId = fieldId;
        mMaxTime = maxDuration;
        mIcon = icon;

    }
    protected boolean invokeAction(int action) { 

        boolean handled = super.invokeAction(action);
        if(!handled) { 

            if(action == ACTION_INVOKE) {
                stopRecording(mFieldId);                
                return true;
            }
        }
        return handled;
    }

    

    // Can be invoked from outside
    public void startRecording() {
        
        try { 
            mRecordingThread = new VideoRecordingThread();
            mRecordingThread.start();

            if (mMaxTime > 0) { 
                mTerminatorThread = new Thread("Recording Terminator") { 
                    public void run() { 
                        try {  
                     
                            Thread.sleep( mMaxTime * 1000);
                            stopRecording( mFieldId );

                        } catch (InterruptedException i) { }  
                    }
                };
                mTerminatorThread.start();

            } 
        } catch (Exception e) { 
        	Logger.ERROR("ice.video - Exception starting recording: " + e);
        }
    }

    /**
     * Synchronized call for stopping the recording. Maybe called from 
     * terminator thread or from UI so needs to be threadsafe. 
     * @param fieldId 
     */
    public synchronized void stopRecording(String fieldId) {
        
        if (mRecordingThread != null) { 
            mRecordingThread.stop(fieldId);
        
        
            final MainScreen removable = this;
            UiApplication.getUiApplication().invokeLater( new Runnable() {
                public void run() {
                	synchronized( UiApplication.getEventLock()) { 
                		UiApplication.getUiApplication().popScreen(removable);
                	}
                }
            });
          
            mRecordingThread = null;
        }        
        
    }

    private class VideoRecordingThread extends Thread implements PlayerListener { 

        private Player mPlayer; 
        private RecordControl mRecordControl;
        private OutputStream mRecordStream;
        private String mVidCaptureFile;


        public void run() { 

            try { 

                Logger.DEBUG("ice.video - Recording thread is starting" );
                mPlayer = javax.microedition.media.Manager
                .createPlayer("capture://video?encoding=video/3gpp");

                mPlayer.addPlayerListener(this);
                mPlayer.realize();

                VideoControl videoControl = (VideoControl)
                mPlayer.getControl("VideoControl");

                mRecordControl = (RecordControl) mPlayer.getControl( "RecordControl" );   
                Field videoField = (Field)
                videoControl.initDisplayMode(VideoControl.USE_GUI_PRIMITIVE,
                "net.rim.device.api.ui.Field");


                try {
                    videoControl.setDisplaySize( Display.getWidth(), Display.getHeight() );
                }
                catch( MediaException me ) { 
                    //Logger.DEBUG("Info. Setting displaySize not Supported");
                }

                synchronized (UiApplication.getEventLock() ) {

                    add(videoField);

                    mVidCaptureFile = FileUtils.getVideoFileURL("video.3gp");

                    FileConnection fc = (FileConnection) Connector.open(mVidCaptureFile);
                    if (fc.exists() ) { 
                        Logger.DEBUG("ice.video - file: " + mVidCaptureFile + " exists"); 
                    } else { 
                        Logger.DEBUG("ice.video - Creating video filename: " + mVidCaptureFile);
                        fc.create();
                    }
                    mRecordStream = fc.openOutputStream(); 

                    Logger.DEBUG("ice.video - Can write vidCapture file? " + fc.canWrite());
                    fc.close();

                    // The direct location approach hangs on closing the file
                    // mRecordControl.setRecordLocation(filename );
                    mRecordControl.setRecordStream(mRecordStream);

                    // These two operate in concert.
                    mRecordControl.startRecord(); 
                    mPlayer.start();
                } 

            } catch( IOException e ) {
            	Logger.ERROR("ice.video - IOException : " + e);

            } catch( MediaException mme ) { 

            	Logger.ERROR("ice.video - MediaException: " + mme);
            } catch (Throwable t) { 
                Logger.DEBUG("ice.video - ERROR in video capture: " + t);
            }
        } 		

        public void playerUpdate(Player player, String event, Object eventData) {  
            Logger.DEBUG("ice.video - Player event " + event +
                    ": " + eventData);
        }

        /**
         * Stop the videoRecordingThread
         * @param fieldId Field to insert thumbnail as
         */
        public void stop(String fieldId) { 

            try {       
                if (mPlayer != null) {
                    mPlayer.close(); 
                    mPlayer = null; 
                }
                if (mRecordControl != null) {              
                    //  _recordControl.stopRecord();  // Implicitly done by commit. 
                    mRecordControl.commit();
                    mRecordStream.close();
                    mRecordControl = null;

                    mController.insertThumbnail(mFieldId, VideoRecordingScreen.this.mIcon );					    	
                    mController.insertHiddenFilenameScript(fieldId, mVidCaptureFile  );
                    Logger.DEBUG("ice.video - done");
                }
            } catch (Throwable e) {                
            	Logger.ERROR("ice.video - Exception stopping recordingThread: " + e);
            }	
        }
        
    }
}
