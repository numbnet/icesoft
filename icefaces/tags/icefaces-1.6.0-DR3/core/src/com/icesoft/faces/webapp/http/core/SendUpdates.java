package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.FixedXMLContentHandler;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class SendUpdates implements Server {
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
            Iterator viewIdentifiers = new HashSet(Arrays.asList(request.getParameterAsStrings("viewNumber"))).iterator();
            while (viewIdentifiers.hasNext()) {
                Object viewIdentifier = viewIdentifiers.next();
                if (commandQueues.containsKey(viewIdentifier)) {
                    CommandQueue queue = (CommandQueue) commandQueues.get(viewIdentifier);
                    queue.take().serializeTo(writer);
                }
            }
        }
    }
}
