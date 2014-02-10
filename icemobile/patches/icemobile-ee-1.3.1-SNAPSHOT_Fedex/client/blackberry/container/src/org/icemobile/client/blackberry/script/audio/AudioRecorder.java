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
package org.icemobile.client.blackberry.script.audio;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.Player;
import javax.microedition.media.control.RecordControl;

import org.icemobile.client.blackberry.ContainerController;
import org.icemobile.client.blackberry.Logger;
import org.icemobile.client.blackberry.utils.FileUtils;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;

/**
 * Implement the AudioRecorder now as a Screen Implementation
 * With GUI controls to drive the previously implicit state 
 * behaviour
 * 
 */
public class AudioRecorder extends MainScreen {

    private ContainerController mController;
    private AudioRecorderThread mRecorderThread;

    private String mCurrentFilename;
    private String mFieldId; 

    private int mMaxDuration;
    private Thread mTerminatorThread;
 
    // no enum construct. The recordingComplete state is essentially 
    // idle, but with having a clip recorded which must be tidied up if 
    // the user moves to record another without saving. 
    private int mDeviceState; 
    private final int idle = 0; 
    private final int starting = 1; 
    private final int recording = 2; 
    private final int stopping = 3; 
    private final int recordingComplete = 4; 

    private Object lockObject = new Object();


    private ButtonField mStartStopButton; 
    private ButtonField mOkButton;
    private ButtonField mCancelButton;
    
    private TextField mStatusField; 

    public AudioRecorder (ContainerController controller) {
        mController = controller;
        initGui();
    }

    /**
     * Initialize the GUI for 
     */
    private void initGui() { 
    	
    	mStartStopButton = new ButtonField("Start Recording...", Field.FIELD_HCENTER | Field.FIELD_TOP );
    	
    	mStartStopButton.setChangeListener( new FieldChangeListener() { 
			public void fieldChanged(Field field, int context) {
				
				switch (mDeviceState) { 
				case idle: 
					startRecording(); 
					break; 
				case recording: 
					haltRecording();	
					break; 
				case starting: 
					Logger.DIALOG("Audio recording starting");
					break; 
				case stopping: 
					Logger.DIALOG("Audio recording stopping...");
					break;
				case recordingComplete: 
					
				}				
			}
    	} ); 
    	
    	add(mStartStopButton); 
    	mStatusField = new TextField(Field.FIELD_HCENTER|Field.FIELD_VCENTER); 
    	add(mStatusField); 
    	
    	HorizontalFieldManager horizontalManager = new HorizontalFieldManager(Field.FIELD_BOTTOM ); 
    	horizontalManager.setMargin(100, 10, 0, 10);
    	add( horizontalManager );     	
    	
    	mOkButton = new ButtonField("Ok",  Field.FIELD_LEFT);     
    	mOkButton.setMargin(4, 50, 4, 20);
    	mOkButton.setChangeListener( new FieldChangeListener() { 
    		
			public void fieldChanged(Field field, int context) {
				
				switch (mDeviceState) { 
				case idle: 
					break; 
				case recording: 
					break; 
				case starting: 
					Logger.DIALOG("Audio recording starting");
					break; 
				case stopping: 
					Logger.DIALOG("Audio recording stopping...");
					break;
				case recordingComplete: 
					acceptRecording();	
					dismissScreen();
					break;
				}				
			}
    	} ); 
    	horizontalManager.add( mOkButton );
    	
    	mCancelButton = new ButtonField("Cancel", Field.FIELD_RIGHT );
    	mOkButton.setMargin(4, 20, 4, 50);
    	mCancelButton.setChangeListener( new FieldChangeListener() { 
    		
			public void fieldChanged(Field field, int context) {
				
				switch (mDeviceState) { 
				case idle: 
					dismissScreen();
					break;
				case recording: 
					haltRecording(); 
					deleteRecording( mCurrentFilename ); 
			    	dismissScreen();
					break; 
				case starting: 
					Logger.DIALOG("Audio recording starting");
					break; 
				case stopping: 
					Logger.DIALOG("Audio recording stopping...");
					break;
				case recordingComplete: 
					deleteRecording( mCurrentFilename ); 
			    	dismissScreen();
				}
			}
    	} ); 
    	
    	horizontalManager.add( mCancelButton ); 
    }
    
    private void startRecording() { 
    	
    	synchronized (lockObject) { 
    		mDeviceState = starting; 
    		Logger.DEBUG( "ice.audio - Idle -> Starting");
    		mRecorderThread =  new AudioRecorderThread();
    		mRecorderThread.start(); 
    		mStartStopButton.setLabel("Stop Recording");
    	} 
    }
    
