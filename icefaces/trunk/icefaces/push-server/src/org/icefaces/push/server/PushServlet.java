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
package org.icefaces.push.server;

import com.icesoft.faces.env.Authorization;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.servlet.PathDispatcher;
import com.icesoft.faces.webapp.http.servlet.ServletConfigConfiguration;
import com.icesoft.faces.webapp.http.servlet.ServletContextConfiguration;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;
import com.icesoft.net.messaging.MessageServiceAdapter;
import com.icesoft.net.messaging.http.HttpAdapter;
import com.icesoft.util.ServerUtility;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PushServlet
extends HttpServlet {
    private static final Log LOG = LogFactory.getLog(PushServlet.class);

    private String localAddress;
    private int localPort;
    private MessageService messageService;
    private PathDispatcher pathDispatcher = new PathDispatcher();
    private ServletContext servletContext;

    public void destroy() {
        super.destroy();
        messageService.stop();
        messageService.close();
        pathDispatcher.shutdown();
    }

    public void init(final ServletConfig servletConfig)
    throws ServletException {
        super.init(servletConfig);
        servletContext = servletConfig.getServletContext();
        if (LOG.isInfoEnabled()) {
            LOG.info(new ProductInfo());
        }
        try {
            final Configuration _servletConfigConfiguration =
                new ServletConfigConfiguration(
                    "org.icefaces.push.server", servletConfig);
            final Configuration _servletContextConfiguration =
                new ServletContextConfiguration(
                    "com.icesoft.faces", servletConfig.getServletContext());
            messageService =
                new MessageService(servletConfig.getServletContext());
            final SessionManager _sessionManager =
                new SessionManager(_servletConfigConfiguration, messageService);
            final ExecuteQueue _executeQueue = new ExecuteQueue();
            SessionDispatcher _sessionDispatcher =
                new SessionDispatcher(
                    _servletContextConfiguration, getServletContext()) {

                    protected Server newServer(
                        final HttpSession httpSession,
                        final Monitor monitor,
                        final Authorization authorization) {

                        return
                            new SessionBoundServlet(
                                _sessionManager, _executeQueue, monitor);
                    }
                };
            pathDispatcher.dispatchOn(
                ".*(block\\/message)",
                ((HttpAdapter)
                    messageService.getMessageServiceClient().
                        getMessageServiceAdapter()).
                    getHttpMessagingDispatcher());
            pathDispatcher.dispatchOn(
                ".*",
                _sessionDispatcher);
            messageService.start();
        } catch (Exception exception) {
            LOG.error(
                "An error occurred while initializing the Push Server!",
                exception);
        }
    }

    protected void service(
        final HttpServletRequest httpServletRequest,
        final HttpServletResponse httpServletResponse)
    throws IOException, ServletException {
        if (localAddress == null) {
            localAddress =
                ServerUtility.getLocalAddr(httpServletRequest, servletContext);
            localPort =
                ServerUtility.getLocalPort(httpServletRequest, servletContext);
            if (messageService != null) {
                MessageServiceAdapter adapter =
                    messageService.getMessageServiceClient().
                        getMessageServiceAdapter();
                if (adapter instanceof HttpAdapter) {
                    ((HttpAdapter)adapter).setLocal(localAddress, localPort);
                }
            }
        }
        try {
            pathDispatcher.service(httpServletRequest, httpServletResponse);
        } catch (RuntimeException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ServletException(exception);
        }
    }
}
