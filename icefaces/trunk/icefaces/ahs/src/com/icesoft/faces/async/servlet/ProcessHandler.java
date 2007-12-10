package com.icesoft.faces.async.servlet;

import com.icesoft.faces.async.common.AbstractHandler;
import com.icesoft.faces.async.common.ExecuteQueue;
import com.icesoft.faces.async.common.Handler;
import com.icesoft.faces.async.common.SequenceNumbers;
import com.icesoft.faces.async.common.SessionManager;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.standard.NotFoundHandler;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProcessHandler
extends AbstractHandler
implements Handler, Runnable {
    private static final int STATE_UNINITIALIZED = 0;
    private static final int STATE_PROCESSING_REQUEST = 1;
    private static final int STATE_WAITING_FOR_RESPONSE = 2;
    private static final int STATE_RESPONSE_IS_READY = 3;
    private static final int STATE_DONE = 4;

    private static final Log LOG = LogFactory.getLog(ProcessHandler.class);

    private final SessionManager sessionManager;
    private final Request request;

    private Set iceFacesIdSet;
    private SequenceNumbers sequenceNumbers;
    private List updatedViewsList;

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
                if (
                    _path == null ||
                    (
                        !_path.endsWith("/block/receive-updated-views") &&
                        !_path.endsWith("/block/receive-updated-views/")
                    )) {

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
                    return;
                }
                if (iceFacesIdSet.isEmpty()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                            "404 Not Found (ICEfaces ID(s))");
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
                    return;
                }
                extractSequenceNumbers();
                state = STATE_WAITING_FOR_RESPONSE;
            case STATE_WAITING_FOR_RESPONSE :
                if (LOG.isTraceEnabled()) {
                    LOG.trace("State: Waiting for Response");
                }
                updatedViewsList =
                    sessionManager.getUpdatedViewsManager().
                        pull(iceFacesIdSet, sequenceNumbers);
                if (updatedViewsList == null || updatedViewsList.isEmpty()) {
                    sessionManager.getRequestManager().
                        push(iceFacesIdSet, this);
                    return;
                }
                state = STATE_RESPONSE_IS_READY;
            case STATE_RESPONSE_IS_READY :
                if (LOG.isTraceEnabled()) {
                    LOG.trace("State: Response is Ready");
                }
                try {
                    request.respondWith(
                        new UpdatedViewsResponseHandler(
                            request, updatedViewsList));
                } catch (Exception exception) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(
                            "An error occurred while " +
                                "trying to response with: 200 OK!",
                            exception);
                    }
                }
                state = STATE_DONE;
            case STATE_DONE :
                if (LOG.isTraceEnabled()) {
                    LOG.trace("State: Done");
                }
                break;
            default :
                // this should never happen!
        }
    }

    private void extractSequenceNumbers() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Extracting Sequence Number(s)...");
        }
        sequenceNumbers =
            new SequenceNumbers(request.getHeaderAsStrings("X-Window-Cookie"));
        if (LOG.isDebugEnabled()) {
            LOG.debug("Sequence Number(s): " + sequenceNumbers);
        }
    }
}
