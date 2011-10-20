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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URL;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import android.graphics.Matrix;
import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;

public class UtilInterface implements JavascriptInterface,
				      UploadProgressListener, Runnable {

    private String result;
    private Handler handler;
    private String url;
    private Activity container;
    private final WebView view;
    private DefaultHttpClient httpClient;
    private HttpPost postRequest;

    private static final int PROGRESS_INTERVAL = 10;  //Percent
    private static final int PROGRESS_MSG = 0;
    private static final int RESPONSE_MSG = 1;

    public UtilInterface (Activity container, WebView webView) {
	this.container = container;
	this.view = webView;
	handler = new Handler() {
		@Override
		    public void handleMessage(Message msg) {
		    switch(msg.what) {
		    case PROGRESS_MSG:
			loadURL("javascript:ice.progress(" + msg.arg1 + ");");
			break;
		    case RESPONSE_MSG:
			loadURL("javascript:ice.handleResponse(window.ICEutil.getResult());");
			break;
		    default:
		    }
		}
	    };
    }

    public void submitForm(String actionUrl, String serializedForm) {
	boolean gotValue = true;
	String[] result;
	long contentSize = 0;
	CountingMultiPartEntity content = new CountingMultiPartEntity(this);
	try {
	    URL relativeAction = new URL(new URL(url), actionUrl);
        url = relativeAction.toString();
	    BasicNameValuePair[] params = getNameValuePairs(serializedForm, "&", "=");
	    for (int i=0; i<params.length; i++) {
		if (params[i].getName().contains("file")) {
		    String fname = params[i].getValue().replaceAll("%2F","/");
		    InputStream is = new BufferedInputStream(new FileInputStream(fname));
		    byte[] data = IOUtils.toByteArray(is);
		    InputStreamBody isb = new InputStreamBody(new ByteArrayInputStream(data), getMimeType(fname), params[i].getValue());
		    contentSize += data.length;
		    content.addPart(URLDecoder.decode(params[i].getName(),"UTF_8"), isb);
		    is.close();
		} else {
		    StringBody sb = new StringBody(URLDecoder.decode(params[i].getValue(),"UTF-8"));
		    contentSize += sb.getContentLength();
		    content.addPart(URLDecoder.decode(params[i].getName(),"UTF-8"), sb);
		}
	    }
            httpClient = new DefaultHttpClient();
            postRequest = new HttpPost(url);
	    CookieSyncManager.createInstance(container);
	    CookieSyncManager.getInstance().sync();
	    CookieManager cookieManager = CookieManager.getInstance();
	    postRequest.setHeader("Cookie", cookieManager.getCookie(url));
	    postRequest.setHeader("Faces-Request", "partial/ajax");
	    content.measureProgress(contentSize/(PROGRESS_INTERVAL+1));
	    postRequest.setEntity(content);
	    loadURL("javascript:ice.progress(0);");
	    Thread thread = new Thread(this);
	    thread.start();
	} catch (Throwable e) {
	    Log.e("ICEutil", e.toString());
	}
    }

    public void run() {
	try {
	    HttpResponse res = httpClient.execute(postRequest);
	    StringWriter writer = new StringWriter();
	    IOUtils.copy(res.getEntity().getContent(), writer);
	    setResult(writer.toString());
	    sendProgress(100);
	    handler.sendEmptyMessage(RESPONSE_MSG);
	} catch (Throwable e) {
	    Log.e("ICEutil", e.toString());
	}
    }

    public String getResult() {
	return result;
    }
    protected void setResult(String res) {
	result=res;
    }

    public void transferred(long count) {
	sendProgress(new Long(count*PROGRESS_INTERVAL).intValue());
    } 

    protected File getTempPath(){
	File path = new File( Environment.getExternalStorageDirectory(), container.getPackageName() );
	if(!path.exists()){
	    path.mkdir();
	}
	return path;
    }

    protected void loadURL(final String url) {
	handler.post(new Runnable() {
		public void run() {
		    //Log.e("ICEmobile","Loading URL: " + url);
		    view.loadUrl(url);
		}
	    });
    }

    private void sendProgress(int progress) {
	if (progress <= 100) {
	    Message msg = new Message();
	    msg.what = PROGRESS_MSG;
	    msg.arg1 = progress;
	    handler.sendMessage(msg);
	}
    }

    private BasicNameValuePair[] getNameValuePairs(String data, String delim1, String delim2) {
	if (!data.contains(delim1)) {
	    return null;
	} else {
	    String splitRes[] = data.split(delim1);
	    
	    BasicNameValuePair[] res = new BasicNameValuePair[splitRes.length];
	    for (int i=0; i<splitRes.length; i++) {
		if (splitRes[i].contains(delim2)) {
		    String nameValue[] = splitRes[i].split(delim2);
		    if (nameValue.length == 2) {
			res[i] = new BasicNameValuePair(nameValue[0],nameValue[1]);
		    } else {
			res[i] = new BasicNameValuePair(nameValue[0],"");
		    }
		}
	    }
	    return res;
	}
    }

    protected void setUrl(String URL) {
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
