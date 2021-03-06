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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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

package com.icesoft.util;

import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.ConfigurationException;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.servlet.ServletContextConfiguration;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerUtility {
    private static final Log LOG = LogFactory.getLog(ServerUtility.class);

    private static String localAddress;
    static {
        try {
            localAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException exception) {
            localAddress = "127.0.0.1";
        } catch (NoClassDefFoundError e)  {
            // Google App Engine
            localAddress = "GAE";
        }
    }

    public static String getLocalAddr(
        final HttpServletRequest request, final ServletContext servletContext) {

        if (request == null || servletContext == null) {
            return null;
        }
        String _localAddr = null;
        if (servletContext.getMajorVersion() > 2 ||
            (servletContext.getMajorVersion() == 2 && servletContext.getMinorVersion() >= 4)) {

            try {
                // Returns null in a portal environment.
                _localAddr = request.getLocalAddr();
            } catch (UnsupportedOperationException exception) {
                // JBoss Portal 2.6.x environment.
            }
        }
        return
            new ServletContextConfiguration("com.icesoft.faces", servletContext).
                getAttribute(
                    "localAddress",
                    _localAddr != null ? _localAddr : localAddress);
    }

    public static String getLocalAddr(
        final Request request, final ServletContext servletContext) {

        if (request == null || servletContext == null) {
            return null;
        }
        if (servletContext.getMajorVersion() > 2 ||
            (servletContext.getMajorVersion() == 2 && servletContext.getMinorVersion() >= 4)) {

            return request.getLocalAddr();
        } else {
            Configuration _configuration =
                new ServletContextConfiguration(
                    "com.icesoft.faces", servletContext);
            return _configuration.getAttribute("localAddress", localAddress);
        }
    }

    public static int getLocalPort(
        final HttpServletRequest request, final ServletContext servletContext) {

        if (request == null || servletContext == null) {
            return -1;
        }
        int _localPort = 0;
        if (servletContext.getMajorVersion() > 2 ||
            (servletContext.getMajorVersion() == 2 && servletContext.getMinorVersion() >= 4)) {

            _localPort = request.getLocalPort(); // returns 0 in portal env.
        }
        if (_localPort != 0) {
            return
                new ServletContextConfiguration(
                    "com.icesoft.faces", servletContext
                ).getAttributeAsInteger("localPort", _localPort);
        } else {
            try {
                return
                    new ServletContextConfiguration(
                        "com.icesoft.faces", servletContext
                    ).getAttributeAsInteger("localPort");
            } catch (ConfigurationException exception) {
                String _serverInfo = servletContext.getServerInfo();
                if (
                    // GlassFish
                    _serverInfo.startsWith("Sun Java System Application Server") ||
                    _serverInfo.startsWith("Sun GlassFish Enterprise Server") ||
                    // JBoss 4.2.x and up
                    _serverInfo.startsWith("JBoss") ||
                    // JBoss 4.0.x and Tomcat
                    _serverInfo.startsWith("Apache Tomcat") ||
                    // Jetty
                    _serverInfo.startsWith("jetty")) {

                    return 8080;
                } else if (
                    // WebLogic
                    _serverInfo.startsWith("WebLogic")) {

                    return 7001;
                } else {
                    return 8080;
                }
            }
        }
    }

    public static String getServletContextPath(
        final ServletContext servletContext) {

        if (servletContext == null) {
            return null;
        }
        if (servletContext.getMajorVersion() > 2 ||
            (servletContext.getMajorVersion() == 2 && servletContext.getMinorVersion() >= 5)) {

            return servletContext.getContextPath();
        } else {
            try {
                String _servletContextPath;
                String _path = servletContext.getResource("/WEB-INF/web.xml").getPath();
                String _serverInfo = servletContext.getServerInfo();
                if (_serverInfo.startsWith("jetty")) {
                    _servletContextPath =
                        _path.substring(
                            _path.indexOf("__") + 2,
                            _path.lastIndexOf("__"));
                } else if (
                    _serverInfo.startsWith("WebLogic") &&
                    (
                        _serverInfo.indexOf("9.") != -1 ||
                        _serverInfo.indexOf("10.") != -1
                    )) {

                    int _index = _path.lastIndexOf("/");
                    for (int i = 0; i < 3; i++) {
                        _index = _path.lastIndexOf("/", _index - 1);
                    }
                    _servletContextPath =
                        _path.substring(
                            _path.lastIndexOf("/", _index - 1) + 1, _index);
                } else {
                    int _index = _path.lastIndexOf("/", _path.lastIndexOf("/") - 1);
                    _servletContextPath =
                        _path.substring(
                            _path.lastIndexOf("/", _index - 1) + 1, _index);
                    if (_serverInfo.startsWith("WebLogic")) {
                        _servletContextPath =
                            _servletContextPath.substring(
                                0, _servletContextPath.indexOf(".war"));
                    }
                }
                return _servletContextPath;
            } catch (MalformedURLException exception) {
                return null;
            }
        }
    }

    public static boolean isIPlanet(final ServletContext servletContext) {
        /*
         * iPlanet Web Server server info strings:
         *
         * - 7.0: Oracle iPlanet Web Server/7.0
         */
        String _serverInfo = servletContext.getServerInfo();
        return _serverInfo.startsWith("Oracle iPlanet Web Server");
    }
}
