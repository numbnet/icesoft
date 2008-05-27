package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.command.Command;
import com.icesoft.faces.webapp.command.SessionExpired;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.FixedXMLContentHandler;
import com.icesoft.faces.webapp.http.common.standard.PathDispatcherServer;
import com.icesoft.faces.webapp.http.common.standard.ResponseHandlerServer;

import java.io.IOException;
import java.io.Writer;

public class SessionExpiredServer implements Server {
    private static final Command SessionExpired = new SessionExpired();
    private static final FixedXMLContentHandler XMLHttpRequestHandler = new FixedXMLContentHandler() {
        public void writeTo(Writer writer) throws IOException {
            SessionExpired.serializeTo(writer);
        }
    };
    private static final ResponseHandler OtherRequestHandler = new ResponseHandler() {
        public void respond(Response response) throws Exception {
            throw new SessionExpiredException();
        }
    };
    private PathDispatcherServer dispatcher = new PathDispatcherServer();

    public SessionExpiredServer() {
        dispatcher.dispatchOn("block\\/", new ResponseHandlerServer(XMLHttpRequestHandler));
        dispatcher.dispatchOn(".*", new ResponseHandlerServer(OtherRequestHandler));
    }

    public void service(Request request) throws Exception {
        dispatcher.service(request);
    }

    public void shutdown() {
        dispatcher.shutdown();
    }
}
