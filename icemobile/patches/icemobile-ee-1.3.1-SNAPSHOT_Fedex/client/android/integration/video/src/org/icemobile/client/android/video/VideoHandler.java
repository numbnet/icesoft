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

package org.icemobile.client.android.video;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.Activity;
import android.webkit.WebView;
import android.content.Context;
import android.content.Intent;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.os.Bundle;
import android.media.ThumbnailUtils;
import android.net.Uri;

import java.io.ByteArrayOutputStream;

import org.icemobile.client.android.util.UtilInterface;

public class VideoHandler {
    private Activity container;
    private WebView view;
    private UtilInterface util;
    private int code;
    private Handler handler;
    private File videoFile;
    int quality;
    int thumbHeight, thumbWidth;
    int maxSize, maxDuration;
    boolean thumbnail;
    String thumbId;
    boolean useGallery = false;

    public VideoHandler(Activity container, WebView view, 
			UtilInterface util, int take_video_code) {
	this.container = container;
	this.view = view;
	this.code = take_video_code;
	this.util = util;
	handler = new Handler();
	videoFile = new File(util.getTempPath(), "video.mp4");
    }

    public void setGallery(boolean gallery) {
	useGallery = gallery;
    }

    public String shootVideo(int quality, String thumbId, int thumbWidth, int thumbHeight,
			     int maxDuration) {
	this.quality = quality;
	this.thumbId = thumbId;
	this.thumbWidth = thumbWidth;
	this.thumbHeight = thumbHeight;
	this.maxSize = maxSize;
	this.maxDuration = maxDuration;
	
        if (useGallery)  {
            return loadFromGallery();
        }
        final Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	//Trying to set MediaStore.EXTRA_CONTENT does not work, so use default location;
        //intent.putExtra(MediaStore.EXTRA_OUTPUT...);

	intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, quality);
	if (maxDuration > 0) {
	    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, maxDuration);
	}
        container.startActivityForResult(intent, code);
        return videoFile.getAbsolutePath();
    }

    public String loadFromGallery()  {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("video/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
        container.startActivityForResult(intent, code);
        intent.putExtra("return-data", "false");
        return videoFile.getAbsolutePath();
    }

    public String gotVideo(Intent data) {

	// mobi-770. As of the BB10 10.1 update, the Camera Activity no longer
	// returns an URI for fetching the video, but the raw file name directly.
	// This could all be avoided if the devices supported setting the EXTRA_OUTPUT parameter,
	// but the early 2.3.3 phones do not.
	Uri uri = data.getData();
	String recordedVideoFilePath;
	String[] projection = { MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE  }; 
	Cursor cursor = container.managedQuery(uri, projection, null, null, null);
	if (cursor == null) {
	    recordedVideoFilePath = uri.toString();
	    if (videoFile.exists()) {
		videoFile.delete();
	    }
	} else {
	    int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
	    cursor.moveToFirst();
	    recordedVideoFilePath = cursor.getString(column_index_data);
	}
	File file = new File(recordedVideoFilePath);
	file.renameTo(videoFile);
	//	int rows = container.getContentResolver().delete(uri,null,null);
	
	if (thumbId != null && thumbId.length() > 0 && 
	    thumbWidth > 0 && thumbHeight > 0) {
	    Bitmap bm;
	    String thumb;
	    bm = ThumbnailUtils.createVideoThumbnail(videoFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
	    if (bm != null) {
		thumb = util.produceThumbnail(bm, thumbWidth, thumbHeight);
	    } else {
		bm = BitmapFactory.decodeResource(container.getResources(), R.drawable.check);
		thumb = util.produceThumbnail(bm, bm.getWidth(), bm.getHeight());
	    }
	    if (bm != null) {
		bm.recycle();
	    }
	    util.loadURL("javascript:ice.setThumbnail('" + thumbId +
			 "', 'data:image/jpg;base64," + thumb + "');");
	    return thumb;
	}
	return null;
    }
}
