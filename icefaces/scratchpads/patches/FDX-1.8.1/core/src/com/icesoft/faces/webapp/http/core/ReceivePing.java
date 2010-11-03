package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.command.Pong;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReceivePing implements Server {
    private static final Log LOG = LogFactory.getLog(ReceivePing.class);
    private static final Pong PONG = new Pong();
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
            CommandQueue queue = (CommandQueue) commandQueues.get(viewIdentifier);
            if (queue != null) {
                queue.put(PONG);
                request.respondWith(NOOPResponse.Handler);
            } else {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("could not get a valid queue for " + viewIdentifier);
                }
            }
        }
    }

    public void shutdown() {
    }
}
