/*
 * Version: MPL 1.1
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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icefaces.push.servlet;

import org.icefaces.push.http.ResponseHandler;
import org.icefaces.push.http.Server;
import org.mortbay.util.ajax.Continuation;
import org.mortbay.util.ajax.ContinuationSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class JettyAdaptingServlet implements PseudoServlet {
    private Map requests = new HashMap();
    private Server server;

    public JettyAdaptingServlet(final Server server) {
        this.server = server;
    }

    public void service(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (requests.containsKey(request)) {
            ResponseHandler handler = (ResponseHandler) requests.remove(request);
            handler.respond(new ServletRequestResponse(request, response));
        } else {
            ContinuationRequestResponse requestResponse = new ContinuationRequestResponse(request, response);
            server.service(requestResponse);
            requestResponse.captureContinuation();
        }
    }

    public void shutdown() {
        server.shutdown();
    }

    private class ContinuationRequestResponse extends ServletRequestResponse {
        private boolean captureContinuation = true;
        private Continuation continuation;

        public ContinuationRequestResponse(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
            super(request, response);
        }

        public synchronized void respondWith(final ResponseHandler handler) throws Exception {
            if (continuation == null) {
                captureContinuation = false;
                super.respondWith(handler);
            } else {
                requests.put(request, handler);
                continuation.resume();
            }
        }

        public synchronized void captureContinuation() {
            if (captureContinuation) {
                continuation = ContinuationSupport.getContinuation(request, this);
                continuation.suspend(request.getSession().getMaxInactiveInterval() * 1000);
            }
        }
    }
}
