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
import android.os.Handler;
import android.util.Log;
import android.os.Bundle;
import android.media.ThumbnailUtils;
import android.net.Uri;

import java.io.ByteArrayOutputStream;

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

    public void gotVideo(Intent data) {

	//Need to move file as MediaStore.Video.EXTRA_OUTPUT does not work;
	Uri uri = data.getData();
	String[] projection = { MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE  }; 
	Cursor cursor = container.managedQuery(uri, projection, null, null, null); 
	int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA); 
	int column_index_size = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE); 
	cursor.moveToFirst(); 
	String recordedVideoFilePath = cursor.getString(column_index_data);
	int recordedVideoFileSize = cursor.getInt(column_index_size);

	File file = new File(recordedVideoFilePath);
	file.renameTo(videoFile);
	int rows = container.getContentResolver().delete(uri,null,null);
	
	if (thumbId != null && thumbId.length() > 0 && 
	    thumbWidth > 0 && thumbHeight > 0) {
	    Bitmap bm = ThumbnailUtils.createVideoThumbnail(videoFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
	    String thumb = util.produceThumbnail(bm, thumbWidth, thumbHeight);
	    util.loadURL("javascript:ice.setThumbnail('" + thumbId +
			 "', 'data:image/jpg;base64," + thumb + "');");
	}
    }
}
