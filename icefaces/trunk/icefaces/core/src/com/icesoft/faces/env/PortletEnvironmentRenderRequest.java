package com.icesoft.faces.env;

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

public class PortletEnvironmentRenderRequest extends CommonEnvironmentRequest implements RenderRequest {
    private RenderRequest request;

    private PortletMode portletMode;
    private WindowState windowState;
    private PortletPreferences portletPreferences;
    private PortletSession portletSession;
    private PortalContext portalContext;
    private String responseContentType;
    private ArrayList responseContentTypes;
    private Map properties;

    public PortletEnvironmentRenderRequest(Object request) {
        this.request = (RenderRequest) request;

        portletMode = this.request.getPortletMode();
        windowState = this.request.getWindowState();
        portletPreferences = this.request.getPreferences();
        portletSession = this.request.getPortletSession();
        portalContext = this.request.getPortalContext();
        responseContentType = this.request.getResponseContentType();
        responseContentTypes = Collections.list(this.request.getResponseContentTypes());
        authType = this.request.getAuthType();
        remoteUser = this.request.getRemoteUser();
        userPrincipal = this.request.getUserPrincipal();
        requestedSessionId = this.request.getRequestedSessionId();
        requestedSessionIdValid = this.request.isRequestedSessionIdValid();
        scheme = this.request.getScheme();
        serverName = this.request.getServerName();
        serverPort = this.request.getServerPort();
        locale = this.request.getLocale();
        locales = Collections.list(this.request.getLocales());
        secure = this.request.isSecure();
        contextPath = this.request.getContextPath();

        attributes = new Hashtable();
        Enumeration attributeNames = this.request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = (String) attributeNames.nextElement();
            Object attribute = this.request.getAttribute(name);
            if ((null != name) && (null != attribute)) {
                attributes.put(name, attribute);
            }
        }

        //Some portal containers do not add the javax.servlet.include.*
        //attributes to the attribute names collection so they are not
        //copied into our own collection.  So we do that here if
        //necessary:

        String[] incKeys = Constants.INC_CONSTANTS;
        for (int index = 0; index < incKeys.length; index++) {
            String incVal = (String)this.request.getAttribute(incKeys[index]);
            if( !attributes.containsKey(incKeys[index]) && incVal != null ){
                attributes.put(incKeys[index], incVal);
            }
        }

        parameters = new HashMap();
        Enumeration parameterNames = this.request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            parameters.put(name, this.request.getParameterValues(name));
        }

        properties = new HashMap();
        Enumeration propertyNames = this.request.getPropertyNames();
        while (propertyNames.hasMoreElements()) {
            String name = (String) propertyNames.nextElement();
            Enumeration values = this.request.getProperties(name);
            properties.put(name, Collections.list(values));
        }
    }

    public boolean isWindowStateAllowed(WindowState windowState) {
        try {
            return request.isWindowStateAllowed(windowState);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPortletModeAllowed(PortletMode portletMode) {
        try {
            return request.isPortletModeAllowed(portletMode);
        } catch (Exception e) {
            return false;
        }
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
        return request.isUserInRole(string);
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public Enumeration getResponseContentTypes() {
        return Collections.enumeration(responseContentTypes);
    }
}