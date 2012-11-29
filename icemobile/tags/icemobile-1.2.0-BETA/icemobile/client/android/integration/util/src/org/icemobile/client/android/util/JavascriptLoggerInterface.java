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
