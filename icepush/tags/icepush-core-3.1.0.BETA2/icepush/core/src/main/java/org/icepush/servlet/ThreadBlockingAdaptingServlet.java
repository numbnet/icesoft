/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 */
package org.icepush.servlet;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletRequest;

import org.icepush.Configuration;
import org.icepush.http.ResponseHandler;
import org.icepush.http.Server;

public class ThreadBlockingAdaptingServlet implements PseudoServlet {
    private static final Logger LOG = Logger.getLogger(ThreadBlockingAdaptingServlet.class.getName());
    private static final int TIMEOUT = 600; // seconds

    private Server server;
    private Configuration configuration;

    public ThreadBlockingAdaptingServlet(final Server server, final Configuration configuration) {
        this.server = server;
        this.configuration = configuration;
    }

    public void service(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ThreadBlockingRequestResponse requestResponse = new ThreadBlockingRequestResponse(request, response, configuration);
        server.service(requestResponse);
        requestResponse.blockUntilRespond();
    }

    public void shutdown() {
        server.shutdown();
    }

    private class ThreadBlockingRequestResponse extends ServletRequestResponse {
        private final Semaphore semaphore;

        public ThreadBlockingRequestResponse(final HttpServletRequest request, final HttpServletResponse response, final Configuration configuration) throws Exception {
            super(request, response, configuration);
            semaphore = new Semaphore(1);
            //Acquire semaphore hoping to have it released by a call to respondWith() method.
            semaphore.acquire();
        }

        public void respondWith(final ResponseHandler handler) throws Exception {
            try {
                super.respondWith(handler);
            } finally {
                semaphore.release();
            }
        }

        public void blockUntilRespond() throws InterruptedException {
            //Block thread by trying to acquire the semaphore a second time.
            boolean acquired = semaphore.tryAcquire(TIMEOUT, TimeUnit.SECONDS);
            if (acquired) {
                //Release the semaphore previously acquired.
                semaphore.release();
            } else {
                String remoteAddress = "unknown";
                if( request instanceof ServletRequest ){
                    remoteAddress = ((ServletRequest)request).getRemoteAddr();
                }
                LOG.warning("No response sent to " +
                        "request '" + request.getRequestURI() + "' " +
                        "with ICEfaces ID '" +
                        request.getParameter("ice.session") + "' " +
                        "from " + remoteAddress + " " +
                        "in " + TIMEOUT + " minutes.  " +
                        "Unblocking " +
                        "thread '" + Thread.currentThread().getName() + "'.");
                //Release the semaphore; most probably respondWith() method was not invoked.
                semaphore.release();
            }
        }
    }
}
