/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package org.icefaces.impl.push.servlet;

import org.icepush.PushContext;
import org.icepush.servlet.MainServlet;

import javax.faces.FactoryFinder;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ICEpushResourceHandler extends ResourceHandlerWrapper implements PhaseListener  {
    private static Logger log = Logger.getLogger(ICEpushResourceHandler.class.getName());
    private static final Pattern ICEpushRequestPattern = Pattern.compile(".*listen\\.icepush");
    private static final String RESOURCE_KEY = "javax.faces.resource";
    private static final String BROWSERID_COOKIE = "ice.push.browser";
    private ResourceHandler handler;
    private MainServlet mainServlet;

    public ICEpushResourceHandler(ResourceHandler handler) {
        try {
            this.handler = handler;
            ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            mainServlet = new MainServlet(context);
            context.setAttribute(ICEpushResourceHandler.class.getName(), this);
            LifecycleFactory factory = (LifecycleFactory)
                    FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            Lifecycle lifecycle = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
            lifecycle.addPhaseListener(this);
        } catch (Throwable t)  {
            log.log(Level.INFO, "Ajax Push Resource Handling not available: " + t);
        }
    }

    public ResourceHandler getWrapped() {
        return handler;
    }

    @Override
    public Resource createResource(String resourceName) {
        if (ICEpushListenResource.RESOURCE_NAME.equals(resourceName)) {
            return new ICEpushListenResource(this);
        }
        else {
            return super.createResource(resourceName);
        }
    }

    public void handleResourceRequest(FacesContext facesContext) throws IOException {
        if (null == mainServlet)  {
            handler.handleResourceRequest(facesContext);
            return;
        }
        ExternalContext externalContext = facesContext.getExternalContext();
        String resourceName = facesContext.getExternalContext()
            .getRequestParameterMap().get(RESOURCE_KEY);
        //special proxied code path for portlets
        if (ICEpushListenResource.RESOURCE_NAME.equals(resourceName))  {
            try {
                mainServlet.service(new ProxyHttpServletRequest(facesContext), 
                        new ProxyHttpServletResponse(facesContext));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (!(externalContext.getRequest() instanceof HttpServletRequest))  {
            handler.handleResourceRequest(facesContext);
            return;
        }
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        String requestURI = request.getRequestURI();
        if (ICEpushRequestPattern.matcher(requestURI).find()) {
            try {
                mainServlet.service(request, response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            handler.handleResourceRequest(facesContext);
        }
    }

    public boolean isResourceRequest(FacesContext facesContext) {
        String resourceName = facesContext.getExternalContext()
            .getRequestParameterMap().get(RESOURCE_KEY);
        if (ICEpushListenResource.RESOURCE_NAME.equals(resourceName))  {
            return true;
        }
        ExternalContext externalContext = facesContext.getExternalContext();
        if (!(externalContext.getRequest() instanceof HttpServletRequest))  {
            return handler.isResourceRequest(facesContext);
        }
        HttpServletRequest servletRequest = (HttpServletRequest) externalContext.getRequest();
        String requestURI = servletRequest.getRequestURI();
        return handler.isResourceRequest(facesContext) || ICEpushRequestPattern.matcher(requestURI).find();
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    public void beforePhase(PhaseEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Object BrowserID = externalContext.getRequestCookieMap()
                .get(BROWSERID_COOKIE);
                
        HttpServletRequest request;
        HttpServletResponse response;

        if (!(externalContext.getRequest() instanceof HttpServletRequest))  {
            request = new ProxyHttpServletRequest(facesContext); 
            response = new ProxyHttpServletResponse(facesContext);
        } else {
            request = (HttpServletRequest) externalContext.getRequest();
            response = (HttpServletResponse) externalContext.getResponse();
        }

        if (null == BrowserID)  {
            //Need better integration with ICEpush to assign ice.push.browser
            //without createPushId()
            ((PushContext) externalContext.getApplicationMap()
                    .get(PushContext.class.getName()))
                    .createPushId(request, response);
        }
    }

    public void afterPhase(PhaseEvent event) {
    }

    public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            mainServlet.service(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static void notifyContextShutdown(ServletContext context) {
        try {
            ((ICEpushResourceHandler) context.getAttribute(ICEpushResourceHandler.class.getName())).mainServlet.shutdown();
        } catch (Throwable t) {
            //no need to log this exception for optional Ajax Push, but may be
            //useful for diagnosing other failures
            log.log(Level.INFO, "MainServlet not found in application scope: " + t);
        }
    }
}
