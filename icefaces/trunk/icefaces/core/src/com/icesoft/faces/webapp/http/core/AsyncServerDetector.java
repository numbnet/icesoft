package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import java.util.Collection;

public class AsyncServerDetector implements Server {
    private static final Log LOG = LogFactory.getLog(AsyncServerDetector.class);
    private Server server;

    public AsyncServerDetector(String icefacesID, Collection synchronouslyUpdatedViews, ViewQueue allUpdatedViews, ServletContext servletContext, Configuration configuration) {
//        boolean useAsyncHttpServerByDefault;
//        try {
//            getClass().getClassLoader().loadClass("com.icesoft.faces.async.server.AsyncHttpServerAdaptingServlet");
//            useAsyncHttpServerByDefault = true;
//        } catch (ClassNotFoundException exception) {
//            useAsyncHttpServerByDefault = false;
//        }
        // new property name
        String blockingRequestHandler = configuration.getAttribute("blockingRequestHandler", null);
        // old property name
        boolean asyncServer = configuration.getAttributeAsBoolean("async.server", false);
        if ((blockingRequestHandler != null && blockingRequestHandler.equalsIgnoreCase("icefaces-ahs")) ||
            (blockingRequestHandler == null && asyncServer)) {

            if (LOG.isInfoEnabled()) {
                LOG.info("Adapting to Asynchronous HTTP Server environment.");
            }
            try {
                server = (Server) this.getClass().getClassLoader().
                        loadClass("com.icesoft.faces.async.server.AsyncHttpServerAdaptingServlet").
                        getDeclaredConstructor(
                                new Class[]{
                                        String.class,
                                        Collection.class,
                                        ViewQueue.class,
                                        ServletContext.class
                                }).
                        newInstance(
                                new Object[]{
                                        icefacesID,
                                        synchronouslyUpdatedViews,
                                        allUpdatedViews,
                                        servletContext
                                });
            } catch (Exception exception) {
                // Possible exceptions: ClassNotFoundException,
                //                      NoSuchMethodException,
                //                      IntantiationException,
                //                      InvocationTargetException,
                //                      IllegalAccessException
                if (LOG.isFatalEnabled()) {
                    LOG.fatal("Failed to instantiate AsyncHttpServerAdaptingServlet!", exception);
                }
            }
        } else {
            server = new SendUpdatedViews(icefacesID, synchronouslyUpdatedViews, allUpdatedViews);
        }
    }

    public void service(Request request) throws Exception {
        server.service(request);
    }

    public void shutdown() {
        server.shutdown();
    }
}
