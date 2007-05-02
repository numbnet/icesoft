package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.standard.NotFoundHandler;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.SequenceInputStream;

public class ServeBootstrapJSCode implements Server {
    private static final String Package = "com/icesoft/faces/webapp/xmlhttp/";
    private ClassLoader loader;

    public ServeBootstrapJSCode() {
        loader = this.getClass().getClassLoader();
    }

    public void service(Request request) throws Exception {
        String path = request.getURI().getPath();

        String contextName = path.substring(0, path.indexOf("/", 1));//first atom in the resource!

        final InputStream bridgeURL = new ByteArrayInputStream((
                "var bridgeURL = '" + contextName + "/xmlhttp/icefaces-d2d.js';\n").getBytes());

        final InputStream in = this.getClass().getClassLoader().getResourceAsStream("com/icesoft/faces/webapp/xmlhttp/icefaces-bootstrap.js");

        if (in == null) {
            request.respondWith(NotFoundHandler.HANDLER);
        } else {
            request.respondWith(new ResponseHandler() {
                public void respond(Response response) throws Exception {
                    response.setHeader("Content-Type", "text/javascript");
                    response.writeBodyFrom(new SequenceInputStream(bridgeURL, in));
                }
            });
        }
    }

    public void shutdown() {
    }
}
