//package com.icesoft.faces.webapp.http.core;
//
//import com.icesoft.faces.webapp.http.common.Configuration;
//import com.icesoft.faces.webapp.http.common.Request;
//import com.icesoft.faces.webapp.http.common.Server;
//import com.icesoft.util.MonitorRunner;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import javax.servlet.ServletContext;
//import java.util.Collection;
//
//public class AsyncServerDetector implements Server {
//    private static final Log LOG = LogFactory.getLog(AsyncServerDetector.class);
//    private Server server;
//
//    public AsyncServerDetector(String icefacesID, Collection synchronouslyUpdatedViews, ViewQueue allUpdatedViews, ServletContext servletContext, MonitorRunner monitorRunner, Configuration configuration) {
//        boolean useAsyncHttpServerByDefault;
//        try {
//            getClass().getClassLoader().loadClass("com.icesoft.faces.async.server.AsyncHttpServerAdaptingServlet");
//            useAsyncHttpServerByDefault = true;
//        } catch (ClassNotFoundException exception) {
//            useAsyncHttpServerByDefault = false;
//        }
//        // new property name
//        String blockingRequestHandler = configuration.getAttribute("blockingRequestHandler", null);
//        // old property name
//        boolean asyncServer = configuration.getAttributeAsBoolean("async.server", false);
//        if ((blockingRequestHandler != null && blockingRequestHandler.equalsIgnoreCase("icefaces-ahs")) ||
//                (blockingRequestHandler == null && asyncServer) ||
//                (useAsyncHttpServerByDefault)) {
//
//            if (LOG.isDebugEnabled()) {
//                LOG.debug("Adapting to Asynchronous HTTP Server environment.");
//            }
//            try {
//                server = (Server) this.getClass().getClassLoader().
//                        loadClass("com.icesoft.faces.async.server.AsyncHttpServerAdaptingServlet").
//                        getDeclaredConstructor(
//                                new Class[]{
//                                        String.class,
//                                        Collection.class,
//                                        ViewQueue.class,
//                                        ServletContext.class
//                                }).
//                        newInstance(
//                                new Object[]{
//                                        icefacesID,
//                                        synchronouslyUpdatedViews,
//                                        allUpdatedViews,
//                                        servletContext
//                                });
//            } catch (Exception exception) {
//                // Possible exceptions: ClassNotFoundException,
//                //                      NoSuchMethodException,
//                //                      IntantiationException,
//                //                      InvocationTargetException,
//                //                      IllegalAccessException
//                if (LOG.isDebugEnabled()) {
//                    LOG.error("Failed to instantiate AsyncHttpServerAdaptingServlet!", exception);
//                } else if (LOG.isErrorEnabled()) {
//                    LOG.error("Failed to instantiate AsyncHttpServerAdaptingServlet!");
//                }
//            }
//        }
//        if (server == null) {
//            server = new SendUpdatedViews(icefacesID, synchronouslyUpdatedViews, allUpdatedViews, monitorRunner, configuration);
//        }
//    }
//
//    public void service(Request request) throws Exception {
//        server.service(request);
//    }
//
//    public void shutdown() {
//        server.shutdown();
//    }
//}

package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.net.messaging.MessageServiceClient;
import com.icesoft.util.MonitorRunner;

