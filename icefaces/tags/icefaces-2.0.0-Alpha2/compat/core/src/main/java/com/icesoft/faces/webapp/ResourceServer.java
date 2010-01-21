package com.icesoft.faces.webapp;

import org.icefaces.push.Configuration;
import org.icefaces.push.http.MimeTypeMatcher;
import org.icefaces.push.http.Request;
import org.icefaces.push.http.Server;
import org.icefaces.push.http.standard.CacheControlledServer;
import org.icefaces.push.http.standard.CompressingServer;
import org.icefaces.push.http.standard.PathDispatcherServer;

import java.io.IOException;
import java.util.logging.Logger;

public class ResourceServer implements Server {
    private static final Logger log = Logger.getLogger(ResourceServer.class.getName());
    private Server dispatcher;

    public ResourceServer(Configuration configuration, MimeTypeMatcher mimeTypeMatcher, FileLocator fileLocator) {
        PathDispatcherServer pathDispatcher = new PathDispatcherServer();
        pathDispatcher.dispatchOn(".*xmlhttp\\/css\\/.*", new CacheControlledServer(new ServeCSSResource(mimeTypeMatcher)));
        //match any path that does not point to WEB-INF directory
        pathDispatcher.dispatchOn("^(?!.*WEB\\-INF.*).*$", new FileServer(fileLocator, mimeTypeMatcher));
        dispatcher = new CompressingServer(pathDispatcher, mimeTypeMatcher, configuration);
    }

    public void service(Request request) throws Exception {
        try {
            dispatcher.service(request);
        } catch (IOException e) {
            //capture & log Tomcat specific exception
            if (e.getClass().getName().endsWith("ClientAbortException")) {
                log.fine("Browser closed the connection prematurely for " + request.getURI());
            } else {
                throw e;
            }
        }
    }

    public void shutdown() {
        dispatcher.shutdown();
    }
}
