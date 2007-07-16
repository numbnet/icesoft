package com.icesoft.faces.webapp.http.portlet;

import com.icesoft.faces.context.AbstractAttributeMap;
import com.icesoft.faces.context.AbstractCopyingAttributeMap;
import com.icesoft.faces.context.BridgeExternalContext;
import com.icesoft.faces.util.EnumerationIterator;
import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.command.Redirect;
import com.icesoft.faces.webapp.command.SetCookie;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.jasper.Constants;
import com.icesoft.util.SeamUtilities;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
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
import java.net.URI;
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
    private RenderRequest request;
    private RenderResponse response;
    private PortletSession session;
    private Map applicationMap;
    private Map sessionMap;
    private Map requestParameterMap;
    private Map requestParameterValuesMap;
    private Map requestMap;
    private Map requestCookieMap;
    private Map responseCookieMap;
    private Redirector redirector;
    private CookieTransporter cookieTransporter;
    private String requestServletPath;
    private String requestPathInfo;

    public PortletExternalContext(String viewIdentifier, final Object request, Object response, CommandQueue commandQueue, Configuration configuration) {
        super(viewIdentifier, commandQueue, configuration);
        this.request = (RenderRequest) request;
        this.response = (RenderResponse) response;
        this.session = this.request.getPortletSession();
        this.context = this.session.getPortletContext();
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

    public Map getApplicationMap() {
        return applicationMap;
    }

    public Map getSessionMap() {
        return sessionMap;
    }

    public Map getApplicationSessionMap() {
        return sessionMap;
    }

    public Map getRequestMap() {
        return requestMap;
    }

    //todo: try to reuse functionality from the next method
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
        this.request = (RenderRequest) request;
        this.requestMap = new RequestAttributeMap();
        //propagate entries
        this.requestMap.putAll(previousRequestMap);
        this.update((RenderRequest) request, (RenderResponse) response);
    }

    public Map getRequestParameterMap() {
        return requestParameterMap;
    }

    public Map getRequestParameterValuesMap() {
        return requestParameterValuesMap;
    }

    public Iterator getRequestParameterNames() {
        return requestParameterMap.keySet().iterator();
    }

    public Map getRequestHeaderMap() {
        return Collections.EMPTY_MAP;
    }

    public Map getRequestHeaderValuesMap() {
        return Collections.EMPTY_MAP;
    }

    public Map getRequestCookieMap() {
        return requestCookieMap;
    }

    public Locale getRequestLocale() {
        return request.getLocale();
    }

    public Iterator getRequestLocales() {
        return new EnumerationIterator(request.getLocales());
    }

    public void setRequestPathInfo(String viewId) {
        requestPathInfo = viewId;
    }

    public String getRequestPathInfo() {
        return requestPathInfo;
    }

    public String getRequestURI() {
        return (String) request.getAttribute(Constants.INC_REQUEST_URI);
    }

    public String getRequestContextPath() {
        return request.getContextPath();
    }

    public void setRequestServletPath(String path) {
        requestServletPath = path;
    }

    public String getRequestServletPath() {
        return requestServletPath;
    }

    public String getInitParameter(String name) {
        return context.getInitParameter(name);
    }

    public Map getInitParameterMap() {
        return new AbstractAttributeMap() {
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

    public void redirect(String requestURI) throws IOException {
        URI uri = URI.create(SeamUtilities.encodeSeamConversationId(requestURI, viewIdentifier));
        String query = uri.getQuery();
        if (query == null) {
            redirector.redirect(uri + "?rvn=" + viewIdentifier);
        } else if (query.matches(".*rvn=.*")) {
            redirector.redirect(uri.toString());
        } else {
            redirector.redirect(uri + "&rvn=" + viewIdentifier);
        }
        FacesContext.getCurrentInstance().responseComplete();
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

    public void addCookie(Cookie cookie) {
        responseCookieMap.put(cookie.getName(), cookie);
        cookieTransporter.send(cookie);
    }

    public Map getResponseCookieMap() {
        return responseCookieMap;
    }

    public Writer getWriter(String encoding) throws IOException {
        return response.getWriter();
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
        redirector = new PortletExternalContext.Redirector() {
            public void redirect(String uri) {
                commandQueue.put(new Redirect(uri));
            }
        };

        cookieTransporter = new PortletExternalContext.CookieTransporter() {
            public void send(Cookie cookie) {
                commandQueue.put(new SetCookie(cookie));
            }
        };

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
