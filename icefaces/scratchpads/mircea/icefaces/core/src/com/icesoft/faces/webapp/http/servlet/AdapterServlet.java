package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.Server;
import edu.emory.mathcs.backport.java.util.concurrent.Semaphore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdapterServlet implements ServerServlet {
    private Server server;

    public AdapterServlet(Server server) {
        this.server = server;
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Semaphore lock = new Semaphore(1);
        lock.acquire();
        server.service(new ServletRequestResponse(request, response) {
            public void respondWith(ResponseHandler handler) throws Exception {
                lock.release();
                super.respondWith(handler);
            }
        });
        lock.acquire();
        lock.release();
    }

    public void shutdown() {
        server.shutdown();
    }
}
