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

import javax.microedition.io.InputConnection;

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldErrorHandler;
import net.rim.device.api.browser.field2.BrowserFieldRequest;

public class FieldErrorHandler extends BrowserFieldErrorHandler {

    public FieldErrorHandler( BrowserField field) { 
        super (field);
    }

    public void displayContentError(String url, String errorMessage) { 
        ICEmobileContainer.ERROR("FieldErrorHandler - URL error: " + errorMessage + ", URL: " + url );
    }

    public void displayContentError(String url, InputConnection connection, Throwable t) { 
        ICEmobileContainer.ERROR("FieldErrorHandler - Stream error: " +  t + ", URL: " + url);
    }

    public BrowserField getBrowserField() { 
        return super.getBrowserField(); 
    }

    public void navigationRequestError(BrowserFieldRequest request, Throwable t) { 
        ICEmobileContainer.ERROR("FieldErrorHandler - navigation request error : " + t);
    }

    public void requestContentError(BrowserFieldRequest request, Throwable t) { 
        ICEmobileContainer.ERROR("FieldErrorHandler - ResourceContent error"); 
    }

    public InputConnection resourceRequestError(BrowserFieldRequest request, Throwable t) { 
        ICEmobileContainer.ERROR("FieldErrorHandler - ResourceError connection");
        return super.resourceRequestError(request, t);
    }
}