    private void haltRecording() { 
    	
    	synchronized( lockObject ) { 
    		Logger.DEBUG( "ice.audio - Recording->Stopping");
    		mRecorderThread.stopRecording();
    		mRecorderThread = null;
    		mStartStopButton.setLabel("Start Recording...");
    	}         
    }
    
    private void acceptRecording() { 
    	
        // If this went ok, insert the hidden field. 		
    	try { 
        mController.insertHiddenFilenameScript(mFieldId, mCurrentFilename);
        Logger.DEBUG( "ice.audio - inserted audio clip in: " + mFieldId );
    	} catch (Exception e) { 
    		Logger.DEBUG("Exception accepting recording: " + e);
    	}
    }
    
    
    private void deleteRecording(String filename) { 
    	
    	FileConnection fc = null; 
    	try { 
    		fc = (FileConnection) Connector.open( filename );
    		if (fc.exists()) { 
    			fc.delete();
    		}
    		
    	} catch (IOException ioe) { 
    		Logger.ERROR("ice.audio - Error deleting temp file: " + ioe); 
    	}
    }
    
    private void dismissScreen() { 
    	try { 
    	synchronized(UiApplication.getEventLock()) { 
    		UiApplication.getUiApplication().popScreen(this);
    	}
    	} catch (Exception e) { 
    		Logger.DEBUG("Exception dismissing screen: " + e);
    	}
    }
    
    private void enableCompletion() { 
    	FileConnection fc = null;
    	long size = 0; 
    	try { 
    		fc = (FileConnection) Connector.open( mCurrentFilename );
    		if (fc.exists()) { 
    			size = fc.fileSize();
    		}

    	} catch (IOException ioe) { 
    		Logger.ERROR("ice.audio - Error deleting temp file: " + ioe); 
    	}
    	AudioRecorder.this.mStatusField.setText("Audio file saved in: " + mCurrentFilename + " containing: " + size + " bytes" );
    	mOkButton.setEnabled(true);
    }
    
    /**
     * Called by the invoke method in the javascript
     * @param fieldId
     * @param maxDuration
     */
    public void setAudioParameters( String fieldId, int maxDuration) {
    	
    	mFieldId = fieldId; 
    	mMaxDuration = maxDuration;   
    	mDeviceState = idle;
    	mOkButton.setEnabled (false); 
    	mStartStopButton.setLabel("Start Recording");
    	mStatusField.setText("");
    }


    class AudioRecorderThread extends Thread implements javax.microedition.media.PlayerListener { 

        private Player player; 
        private RecordControl recordControl;		


        public void run() {

            synchronized (lockObject) { 

                try { 
                    player = javax.microedition.media.Manager.createPlayer(
                    "capture://audio?encoding=audio/amr" ); 

                    player.addPlayerListener(this); 
                    player.realize();
                    recordControl = (RecordControl) player.getControl("RecordControl");
                    mCurrentFilename = FileUtils.getAudioFileURL("AudioRecording.amr");				
                    recordControl.setRecordLocation(mCurrentFilename);

                    recordControl.startRecord(); 
                    player.start();	
                    Logger.DEBUG( "ice.audio - Starting -> Recording");

                    if (mMaxDuration > 0) { 
                        mTerminatorThread = new Thread("Recording Terminator") { 
                            public void run() { 

                                try {  
                                    Thread.sleep( mMaxDuration * 1000);
                                    stopRecording();
                                } catch (InterruptedException i) { }                                
                            }
                        };

                        mTerminatorThread.start();
                    } 
                    mDeviceState = recording;

                } catch (Exception ioe) { 
                	
                	// Got to watch for IOExceptions here if the device is full.  
                	Logger.ERROR("ice.audio - Exception capturing audio: " + ioe);
                	Logger.DIALOG("ice.audio - Exception recording audio: " + ioe); 
                	stopRecording();
                }
            }
        }

        public void stopRecording() { 

            synchronized (lockObject) { 
                
                mDeviceState = stopping;
                if (mTerminatorThread != null) { 
                    mTerminatorThread.interrupt(); 
                }
                try { 
                    if (player != null) { 
                        player.close(); 
                        player = null;
                    }

                    if (recordControl != null) { 
                        recordControl.stopRecord();
                        recordControl.commit();
                        Logger.DEBUG( "ice.audio - Stopped");
                        Logger.DEBUG( "ice.audio recorded in: " + mCurrentFilename);
                        
                     enableCompletion();
                     mDeviceState = recordingComplete;
                     // save the file later, on demand
                    }
                } catch (Exception e) { 
                	Logger.ERROR("ice.audio - Exception stopping recording: " + e);
                	mDeviceState = idle;
                } finally { 
                    player = null;
                    recordControl = null;                    
                }
            }
        }

        public void playerUpdate(Player player, String event, Object eventData) {
            // TODO Auto-generated method stub

        }
    }

}
