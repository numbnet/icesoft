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

package org.icefaces.impl.push.servlet;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.faces.FactoryFinder;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icefaces.util.EnvUtils;
import org.icepush.PushContext;
import org.icepush.servlet.MainServlet;

public class ICEpushResourceHandler extends ResourceHandlerWrapper implements PhaseListener  {
    private static final Logger log = Logger.getLogger(ICEpushResourceHandler.class.getName());

    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    private AbstractICEpushResourceHandler resourceHandler;

    public ICEpushResourceHandler(final ResourceHandler resourceHandler) {
        try {
            final ServletContext servletContext =
                (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
            String serverInfo = servletContext.getServerInfo();
            if (!serverInfo.startsWith("JBossWeb") && !serverInfo.startsWith("JBoss Web")) {
                this.resourceHandler = new ICEpushResourceHandlerImpl();
                this.resourceHandler.initialize(resourceHandler, servletContext, this);
            } else {
                final ICEpushResourceHandlerImpl impl = new ICEpushResourceHandlerImpl();
                this.resourceHandler = new BlockingICEpushResourceHandlerWrapper(impl);
                new Thread(
                    new Runnable() {
                        public void run() {
                            ICEpushResourceHandler.this.resourceHandler.initialize(resourceHandler, servletContext, ICEpushResourceHandler.this);
                            ICEpushResourceHandler.this.resourceHandler = impl;
                            lock.lock();
                            try {
                                condition.signalAll();
                            } finally {
                                lock.unlock();
                            }
                        }
                    }).start();
            }
        } catch (Throwable throwable) {
            log.log(Level.INFO, "Ajax Push Resource Handling not available: " + throwable);
        }
    }

    public void afterPhase(final PhaseEvent event) {
        resourceHandler.afterPhase(event);
    }

    public void beforePhase(final PhaseEvent event) {
        resourceHandler.beforePhase(event);
    }

    @Override
    public Resource createResource(final String resourceName) {
        return resourceHandler.createResource(resourceName);
    }

    public PhaseId getPhaseId() {
        return resourceHandler.getPhaseId();
    }

    public ResourceHandler getWrapped() {
        return resourceHandler.getWrapped();
    }

    @Override
    public void handleResourceRequest(final FacesContext facesContext) throws IOException {
        resourceHandler.handleResourceRequest(facesContext);
    }

    @Override
    public boolean isResourceRequest(final FacesContext facesContext) {
        return resourceHandler.isResourceRequest(facesContext);
    }

    public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        resourceHandler.service(request, response);
    }

    public static void notifyContextShutdown(ServletContext context) {
        try {
            ((ICEpushResourceHandler)context.getAttribute(ICEpushResourceHandler.class.getName())).shutdownMainServlet();
        } catch (Throwable t) {
            //no need to log this exception for optional Ajax Push, but may be
            //useful for diagnosing other failures
            log.log(Level.INFO, "MainServlet not found in application scope: " + t);
        }
    }

    private void shutdownMainServlet() {
        resourceHandler.shutdownMainServlet();
    }

    private static abstract class AbstractICEpushResourceHandler extends ResourceHandlerWrapper implements PhaseListener {
        abstract void initialize(ResourceHandler resourceHandler, ServletContext servletContext, ICEpushResourceHandler icePushResourceHandler);

        abstract void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

        abstract void shutdownMainServlet();
    }

    private static class ICEpushResourceHandlerImpl extends AbstractICEpushResourceHandler {
        private static final Pattern ICEpushRequestPattern = Pattern.compile(".*listen\\.icepush");
        private static final String RESOURCE_KEY = "javax.faces.resource";
        private static final String BROWSERID_COOKIE = "ice.push.browser";

        private ResourceHandler resourceHandler;
        private MainServlet mainServlet;

        public void afterPhase(final PhaseEvent event) {
            // Do nothing.
        }

        public void beforePhase(final PhaseEvent event) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            Object BrowserID = externalContext.getRequestCookieMap().get(BROWSERID_COOKIE);
            HttpServletRequest request = EnvUtils.getSafeRequest(facesContext);
            HttpServletResponse response = EnvUtils.getSafeResponse(facesContext);
            if (null == BrowserID)  {
                //Need better integration with ICEpush to assign ice.push.browser
                //without createPushId()
                ((PushContext) externalContext.getApplicationMap()
                        .get(PushContext.class.getName()))
                        .createPushId(request, response);
            }
        }

        @Override
        public Resource createResource(final String resourceName) {
            if (ICEpushListenResource.RESOURCE_NAME.equals(resourceName)) {
                return new ICEpushListenResource(this);
            }
            else {
                return super.createResource(resourceName);
            }
        }

