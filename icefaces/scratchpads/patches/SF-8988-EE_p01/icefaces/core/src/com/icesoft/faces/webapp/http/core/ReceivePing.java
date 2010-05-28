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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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

package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.command.Pong;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.Server;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class ReceivePing implements Server {
    private static final Log LOG = LogFactory.getLog(ReceivePing.class);
    private static final Pong PONG = new Pong();
    private static final ResponseHandler CLOSE_RESPONSE = new ResponseHandler() {
        public void respond(Response response) throws Exception {
            //let the bridge know that this blocking connection should not be re-initialized
            response.setHeader("X-Connection", "close");
            response.setHeader("Content-Length", 0);
        }
    };
    private Map commandQueues;
    private PageTest pageTest;

    public ReceivePing(final Map commandQueues, final PageTest pageTest) {
        this.commandQueues = commandQueues;
        this.pageTest = pageTest;
    }

    public void service(final Request request) throws Exception {
        if (!pageTest.isLoaded()) {
            request.respondWith(new ReloadResponse(""));
        } else {
            String viewIdentifier = request.getParameter("ice.view");
            if (ViewIdVerifier.isValid(viewIdentifier)) {
                CommandQueue queue = (CommandQueue) commandQueues.get(viewIdentifier);
                if (queue != null) {
                    queue.put(PONG);
                } else {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("could not get a valid queue for " + viewIdentifier);
                    }
                }
            }
            request.respondWith(NOOPResponse.Handler);
        }
    }

    public void shutdown() {
    }
}
