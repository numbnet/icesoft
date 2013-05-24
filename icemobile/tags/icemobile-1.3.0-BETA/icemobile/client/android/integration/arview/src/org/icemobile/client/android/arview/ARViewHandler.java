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

package org.icemobile.client.android.arview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import android.app.Activity;
import android.webkit.WebView;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.os.Bundle;

import android.media.ExifInterface;
import android.graphics.Matrix;

import org.icemobile.client.android.util.UtilInterface;

public class ARViewHandler {
    private Activity container;
    private WebView view;
    private UtilInterface util;
    private int code;
    private String url;
    private Handler handler;
    private ARViewInterface arViewInterface;
    int height, width;
    int thumbHeight, thumbWidth;
    boolean thumbnail;
    String thumbId;
    boolean useGallery = false;

    public ARViewHandler(Activity container, WebView view, 
			 UtilInterface util, int arview_code) {
        this.container = container;
        this.view = view;
        this.code = arview_code;
        this.util = util;
        handler = new Handler();
    }

    public void setARViewInterface(ARViewInterface arViewInterface) {
        this.arViewInterface = arViewInterface;
    }

    public void setUrl(String URL) {
        url = URL;
    }

    public String arView(String id, String attr) {
        Log.d("ARView", "starting ARViewActivity");
        Intent arIntent = new Intent(container.getApplicationContext(), ARViewActivity.class);
        arIntent.putExtra(id, code);
        arIntent.putExtra("attributes", attr);
        container.startActivityForResult(arIntent, code);
        return "selectedlabel";
    }
}
