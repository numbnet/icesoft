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

package org.icemobile.client.android.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.icemobile.client.android.JavascriptInterface;
import org.icemobile.client.android.ICEmobileContainer;

public class CaptureJSInterface implements JavascriptInterface {
  private static final String TAG = CaptureJSInterface.class.getSimpleName();

    private Activity activity;
    private String result;

    public CaptureJSInterface (Activity activity) {
        this.activity = activity;
    }

    public String scan(String id, String attr) {
//        AttributeExtractor attributes = new AttributeExtractor(attr);
        Log.d(TAG, "starting QRCode CaptureActivity");
        Intent myIntent = new Intent(activity.getApplicationContext(), CaptureActivity.class);
        myIntent.putExtra(ICEmobileContainer.SCAN_ID, id);
        activity.startActivityForResult(myIntent, 
                ICEmobileContainer.SCAN_CODE);
        Log.d(TAG, "returning value from ICEmobile.scan");
        return "howdoigetthescanout";
    }
}
