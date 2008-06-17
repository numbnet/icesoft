package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.command.Command;
import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.command.NOOP;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.FixedXMLContentHandler;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class SendUpdates implements Server {
    private static final Command NOOP = new NOOP();
    private Map commandQueues;

    public SendUpdates(Map commandQueues) {
        this.commandQueues = commandQueues;
    }

    public void service(final Request request) throws Exception {
        request.respondWith(new Handler(commandQueues, request));
    }

    public void shutdown() {
    }

    public static class Handler extends FixedXMLContentHandler {
        private final Request request;
        private Map commandQueues;

        public Handler(Map commandQueues, Request request) {
            this.commandQueues = commandQueues;
            this.request = request;
        }

        public void writeTo(Writer writer) throws IOException {
            String viewIdentifier = request.getParameter("ice.view");
            if (commandQueues.containsKey(viewIdentifier)) {
                CommandQueue queue = (CommandQueue) commandQueues.get(viewIdentifier);
                queue.take().serializeTo(writer);
            } else {
                NOOP.serializeTo(writer);
            }
        }
    }
}
