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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

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

public class CameraHandler {
    private Activity container;
    private WebView view;
    private UtilInterface util;
    private int code;
    private String url;
    private Handler handler;
    private File photoFile;
    private CameraInterface cameraInterface;
    int height, width;
    int thumbHeight, thumbWidth;
    boolean thumbnail;
    String thumbId;
    boolean useGallery = false;

    public CameraHandler(Activity container, WebView view, 
			 UtilInterface util, int take_photo_code) {
	this.container = container;
	this.view = view;
	this.code = take_photo_code;
	this.util = util;
	handler = new Handler();
	photoFile = new File(util.getTempPath(), "camera.jpeg");
    }

    public void setCameraInterface(CameraInterface cameraInterface) {
	this.cameraInterface = cameraInterface;
    }

    public void setUrl(String URL) {
	url = URL;
    }

    public void setGallery(boolean gallery) {
	useGallery = gallery;
    }

    public String shootPhoto(int width, int height, String thumbId, 
			     int thumbWidth, int thumbHeight ) {
	this.width = width;
	this.height = height;
	this.thumbId = thumbId;
	this.thumbWidth = thumbWidth;
	this.thumbHeight = thumbHeight;
        if (useGallery)  {
            return loadFromGallery();
        }
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        container.startActivityForResult(intent, code);
        return photoFile.getAbsolutePath();
    }

    public String loadFromGallery()  {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("crop", "true");
        intent.putExtra("scale", "true");
        container.startActivityForResult(intent, code);
        intent.putExtra("return-data", "false");
        return photoFile.getAbsolutePath();
    }

    public void gotPhoto() {
	try {
	    int orientation = getExifOrientation(photoFile.getAbsolutePath());
	    Bitmap captureBmp = Media.getBitmap(container.getContentResolver(), Uri.fromFile(photoFile) );
	    int captureWidth,captureHeight;
	    if (orientation == 0 || orientation == 180) {
		captureWidth = captureBmp.getWidth();
		captureHeight = captureBmp.getHeight();
	    } else {
		captureHeight = captureBmp.getWidth();
		captureWidth = captureBmp.getHeight();
	    }
	    Matrix matrix = new Matrix();
	    if (orientation != 0) {
		matrix.postRotate(orientation);
	    }

	    float scaleWidth = (width*1.0f)/captureWidth;
	    float scaleHeight = (height*1.0f)/captureHeight;
	    float scaleAmount = Math.min(scaleWidth,scaleHeight);
	    if (scaleAmount > 1.0f) {
		scaleAmount = 1.0f;
	    }
	    matrix.postScale(scaleAmount,scaleAmount);
	    if (!matrix.isIdentity()) {
		captureBmp = Bitmap.createBitmap(captureBmp, 0, 0, 
						 captureBmp.getWidth(),
						 captureBmp.getHeight(), 
						 matrix, true);
		captureBmp.compress(Bitmap.CompressFormat.JPEG, 100, 
				    new FileOutputStream(photoFile)); 
	
	    }
	    if (thumbId != null && thumbId.length() > 0 && 
		thumbWidth > 0 && thumbHeight > 0) {
		String b64Image = util.produceThumbnail(captureBmp, thumbWidth, 
							thumbHeight);
		util.loadURL("javascript:ice.setThumbnail('" + thumbId +
			     "', 'data:image/jpg;base64," + b64Image + "');");
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            Log.e("ICEcamera", "cannot read exif", ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }
}
