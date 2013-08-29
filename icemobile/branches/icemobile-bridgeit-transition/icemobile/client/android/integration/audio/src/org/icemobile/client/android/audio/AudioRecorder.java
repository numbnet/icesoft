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

package org.icemobile.client.android.audio;

import android.app.Activity;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.database.Cursor;
import android.net.Uri;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.content.pm.PackageManager;
import java.util.List;
import java.io.IOException;
import java.io.File;
import java.lang.reflect.Field;
import android.os.Build;

import org.icemobile.client.android.util.UtilInterface;

public class AudioRecorder {

    private Activity container;
    private boolean recording;
    private MediaRecorder mRecorder;
    private File audioFile;
    private UtilInterface utilInterface;
    private boolean cantStop;
    private boolean stopPending;
    private int recordCode;

    public AudioRecorder (Activity container, UtilInterface util, int recordCode) {
	this.container = container;
	utilInterface = util;
	this.recordCode = recordCode;
	recording = false;
	audioFile = new File(utilInterface.getTempPath(), "micCapture.3gpp");
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
        try {
            mRecorder = null;
            try {
		// Can't use AAC on Samsung Tablets 3.1;
		String model = Build.MODEL;
		String version = Build.VERSION.RELEASE;
		if (model.contains("GT-P7")  && 
		    version.equals("3.1")) {
                    mRecorder = getMediaRecorder(MediaRecorder.AudioEncoder.DEFAULT);
		} else {
		    Field aacField = MediaRecorder.AudioEncoder.class
                        .getField("AAC");
		    if (null != aacField)  {
			//set to AAC from android-10 for better iPhone compatibility
			mRecorder = getMediaRecorder(aacField.getInt(null));
			if (maxDuration > 0) {
			    mRecorder.setMaxDuration(maxDuration*1000);
			}
		    }
		}
            } catch (Throwable t)  {
                Log.d("ICEaudio", "AAC setup failed");
            }
            mRecorder.prepare();
            Log.d("ICEaudio", "AAC audio encoding ");
        } catch (Exception e) {
            Log.e("ICEaudio", "prepare() failed for AAC");
            try {
                mRecorder = getMediaRecorder(
                        MediaRecorder.AudioEncoder.DEFAULT );
                if (maxDuration > 0) {
                    mRecorder.setMaxDuration(maxDuration*1000);
                }
                mRecorder.prepare();
                Log.d("ICEaudio", "DEFAULT audio encoding ");
            } catch (IOException x) {
                Log.e("ICEaudio", "prepare() failed for DEFAULT");
            }
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

    MediaRecorder getMediaRecorder(int encoder)  {
        MediaRecorder recorder = new MediaRecorder();
        recorder.reset();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(encoder);
        recorder.setOutputFile(audioFile.getAbsolutePath());
        return recorder;
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


    public String recordAudio(int maxDuration) {
	Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
    PackageManager packageManager = container.getPackageManager();
    List list = packageManager.queryIntentActivities(intent,  
            packageManager.MATCH_DEFAULT_ONLY);

    boolean olddroid =
        android.os.Build.VERSION.RELEASE.startsWith("1.") ||
        android.os.Build.VERSION.RELEASE.startsWith("2.");

    if ( olddroid || (0 == list.size()) )  {
        Log.d("ICEaudio", "falling back to SimpleAudioRecorder");
        intent = new Intent(container.getApplicationContext(), 
                SimpleAudioRecorder.class);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, audioFile.getAbsolutePath());
    }

    // Would like to set the location, but it does not work;
    //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(audioFile));
    if (maxDuration > 0) {
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, maxDuration);
    }
    container.startActivityForResult(intent, recordCode);
    return audioFile.getAbsolutePath();
    }

    public void gotAudio(Intent data) {
    Bundle extras = data.getExtras();
    if (null != extras) {
        String recordedAudioFilePath = 
                extras.getString(MediaStore.EXTRA_OUTPUT);
        if (null != recordedAudioFilePath)  {
            File file = new File(recordedAudioFilePath);
            file.renameTo(audioFile);
            return;
        }
    }

	Uri uri = data.getData();
	String[] projection = { MediaStore.Audio.Media.DATA };
	Cursor cursor = container.managedQuery(uri, projection, null, null, null); 
	int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA); 
	cursor.moveToFirst(); 
	String recordedAudioFilePath = cursor.getString(column_index_data);

	// Move the file
	File file = new File(recordedAudioFilePath);
	file.renameTo(audioFile);
	int rows = container.getContentResolver().delete(uri,null,null);
	
    }
}


