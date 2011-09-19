/*
 * Copyright (c) 2011, ICEsoft Technologies Canada Corp.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */ 
package org.icemobile.client.blackberry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.InputConnection;
import javax.microedition.io.file.FileSystemRegistry;

import org.icemobile.client.blackberry.options.BlackberryOptionsProperties;
import org.icemobile.client.blackberry.options.BlackberryOptionsProvider;
import org.icemobile.client.blackberry.push.PushAgent;
import org.icemobile.client.blackberry.push.PushServiceListener;
import org.icemobile.client.blackberry.script.audio.AudioPlayback;
import org.icemobile.client.blackberry.script.audio.AudioRecorder;
import org.icemobile.client.blackberry.script.camera.VideoController;
import org.icemobile.client.blackberry.script.camera.WidgetCameraController;
import org.icemobile.client.blackberry.script.test.ScriptableTest;
import org.icemobile.client.blackberry.script.upload.AjaxUpload;
import org.icemobile.client.blackberry.script.upload.ScriptResultReader;
import org.icemobile.client.blackberry.utils.HistoryManager;
import org.icemobile.client.blackberry.utils.ResultHolder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.rim.blackberry.api.options.OptionsManager;
import net.rim.device.api.browser.field.RenderingOptions;
import net.rim.device.api.browser.field.RenderingSession;
import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldConfig;
import net.rim.device.api.browser.field2.BrowserFieldConnectionManager;
import net.rim.device.api.browser.field2.BrowserFieldCookieManager;
import net.rim.device.api.browser.field2.BrowserFieldListener;
import net.rim.device.api.browser.field2.BrowserFieldRequest;
import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.io.http.HttpHeaders;
import net.rim.device.api.script.ScriptEngine;
import net.rim.device.api.script.Scriptable;
import net.rim.device.api.script.ScriptableFunction;
import net.rim.device.api.system.ApplicationManager;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.EventLogger;
import net.rim.device.api.system.SystemListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

