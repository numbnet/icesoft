package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.context.BridgeExternalContext;
import com.icesoft.faces.util.EnumerationIterator;
import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.command.Redirect;
import com.icesoft.faces.webapp.command.SetCookie;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesCommonlet;
import com.icesoft.util.SeamUtilities;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
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

//for now extend BridgeExternalContext since there are so many bloody 'instanceof' tests
public class ServletExternalContext extends BridgeExternalContext {
    private static String postBackKey;
    static {
        //We will place VIEW_STATE_PARAM in the requestMap so that
        //JSF 1.2 doesn't think the request is a postback and skip
        //execution
        try {
            Field field = ResponseStateManager.class.getField("VIEW_STATE_PARAM");
            if (null != field) {
                postBackKey = (String) field.get(ResponseStateManager.class);
            }
        } catch (Exception e) {
        }
    }

    private String viewIdentifier;
    private ServletContext context;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private Map applicationMap;
    private Map sessionMap;
    private Map requestParameterMap;
    private Map requestParameterValuesMap;
    private Map initParameterMap;
    private Map requestMap;
    private Map requestCookieMap;
    private CommandQueue commandQueue;
    private Redirector redirector;
    private CookieTransporter cookieTransporter;

    public ServletExternalContext(String viewIdentifier, ServletContext context, HttpServletRequest request, HttpServletResponse response, CommandQueue commandQueue) {
        this.viewIdentifier = viewIdentifier;
        this.context = context;
        this.request = request;
        this.response = response;
        this.commandQueue = commandQueue;
        this.session = this.request.getSession();
        this.requestMap = new ServletRequestMap(this.request);
        this.applicationMap = new ServletApplicationMap(this.context);
        this.sessionMap = new ServletSessionMap(this.session);
        this.requestCookieMap = new HashMap();
        this.initParameterMap = new HashMap();
        Enumeration names = this.context.getInitParameterNames();
        while (names.hasMoreElements()) {
            String key = (String) names.nextElement();
            initParameterMap.put(key, this.context.getInitParameter(key));
        }

        this.update(this.request, this.response);
        this.setupSeamEnvironment();
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

    public void update(HttpServletRequest request, HttpServletResponse response) {
        //update parameters
        requestParameterMap = new HashMap();
        requestParameterValuesMap = new HashMap();
        insertPostbackKey();
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            requestParameterMap.put(name, request.getParameter(name));
            requestParameterValuesMap.put(name, request.getParameterValues(name));
        }
        requestCookieMap = new HashMap();
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                requestCookieMap.put(cookie.getName(), cookie);
            }
        }

        this.response = response;
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

    //todo: implement!
    public Map getRequestHeaderMap() {
        return Collections.EMPTY_MAP;
    }

    //todo: implement!
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

    private String requestPathInfo;

    public void setRequestPathInfo(String viewId) {
        requestPathInfo = viewId;
    }

    public String getRequestPathInfo() {
        return requestPathInfo == null ? request.getPathInfo() : requestPathInfo;
    }

    public String getRequestURI() {
        return request.getRequestURI();
    }

    public String getRequestContextPath() {
        return request.getContextPath();
    }

    private String requestServletPath;

    public void setRequestServletPath(String path) {
        requestServletPath = path;
    }

    public String getRequestServletPath() {
        return null == requestServletPath ? request.getServletPath() : requestServletPath;
    }

    public String getInitParameter(String name) {
        return context.getInitParameter(name);
    }

    public Map getInitParameterMap() {
        return initParameterMap;
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

    public void redirect(String requestURI) throws IOException {
        URI uri = URI.create(SeamUtilities.encodeSeamConversationId(requestURI));
        redirector.redirect(uri + (uri.getQuery() == null ? "?" : "&") + "rvn=" + viewIdentifier);
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
        cookieTransporter.send(cookie);
    }

    //todo: see if we can execute full JSP cycle all the time (not only when page is parsed)
    //todo: this way the bundles are put into the request map every time, so we don't have to carry
    //todo: them between requests
    public Map collectBundles() {
        Map result = new HashMap();
        Iterator entries = requestMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            Object value = entry.getValue();
            if (value != null) {
                String className = value.getClass().getName();
                if ((className.indexOf("LoadBundleTag") > 0) ||  //Sun RI
                        (className.indexOf("BundleMap") > 0)) {     //MyFaces
                    result.put(entry.getKey(), value);
                }
            }
        }

        return result;
    }

    public void setupSeamEnvironment() {
        //Any request handled by the PersistentFacesServlet should have the Seam
        //PageContext removed from our internal context complex. But we cannot do
        //it here, since the machinery is not yet in place, so put a flag into
        //the external context, allowing someone else to do it later.
        requestMap.put(PersistentFacesCommonlet.REMOVE_SEAM_CONTEXTS, Boolean.TRUE);
        // Always on a GET request, create a new ViewRoot. New theory.
        // This works now that the ViewHandler only calls restoreView once,
        // as opposed to calling it again from createView
        if (SeamUtilities.isSeamEnvironment()) {
            requestParameterMap.put(
                    PersistentFacesCommonlet.SEAM_LIFECYCLE_SHORTCUT,
                    Boolean.TRUE);
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
        redirector = new Redirector() {
            public void redirect(String uri) {
                commandQueue.put(new Redirect(uri));
            }
        };

        cookieTransporter = new CookieTransporter() {
            public void send(Cookie cookie) {
                commandQueue.put(new SetCookie(cookie));
            }
        };
    }

    /**
     * If this is found to be a Seam environment, then we have to clear out any
     * left over request attributes. Otherwise, since this context is
     * incorporated into the Seam Contexts structure, things put into this
     * context linger beyond the scope of the request, which can cause problems.
     * This method should only be called from the blocking servlet, as it's the
     * handler for the Ajax requests that cause the issue.
     */
    public void clearRequestContext() {
        if (SeamUtilities.isSeamEnvironment()) {
            try {
                requestMap.clear();
            } catch (IllegalStateException ise) {
                // Can be thrown in Seam example applications as a result of
                // eg. logout, which has already invalidated the session.
            }
        }
    }

    public void resetRequestMap() {
        clearRequestContext();
    }

    public void injectBundles(Map bundles) {
        requestMap.putAll(bundles);
    }

    private interface Redirector {
        void redirect(String uri);
    }

    private interface CookieTransporter {
        void send(Cookie cookie);
    }

    private void insertPostbackKey() {
        if (null != postBackKey) {
            requestParameterMap.put(postBackKey, "not reload");
            requestParameterValuesMap.put(postBackKey, new String[] { "not reload" });
        }
    }
}
