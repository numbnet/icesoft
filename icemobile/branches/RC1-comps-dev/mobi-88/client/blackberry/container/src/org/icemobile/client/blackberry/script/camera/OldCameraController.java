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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import org.icemobile.client.blackberry.ICEmobileContainer;

import net.rim.blackberry.api.invoke.CameraArguments;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.device.api.io.Base64OutputStream;
import net.rim.device.api.io.file.FileSystemJournal;
import net.rim.device.api.io.file.FileSystemJournalEntry;
import net.rim.device.api.io.file.FileSystemJournalListener;
import net.rim.device.api.script.ScriptableFunction;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.ControlledAccessException;
import net.rim.device.api.system.EventInjector;
import net.rim.device.api.system.EventLogger;
import net.rim.device.api.ui.UiApplication;

/**
 * This camera controller interacts with the camera application 
 * as a standalone program, and requires a FileSystemJournalListener to determine when a photo 
 * has been taken. 
 * 
 */
public class OldCameraController extends ScriptableFunction {

    private long lastUSN = 0;
    private String photoPath;
    private String returnVal;
    private FileSystemJournalListener listener;
    private ICEmobileContainer _container;

    private int mImageWidth = 640;
    private int mImageHeight = 480; 

    private int mThumbWidth = 64; 
    private int mThumbHeight = 64;

    // Id of containing form in the event more than one image is on page
    private String mFieldId;
    private String mCurrentFilename;

    public OldCameraController(ICEmobileContainer container) { 
        _container = container; 
    }


    public  Object invoke( Object thiz, Object[] args) { 

        if (args.length != 5) { 
            ICEmobileContainer.ERROR("ice.AudioRecorder wrong number of arguments");
            return Boolean.FALSE; 
        }
        mFieldId = (String) args[2];
        try { 
            mImageWidth = ((Integer) args[0]).intValue();
            mImageHeight = ((Integer) args[1]).intValue(); 

            mThumbWidth = ((Integer) args[3] ).intValue(); 
            mThumbHeight = ((Integer) args[4] ).intValue();

        } catch (Exception e) { 
            EventLogger.logEvent(ICEmobileContainer.GUID, "ice.OldAudioRecorder size format error is js".getBytes(), EventLogger.ERROR);
        }


        listener = new FileSystemJournalListener() {
            public void fileJournalChanged() {
                long USN = FileSystemJournal.getNextUSN();
                for (long i = USN - 1; i >= lastUSN; --i) {
                    FileSystemJournalEntry entry = FileSystemJournal.getEntry(i);
                    if (entry != null) {
                        if (entry.getEvent() == FileSystemJournalEntry.FILE_CHANGED) {
                            if (entry.getPath().indexOf(".jpg") != -1) {
                                lastUSN = USN;
                                photoPath = entry.getPath();

                                closeCamera();
                                _container.insertHiddenFilenameScript(mFieldId, photoPath );
                                //								_container.setTakenPhotoName(photoPath);
                            }
                        }
                    }
                }
                lastUSN = USN;
            }
        };
        try { 
            UiApplication.getUiApplication().addFileSystemJournalListener(listener);
            Invoke.invokeApplication(Invoke.APP_TYPE_CAMERA, new CameraArguments());
        } catch (Exception e) { 
            EventLogger.logEvent(ICEmobileContainer.GUID, ("ice.takePhoto exception: " + e).getBytes(), EventLogger.ERROR);
        }

        return Boolean.TRUE;
    }	

    public void closeCamera() {
        try {
            UiApplication.getUiApplication().removeFileSystemJournalListener(listener);
            EventInjector.KeyEvent inject = new EventInjector.KeyEvent(EventInjector.KeyEvent.KEY_DOWN, Characters.ESCAPE, 0);
            inject.post();
            inject.post();
        } catch (ControlledAccessException ex) {
            EventLogger.logEvent(ICEmobileContainer.GUID, ("ice.takePhoto: Access exception stopping camera" + ex).getBytes(), EventLogger.ERROR);
        }
    }
}
