package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.context.AbstractAttributeMap;
import com.icesoft.faces.context.AbstractCopyingAttributeMap;
import com.icesoft.faces.context.BridgeExternalContext;
import com.icesoft.faces.env.AcegiAuthWrapper;
import com.icesoft.faces.env.AuthenticationVerifier;
import com.icesoft.faces.env.ServletEnvironmentRequest;
import com.icesoft.faces.util.EnumerationIterator;
import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.http.common.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    private static final Log Log = LogFactory.getLog(ServletExternalContext.class);
    private static final RequestAttributes NOOPRequestAttributes = new RequestAttributes() {
        public Object getAttribute(String name) {
            return null;
        }

        public Enumeration getAttributeNames() {
            return Collections.enumeration(Collections.EMPTY_LIST);
        }

        public void removeAttribute(String name) {
        }

        public void setAttribute(String name, Object value) {
        }
    };
    private static Class AuthenticationClass;

    static {
        try {
            AuthenticationClass = Class.forName("org.acegisecurity.Authentication");
            Log.debug("Acegi Security detected.");
        } catch (Throwable t) {
            Log.debug("Acegi Security not detected.");
        }
    }

    private final ServletContext context;
    private final HttpSession session;
    private final AuthenticationVerifier authenticationVerifier;
    private RequestAttributes requestAttributes;
    private HttpServletRequest initialRequest;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public ServletExternalContext(String viewIdentifier, final Object request, Object response, CommandQueue commandQueue, Configuration configuration, final SessionDispatcher.Listener.Monitor sessionMonitor) {
        super(viewIdentifier, commandQueue, configuration);
        this.request = (HttpServletRequest) request;
        this.session = new InterceptingHttpSession(this.request.getSession(), sessionMonitor);
        this.context = this.session.getServletContext();
        this.authenticationVerifier = createAuthenticationVerifier();
        this.requestAttributes = new ActiveRequestAttributes();
        this.initialRequest = new ServletEnvironmentRequest(request, authenticationVerifier, session) {
            public RequestAttributes requestAttributes() {
                return requestAttributes;
            }
        };
        this.response = (HttpServletResponse) response;
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
        // #ICE-1722 default to normal mode before the first request
        switchToNormalMode();
    }

    public Object getSession(boolean create) {
        return session;
    }

    public Object getContext() {
        return context;
    }

    public Object getRequest() {
        return initialRequest;
    }

    public Object getResponse() {
        return response;
    }

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

        this.request = request;
        this.response = response;
    }

    public void updateOnReload(Object request, Object response) {
        Map previousRequestMap = this.requestMap;
        this.initialRequest = new ServletEnvironmentRequest(request, authenticationVerifier, session) {
            public RequestAttributes requestAttributes() {
                return requestAttributes;
            }
        };
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
        return initialRequest.getLocale();
    }

    public Iterator getRequestLocales() {
        return new EnumerationIterator(initialRequest.getLocales());
    }

    public String getRequestPathInfo() {
        return convertEmptyStringToNull(requestPathInfo == null ? initialRequest.getPathInfo() : requestPathInfo);
    }

    public String getRequestURI() {
        String requestURI = (String) initialRequest.getAttribute("javax.servlet.forward.request_uri");
        return requestURI == null ? initialRequest.getRequestURI() : requestURI;
    }

    public String getRequestContextPath() {
        String contextPath = (String) initialRequest.getAttribute("javax.servlet.forward.context_path");
        return contextPath == null ? initialRequest.getContextPath() : contextPath;
    }

    public String getRequestServletPath() {
        //crazy "workaround": solves the different behaviour MyFaces and Icefaces (including Sun-RI) need from this method
        boolean callFromMyfaces = new Exception().getStackTrace()[1].getClassName().startsWith("org.apache.myfaces");
        if (callFromMyfaces) {
            return requestServletPath == null ? initialRequest.getServletPath() : requestServletPath;
        } else {
            String servletPath = (String) initialRequest.getAttribute("javax.servlet.forward.servlet_path");
            return servletPath == null ? initialRequest.getServletPath() : servletPath;
        }
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
            initialRequest.getRequestDispatcher(path).forward(initialRequest, response);
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
        return initialRequest.getAuthType();
    }

    public String getRemoteUser() {
        return initialRequest.getRemoteUser();
    }

    public Principal getUserPrincipal() {
        return initialRequest.getUserPrincipal();
    }

    public boolean isUserInRole(String role) {
        return initialRequest.isUserInRole(role);
    }

    public Writer getWriter(String encoding) throws IOException {
        try {
            return new OutputStreamWriter(response.getOutputStream(), encoding);
        } catch (IllegalStateException e) {
            // getWriter() already called, perhaps because of JSP include
            return response.getWriter();
        }
    }

    /**
     * Switch to normal redirection mode, using the HTTPServletResponse object
     */
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

    public void release() {
        super.release();
        //disable any changes on the request once the response was commited
        requestAttributes = NOOPRequestAttributes;
    }

    private class RequestAttributeMap extends AbstractCopyingAttributeMap {
        public Enumeration getAttributeNames() {
            return initialRequest.getAttributeNames();
        }

        public Object getAttribute(String name) {
            return initialRequest.getAttribute(name);
        }

        public void setAttribute(String name, Object value) {
            initialRequest.setAttribute(name, value);
        }

        public void removeAttribute(String name) {
            initialRequest.removeAttribute(name);
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

    public class InterceptingHttpSession extends ProxyHttpSession {
        private final SessionDispatcher.Listener.Monitor sessionMonitor;

        public InterceptingHttpSession(HttpSession session, SessionDispatcher.Listener.Monitor sessionMonitor) {
            super(session);
            this.sessionMonitor = sessionMonitor;
        }

        public void invalidate() {
            sessionMonitor.shutdown();
        }
    }

    private AuthenticationVerifier createAuthenticationVerifier() {
        Principal principal = request.getUserPrincipal();
        if (AuthenticationClass != null && AuthenticationClass.isInstance(principal)) {
            return new AcegiAuthWrapper(principal);
        } else {
            return new AuthenticationVerifier() {
                public boolean isUserInRole(String role) {
                    return request.isUserInRole(role);
                }
            };
        }
    }

    private class ActiveRequestAttributes implements RequestAttributes {
        public Object getAttribute(String name) {
            return request.getAttribute(name);
        }

        public Enumeration getAttributeNames() {
            return request.getAttributeNames();
        }

        public void removeAttribute(String name) {
            request.removeAttribute(name);
        }

        public void setAttribute(String name, Object value) {
            request.setAttribute(name, value);
        }
    }
}