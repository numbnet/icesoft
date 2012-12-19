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
package org.icemobile.client.blackberry.script.audio;

import java.util.Hashtable;

import javax.microedition.media.Player;
import javax.microedition.media.control.RecordControl;

import org.icemobile.client.blackberry.ICEmobileContainer;
import org.icemobile.client.blackberry.utils.FileUtils;
import org.icemobile.client.blackberry.utils.NameValuePair;
import org.icemobile.client.blackberry.utils.UploadUtilities;

import net.rim.device.api.script.ScriptableFunction;

public class AudioRecorder extends ScriptableFunction {

    private ICEmobileContainer mContainer;
    private AudioRecorderThread mRecorderThread;

    private String mCurrentFilename;
    private String mFieldId; 

    private int mMaxDuration;
    private Thread mTerminatorThread;

    // no enum construct
    private int mDeviceState; 
    private final int idle = 0; 
    private final int starting = 1; 
    private final int recording = 2; 
    private final int stopping = 3; 

    private Object lockObject = new Object();



    public AudioRecorder (ICEmobileContainer container) {
        mContainer = container;
    }

    /** 
     * Use this if things get confused to reset the audio state
     */
    public void resetAudioState() { 

        if (mRecorderThread != null) { 
            mRecorderThread.stop();
            mRecorderThread = null;

        }
    }

    public Object invoke( Object thiz, Object[] args) {

        Hashtable params = new Hashtable();
        if (args.length == 2) { 
            mFieldId = (String) args[0];
         
         // 'undefined' object indicates arguments not being passed 
            if (! (args[1] instanceof Object)) { 
                params = UploadUtilities.getNameValuePairHash((String)args[1], "=", "&");
            } 
            
        } else { 
            ICEmobileContainer.ERROR("ice.audio - wrong number of arguments");
            return Boolean.FALSE; 
        }     
        
        NameValuePair temp; 
        
        temp = (NameValuePair) params.get("maxtime");
        if (temp != null) {
            mMaxDuration = Integer.parseInt( temp.getValue() ); 
        } else { 
            ICEmobileContainer.DEBUG("ice.audio indefinite recording");
        }

        synchronized (lockObject) { 
            try { 
                if ( mDeviceState == idle) { 
                    mDeviceState = starting; 
                    ICEmobileContainer.DEBUG( "ice.audio - IDLE -> Starting");
                    mRecorderThread =  new AudioRecorderThread();
                    mRecorderThread.start(); 

                } else if (mDeviceState == recording){

                    ICEmobileContainer.DEBUG( "ice.audio - Recording->Stopping");
                    mRecorderThread.stop();
                    mRecorderThread = null;

                } else if (mDeviceState == starting) { 
                    ICEmobileContainer.DEBUG( "ice.audio - Still Starting");
                } else if (mDeviceState == stopping) { 
                    ICEmobileContainer.DEBUG( "ice.audio - Still Stopping");
                }
            } catch (Exception e) {  
                ICEmobileContainer.ERROR("Exception recording audio: " + e);
            }
        }
        return Boolean.TRUE;		
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
                    ICEmobileContainer.DEBUG( "ice.audio - Starting -> Recording");

                    if (mMaxDuration > 0) { 
                        mTerminatorThread = new Thread("Recording Terminator") { 
                            public void run() { 

                                try {  
                                    Thread.currentThread().sleep( mMaxDuration * 1000);
                                    stop();
                                } catch (InterruptedException i) { }                                
                            }
                        };

                        mTerminatorThread.start();
                    } 
                    mDeviceState = recording;

                } catch (Exception ioe) { 
                    ICEmobileContainer.ERROR("ice.audio - Exception capturing audio: " + ioe);
                }
            }
        }

        public void stop() { 

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
                        ICEmobileContainer.DEBUG( "ice.audio - Stopped");
                        ICEmobileContainer.DEBUG( "ice.audio recorded in: " + mCurrentFilename);

                        // If this went ok, insert the hidden field. 					
                        mContainer.insertHiddenFilenameScript(mFieldId, mCurrentFilename);
                        ICEmobileContainer.DEBUG( "ice.audio - inserted audio clip");
                    }
                } catch (Exception e) { 
                    ICEmobileContainer.ERROR("Exception stopping: " + e);
                } finally { 
                    player = null;
                    recordControl = null;
                    mDeviceState = idle;
                }
            }
        }

        public void playerUpdate(Player player, String event, Object eventData) {
            // TODO Auto-generated method stub

        }
    }

}
