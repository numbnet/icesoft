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

package org.icefaces.impl.application;

import org.icefaces.util.EnvUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.servlet.http.HttpSession;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuxUploadResourceHandler extends ResourceHandlerWrapper  {
    private static Logger log = Logger.getLogger(AuxUploadResourceHandler.class.getName());
    public static String AUX_REQ_MAP_KEY = 
            AuxUploadResourceHandler.class.getName() + "-request-map";
    private ResourceHandler wrapped;
    private Resource tokenResource;

    public AuxUploadResourceHandler(ResourceHandler wrapped)  {
        this.wrapped = wrapped;
        tokenResource = createResource("icemobilebud.txt");
    }

    public ResourceHandler getWrapped() {
        return wrapped;
    }

    public void handleResourceRequest(FacesContext facesContext) throws IOException {
        ExternalContext externalContext = facesContext.getExternalContext();
    
        if ( tokenResource.getRequestPath().equals(
            getResourcePath(facesContext)) )  {
            storeParts(externalContext);
            externalContext.setResponseContentType("text/plain");
            OutputStream out = externalContext.getResponseOutputStream();
            out.write("handled by AuxUploadResourceHandler".getBytes());
            return;
        }
        
        wrapped.handleResourceRequest(facesContext);
        
    }

    public static Map getAuxRequestMap()  {
        Map auxRequestMap = (Map) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get(AUX_REQ_MAP_KEY);
        return auxRequestMap;
    }

    private void storeParts(ExternalContext externalContext)  {
        HttpServletRequest request = 
                (HttpServletRequest) externalContext.getRequest();
        HttpSession session = request.getSession();
        try {
        
            Map auxRequestMap = 
                    (Map) session.getAttribute(AUX_REQ_MAP_KEY);
            if (null == auxRequestMap)  {
                auxRequestMap = new HashMap();
            }

            for (Part part : request.getParts()) {
                String partType = part.getContentType();
                String partName = part.getName();
                if (null == partType)  {
                    auxRequestMap.put(partName, 
                            request.getParameter(part.getName()) );
                } else {
                    auxRequestMap.put(partName, part );
                }
            }
            
            session.setAttribute(AUX_REQ_MAP_KEY, auxRequestMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTokenResourcePath()  {
        return tokenResource.getRequestPath();
    }

    private static String getResourcePath(FacesContext facesContext)  {
        ExternalContext externalContext = facesContext.getExternalContext();
        String path = externalContext.getRequestServletPath();
        if (null == path)  {
            path = externalContext.getRequestPathInfo();
        }
        return (externalContext.getRequestContextPath() + path);
    }

}
