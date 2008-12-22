package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.async.common.AsyncHttpServerAdaptingServlet;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.net.messaging.MessageServiceClient;
import com.icesoft.util.MonitorRunner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;

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
            final MessageServiceClient messageServiceClient, final PageTest pageTest) {

        if (factory == null) {
            synchronized (LOCK) {
                if (factory == null) {
                    String blockingRequestHandler =
                            configuration.getAttribute(
                                    "blockingRequestHandler",
                                    configuration.getAttributeAsBoolean(
                                            "async.server", false) ?
                                            "icefaces-ahs" :
                                            "icefaces");
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Blocking Request Handler: " +
                                blockingRequestHandler);
                    }
                    // checking if Asynchronous HTTP Service is available...
                    boolean isJMSAvailable = isJMSAvailable();
                    if (LOG.isInfoEnabled()) {
                        LOG.info("JMS API available: " + isJMSAvailable);
                    }
                    if (blockingRequestHandler.
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
                        monitorRunner, configuration, messageServiceClient, pageTest);
    }

    public void service(final Request request) throws Exception {
        server.service(request);
    }

    public void shutdown() {
        server.shutdown();
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
                final MessageServiceClient messageServiceClient,
                final PageTest pageTest);
    }

    private static class AsyncHttpServerAdaptingServletFactory
            implements ServerFactory {
        public Server newServer(
                final String icefacesID, final Collection synchronouslyUpdatedViews,
                final ViewQueue allUpdatedViews,
                final MonitorRunner monitorRunner,
                final Configuration configuration,
                final MessageServiceClient messageServiceClient,
                final PageTest pageTest) {

            try {
                return
                        new AsyncHttpServerAdaptingServlet(
                                icefacesID,
                                synchronouslyUpdatedViews,
                                allUpdatedViews,
                                messageServiceClient);
            } catch (Exception exception) {
                // Possible exceptions: MessageServiceException
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
                                monitorRunner, configuration, messageServiceClient, pageTest);
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
                final MessageServiceClient messageServiceClient,
                final PageTest pageTest) {

            return
                    new SendUpdatedViews(
                            icefacesID, synchronouslyUpdatedViews, allUpdatedViews,
                            monitorRunner, configuration, pageTest);
        }
    }
}
