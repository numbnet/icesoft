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

import android.app.Activity;
import android.webkit.WebView;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import android.graphics.Matrix;
import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;

public class UtilInterface implements JavascriptInterface,
				      UploadProgressListener, Runnable {

    private Handler handler;
    private String url;
    private String userAgent;
    private final Activity container;
    private final WebView view;
    private LinkedList<HttpPostTask> postQueue;
    private LinkedList<String> responseQueue;
    private static final int PROGRESS_INTERVAL = 10;  //Percent
    private static final int RESPONSE_MSG = 1;
    private static final int MSG_PADDING = 150;

    public UtilInterface (Activity container, WebView webView, String userAgent) {
	this.container = container;
	this.view = webView;
    this.userAgent = userAgent;
	postQueue = new LinkedList();
	responseQueue = new LinkedList();
	handler = new Handler() {
		@Override
		    public void handleMessage(Message msg) {
		    switch(msg.what) {
		    case RESPONSE_MSG:
			loadURL("javascript:ice.handleResponse(window.ICEutil.getResult());");
			break;
		    default:
		    }
		}
	    };
    }

    public static InputStream getContentStream(String url) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(url);
	    CookieManager cookieManager = CookieManager.getInstance();
	    getRequest.setHeader("Cookie", cookieManager.getCookie(url));
		HttpResponse res = httpClient.execute(getRequest);
        return res.getEntity().getContent();
    }

    public void submitForm(String actionUrl, String serializedForm) {
        submitForm(actionUrl, serializedForm, null);
    }

    public void submitForm(String actionUrl, String serializedForm,
			   Runnable callback) {
	boolean gotValue = true;
	String[] result;
	long contentSize = 0;
	CountingMultiPartEntity content = new CountingMultiPartEntity(this);
	try {
	    if (actionUrl != null && actionUrl.length() > 0) {
		URL relativeAction = new URL(new URL(url), actionUrl);
		url = relativeAction.toString();
	    }
	    BasicNameValuePair[] params = getNameValuePairs(serializedForm, "&", "=");
	    for (int i=0; i<params.length; i++) {
		String packedName = params[i].getName();
		int nameSplit = packedName.indexOf("-");
		//if type is missing, "hidden" will be assumed
		String paramType = "hidden";
		String paramName = packedName;
		if (nameSplit > 0)  {
		    paramType = packedName.substring(0, nameSplit);
		    paramName = packedName.substring(nameSplit + 1);
		}
		if ("file".equals(paramType)) {
		    String fname = "undefined";
		    try {
			fname = params[i].getValue().replaceAll("%2F","/");

			InputStream is = new BufferedInputStream(new FileInputStream(fname));
			byte[] data = IOUtils.toByteArray(is);
			InputStreamBody isb = new InputStreamBody(
								  new ByteArrayInputStream(data), 
								  getMimeType(fname), params[i].getValue());
			contentSize += data.length;
			content.addPart(URLDecoder.decode(paramName,"UTF_8"), isb);
			is.close();
		    } catch (Exception e)  {
			Log.e("ICEutil", "Error Opening file " + fname, e);
		    }
		} else {
		    StringBody sb = new StringBody(URLDecoder.decode(params[i].getValue(),"UTF-8"));
		    contentSize += sb.getContentLength() + MSG_PADDING;
		    content.addPart(URLDecoder.decode(paramName,"UTF-8"), sb);
		}
	    }
	    if (!url.startsWith("http://") && !url.startsWith("http://")) {
		url = "http://" + url;
	    }
            HttpPost postRequest = new HttpPost(url);
	    CookieSyncManager.createInstance(container);
	    CookieSyncManager.getInstance().sync();
	    CookieManager cookieManager = CookieManager.getInstance();
	    postRequest.setHeader("Cookie", cookieManager.getCookie(url));
	    postRequest.setHeader("Faces-Request", "partial/ajax");
	    if (contentSize < content.getContentLength()) {
		contentSize = content.getContentLength();
	    }
	    content.measureProgress(contentSize/(PROGRESS_INTERVAL+1));
	    postRequest.setEntity(content);
	    queueRequest(postRequest, callback);
	} catch (Throwable e) {
	    Log.e("ICEutil", "Failed to submit form ", e);
	}
    }

    private void queueRequest(HttpPost postRequest, Runnable callback) {
	postQueue.add(new HttpPostTask(postRequest, callback));
	if (postQueue.size() == 1) {
	    Thread thread = new Thread(this);
	    thread.start();
	}
    }

    public void run() {
	HttpPost postRequest;
	DefaultHttpClient httpClient = new DefaultHttpClient();
	httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, userAgent);
	try {
	    while (postQueue.size() > 0) {
		sendProgress(0);
		HttpPostTask postTask = postQueue.remove();
		postRequest = postTask.httpPost;
		HttpResponse res = httpClient.execute(postRequest);
		sendProgress(100);
		StringWriter writer = new StringWriter();
		IOUtils.copy(res.getEntity().getContent(), writer);
		setResult(writer.toString());
		if (null != postTask.callback) {
		    postTask.callback.run();
		}
		handler.sendEmptyMessage(RESPONSE_MSG);
	    }
	} catch (Throwable e) {
	    Log.e("ICEutil", "HTTP POST failed ", e);
	}
    }

    public String getResult() {
	//Log.e("ICEutil", "Response q=" + responseQueue.size());
	if (responseQueue.size() > 0) { 
	    return responseQueue.remove();
	}
	return null;
    }
    protected void setResult(String res) {
	responseQueue.add(res);
    }

    public void transferred(long count) {
	sendProgress(new Long(count*PROGRESS_INTERVAL).intValue());
    } 

    public File getTempPath(){
	File path = new File( Environment.getExternalStorageDirectory(), container.getPackageName() );
	if(!path.exists()){
	    path.mkdir();
	}
	return path;
    }

    public void loadURL(final String url) {
        //ICEmobile-SX case where we do not have a WebView
        if (null == view)  {
            Log.d("ICEutil", "WebView is null, ignoring loadURL " + url);
            return;
        }
	handler.post(new Runnable() {
		public void run() {
		    //Log.e("ICEmobile","Loading URL: " + url);
		    view.loadUrl(url);
		}
	    });
    }

    private void sendProgress(int progress) {
	((SubmitProgressListener)container).submitProgress(progress);
    }

    private BasicNameValuePair[] getNameValuePairs(String data, String delim1, String delim2) {
        String splitRes[] = data.split(delim1);

        ArrayList<BasicNameValuePair> res = new ArrayList(splitRes.length);
        for (int i=0; i<splitRes.length; i++) {
            if (splitRes[i].contains(delim2)) {
                String nameValue[] = splitRes[i].split(delim2);
                if (nameValue.length == 2) {
                    res.add(new BasicNameValuePair(nameValue[0],nameValue[1]));
                } else if (nameValue.length == 1)   {
                            //likely corrupt pair
                    Log.w("ICEutil", "singleton form value " + splitRes[i]);
                    BasicNameValuePair pair = 
                                    new BasicNameValuePair(nameValue[0],"");
                    res.add(pair);
                }  else {
                    Log.e("ICEutil", "empty form value " + splitRes[i]);
                    res.add(new BasicNameValuePair("empty" + i,""));
                } 

            }
        }
        return res.toArray(new BasicNameValuePair[0]);
    }

    public void setCookie(String URL, String value) {
        CookieSyncManager.createInstance(container);
	    CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(URL, value);
    }

    public void setUrl(String URL) {
        CookieSyncManager.createInstance(container);
	    CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(URL,
                "com.icesoft.user-agent=HyperBrowser/1.0");
	url = URL;
    }

    private String getMimeType(String fname) {
	String fileType = fname.substring(fname.lastIndexOf('.')).toLowerCase();
	if (fileType.equals(".jpeg")) {
	    return "image/jpeg";
	} else if (fileType.equals(".mpga")) {
	    return "audio/mp4";
	} else if (fileType.equals(".3gpp")) {
	    return "audio/mp4";
	} else if (fileType.equals(".mp4")) {
	    return "video/mp4";
	}

	return "text/plain";
    }

    public String produceThumbnail(Bitmap image, int thumbWidth, 
				   int thumbHeight) {
	float scaleWidth = (thumbWidth*1.0f)/(image.getWidth()*1.0f);
	float scaleHeight = (thumbHeight*1.0f)/(image.getHeight()*1.0f);
	float scaleAmount = Math.min(scaleWidth,scaleHeight);
	Matrix matrix = new Matrix();
	matrix.postScale(scaleAmount,scaleAmount);
	if (!matrix.isIdentity()) {
	    image = Bitmap.createBitmap(image, 0, 0, image.getWidth(),
					image.getHeight(), matrix, true);
	}
	ByteArrayOutputStream buffer = new ByteArrayOutputStream(image.getRowBytes()*image.getHeight());
	image.compress(Bitmap.CompressFormat.JPEG, 100, buffer);
	return new Base64().encode(buffer.toByteArray());
    }

    private class Base64 {
	String base64code = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz" + "0123456789" + "+/";
  
	public byte[] zeroPad(int length, byte[] bytes) {
	    byte[] padded = new byte[length]; // initialized to zero by JVM
	    System.arraycopy(bytes, 0, padded, 0, bytes.length);
	    return padded;
	}
 
	public String encode(String string) {
	    return encode(string.getBytes());
	}

	public String encode(byte[] stringArray) {
 
	    String encoded = "";

	    // determine how many padding bytes to add to the output
	    int paddingCount = (3 - (stringArray.length % 3)) % 3;
	    // add any necessary padding to the input
	    stringArray = zeroPad(stringArray.length + paddingCount, stringArray);
	    // process 3 bytes at a time, churning out 4 output bytes
	    // worry about CRLF insertions later
	    for (int i = 0; i < stringArray.length; i += 3) {
		int j = ((stringArray[i] & 0xff) << 16) +
		    ((stringArray[i + 1] & 0xff) << 8) + 
		    (stringArray[i + 2] & 0xff);
		encoded = encoded + base64code.charAt((j >> 18) & 0x3f) +
		    base64code.charAt((j >> 12) & 0x3f) +
		    base64code.charAt((j >> 6) & 0x3f) +
		    base64code.charAt(j & 0x3f);
	    }
	    // replace encoded padding nulls with "="
	    return encoded.substring(0, encoded.length() -
				     paddingCount) + "==".substring(0, paddingCount);
 
	}
    }

    // code borrowed from http://stackoverflow.com/questions/254719/file-upload-with-java-with-progress-bar
    private UploadProgressListener listener_;

    private class CountingMultiPartEntity extends MultipartEntity {

	private CountingOutputStream outputStream_;
	private OutputStream lastOutputStream_;
	private long chunkSize;

	// the parameter is the same as the ProgressListener class in tuler's answer
	public CountingMultiPartEntity(UploadProgressListener listener) {
	    super();
	    listener_ = listener;
	}

	@Override
	    public void writeTo(OutputStream out) throws IOException {
	    // If we have yet to create the CountingOutputStream, or the
	    // OutputStream being passed in is different from the OutputStream used
	    // to create the current CountingOutputStream
	    if ((lastOutputStream_ == null) || (lastOutputStream_ != out)) {
		lastOutputStream_ = out;
		outputStream_ = new CountingOutputStream(out, chunkSize);
	    }

	    super.writeTo(outputStream_);
	}

	public void measureProgress(long chunkSize) {
	    this.chunkSize = chunkSize;
	    if (outputStream_ != null) {
		outputStream_.setChunkSize(chunkSize);
	    }
	}	    
    }

    private class CountingOutputStream extends FilterOutputStream {

	private long transferred = 0;
	private OutputStream wrappedOutputStream_;
	private long chunkSize;
	private long outCount=0;

	public CountingOutputStream(final OutputStream out, long chunkSize) {
	    super(out);
	    wrappedOutputStream_ = out;
	    this.chunkSize = chunkSize;
	}

	public void setChunkSize(long chunkSize) {
	    this.chunkSize = chunkSize;
	    transferred = 0;
	    outCount = 0;
	}

	public void write(byte[] b, int off, int len) throws IOException {
	    wrappedOutputStream_.write(b,off,len);
	    outCount += len;
	    if (outCount >= chunkSize) {
		outCount = 0;
		++transferred;
		listener_.transferred(transferred);
	    }
	}

	public void write(int b) throws IOException {
	    super.write(b);
	}
    }
}

class HttpPostTask  {
    HttpPost httpPost;
    Runnable callback;

    public HttpPostTask(HttpPost httpPost, Runnable callback)  {
        this.httpPost = httpPost;
        this.callback = callback;
    }

}