import java.lang.reflect.Constructor;
import java.util.Collection;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AsyncServerDetector implements Server {
    private static final Log LOG = LogFactory.getLog(AsyncServerDetector.class);
    private static final Object LOCK = new Object();

    private static ServerFactory factory;
    private static ServerFactory fallbackFactory;

    private Server server;

    public AsyncServerDetector(
        final String icefacesID, final Collection synchronouslyUpdatedViews,
        final ViewQueue allUpdatedViews, final MonitorRunner monitorRunner,
        final Configuration configuration,
        final MessageServiceClient messageServiceClient) {

        if (factory == null) {
            synchronized (LOCK) {
                if (factory == null) {
                    // checking if Asynchronous HTTP Service is available...
                    boolean isAsyncHttpServiceAvailable =
                        isAsyncHttpServiceAvailable();
                    if (LOG.isInfoEnabled()) {
                        LOG.info(
                            "Asynchronous HTTP Service available: " +
                                isAsyncHttpServiceAvailable);
                    }
                    boolean isJMSAvailable = isJMSAvailable();
                    if (LOG.isInfoEnabled()) {
                        LOG.info("JMS API available: " + isJMSAvailable);
                    }
                    if (isAsyncHttpServiceAvailable &&
                        configuration.getAttribute(
                            "blockingRequestHandler",
                            configuration.getAttributeAsBoolean(
                                "async.server", true) ?
                                    "icefaces-ahs" :
                                    "icefaces").
                                equalsIgnoreCase("icefaces-ahs") &&
                        isJMSAvailable) {

                        LOG.info(
                            "Adapting to Asynchronous HTTP Service " +
                                "environment.");
                        factory = new AsyncHttpServerAdaptingServletFactory();
                        fallbackFactory = new SendUpdatedViewsFactory();
                    } else {
                        LOG.info("Adapting to Send Updated Views environment.");
                        factory = new SendUpdatedViewsFactory();
                    }
                }
            }
        }
        server =
            factory.newServer(
                icefacesID, synchronouslyUpdatedViews, allUpdatedViews,
                monitorRunner, configuration, messageServiceClient);
    }

    public void service(Request request) throws Exception {
        server.service(request);
    }

    public void shutdown() {
        server.shutdown();
    }

    private boolean isAsyncHttpServiceAvailable() {
        try {
            this.getClass().getClassLoader().loadClass(
                "com.icesoft.faces.async.server." +
                    "AsyncHttpServerAdaptingServlet");
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    private boolean isJMSAvailable() {
        try {
            this.getClass().getClassLoader().loadClass(
                "javax.jms.TopicConnectionFactory");
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    private static interface ServerFactory {
        public Server newServer(
            final String icefacesID, final Collection synchronouslyUpdatedViews,
            final ViewQueue allUpdatedViews,
            final MonitorRunner monitorRunner,
            final Configuration configuration,
            final MessageServiceClient messageServiceClient);
    }

    private static class AsyncHttpServerAdaptingServletFactory
    implements ServerFactory {
        private static Constructor constructor;
        static {
            try {
                constructor =
                    AsyncHttpServerAdaptingServletFactory.class.
                        getClassLoader().
                        loadClass(
                            "com.icesoft.faces.async.server." +
                                "AsyncHttpServerAdaptingServlet").
                        getDeclaredConstructor(
                            new Class[] {
                                    String.class,
                                    Collection.class,
                                    ViewQueue.class,
                                    MessageServiceClient.class
                            });
            } catch (Exception exception) {
                // Possible exceptions: ClassNotFoundException,
                //                      NoSuchMethodException,
                LOG.warn(
                    "Failed to adapt to Asynchronous HTTP Service " +
                        "environment. Falling back to Send Updated Views " +
                        "environment.",
                    exception);
                synchronized (LOCK)  {
                    factory = fallbackFactory;
                    fallbackFactory = null;
                }
            }
        }
        public Server newServer(
            final String icefacesID, final Collection synchronouslyUpdatedViews,
            final ViewQueue allUpdatedViews,
            final MonitorRunner monitorRunner,
            final Configuration configuration,
            final MessageServiceClient messageServiceClient) {

            try {
                return                             
                    (Server)
                        constructor.newInstance(
                            new Object[]{
                                    icefacesID,
                                    synchronouslyUpdatedViews,
                                    allUpdatedViews,
                                    messageServiceClient
                            });
            } catch (Exception exception) {
                // Possible exceptions: IntantiationException,
                //                      InvocationTargetException,
                //                      IllegalAccessException
                LOG.warn(
                    "Failed to adapt to Asynchronous HTTP Service " +
                        "environment. Falling back to Send Updated Views " +
                        "environment.",
                    exception);
                synchronized (LOCK) {
                    factory = fallbackFactory;
                    fallbackFactory = null;
                }
                return
                    factory.newServer(
                        icefacesID, synchronouslyUpdatedViews, allUpdatedViews,
                        monitorRunner, configuration, messageServiceClient);
            }
        }
    }

    private static class SendUpdatedViewsFactory
    implements ServerFactory {
        public Server newServer(
            final String icefacesID, final Collection synchronouslyUpdatedViews,
            final ViewQueue allUpdatedViews,
            final MonitorRunner monitorRunner,
            final Configuration configuration,
            final MessageServiceClient messageServiceClient) {

            return
                new SendUpdatedViews(
                    icefacesID, synchronouslyUpdatedViews, allUpdatedViews,
                    monitorRunner, configuration);
        }
    }
}
