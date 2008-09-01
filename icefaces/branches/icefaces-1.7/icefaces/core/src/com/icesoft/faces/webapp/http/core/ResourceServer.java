package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.http.common.*;
import com.icesoft.faces.webapp.http.common.standard.CacheControlledServer;
import com.icesoft.faces.webapp.http.common.standard.CompressingServer;
import com.icesoft.faces.webapp.http.common.standard.PathDispatcherServer;

public class ResourceServer implements Server {
    private Server dispatcher;

    public ResourceServer(Configuration configuration, MimeTypeMatcher mimeTypeMatcher, FileLocator fileLocator) {
        PathDispatcherServer pathDispatcher = new PathDispatcherServer();
        pathDispatcher.dispatchOn(".*xmlhttp\\/javascript-blocked$", new RedirectOnJSBlocked(configuration));
        pathDispatcher.dispatchOn(".*xmlhttp\\/.*\\/.*\\.js$", new CacheControlledServer(new ServeJSCode()));
        pathDispatcher.dispatchOn(".*xmlhttp\\/css\\/.*", new CacheControlledServer(new ServeCSSResource(mimeTypeMatcher)));
        pathDispatcher.dispatchOn(".*xmlhttp\\/blank$", new CacheControlledServer(new ServeBlankPage()));
        //match any path that does not point to WEB-INF directory
        pathDispatcher.dispatchOn("^(?!.*WEB\\-INF.*).*$", new FileServer(fileLocator, mimeTypeMatcher));
        if (configuration.getAttributeAsBoolean("compressResources", true)) {
            dispatcher = new CompressingServer(pathDispatcher, mimeTypeMatcher, configuration);
        } else {
            dispatcher = pathDispatcher;
        }
    }

    public void service(Request request) throws Exception {
        dispatcher.service(request);
    }

    public void shutdown() {
        dispatcher.shutdown();
    }
}
