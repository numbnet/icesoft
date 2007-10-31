package com.icesoft.faces.webapp.http.portlet;

import com.icesoft.faces.context.AbstractAttributeMap;
import com.icesoft.faces.context.AbstractCopyingAttributeMap;
import com.icesoft.faces.context.BridgeExternalContext;
import com.icesoft.faces.env.PortletEnvironmentRenderRequest;
import com.icesoft.faces.util.EnumerationIterator;
import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;
import com.icesoft.jasper.Constants;

import javax.faces.FacesException;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class PortletExternalContext extends BridgeExternalContext {
    private PortletContext context;
    private PortletConfig config;
    private RenderRequest request;
    private RenderResponse response;
    private PortletSession session;

    public PortletExternalContext(String viewIdentifier, final Object request, Object response, CommandQueue commandQueue, Configuration configuration, final SessionDispatcher.Listener.Monitor monitor, Object config) {
        super(viewIdentifier, commandQueue, configuration);
        this.config = (PortletConfig) config;
        this.request = new PortletEnvironmentRenderRequest(request);
        this.response = (RenderResponse) response;
        this.session = new ProxyPortletSession(this.request.getPortletSession()) {
            public void invalidate() {
                monitor.shutdown();
            }
        };
        this.context = this.session.getPortletContext();
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

    //todo: try to reuse functionality from the next method
    public void update(HttpServletRequest request, HttpServletResponse response) {
        //update parameters
        boolean persistSeamKey = isSeamLifecycleShortcut();

        requestParameterMap = Collections.synchronizedMap(new HashMap());
        requestParameterValuesMap = Collections.synchronizedMap(new HashMap());
        //#2139 removed call to insert postback key here. 
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            Object value = request.getParameter(name);
            requestParameterMap.put(name, value);
            requestParameterValuesMap.put(name, request.getParameterValues(name));
        }

        applySeamLifecycleShortcut(persistSeamKey);

        requestCookieMap = Collections.synchronizedMap(new HashMap());
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                requestCookieMap.put(cookie.getName(), cookie);
            }
        }
        responseCookieMap = Collections.synchronizedMap(new HashMap());
    }

    public void update(RenderRequest request, RenderResponse response) {
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
        responseCookieMap = new HashMap();

        this.response = response;
    }

    public void updateOnReload(Object request, Object response) {
        Map previousRequestMap = this.requestMap;
        this.request = new PortletEnvironmentRenderRequest(request);
        this.requestMap = new RequestAttributeMap();
        //propagate entries
        this.requestMap.putAll(previousRequestMap);
        this.update((RenderRequest) request, (RenderResponse) response);
    }

    public Map getRequestHeaderMap() {
        return Collections.EMPTY_MAP;
    }

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
        return (String) request.getAttribute(Constants.INC_PATH_INFO);
    }

    public String getRequestURI() {
        return (String) request.getAttribute(Constants.INC_REQUEST_URI);
    }

    public String getRequestContextPath() {
        return (String) request.getAttribute(Constants.INC_CONTEXT_PATH);
    }

    public String getRequestServletPath() {
        return (String) request.getAttribute(Constants.INC_SERVLET_PATH);
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
        return encodeResourceURL(url);
    }

    public String encodeResourceURL(String url) {
        try {
            return response.encodeURL(url);
        } catch (Exception e) {
            return url;
        }
    }

    public String encodeNamespace(String name) {
        return response.getNamespace() + name;
    }

    public void dispatch(String path) throws IOException, FacesException {
        try {
            context.getRequestDispatcher(path).include(request, response);
        } catch (PortletException e) {
            throw new FacesException(e);
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

    public java.security.Principal getUserPrincipal() {
        return request.getUserPrincipal();
    }

    public boolean isUserInRole(String role) {
        return request.isUserInRole(role);
    }

    public Writer getWriter(String encoding) throws IOException {
        return response.getWriter();
    }

    public PortletConfig getConfig() {
        return config;
    }

    public void switchToNormalMode() {
        redirector = new PortletExternalContext.Redirector() {
            public void redirect(String uri) {
                //cannot redirect
            }
        };

        cookieTransporter = new PortletExternalContext.CookieTransporter() {
            public void send(Cookie cookie) {
                //cannot send cookie
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
}
