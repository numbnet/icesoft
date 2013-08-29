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

package org.icemobile.client.blackberry;

import javax.microedition.io.InputConnection;

import net.rim.device.api.io.http.HttpHeaders;

import org.icemobile.client.blackberry.utils.HistoryManager;
import org.icemobile.client.blackberry.utils.ResultHolder;

public interface ContainerController {

    // Application GUID
    // In Eclipse, type a string, select it and right click->"Convert String to Long"
    // to create a unique GUID. Last value is "ICEmobileContainer 1.3 Beta"
    public final String PRODUCT_ID = "ICEmobileContainer 1.3 Beta";
    public final long GUID = 0x474f3ee26aad77b2L;
    
    // Container navigation methods
    public void reloadCurrentPage();

    public void loadPage(String url);

    public void back();

    /**
     * Sets in motion the chain for handling the response from a request. The Controller
     * is meant to retain the ResultHolder with the given key so that the javascript layer
     * can subsequently request the response with the key. This is to allow asynchronous
     * operation with a measure of protection from response collisions. The Key should be
     * unique for each call to this method
     *
     * @param resultKey The key to identify this result
     * @param holder    Instance of the ResultHolder holding the previous response
     */
    public void processResult(String resultKey, ResultHolder holder);

    /**
     * The container is required to fetch the ResultHolder associated with a given key @see processResult
     * when called from Javascript. The call from Javascript (in a different thread) will call ice.handleResponse( resultHolder.getResult() );
     *
     * @param responseKey
     * @return Instance of the ResultHolder containing the previous response
     */
    public ResultHolder getPendingResponse(String responseKey);

    /**
     * Container can expose a method that checks for the existence of javascript variables in the
     * namespace. For debugging purposes only.
     */
    public void testJavascript();

    /**
     * Post a request to the server 
     * @param actionMethod action method from the form 
     * @param request Body of the request
     * @param headers pre set header fields 
     * @return InputConnection for reading response
     * @throws Exception
     */
    public InputConnection postRequest(String actionField, String request,
                                       HttpHeaders headers) throws Exception;

    /**
     * Called from EulaManager to indicate the user has accepted the EULA. Should persist the
     * fact
     */
    public void acceptEula();

    /**
     * Perform GUI and any required initialization of the container.
     */
    public void init();

    /**
     * Callback from the external Options system when the options have materially
     * changed.
     */
    public void optionsChanged();

    /**
     * Indicate to the Container that it should reload the HOME_URL when the application is
     * resumed. Called from the external Options system. Usually the user doesn't want to
     * reload the HOME_URL on reentry, even if some option values have been changed, but this
     * lets them do that.
     */
    public void reloadApplicationOnReentry();

    /**
     * Define the URL to be loaded from the menu system. This provides a persistent way to
     * jump to a page other than the HOME_URL. Also loads this page.
     *
     * @param url URL to load
     */
    public void setQuickURL(String url);

    /**
     * Retrieve the persisted value of the quick URL. If this hasn't bee defined, defaults
     * to the HOME_URL
     *
     * @return The quick url.
     */
    public String getQuickURL();

    /**
     * Insert a thumbnail image into the DOM. Executes the following javascript:
     * <code>ice.setThumbnail( id, icon ); </code>
     *
     * @param fieldId The id of the component
     * @param icon    The base64 encoded image to insert
     */
    public void insertThumbnail(String fieldId, String icon);

    /**
     * Insert a scanned QR code message into the DOM. Executes the following javascript:
     * <code>ice.addHiddenField( id , message); </code>
     *
     * @param fieldId The Id of the component.
     * @param message
     */
    public void insertQRCodeScript(String fieldId, String message);

    public HistoryManager getHistoryManager();

    /**
     * Dissolve the BIS push agents and reconstruct
     */
    public void resetPushAgent();
    
    /**
     * Reset any cached Authorization keys causing user to be 
     * prompted for new username/password
     */
    public void clearAuthorizationCache();

    /**
     * fetch the contents of a file as a resource
     *
     * @param name name of resource
     * @return Contents of file
     */
    public String readLocalFileSystem(Class clazz, String name);

    /**
     * Insert a filename as a hidden field in the DOM. Executes teh following javascript:
     * <code>ice.addHiddenField( id, filename); </code>
     *
     * @param fieldId  fieldId of the component
     * @param filename filename to insert
     */
    public void insertHiddenFilenameScript(String fieldId, String filename);
    
    /**
     * This method will insert a hidden field value into the DOM. 
     * @param id The id of the parent in which to insert the hidden input field
     * @param hiddenArgument The value of the input field. 
     */
    public void insertHiddenFieldUntyped(final String id, final String hiddenArgument); 
    
    /**
     * Exit the container 
     */
    public void shutdownContainer(); 
    
    /**
     * Run a series of diagnostic checks on the javascript namespace defined in the BrowserField
     */
    public void testJavascriptNamespace();
    
  
}