import net.rim.blackberry.api.messagelist.*; 

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class ICEmobileContainer extends UiApplication implements SystemListener {

    // Default, compile time, initial page. This value is passed to BlackberryOptionsProperties
    // during construction to be used if the Properties object is new
    public static final String HOME_URL = "http://www.icemobile.org/demos.html";

    public static final int HISTORY_SIZE = 10; 
    
    // Some startup branding options
    private static final String LOADING_IMAGE = "ICEfaces-360x480.gif";

    // Application GUID for logging; In Eclipse, type a string, select it and right click->"Convert String to Long"
    // to create a unique GUID.
    public static final long GUID = 0xb8aec4694336218fL; 

    // Essential Webkit browser member variables 
    private BrowserField mBrowserField;
    private ScriptEngine mScriptEngine;
    private BrowserFieldCookieManager mBrowserCookieManager;
    private String mInitialScript;
    private RenderingSession mRenderingSession;

    // Blackberry Options variables 
    private BlackberryOptionsProperties mOptionsProperties; 
    private BlackberryOptionsProvider mOptionsProvider; 

    // The ScriptExtension points 
    private Scriptable mCameraController;
    private Scriptable mAudioRecorder; 
    private Scriptable mAudioPlayer;
    private Scriptable mAjaxUpload;
    private Scriptable mResultReader; 
    private Scriptable mVideoController; 

    // load screen variables 
    private MainScreen mMainScreen;
    private EncodedImage mLoadingImage;
    
    // Load screen vars
    private BitmapField mLoadingField = new BitmapField();
    private MainScreen mLoadingScreen; 

    // Hashtable of pending responses for asynchronous update
    private Hashtable mPendingResponses = new Hashtable();

    // if true, the application will reload the page on Container activation. 
    // Set from the options menu. 
    private boolean mReloadOnActivate; 
    private HistoryManager mHistoryManager; 

    // RIM public infrastructure agent
    //private PushAgent mPushAgent;

//    private PushServiceListener mPushServiceListener;
    private static ApplicationIndicator mAppIndicator; 
    
    // Keeping track of the home page, and 
    private String mCurrentHome;
    private String mCurrentPage; 
    
    // Keeping track of page load times
    private String mCurrentlyLoadingDocument; 
    private long mDocumentStartTime;

    private boolean mApplicationPaused;

//    private String mResumeScript = "ice.push.resumeBlockingConnection();"; 
    private String mParkScript = "ice.push.parkInactivePushIds('bpns:" + 
                                    Integer.toHexString(DeviceInfo.getDeviceId()).toUpperCase()
                                    + "');"; 


    /**
     * Main Entry point
     */
    public static void main(String[] args) {
        
        ICEmobileContainer iceMobile = new ICEmobileContainer();
        // if system startup is still in progress when this application is run 
        if (ApplicationManager.getApplicationManager().inStartup()) { 
            iceMobile.addSystemListener( iceMobile );
        } else { 
            iceMobile.eulaCheck();
        }
        
        iceMobile.enterEventDispatcher();
    }

    public ICEmobileContainer() {      
        
        EventLogger.clearLog();
        EventLogger.register(GUID, "ICE", EventLogger.VIEWER_STRING);
        EventLogger.setMinimumLevel( EventLogger.DEBUG_INFO );
        optionsChanged();     
    }

        
    /**
     * System callback method for powerup. This indicates device is up.  
     */
    public void powerUp() { 
        
        removeSystemListener( this ); 
        eulaCheck();        
    }
    
    /**
     * Check if the eula has been read. If so go on to init(). 
     * Otherwise let the EulaManager show the screen. By convention 
     * the init() method will be called from the 'ok' button to 
     * guarantee the eula has been ok'd. 
     */
    private void eulaCheck() {
        
        if (!mOptionsProperties.isEulaViewed()) { 
            DEBUG("Launching eula viewer");
            EulaManager em = new EulaManager(this); 
            em.show();            
        } else { 
            init(); 
        }
    }
    

    /**
     * Initialize the Container. This method initializes the BrowserField settings for future operation 
     * and loads the option system to setup the correct start page and settings.  This method should 
     * be called from the EventThread once the device is initialized after the eulaCheck has been 
     * performed. 
     * 
     * @param url 
     */
    void init() {

        mHistoryManager = new HistoryManager( HISTORY_SIZE );           

        EventLogger.clearLog();
        EventLogger.register(GUID, "ICE", EventLogger.VIEWER_STRING);
        EventLogger.setMinimumLevel( EventLogger.DEBUG_INFO );

        try { 

            mMainScreen = new ApplicationScreen(this);
            pushScreen(mMainScreen);

            enumerateStorageLocations();

            // Add loading screen and display ASAP
            mLoadingImage = EncodedImage.getEncodedImageResource( LOADING_IMAGE );

            if (mLoadingImage != null) {
                // If a loading image exists, add it to the loading field and push it onto the screen stack.
                mLoadingField.setImage(mLoadingImage);
                mLoadingScreen = new MainScreen();
                mLoadingScreen.add(mLoadingField);
                pushScreen(mLoadingScreen);
            }

            mInitialScript = readLocalFileSystem (getClass(), "blackberry-interface.js");

            // This should put an error screen on the page. No sense continuing if the javascript isn't found
            if (mInitialScript == null) { 
                ERROR( "interface.js NOT found");
                return; 
            } else { 
                DEBUG("local js - length: " + mInitialScript.length());
            }


            // Set up the browser/renderer.
            mRenderingSession = RenderingSession.getNewInstance();
            mRenderingSession.getRenderingOptions().setProperty(RenderingOptions.CORE_OPTIONS_GUID, RenderingOptions.JAVASCRIPT_ENABLED, true);
            mRenderingSession.getRenderingOptions().setProperty(RenderingOptions.CORE_OPTIONS_GUID, RenderingOptions.JAVASCRIPT_LOCATION_ENABLED, true);
            // Enable nice-looking BlackBerry browser field.
            mRenderingSession.getRenderingOptions().setProperty(RenderingOptions.CORE_OPTIONS_GUID, 17000, true);			

            mOptionsProvider = new BlackberryOptionsProvider( this );
            OptionsManager.registerOptionsProvider( mOptionsProvider );


            // Perform BrowserField configuration 
            BrowserFieldConfig bfc = new BrowserFieldConfig(); 
            bfc.setProperty(BrowserFieldConfig.ENABLE_COOKIES, Boolean.TRUE);
            bfc.setProperty(BrowserFieldConfig.ERROR_HANDLER, new FieldErrorHandler( mBrowserField ) );
            bfc.setProperty(BrowserFieldConfig.ALLOW_CS_XHR, Boolean.TRUE);

            mBrowserField = new BrowserField( bfc );
            mMainScreen.add(mBrowserField);	


            // Push registration will be performed here via one of two 
            // mechanisms
            //            setupPushListener();
            //mPushAgent = new PushAgent();


            ApplicationIndicatorRegistry reg = ApplicationIndicatorRegistry.getInstance(); 
            EncodedImage image = EncodedImage.getEncodedImageResource("icebox-32x32.png"); 
            ApplicationIcon icon  = new ApplicationIcon( image ); 

            ApplicationIndicator indicator = reg.register(icon, true, false );
            mAppIndicator = reg.getApplicationIndicator(); 
            //			mAppIndicator.set ( )

            mBrowserCookieManager = mBrowserField.getCookieManager();			
            mBrowserCookieManager.setCookie( mCurrentHome , "com.icesoft.user-agent=HyperBrowser/1.0");		



            mBrowserField.addListener(new BrowserFieldListener() { 
                public void documentLoaded( BrowserField field, Document document) { 

                    try { 
                        // DEBUG("DocumentLoaded: " + document.getBaseURI() );

                        final String uri = document.getBaseURI();
                        if (uri.indexOf("about:blank") == -1 ) { 		
                            
                            if (uri != null && uri.equals(mCurrentlyLoadingDocument)) { 
                                TIME(mDocumentStartTime, "iceMobile loading page: " + uri );
                            }
                            
                            mScriptEngine = mBrowserField.getScriptEngine();
                            mBrowserCookieManager.setCookie( uri , "com.icesoft.user-agent=HyperBrowser/1.0");//	

                            if (mLoadingScreen != null) { 
                                popScreen(mLoadingScreen); 
                                mLoadingScreen = null;
                            }

                            if (mScriptEngine != null ) {

                                invokeLater(new Runnable() {
                                    public void run() { 
                                        try {
                                            Document document = mBrowserField.getDocument();
                                            Element scriptElem = document.createElement("script");
                                            scriptElem.setTextContent(mInitialScript);
                                            //append to head
                                            document.getDocumentElement().getFirstChild().appendChild(scriptElem);
                                            ScriptableFunction myTestFunction = new ScriptableTest(null);
                                            mScriptEngine.addExtension("icefaces.shootPhoto", mCameraController );
                                            //											mScriptEngine.addExtension("icefaces.uploadPhoto", oldUploadPhoto );
                                            mScriptEngine.addExtension("icefaces.test", myTestFunction );
                                            mScriptEngine.addExtension("icefaces.toggleMic", mAudioRecorder);
                                            mScriptEngine.addExtension("icefaces.playAudio", mAudioPlayer);
                                            mScriptEngine.addExtension("icefaces.ajaxUpload", mAjaxUpload);
                                            mScriptEngine.addExtension("icefaces.getResult", mResultReader);
                                            mScriptEngine.addExtension("icefaces.shootVideo", mVideoController);
                                            DEBUG("ICEmobile - native script executed");

                                        } catch (Throwable t) {
                                            ERROR("ICEmobile - Error executing Blackberry-interface.js: " + t + ", document: " + uri);
                                        }
                                    } 
                                });
                            }

                            mCurrentPage = uri;
                            mHistoryManager.addLocation(uri);
                            ((ApplicationScreen)mMainScreen).updateHistoryMenus();

                        } 
                    } catch (Exception e) { 
                        ERROR("Exception in DocumentLoad: " + e);
                    }
                } 



                // public void documentAborted( BrowserField field, Document document ) { 
                //					
                // }
                //// Only change the script engine when a document is loaded.
                public void documentCreated ( BrowserField field, ScriptEngine scriptEngine, Document document) { 
                    //	DEBUG("Document created: " + document.getBaseURI());	
                    mCurrentlyLoadingDocument = document.getBaseURI(); 
                    mDocumentStartTime = System.currentTimeMillis();
                }
                //				
                public void documentUnloading (BrowserField field, Document document) {
                    DEBUG("Document unloading??? ");
                }
                //				
                //  public void downloadProgress (BrowserField field, ContentReadEvent readEvent) {					
                //      System.out.println("--------------------------Downloaded: " + readEvent.getItemsRead() + " of " + readEvent.getItemsToRead() );			
                //				
                //  }
                //				 
                //				
            }); 				


            instantiateScriptExtensions();
            loadPage( mCurrentHome );


        } catch (Throwable e) { 
            ERROR ("Error loading initial page: " + e);
            Dialog.alert("ICEmobile - exception during init: " + e);
        }
    }

    /**
     * Construct the custom script extensions based on capabilities flags.  
     */
    private void instantiateScriptExtensions() {
        mAjaxUpload = new AjaxUpload(this );
        mCameraController = new WidgetCameraController(this);
        mAudioRecorder = new AudioRecorder(this);
        mAudioPlayer = new AudioPlayback(this);
        mResultReader = new ScriptResultReader( this );			
        mVideoController = new VideoController( this );
    }

    /**
     * Log a list of the device capabilities and define the File System capabilities 
     * in the FileUtils class. 
     */
    private void enumerateStorageLocations() {
        String root = null; 
        Enumeration i = FileSystemRegistry.listRoots(); 
        while (i.hasMoreElements()) { 

            root = (String) i.nextElement(); 
            DEBUG("File device: " + root);	

        }
    }

 
    public HistoryManager getHistoryManager() { 
        return mHistoryManager;
    }
    
    /**
     * Back function 
     */
    public void back() { 
    	mBrowserField.back(); 
    }
   
 
    // ----------------   Asynchronous callback methods for javascript extensions to manipulate the DOM. 

    public void resetPushAgent() { 

       //mPushAgent.shutdown(); 
       // mPushAgent = new PushAgent();
    }


    /**
     * Post a request to the server on behalf of javascript extensions. The BrowserField used 
     * to load the intitial request must be used for all subsequent requests in order to maintain 
     * the correct cookie setup. 
     */
    public InputConnection postRequest( String url, String request, HttpHeaders headers) throws Exception { 

        BrowserFieldConnectionManager bfconman = mBrowserField.getConnectionManager();
        BrowserFieldRequest bfr = new BrowserFieldRequest(url, request.getBytes(), headers);
        return bfconman.makeRequest(bfr);
    }

    public String getCurrentURL() { 		
        return mBrowserField.getDocumentUrl();		
    }

    public static void showNotificationIcon(boolean show) { 
    	mAppIndicator.setVisible(show);
    }


    /**
     * Insert a filename hidden field before a given id. It's necessary to do this in the event there 
     * are more than one file inserting components on the page. Currently, it's the javascript that adds 
     * the '-file' to the id to add it to the form. 
     * 
     * @param id The id of an element to insert the filename before
     * @param filename the name of the file in the local filesystem. 
     */
    public void insertHiddenFilenameScript(final String id, final String filename) {

        if (filename != null && filename.length() > 0) { 

            String updateScript = "ice.addHiddenField('" + id + "' , '" + filename + "');";			
            try { 				

                mScriptEngine.executeScript(updateScript, null);
                DEBUG("ICEmobile - added field to DOM: " + filename);

            } catch (Throwable e) {							
                ERROR ("Error adding hidden field: " + e );
            }
        } else { 
            ERROR("Captured filename is invalid " );
        }			
    }

    /**
     * Handle the result of a POST in an AJAX context. The holder of the result
     *  
     * @param resultKey The key into the result hash 
     */
    public void processResult ( String resultKey, ResultHolder holder ) { 

        mPendingResponses.put(resultKey, holder);
        String updateScript = "ice.handleResponse(icefaces.getResult('" + resultKey + "') ); ";
        DEBUG("Executing update script: " + resultKey + ", holder? " + (holder != null));
        try { 
            if (mScriptEngine != null) { 
                mScriptEngine.executeScript(updateScript, null);
                DEBUG("ICEmobile - Ajax response handled ok");
            } else { 
                ERROR("ICEmobile Null ScriptEngine handling Ajax Response?");
            }

        } catch (Throwable t) {

            if (t.getMessage().indexOf("DOM" ) == -1) {  
                ERROR( "ICEmobile - Error handling AJAX response: "  + t );
            } else { 
                ERROR("DOM exception still occurs");
            }
        }
    }

    public ResultHolder getPendingResponse( String responseKey) { 
        ResultHolder value = (ResultHolder)mPendingResponses.get( responseKey ); 
        mPendingResponses.remove( responseKey );
        return value;
    }

    /**
     * Development level script for re-executing blackberry-interface.js
     */
    public void rerunScriptImmediate() { 

        try { 
            mScriptEngine = mBrowserField.getScriptEngine();
        } catch (Throwable t) { 
            ERROR("Exception fetching engine: " + t);
        }

        if (mScriptEngine != null) { 
            if (mInitialScript != null) { 

                try { 

                    mScriptEngine.executeScript(mInitialScript, null ); 
                    DEBUG("Rerun script - ok");
                } catch (Throwable t) { 
                    ERROR("Rerun script - error: " + t); 
                }
            } else { 
                ERROR("Rerun script - Initial script is null");
            }

        } else { 
            DEBUG("Rerun script - Script engine is null?");
        }
    }

    /**
     * call into javascript to define an image thumbnail
     * @param id Id of the image source to update. 
     * @param base64ImageBytes
     */
    public void insertThumbnail( String id, String base64ImageBytes ) { 

        if (id != null && id.length() > 0) {

            String thumbId = id + "-thumb";

            String updateScript = "ice.setThumbnail('" + thumbId + "' , 'data:image/jpg;base64," + 
            base64ImageBytes + "');";

            try { 
                mScriptEngine.executeScript(updateScript, null);

            } catch (Throwable e) {							
                ERROR ("Insert-thumb - exception: " + e);
            }
        } else { 
            ERROR("Insert-thumb - invalid id: " + id);
        }				
    }

    /**
     * Load an URL. Page will be loaded in the event thread
     * 
     * @param url the page to load. 
     */
    public void loadPage(final String url) { 

        if (checkNetworkAvailability()) { 
            UiApplication.getUiApplication().invokeLater( new Runnable() {
                public void run() { 
                    mBrowserField.requestContent( url );
                }
            });		
        } 
    }	

    /**
     * Suitable for the options page where we have exited the app and want 
     * to reload when we rejoin. 
     */
    public void reloadApplication() { 
        mReloadOnActivate = true; 
    }
    
    /** 
     * Callback for options change. DO NOTHING LONG HERE. 
     */
    public void optionsChanged() { 
    	
    	mOptionsProperties = BlackberryOptionsProperties.fetch();
        mCurrentHome = mOptionsProperties.getHomeURL();
    }

    /**
     * Suitable for the Menu system when we want to reload the page 
     * immediately from the application
     */
    public void reloadApplicationImmediately() { 
        loadPage ( mOptionsProperties.getHomeURL() );
    }
    
    /**
     * Reload the current page. 
     */
    public void reloadCurrentPage() { 
        loadPage( mCurrentPage );
    }

    /**
     * Allow a borkened demo system to reset the audio recorder structure.
     */
    public void resetAudioSystem() { 
        ((AudioRecorder)mAudioRecorder).resetAudioState();
    }


    /**
     * This method will resume the ajax push interaction through the bridge and 
     * unpark the application pushId on the server. At this time, we haven't a way 
     * to park the pushId on deactivate, so this isn't strictly necessary.
     */
    public void resumeICEPush() { 

        try { 				
            if (mApplicationPaused && mScriptEngine != null) { 
                //mScriptEngine.executeScript(RESUME_SCRIPT, null);
                mApplicationPaused = false; 
            } 

        } catch (Throwable e) {							
            ERROR("Error Resuming ICEPush: " + e );
        }		
    }

    public void pauseICEPush() { 
        try { 				
            if (mScriptEngine != null && mParkScript != null) {
            	DEBUG("ICEmobile - park script: " + mParkScript);
            	mBrowserField.executeScript( mParkScript );
//                mScriptEngine.executeScript(mParkScript, null);
                mApplicationPaused = true; 
            }
        } catch (final Throwable e) {							
            ERROR("Error Pausing ICEPush: " + e);
//            UiApplication.getUiApplication().invokeLater( new Runnable() {
//            	public void run() { 
//            		Dialog.alert("Exception in park: " + e); 
//            	}
//            });             
        }		
    }

    /**
     * UIApplication activate override. 
     */
    public void activate() { 

        // method gets called several times in contravention of docs on activate();
        if (isForeground() ) { 

            if (mAppIndicator != null) { 
                showNotificationIcon(false);
            } 
            // resumeICEPush();
            if (mReloadOnActivate && checkNetworkAvailability()) { 
                loadPage ( mCurrentHome );
                mReloadOnActivate = false; 
            }
        } 
    }

    public void deactivate() { 		
        pauseICEPush();
    }


    // -------------------  Utility methods ---------------------------------


    /**
     * fetch the contents of a file as a resource
     * @param name name of resource
     * @return Contents of file 
     */
    public String readLocalFileSystem ( Class clazz, String name) { 

        InputStream rStream = null;
        String returnVal = null;
        try { 
            rStream = clazz.getResourceAsStream(name); 

            if (rStream != null) { 
                byte data[] = IOUtilities.streamToBytes(rStream); 
                returnVal = new String(data); 
            }
        } catch (IOException ioe) { 
            ICEmobileContainer.ERROR("Exception reading resource: " + ioe);
        } finally { 
        
            try { 
                if (rStream != null) { 
                    rStream.close();
                }
            } catch (Exception e) {}
        }       
        return returnVal;
    } 
    
    /**
     * 
     */
    void accept() { 
        mOptionsProperties.setEulaViewed(true); 
        mOptionsProperties.save();
    }
    
    /**
     * Check network Availability. Returns false if none of the available transports 
     * show sufficient coverage to operate. Should be checked before attempting 
     * to load pages, to prevent an endless black screen. 
     */
    private boolean checkNetworkAvailability() { 

        if ( (!CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_DIRECT )) && 
                (!CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_BIS_B)) && 
                (!CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS) )) {

            invokeLater(new Runnable() {
                
                public void run() { 
                    Dialog.alert("Network unavailable");
                }
            });
            return false; 
        } 
        return true;
    }

    
    

    // ----------------------- Convenience logging methods

    public static void DEBUG( String message) { 
        EventLogger.logEvent(GUID, message.getBytes(), EventLogger.DEBUG_INFO);
    }

    public static void ERROR( String message) { 
        EventLogger.logEvent(GUID, message.getBytes(), EventLogger.ERROR);
    }	

    public static void WARN(String message) { 
        EventLogger.logEvent(GUID, message.getBytes(), EventLogger.WARNING);
    }

    public static void TIME( long startTime, String message) { 
        DEBUG( message + " took: " + (System.currentTimeMillis() - startTime) + " ms"); 
    }
    
    
    
    // -------------------- System event methods ----------------------------
    
    public void batteryGood() {
        // TODO Auto-generated method stub
        
    }

    public void batteryLow() {
        // TODO Auto-generated method stub
        
    }

    public void batteryStatusChange(int arg0) {
        // TODO Auto-generated method stub
        
    }

    public void powerOff() {
        // TODO Auto-generated method stub    
    }
}

