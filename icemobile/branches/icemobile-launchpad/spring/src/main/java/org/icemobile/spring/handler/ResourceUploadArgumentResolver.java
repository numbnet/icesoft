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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icemobile.application.Resource;
import org.icemobile.application.ResourceStore;
import org.icemobile.spring.annotation.ICEmobileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * An argument resolver to populate @ICEmobileResourceUpload annotated
 * method parameters. (requires Spring 3.1)
 *
 */
public class ResourceUploadArgumentResolver 
    implements HandlerMethodArgumentResolver {
    
    private static final Log LOG = LogFactory
            .getLog(ResourceUploadArgumentResolver.class);
    
    @Autowired
    private StoreLocator storeLocator;
             
    /**
     * Defines this ArgumentResolver to only support method parameters
     * annotated with the @ICEmobileResource annotation.
     * 
     * @param parameter The MethodParameter defining the argument
     * @return true if the method is annotated with @ICEmobileResource
     */
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ICEmobileResource.class);
    }
 
    /**
     * Resolve the argument and populate it with the associated ICEmobile
     * resource from the @ICEmobileResourceStore on the associated 
     * controller.
     * 
     * @param parameter The MethodParameter defining the argument
     * @param mavContainer The ModelAndViewContainer for the request
     * @param webRequest The wrapped request
     * @param binderFactor The binder factor.
     * @return The ICEmobile Resource to populate the argument.
     */
    public Resource resolveArgument(MethodParameter parameter, 
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, 
            WebDataBinderFactory binderFactory) 
                            throws Exception {
        String id = parameter.getParameterAnnotation(ICEmobileResource.class).value();
        ResourceStore store = storeLocator.getStore(parameter.getDeclaringClass(), 
                (HttpServletRequest)webRequest.getNativeRequest());
        Resource resource = null;
        if( store != null ){
            String token = webRequest.getSessionId();
            store.handleRequest((HttpServletRequest)webRequest.getNativeRequest(), token);
            resource = store.get(webRequest.getSessionId(), id);
            LOG.debug("resolved " + resource + " for " + id);
        }
        else{
            LOG.warn("Could not locate ICEmobile ResourceStore for " + parameter);
        }
        return resource;        
    }
 
     
}
