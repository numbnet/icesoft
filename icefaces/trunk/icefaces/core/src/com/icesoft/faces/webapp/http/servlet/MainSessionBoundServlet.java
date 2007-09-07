package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.context.View;
import com.icesoft.faces.util.event.servlet.ContextEventRepeater;
import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.command.SessionExpired;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.CompressingServer;
import com.icesoft.faces.webapp.http.common.standard.OKHandler;
import com.icesoft.faces.webapp.http.common.standard.PathDispatcherServer;
import com.icesoft.faces.webapp.http.core.AsyncServerDetector;
import com.icesoft.faces.webapp.http.core.DisposeViews;
import com.icesoft.faces.webapp.http.core.IDVerifier;
import com.icesoft.faces.webapp.http.core.MultiViewServer;
import com.icesoft.faces.webapp.http.core.ReceivePing;
import com.icesoft.faces.webapp.http.core.ReceiveSendUpdates;
import com.icesoft.faces.webapp.http.core.SendUpdates;
import com.icesoft.faces.webapp.http.core.SingleViewServer;
import com.icesoft.faces.webapp.http.core.UploadServer;
import com.icesoft.faces.webapp.http.core.ViewBoundServer;
import com.icesoft.faces.webapp.http.core.ViewQueue;
import com.icesoft.util.IdGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class MainSessionBoundServlet implements PseudoServlet {
    private static final Log Log = LogFactory.getLog(MainSessionBoundServlet.class);
    private static final SessionExpired SessionExpired = new SessionExpired();
    private static final Server NOOPServer = new Server() {
        public void service(Request request) throws Exception {
            request.respondWith(OKHandler.HANDLER);
        }

        public void shutdown() {
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
    private Map views = new HashMap();
    private ViewQueue allUpdatedViews = new ViewQueue();
    private Collection synchronouslyUpdatedViews = new HashSet();
    private String sessionID;
    private PseudoServlet servlet;
    private HttpSession session;

    public MainSessionBoundServlet(HttpSession session, SessionDispatcher.Listener.Monitor sessionMonitor, IdGenerator idGenerator, Configuration configuration) {
        this.session = session;
        sessionID = idGenerator.newIdentifier();
        ContextEventRepeater.iceFacesIdRetrieved(session, sessionID);

        final PathDispatcherServer resourceDispatcher = new PathDispatcherServer();
        final Server viewServlet;
        final Server disposeViews;
        if (configuration.getAttributeAsBoolean("concurrentDOMViews", false)) {
            viewServlet = new MultiViewServer(session, sessionID, sessionMonitor, views, allUpdatedViews, configuration, resourceDispatcher);
            disposeViews = new IDVerifier(sessionID, new DisposeViews(views));
        } else {
            viewServlet = new SingleViewServer(session, sessionID, sessionMonitor, views, allUpdatedViews, configuration, resourceDispatcher);
            disposeViews = NOOPServer;
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
            sendUpdatedViews = new IDVerifier(sessionID, new AsyncServerDetector(sessionID, synchronouslyUpdatedViews, allUpdatedViews, session.getServletContext(), configuration));
            sendUpdates = new IDVerifier(sessionID, new SendUpdates(views));
            receivePing = new IDVerifier(sessionID, new ReceivePing(views));
        }

        Server upload = new UploadServer(views, configuration);
        Server receiveSendUpdates = new ViewBoundServer(new IDVerifier(sessionID, new ReceiveSendUpdates(views, synchronouslyUpdatedViews)), sessionMonitor, views);

        PathDispatcherServer dispatcher = new PathDispatcherServer();
        dispatcher.dispatchOn(".*block\\/send\\-receive\\-updates$", receiveSendUpdates);
        dispatcher.dispatchOn(".*block\\/receive\\-updated\\-views$", sendUpdatedViews);
        dispatcher.dispatchOn(".*block\\/receive\\-updates$", sendUpdates);
        dispatcher.dispatchOn(".*block\\/ping$", receivePing);
        dispatcher.dispatchOn(".*block\\/dispose\\-views$", disposeViews);
        dispatcher.dispatchOn(".*block\\/resource\\/.*", new CompressingServer(resourceDispatcher));
        dispatcher.dispatchOn(".*uploadHtml", upload);
        dispatcher.dispatchOn(".*", viewServlet);
        servlet = new EnvironmentAdaptingServlet(dispatcher, configuration);
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        servlet.service(request, response);
    }

    public void shutdown() {
        Iterator i = views.values().iterator();
        while (i.hasNext()) {
            CommandQueue commandQueue = (CommandQueue) i.next();
            commandQueue.put(SessionExpired);
        }
        ContextEventRepeater.iceFacesIdDisposed(session, sessionID);
        try {
            //wait for the for the bridge to receive the 'session-expire' command
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //do nothing
        } finally {
            servlet.shutdown();
        }

        Iterator viewIterator = views.values().iterator();
        while (viewIterator.hasNext()) {
            View view = (View) viewIterator.next();
            view.dispose();
        }
    }

    //Exposing queues for Tomcat 6 Ajax Push
    public ViewQueue getAllUpdatedViews() {
        return allUpdatedViews;
    }

    public Collection getSynchronouslyUpdatedViews() {
        return synchronouslyUpdatedViews;
    }
}
