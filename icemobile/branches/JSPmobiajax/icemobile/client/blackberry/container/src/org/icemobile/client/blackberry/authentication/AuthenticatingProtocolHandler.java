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

package org.icemobile.client.blackberry.authentication;

import java.io.IOException;

import javax.microedition.io.HttpConnection;
import javax.microedition.io.InputConnection;

import org.icemobile.client.blackberry.Logger;

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldCookieManager;
import net.rim.device.api.browser.field2.BrowserFieldNavigationRequestHandler;
import net.rim.device.api.browser.field2.BrowserFieldRequest;
import net.rim.device.api.browser.field2.BrowserFieldResourceRequestHandler;
import net.rim.device.api.io.http.HttpHeaders;

public class AuthenticatingProtocolHandler implements 
BrowserFieldNavigationRequestHandler, 
BrowserFieldResourceRequestHandler {

	private AuthenticationManager mManager;
	private BrowserField mBrowserField; 
	protected BrowserFieldCookieManager mBrowserCookieManager;
	
	protected String[] mAuthDetails;

	private final int MAX_RETRY_ATTEMPTS = 3; 

	public AuthenticatingProtocolHandler (AuthenticationManager manager,
                                          BrowserField browserField) {

		mManager = manager; 
		mBrowserField = browserField;
		mBrowserCookieManager = mBrowserField.getCookieManager();
	}

	public void handleNavigation(BrowserFieldRequest request)
	throws Exception {
		String URL = request.getURL(); 		
		Logger.DEBUG("Protocol-Handler: URL: " + URL );
		int spos = URL.indexOf("#"); 
		if (spos > 0) { 
			URL = URL.substring(0, spos); 
			request.setURL(URL);
		}
		InputConnection connection = handleResource( request ); 
		mBrowserField.displayContent( connection, request.getURL() ); 
	}

	public InputConnection handleResource(BrowserFieldRequest request)
	throws Exception {
		InputConnection inputConnection = null;

		try { 
			mBrowserCookieManager.setCookie(request.getURL(), "com.icesoft.user-agent=HyperBrowser/1.0");
			inputConnection = mBrowserField.getConnectionManager().makeRequest(request); 
			if (inputConnection instanceof HttpConnection ) { 
				inputConnection = retrieveAuthorizedConnection((HttpConnection)inputConnection, request); 
			}
		} catch (IOException ioe) { 
			// Ignorable EOF exceptions in blocking thread
		} catch (Exception e) { 
			Logger.DEBUG("Exception in ProtocolHandler " + e);
		}
		return inputConnection;
	}

	/**
	 * Attempt to retrieve an authorized HttpConnection. This method <p><ol> 
	 * <li>Checks if the existing connection requires authorization (401 response)</li> 
	 * <li>If so, defers to the AuthenticationManager to farm out the implementation</li>  
	 * <li>The user may cancel or may enter the wrong password (MAX_RETRIES) in which 
	 *     case the user gets the unauthorized connection feed. </li>   
	 * Exception in request
	 */
	private InputConnection retrieveAuthorizedConnection(
			HttpConnection httpConnection, BrowserFieldRequest request ) {

		try {

			String authentication = null;
			int retryCount = 0; 
			while ( requiresAuthorization( httpConnection) && retryCount < MAX_RETRY_ATTEMPTS ) { 

				mAuthDetails = getDetailsFromAuthHeader(httpConnection);
				authentication = mManager.doAuthentication( request.getURL(), mAuthDetails[1], mAuthDetails[0]);
				if (authentication == null) {
					// just let the user have the unauthenticated response
					return httpConnection;
				} else { 
					httpConnection.close();
					httpConnection = requestContentWithCredentials(request, authentication);
				}
				retryCount++; 
			}  
			return httpConnection;
				            	
		} catch (Exception e) {
			Logger.ERROR("ICEmobile - exception requesting content: " + e);
		}
		// Return the httpConnection, whether it is authenticated or not.
		return httpConnection;
	}  
	
	/**
	 * Check the input Connection for the Unauthorized flag
	 * @param httpConnection 
	 * @return true if responseCode == HTTP_UNAUTHORIZED or HTTP_PROXY_AUTH
	 */
	private boolean requiresAuthorization( HttpConnection httpConnection) {
		try { 
		int code = httpConnection.getResponseCode(); 
		return (code == HttpConnection.HTTP_UNAUTHORIZED || 
				code == HttpConnection.HTTP_PROXY_AUTH);
		} catch (IOException ioe) { 
			Logger.ERROR("IOException checking response status? " + ioe);
		}
		return false; 
	}

	/**
	 * Re Fetch a Connection using a new Request with the details brought forward from 
	 * the previous request, including an authorization header. 
	 * 
	 * @param request The previous request 
	 * @param authentication The value for the "Authorization" header 
	 * @return The new open HttpConnection
	 */
	private HttpConnection requestContentWithCredentials(BrowserFieldRequest request, 
														 String authentication) { 
		HttpConnection returnVal = null;
		try { 
			HttpHeaders headers = request.getHeaders(); 
			headers.addProperty("Authorization",  authentication); 
			BrowserFieldRequest newRequest = 
				new BrowserFieldRequest( request.getURL(), request.getPostData(), headers);
			returnVal =  (HttpConnection) mBrowserField.getConnectionManager().makeRequest(newRequest);
			
			if (!requiresAuthorization( returnVal )) {
				mManager.cacheSuccessfulAuthorization( request.getURL(), mAuthDetails[0], authentication ); 	            
	        }
		} catch (Throwable t) { 
			
		}
		return returnVal;		
	}
	
	/**
	 * Read the authorization header, extracting the interesting bits. This may need to be 
	 * expanded for different implementations. 
	 * 
	 * @param httpConnection open connection 
	 * @return String[] of authorization parameters val[0] = scheme, val[1] = realm
	 * @throws IOException
	 */
	private String [] getDetailsFromAuthHeader(HttpConnection httpConnection) throws IOException {
		
		String[] returnVal= new String[2]; 
		
		int code = httpConnection.getResponseCode(); 
		String authHeader = (code == HttpConnection.HTTP_UNAUTHORIZED) ? 
				httpConnection.getHeaderField("WWW-Authenticate") : 
					httpConnection.getHeaderField("Proxy-Authenticate");
				
		if (authHeader == null) { 
			throw new IllegalArgumentException("Authentication header is null"); 
		}
		String realmOTest = authHeader.toLowerCase();
		int realmDx = realmOTest.indexOf("realm=");
		int schemeDx = realmOTest.indexOf(" ");
		
		if (realmDx > -1) {
			returnVal[1] = realmOTest.substring(realmDx+"realm=".length() ); 			
		}
		if (schemeDx > -1) { 
			returnVal[0] = realmOTest.substring(0, schemeDx); 
		}
	    return returnVal;
	}
	
} 