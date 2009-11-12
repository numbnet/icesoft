package org.icepush.servlet;

import org.icepush.CodeServer;
import org.icepush.Configuration;
import org.icepush.PushContext;
import org.icepush.http.standard.CacheControlledServer;
import org.icepush.http.standard.CompressingServer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.SocketException;
import java.util.Observable;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainServlet implements PseudoServlet {
    private static Logger log = Logger.getLogger(MainServlet.class.getName());
    private PseudoServlet dispatcher;
    private Timer timer;

    public MainServlet(final ServletContext context) {
        timer = new Timer(true);
        final Configuration configuration = new ServletContextConfiguration("org.icepush", context);
        final Observable notifier = new Observable() {
            public synchronized void notifyObservers(Object o) {
                setChanged();
                super.notifyObservers(o);
                clearChanged();
            }
        };
        final PushContext pushContext = new PushContext(notifier, context);

        PathDispatcher pathDispatcher = new PathDispatcher();
        pathDispatcher.dispatchOn(".*code\\.icepush", new BasicAdaptingServlet(new CacheControlledServer(new CompressingServer(new CodeServer()))));
        pathDispatcher.dispatchOn(".*", new BrowserDispatcher() {
            protected PseudoServlet newServer(String browserID) {
                return new BrowserBoundServlet(context, pushContext, notifier, timer, configuration);
            }
        });

        dispatcher = pathDispatcher;
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
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

    public void shutdown() {
        dispatcher.shutdown();
        timer.cancel();
    }
}
