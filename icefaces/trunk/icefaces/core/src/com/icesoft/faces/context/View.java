package com.icesoft.faces.context;

import com.icesoft.faces.util.event.servlet.ContextEventRepeater;
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
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.lifecycle.Lifecycle;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
                facesContext.switchToNormalMode();
                LifecycleExecutor.getLifecycleExecutor(facesContext).apply(facesContext);
                facesContext.switchToPushMode();
            }
        };

        public void serve(Request request) throws Exception {
            if (facesContext != null) {
                facesContext.dispose();
            }
            facesContext = new BridgeFacesContext(request, viewIdentifier, sessionID, View.this, configuration, resourceDispatcher, sessionMonitor);
            makeCurrent();
            request.respondWith(lifecycleResponseHandler);
        }
    };
    private Page page = lifecycleExecutedPage;
    private ReentrantLock queueLock = new ReentrantLock();
    private ReentrantLock lifecycleLock = new ReentrantLock();
    private BridgeFacesContext facesContext;
    private PersistentFacesState persistentFacesState;
    private Command currentCommand = NOOP;
    private String viewIdentifier;
    private Collection viewListeners = new ArrayList();
    private String sessionID;
    private Configuration configuration;
    private SessionDispatcher.Monitor sessionMonitor;
    private ResourceDispatcher resourceDispatcher;
    private Runnable dispose;
    private Lifecycle lifecycle;
    private ViewQueue allServedViews;

    public View(final String viewIdentifier, String sessionID, HttpSession session, Request request, final ViewQueue allServedViews, final Configuration configuration, final SessionDispatcher.Monitor sessionMonitor, ResourceDispatcher resourceDispatcher, Lifecycle lifecycle) throws Exception {
        this.sessionID = sessionID;
        this.configuration = configuration;
        this.viewIdentifier = viewIdentifier;
        this.sessionMonitor = sessionMonitor;
        this.resourceDispatcher = resourceDispatcher;
        this.lifecycle = lifecycle;
        this.allServedViews = allServedViews;
        this.facesContext = new BridgeFacesContext(request, viewIdentifier, sessionID, this, configuration, resourceDispatcher, sessionMonitor);
        this.persistentFacesState = new PersistentFacesState(this, viewListeners, configuration);
        ContextEventRepeater.viewNumberRetrieved(session, sessionID, Integer.parseInt(viewIdentifier));
        //fail fast if environment cannot be detected
        dispose = new Runnable() {
            public void run() {
                //dispose view only once
                dispose = DoNothing;
                Log.debug("Disposing " + this);
                installThreadLocals();
                notifyViewDisposal();
                release();
                persistentFacesState.dispose();
                facesContext.dispose();
                allServedViews.remove(viewIdentifier);
            }
        };
        acquireLifecycleLock();
        Log.debug("Created " + this);
    }

    //this is the "postback" request
    public void processPostback(Request request) throws Exception {
        acquireLifecycleLock();
        facesContext.processPostback(request);
        makeCurrent();
    }

    //this is the page load request
    public void servePage(Request request) throws Exception {
        acquireLifecycleLock();
        page.serve(request);
    }

    public void put(Command command) {
        queueLock.lock();
        currentCommand = currentCommand.coalesceWith(command);
        queueLock.unlock();
        try {
            allServedViews.put(viewIdentifier);
        } catch (InterruptedException e) {
            Log.warn("Failed to queue updated view", e);
        }
    }

    public Command take() {
        queueLock.lock();
        Command command = currentCommand;
        currentCommand = NOOP;
        queueLock.unlock();
        return command;
    }

    public void release() {
        facesContext.release();
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

    public void acquireLifecycleLock() {
        lifecycleLock.lock();
    }

    public void releaseLifecycleLock() {
        //release all locks corresponding to current thread!
        for (int i = 0, count = lifecycleLock.getHoldCount(); i < count; i++) {
            lifecycleLock.unlock();
        }
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

    private void makeCurrent() {
        acquireLifecycleLock();
        installThreadLocals();
        facesContext.injectBundles();
        facesContext.applyBrowserDOMChanges();
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
