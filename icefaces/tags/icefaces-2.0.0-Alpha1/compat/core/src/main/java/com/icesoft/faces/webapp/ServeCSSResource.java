package com.icesoft.faces.webapp;

import org.icefaces.push.http.MimeTypeMatcher;
import org.icefaces.push.http.Request;
import org.icefaces.push.http.Response;
import org.icefaces.push.http.ResponseHandler;
import org.icefaces.push.http.Server;
import org.icefaces.push.http.standard.NotFoundHandler;

import java.io.InputStream;


public class ServeCSSResource implements Server {
    private static final String Package = "com/icesoft/faces/resources/css/";
    private ClassLoader loader;
    private MimeTypeMatcher matcher;

    public ServeCSSResource(MimeTypeMatcher mimeTypeMatcher) {
        loader = this.getClass().getClassLoader();
        matcher = mimeTypeMatcher;
    }

    public void service(Request request) throws Exception {
        final String path = request.getURI().getPath();
        String file = path.substring(path.lastIndexOf("css/") + 4, path.length());
        final InputStream in = loader.getResourceAsStream(Package + file);

        if (in == null) {
            request.respondWith(new NotFoundHandler("Cannot find CSS file for " + path));
        } else {
            request.respondWith(new ResponseHandler() {
                public void respond(Response response) throws Exception {
                    response.setHeader("Content-Type", matcher.mimeTypeFor(path));
                    response.writeBodyFrom(in);
                }
            });
        }
    }

    public void shutdown() {
    }
}
