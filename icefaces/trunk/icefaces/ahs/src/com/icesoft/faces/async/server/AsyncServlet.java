/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */
package com.icesoft.faces.async.server;

//import com.icesoft.faces.webapp.xmlhttp.ResponseStateManagerFactory;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A very simple servlet for starting up the Asynchronous HTTP Server for
 * handling blocking requests from the client in a thread effecient manner.
 * Right now this servlet is VERY basic. In time it should become an entry point
 * for managing the Asynchronous HTTP Server as well.
 */
public class AsyncServlet extends HttpServlet {
    private static final String PORT_KEY =
        "com.icesoft.faces.async.server.port";
    private static final String BLOCKING_KEY =
        "com.icesoft.faces.async.server.blocking";
    private static final String PERSISTENT_KEY =
        "com.icesoft.faces.async.server.persistent";
    private static final String COMPRESSION_KEY =
        "com.icesoft.faces.async.server.compression";
    private static final String EXECUTE_QUEUE_SIZE_KEY =
        "com.icesoft.faces.async.server.executeQueueSize";
    private static final String RESPONSE_QUEUE_SIZE_KEY =
        "com.icesoft.faces.async.server.responseQueueSize";
    private static final String RESPONSE_QUEUE_THRESHOLD_KEY =
        "com.icesoft.faces.async.server.responseQueueThreshold";
    private static final String PURGE_MESSAGE_CONTENTS_KEY =
        "com.icesoft.faces.async.server.purgeMessageContents";

    private static final Log LOG = LogFactory.getLog(AsyncServlet.class);

    /*
     * The AsyncServlet extends from HttpServlet which implements the
     * Serializable interface. As I do not know why the HttpServlet implements
     * the Serializable interface, I declared the Asynchnronous HTTP Server
     * reference asyncHttpServer transient for good practice.
     */
    private transient AsyncHttpServer asyncHttpServer;

    public void destroy() {
        asyncHttpServer.stop();
    }

    public void init(final ServletConfig servletConfig)
    throws ServletException {
//        try {
//            AsyncHttpServerSettings _asyncHttpServerSettings =
//                new AsyncHttpServerSettings();
//            String blockingValue =
//                servletConfig.getInitParameter(BLOCKING_KEY);
//            if (blockingValue != null) {
//                _asyncHttpServerSettings.setBlocking(
//                    Boolean.valueOf(blockingValue).booleanValue());
//            }
//            String compressionValue =
//                servletConfig.getInitParameter(COMPRESSION_KEY);
//            if (compressionValue != null) {
//                _asyncHttpServerSettings.setCompression(
//                    Boolean.valueOf(compressionValue).booleanValue());
//            }
//            String persistentValue =
//                servletConfig.getInitParameter(PERSISTENT_KEY);
//            if (persistentValue != null) {
//                _asyncHttpServerSettings.setPersistent(
//                    Boolean.valueOf(persistentValue).booleanValue());
//            }
//            String portValue =
//                servletConfig.getInitParameter(PORT_KEY);
//            if (portValue != null) {
//                try {
//                    _asyncHttpServerSettings.setPort(
//                        Integer.parseInt(portValue));
//                } catch (NumberFormatException exception) {
//                    if (LOG.isErrorEnabled()) {
//                        LOG.error("Illegal port: " + portValue, exception);
//                    }
//                } catch (IllegalArgumentException exception) {
//                    if (LOG.isErrorEnabled()) {
//                        LOG.error("Illegal port: " + portValue);
//                    }
//                }
//            }
//            String executeQueueSizeValue =
//                servletConfig.getInitParameter(EXECUTE_QUEUE_SIZE_KEY);
//            if (executeQueueSizeValue != null) {
//                try {
//                    _asyncHttpServerSettings.setExecuteQueueSize(
//                        Integer.parseInt(executeQueueSizeValue));
//                } catch (NumberFormatException exception) {
//                    if (LOG.isErrorEnabled()) {
//                        LOG.error(
//                            "Illegal execute queue size: " +
//                                executeQueueSizeValue,
//                            exception);
//                    }
//                }
//            }
//            String responseQueueSize =
//                servletConfig.getInitParameter(RESPONSE_QUEUE_SIZE_KEY);
//            if (responseQueueSize != null) {
//                try {
//                    _asyncHttpServerSettings.setResponseQueueSize(
//                        Integer.parseInt(responseQueueSize));
//                    _asyncHttpServerSettings.setUpdatedViewsQueueSize(
//                        Integer.parseInt(responseQueueSize));
//                } catch (NumberFormatException exception) {
//                    if (LOG.isErrorEnabled()) {
//                        LOG.error(
//                            "Illegal response queue size: " + responseQueueSize,
//                            exception);
//                    }
//                }
//            }
//            String responseQueueThreshold =
//                servletConfig.getInitParameter(RESPONSE_QUEUE_THRESHOLD_KEY);
//            if (responseQueueThreshold != null) {
//                try {
//                    _asyncHttpServerSettings.setResponseQueueThreshold(
//                        Double.parseDouble(responseQueueThreshold));
//                    _asyncHttpServerSettings.setUpdatedViewsQueueThreshold(
//                        Double.parseDouble(responseQueueThreshold));
//                } catch (NumberFormatException exception) {
//                    if (LOG.isErrorEnabled()) {
//                        LOG.error(
//                            "Illegal response queue threshold: " +
//                                responseQueueThreshold,
//                            exception);
//                    }
//                }
//            }
//            String purgeMessageContents =
//                servletConfig.getInitParameter(PURGE_MESSAGE_CONTENTS_KEY);
//            if (purgeMessageContents != null) {
//                _asyncHttpServerSettings.setPurgeMessageContents(
//                    purgeMessageContents);
//            }
//            //Start the asyncHttpServer
//            asyncHttpServer =
//                new AsyncHttpServer(
//                    _asyncHttpServerSettings,
//                    servletConfig.getServletContext());
//            asyncHttpServer.start();
//            //Register the asyncHttpServer as the handler for responses
////            ResponseStateManagerFactory.
////                getResponseStateManager(servletConfig.getServletContext()).
////                    setResponseHandler(asyncHttpServer);
//        } catch (Throwable throwable) {
//            if (LOG.isErrorEnabled()) {
//                LOG.error("An error occurred!", throwable);
//            }
//        }
    }

    protected void service(
        final HttpServletRequest req, final HttpServletResponse res)
    throws IOException, ServletException {
        res.setContentType( "text/plain" );
        PrintWriter out = res.getWriter();
        out.print( "Asynchonous HTTP Server\n\n" );
        out.print( "Server is listening on port: " + asyncHttpServer.getPort() );
        out.flush();
    }
}
