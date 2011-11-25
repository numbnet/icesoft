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
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.io.InputConnection;
import javax.microedition.io.file.FileSystemRegistry;

import org.icemobile.client.blackberry.options.BlackberryOptionsProperties;
import org.icemobile.client.blackberry.options.BlackberryOptionsProvider;
import org.icemobile.client.blackberry.push.PushAgent;
import org.icemobile.client.blackberry.script.audio.AudioPlayback;
import org.icemobile.client.blackberry.script.audio.AudioRecorder;
import org.icemobile.client.blackberry.script.camera.VideoController;
import org.icemobile.client.blackberry.script.camera.WidgetCameraController;
import org.icemobile.client.blackberry.script.debug.JavascriptDebugger;
import org.icemobile.client.blackberry.script.scan.QRCodeScanner;
import org.icemobile.client.blackberry.script.test.ScriptableTest;
import org.icemobile.client.blackberry.script.upload.AjaxUpload;
import org.icemobile.client.blackberry.script.upload.ScriptResultReader;
import org.icemobile.client.blackberry.utils.HistoryManager;
import org.icemobile.client.blackberry.utils.ResultHolder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
    // Last used version -> "ICEmobileContainer 1.0 Beta" 
    public static final long GUID = 0xd48f0ea85b8c7e00L; 

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
    private Scriptable mQRCodeScanner; 
    private Scriptable mJavascriptLogger; 

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

    // RIM public infrastructure PUSH agent
    private PushAgent mPushAgent;

