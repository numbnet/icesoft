package org.icepush.servlet;

import org.icepush.CodeServer;
import org.icepush.Configuration;
import org.icepush.http.standard.CacheControlledServer;
import org.icepush.http.standard.CompressingServer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.SocketException;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ICEpushServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(ICEpushServlet.class.getName());
    private PseudoServlet dispatcher;
    private Timer timer;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        final ServletContext context = servletConfig.getServletContext();
        final Configuration configuration = new ServletContextConfiguration("org.icepush", context);
        timer = new Timer(true);

        //todo: replace SessionDispatcher with BrowserDispatcher -- dispatching based on the BROWSERID cookie
        PathDispatcher pathDispatcher = new PathDispatcher();
        pathDispatcher.dispatchOn(".*code\\.icepush", new BasicAdaptingServlet(new CacheControlledServer(new CompressingServer(new CodeServer()))));
        pathDispatcher.dispatchOn(".*", new SessionDispatcher(context) {
            protected PseudoServlet newServer(HttpSession session) {
                return new SessionBoundServlet(session, timer, configuration);
            }
        });

        dispatcher = pathDispatcher;
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            dispatcher.service(request, response);
        } catch (SocketException e) {
            if ("Broken pipe".equals(e.getMessage())) {
                // client left the page
                if (log.isLoggable(Level.FINEST)) {
                    log.log(Level.FINEST, "Connection broken by client.", e);
                } else if (log.isLoggable(Level.FINE)) {
                    log.log(Level.FINE, "Connection broken by client: " + e.getMessage());
                }
            } else {
                throw new ServletException(e);
            }
        } catch (RuntimeException e) {
            //ICE-4261: We cannot wrap RuntimeExceptions as ServletExceptions because of support for Jetty
            //Continuations.  However, if the message of a RuntimeException is null, Tomcat won't
            //properly redirect to the configured error-page.  So we need a new RuntimeException
            //that actually includes a message.
            if ("org.mortbay.jetty.RetryRequest".equals(e.getClass().getName())) {
                throw e;
            } else if (e.getMessage() != null) {
                throw e;
            } else {
                throw new RuntimeException("wrapped Exception: " + e, e);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void destroy() {
        dispatcher.shutdown();
        timer.cancel();
    }
}
