/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

package org.icemobile.jsp.tags;


import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.icemobile.jsp.util.MobiJspConstants;
import org.icemobile.jsp.util.Util;
import org.icemobile.util.ClientDescriptor;
import org.icemobile.util.Constants;
import org.icemobile.util.MobiEnvUtils;
import org.icemobile.util.SXUtils;
import org.icemobile.util.Utils;

/**
 * This is the Device specific detection and script writing tag.
 */
public class DeviceResourceTag extends BaseSimpleTag {

	static Logger log = Logger.getLogger(DeviceResourceTag.class.getName());

	// compressed css post-fix notation.
	public static final String CSS_COMPRESSION_POSTFIX = "-min";

	// iPhone style sheet name found in jar.
	public static final String IPHONE_CSS = "iphone.css";
	// iPad style sheet name found in jar.
	public static final String IPAD_CSS = "ipad.css";
	// Android style sheet name found in jar.
	public static final String ANDROID_CSS = "android.css";
	// Android honeycomb style sheet name found in jar.
	public static final String HONEYCOMB_CSS = "honeycomb.css";
	// Blackberry style sheet name found in jar.
	public static final String BBERRY_CSS = "bberry.css";


	// default resource library for a default themes,  if not specified in
	// component definition this library will be loaded.
	private static final String DEFAULT_LIBRARY = "org.icefaces.component.skins";
	// url of a resource that could not be resolved, danger Will Robertson.
	public static final String RESOURCE_URL_ERROR = "RES_NOT_FOUND";
	// key to store if algorithm to detect device type has run. If a device
	// was detected this key used to store the device name in session scope
	// for later retrial on subsequent component renders.
	public static final String MOBILE_DEVICE_TYPE_KEY = "mobile_device_type";
	private final String EMPTY_STRING = "";
	public static final String IOS_APP_ID = "485908934";
	
	public static final String META_CONTENTTYPE = "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>";
	public static final String META_VIEWPORT = "<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0'/>";
	public static final String META_IOS_WEBAPPCAPABLE = "<meta name='apple-mobile-web-app-capable' content='yes'/>";
	public static final String META_IOS_APPSTATUSBAR = "<meta name='apple-mobile-web-app-status-bar-style' content='black'/>";
	public static final String META_IOS_SMARTAPPBANNER = "<meta name='apple-itunes-app' content=\"app-id=%s, app-argument=%s\"/>";
	
	public static final String LINK_SHORTCUT_ICON = "<link href='%s/resources/images/favicon.ico' rel='shortcut icon' type='image/x-icon'/>";
	public static final String LINK_FAV_ICON = "<link href='%s/resources/images/favicon.ico' rel='icon' type='image/x-icon'/>";
	
	public static final String SCRIPT_ICEPUSH = "<script type='text/javascript' src='code.icepush'></script>";
	public static final String SCRIPT_ICEMOBILE = "<script type='text/javascript' src='%s%s/javascript/icemobile.js'></script>";
	public static final String SCRIPT_ICEMOBILE_PROD = "<script type='text/javascript' src='%s%s/javascript/icemobile-min.js'></script>";
	public static final String SCRIPT_SIMULATOR = "<script type='text/javascript' src='%s%s/javascript/simulator-interface.js'></script>";
	
	//tag attributes
	private String name;
	private String library;
	private String view;
	private boolean includeIOSSmartAppBanner = true;
	private boolean includePush = false;

	public void doTag() throws IOException {
		
	    boolean ios6 = false;
		boolean desktop = false;
				
		PageContext pageContext = getContext();
		ClientDescriptor client = getClient();
		
		ios6 = client.isIOS6();
		if( !ios6 ){
			desktop = client.isDesktopBrowser();
		}
		JspWriter out = pageContext.getOut();
		String contextRoot = getContextRoot();
		out.write(META_CONTENTTYPE);
		out.write(String.format(LINK_FAV_ICON, contextRoot));
		out.write(String.format(LINK_SHORTCUT_ICON, contextRoot));
		if( !desktop ){
			out.write(META_VIEWPORT);
			if( ios6 ){
				out.write(META_IOS_WEBAPPCAPABLE);
				out.write(META_IOS_APPSTATUSBAR);
				if (isShowAppBanner(client))  {
				    //String uploadURL = SXUtils.getRegisterSXURL(getRequest(),MobiJspConstants.SX_UPLOAD_PATH); //TODO MOBI-359
					String uploadURL = SXUtils.getRegisterSXURL(getRequest());
				    String smartAppMeta = String.format(META_IOS_SMARTAPPBANNER, IOS_APP_ID, uploadURL);
					out.write(smartAppMeta);
					pageContext.setAttribute(Constants.IOS_SMART_APP_BANNER_KEY, Boolean.TRUE);
				}
			}
		}
		writeOutDeviceStyleSheets();
		if (MobiEnvUtils.isProductionStage(getServletContext()))  {
		    out.write(String.format(SCRIPT_ICEMOBILE_PROD, contextRoot, MobiJspConstants.RESOURCE_BASE_URL));
		}
		else{
		    out.write(String.format(SCRIPT_ICEMOBILE, contextRoot, MobiJspConstants.RESOURCE_BASE_URL));
		}
		
		if( client.isDesktopBrowser() ){
		    out.write(String.format(SCRIPT_SIMULATOR, contextRoot, MobiJspConstants.RESOURCE_BASE_URL));
		}
		
		if( includePush ){
			out.write(SCRIPT_ICEPUSH);
            String cloudPushId = Utils.getCloudPushId(getRequest());
            if (null != cloudPushId) {
                out.write("<script type='text/javascript'>");
                out.write(
                        "window.addEventListener('load', function() { ice.push.parkInactivePushIds('"
                                + cloudPushId + "'); }, false);");
                out.write("</script>");
            }
		}
	}
	
