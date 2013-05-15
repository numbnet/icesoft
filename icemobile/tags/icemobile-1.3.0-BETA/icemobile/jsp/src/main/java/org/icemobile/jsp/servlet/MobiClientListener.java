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
package org.icemobile.jsp.servlet;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import org.icemobile.util.ClientDescriptor;

public class MobiClientListener implements ServletRequestListener{
    
    private static final String KEY = "mobiClient";

    public void requestDestroyed(ServletRequestEvent arg0) {
        // do nothing        
    }

    public void requestInitialized(ServletRequestEvent evt) {
        evt.getServletRequest().setAttribute(KEY, 
                ClientDescriptor.getInstance((HttpServletRequest)evt.getServletRequest()));
        
    }

}
