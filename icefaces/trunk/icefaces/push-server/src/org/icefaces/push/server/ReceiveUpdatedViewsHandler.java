/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */
package org.icefaces.push.server;

import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.standard.NotFoundHandler;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReceiveUpdatedViewsHandler
extends AbstractHandler
implements Handler, Runnable {
    private static final int STATE_UNINITIALIZED = 0;
    private static final int STATE_PROCESSING_REQUEST = 1;
    private static final int STATE_WAITING_FOR_RESPONSE = 2;
    private static final int STATE_RESPONSE_IS_READY = 3;
    private static final int STATE_DONE = 4;

    private static final Log LOG =
        LogFactory.getLog(ReceiveUpdatedViewsHandler.class);

    private static final ResponseHandler CLOSE_RESPONSE_HANDLER =
        new ResponseHandler() {
            public void respond(Response response) throws Exception {
                /*
                 * let the bridge know that this blocking connection should not
                 * be re-initialized...
                 */
                response.setStatus(200);
                // entity header fields
                response.setHeader("Content-Length", 0);
                // extension header fields
                response.setHeader("X-Connection", "close");
            }
        };

    private final SessionManager sessionManager;
    private final Request request;

    private Set iceFacesIdSet;
    private List updatedViewsList;
    private SequenceNumbers sequenceNumbers;
    
    private int state = STATE_UNINITIALIZED;

    public ReceiveUpdatedViewsHandler(
        final Request request, final Set iceFacesIdSet,
        final SessionManager sessionManager, final ExecuteQueue executeQueue) {

        super(executeQueue);
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
                    state = STATE_DONE;
                    return;
                }
                extractSequenceNumbers();
                // checking pending request...
                ReceiveUpdatedViewsHandler _receiveUpdatedViewsHandler =
                    (ReceiveUpdatedViewsHandler)
                        sessionManager.getRequestManager().
                            pull(iceFacesIdSet);
                if (_receiveUpdatedViewsHandler != null) {
                    // respond to pending request.
                    try {
                        _receiveUpdatedViewsHandler.request.respondWith(
                            CLOSE_RESPONSE_HANDLER);
                    } catch (Exception exception) {
                        if (LOG.isErrorEnabled()) {
                            LOG.error(
                                "An error occurred while " +
                                    "trying to response with: 200 OK!",
                                exception);
                        }
                    }
                }
                state = STATE_WAITING_FOR_RESPONSE;
            case STATE_WAITING_FOR_RESPONSE :
                if (LOG.isTraceEnabled()) {
                    LOG.trace("State: Waiting for Response");
                }
                if (sessionManager.isValid(iceFacesIdSet)) {
                    updatedViewsList =
                        sessionManager.getUpdatedViewsManager().
                            pull(iceFacesIdSet, sequenceNumbers);
                    if (updatedViewsList == null || updatedViewsList.isEmpty()) {
                        sessionManager.getRequestManager().
                            push(iceFacesIdSet, this);
                        return;
                    }
                    state = STATE_RESPONSE_IS_READY;
                } else {
                    try {
                        request.respondWith(CLOSE_RESPONSE_HANDLER);
                    } catch (Exception exception) {
                        if (LOG.isErrorEnabled()) {
                            LOG.error(
                                "An error occurred while " +
                                    "trying to responde with: 200 OK (close)",
                                exception);
                        }
                    }
                    state = STATE_DONE;
                    return;
                }
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
