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

package org.icemobile.client.android;

import java.io.FileWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.content.DialogInterface;
import android.webkit.HttpAuthHandler;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.view.ViewGroup;
import android.os.Bundle;
import android.content.res.Configuration;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.GeolocationPermissions;
import android.content.res.AssetManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.webkit.WebResourceResponse;
import android.net.Uri;

import android.media.MediaPlayer;
import android.widget.VideoView;
import android.widget.FrameLayout;
import android.view.View;
import android.webkit.DownloadListener;
import android.net.Uri;
import android.content.ActivityNotFoundException;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation.AnimationListener;


import org.icemobile.client.android.c2dm.C2dmHandler;
import org.icemobile.client.android.c2dm.C2dmRegistrationHandler;
import org.icemobile.client.android.qrcode.CaptureActivity;
import org.icemobile.client.android.qrcode.CaptureJSInterface;
import org.icemobile.client.android.qrcode.Intents;

import org.icemobile.client.android.arview.ARViewInterface;
import org.icemobile.client.android.arview.ARViewHandler;
import org.icemobile.client.android.audio.AudioInterface;
import org.icemobile.client.android.audio.AudioRecorder;
import org.icemobile.client.android.audio.AudioPlayer;
import org.icemobile.client.android.camera.CameraInterface;
import org.icemobile.client.android.camera.CameraHandler;
import org.icemobile.client.android.contacts.ContactListInterface;
import org.icemobile.client.android.video.VideoInterface;
import org.icemobile.client.android.video.VideoHandler;

import org.icemobile.client.android.util.UtilInterface;
import org.icemobile.client.android.util.JavascriptLoggerInterface;
import org.icemobile.client.android.util.FileLoader;
import org.icemobile.client.android.util.SubmitProgressListener;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

import android.graphics.Paint;

