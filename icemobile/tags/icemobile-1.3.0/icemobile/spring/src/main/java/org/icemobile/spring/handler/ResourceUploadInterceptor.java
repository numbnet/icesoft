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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icemobile.application.ResourceStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * This class can be used for ICEmobile resource upload support. 
 * The class will intercept all requests checking for multipart file
 * uploads. The ICEmobileResourceUploadStore configured on the controller
 * will be populated with the uploaded resources.
 * 
 * To configure your application, include the 
 * following in your Spring configuration:
 * 
 * <interceptors>
 *    <beans:bean class="org.icemobile.spring.handler.ICEmobileResourceUploadInterceptor" />
 * </interceptors>
 *
 */
public class ResourceUploadInterceptor extends HandlerInterceptorAdapter{
    
    private static final Log LOG = LogFactory.getLog(ResourceUploadInterceptor.class);
    
    @Autowired
    protected ServletContext servletContext;
    
    @Autowired 
    protected ApplicationContext applicationContext;
    
    @Autowired
    private StoreLocator storeLocator;
    
    /**
     * Process the request for ICEmobile resource uploads.
     * 
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return <code>true</code> if the execution chain should proceed with the
     * next interceptor or the handler itself. Else, DispatcherServlet assumes
     * that this interceptor has already dealt with the response itself.
     * @see    org.springframework.web.servlet.HandlerInterceptor 
     * @throws Exception in case of errors
     */
    public boolean preHandle(HttpServletRequest request, 
            HttpServletResponse response, Object handler)
        throws Exception {
        
        //only process post submissions
        if( request.getMethod().equals("POST") && handler instanceof HandlerMethod ){
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            Class beanClass = handlerMethod.getBean().getClass();
            ResourceStore store = storeLocator.getStore(beanClass, request);
            LOG.debug("beanClass: " + beanClass.getName() + ", store: " + store);
            if( store != null ){
                processUploads(request, store);
            }
            else{
                LOG.warn("missing @ICEmobileResourceStore annotation on " + beanClass.getName() +
                		"controller class, cannot process uploads");
            }
        }
        return true;
    }
    
    protected void processUploads(HttpServletRequest request, ResourceStore store){
        String token = request.getSession().getId();
        store.handleRequest(request, token);
    }

}
