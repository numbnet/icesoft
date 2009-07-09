package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.Server;
import edu.emory.mathcs.backport.java.util.concurrent.Semaphore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

        public void respondWith(final ResponseHandler handler) throws Exception {
            try {
                super.respondWith(handler);
            } finally {
                if (semaphore == null) {
                    blockResponse = false;
                } else {
                    semaphore.release();
                }
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
