package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.FileLocator;
import com.icesoft.faces.webapp.http.common.MimeTypeMatcher;
import com.icesoft.faces.webapp.http.core.DisposeBeans;
import com.icesoft.faces.webapp.http.core.ResourceServer;
import com.icesoft.net.messaging.MessageServiceAdapter;
import com.icesoft.net.messaging.MessageServiceClient;
import com.icesoft.net.messaging.MessageServiceException;
import com.icesoft.net.messaging.jms.JMSAdapter;
import com.icesoft.util.IdGenerator;
import com.icesoft.util.MonitorRunner;
import com.icesoft.util.SeamUtilities;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    private DisposeViewsHandler disposeViewsHandler;

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
            //don't create new sessions for XMLHTTPRequests identified by the path prefix "block/*"
            PseudoServlet sessionDispatcher = new SessionDispatcher() {
                protected PseudoServlet newServlet(HttpSession session, Monitor sessionMonitor) {
                    return new MainSessionBoundServlet(session, sessionMonitor, idGenerator, mimeTypeMatcher, monitorRunner, configuration);
                }
            };
            if (SeamUtilities.isSpringEnvironment()) {
                //Need to dispatch to the Spring resource server
                dispatcher.dispatchOn("/spring/resources/", resourceServer);
            }
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
        tearDownMessageServiceClient();
        monitorRunner.stop();
        DisposeBeans.in(context);
        dispatcher.shutdown();
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

    private void setUpMessageServiceClient() {
        if (!isJMSAvailable()) {
            // todo: create DummyAdapter
            return;
        }
        MessageServiceAdapter messageServiceAdapter = new JMSAdapter(context);
        messageServiceClient =
            new MessageServiceClient(
                messageServiceAdapter.getMessageServiceConfiguration(),
                messageServiceAdapter,
                context);
        disposeViewsHandler = new DisposeViewsHandler();
        disposeViewsHandler.setCallback(
            new DisposeViewsHandler.Callback() {
                public void disposeView(
                    final String iceFacesId, final String viewNumber) {

                    // todo: dispose view (Ted/Mircea)
                }
            });
        try {
            messageServiceClient.subscribe(
                MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME,
                disposeViewsHandler.getMessageSelector());
        messageServiceClient.addMessageHandler(
            disposeViewsHandler, MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
            messageServiceClient.start();
        } catch (MessageServiceException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Failed to start message delivery!", exception);
            }
            // todo: create DummyAdapter
            messageServiceClient = null/*new DummyAdapter()*/;
        }
    }

    private void tearDownMessageServiceClient() {
        if (null == messageServiceClient)  {
            return;
        }
        try {
            messageServiceClient.stop();
        } catch (MessageServiceException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Failed to stop message delivery!", exception);
            }
        }
        messageServiceClient.removeMessageHandler(
            disposeViewsHandler, MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
        try {
            messageServiceClient.stop();
        } catch (MessageServiceException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "Failed to close connection due to some internal error!",
                    exception);
            }
        }
    }
}