public class ICEmobileContainer extends Activity
    implements SharedPreferences.OnSharedPreferenceChangeListener,
	       ConnectionChangeListener, C2dmRegistrationHandler, SubmitProgressListener {

    /* Container configuration constants */
    protected static final String HOME_URL = "http://www.icesoft.org/java/demos/m/icemobile-demos.html";
    protected static final boolean INCLUDE_CAMERA = true;
    protected static final boolean INCLUDE_AUDIO = true;
    protected static final boolean INCLUDE_VIDEO = true;
    protected static final boolean INCLUDE_CONTACTS = true;
    protected static final boolean INCLUDE_LOGGING = true;
    protected static final boolean INCLUDE_QRCODE = true;
    protected static final boolean INCLUDE_ARVIEW = true;
    protected static final int HISTORY_SIZE = 20;
    protected static final int NETWORK_DOWN_DELAY = 5000;
    protected static final String C2DM_SENDER = "1020381675267";
    /* Intent Return Codes */
    protected static final int TAKE_PHOTO_CODE = 1;
    protected static final int TAKE_VIDEO_CODE = 2;
    protected static final int HISTORY_CODE = 3;
    public static final int SCAN_CODE = 4;
    protected static final int RECORD_CODE = 5;
    protected static final int ARVIEW_CODE = 6;
    protected static final int CONTACT_CODE = 7;

    public static final String SCAN_ID = "org.icemobile.id";

    // Handler messages
    protected static final int PROGRESS_MSG = 0;

    // Authentication dialog
    protected static final int AUTH_DIALOG = 2;

    private WebView mWebView;
    private Handler mHandler = new Handler();
    private UtilInterface utilInterface;
    private CameraHandler mCameraHandler;
    private ContactListInterface mContactListInterface;
    private CameraInterface mCameraInterface;
    private CaptureActivity mCaptureActivity;
    private JavascriptLoggerInterface mLoggerInterface;
    private CaptureJSInterface mCaptureInterface;
    private AudioInterface mAudioInterface;
    private AudioRecorder mAudioRecorder;
    private AudioPlayer mAudioPlayer;
    private C2dmHandler mC2dmHandler;
    private VideoHandler mVideoHandler;
    private VideoInterface mVideoInterface;
    private ARViewHandler mARViewHandler;
    private ARViewInterface mARViewInterface;
    private AssetManager assetManager;
    private FileLoader fileLoader;
    private Activity self;
    private String currentURL = "";
    private String newURL;
    private SharedPreferences prefs;
    private HistoryManager historyManager;
    private Vector history;
    private boolean isNetworkUp;
    private ConnectionChangeService connectionChangeService;
    private AlertDialog networkDialog;
    private boolean accelerated;
    private String authUser;
    private String authPw;
    private String mUserAgentString; 
    private boolean pendingCloudPush;
    private ProgressBar pBar;
    private Animation fadeOut;
    private int lastProgress;
    private View mCachedVideoView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
	pendingCloudPush = false;

	// Create handler;
	mHandler = new Handler() {
		@Override
		    public void handleMessage(Message msg) {
		    switch(msg.what) {
		    case PROGRESS_MSG:
			adjustProgress(msg.arg1);
			break;
		    default:
		    }
		}
	    };

        /* Bind to network connectivity monitoring service */
        Intent bindingIntent = new Intent(self, ConnectionChangeService.class);
        boolean bound = self.bindService(bindingIntent, mConnection,
                                         Context.BIND_AUTO_CREATE);

        /* Establish view */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        /* Initialize the WebView */
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.clearCache(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setPluginsEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebViewClient(new ICEfacesWebViewClient());
        mWebView.setWebChromeClient(new ICEfacesWebChromeClient());
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        Log.d("ICEmobile", "User Agent = " + mWebView.getSettings().getUserAgentString());

	// Initialize Progress bar
	pBar = (ProgressBar) findViewById(R.id.pbar);
	pBar.setVisibility(android.view.View.INVISIBLE);
	pBar.setMax(100);
	pBar.setProgress(0);
	fadeOut = new AlphaAnimation(1, 0);
	fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
	fadeOut.setDuration(250);
	fadeOut.setAnimationListener(new AnimationListener() {
		public void onAnimationEnd(Animation animation) {
		    pBar.setVisibility(android.view.View.INVISIBLE);
		}
		public void onAnimationStart(Animation animation) {
		}
		public void onAnimationRepeat(Animation animation) {
		}
	    });

        mUserAgentString = mWebView.getSettings().getUserAgentString();
        assetManager = getAssets();
        fileLoader = new FileLoader(assetManager);
        mWebView.addJavascriptInterface(fileLoader, "ICEassets");

        mWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition,
                                        String mimeType, long size) {
                Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                viewIntent.setDataAndType(Uri.parse(url), mimeType);
                try {
                    startActivity(viewIntent);
                } catch (ActivityNotFoundException ex) {
                    Log.e("ICEcontainer", "Couldn't find activity to view mimetype: " + mimeType);
                }
            }
        });

        /* Establish initial container configuration */
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        accelerated = prefs.getBoolean("accelerate", false);
        includeUtil();
        if (INCLUDE_QRCODE) {
            includeQRCode();
        }
        if (INCLUDE_ARVIEW && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
            includeARView();
        }
        if (INCLUDE_CAMERA) {
            includeCamera();
        }
        if (INCLUDE_AUDIO) {
            includeAudio();
        }
        if (INCLUDE_VIDEO) {
            includeVideo();
        }
        if (INCLUDE_CONTACTS) {
            includeContacts();
        }
        if (INCLUDE_LOGGING) {
            includeLogger();
        }

        if (prefs.getBoolean("c2dm", true)) {
            includeC2dm();
        }

        historyManager = new HistoryManager(HISTORY_SIZE);
        prefs.registerOnSharedPreferenceChangeListener(this);
        SharedPreferences.Editor editor = prefs.edit();
        if (!prefs.contains("url")) {
            editor.putString("url", HOME_URL);
            editor.commit();
        }
        newURL = prefs.getString("url", HOME_URL);
        setGallery(prefs.getBoolean("gallery", false));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO_CODE:
                    mCameraHandler.gotPhoto();
                    break;
                case TAKE_VIDEO_CODE:
                    mVideoHandler.gotVideo(data);
                    break;
                case HISTORY_CODE:
                    newURL = data.getStringExtra("url");
                    historyManager.add(newURL);
                    loadUrl();
                    break;
                case SCAN_CODE:
                    String scanResult = data.getStringExtra(Intents.Scan.RESULT);
                    utilInterface.loadURL(
                        "javascript:ice.addHidden(ice.currentScanId, ice.currentScanId, '" +
                            scanResult + "');");
                    break;
                case RECORD_CODE:
                    mAudioRecorder.gotAudio(data);
                    break;
                case ARVIEW_CODE:
