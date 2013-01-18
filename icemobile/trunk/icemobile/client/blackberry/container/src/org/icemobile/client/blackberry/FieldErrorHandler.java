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

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldErrorHandler;
import net.rim.device.api.browser.field2.BrowserFieldRequest;

public class FieldErrorHandler extends BrowserFieldErrorHandler {

    public FieldErrorHandler( BrowserField field) { 
        super (field);
    }

    public void displayContentError(String url, String errorMessage) { 
    	Logger.ERROR("FieldErrorHandler - URL error: " + errorMessage + ", URL: " + url );
    }

    public void displayContentError(String url, InputConnection connection, Throwable t) { 
    	Logger.ERROR("FieldErrorHandler - Stream error: " +  t + ", URL: " + url);
    }

    public BrowserField getBrowserField() { 
        return super.getBrowserField(); 
    }

    public void navigationRequestError(BrowserFieldRequest request, Throwable t) { 
    	Logger.ERROR("FieldErrorHandler - navigationRequestError : " + t);
    }

    public void requestContentError(BrowserFieldRequest request, Throwable t) { 
    	Logger.ERROR("FieldErrorHandler - ResourceContentError: " + t); 
    }

    public InputConnection resourceRequestError(BrowserFieldRequest request, Throwable t) { 
    	Logger.ERROR("FieldErrorHandler - ResourceRequestError: " + t);
        return super.resourceRequestError(request, t);
    }
}
