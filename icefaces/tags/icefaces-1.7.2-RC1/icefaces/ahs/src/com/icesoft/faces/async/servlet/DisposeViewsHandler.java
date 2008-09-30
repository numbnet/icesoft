package com.icesoft.faces.async.servlet;

import com.icesoft.faces.async.common.AbstractHandler;
import com.icesoft.faces.async.common.ExecuteQueue;
import com.icesoft.faces.async.common.Handler;
import com.icesoft.faces.async.common.SessionManager;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.net.messaging.MessageServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DisposeViewsHandler
        extends AbstractHandler
        implements Handler, Runnable {
    private static final int STATE_UNINITIALIZED = 0;
    private static final int STATE_PROCESSING_REQUEST = 1;
    private static final int STATE_DONE = 2;

    private static final ResponseHandler EMPTY_RESPONSE_HANDLER =
            new ResponseHandler() {
                public void respond(final Response response)
                        throws Exception {
                    response.setStatus(200);
                    // general header fields
                    response.setHeader("Pragma", "no-cache");
                    response.setHeader("Cache-Control", "no-cache, no-store");
                    // entity header fields
                    response.setHeader("Content-Length", 0);
                    response.setHeader("Content-Type", "text/xml");
                }
            };

    private static final String DISPOSE_VIEWS_MESSAGE_TYPE = "DisposeViews";

    private static final Log LOG = LogFactory.getLog(ProcessHandler.class);

    private final SessionManager sessionManager;
    private final Request request;

    private int state = STATE_UNINITIALIZED;

    public DisposeViewsHandler(
            final Request request, final SessionManager sessionManager,
            final ExecuteQueue executeQueue)
            throws IllegalArgumentException {
        super(executeQueue);
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
        if (sessionManager == null) {
            throw new IllegalArgumentException("sessionManager is null");
        }
        this.request = request;
        this.sessionManager = sessionManager;
    }

    public void run() {
        switch (state) {
            case STATE_UNINITIALIZED:
                if (LOG.isTraceEnabled()) {
                    LOG.trace("State: Uninitialized");
                }
                state = STATE_PROCESSING_REQUEST;
            case STATE_PROCESSING_REQUEST:
                if (LOG.isTraceEnabled()) {
                    LOG.trace("State: Processing Request");
                }
                // Parameter Name  : ice.views
                // Parameter Value : <ICEfaces ID>:<View Number>,
                //                   <ICEfaces ID>:<View Number>, etc.
                StringBuffer _message = new StringBuffer();
                String[] _iceSessions = request.getParameterNames();
                for (int i = 0; i < _iceSessions.length; i++) {
                    String[] _iceViews = request.getParameterAsStrings(_iceSessions[i]);
                    for (int j = 0; j < _iceViews.length; j++) {
                        _message.
                                // ICEfaces ID
                                        append(_iceSessions[i]).append(";").
                                // View Number
                                        append(_iceViews[j]).append("\r\n");
                    }
                }
                sessionManager.getMessageService().publish(
                        _message.toString(),
                        DISPOSE_VIEWS_MESSAGE_TYPE,
                        MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
                try {
                    request.respondWith(EMPTY_RESPONSE_HANDLER);
                } catch (Exception exception) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(
                                "An error occurred while " +
                                        "trying to response with: 200 OK!",
                                exception);
                    }
                }
                state = STATE_DONE;
            case STATE_DONE:
                if (LOG.isTraceEnabled()) {
                    LOG.trace("State: Done");
                }
                break;
            default:
                // this should never happen!
        }
    }
}