    private boolean isShowAppBanner(ClientDescriptor client)  {
        if (MobiEnvUtils.isDevelopmentStage(getServletContext()))  {
            return false;
        }
        return (includeIOSSmartAppBanner && !client.isSXRegistered());
    }

	private void writeOutDeviceStyleSheets() throws IOException {
		
		PageContext pageContext = getContext();
		JspWriter out = pageContext.getOut();
		
		/**
		 * The component has three modes in which it executes.
		 * 1.) no attributes - then component tries to detect a mobile device
		 *     in from the user-agent.  If a mobile device is discovered, then
		 *     it will fall into three possible matches, iphone, ipad,  android and
		 *     blackberry.  If the mobile device is not not know then ipad
		 *     is loaded. Library is always assumed to be DEFAULT_LIBRARY.
		 *
		 * 2.) name attribute - component will default to using a library name
		 *     of DEFAULT_LIBRARY.  The name attribute specifies one of the
		 *     possible device themes; iphone.css, android.css or bberry.css.
		 *     Error will result if named resource could not be resolved.
		 *
		 * 3.) name and libraries attributes. - component will use the library
		 *     and name specified by the user.  Component is fully manual in this
		 *     mode. Error will result if name and library can not generate a
		 *     value resource.
		 */

		// check for the existence of the name and library attributes.
		String nameVal = getName();
		String fileName = nameVal;
		String libVal = getLibrary();

		String viewVal = getView();

		// check for empty string on name attribute used for auto mode where
		// name value binding is used.
		nameVal = nameVal != null && nameVal.equals(EMPTY_STRING) ? null : name;

		// 1.) full automatic device detection.
		if (nameVal == null && libVal == null) {
		    ClientDescriptor client = getClient();
		    if( client.isIOS()){
		        if( client.isHandheldBrowser()){
		            nameVal = "iphone";
		        }
		        else{
		            nameVal = "ipad";
		        }
		    }
		    else if( client.isAndroidOS()){
		        if( client.isHandheldBrowser()){
		            nameVal = "android";
		        }
		        else{
		            nameVal = "honeycomb";
		        }
		    }
		    else if( client.isBlackBerryOS()){
		        nameVal = "bberry";
		    }
		    else{
		        nameVal = "ipad";
		    }
			log.fine("detected " + nameVal);

			// the view attribute if specified will apply a small or large
			// theme, large theme's are tablet based, so ipad and honeycomb.
			// small themes are android, iphone, and bberry.
			if (viewVal != null) {
				// forces a small view
				if (viewVal.equalsIgnoreCase(TagUtil.VIEW_TYPE_SMALL)) {
					if (nameVal.equals("ipad")) {
						nameVal = "iphone";
					} else if (nameVal.equals("honeycomb")) {
						nameVal = "android";
					}
				} else if (viewVal.equalsIgnoreCase(TagUtil.VIEW_TYPE_LARGE)) {
					if (nameVal.equals("iphone")) {
						nameVal = "ipad";
					} else if (name.equals("android")) {
						nameVal = "honeycomb";
					}
				} else {
					log.warning("View type " + viewVal + " is not a recognized view type");
				}
			}

			// load compressed css if this is production environment.
			fileName = nameVal;
			
			if (MobiEnvUtils.isProductionStage(getServletContext())) {
				fileName = fileName.concat(CSS_COMPRESSION_POSTFIX);
			}
			libVal = DEFAULT_LIBRARY;

		}
		// 2.) User has specified a named theme they want to load, no auto detect
		else if (nameVal != null && libVal == null) {
			// keep the name but apply default library.
			libVal = DEFAULT_LIBRARY;
		}
		// 3.) User has specified a name and theme of their own, anything goes.
		else {
			// nothing to do, any error will be displayed back to user at runtime
			// if the resource can't be found.
		}
		
		String contextRoot = Util.getContextRoot(pageContext.getRequest());
		out.write("<script type='text/javascript'>document.documentElement.className = document.documentElement.className+' "+nameVal+"';</script>");

		String cssLink = String.format("<link type='text/css' rel='stylesheet' href='%s%s/%s/%s/%s.css' />", 
				contextRoot, MobiJspConstants.RESOURCE_BASE_URL, libVal, nameVal, fileName);
		out.write(cssLink);
		
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public boolean isIncludeIOSSmartAppBanner() {
		return includeIOSSmartAppBanner;
	}

	public void setIncludeIOSSmartAppBanner(boolean includeIOSSmartAppBanner) {
		this.includeIOSSmartAppBanner = includeIOSSmartAppBanner;
	}

	public boolean isIncludePush() {
		return includePush;
	}

	public void setIncludePush(boolean includePush) {
		this.includePush = includePush;
	}
	
	public void release(){
	    super.release();
	    name = null;
	    library = null;
	    view = null;
	    includeIOSSmartAppBanner = true;
	    includePush = false;
	}

}
