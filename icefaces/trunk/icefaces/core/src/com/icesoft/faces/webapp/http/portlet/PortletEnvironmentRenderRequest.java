package com.icesoft.faces.webapp.http.portlet;

import com.icesoft.faces.env.AuthenticationVerifier;
import com.icesoft.faces.env.CommonEnvironmentRequest;
import com.icesoft.faces.env.RequestAttributes;
import com.icesoft.jasper.Constants;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.WindowState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class PortletEnvironmentRenderRequest extends CommonEnvironmentRequest implements RenderRequest {
    private PortletMode portletMode;
    private WindowState windowState;
    private PortletPreferences portletPreferences;
    private PortletSession portletSession;
    private PortalContext portalContext;
    private String responseContentType;
    private ArrayList responseContentTypes;
    private Map properties;

    public PortletEnvironmentRenderRequest(PortletSession session, RenderRequest request) {
        portletSession = session;
        portletMode = request.getPortletMode();
        windowState = request.getWindowState();
        portletPreferences = request.getPreferences();
        portalContext = request.getPortalContext();
        responseContentType = request.getResponseContentType();
        responseContentTypes = Collections.list(request.getResponseContentTypes());
        authType = request.getAuthType();
        remoteUser = request.getRemoteUser();
        userPrincipal = request.getUserPrincipal();
        requestedSessionId = request.getRequestedSessionId();
        requestedSessionIdValid = request.isRequestedSessionIdValid();
        scheme = request.getScheme();
        serverName = request.getServerName();
        serverPort = request.getServerPort();
        locale = request.getLocale();
        locales = Collections.list(request.getLocales());
        secure = request.isSecure();
        contextPath = request.getContextPath();

        attributes = new Hashtable();
        Enumeration attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = (String) attributeNames.nextElement();
            Object attribute = request.getAttribute(name);
            if ((null != name) && (null != attribute)) {
                attributes.put(name, attribute);
            }
        }

        //Some portal containers do not add the javax.servlet.include.*
        //attributes to the attribute names collection so they are not
        //copied into our own collection.  We do that here as necessary.
        addExtraAttributes(Constants.INC_CONSTANTS, request);

        //ICE-2247: Some javax.portlet.* constants to add as well as
        //Liferay specific ones.
        addExtraAttributes(Constants.PORTLET_CONSTANTS, request);
        addExtraAttributes(Constants.LIFERAY_CONSTANTS, request);

        parameters = new HashMap();
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            parameters.put(name, request.getParameterValues(name));
        }

        properties = new HashMap();
        Enumeration propertyNames = request.getPropertyNames();
        while (propertyNames.hasMoreElements()) {
            String name = (String) propertyNames.nextElement();
            Enumeration values = request.getProperties(name);
            properties.put(name, Collections.list(values));
        }
    }

    private void addExtraAttributes(String[] keys, RenderRequest request) {
        for (int index = 0; index < keys.length; index++) {
            Object val = request.getAttribute(keys[index]);
            if (!attributes.containsKey(keys[index]) && val != null) {
                attributes.put(keys[index], val);
            }
        }
    }

    public boolean isWindowStateAllowed(WindowState windowState) {
        return allowMode().isWindowStateAllowed(windowState);
    }

    public boolean isPortletModeAllowed(PortletMode portletMode) {
        return allowMode().isPortletModeAllowed(portletMode);
    }

    public PortletMode getPortletMode() {
        return portletMode;
    }

    public WindowState getWindowState() {
        return windowState;
    }

    public PortletPreferences getPreferences() {
        return portletPreferences;
    }

    public PortletSession getPortletSession() {
        return portletSession;
    }

    public PortletSession getPortletSession(boolean create) {
        return portletSession;
    }

    public String getProperty(String name) {
        if (properties.containsKey(name)) {
            List values = (LinkedList) properties.get(name);
            return (String) values.get(0);
        } else {
            return null;
        }
    }

    public Enumeration getProperties(String name) {
        if (properties.containsKey(name)) {
            LinkedList values = (LinkedList) properties.get(name);
            return Collections.enumeration(values);
        } else {
            return Collections.enumeration(Collections.EMPTY_LIST);
        }
    }

    public Enumeration getPropertyNames() {
        return Collections.enumeration(properties.keySet());
    }

    public PortalContext getPortalContext() {
        return portalContext;
    }

    public boolean isUserInRole(String string) {
        return authenticationVerifier().isUserInRole(string);
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public Enumeration getResponseContentTypes() {
        return Collections.enumeration(responseContentTypes);
    }

    public void removeAttribute(String name) {
        super.removeAttribute(name);
        requestAttributes().removeAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        super.setAttribute(name, value);
        requestAttributes().setAttribute(name, value);
    }

    public abstract AllowMode allowMode();

    public abstract AuthenticationVerifier authenticationVerifier();

    public abstract RequestAttributes requestAttributes();
}