//		mARViewHandler.arViewComplete(data);
                    break;
                case CONTACT_CODE:
		    Uri mContactUri = (Uri)data.getData();
		    mContactListInterface.gotContact(mContactUri);
                    break;
            }
        }
    }

    @Override
	protected void onResume() {
        super.onResume();
        utilInterface.loadURL("javascript:if( ice.push ){ice.push.connection.resumeConnection();}");
        if (!newURL.equals(currentURL)) {
            loadUrl();
        } else {
	    // Clear any existing C2DM notifications;
	    if (pendingCloudPush) {
		pendingCloudPush = false;
		mC2dmHandler.clearPendingNotification();
		utilInterface.loadURL("javascript:if( ice ){ice.mobiRefresh();}");
	    }
	}
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAudioPlayer.release();
        utilInterface.loadURL("javascript:if( ice.push ){ice.push.connection.pauseConnection();}");
    }

    @Override
    protected void onStop() {
        historyManager.save();
        try {
            self.unbindService(mConnection);
        } catch (Exception e) {
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dev_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.reload:
                mWebView.clearCache(true);
                mWebView.reload();
                return true;
            case R.id.preferences:
                intent = new Intent(this, ContainerPreferences.class);
                intent.putExtra("url", currentURL);
                startActivity(intent);
                return true;
            case R.id.history:
                intent = new Intent();
                intent.setClass(this, HistoryList.class);
                String[] historyList = (String[]) history.toArray(new String[history.size()]);
                intent.putExtra("history", historyList);
                startActivityForResult(intent, HISTORY_CODE);
                return true;
            case R.id.clear_cookies:
		CookieSyncManager.createInstance(this);         
		CookieManager cookieManager = CookieManager.getInstance();      		cookieManager.removeAllCookie();
		cookieManager.setCookie(currentURL, "com.icesoft.user-agent=HyperBrowser/1.0");
                return true;
            case R.id.stop:
                if (mC2dmHandler != null) {
                    mC2dmHandler.stop();
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        historyManager.save();
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        historyManager.load();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        //don't want to recreate the activity;
        super.onConfigurationChanged(config);
    }

    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals("url")) {
            newURL = prefs.getString(key, HOME_URL);
	    if (!newURL.startsWith("http://")) {
		newURL = "http://" + newURL;
	    }
            historyManager.add(newURL);
        } else if (key.equals("gallery")) {
            setGallery(prefs.getBoolean(key, false));
        } else if (key.equals("c2dm")) {
            if (prefs.getBoolean(key, true)) {
                // Turn on C2DM
                includeC2dm();
            } else {
                // Turn off C2DM
                if (mC2dmHandler != null) {
                    mC2dmHandler.stop();
                }
            }
        }
    }

    public void handleC2dmRegistration(String id) {
        setCloudNotificationId();
    }

    public void handleC2dmNotification() {
        pendingCloudPush = true;
    }

    public void submitProgress(int progress) {
	Message msg = new Message();
	msg.what = PROGRESS_MSG;
	msg.arg1 = progress;
	mHandler.sendMessage(msg);
    }

    private void adjustProgress(int progress) {
	if (progress != 100) {
	    pBar.setVisibility(android.view.View.VISIBLE);
	} else if (lastProgress != 100) {
	    pBar.startAnimation(fadeOut);
	}
	self.setProgress(progress * 100);
	lastProgress = progress;
	pBar.setProgress(progress);
    }

    protected void setCloudNotificationId() {
        Log.d("ICEmobile", "Setting cloud push: " + getCloudNotificationId());
        utilInterface.loadURL("javascript:if( ice.push ){ ice.push.parkInactivePushIds('" +
                                  getCloudNotificationId() + "');}");
    }

    protected String getCloudNotificationId() {
        String id = null;
        if (mC2dmHandler != null) {
            id = mC2dmHandler.getRegistrationId();
        }
        if (id == null) {
            id = prefs.getString("email", null);
            if (id != null) {
                if (id.contains("@")) {
                    id = new String("mail:" + id);
                } else {
                    id = null;
                }
            }
        }
        if (id == null) {
            id = new String("");
        }
        return id;
    }

    private void setGallery(boolean gallery) {
        if (mCameraHandler != null) {
            mCameraHandler.setGallery(gallery);
        }
        if (mVideoHandler != null) {
            mVideoHandler.setGallery(gallery);
        }
    }

    private class ICEfacesWebViewClient extends WebViewClient {
        boolean stillLoading = true;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            stillLoading = true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:eval(' ' + window.ICEassets.loadAssetFile('native-interface.js'));");
            setCloudNotificationId();
            utilInterface.setUrl(url);
            historyManager.add(url);
            currentURL = url;
            newURL = url;
            stillLoading = false;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            //prevent icepush from blocking if page not yet loaded
            if (url.contains("listen.icepush"))  {
                if (stillLoading)  {
                    return new WebResourceResponse(null, null, null);
                }
            }
            return null;
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view,
                                              HttpAuthHandler handler,
                                              String host, String realm) {
            self.showDialog(AUTH_DIALOG);
            if (authUser != null && authPw != null) {
                handler.proceed(authUser, authPw);
            } else {
                handler.cancel();
            }
        }
    }

    final class ICEfacesWebChromeClient extends WebChromeClient
        implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        WebChromeClient.CustomViewCallback {

        private VideoView video;
        private View view;
        private CustomViewCallback callback;

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            Log.e("ICEcontainer", "Alert: " + message);
            result.confirm();
            return true;
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            this.view = view;
            this.callback = callback;
            if (view instanceof FrameLayout) {
                FrameLayout frame = (FrameLayout) view;
                if (frame.getFocusedChild() instanceof VideoView) {
                    video = (VideoView) frame.getFocusedChild();
                    frame.removeView(video);
                    mWebView.addView(video);
                    mCachedVideoView = video;
                    video.setOnCompletionListener(this);
                    video.setOnErrorListener(this);
                    video.start();
                }
            }
        }

        public void onCompletion(MediaPlayer mp) {
            returnToWebView();
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            returnToWebView();
            return true;
        }

        public void onCustomViewHidden() {
        }

        private void returnToWebView() {
            video.stopPlayback();
            callback.onCustomViewHidden();
            if (mCachedVideoView != null) {
                mWebView.removeView(mCachedVideoView);
                mCachedVideoView = null;
            }
        }

        /**
         * Listen for content loading progress state.  Dialog will show
         * as progress events come in and hide when 100% is reached.
         *
         * @param view     current webview.
         * @param progress progress value in 0.0 - 1.0
         */
        @Override
        public void onProgressChanged(WebView view, int progress) {
	    adjustProgress(progress);
        }
    }

    private void loadUrl() {
        currentURL = newURL;
        utilInterface.setUrl(currentURL);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("url", currentURL);
        editor.commit();
        mWebView.loadUrl(currentURL);
        historyManager.add(currentURL);
    }

    private void includeUtil() {
        utilInterface = new UtilInterface(this, mWebView, mUserAgentString);
        mWebView.addJavascriptInterface(utilInterface, "ICEutil");
    }

    private void includeCamera() {
        mCameraHandler = new CameraHandler(this, mWebView, utilInterface, TAKE_PHOTO_CODE);
        mCameraInterface = new CameraInterface(mCameraHandler);
        mWebView.addJavascriptInterface(mCameraInterface, "ICEcamera");
    }

    private void includeContacts() {

        mContactListInterface = new ContactListInterface(utilInterface, this, getContentResolver(), CONTACT_CODE);
        mWebView.addJavascriptInterface(mContactListInterface, "ICEContacts");
    }

    private void includeARView() {
        mARViewHandler = new ARViewHandler(this, mWebView, utilInterface, ARVIEW_CODE);
        mARViewInterface = new ARViewInterface(mARViewHandler);
        mWebView.addJavascriptInterface(mARViewInterface, "ARView");
    }

    private void includeQRCode() {
        mCaptureInterface = new CaptureJSInterface(this, SCAN_CODE, SCAN_ID);
        mWebView.addJavascriptInterface(mCaptureInterface, "ICEqrcode");
    }

    private void includeLogger() {
        mLoggerInterface = new JavascriptLoggerInterface();
        mWebView.addJavascriptInterface(mLoggerInterface, "ICElogger");
    }

    private void includeVideo() {
        mVideoHandler = new VideoHandler(this, mWebView, utilInterface, TAKE_VIDEO_CODE);
        mVideoInterface = new VideoInterface(mVideoHandler);
        mWebView.addJavascriptInterface(mVideoInterface, "ICEvideo");
    }

    private void includeAudio() {
        mAudioRecorder = new AudioRecorder(this, utilInterface, RECORD_CODE);
        mAudioPlayer = new AudioPlayer();
        mAudioInterface = new AudioInterface(mAudioRecorder, mAudioPlayer);
        mWebView.addJavascriptInterface(mAudioInterface, "ICEaudio");
    }
    
    private void includeC2dm() {
        if (mC2dmHandler == null) {
            mC2dmHandler = new C2dmHandler(this, R.drawable.c2dm_icon, "ICE", "ICEmobile", "C2DM Notification", this);
        }
        mC2dmHandler.start(C2DM_SENDER);
    }

    private class HistoryManager {
        private File historyFile;
        private int maxSize;

        HistoryManager(int maxSize) {
            this.maxSize = maxSize;
            history = new Vector(maxSize);
            load();
        }

        public void add(String url) {
            int i = 0;
            while (i < history.size() && !url.equals((String) history.get(i))) {
                i++;
            }
            boolean changed = false;
            if (i < history.size()) {
                if (i != 0) {
                    history.remove(i);
                    changed = true;
                }
            } else {
                changed = true;
                if (history.size() == maxSize) {
                    history.remove(maxSize - 1);
                }
            }

            if (changed && url != null) {
                history.add(0, url);
                save();
            }
        }

        public void load() {
            File historyFile = new File(utilInterface.getTempPath(), "history.log");
            try {
                String historyList = fileLoader.readTextFile(new FileInputStream(historyFile));
                history = new Vector(Arrays.asList(historyList.split(" ")));
            } catch (IOException e) {
            }
        }

        public void save() {
            FileWriter out;
            try {

                File historyFile = new File(utilInterface.getTempPath(), "history.log");
                out = new FileWriter(historyFile);
                for (Enumeration i = history.elements(); i.hasMoreElements(); ) {
                    out.write((String) i.nextElement());
                    out.write(" ");
                }
                out.close();
            } catch (IOException e) {
                Log.e("ICEcontainer", "Can't write URL history");
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case AUTH_DIALOG:
                AlertDialog.Builder builder;
                AlertDialog authDialog;

                LayoutInflater inflater = (LayoutInflater) self.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.auth_dialog,
                                               (ViewGroup) findViewById(R.id.auth_root));
                final EditText user = (EditText) layout.findViewById(R.id.auth_user_input);
                final EditText pw = (EditText) layout.findViewById(R.id.auth_pw_input);

                builder = new AlertDialog.Builder(self);
                builder.setTitle(R.string.auth_title);
                builder.setView(layout);
                builder.setNegativeButton(R.string.auth_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        authUser = null;
                        authPw = null;
                        removeDialog(AUTH_DIALOG);
                    }
                });

                builder.setPositiveButton(R.string.auth_login, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        authUser = user.getText().toString();
                        authPw = pw.getText().toString();
                        removeDialog(AUTH_DIALOG);
                    }
                });
                authDialog = builder.create();
                return authDialog;
            default:
                return null;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName comp, IBinder service) {
            connectionChangeService = ((ConnectionChangeService.LocalBinder) service).getService();
            isNetworkUp = connectionChangeService.setListener(ICEmobileContainer.this,
                                                              NETWORK_DOWN_DELAY, self);
            if (!isNetworkUp) {
                networkIsDown();
            }
        }

        public void onServiceDisconnected(ComponentName arg0) {
            connectionChangeService = null;
        }
    };

    public void networkIsDown() {
        isNetworkUp = false;
        mHandler.post(new Runnable() {
            public void run() {
                if (networkDialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(self);
                    builder.setTitle(self.getString(R.string.networkDialogTitle));
                    builder.setMessage(self.getString(R.string.networkDialogMsg));
                    builder.setCancelable(false);
                    networkDialog = builder.create();
                }
                networkDialog.show();
            }
        });
    }

    public void networkIsUp() {
        isNetworkUp = true;
        mHandler.post(new Runnable() {
            public void run() {
                if (networkDialog != null) {
                    networkDialog.hide();
                }
                mWebView.reload();
            }
        });
    }
}
