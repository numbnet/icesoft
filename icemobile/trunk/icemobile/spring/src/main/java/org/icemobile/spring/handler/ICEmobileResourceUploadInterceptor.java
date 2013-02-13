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
package org.icemobile.spring.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.icemobile.application.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ICEmobileResourceUploadInterceptor extends HandlerInterceptorAdapter{
    
    private static final String STORE_KEY = "ICEMOBILE_UPLOAD_STORE";
    
    @Autowired
    protected ServletContext servletContext;
    
    public boolean preHandle(HttpServletRequest request, 
            HttpServletResponse response, Object handler)
        throws Exception {
        Map<String, Resource> store = getStore(handler, request);
        processUploads(request, handler, store);
        return true;
    }
    
    protected void processUploads(HttpServletRequest request, Object handler, Map<String,Resource> store){
        
    }
    
    private Map<String,Resource> getStore(Object handler, HttpServletRequest request){
        //default to session store for now
        HttpSession session = request.getSession(true);
        @SuppressWarnings("unchecked")
        Map<String,Resource> store = (Map<String,Resource>)session.getAttribute(STORE_KEY);
        if( store == null ){
            store = new HashMap<String,Resource>();
            session.setAttribute(STORE_KEY, store);
        }
        return store;
    }

}
