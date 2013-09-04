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

package org.icemobile.client.android.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.icemobile.client.android.util.JavascriptInterface;

public class CaptureJSInterface implements JavascriptInterface {
  private static final String TAG = CaptureJSInterface.class.getSimpleName();

    private Activity activity;
    private String result;
    private int scanCode;
    private String scanId;

    public CaptureJSInterface (Activity activity, int scanCode, String scanId) {
        this.activity = activity;
	this.scanCode = scanCode;
	this.scanId = scanId;
    }

    public String scan(String id, String attr) {
//        AttributeExtractor attributes = new AttributeExtractor(attr);
        Log.d(TAG, "starting QRCode CaptureActivity");
        Intent myIntent = new Intent(activity.getApplicationContext(), CaptureActivity.class);
        myIntent.putExtra(scanId, id);
        activity.startActivityForResult(myIntent, scanCode);
        Log.d(TAG, "returning value from ICEmobile.scan");
        return "howdoigetthescanout";
    }
}
