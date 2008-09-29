package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.async.render.RenderManager;
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

public class MainServlet extends HttpServlet {
    private static final Log LOG = LogFactory.getLog(MainServlet.class);

    static {
        final String headless = "java.awt.headless";
        if (null == System.getProperty(headless)) {
            System.setProperty(headless, "true");
        }
    }

    private PathDispatcher dispatcher = new PathDispatcher();
    private String contextPath;
    private ServletContext context;
    private MonitorRunner monitorRunner;
    private MessageServiceClient messageServiceClient;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.context = servletConfig.getServletContext();
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
                    URI contextURI = URI.create(contextPath);
                    URI pathURI = URI.create(path);
                    String result = contextURI.relativize(pathURI).getPath();
                    String fileLocation = context.getRealPath(result);
                    return new File(fileLocation);
                }
            };
            monitorRunner = new MonitorRunner(configuration.getAttributeAsLong("monitorRunnerInterval", 10000));
            setUpMessageServiceClient();
            RenderManager.setServletConfig(servletConfig);
            PseudoServlet resourceServer = new BasicAdaptingServlet(new ResourceServer(configuration, mimeTypeMatcher, localFileLocator));
            PseudoServlet sessionDispatcher = new SessionDispatcher(configuration, context) {
                protected Server newServer(HttpSession session, Monitor sessionMonitor) {
                    return new MainSessionBoundServlet(session, sessionMonitor, idGenerator, mimeTypeMatcher, monitorRunner, configuration, messageServiceClient);
                }
            };
            if (SeamUtilities.isSpringEnvironment()) {
                //Need to dispatch to the Spring resource server
                dispatcher.dispatchOn("/spring/resources/", resourceServer);
            }
            //don't create new sessions for XMLHTTPRequests identified by "block/*" prefixed paths
            dispatcher.dispatchOn(".*(block\\/)", new SessionVerifier(sessionDispatcher));
            dispatcher.dispatchOn(".*(\\.iface$|\\.jsf|\\.faces$|\\.jsp$|\\.jspx$|\\.html$|\\.xhtml$|\\.seam$|uploadHtml$|/spring/)", sessionDispatcher);
            dispatcher.dispatchOn(".*", resourceServer);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        contextPath = request.getContextPath();
        try {
            dispatcher.service(request, response);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void destroy() {
        monitorRunner.stop();
        DisposeBeans.in(context);
        dispatcher.shutdown();
        tearDownMessageServiceClient();
    }

    private boolean isAsyncHttpServiceAvailable() {
        try {
            this.getClass().getClassLoader().loadClass("com.icesoft.faces.async.server.AsyncHttpServerAdaptingServlet");
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    private boolean isJMSAvailable() {
        try {
            this.getClass().getClassLoader().loadClass("javax.jms.TopicConnectionFactory");
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    private void setUpMessageServiceClient() {
        if (!isAsyncHttpServiceAvailable() || !isJMSAvailable()) {
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
}
