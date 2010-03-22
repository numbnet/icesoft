package com.icesoft.faces.async.servlet;

import com.icesoft.faces.async.common.ExecuteQueue;
import com.icesoft.faces.async.common.SessionManager;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;
import java.util.Set;

public class SendUpdatedViewsServer
        implements Server {
    private static final Log LOG =
            LogFactory.getLog(SendUpdatedViewsServer.class);

    private final SessionManager sessionManager;
    private final ExecuteQueue executeQueue;
    private final SessionDispatcher.Monitor monitor;

    public SendUpdatedViewsServer(
            final SessionManager sessionManager, final ExecuteQueue executeQueue,
            final SessionDispatcher.Monitor monitor)
            throws IllegalArgumentException {
        if (sessionManager == null) {
            throw new IllegalArgumentException("sessionManager is null");
        }
        if (executeQueue == null) {
            throw new IllegalArgumentException("executeQueue is null");
        }
        if (monitor == null) {
            throw new IllegalArgumentException("monitor is null");
        }
        this.sessionManager = sessionManager;
        this.executeQueue = executeQueue;
        this.monitor = monitor;
    }

    public void service(final Request request)
            throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Extracting ICEfaces ID(s)...");
        }
        Set _iceFacesIdSet = new HashSet();
        try {
            String[] _iceFacesIds =
                    request.getParameterAsStrings("ice.session");
            for (int i = 0; i < _iceFacesIds.length; i++) {
                if (sessionManager.isValid(_iceFacesIds[i])) {
                    _iceFacesIdSet.add(_iceFacesIds[i]);
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                                "Invalid ICEfaces ID: " +
                                        _iceFacesIds[i] + ")");
                    }
                }
            }
        } catch (Exception e) {
            //todo: remove this
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("ICEfaces ID(s): " + _iceFacesIdSet);
        }
        monitor.touchSession();
        new ProcessHandler(
                request, _iceFacesIdSet, sessionManager, executeQueue).handle();
    }

    public void shutdown() {
        // do nothing.
    }
}
