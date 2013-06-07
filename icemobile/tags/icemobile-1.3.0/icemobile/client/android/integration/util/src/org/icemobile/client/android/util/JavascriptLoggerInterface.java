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

package org.icemobile.client.android.util;

import android.util.Log;

public class JavascriptLoggerInterface implements JavascriptInterface {

    private final String JAVASCRIPT_TAG = "Javascript layer";

    public void logInContainer(String message, int category) {
        switch (category) {
            case Log.ERROR:
                Log.e(JAVASCRIPT_TAG, message);
                break;
            case Log.DEBUG:
                Log.d(JAVASCRIPT_TAG, message);
                break;
            case Log.WARN:
                Log.w(JAVASCRIPT_TAG, message);
                break;
            default:
                Log.d(JAVASCRIPT_TAG, message);
        }
    }

    public void logInContainer(String message) {
        logInContainer(message, Log.DEBUG);
    }
}
