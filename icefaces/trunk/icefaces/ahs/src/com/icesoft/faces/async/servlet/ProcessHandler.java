package com.icesoft.faces.async.servlet;

import com.icesoft.faces.async.common.AbstractHandler;
import com.icesoft.faces.async.common.ExecuteQueue;
import com.icesoft.faces.async.common.Handler;
import com.icesoft.faces.async.common.SessionManager;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.standard.NotFoundHandler;

import java.net.URI;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProcessHandler
extends AbstractHandler
implements Handler, Runnable {
    private static final int STATE_UNINITIALIZED = 0;
    private static final int STATE_PROCESSING_REQUEST = 1;
    private static final int STATE_DONE = 2;

    private static final Log LOG = LogFactory.getLog(ProcessHandler.class);

    private final SessionManager sessionManager;
    private final Request request;

    private Set iceFacesIdSet;

    private int state = STATE_UNINITIALIZED;

    public ProcessHandler(
        final Request request, final Set iceFacesIdSet,
        final SessionManager sessionManager, final ExecuteQueue executeQueue)
    throws IllegalArgumentException {
        super(executeQueue);
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
        if (sessionManager == null) {
            throw new IllegalArgumentException("sessionManager is null");
        }
        this.request = request;
        this.iceFacesIdSet = iceFacesIdSet;
        this.sessionManager = sessionManager;
    }

    public void run() {
        switch (state) {
            case STATE_UNINITIALIZED :
                if (LOG.isTraceEnabled()) {
                    LOG.trace("State: Uninitialized");
                }
                state = STATE_PROCESSING_REQUEST;
            case STATE_PROCESSING_REQUEST :
                if (LOG.isTraceEnabled()) {
                    LOG.trace("State: Processing Request");
                }
                URI _requestUri = request.getURI();
                String _path = _requestUri.getPath();
                // There are now two type of HTTP requests to be handled by AHS:
                //
                // - .../block/receive-updated-views (the old blocking request)
                // - .../block/dispose-views (the new non-blocking request)
                if (_path != null && (
                        _path.endsWith("/block/receive-updated-views") ||
                        _path.endsWith("/block/receive-updated-views/"))) {

                    new ReceiveUpdatedViewsHandler(
                        request, iceFacesIdSet, sessionManager, executeQueue).
                            handle();
                    state = STATE_DONE;
                } else if (
                    _path != null && (
                        _path.endsWith("/block/dispose-views") ||
                        _path.endsWith("/block/dispose-views/"))) {

                    new DisposeViewsHandler(
                        request, sessionManager, executeQueue).
                            handle();
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                            "404 Not Found (Request-URI: " + _requestUri + ")");
                    }
                    try {
                        request.respondWith(new NotFoundHandler(""));
                    } catch (Exception exception) {
                        if (LOG.isErrorEnabled()) {
                            LOG.error(
                                "An error occurred " +
                                    "while trying to respond with: " +
                                        "404 Not Found!",
                                exception);
                        }
                    }
                    state = STATE_DONE;
                    return;
                }
            case STATE_DONE :
                if (LOG.isTraceEnabled()) {
                    LOG.trace("State: Done");
                }
                break;
            default :
                // this should never happen!
        }
    }
}
