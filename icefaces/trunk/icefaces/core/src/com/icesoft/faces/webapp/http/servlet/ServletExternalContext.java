package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.context.AbstractAttributeMap;
import com.icesoft.faces.context.AbstractCopyingAttributeMap;
import com.icesoft.faces.context.BridgeExternalContext;
import com.icesoft.faces.util.EnumerationIterator;
import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.http.common.Configuration;

import javax.faces.FacesException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ServletExternalContext extends BridgeExternalContext {
    private ServletContext context;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    public ServletExternalContext(String viewIdentifier, final Object request, Object response, CommandQueue commandQueue, Configuration configuration) {
        super(viewIdentifier, commandQueue, configuration);
        this.request = (HttpServletRequest) request;
        this.response = (HttpServletResponse) response;
        this.session = this.request.getSession();
        this.context = this.session.getServletContext();
        this.initParameterMap = new AbstractAttributeMap() {
            protected Object getAttribute(String key) {
                return context.getInitParameter(key);
            }

            protected void setAttribute(String key, Object value) {
                throw new IllegalAccessError("Read only map.");
            }

            protected void removeAttribute(String key) {
                throw new IllegalAccessError("Read only map.");
            }

            protected Enumeration getAttributeNames() {
                return context.getInitParameterNames();
            }
        };
        this.applicationMap = new AbstractAttributeMap() {
            protected Object getAttribute(String key) {
                return context.getAttribute(key);
            }

            protected void setAttribute(String key, Object value) {
                context.setAttribute(key, value);
            }

            protected void removeAttribute(String key) {
                context.removeAttribute(key);
            }

            protected Enumeration getAttributeNames() {
                return context.getAttributeNames();
            }
        };
        this.sessionMap = new AbstractAttributeMap() {
            protected Object getAttribute(String key) {
                return session.getAttribute(key);
            }

            protected void setAttribute(String key, Object value) {
                session.setAttribute(key, value);
            }

            protected void removeAttribute(String key) {
                session.removeAttribute(key);
            }

            protected Enumeration getAttributeNames() {
                return session.getAttributeNames();
            }
        };
        this.requestMap = new RequestAttributeMap();
        this.requestCookieMap = new HashMap();

        this.update(this.request, this.response);
        this.insertNewViewrootToken();
    }

    public Object getSession(boolean create) {
        return session;
    }

    public Object getContext() {
        return context;
    }

    public Object getRequest() {
        return request;
    }

    public Object getResponse() {
        return response;
    }

    public void update(HttpServletRequest request, HttpServletResponse response) {
        //update parameters
        boolean persistSeamKey = isSeamLifecycleShortcut();

        requestParameterMap = new HashMap();
        requestParameterValuesMap = new HashMap();
        insertPostbackKey();
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            Object value = request.getParameter(name);
            requestParameterMap.put(name, value);
            requestParameterValuesMap.put(name, request.getParameterValues(name));
        }

        applySeamLifecycleShortcut(persistSeamKey);

        requestCookieMap = new HashMap();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                requestCookieMap.put(cookie.getName(), cookie);
            }
        }
        responseCookieMap = new HashMap();

        this.response = response;
    }

    public void updateOnReload(Object request, Object response) {
        Map previousRequestMap = this.requestMap;
        this.request = (HttpServletRequest) request;
        this.requestMap = new RequestAttributeMap();
        //propagate entries
        this.requestMap.putAll(previousRequestMap);
        this.update((HttpServletRequest) request, (HttpServletResponse) response);
    }

    //todo: implement!
    public Map getRequestHeaderMap() {
        return Collections.EMPTY_MAP;
    }

    //todo: implement!
    public Map getRequestHeaderValuesMap() {
        return Collections.EMPTY_MAP;
    }

    public Locale getRequestLocale() {
        return request.getLocale();
    }

    public Iterator getRequestLocales() {
        return new EnumerationIterator(request.getLocales());
    }

    public String getRequestPathInfo() {
        if (requestPathInfo != null && requestPathInfo.trim().length() > 0) {
            return requestPathInfo;
        }

        //If we start out null (because it hasn't been specifically set) then
        //use the wrapped request value.
        if (requestPathInfo == null) {
            requestPathInfo = request.getPathInfo();
        }

        //We need to fix any occurrences of the "" (the empty String) because
        //the JSF Lifecycle implementations won't be able to properly create
        //a view ID otherwise because they check for null but not the empty
        //String.
        return requestPathInfo = convertEmptyStringToNull(requestPathInfo);
    }

    public String getRequestURI() {
        return request.getRequestURI();
    }

    public String getRequestContextPath() {
        return request.getContextPath();
    }

    public String getRequestServletPath() {
        return null == requestServletPath ? request.getServletPath() : requestServletPath;
    }

    public Set getResourcePaths(String path) {
        return context.getResourcePaths(path);
    }

    public URL getResource(String path) throws MalformedURLException {
        return context.getResource(path);
    }

    public InputStream getResourceAsStream(String path) {
        return context.getResourceAsStream(path);
    }

    public String encodeActionURL(String url) {
        return url;
    }

    public String encodeResourceURL(String url) {
        try {
            return response.encodeURL(url);
        } catch (Exception e) {
            return url;
        }
    }

    public String encodeNamespace(String name) {
        return name;
    }

    public void dispatch(String path) throws IOException, FacesException {
        try {
            request.getRequestDispatcher(path).forward(request, response);
        } catch (ServletException se) {
            throw new FacesException(se);
        }
    }

    public void log(String message) {
        context.log(message);
    }

    public void log(String message, Throwable throwable) {
        context.log(message, throwable);
    }

    public String getAuthType() {
        return request.getAuthType();
    }

    public String getRemoteUser() {
        return request.getRemoteUser();
    }

    public Principal getUserPrincipal() {
        return request.getUserPrincipal();
    }

    public boolean isUserInRole(String role) {
        return request.isUserInRole(role);
    }

    public Writer getWriter(String encoding) throws IOException {
        try {
            return new OutputStreamWriter(response.getOutputStream(), encoding);
        } catch (IllegalStateException e) {
            // getWriter() already called, perhaps because of JSP include
            return response.getWriter();
        }
    }

    public void switchToNormalMode() {
        redirector = new Redirector() {
            public void redirect(String uri) {
                try {
                    response.sendRedirect(uri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        cookieTransporter = new CookieTransporter() {
            public void send(Cookie cookie) {
                response.addCookie(cookie);
            }
        };
    }

    public void switchToPushMode() {
        redirector = new CommandQueueRedirector();
        cookieTransporter = new CommandQueueCookieTransporter();
        resetRequestMap();
    }

    private class RequestAttributeMap extends AbstractCopyingAttributeMap {
        public Enumeration getAttributeNames() {
            return request.getAttributeNames();
        }

        public Object getAttribute(String name) {
            return request.getAttribute(name);
        }

        public void setAttribute(String name, Object value) {
            request.setAttribute(name, value);
        }

        public void removeAttribute(String name) {
            request.removeAttribute(name);
        }
    }

    /**
     * Utility method that returns the original value of the supplied String
     * unless it is emtpy (val.trim().length() == 0).  In that particlar case
     * the value returned is null.
     *
     * @param val
     * @return
     */
    private static String convertEmptyStringToNull(String val) {
        return val == null || val.trim().length() == 0 ? null : val;
    }
}
