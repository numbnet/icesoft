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

import android.util.Log;
import android.media.MediaPlayer;
import android.media.AudioManager;
import java.lang.Exception;

public class AudioPlayer implements MediaPlayer.OnCompletionListener,
				    MediaPlayer.OnPreparedListener {

    private MediaPlayer player;
    private boolean prepared = false;
    private boolean waitingToStart = false;
    private boolean autoRelease;

    public AudioPlayer () {
	player = null;
    }

    public void onPrepared(MediaPlayer mp) {
	prepared = true;
	if (waitingToStart) {
	    player.start();
	    waitingToStart = false;
	}
    }

    public void onCompletion(MediaPlayer mp) {
	player.stop();
	if (autoRelease) {
	    release();
	}
    }

    protected void prepare(String url, boolean autoRelease) {
	try {
	    if (player == null) {
		createPlayer();
	    } else {
		player.reset();
	    }
	    player.setDataSource(url);
	    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
	    player.prepareAsync();
	    this.autoRelease = autoRelease;
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    protected void start() {
	if (prepared) {
	    player.start();
	} else {
	    waitingToStart = true;
	}
    }

    protected void pause() {
	player.pause();
    }

    protected void stop() {
	player.stop();
    }

    protected void complete() {
	stop();
	release();
    }

    protected void release() {
	if (player != null) {
	    player.release();
	    player = null;
	    prepared = false;
	}
    }

    private void createPlayer() {
	player = new MediaPlayer();
	player.setOnPreparedListener(this);
	player.setOnCompletionListener(this);
    }
}
