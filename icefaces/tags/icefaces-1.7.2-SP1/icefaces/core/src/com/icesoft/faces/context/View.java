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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class View implements CommandQueue {
    private static final Log Log = LogFactory.getLog(View.class);
    private static final NOOP NOOP = new NOOP();
    private static final Runnable DoNothing = new Runnable() {
        public void run() {
        }
    };
    private final ResponseHandler lifecycleResponseHandler = new NoCacheContentHandler("text/html", "UTF-8") {
        public void respond(Response response) throws Exception {
            super.respond(response);
            com.icesoft.util.SeamUtilities.removeSeamDebugPhaseListener(lifecycle);
            switchToNormalMode();
            LifecycleExecutor.getLifecycleExecutor(facesContext).apply(facesContext);
            switchToPushMode();
        }
    };
    private ResponseHandler responseHandler = lifecycleResponseHandler;

    private ReentrantLock queueLock = new ReentrantLock();
    private ReentrantLock lifecycleLock = new ReentrantLock();
    private BridgeExternalContext externalContext;
    private BridgeFacesContext facesContext;
    private PersistentFacesState persistentFacesState;
    private Map bundles = Collections.EMPTY_MAP;
    private Command currentCommand = NOOP;
    private String viewIdentifier;
    private ArrayList onPutListeners = new ArrayList();
    private ArrayList onTakeListeners = new ArrayList();
    private Collection viewListeners = new ArrayList();
    private String sessionID;
    private Configuration configuration;
    private SessionDispatcher.Monitor sessionMonitor;
    private ResourceDispatcher resourceDispatcher;
    private Runnable dispose;
    private Lifecycle lifecycle;

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

    private void acquireLifecycleLock() {
        if (!lifecycleLock.isHeldByCurrentThread()) {
            lifecycleLock.lock();
        }
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

    public void updateOnPageRequest(Request request) throws Exception {
        acquireLifecycleLock();
        request.detectEnvironment(new Request.Environment() {
            public void servlet(Object request, Object response) {
                if (differentURI((HttpServletRequest) request)) {
                    //page redirect
                    externalContext.dispose();
                    externalContext = new ServletExternalContext(viewIdentifier, request, response, View.this, configuration, sessionMonitor);
                    facesContext.dispose();
                    facesContext = new BridgeFacesContext(externalContext, viewIdentifier, sessionID, View.this, configuration, resourceDispatcher);
                    //reuse  PersistentFacesState instance when page redirects occur                    
                    persistentFacesState.setFacesContext(facesContext);
                } else {
                    //page reload
                    externalContext.updateOnPageLoad(request, response);
                }
            }

            public void portlet(Object request, Object response, Object config) {
                //page reload
                externalContext.updateOnPageLoad(request, response);
            }
        });
        makeCurrent();
    }

    /**
     * Check to see if the URI is different in any material (or Seam) way.
     *
     * @param request ServletRequest
     * @return true if the URI is considered different
     */
    public boolean differentURI(HttpServletRequest request) {
        // As a temporary fix, all GET requests are non-faces requests, and thus,
        // are considered different to force a new ViewRoot to be constructed.
        return SeamUtilities.isSeamEnvironment() ||
                !request.getRequestURI().equals(((HttpServletRequest) externalContext.getRequest()).getRequestURI());
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

    private void switchToNormalMode() {
        acquireLifecycleLock();
        facesContext.switchToNormalMode();
        externalContext.switchToNormalMode();
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

    public String toString() {
        return "View[" + sessionID + ":" + viewIdentifier + "]";
    }

    public void servePage(Request request) throws Exception {
        request.respondWith(responseHandler);
    }

    void preparePage(final ResponseHandler handler) {
        responseHandler = new ResponseHandler() {
            public void respond(Response response) throws Exception {
                handler.respond(response);
                responseHandler = lifecycleResponseHandler;
            }
        };
    }
}