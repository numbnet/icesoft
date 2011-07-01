/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.application;

import java.util.Map;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.application.Application;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.icefaces.util.EnvUtils;
import org.icefaces.impl.util.Util;
import org.icefaces.impl.application.WindowScopeManager;

/**
 * <p>
 *   The <code>ResourceRegistry</code> allows an application to register
 *   javax.faces.application.Resource instances at runtime.  Each Resource
 *   is registered in a specified scope (Application, Session, View, Flash
 *   Window) so that the resource can be garbage collected when the scope
 *   expires.
 * </p>
 */
public class ResourceRegistry extends ResourceHandlerWrapper  {
    private static Logger log = Logger.getLogger(ResourceRegistry.class.getName());
    private ResourceHandler wrapped;

    private static String CURRENT_KEY = "org.icefaces.resourceRegistry.resourceKey";
    private static String RESOURCE_PREFIX = "/javax.faces.resource/";
    private static String MAP_PREFIX = "org.icefaces.resource-";

    public ResourceRegistry(ResourceHandler wrapped)  {
        this.wrapped = wrapped;
    }

    public ResourceHandler getWrapped() {
        return wrapped;
    }


    public void handleResourceRequest(FacesContext facesContext) throws IOException {
        ExternalContext externalContext = facesContext.getExternalContext();
        Application application = facesContext.getApplication();
        String key = extractResourceId(facesContext);
        if (null == key)  {
            wrapped.handleResourceRequest(facesContext);
            return;
        }

        ELContext elContext = facesContext.getELContext();
        
        ResourceRegistryHolder holder = (ResourceRegistryHolder) elContext
            .getELResolver().getValue(elContext, null, MAP_PREFIX + key);

        if (null == holder)  {
            wrapped.handleResourceRequest(facesContext);
            return;
        }
        //TODO: also check the name
        Resource resource = holder.resource;
        String contentType = resource.getContentType();
        if (contentType != null) {
            externalContext.setResponseContentType(resource.getContentType());
        }
        Map<String,String> headers = resource.getResponseHeaders();
        for (String header : headers.keySet())  {
            externalContext.setResponseHeader(header, headers.get(header));
        }
        InputStream in = resource.getInputStream();
        OutputStream out = externalContext.getResponseOutputStream();

        if (Util.acceptGzip(externalContext) && 
                EnvUtils.isCompressResources(facesContext) && 
                Util.shouldCompress(resource.getContentType()) )  {
            externalContext.setResponseHeader("Content-Encoding", "gzip");
            Util.compressStream(in, out);
        } else {
            Util.copyStream(in, out);
        }

    }

    /**
     * Add the provided resource to the custom scope Map.  This is intended to 
     * be used only in cases not covered by the other scope-specific methods.
     *
     * @param scopeMap the resource
     * @param scopeMap the resource
     * @return the requestPath of the resource
     */
    public static String addResource(Map scopeMap, Resource resource)  {
        return addResource("r", scopeMap, resource );
    }

    private static String addResource(String prefix, Map scopeMap, 
            Resource resource)  {
        String name = resource.getResourceName();
        String key;
        if ( (null != name) && (name.length() > 0) )  {
            key = name;
        } else {
            int index = getNextKey();
            key = prefix + String.valueOf(index);
        }
        ResourceRegistryHolder holder = 
                new ResourceRegistryHolder(key, resource);
        scopeMap.put(MAP_PREFIX + key, holder);

        String[] pathTemplate = EnvUtils.getPathTemplate();
        return pathTemplate[0] + key + pathTemplate[1];
    }

    private static String extractResourceId(FacesContext facesContext)  {
        ExternalContext externalContext = facesContext.getExternalContext();
        String path = externalContext.getRequestServletPath();
        if (!path.startsWith(RESOURCE_PREFIX))  {
            return null;
        }
        try {
            //strip off the javax.faces.resource prefix and remove
            //any extension found in the path template
            String key = path.substring(RESOURCE_PREFIX.length(), 
                    path.length() - EnvUtils.getPathTemplate()[1].length());
            return key;
        } catch (Exception e)  {
            return null;
        }
    }

    public static Resource getResourceByPath(
            FacesContext facesContext, String resPath) {
        if (resPath == null) {
            return null;
        }
        int prefixIndex = resPath.lastIndexOf(RESOURCE_PREFIX);
        if (prefixIndex < 0) {
            return null;
        }
        String key = resPath.substring(prefixIndex+RESOURCE_PREFIX.length(),
                resPath.length() - EnvUtils.getPathTemplate()[1].length());

        ELContext elContext = facesContext.getELContext();
        ResourceRegistryHolder holder = (ResourceRegistryHolder) elContext
            .getELResolver().getValue(elContext, null, MAP_PREFIX + key);
        if (null == holder)  {
            return null;
        }
        return holder.resource;
    }

    private synchronized static int getNextKey()  {
        Map appMap = FacesContext.getCurrentInstance().getExternalContext()
                .getApplicationMap();
        Integer currentKey = (Integer) appMap.get(CURRENT_KEY);
        if (null == currentKey)  {
            currentKey = new Integer(1);
        }
        currentKey = new Integer(currentKey.intValue() + 1);
        appMap.put(CURRENT_KEY, currentKey);
        return currentKey.intValue();
    }

    /**
     * Add the provided resource in application scope.
     *
     * @param scopeMap the resource
     * @param scopeMap the resource
     * @return the requestPath of the resource
     */
    public static String addApplicationResource(Resource resource)  {
        return addResource("a", FacesContext.getCurrentInstance()
                .getExternalContext().getApplicationMap(), resource );
    }

    /**
     * Add the provided resource in session scope.  Note that session scope
     * resources should be Serializable to support cluster replication
     * and session passivation.
     *
     * @param scopeMap the resource
     * @param scopeMap the resource
     * @return the requestPath of the resource
     */
    public static String addSessionResource(Resource resource)  {
        return addResource("s", FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap(), resource );
    }

    /**
     * Add the provided resource in flash scope.
     *
     * @param scopeMap the resource
     * @param scopeMap the resource
     * @return the requestPath of the resource
     */
    public static String addFlashResource(Resource resource)  {
        return addResource("f", FacesContext.getCurrentInstance()
                .getExternalContext().getFlash(), resource );
    }

    /**
     * Add the provided resource in view scope.
     *
     * @param scopeMap the resource
     * @param scopeMap the resource
     * @return the requestPath of the resource
     */
    public static String addViewResource(Resource resource)  {
        return addResource("v", FacesContext.getCurrentInstance()
                .getViewRoot().getViewMap(), resource );
    }

    /**
     * Add the provided resource in window scope.
     *
     * @param scopeMap the resource
     * @param scopeMap the resource
     * @return the requestPath of the resource
     */
    public static String addWindowResource(Resource resource)  {
        return addResource("w", WindowScopeManager.lookupWindowScope(
                FacesContext.getCurrentInstance()), resource );
    }


}

//Hold the resources in an instance of this private class to provide
//security by ensuring that only resources stored via this API are served
class ResourceRegistryHolder implements Serializable {
    public String key;
    public Resource resource;
    
    ResourceRegistryHolder(String key, Resource resource)  {
        this.key = key;
        this.resource = resource;
    }
}