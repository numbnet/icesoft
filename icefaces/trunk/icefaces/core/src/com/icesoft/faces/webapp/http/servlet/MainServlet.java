package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.env.Authorization;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.FileLocator;
import com.icesoft.faces.webapp.http.common.MimeTypeMatcher;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.core.DisposeBeans;
import com.icesoft.faces.webapp.http.core.ResourceServer;
import com.icesoft.net.messaging.MessageServiceClient;
import com.icesoft.net.messaging.MessageServiceException;
import com.icesoft.net.messaging.jms.JMSAdapter;
import com.icesoft.util.IdGenerator;
import com.icesoft.util.MonitorRunner;
import com.icesoft.util.SeamUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.SocketException;

public class MainServlet extends HttpServlet {
    private static final Log LOG = LogFactory.getLog(MainServlet.class);
    private static final CurrentContextPath currentContextPath = new CurrentContextPath();

    static {
        final String headless = "java.awt.headless";
        if (null == System.getProperty(headless)) {
            System.setProperty(headless, "true");
        }
    }

    private PathDispatcher dispatcher = new PathDispatcher();
    private ServletContext context;
    private MonitorRunner monitorRunner;
    private MessageServiceClient messageServiceClient;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        context = servletConfig.getServletContext();
        try {
            final Configuration configuration = new ServletContextConfiguration("com.icesoft.faces", context);
            final IdGenerator idGenerator = new IdGenerator(context.getResource("/WEB-INF/web.xml").getPath());
            final MimeTypeMatcher mimeTypeMatcher = new MimeTypeMatcher() {
                public String mimeTypeFor(String extension) {
                    return context.getMimeType(extension);
                }
            };
            final FileLocator localFileLocator = new FileLocator() {
                public File locate(String path) {
                    URI contextURI = URI.create(currentContextPath.lookup());
                    URI pathURI = URI.create(path);
                    String result = contextURI.relativize(pathURI).getPath();
                    String fileLocation = context.getRealPath(result);
                    return new File(fileLocation);
                }
            };
            monitorRunner = new MonitorRunner(configuration.getAttributeAsLong("monitorRunnerInterval", 10000));
            setUpMessageServiceClient(configuration);
            RenderManager.setServletConfig(servletConfig);
            PseudoServlet resourceServer = new BasicAdaptingServlet(new ResourceServer(configuration, mimeTypeMatcher, localFileLocator));
            PseudoServlet sessionDispatcher = new SessionDispatcher(configuration, context) {
                protected Server newServer(HttpSession session, Monitor sessionMonitor, Authorization authorization) {
                    return new MainSessionBoundServlet(session, sessionMonitor, idGenerator, mimeTypeMatcher, monitorRunner, configuration, messageServiceClient, authorization);
                }
            };
            if (SeamUtilities.isSpringEnvironment()) {
                //Need to dispatch to the Spring resource server
                dispatcher.dispatchOn("/spring/resources/", resourceServer);
            }
            //don't create new session for resources belonging to expired user sessions
            dispatcher.dispatchOn(".*(block\\/resource\\/)", new SessionVerifier(sessionDispatcher, false));
            //don't create new session for XMLHTTPRequests identified by "block/*" prefixed paths
            dispatcher.dispatchOn(".*(block\\/)", new SessionVerifier(sessionDispatcher, true));
            dispatcher.dispatchOn(".*(\\/$|\\.iface$|\\.jsf|\\.faces$|\\.jsp$|\\.jspx$|\\.html$|\\.xhtml$|\\.seam$|uploadHtml$|/spring/)", sessionDispatcher);
            dispatcher.dispatchOn(".*", resourceServer);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            currentContextPath.attach(request.getContextPath());
            dispatcher.service(request, response);
        } catch (SocketException e) {
            if ("Broken pipe".equals(e.getMessage())) {
                // client left the page
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Connection broken by client.", e);
                } else if (LOG.isDebugEnabled()) {
                    LOG.debug("Connection broken by client: " + e.getMessage());
                }
            } else {
                throw new ServletException(e);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            currentContextPath.detach();
        }
    }

    public void destroy() {
        monitorRunner.stop();
        DisposeBeans.in(context);
        dispatcher.shutdown();
        tearDownMessageServiceClient();
    }

    private boolean isJMSAvailable() {
        try {
            this.getClass().getClassLoader().loadClass("javax.jms.TopicConnectionFactory");
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    private void setUpMessageServiceClient(final Configuration configuration) {
        String blockingRequestHandler = configuration.getAttribute("blockingRequestHandler",
                configuration.getAttributeAsBoolean("async.server", false) ? "icefaces-ahs" : "icefaces");
        if (LOG.isInfoEnabled()) {
            LOG.info("Blocking Request Handler: " + blockingRequestHandler);
        }
        boolean isJMSAvailable = isJMSAvailable();
        if (LOG.isInfoEnabled()) {
            LOG.info("JMS API available: " + isJMSAvailable);
        }
        if (!blockingRequestHandler.equalsIgnoreCase("icefaces-ahs") || !isJMSAvailable) {
            return;
        }
        try {
            messageServiceClient = new MessageServiceClient(new JMSAdapter(context), context);
            //todo: make message selector static to avoid instantiating the message handler
            messageServiceClient.subscribe(MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME, new DisposeViewsHandler().getMessageSelector());
            messageServiceClient.start();
        } catch (Exception exception) {
            LOG.info("Did not start Ajax Push JMS services: " + exception);
            messageServiceClient = null;
        }
    }

    private void tearDownMessageServiceClient() {
        if (messageServiceClient == null) {
            return;
        }
        try {
            messageServiceClient.stop();
        } catch (MessageServiceException exception) {
            LOG.error("Failed to close connection due to some internal error!", exception);
        }
    }


    //todo: factor out into a ServletContextDispatcher
    private static class CurrentContextPath extends ThreadLocal {
        public String lookup() {
            return (String) get();
        }

        public void attach(String path) {
            set(path);
        }

        public void detach() {
            set(null);
        }
    }
}
