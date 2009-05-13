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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Semaphore;

public class ThreadBlockingAdaptingServlet implements PseudoServlet {
    private Server server;

    public ThreadBlockingAdaptingServlet(Server server) {
        this.server = server;
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThreadBlockingRequestResponse requestResponse = new ThreadBlockingRequestResponse(request, response);
        server.service(requestResponse);
        requestResponse.blockUntilRespond();
    }

    public void shutdown() {
        server.shutdown();
    }

    private class ThreadBlockingRequestResponse extends ServletRequestResponse {
        private boolean blockResponse = true;
        private Semaphore semaphore;

        public ThreadBlockingRequestResponse(HttpServletRequest request, HttpServletResponse response) throws Exception {
            super(request, response);
        }

        public void respondWith(ResponseHandler handler) throws Exception {
            super.respondWith(handler);
            if (semaphore == null) {
                blockResponse = false;
            } else {
                semaphore.release();
            }
        }

        public void blockUntilRespond() throws InterruptedException {
            if (blockResponse) {
                semaphore = new Semaphore(1);
                semaphore.acquire();
                semaphore.acquire();
                semaphore.release();
            }
        }
    }
}
