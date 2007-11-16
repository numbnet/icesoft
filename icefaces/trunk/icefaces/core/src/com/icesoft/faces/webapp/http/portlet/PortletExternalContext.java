package com.icesoft.faces.webapp.http.portlet;

import com.icesoft.faces.context.BridgeExternalContext;
import com.icesoft.faces.env.AuthenticationVerifier;
import com.icesoft.faces.env.PortletEnvironmentRenderRequest;
import com.icesoft.faces.env.RequestAttributes;
import com.icesoft.faces.util.EnumerationIterator;
import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.servlet.ServletRequestAttributes;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;
import com.icesoft.jasper.Constants;

import javax.faces.FacesException;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
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
    private static final AllowMode DoNotAllow = new AllowMode() {
        public boolean isPortletModeAllowed(PortletMode portletMode) {
            return false;
        }

        public boolean isWindowStateAllowed(WindowState windowState) {
            return false;
        }
    };
    private static final Dispatcher RequestNotAvailable = new Dispatcher() {
        public void dispatch(String path) throws IOException, FacesException {
            throw new IOException("No request available for dispatch.");
        }
    };
    private static final AuthenticationVerifier UserInfoNotAvailable = new AuthenticationVerifier() {
        public boolean isUserInRole(String role) {
            throw new RuntimeException("Cannot determine if user in role. User information is not available.");
        }
    };
    private static final Dispatcher CannotDispatchOnXMLHTTPRequest = new Dispatcher() {
        public void dispatch(String path) throws IOException, FacesException {
            throw new IOException("Cannot dispatch on XMLHTTP request.");
        }
    };
    private static final Redirector NOOPRedirector = new Redirector() {
        public void redirect(String uri) {
        }
    };
    private static final CookieTransporter NOOPCookieTransporter = new CookieTransporter() {
        public void send(Cookie cookie) {
        }
    };
    private final PortletContext context;
    private final PortletConfig config;
    private final PortletSession session;
    private RenderRequest initialRequest;
    private RenderResponse response;
    private AllowMode allowMode;
    private AuthenticationVerifier authenticationVerifier;
    private Dispatcher dispatcher;
    private RequestAttributes requestAttributes;

    public PortletExternalContext(String viewIdentifier, final Object request, Object response, CommandQueue commandQueue, Configuration configuration, final SessionDispatcher.Monitor monitor, Object portletConfig) {
        super(viewIdentifier, commandQueue, configuration);
        final RenderRequest renderRequest = (RenderRequest) request;
        final RenderResponse renderResponse = (RenderResponse) response;

        config = (PortletConfig) portletConfig;
        session = new InterceptingPortletSession(renderRequest.getPortletSession(), monitor);
        context = session.getPortletContext();
        initParameterMap = new PortletContextInitParameterMap(context);
        applicationMap = new PortletContextAttributeMap(context);
        sessionMap = new PortletSessionAttributeMap(session);
        updateOnReload(renderRequest, renderResponse);
        insertNewViewrootToken();
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

    public void update(final HttpServletRequest request, HttpServletResponse response) {
        //update parameters
        boolean persistSeamKey = isSeamLifecycleShortcut();
        recreateParameterAndCookieMaps();

        //#2139 removed call to insert postback key here.
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            requestParameterMap.put(name, request.getParameter(name));
            requestParameterValuesMap.put(name, request.getParameterValues(name));
        }

        applySeamLifecycleShortcut(persistSeamKey);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                requestCookieMap.put(cookie.getName(), cookie);
            }
        }
        allowMode = DoNotAllow;
        authenticationVerifier = new AuthenticationVerifier() {
            public boolean isUserInRole(String role) {
                return request.isUserInRole(role);
            }
        };
        dispatcher = CannotDispatchOnXMLHTTPRequest;
        requestAttributes = new ServletRequestAttributes(request);
    }

    public void update(final RenderRequest request, final RenderResponse response) {
        //update parameters
        boolean persistSeamKey = isSeamLifecycleShortcut();
        recreateParameterAndCookieMaps();

        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            requestParameterMap.put(name, request.getParameter(name));
            requestParameterValuesMap.put(name, request.getParameterValues(name));
        }

        applySeamLifecycleShortcut(persistSeamKey);

        allowMode = new PortletRequestAllowMode(request);
        authenticationVerifier = new AuthenticationVerifier() {
            public boolean isUserInRole(String role) {
                return request.isUserInRole(role);
            }
        };
        dispatcher = new Dispatcher() {
            public void dispatch(String path) throws IOException, FacesException {
                try {
                    context.getRequestDispatcher(path).include(request, response);
                } catch (PortletException e) {
                    throw new FacesException(e);
                }
            }
        };
        requestAttributes = new PortletRequestAttributes(request);

        this.response = response;
    }

    public void updateOnReload(Object request, Object response) {
        RenderRequest renderRequest = (RenderRequest) request;
        RenderResponse renderResponse = (RenderResponse) response;
        initialRequest = new PortletEnvironmentRenderRequest(session, renderRequest) {
            public AllowMode allowMode() {
                return allowMode;
            }

            public AuthenticationVerifier authenticationVerifier() {
                return authenticationVerifier;
            }

            public RequestAttributes requestAttributes() {
                return requestAttributes;
            }
        };
        requestMap = new PortletRequestAttributeMap(initialRequest);
        update(renderRequest, renderResponse);
    }

    public Map getRequestHeaderMap() {
        return Collections.EMPTY_MAP;
    }

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
        return (String) initialRequest.getAttribute(Constants.INC_PATH_INFO);
    }

    public String getRequestURI() {
        return (String) initialRequest.getAttribute(Constants.INC_REQUEST_URI);
    }

    public String getRequestContextPath() {
        return (String) initialRequest.getAttribute(Constants.INC_CONTEXT_PATH);
    }

    public String getRequestServletPath() {
        return (String) initialRequest.getAttribute(Constants.INC_SERVLET_PATH);
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
        dispatcher.dispatch(path);
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

    public java.security.Principal getUserPrincipal() {
        return initialRequest.getUserPrincipal();
    }

    public boolean isUserInRole(String role) {
        return initialRequest.isUserInRole(role);
    }

    public Writer getWriter(String encoding) throws IOException {
        return response.getWriter();
    }

    public PortletConfig getConfig() {
        return config;
    }

    public void switchToNormalMode() {
        redirector = NOOPRedirector;
        cookieTransporter = NOOPCookieTransporter;
    }

    public void switchToPushMode() {
        redirector = new CommandQueueRedirector();
        cookieTransporter = new CommandQueueCookieTransporter();
        resetRequestMap();
    }

    public void release() {
        super.release();
        allowMode = DoNotAllow;
        authenticationVerifier = UserInfoNotAvailable;
        dispatcher = RequestNotAvailable;
        requestAttributes = NOOPRequestAttributes;
    }

    private void recreateParameterAndCookieMaps() {
        requestParameterMap = Collections.synchronizedMap(new HashMap());
        requestParameterValuesMap = Collections.synchronizedMap(new HashMap());
        requestCookieMap = Collections.synchronizedMap(new HashMap());
        responseCookieMap = Collections.synchronizedMap(new HashMap());
    }
}
