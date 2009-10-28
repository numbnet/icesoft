package org.icepush;

import org.icepush.http.Response;
import org.icepush.http.ResponseHandler;
import org.icepush.http.standard.ResponseHandlerServer;

import java.io.InputStream;

public class CodeServer extends ResponseHandlerServer {
    public CodeServer() {
        super(new ResponseHandler() {
            public void respond(Response response) throws Exception {
                InputStream in = CodeServer.class.getResourceAsStream("/icepush.js");
                response.setHeader("Content-Type", "text/javascript");
                response.writeBodyFrom(in);
            }
        });
    }
}