package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.context.View;
import com.icesoft.faces.util.event.servlet.ContextEventRepeater;
import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.command.SessionExpired;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.MimeTypeMatcher;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.ServerProxy;
import com.icesoft.faces.webapp.http.common.standard.OKHandler;
import com.icesoft.faces.webapp.http.common.standard.PathDispatcherServer;
import com.icesoft.faces.webapp.http.core.*;
import com.icesoft.util.IdGenerator;
import com.icesoft.util.MonitorRunner;
import edu.emory.mathcs.backport.java.util.concurrent.Semaphore;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class MainSessionBoundServlet implements PseudoServlet {
    private static final String ResourcePrefix = "/block/resource/";
    private static final String ResourceRegex = ".*" + ResourcePrefix.replaceAll("\\/", "\\/") + ".*";
    private static final Log Log = LogFactory.getLog(MainSessionBoundServlet.class);
    private static final SessionExpired SessionExpired = new SessionExpired();
    private static final Server NOOPServer = new Server() {
        public void service(Request request) throws Exception {
            request.respondWith(OKHandler.HANDLER);
        }

        public void shutdown() {
        }
    };
    private static final Runnable NOOP = new Runnable() {
        public void run() {
        }
    };
    private Runnable drainUpdatedViews = new Runnable() {
        public void run() {
            allUpdatedViews.removeAll(synchronouslyUpdatedViews);
            if (!allUpdatedViews.isEmpty()) {
                Log.warn(allUpdatedViews + " views have accumulated updates");
            }
            allUpdatedViews.clear();
        }
    };
    private Map views = Collections.synchronizedMap(new HashMap());
    private ViewQueue allUpdatedViews = new ViewQueue();
    private Collection synchronouslyUpdatedViews = new HashSet();
    private String sessionID;
    private PseudoServlet servlet;
    private ShutdownHook disposeViews;
    private Runnable shutdown;

    public MainSessionBoundServlet(final HttpSession session, final SessionDispatcher.Monitor sessionMonitor, IdGenerator idGenerator, MimeTypeMatcher mimeTypeMatcher, MonitorRunner monitorRunner, Configuration configuration) {
        sessionID = idGenerator.newIdentifier();
        ContextEventRepeater.iceFacesIdRetrieved(session, sessionID);

        final ResourceDispatcher resourceDispatcher = new ResourceDispatcher(ResourcePrefix, mimeTypeMatcher, sessionMonitor);
        final Server viewServlet;
        if (configuration.getAttributeAsBoolean("concurrentDOMViews", false)) {
            viewServlet = new MultiViewServer(session, sessionID, sessionMonitor, views, allUpdatedViews, configuration, resourceDispatcher);
            disposeViews = new ShutdownHook(new DisposeViews(sessionID, views));
        } else {
            viewServlet = new SingleViewServer(session, sessionID, sessionMonitor, views, allUpdatedViews, configuration, resourceDispatcher);
            disposeViews = new ShutdownHook(NOOPServer);
        }

        final Server sendUpdatedViews;
        final Server sendUpdates;
        final Server receivePing;
        if (configuration.getAttributeAsBoolean("synchronousUpdate", false)) {
            //drain the updated views queue if in 'synchronous mode'
            allUpdatedViews.onPut(drainUpdatedViews);
            sendUpdatedViews = NOOPServer;
            sendUpdates = NOOPServer;
            receivePing = NOOPServer;
        } else {
            //setup blocking connection server
            sendUpdatedViews = new RequestVerifier(sessionID, new AsyncServerDetector(sessionID, synchronouslyUpdatedViews, allUpdatedViews, session.getServletContext(), monitorRunner, configuration));
            sendUpdates = new RequestVerifier(sessionID, new SendUpdates(views));
            receivePing = new RequestVerifier(sessionID, new ReceivePing(views));
        }

        Server upload = new UploadServer(views, configuration);
        Server receiveSendUpdates = new RequestVerifier(sessionID, new ViewBoundServer(new ReceiveSendUpdates(views, synchronouslyUpdatedViews), sessionMonitor, views));

        PathDispatcherServer dispatcher = new PathDispatcherServer();
        dispatcher.dispatchOn(".*block\\/send\\-receive\\-updates$", receiveSendUpdates);
        dispatcher.dispatchOn(".*block\\/receive\\-updated\\-views$", sendUpdatedViews);
        dispatcher.dispatchOn(".*block\\/receive\\-updates$", sendUpdates);
        dispatcher.dispatchOn(".*block\\/ping$", receivePing);
        dispatcher.dispatchOn(".*block\\/dispose\\-views$", disposeViews);
        dispatcher.dispatchOn(ResourceRegex, resourceDispatcher);
        dispatcher.dispatchOn(".*uploadHtml", upload);
        dispatcher.dispatchOn(".*", viewServlet);
        servlet = new EnvironmentAdaptingServlet(dispatcher, configuration, session.getServletContext());
        shutdown = new Runnable() {
            public void run() {
                //avoid running shutdown more than once
                shutdown = NOOP;
                //send 'session-expired' to all views
                Iterator i = views.values().iterator();
                while (i.hasNext()) {
                    CommandQueue commandQueue = (CommandQueue) i.next();
                    commandQueue.put(SessionExpired);
                }
                //block until all views notify their disposal only when session expires
                if (sessionMonitor.isExpired()) {
                    disposeViews.waitForViewsShutdown();
                }
                //dispose session scoped beans
                DisposeBeans.in(session);
                ContextEventRepeater.iceFacesIdDisposed(session, sessionID);
                //shutdown all contained servers
                servlet.shutdown();
                //dispose all views
                Iterator viewIterator = views.values().iterator();
                while (viewIterator.hasNext()) {
                    View view = (View) viewIterator.next();
                    view.dispose();
                }
            }
        };
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        servlet.service(request, response);
    }

    public void shutdown() {
        shutdown.run();
    }

    public Map getViews() {
        return views;
    }

    //Exposing queues for Tomcat 6 Ajax Push
    public ViewQueue getAllUpdatedViews() {
        return allUpdatedViews;
    }

    public Collection getSynchronouslyUpdatedViews() {
        return synchronouslyUpdatedViews;
    }

    public String getSessionID() {
        return sessionID;
    }

    private class ShutdownHook extends ServerProxy {
        public ShutdownHook(Server server) {
            super(server);
        }

        public void waitForViewsShutdown() {
            int size = views.size();
            final Semaphore lock = new Semaphore(size, true);

            server = new ServerProxy(server) {
                public void service(Request request) throws Exception {
                    lock.release();
                    super.service(request);
                }
            };

            try {
                lock.acquire(size);
            } catch (InterruptedException e) {
                //do nothing
            }

            //block until all views send their "dispose-views" message 
            try {
                lock.tryAcquire(size, 15, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Log.warn("Some views failed to confirm their shutdown.");
            }
        }
    }
}
