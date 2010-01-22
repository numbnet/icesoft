package com.icesoft.faces.webapp;

import org.icefaces.push.http.MimeTypeMatcher;
import org.icefaces.push.http.Request;
import org.icefaces.push.http.Response;
import org.icefaces.push.http.ResponseHandler;
import org.icefaces.push.http.Server;
import org.icefaces.push.http.standard.NotFoundHandler;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

public class FileServer implements Server {
    private FileLocator locator;
    private MimeTypeMatcher mimeTypeMatcher;

    public FileServer(FileLocator locator, MimeTypeMatcher mimeTypeMatcher) {
        this.locator = locator;
        this.mimeTypeMatcher = mimeTypeMatcher;
    }

    public void service(Request request) throws Exception {
        final String path = request.getURI().getPath();
        final File file = locator.locate(path);
        if (file.exists()) {
            request.respondWith(new ResponseHandler() {
                public void respond(Response response) throws Exception {
                    String mimeType = mimeTypeMatcher.mimeTypeFor(path);
                    Date lastModified = new Date(file.lastModified());

                    response.setHeader("Content-Type", mimeType);
                    response.setHeader("Last-Modified", lastModified);
                    response.writeBodyFrom(new FileInputStream(file));
                }
            });
        } else {
            request.respondWith(new NotFoundHandler("Could not find file at " + path));
        }
    }

    public void shutdown() {
    }
}
