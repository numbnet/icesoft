package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.context.View;
import com.icesoft.faces.util.event.servlet.ContextEventRepeater;
import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.command.SessionExpired;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.MimeTypeMatcher;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.OKResponse;
import com.icesoft.faces.webapp.http.common.standard.PathDispatcherServer;
import com.icesoft.faces.webapp.http.common.standard.ResponseHandlerServer;
import com.icesoft.faces.webapp.http.core.AsyncServerDetector;
import com.icesoft.faces.webapp.http.core.DisposeBeans;
import com.icesoft.faces.webapp.http.core.DisposeViews;
import com.icesoft.faces.webapp.http.core.MultiViewServer;
import com.icesoft.faces.webapp.http.core.ReceivePing;
import com.icesoft.faces.webapp.http.core.ReceiveSendUpdates;
import com.icesoft.faces.webapp.http.core.RequestVerifier;
import com.icesoft.faces.webapp.http.core.ResourceDispatcher;
import com.icesoft.faces.webapp.http.core.SendUpdates;
import com.icesoft.faces.webapp.http.core.SingleViewServer;
import com.icesoft.faces.webapp.http.core.UploadServer;
import com.icesoft.faces.webapp.http.core.ViewQueue;
import com.icesoft.faces.webapp.http.portlet.page.AssociatedPageViews;
import com.icesoft.faces.webapp.http.portlet.page.AssociatedPageViewsImpl;
import com.icesoft.net.messaging.MessageHandler;
import com.icesoft.net.messaging.MessageServiceClient;
import com.icesoft.util.IdGenerator;
import com.icesoft.util.MonitorRunner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

//todo: rename to MainSessionBoundServer and move in com.icesoft.faces.webapp.http.core
public class MainSessionBoundServlet implements Server {
    private static final Log Log = LogFactory.getLog(MainSessionBoundServlet.class);
    private static final String ResourcePrefix = "/block/resource/";
    private static final String ResourceRegex = ".*" + ResourcePrefix.replaceAll("\\/", "\\/") + ".*";
    private static final SessionExpired SessionExpired = new SessionExpired();
    private static final Server OKServer = new ResponseHandlerServer(OKResponse.Handler);
    private static final Runnable NOOP = new Runnable() {
        public void run() {
        }
    };
    private final Runnable drainUpdatedViews = new Runnable() {
        public void run() {
            allUpdatedViews.removeAll(synchronouslyUpdatedViews);
            if (!allUpdatedViews.isEmpty()) {
                Log.warn(allUpdatedViews + " views have accumulated updates");
            }
            allUpdatedViews.clear();
        }
    };
    private final Map views = Collections.synchronizedMap(new HashMap());
    private final ViewQueue allUpdatedViews = new ViewQueue();
    private final Collection synchronouslyUpdatedViews = new HashSet();
    private final PathDispatcherServer dispatcher = new PathDispatcherServer();
    private final String sessionID;
    private Runnable shutdown;

    public MainSessionBoundServlet(final HttpSession session, final SessionDispatcher.Monitor sessionMonitor, IdGenerator idGenerator, MimeTypeMatcher mimeTypeMatcher, MonitorRunner monitorRunner, Configuration configuration, final MessageServiceClient messageService) {
        sessionID = idGenerator.newIdentifier();
        ContextEventRepeater.iceFacesIdRetrieved(session, sessionID);
        final ResourceDispatcher resourceDispatcher = new ResourceDispatcher(ResourcePrefix, mimeTypeMatcher, sessionMonitor, configuration);
        final Server viewServlet;
        final Server disposeViews;
        final MessageHandler handler;
        if (configuration.getAttributeAsBoolean("concurrentDOMViews", false)) {
            final AssociatedPageViews associatedPageViews = AssociatedPageViewsImpl.getImplementation(configuration);
            viewServlet = new MultiViewServer(session,
                    sessionID,
                    sessionMonitor,
                    views,
                    allUpdatedViews,
                    configuration,
                    resourceDispatcher,
                    associatedPageViews);
            if (messageService == null) {
                disposeViews = new DisposeViews(sessionID, views, associatedPageViews);
                handler = null;
            } else {
                disposeViews = OKServer;
                handler = new DisposeViewsHandler();
                handler.setCallback(
                        new DisposeViewsHandler.Callback() {
                            public void disposeView(final String sessionIdentifier, final String viewIdentifier) {
                                if (sessionID.equals(sessionIdentifier)) {
                                    View view = (View) views.remove(viewIdentifier);
                                    if (view != null) {
                                        view.dispose();
                                    }
                                }
                            }
                        });
                messageService.addMessageHandler(handler, MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
            }
        } else {
            viewServlet = new SingleViewServer(session, sessionID, sessionMonitor, views, allUpdatedViews, configuration, resourceDispatcher);
            disposeViews = OKServer;
            handler = null;
        }

        final Server sendUpdatedViews;
        final Server sendUpdates;
        final Server receivePing;
        if (configuration.getAttributeAsBoolean("synchronousUpdate", false)) {
            //drain the updated views queue if in 'synchronous mode'
            allUpdatedViews.onPut(drainUpdatedViews);
            sendUpdatedViews = OKServer;
            sendUpdates = OKServer;
            receivePing = OKServer;
        } else {
            //setup blocking connection server
            sendUpdatedViews = new RequestVerifier(sessionID, new AsyncServerDetector(sessionID, synchronouslyUpdatedViews, allUpdatedViews, monitorRunner, configuration, messageService));
            sendUpdates = new RequestVerifier(sessionID, new SendUpdates(configuration, views));
            receivePing = new RequestVerifier(sessionID, new ReceivePing(views));
        }

        Server upload = new UploadServer(views, configuration);
        Server receiveSendUpdates = new RequestVerifier(sessionID, new ReceiveSendUpdates(views, synchronouslyUpdatedViews, sessionMonitor));

        dispatcher.dispatchOn(".*block\\/send\\-receive\\-updates$", receiveSendUpdates);
        dispatcher.dispatchOn(".*block\\/receive\\-updated\\-views$", sendUpdatedViews);
        dispatcher.dispatchOn(".*block\\/receive\\-updates$", sendUpdates);
        dispatcher.dispatchOn(".*block\\/ping$", receivePing);
        dispatcher.dispatchOn(".*block\\/dispose\\-views$", disposeViews);
        dispatcher.dispatchOn(ResourceRegex, resourceDispatcher);
        dispatcher.dispatchOn(".*uploadHtml", upload);
        dispatcher.dispatchOn(".*", viewServlet);
        shutdown = new Runnable() {
            public void run() {
                //avoid running shutdown more than once
                shutdown = NOOP;
                //dispose session scoped beans
                DisposeBeans.in(session);
                //send 'session-expired' to all views
                Iterator i = views.values().iterator();
                while (i.hasNext()) {
                    CommandQueue commandQueue = (CommandQueue) i.next();
                    commandQueue.put(SessionExpired);
                }
                ContextEventRepeater.iceFacesIdDisposed(session, sessionID);
                //shutdown all contained servers
                dispatcher.shutdown();
                //dispose all views
                Iterator viewIterator = views.values().iterator();
                while (viewIterator.hasNext()) {
                    View view = (View) viewIterator.next();
                    view.dispose();
                }

                if (handler != null) {
                    //todo: introduce NOOP handler
                    messageService.removeMessageHandler(handler, MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
                }
            }
        };
    }

    public void service(Request request) throws Exception {
        dispatcher.service(request);
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
}