        public PhaseId getPhaseId() {
            return PhaseId.RESTORE_VIEW;
        }

        public ResourceHandler getWrapped() {
            return resourceHandler;
        }

        @Override
        public void handleResourceRequest(final FacesContext facesContext) throws IOException {
            if (null == mainServlet)  {
                resourceHandler.handleResourceRequest(facesContext);
                return;
            }
            ExternalContext externalContext = facesContext.getExternalContext();
            String resourceName = facesContext.getExternalContext()
                .getRequestParameterMap().get(RESOURCE_KEY);

            //Return safe, proxied versions of the request and response if we are
            //running in a portlet environment.
            HttpServletRequest request = EnvUtils.getSafeRequest(facesContext);
            HttpServletResponse response = EnvUtils.getSafeResponse(facesContext);

            if (ICEpushListenResource.RESOURCE_NAME.equals(resourceName))  {
                try {
                    mainServlet.service(request,response);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            if (request instanceof ProxyHttpServletRequest)  {
                resourceHandler.handleResourceRequest(facesContext);
                return;
            }
            String requestURI = request.getRequestURI();
            if (ICEpushRequestPattern.matcher(requestURI).find()) {
                try {
                    mainServlet.service(request, response);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                resourceHandler.handleResourceRequest(facesContext);
            }
        }

        @Override
        public boolean isResourceRequest(final FacesContext facesContext) {
            String resourceName = facesContext.getExternalContext()
                .getRequestParameterMap().get(RESOURCE_KEY);
            if (ICEpushListenResource.RESOURCE_NAME.equals(resourceName))  {
                return true;
            }
            ExternalContext externalContext = facesContext.getExternalContext();
            if (EnvUtils.instanceofPortletRequest(externalContext.getRequest()))  {
                return resourceHandler.isResourceRequest(facesContext);
            }
            HttpServletRequest servletRequest = (HttpServletRequest) externalContext.getRequest();
            String requestURI = servletRequest.getRequestURI();
            return resourceHandler.isResourceRequest(facesContext) || ICEpushRequestPattern.matcher(requestURI).find();
        }

        void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
            try {
                mainServlet.service(request, response);
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }

        void initialize(final ResourceHandler resourceHandler, final ServletContext servletContext, final ICEpushResourceHandler icePushResourceHandler) {
            this.resourceHandler = resourceHandler;
            mainServlet = new MainServlet(servletContext);
            servletContext.setAttribute(ICEpushResourceHandler.class.getName(), icePushResourceHandler);
            ((LifecycleFactory)FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY)).
                getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE).addPhaseListener(icePushResourceHandler);
        }

        void shutdownMainServlet() {
            mainServlet.shutdown();
        }
    }

    private static class BlockingICEpushResourceHandlerWrapper extends AbstractICEpushResourceHandler {
        private final ICEpushResourceHandlerImpl resourceHandler;

        private BlockingICEpushResourceHandlerWrapper(final ICEpushResourceHandlerImpl resourceHandler) {
            this.resourceHandler = resourceHandler;
        }

        public void afterPhase(final PhaseEvent event) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                resourceHandler.afterPhase(event);
            } finally {
                lock.unlock();
            }
        }

        public void beforePhase(final PhaseEvent event) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                resourceHandler.beforePhase(event);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public Resource createResource(final String resourceName) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.createResource(resourceName);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public Resource createResource(final String resourceName, final String libraryName) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.createResource(resourceName, libraryName);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public Resource createResource(final String resourceName, final String libraryName, final String contentType) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.createResource(resourceName, libraryName, contentType);
            } finally {
                lock.unlock();
            }
        }

        public PhaseId getPhaseId() {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.getPhaseId();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public String getRendererTypeForResourceName(final String resourceName) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.getRendererTypeForResourceName(resourceName);
            } finally {
                lock.unlock();
            }
        }

        public ResourceHandler getWrapped() {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.getWrapped();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void handleResourceRequest(final FacesContext facesContext) throws IOException {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                resourceHandler.handleResourceRequest(facesContext);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public boolean isResourceRequest(final FacesContext facesContext) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.isResourceRequest(facesContext);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public boolean libraryExists(final String libraryName) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.libraryExists(libraryName);
            } finally {
                lock.unlock();
            }
        }

        void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                resourceHandler.service(request, response);
            } finally {
                lock.unlock();
            }
        }

        void initialize(final ResourceHandler resourceHandler, final ServletContext servletContext, final ICEpushResourceHandler icePushResourceHandler) {
            this.resourceHandler.initialize(resourceHandler, servletContext, icePushResourceHandler);
        }

        void shutdownMainServlet() {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                resourceHandler.shutdownMainServlet();
            } finally {
                lock.unlock();
            }
        }
    }
}
