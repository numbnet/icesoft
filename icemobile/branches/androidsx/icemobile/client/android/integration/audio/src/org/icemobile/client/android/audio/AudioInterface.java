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

import android.util.Log;
import org.icemobile.client.android.util.JavascriptInterface;
import org.icemobile.client.android.util.AttributeExtractor;

public class AudioInterface implements JavascriptInterface {

    private AudioRecorder recorder;
    private AudioPlayer player;

    public AudioInterface (AudioRecorder recorder, AudioPlayer player) {
	this.recorder = recorder;
    }

    public String toggleMic(String attr) {
	AttributeExtractor attributes = new AttributeExtractor(attr);
	int maxDuration = Integer.parseInt(attributes.getAttribute("maxtime", "-1"));
	return recorder.toggleMic(maxDuration);
    }

    public String recordAudio(String attr) {
    String result = "";
    try {
        AttributeExtractor attributes = new AttributeExtractor(attr);
        int maxDuration = Integer.parseInt(attributes.getAttribute("maxtime", "-1"));
        result = recorder.recordAudio(maxDuration);
    } catch (Exception e)  {
        Log.e("ICEmobile", "recordAudio failed ", e);
    }
	return result;
    }

    public void playUrl(String url, boolean autoRelease) {
	prepareAudio(url, autoRelease);
	startAudio();
    }

    public void prepareAudio(String url, boolean autoRelease) {
	player.prepare(url, autoRelease);
    }

    public void startAudio() {
	player.start();
    }

    public void pauseAudio() {
	player.pause();
    }

    public void stopAudio() {
	player.start();
    }

    public void completeAudio() {
	player.complete();
    }
}
