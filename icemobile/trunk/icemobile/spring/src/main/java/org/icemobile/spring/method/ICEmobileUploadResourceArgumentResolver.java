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
package org.icemobile.spring.method;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.icemobile.application.Resource;
import org.icemobile.spring.annotation.ICEmobileResourceUpload;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ICEmobileUploadResourceArgumentResolver implements HandlerMethodArgumentResolver {
    
    private static final String STORE_KEY = "ICEMOBILE_UPLOAD_STORE";
         
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ICEmobileResourceUpload.class);
    }
 
    public Resource resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) 
                            throws Exception {
        String id = parameter.getParameterAnnotation(ICEmobileResourceUpload.class).value();
        Resource resource = getStore(webRequest).get(id);
        return resource;        
    }
 
    private Map<String,Resource> getStore(NativeWebRequest webRequest){
        //default to session store for now
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpSession session = httpServletRequest.getSession(true);
        @SuppressWarnings("unchecked")
        Map<String,Resource> store = (Map<String,Resource>)session.getAttribute(STORE_KEY);
        if( store == null ){
            store = new HashMap<String,Resource>();
            session.setAttribute(STORE_KEY, store);
        }
        return store;
    }

     
     
}
