/*
* Copyright 2004-2011 ICEsoft Technologies Canada Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions an
* limitations under the License.
*/ 

package org.icemobile.client.android;

import android.content.Context;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.os.Environment;
import java.io.IOException;
import java.io.File;

public class AudioRecorder {

    private Context context;
    private boolean recording;
    private MediaRecorder mRecorder;
    private File audioFile;
    private UtilInterface utilInterface;
    private boolean cantStop;
    private boolean stopPending;

    public AudioRecorder (Context context, UtilInterface util) {
	this.context = context;
	utilInterface = util;
	recording = false;
	audioFile = new File(utilInterface.getTempPath(), "micCapture.mpga");
    }

    public String toggleMic(int maxDuration) {
	if (recording) {
	    stopRecording();
	} else {
	    startRecording(maxDuration);
	}
        return audioFile.getAbsolutePath();
    }

    private void startRecording(int maxDuration) {

	mRecorder = new MediaRecorder();
	mRecorder.reset();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
	if (maxDuration > 0) {
	    mRecorder.setMaxDuration(maxDuration*1000);
	}
        //set to AAC from android-10 for better iPhone compatibility
//        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setOutputFile(audioFile.getAbsolutePath());
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("ICEaudio", "prepare() failed");
        }

        //record in a separate thread so that the UI is not blocked
	Thread t = new Thread(new Runnable()  {
            public void run()  {
		stopPending = false;
		cantStop = true;
                mRecorder.start();
		try {
		    Thread.currentThread().sleep(10);
		} catch (Exception e) {
		    Log.e("ICEaudio", e.toString());
		}
		cantStop = false;
		if (stopPending) {
		    stopRecording();
		}
            }
        });
        t.start();
	recording = true;
    }

    private void stopRecording() {
	if (cantStop) {
	    stopPending = true;
	} else {
	    mRecorder.stop();
	    mRecorder.release();
	    mRecorder = null;
	    recording = false;
	    stopPending = false;
	}
    }
}