//    private PushServiceListener mPushServiceListener;
    private static ApplicationIndicator mAppIndicator; 
    
    // Keeping track of the home page, and 
    private String mCurrentHome;
    private String mCurrentPage; 
    
    // Keeping track of page load times
    private String mCurrentlyLoadingDocument; 
    private long mDocumentStartTime;
    
    private boolean mRealDevice  = !DeviceInfo.isSimulator(); 
    
    private Timer mJavascriptProbe = new Timer();
    
    private static long mStartTime;
    
    
    private Runnable mParkScriptRunner = new Runnable() { 
        public void run() {
        	
        	try {   
        		if (mParkScript != null && mRealDevice ) {
        			mScriptEngine.executeScript(mParkScript, null);
        			TRACE( System.currentTimeMillis(),  "ICEmobile - parkScript success" );
//        			mJavascriptProbe.cancel();
        		}
        	} catch (Throwable t) {
        		TRACE(System.currentTimeMillis(), "ICEmobile - Park script: " + mParkScript + ",  failed");
        		DEBUG("ICEmobile - Exception is: " + t);
        	} 
        }
    }; 

    private String mParkScript;  
    private String mPauseScript; 


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
            DEBUG("ICEmobile - Launching eula viewer");
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

    	mStartTime = System.currentTimeMillis();
        mHistoryManager = new HistoryManager( HISTORY_SIZE );           

        EventLogger.clearLog();
        EventLogger.register(GUID, "ICE", EventLogger.VIEWER_STRING);
        EventLogger.setMinimumLevel( EventLogger.DEBUG_INFO  );

        try { 

            mMainScreen = new ApplicationScreen(this);
            pushScreen(mMainScreen);

            //enumerateStorageLocations();

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
            mPushAgent = new PushAgent();

            ApplicationIndicatorRegistry reg = ApplicationIndicatorRegistry.getInstance(); 
            EncodedImage image = EncodedImage.getEncodedImageResource("icemobile-icon-32x32.png"); 
            ApplicationIcon icon  = new ApplicationIcon( image ); 

            ApplicationIndicator indicator = reg.register(icon, true, false );
            mAppIndicator = reg.getApplicationIndicator(); 
            //			mAppIndicator.set ( )

            mBrowserCookieManager = mBrowserField.getCookieManager();			

            mBrowserField.addListener(new BrowserFieldListener() { 
                public void documentLoaded( BrowserField field, Document document) { 

                    try { 
                         TRACE(System.currentTimeMillis(), "DocumentLoaded: " + document.getBaseURI() );

                        final String uri = document.getBaseURI();
                        if (uri.indexOf("about:blank") == -1 ) { 		
                            
                            if (uri != null && uri.equals(mCurrentlyLoadingDocument)) { 
                                TIME(mDocumentStartTime, "ICEmobile loading page: " + uri );
                            }
                            
                            mScriptEngine = mBrowserField.getScriptEngine();
                            mBrowserCookieManager.setCookie(uri, "com.icesoft.user-agent=HyperBrowser/1.0");

                            if (mLoadingScreen != null) { 
                                popScreen(mLoadingScreen); 
                                mLoadingScreen = null;                               
                            }
                            
                            if (mScriptEngine != null ) {

                                invokeLater(new Runnable() {
                                    public void run() { 
                                        try {
                                            
                                            Document document = mBrowserField.getDocument();
//                                            DEBUG("Document creation: uri: " + uri + ", document: " + document);
                                            Element scriptElem = document.createElement("script");
                                            scriptElem.setTextContent(mInitialScript);
                                            //append to head
                                            Element docElement = document.getDocumentElement();
                                            Node child = docElement.getFirstChild();  
                                            while ( child != null && ( !child.getNodeName().equalsIgnoreCase("head"))) { 
                                                child = child.getNextSibling();
                                            }
                                            
                                            if (child != null) { 
//                                                DEBUG("Found true head element at: " + idx);
                                                child.appendChild(scriptElem);
                                            } 
                             
                                            ScriptableFunction myTestFunction = new ScriptableTest(null);
                                            mScriptEngine.addExtension("icefaces.shootPhoto", mCameraController );
                                            //											mScriptEngine.addExtension("icefaces.uploadPhoto", oldUploadPhoto );
                                            mScriptEngine.addExtension("icefaces.test", myTestFunction );
                                            mScriptEngine.addExtension("icefaces.toggleMic", mAudioRecorder);
                                            mScriptEngine.addExtension("icefaces.playAudio", mAudioPlayer);
                                            mScriptEngine.addExtension("icefaces.ajaxUpload", mAjaxUpload);
                                            mScriptEngine.addExtension("icefaces.getResult", mResultReader);
                                            mScriptEngine.addExtension("icefaces.shootVideo", mVideoController);
                                            mScriptEngine.addExtension("icefaces.scan", mQRCodeScanner);
                                            mScriptEngine.addExtension("icefaces.logInContainer", mJavascriptLogger);
                                            DEBUG("ICEmobile - native script executed");
                                            invokeLater (mParkScriptRunner);
                                                

                                        } catch (Throwable t) {
                                            ERROR("ICEmobile - Error executing startup scripts: " + t + ", document: " + uri);
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
//            mJavascriptProbe.scheduleAtFixedRate(javascriptTester, 60000, 20000);
                      


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
        mQRCodeScanner = new QRCodeScanner(this);
        mJavascriptLogger = new JavascriptDebugger(); 
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

        mPushAgent.shutdown(); 
        mPushAgent = new PushAgent();
        DEBUG("ICEmobile - PushAgent reset");
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
    
    /**
     * Get the quickURL. The quick URL is a persisted value used in the URL 
     * navigation menu. If it hasn't been defined, use the current URL. 
     * Can't be kept in the menu, as those are 
     * @param
     */
    public String getQuickURL() { 
        String u = mOptionsProperties.getQuickURL(); 
        if (u == null) { 
            u = getCurrentURL(); 
        }
        return u; 
    }
    
    /**
     * Set the QuickURL. This will persist the value
     * @param url
     */
    public void setQuickURL(String url) { 
        if (url != null && url.trim().length() > 0) { 
            loadPage(url);
            mOptionsProperties.setQuickURL( url ); 
            mOptionsProperties.save();
        } 
    }

    public static void showNotificationIcon(boolean show) { 
    	mAppIndicator.setVisible(show);
    }


    /**
     * Insert a filename hidden field before a given id. It's necessary to do this in the event there 
     * are more than one file inserting components on the page. 
     * 
     * @param id The id of an element to insert the filename before
     * @param filename the name of the file in the local filesystem. 
     */
    public void insertHiddenFilenameScript(final String id, final String filename) {

        if (filename != null && filename.length() > 0) { 

            String idType = id + "-file"; 
            String updateScript = "ice.addHiddenField('" + id + "' , '" + idType + "' , '"+ filename + "');";			
            insertHiddenScript( updateScript );
            DEBUG("ICEmobile - Hidden File field inserted");
        } else { 
            ERROR("ICEmobile - Captured filename is invalid ");
        }			
    }
    
    /**
     * 
     * @param id The id of an element to insert the filename before
     * @param qrResult the scanned result  
     */
    public void insertQRCodeScript(final String id, final String qrResult) {

        if (qrResult != null && qrResult.length() > 0) { 
            String idType = id + "-text"; 
            String updateScript = "ice.addHiddenField('" + id + "' , '" + idType + "' , ' "+ qrResult + "');";         
            insertHiddenScript( updateScript );
            DEBUG("ICEmobile - QRCode text inserted");
            
        } else { 
            ERROR("ICEmobile - Invalid qrCode scan result" );
        }           
    }
    
    /**
     * 
     * @param id The id of an element to insert the filename before
     * @param qrResult the scanned result  
     */
    public void insertHiddenScript(String script) {

        try {               
            mScriptEngine.executeScript(script, null);
        } catch (Throwable e) {                         
            ERROR ("ICEmobile - Error inserting field: " + e );
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
        try { 
            if (mScriptEngine != null) { 
                mScriptEngine.executeScript(updateScript, null);
                DEBUG("ICEmobile - Ajax response handled");
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
                ERROR ("ICEmobile - Exception inserting thumbnail image: " + e);
            }
        } else { 
            ERROR("ICEmobile - Insert thumbnail - invalid id: " + id);
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
                    mBrowserCookieManager.setCookie( url , "com.icesoft.user-agent=HyperBrowser/1.0");
                    mBrowserField.requestContent( url );
                }
            });		
        } else { 
            DIALOG("Network Unavailable");
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
        
        String argument; 
        if (mOptionsProperties.isUsingEmailNotification()) { 
            argument = "('mail:" + 
                mOptionsProperties.getEmailNotification() + "');"; 
        } else { 
            argument = "('bpns:" + 
                Integer.toHexString(DeviceInfo.getDeviceId()).toUpperCase()
                + "');"; 
        }
        
        // Use either an email notification (if desired) or the 
        // RIM push version
        if (mOptionsProperties.isUsingEmailNotification()) { 
            mParkScript  = "if (ice.push) {ice.push.parkInactivePushIds" + argument + "}";      
            mPauseScript  = "if (ice.push) {ice.push.pauseBlockingConnection" + argument + "}";       
        } else { 
            mParkScript  = "if (ice.push) {ice.push.parkInactivePushIds"+ argument +"}";
            mPauseScript = "if (ice.push) {ice.push.pauseBlockingConnection" + argument + "}";
        }
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
     * UIApplication activate override. 
     */
    public void activate() { 

        // method gets called several times in contravention of docs on activate();
        if (isForeground() ) { 
            // turn off the notification indicator if we're revisiting the app. 
            // However, it wont be defined if this is the first time entry. 
            if (mAppIndicator != null) { 
                showNotificationIcon(false);
            } 
        
            if (mReloadOnActivate && checkNetworkAvailability()) { 
                loadPage ( mCurrentHome );
                mReloadOnActivate = false; 
            } 
            
            try { 
                if (mScriptEngine != null && mRealDevice) { 
                    mScriptEngine.executeScript("if (ice.push) { ice.push.resumeBlockingConnection();}" , null);
                    DEBUG("ICEmobile - resumed blocking connection");
                }
            } catch (Throwable t) { 
                ERROR("ICEmobile - resumeScript exception: " + t);
            }
        } 
    }

    public void deactivate() { 		
        
        if (mPauseScript != null && mScriptEngine != null && mRealDevice) {
            try { 
                mScriptEngine.executeScript(mPauseScript, null);
                DEBUG ("ICEmobile - Paused blocking connection");
              
            } catch (Throwable t) { 
                ERROR("ICEmobile - Exception pausing Blocking Connection: " + t);
            }
        }
        
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
     * The eula has been accepted, persist the change. 
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

        return  ( CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_DIRECT ) || 
                CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_BIS_B) || 
                CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS) ); 
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
    public static void TRACE( long traceTime, String message) { 
        DEBUG( message + " at: " + (traceTime - mStartTime) + " ms"); 
    }
    
    public static void DIALOG(final String message) {
        
        UiApplication.getUiApplication().invokeLater(new Runnable() {
            
            public void run() { 
                Dialog.alert(message);
            }
        });
    }
    
    
    // -------------------- System event methods ----------------------------
    
    public void batteryGood() {
        DEBUG("BATTERY GOOD");
        // TODO Auto-generated method stub
        
    }

    public void batteryLow() {
        DEBUG("BATTERY LOW");
        // TODO Auto-generated method stub
        
    }

    public void batteryStatusChange(int arg0) {
        DEBUG ("BATTERY STATUS: " + arg0);
        // TODO Auto-generated method stub
        
    }

    public void powerOff() {
        DEBUG ("POWER OFF");
        // TODO Auto-generated method stub    
    }
    
    private TimerTask javascriptTester = new TimerTask () { 
        public void run() { 
            if (mScriptEngine != null) { 
                try { 
                    mScriptEngine.executeScript("ice.test();" , null);
                } catch (Exception e) { 
                    ERROR("ice.test - exception testing namespace: " + e); 
                }
            } else { 
                DEBUG("ice.test - script engine is null!"); 
            }

        }
    };
    
}

