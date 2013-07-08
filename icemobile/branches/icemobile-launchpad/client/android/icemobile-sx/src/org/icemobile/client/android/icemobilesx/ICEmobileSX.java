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

package org.icemobile.client.android.icemobilesx;

import java.util.Map;
import java.util.HashMap;
import java.net.URLEncoder;
import java.net.URLDecoder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.widget.ImageView;
import android.view.Window;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.net.Uri;
import android.provider.Browser;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import java.net.MalformedURLException;

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
import org.icemobile.client.android.util.FileLoader;
import org.icemobile.client.android.util.SubmitProgressListener;

public class ICEmobileSX extends Activity 
    implements C2dmRegistrationHandler, SubmitProgressListener {

    private static final String LT = "ICEmobileSX";

    private class SxProgress extends ProgressDialog {
	public SxProgress(Context context) {
	    super(context);
	}

	@Override
        protected void onStop() {
	    stopped = true;
	}
    }

    /* Container configuration constants */
    public static final String HOME_URL = "http://www.icesoft.org/java/demos/icemobile-demos.jsf";
    protected static final boolean INCLUDE_CAMERA = true;
    protected static final boolean INCLUDE_AUDIO = true;
    protected static final boolean INCLUDE_VIDEO = true;
    protected static final boolean INCLUDE_CONTACTS = true;
    protected static final boolean INCLUDE_LOGGING = true;
    protected static final boolean INCLUDE_QRCODE = true;
    protected static final boolean INCLUDE_ARVIEW = true;
    protected static final boolean INCLUDE_GCM = true;
    protected static final String GCM_SENDER = "1020381675267";

    /* Intent Return Codes */
    protected static final int TAKE_PHOTO_CODE = 1;
    protected static final int TAKE_VIDEO_CODE = 2;
    protected static final int HISTORY_CODE = 3;
    public static final int SCAN_CODE = 4;
    protected static final int RECORD_CODE = 5;
    protected static final int ARVIEW_CODE = 6;
    protected static final int ARMVIEW_CODE = 7;
    protected static final int ADD_LAUNCHABLE = 8;

    public static final String SCAN_ID = "org.icemobile.id";

    private Handler mHandler = new Handler();
    private UtilInterface utilInterface;
    private FileLoader fileLoader;
    private CameraHandler mCameraHandler;
    private ContactListInterface mContactListInterface;
    private CameraInterface mCameraInterface;
    private CaptureActivity mCaptureActivity;
    private CaptureJSInterface mCaptureInterface;
    private AudioInterface mAudioInterface;
    private AudioRecorder mAudioRecorder;
    private AudioPlayer mAudioPlayer;
    private C2dmHandler mC2dmHandler;
    private VideoHandler mVideoHandler;
    private VideoInterface mVideoInterface;
    private ARViewHandler mARViewHandler;
    private ARViewInterface mARViewInterface;
    private Activity self;
    private boolean pendingCloudPush;
    private Uri mReturnUri;
    private Uri mPOSTUri;
    private String mParameters;
    private String mCurrentId;
    private String mCurrentjsessionid;
    private String mCurrentMediaFile;
    private Runnable mBrowserReturn;
    private boolean doRegister = false;
    private SxProgress progressDialog;
    private boolean stopped;
    private LaunchManager launchManager;
    private Dialog workingDialog;

    public void returnToBrowser()  {
        if (null == mReturnUri)  {
	    return;
	    //            mReturnUri = Uri.parse(HOME_URL);
	 }
	 Intent browserIntent = new 
		 Intent(Intent.ACTION_VIEW, 
		 mReturnUri);
	 Launchable app = launchManager.find(mReturnUri.toString());
	 if (app != null) {
	     browserIntent.putExtra(Browser.EXTRA_APPLICATION_ID, app.getName());
	 }
	 startActivity(browserIntent);
     }

     public Map parseQuery(String uri)  {
	 HashMap parts = new HashMap();
	 String[] nvpairs = uri.split("&");
	 for (String pair : nvpairs)  {
	     int index = pair.indexOf("=");
	     if (-1 == index)  {
		 parts.put(pair, null);
	     } else {
		 String name = URLDecoder.decode(pair.substring(0, index));
		 String value = URLDecoder.decode(pair.substring(index + 1));
		 parts.put(name, value);
	     }
	 }

	 return parts;

     }

     /**
      * Called when the activity is first created.
      */
     @Override
     public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 self = (Activity)this;
	 pendingCloudPush = false;
	 includeUtil();
	 fileLoader = new FileLoader(getAssets());
	 launchManager = new LaunchManager(utilInterface, fileLoader);
	 workingDialog = new Dialog(this, android.R.style.Theme_NoTitleBar_Fullscreen);
	 workingDialog.setContentView(R.layout.working);

	 mBrowserReturn = new Runnable()  {
	     public void run()  {
		 returnToBrowser();
		 workingDialog.dismiss();
	     }
	 };

	 //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 setContentView(R.layout.main);
	 Log.e(LT, "Content view main");
	 GridView gridview = (GridView) findViewById(R.id.gridview);
	 gridview.setAdapter(new LaunchableAdapter(this, launchManager));

	 gridview.setOnItemClickListener(new OnItemClickListener() {
		 public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		     mReturnUri = Uri.parse(launchManager.get(position).getAppUrl().toString());
		     returnToBrowser();
		     Log.e("ICEmobileSX", "Grid pressed: " + position);
		 }
	     });
	 //	ImageView myImage = (ImageView) findViewById(R.id.background);
	 //myImage.setAlpha(127);
	 //progressDialog = new SxProgress(this);
	 //stopped = false;
	 //progressDialog.show( self, "ICEmobile-SX", "is working...",false, true);

	 if (INCLUDE_QRCODE) {
	     includeQRCode();
	 }
	 if (INCLUDE_ARVIEW && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
	     includeARView();
	 }
	 if (INCLUDE_ARVIEW ) {
	     includeARMarkers();
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
	 if (INCLUDE_GCM) {
	     includeC2dm();
	 }

	 handleIntent(getIntent());
     }

     public void onNewIntent(Intent intent) {

	 handleIntent(intent);
     }

     private void handleIntent (Intent intent) {
	 Uri uri = intent.getData();
	 Log.d(LT, "handleIntent" + uri);
	 Map commandParts = new HashMap();
	 Map commandParams = new HashMap();
	 String commandName = null;

	 if (null != uri)  {
	     String uriString = uri.toString();
	     String fullCommand = uriString.substring("icemobile://".length());
	     commandParts = parseQuery(fullCommand);

	     Log.d(LT, "processing commandParts " + commandParts);
	     String command = (String) commandParts.get("c");
	     Log.d(LT, "processing command " + command);

	     int queryIndex = command.indexOf("?");
	     if (-1 == queryIndex)  {
		 commandName = command;
	     } else {
		 commandName = command.substring(0, queryIndex);
		 String commandParamsString = command.substring(queryIndex + 1);
		 commandParams = parseQuery(commandParamsString);
		 mCurrentId = (String) commandParams.get("id");
	     }

	     mReturnUri = Uri.parse((String) commandParts.get("r"));
	     mPOSTUri = Uri.parse((String) commandParts.get("u"));
	     mParameters = (String) commandParts.get("p");

	     // Set working splash screen;
	     Log.e(LT, "Content view working");
	     ImageView imageView = (ImageView) workingDialog.findViewById(R.id.background);

	     Launchable app = launchManager.find(mReturnUri.toString()); 

	     Bitmap splash = null;
	     if (app != null) {
		 splash = app.getSplash();
	     } else {
		 Log.e(LT, "Couldn't find splash screen for: " + mReturnUri.toString());
	     }

	     if (splash == null) {
		splash = BitmapFactory.decodeResource(getResources(), R.drawable.background);
	     }
	     imageView.setImageBitmap(splash);
	     Log.e(LT, "Showing Dialog");
	     workingDialog.show();
	     
	     if (null != commandName)  {
		 dispatch(commandName, commandParts, commandParams);
		 }

	 } else {
	     Log.e(LT, "Dismissing Dialog");
	     workingDialog.dismiss();
	     //Log.e(LT, "Content view main");
	     //setContentView(R.layout.main);
	 }
     }

     public void dispatch(String command, Map env, Map params)  {
	 Log.d(LT, "dispatch " + command);
	 mCurrentjsessionid  = (String) env.get("JSESSIONID");
	 String postUriString = mPOSTUri.toString();

	 if (null != mCurrentjsessionid)  {
	     utilInterface.setCookie(postUriString,
		     "JSESSIONID=" +
		     mCurrentjsessionid);
	 }

	 if ("camera".equals(command))  {
	     String path = mCameraInterface
		 .shootPhoto(mCurrentId, "");
	     mCurrentMediaFile = path;
	     Log.d(LT, "dispatched camera " + path);
	 } else if ("camcorder".equals(command))  {
	     String path = mVideoInterface
		 .shootVideo(mCurrentId, "");
	     mCurrentMediaFile = path;
	     Log.d(LT, "dispatched camcorder " + path);
	 } else if ("fetchContacts".equals(command))  {
	     mContactListInterface
		 .fetchContact(mCurrentId, packParams(params));
	 } else if ("microphone".equals(command))  {
	     String path = mAudioInterface
		 .recordAudio(mCurrentId);
	     mCurrentMediaFile = path;
	     Log.d(LT, "dispatched microphone " + path);
	 } else if ("aug".equals(command))  {
	     Log.d(LT, "checking augmented reality view option " + params.get("v"));
	     if ("vuforia".equals(params.get("v")))  {
		 //will need to implement wrappers to support container invocation
		 Log.d(LT, "using Class.forName to load AR Marker view");
		 try {
		     Intent arIntent = new Intent(getApplicationContext(),
			     Class.forName("com.qualcomm.QCARSamples.FrameMarkers.FrameMarkers"));
		     arIntent.putExtra(mCurrentId, ARMVIEW_CODE);
		     arIntent.putExtra("attributes", packParams(params));
		     startActivityForResult(arIntent, ARMVIEW_CODE);
		 } catch (Exception e)  {
 Log.e(LT, "Augmented Reality marker view not available ", e);
		     returnToBrowser();
		 }
            } else {
                mARViewInterface
                    .arView(mCurrentId, packParams(params));
	    Log.d(LT, "dispatched augmented reality " + packParams(params));
            }

        } else if ("scan".equals(command))  {
            mCaptureInterface
                .scan(mCurrentId, packParams(params));
	    Log.d(LT, "dispatched qr scan " + packParams(params));
        } else if ("register".equals(command))  {
	    Log.d(LT, "dispatched register ");
	    if (INCLUDE_GCM) {
		//if GCM registration does not occur in 3 seconds,
		//proceed with registration anyway
		mHandler.postDelayed(new Runnable() {
			public void run()  {
			    if (doRegister)  {
				registerSX();
			    }
			}
		    }, 3000);
		doRegister = true;
	    } else {
		// No GCM so proceed with registration;
		registerSX();
	    }
        }
    }

    private void registerSX() {
	String postUriString = mPOSTUri.toString();
	doRegister = false;
	String encodedForm = "";
	String cloudNotificationId = getCloudNotificationId();
	if (null != cloudNotificationId)  {
	    encodedForm = "hidden-iceCloudPushId=" +
		URLEncoder.encode(cloudNotificationId);
	}
	Log.d(LT, "POSTing to " + postUriString);
	Log.d(LT, "POST to register will send " + encodedForm);
	utilInterface.setUrl(postUriString);
	utilInterface.submitForm("", encodedForm, mBrowserReturn);
	Log.d(LT, "POST to register URL with jsessionid " + mCurrentjsessionid);
    }

    public String packParams(Map params)  {
        StringBuilder result = new StringBuilder();
        String separator = "";
        for (Object key : params.keySet())  {
            result.append(separator);
            result.append(String.valueOf(key));
            result.append("=");
            result.append(String.valueOf(params.get(key)));
            separator = "&";
        }
        return result.toString();
    }

    public String encodeMedia(String id, String path)  {
        return "file-" + id + "=" + URLEncoder.encode(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        //better to store return data in the intent than keep member
        //fields on this class

        String encodedForm = "";
        
        if (null != mParameters)  {
            encodedForm = mParameters + "&";
        }

	Log.d(LT, "onActivityResult: request = " + requestCode + ", result = " + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO_CODE:
		    Log.d(LT, "onActivityResult will POST to " + mPOSTUri);
                    mCameraHandler.gotPhoto();
                    encodedForm += encodeMedia(mCurrentId,
                            mCurrentMediaFile);
                    utilInterface.setUrl(mPOSTUri.toString());
                    utilInterface.submitForm("", encodedForm, mBrowserReturn);
		    Log.d(LT, "onActivityResult completed TAKE_PHOTO_CODE");
                    break;
                case TAKE_VIDEO_CODE:
                    mVideoHandler.gotVideo(data);
                    encodedForm += encodeMedia(mCurrentId,
                            mCurrentMediaFile);
                    utilInterface.setUrl(mPOSTUri.toString());
                    utilInterface.submitForm("", encodedForm, mBrowserReturn);
		    Log.d(LT, "onActivityResult completed TAKE_VIDEO_CODE");
                    break;
                case SCAN_CODE:
                    String scanResult = data.getStringExtra(Intents.Scan.RESULT);

                    encodedForm += "hidden-" + mCurrentId + "=" +
                            URLEncoder.encode(scanResult);
                    utilInterface.setUrl(mPOSTUri.toString());
                    utilInterface.submitForm("", encodedForm, mBrowserReturn);
                    break;
                case RECORD_CODE:
                    mAudioRecorder.gotAudio(data);
                    encodedForm += encodeMedia(mCurrentId,
                            mCurrentMediaFile);
                    utilInterface.setUrl(mPOSTUri.toString());
                    utilInterface.submitForm("", encodedForm, mBrowserReturn);
		    Log.d(LT, "onActivityResult completed RECORD_CODE");
                    break;
                case ARVIEW_CODE:
		    Log.d(LT, "onActivityResult completed ARVIEW_CODE");
                    returnToBrowser();
                    break;
                case ARMVIEW_CODE:
//		mARViewHandler.arViewComplete(data);
		    Log.d(LT, "completed AR Marker View");
                    returnToBrowser();
                    break;
	        case ADD_LAUNCHABLE:
		    try {
			Launchable newApp = new Launchable(data.getStringExtra("name"),
							   data.getStringExtra("url"),
							   data.getStringExtra("suffix"),
							   data.getStringExtra("splash"),
							   data.getStringExtra("icon"));
			launchManager.add(newApp);
			launchManager.save();
		    } catch (MalformedURLException e) {
			Log.e(LT, "BAD URL: Could not add application");
		    }

		    break;
            }
        } else {
	    //	    returnToBrowser();
	}
    }

    @Override
	protected void onResume() {
	Log.d(LT, "Resuming");
        super.onResume();
	if (stopped) {
	    stopped = false;
	    //progressDialog.show( self, "ICEmobile-SX", "is working...",false, true);
	}
	// Clear any existing C2DM notifications;
	if (pendingCloudPush) {
	    pendingCloudPush = false;
	    mC2dmHandler.clearPendingNotification();
	}
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAudioPlayer.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sx_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.add_launchable:
		startActivityForResult(new Intent(this,AddLaunchable.class),ADD_LAUNCHABLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void handleC2dmRegistration(String id) {
	Log.d(LT, "handleC2dmRegistration " + id);
        if (doRegister)  {
	    registerSX();
        }
    }

    public void handleC2dmNotification() {
	Log.d(LT, "handleC2dmNotification ");
        pendingCloudPush = true;
    }

    public void submitProgress(int progress) {
    }

    protected String getCloudNotificationId() {
        String id = null;
        if (mC2dmHandler != null) {
            id = mC2dmHandler.getRegistrationId();
        }
        return id;
    }

    private void includeUtil() {
        utilInterface = new UtilInterface(this, null, "");
    }

    private void includeCamera() {
        mCameraHandler = new CameraHandler(this, null, utilInterface, TAKE_PHOTO_CODE);
        mCameraInterface = new CameraInterface(mCameraHandler);
    }

    private void includeContacts() {
        mContactListInterface = new ContactListInterface(utilInterface, this, getContentResolver());
        mContactListInterface.setCompletionCallback(new Runnable() {
                public void run()  {
                    String encodedContact = 
                            mContactListInterface.getEncodedContactList();
                    String encodedForm = "hidden-" + mCurrentId + "=" + encodedContact;
                    if (null != mParameters)  {
                        encodedForm += "&" + mParameters;
                    }
                    utilInterface.setUrl(mPOSTUri.toString());
                    utilInterface.submitForm("", encodedForm);
		    Log.d(LT, "completionCallback completed fetchContacts:" + encodedContact);
                    returnToBrowser();
                }
            });
    }

    private void includeARView() {
        mARViewHandler = new ARViewHandler(this, null, utilInterface, ARVIEW_CODE);
        mARViewInterface = new ARViewInterface(mARViewHandler);
    }

    private void includeARMarkers() {
//        mARMarkersHandler = new ARMarkersHandler(this, mWebView, utilInterface, ARVIEW_CODE);
//        mARMarkersInterface = new ARViewInterface(mARViewHandler);
    }

    private void includeQRCode() {
        mCaptureInterface = new CaptureJSInterface(this, SCAN_CODE, SCAN_ID);
    }

    private void includeVideo() {
        mVideoHandler = new VideoHandler(this, null, utilInterface, TAKE_VIDEO_CODE);
        mVideoInterface = new VideoInterface(mVideoHandler);
    }

    private void includeAudio() {
        mAudioRecorder = new AudioRecorder(this, utilInterface, RECORD_CODE);
        mAudioPlayer = new AudioPlayer();
        mAudioInterface = new AudioInterface(mAudioRecorder, mAudioPlayer);
    }
    
    private void includeC2dm() {
        if (mC2dmHandler == null) {
            mC2dmHandler = new C2dmHandler(this, R.drawable.c2dm_icon, "ICE", "ICEmobile", "GCM Notification", this);
        }
        mC2dmHandler.start(GCM_SENDER);
    }

    public class LaunchableAdapter extends BaseAdapter {
	private Context mContext;
	private LaunchManager manager;

	public LaunchableAdapter(Context c, LaunchManager manager) {
	    this.manager = manager;
	    mContext = c;
	}

	public int getCount() {
	    return manager.size();
	}

	public Object getItem(int position) {
	    return null;
	}

	public long getItemId(int position) {
	    return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) mContext
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
	    View gridView;
	    if (convertView == null) {  // if it's not recycled, initialize some attributes
		gridView = new View(mContext);
		gridView = inflater.inflate(R.layout.grid_item, null);
	    } else {
		gridView = convertView;
	    }

	    TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
	    ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);
	    textView.setText(manager.get(position).getName());
	    Bitmap icon = manager.get(position).getIcon();
	    if (icon == null) {
		icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon);
	    }
	    imageView.setImageBitmap(icon);
	    return gridView;
	}
    }
}
