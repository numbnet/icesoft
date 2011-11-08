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

package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.portlet.PortletArtifactWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServletRequestResponse implements Request, Response {
    private final static Log log = LogFactory.getLog(ServletRequestResponse.class);
    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    private static Pattern HEADER_FIXER = null;

    private URI requestURI;

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        HEADER_FIXER = Pattern.compile("[\r\n]");
    }

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected boolean disableRemoteHostLookup;

    public ServletRequestResponse(final HttpServletRequest request, final HttpServletResponse response, final Configuration configuration) throws Exception {
        this.request = request;
        this.response = response;
        this.disableRemoteHostLookup = configuration.getAttributeAsBoolean("disableRemoteHostLookup", false);

        //Need to determine the type of request URI we are using based on the
        //environment we are running in (servlet vs portlet).
        detectEnvironment(new Environment() {
            public void servlet(Object request, Object response) {
                HttpServletRequest req = (HttpServletRequest) request;
                String query = req.getQueryString();
                URI uri = null;
                while (null == uri) {
                    try {
                        uri = URI.create(req.getRequestURL().toString());
                    } catch (NullPointerException e) {
                        //TODO remove this catch block when GlassFish bug is addressed
                        if (log.isDebugEnabled()) {
                            log.debug("Null Protocol Scheme in request", e);
                        }
                        uri = URI.create("http://" + req.getServerName() + ":"
                                + req.getServerPort() + req.getRequestURI());
                    }
                }
                requestURI = (query == null ? uri : URI.create(uri + "?" + query));
            }

            public void portlet(Object request, Object response, Object portletConfig) {
                javax.portlet.PortletRequest req = (javax.portlet.PortletRequest) request;
                String reqURI = (String) req.getAttribute(com.icesoft.jasper.Constants.INC_REQUEST_URI);
                requestURI = URI.create(reqURI);
            }
        });
    }

    public String getMethod() {
        return request.getMethod();
    }

    public URI getURI() {
        return requestURI;
    }

    public String[] getHeaderNames() {
        List headerNames = new ArrayList();
        Enumeration e = request.getHeaderNames();
        while (e.hasMoreElements()) {
            headerNames.add(e.nextElement());
        }
        return (String[])headerNames.toArray(new String[headerNames.size()]);
    }
    
    public String getHeader(String name) {
        return request.getHeader(name);
    }

    public String[] getHeaderAsStrings(String name) {
        Enumeration e = request.getHeaders(name);
        ArrayList values = new ArrayList();
        while (e.hasMoreElements()) values.add(e.nextElement());
        return (String[]) values.toArray(new String[values.size()]);
    }

    public Date getHeaderAsDate(String name) {
        try {
            return DATE_FORMAT.parse(request.getHeader(name));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public int getHeaderAsInteger(String name) {
        return Integer.parseInt(request.getHeader(name));
    }

    public boolean containsParameter(String name) {
        return request.getParameter(name) != null;
    }

    public String[] getParameterNames() {
        Collection result = request.getParameterMap().keySet();
        return (String[]) result.toArray(new String[result.size()]);
    }

    public String getParameter(String name) {
        checkExistenceOf(name);
        return (String) request.getParameter(name);
    }

    public String[] getParameterAsStrings(String name) {
        checkExistenceOf(name);
        return request.getParameterValues(name);
    }

    public int getParameterAsInteger(String name) {
        return Integer.parseInt(getParameter(name));
    }

    public boolean getParameterAsBoolean(String name) {
        return Boolean.valueOf(getParameter(name)).booleanValue();
    }

    public String getParameter(String name, String defaultValue) {
        try {
            return getParameter(name);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int getParameterAsInteger(String name, int defaultValue) {
        try {
            return getParameterAsInteger(name);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean getParameterAsBoolean(String name, boolean defaultValue) {
        try {
            return getParameterAsBoolean(name);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public Cookie[] getCookies() {
        return request.getCookies();
    }

    public String getLocalAddr() {
        return request.getLocalAddr();
    }

    public String getLocalName() {
        return request.getLocalName();
    }

    public String getRemoteAddr() {
        return request.getRemoteAddr();
    }

    public String getRemoteHost() {
        if (!disableRemoteHostLookup) {
            return request.getRemoteHost();
        } else {
            return request.getRemoteAddr();
        }
    }

    public String getServerName() {
        return request.getServerName();
    }

    public InputStream readBody() throws IOException {
        return request.getInputStream();
    }

    public void readBodyInto(OutputStream out) throws IOException {
        copy(readBody(), out);
    }

    public void respondWith(ResponseHandler handler) throws Exception {
        handler.respond(this);
    }

    public void setStatus(int code) {
        response.setStatus(code);
    }

    public void setHeader(String name, String value) {
        if (ignoreHeader(name, value)) return;
        //CR and LF embedded in headers can corrupt the HTTP response
        value = HEADER_FIXER.matcher(value).replaceAll("");
        if ("Content-Type".equals(name)) {
            response.setContentType(value);
        } else if ("Content-Length".equals(name)) {
            response.setContentLength(Integer.parseInt(value));
        } else {
            response.setHeader(name, value);
        }
    }

    public void setHeader(String name, String[] values) {
        if (ignoreHeader(name, values)) return;
        for (int i = 0; i < values.length; i++) {
            String safeValue = HEADER_FIXER.matcher(values[i]).replaceAll("");
            response.addHeader(name, safeValue);
        }
    }

    public void setHeader(String name, Date value) {
        if (ignoreHeader(name, value)) return;
        response.setDateHeader(name, value.getTime());
    }

    public void setHeader(String name, int value) {
        response.setIntHeader(name, value);
    }

    public void setHeader(String name, long value) {
        response.setHeader(name, String.valueOf(value));
    }

    public void addCookie(Cookie cookie) {
        response.addCookie(cookie);
    }

    public OutputStream writeBody() throws IOException {
        return response.getOutputStream();
    }

    public void writeBodyFrom(InputStream in) throws IOException {
        try {
            copy(in, writeBody());
        } finally {
            in.close();
        }
    }

    public void detectEnvironment(Environment environment) throws Exception {

        Object portletEnvironment = request.getAttribute(PortletArtifactWrapper.PORTLET_ARTIFACT_KEY);
        if (portletEnvironment == null) {
            environment.servlet(request, response);
        } else {
            PortletArtifactWrapper portletArtifact = (PortletArtifactWrapper) portletEnvironment;
            environment.portlet(portletArtifact.getRequest(),
                    portletArtifact.getResponse(),
                    portletArtifact.getPortletConfig());

            //Due to the fact that the original portlet request used a RequestDispatcher to
            //connect to the ICEfaces framework, we need to adjust the java.servlet.include*
            //attributes in the original portlet request to match the dispatched request.
            String[] incKeys = com.icesoft.jasper.Constants.INC_CONSTANTS;
            for (int index = 0; index < incKeys.length; index++) {
                String incVal = (String) request.getAttribute(incKeys[index]);
                if (incVal != null) {
                    portletArtifact.getRequest().setAttribute(incKeys[index], incVal);
                } else {
                    portletArtifact.getRequest().removeAttribute(incKeys[index]);
                }
            }
        }
    }

    private static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buf = new byte[4096];
        int len = 0;
        while ((len = input.read(buf)) > -1) output.write(buf, 0, len);
    }

    private void checkExistenceOf(String name) {
        if (request.getParameter(name) == null) {

            // This block is removable once we find out why sometimes the request
            // object appears a little corrupted.

            String host = getRemoteHost();
            StringBuffer data = new StringBuffer("+ Request does not contain parameter '" + name + "' host: \n");
            data.append("  Originator: ").append(host).append("\n");
            data.append("  Path: ").append(requestURI.toString()).append("\n");

            Enumeration e = request.getParameterNames();
            String key;
            int i = 0;

            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                if (i == 0) {
                    data.append("  Available request parameters are: \n");
                }
                data.append("  - parameter name: ").append(key).append(", value: ").append(request.getParameter(key)).append("\n");
                i++;
            }
            if (i == 0) {
                data.append("   Request map is empty!\n");
            }

            data.append("- SRR hashcode: ").append(this.hashCode()).append(" Servlet request hash: ").append(request.hashCode());
            log.debug(data.toString());
            // we can't just carry on. We seriously need those paramters ...
            throw new RuntimeException("Query does not contain parameter named: " + name);

        }
    }

    private static boolean ignoreHeader(String name, Object value) {
        return name == null || value == null;
    }
}
