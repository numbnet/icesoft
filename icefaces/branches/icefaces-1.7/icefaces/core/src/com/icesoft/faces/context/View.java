package com.icesoft.faces.context;

import com.icesoft.faces.webapp.command.Command;
import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.command.NOOP;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.standard.NoCacheContentHandler;
import com.icesoft.faces.webapp.http.core.LifecycleExecutor;
import com.icesoft.faces.webapp.http.core.ResourceDispatcher;
import com.icesoft.faces.webapp.http.core.ViewQueue;
import com.icesoft.faces.webapp.http.portlet.PortletExternalContext;
import com.icesoft.faces.webapp.http.servlet.ServletExternalContext;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.util.SeamUtilities;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.lifecycle.Lifecycle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class View implements CommandQueue {
    private static final Log Log = LogFactory.getLog(View.class);
    private static final NOOP NOOP = new NOOP();
    private static final Runnable DoNothing = new Runnable() {
        public void run() {
        }
    };
    private final Page lifecycleExecutedPage = new Page() {
        private final ResponseHandler lifecycleResponseHandler = new NoCacheContentHandler("text/html", "UTF-8") {
            public void respond(Response response) throws Exception {
                super.respond(response);
                com.icesoft.util.SeamUtilities.removeSeamDebugPhaseListener(lifecycle);
                switchToNormalMode();
                LifecycleExecutor.getLifecycleExecutor(facesContext).apply(facesContext);
                switchToPushMode();
            }
        };

        public void serve(Request request) throws Exception {
            updateOnPageRequest(request);
            request.respondWith(lifecycleResponseHandler);
        }
    };
    private Page page = lifecycleExecutedPage;
    private final ReentrantLock queueLock = new ReentrantLock();
    private final ReentrantLock lifecycleLock = new ReentrantLock();
    private BridgeExternalContext externalContext;
    private BridgeFacesContext facesContext;
    private PersistentFacesState persistentFacesState;
    private Map bundles = Collections.EMPTY_MAP;
    private Command currentCommand = NOOP;
    private final String viewIdentifier;
    private final ArrayList onPutListeners = new ArrayList();
    private final ArrayList onTakeListeners = new ArrayList();
    private final Collection viewListeners = new ArrayList();
    private final String sessionID;
    private final Configuration configuration;
    private final SessionDispatcher.Monitor sessionMonitor;
    private final ResourceDispatcher resourceDispatcher;
    private final Lifecycle lifecycle;
    private Runnable dispose;
    private String lastPath;

    public View(final String viewIdentifier, String sessionID, Request request, final ViewQueue allServedViews, final Configuration configuration, final SessionDispatcher.Monitor sessionMonitor, ResourceDispatcher resourceDispatcher, Lifecycle lifecycle) throws Exception {
        this.sessionID = sessionID;
        this.configuration = configuration;
        this.viewIdentifier = viewIdentifier;
        this.sessionMonitor = sessionMonitor;
        this.resourceDispatcher = resourceDispatcher;
        this.lifecycle = lifecycle;

        //fail fast if environment cannot be detected
        this.externalContext = new UnknownExternalContext(this, configuration);
        request.detectEnvironment(new Request.Environment() {
            public void servlet(Object request, Object response) {
                externalContext = new ServletExternalContext(viewIdentifier, request, response, View.this, configuration, sessionMonitor);
            }

            public void portlet(Object request, Object response, Object portletConfig) {
                externalContext = new PortletExternalContext(viewIdentifier, request, response, View.this, configuration, sessionMonitor, portletConfig);
            }
        });
        this.facesContext = new BridgeFacesContext(externalContext, viewIdentifier, sessionID, this, configuration, resourceDispatcher);
        this.persistentFacesState = new PersistentFacesState(facesContext, lifecycleLock, viewListeners, configuration);
        this.onPut(new Runnable() {
            public void run() {
                try {
                    allServedViews.put(viewIdentifier);
                } catch (InterruptedException e) {
                    Log.warn("Failed to queue updated view", e);
                }
            }
        });
        this.dispose = new Runnable() {
            public void run() {
                Log.debug("Disposing " + this);
                installThreadLocals();
                notifyViewDisposal();
                release();
                persistentFacesState.dispose();
                facesContext.dispose();
                externalContext.dispose();
                allServedViews.remove(viewIdentifier);
                //dispose view only once
                dispose = DoNothing;
            }
        };
        acquireLifecycleLock();
        Log.debug("Created " + this);
    }

    public void updateOnXMLHttpRequest(Request request) throws Exception {
        acquireLifecycleLock();
        request.detectEnvironment(new Request.Environment() {
            public void servlet(Object request, Object response) {
                externalContext.update((HttpServletRequest) request, (HttpServletResponse) response);
                //#2139 this is a postback, so insert the key now 
                externalContext.insertPostbackKey();
            }

            public void portlet(Object request, Object response, Object config) {
                //this call cannot arrive from a Portlet
            }
        });
        makeCurrent();
    }

    //this is the page load request
    public void servePage(Request request) throws Exception {
        acquireLifecycleLock();
        page.serve(request);
    }

    private void updateOnPageRequest(final Request request) throws Exception {
        request.detectEnvironment(new Request.Environment() {
            public void servlet(Object servletRequest, Object servletResponse) {
                String path = request.getURI().getPath();
                boolean reloded = path.equals(lastPath);
                lastPath = path;

                //reuse FacesContext on reload -- this preserves the ViewRoot in case forward navigation rules were executed
                if (reloded && !SeamUtilities.isSeamEnvironment()) {
                    //page reload
                    externalContext.updateOnPageLoad(servletRequest, servletResponse);
                    facesContext.renderResponse();
                } else {
                    //page redirect
                    externalContext.dispose();
                    externalContext = new ServletExternalContext(viewIdentifier, servletRequest, servletResponse, View.this, configuration, sessionMonitor);
                    facesContext.dispose();
                    facesContext = new BridgeFacesContext(externalContext, viewIdentifier, sessionID, View.this, configuration, resourceDispatcher);
                    //reuse  PersistentFacesState instance when page redirects occur
                    persistentFacesState.setFacesContext(facesContext);
                }
            }

            public void portlet(Object request, Object response, Object config) {
                //page reload
                externalContext.updateOnPageLoad(request, response);
            }
        });
        makeCurrent();
    }

    public void put(Command command) {
        queueLock.lock();
        currentCommand = currentCommand.coalesceWith(command);
        queueLock.unlock();
        broadcastTo(onPutListeners);
    }

    public Command take() {
        queueLock.lock();
        Command command = currentCommand;
        currentCommand = NOOP;
        queueLock.unlock();
        broadcastTo(onTakeListeners);
        return command;
    }

    public void onPut(Runnable listener) {
        onPutListeners.add(listener);
    }

    public void onTake(Runnable listener) {
        onTakeListeners.add(listener);
    }

    private void broadcastTo(Collection listeners) {
        Iterator i = listeners.iterator();
        while (i.hasNext()) {
            Runnable listener = (Runnable) i.next();
            try {
                listener.run();
            } catch (Exception e) {
                Log.error("Failed to notify listener: " + listener, e);
            }
        }
    }

    public void release() {
        facesContext.release();
        externalContext.release();
        persistentFacesState.release();
    }

    public BridgeFacesContext getFacesContext() {
        return facesContext;
    }

    public PersistentFacesState getPersistentFacesState() {
        return persistentFacesState;
    }

    public void dispose() {
        dispose.run();
    }

    public void installThreadLocals() {
        persistentFacesState.setCurrentInstance();
        facesContext.setCurrentInstance();
    }

    private void makeCurrent() {
        acquireLifecycleLock();
        installThreadLocals();
        externalContext.injectBundles(bundles);
        facesContext.applyBrowserDOMChanges();
    }

    private void acquireLifecycleLock() {
        if (!lifecycleLock.isHeldByCurrentThread()) {
            lifecycleLock.lock();
        }
    }

    private void switchToNormalMode() {
        acquireLifecycleLock();
        facesContext.switchToNormalMode();
        externalContext.switchToNormalMode();
    }

    public String toString() {
        return "View[" + sessionID + ":" + viewIdentifier + "]";
    }

    void preparePage(final ResponseHandler handler) {
        page = new Page() {
            public void serve(Request request) throws Exception {
                request.respondWith(handler);
                page = lifecycleExecutedPage;
            }
        };
    }

    private void switchToPushMode() {
        acquireLifecycleLock();
        //collect bundles put by Tag components when the page is parsed
        bundles = externalContext.collectBundles();
        facesContext.switchToPushMode();
        externalContext.switchToPushMode();
    }

    private void notifyViewDisposal() {
        Iterator i = viewListeners.iterator();
        while (i.hasNext()) {
            try {
                ViewListener listener = (ViewListener) i.next();
                listener.viewDisposed();
            } catch (Throwable t) {
                Log.warn("Failed to invoke view listener", t);
            }
        }
    }

    private interface Page {
        void serve(Request request) throws Exception;
    }
}
