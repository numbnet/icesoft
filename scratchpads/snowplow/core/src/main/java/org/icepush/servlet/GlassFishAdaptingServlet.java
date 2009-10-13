/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icepush.servlet;

import com.sun.enterprise.web.connector.grizzly.comet.CometContext;
import com.sun.enterprise.web.connector.grizzly.comet.CometEngine;
import com.sun.enterprise.web.connector.grizzly.comet.CometEvent;
import com.sun.enterprise.web.connector.grizzly.comet.CometHandler;
import org.icepush.http.ResponseHandler;
import org.icepush.http.Server;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GlassFishAdaptingServlet implements PseudoServlet {
    private static Logger log = Logger.getLogger("org.icepushservlet");
    private final Server server;
    private final String contextPath;

    public GlassFishAdaptingServlet(final Server server, final ServletContext servletContext) throws ServletException {
        this.server = server;
        try {
            contextPath = ServletContext.class.getMethod("getContextPath", new Class[]{}).invoke(servletContext, (Object[]) null) + "/";
        } catch (NoSuchMethodException exception) {
            throw new EnvironmentAdaptingException("No such method: ServletContext.getContextPath", exception);
        } catch (IllegalAccessException exception) {
            throw new EnvironmentAdaptingException("Illegal access: ServletContext.getContextPath", exception);
        } catch (InvocationTargetException exception) {
            throw new EnvironmentAdaptingException("Invocation target: ServletContext.getContextPath", exception);
        }
        CometEngine.getEngine().register(contextPath).setExpirationDelay(-1);
    }

    public void service(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        GlassFishRequestResponse requestResponse = new GlassFishRequestResponse(request, response);
        server.service(requestResponse);
        synchronized (requestResponse) {
            if (!requestResponse.isDone()) {
                requestResponse.park();
                /*
                 * This will make sure that the HTTP request and response are
                 * being "parked" by Grizzly's Comet Engine.
                 *
                 * Please note that, the actual "parking" happens somewhere
                 * after the onInitialize(CometEvent) method of the CometHandler
                 * has been invoked.
                 */
                try {
                    /*
                     * CometContext.addCometHandler(CometHandler) throws an
                     * IllegalStateException when the cometSupport property is
                     * not set to true in the config/domain.xml file.
                     */
                    CometEngine.getEngine().register(contextPath).addCometHandler(requestResponse);
                } catch (IllegalStateException exception) {
                    if (log.isLoggable(Level.SEVERE)) {
                        log.log(Level.SEVERE,
                                "\r\n" +
                                        "\r\n" +
                                        "Failed to add Comet handler: \r\n" +
                                        "    Exception message: " +
                                        exception.getMessage() + "\r\n" +
                                        "    Exception cause: " +
                                        exception.getCause() + "\r\n\r\n" +
                                        "To enable GlassFish ARP, please set the " +
                                        "cometSupport property to true in the \r\n" +
                                        "domain's config/domain.xml for the " +
                                        "http-listener listening to port " +
                                        request.getServerPort() + ".\r\n");
                    }
                    throw new EnvironmentAdaptingException(exception);
                }
            }
        }
    }

    public void shutdown() {
        server.shutdown();
    }

    private class GlassFishRequestResponse extends ServletRequestResponse implements CometHandler {
        private boolean parked = false;
        private boolean done = false;

        public GlassFishRequestResponse(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
            super(request, response);
        }

        public void attach(final Object object) {
            // do nothing.
        }

        public boolean isDone() {
            return done;
        }

        public boolean isParked() {
            return parked;
        }

        public void onEvent(final CometEvent event) throws IOException {
            // do nothing.
        }

        public void onInitialize(final CometEvent event) throws IOException {
            // do nothing.
        }

        public void onTerminate(final CometEvent event) throws IOException {
            // do nothing.
        }

        public void onInterrupt(final CometEvent event) throws IOException {
            // do nothing.
        }

        public void park() {
            parked = true;
        }

        public void respondWith(final ResponseHandler handler)
                throws Exception {
            synchronized (this) {
                if (!isParked()) {
                    handler.respond(this);
                    done = true;
                } else {
                    CometContext cometContext = CometEngine.getEngine().register(contextPath);
                    int count = 0;
                    while (!cometContext.isActive(this)) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException exception) {
                            // ignoring interrupts...
                        }
                        if (count++ > 10) {
                            if (log.isLoggable(Level.FINE)) {
                                log.log(Level.FINE, "cometContext.isActive failed");
                            }
                            break;
                        }
                    }
                    if (cometContext.isActive(this)) {
                        handler.respond(this);
                        cometContext.resumeCometHandler(this);
                        unpark();
                    }
                }
            }
        }

        public void unpark() {
            parked = false;
        }
    }
}
