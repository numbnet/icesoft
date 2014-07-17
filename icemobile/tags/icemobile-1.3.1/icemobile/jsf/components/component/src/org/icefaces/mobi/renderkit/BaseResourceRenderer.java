/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobi.renderkit;


import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.icefaces.application.ResourceRegistry;
import org.icefaces.mobi.utils.IceOutputResource;
import org.icefaces.mobi.utils.MobiJSFUtils;

/**
 * Created for mobility project for Audio and video playback
 * components. Should be also used for GraphicImage (yet to be refactored)
 */
public class BaseResourceRenderer extends CoreRenderer {
    private static final Logger logger = Logger.getLogger(BaseResourceRenderer.class.getName());


    private String registerAndGetPath(FacesContext facesContext,
            String scope, IceOutputResource iceResource) {
        String registeredPath = "";
        if (scope.equals("flash"))  {
            registeredPath = ResourceRegistry.addSessionResource(iceResource);
        } else if (scope.equals("application"))  {
            registeredPath = ResourceRegistry.addApplicationResource(iceResource);
        } else if (scope.equals("window")) {
            registeredPath = ResourceRegistry.addWindowResource(iceResource);
        } else if (scope.equals("view"))  {
            registeredPath = ResourceRegistry.addViewResource(iceResource);
        } else if (scope.equals("session")) {
            registeredPath = ResourceRegistry.addSessionResource(iceResource);
        }

        //all but application scope may require a session for the resource
        HttpSession session  = (HttpSession) facesContext
                .getExternalContext().getSession(false);
        if (null != session)  {
            registeredPath += ";jsessionid=" + 
                    MobiJSFUtils.getSessionIdCookie(facesContext);
        }

        return registeredPath;
    }

    protected String processStaticSrc(FacesContext facesContext, Object o, String url) {
        String value = String.valueOf(o);
        // support url as an alias for value
        if (value == null) {
            value = url;
        }
        if (value != null) {
//            String ctxtPath = facesContext.getExternalContext().getRequestContextPath();
            return facesContext.getApplication().getViewHandler().getResourceURL(facesContext, value);
        } else {
            return "";
        }
    }

    private ResourceRegistry lookupRegistry(FacesContext facesContext) {
        Map applicationMap = facesContext.getExternalContext().getApplicationMap();
        if (applicationMap.containsKey(ResourceRegistry.class.getName())) {
            ResourceRegistry rr = (ResourceRegistry) applicationMap.get(ResourceRegistry.class.getName());
            return rr;
        }
        return null;
    }

    public String processSrcAttribute(FacesContext facesContext, Object o,
                                      String name, String mimeType,
                                      String scope, String url) {
//        ResourceRegistry registry = this.lookupRegistry(facesContext);
        if (o instanceof IceOutputResource) {
            //register resource..do I have to check if it's already there?
            IceOutputResource iceResource = (IceOutputResource) o;
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("ALREADY have IceOutputResource so returning:-" + iceResource.getRequestPath());
            }
            //set name for resource to component?Hopefully user has created proper iceoutputresource object
            //for now just use session scope for testing
            return registerAndGetPath(facesContext, scope, iceResource);
        }
//	 
        if (o instanceof byte[]) {
            // have to create the resource first and cache it in ResourceRegistry
            //create IceOutputResource
            IceOutputResource ior = new IceOutputResource(name, o, mimeType);
//	        	if (null!= this.mimeType){
//		              final String mimeType = attributes.containsKey("mimeType") ? String.valueOf(attributes.get("mimeType")) : "";
//		              logger.info("\t\t mimeType="+this.mimeType);
//		             // update the request headers in iceOutputResource class? as well as content
//		        }
            String registeredPath = 
                    registerAndGetPath(facesContext, scope, ior);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("instance of byte array returning path=" + registeredPath + " with mimeType=" + ior.getContentType());
            }
            return registeredPath;
        } else {
            // just do default from compat ImageRenderer as it's a static file
            return processStaticSrc(facesContext, o, url);
        }

    }


}
