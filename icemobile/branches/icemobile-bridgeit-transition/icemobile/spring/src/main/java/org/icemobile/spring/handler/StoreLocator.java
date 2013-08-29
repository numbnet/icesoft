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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icemobile.application.ResourceStore;
import org.icemobile.spring.annotation.ICEmobileResourceStore;
import org.icemobile.spring.controller.ICEmobileSXController;
import org.icemobile.spring.handler.support.MapFileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * A service to locate the ResourceStore. Used by ICEmobileResourceUploadArgumentResolver
 * and ICEmobileResourceUploadIntercepters.
 *
 */
@Service
@SuppressWarnings("rawtypes")
public class StoreLocator {
    
    @Autowired
    protected ServletContext servletContext;
        
    private static final Log LOG = LogFactory.getLog(StoreLocator.class);
    
    private static final String STORE_KEY = "ICEMOBILE_UPLOAD_STORE";
    
    @SuppressWarnings("unchecked")
    ResourceStore getStore(Class beanClass, HttpServletRequest request){
        ResourceStore store = null;
        
        if (beanClass.equals(ICEmobileSXController.class))  {
            //use session scoped store for ICEmobile-SX registration
            store = getSessionScopedStore(request);
        } else if (beanClass.isAnnotationPresent(
                ICEmobileResourceStore.class))  {
            ICEmobileResourceStore storeAnn = (ICEmobileResourceStore)
                    beanClass.getAnnotation(ICEmobileResourceStore.class);
            String beanName = storeAnn.bean();
            if( beanName != null && beanName.length() > 0 ){
                LOG.debug("getting bean store " + beanName);
                store = getBeanStore(request, beanClass, beanName);
            }
            else{
                if( "application".equals(storeAnn.scope())){
                    store = getApplicationScopedStore();
                    LOG.debug("getting application-scoped store");
                }
                else{
                    store = getSessionScopedStore(request);
                    LOG.debug("getting session-scoped store");
                }
            }
        }
        else{
            LOG.warn(beanClass.getName() + " bean does not have ICEmobileResourceAnnotation ");
        }
        return store;
    }
    
    private ResourceStore getSessionScopedStore(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        ResourceStore store = (ResourceStore)session.getAttribute(STORE_KEY);
        if( store == null ){
            store = new MapFileStore();
            session.setAttribute(STORE_KEY, store);
        }
        return store;
    }
    
    private ResourceStore getApplicationScopedStore(){
        ResourceStore store = (ResourceStore)servletContext.getAttribute(STORE_KEY);
        if( store == null ){
            store = new MapFileStore();
            servletContext.setAttribute(STORE_KEY, store);
        }
        return store;
    }
    
    private ResourceStore getBeanStore(HttpServletRequest request, Class beanClass, 
            String beanName){
        ResourceStore store = null;
        WebApplicationContext context = 
                RequestContextUtils.getWebApplicationContext(request);
        if( context.containsBean(beanName) ){
            try{
                store = (ResourceStore)context.getBean(beanName);
                LOG.debug("retrieved bean store: " + store);
            }
            catch( ClassCastException e){
                LOG.warn("@ICEmobileResourceUploadStore for " 
                        + beanClass.getName() + " declares bean='"
                        + beanName + "' which cannot be found");
            }
        }
        return store;
    }

}
