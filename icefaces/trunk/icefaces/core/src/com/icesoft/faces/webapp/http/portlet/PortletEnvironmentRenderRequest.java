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

package com.icesoft.faces.webapp.http.portlet;

import com.icesoft.faces.env.Authorization;
import com.icesoft.faces.env.CommonEnvironmentRequest;
import com.icesoft.faces.env.RequestAttributes;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.jasper.Constants;

import javax.portlet.*;
import java.util.*;

public abstract class PortletEnvironmentRenderRequest extends CommonEnvironmentRequest implements RenderRequest {
    private static final Collection PersistentRequestConstants = Arrays.asList(new String[]{
            Constants.INC_CONTEXT_PATH,
            Constants.INC_PATH_INFO,
            Constants.INC_QUERY_STRING,
            Constants.INC_REQUEST_URI,
            Constants.INC_SERVLET_PATH,
            Constants.PORTLET_CONFIG,
            Constants.PORTLET_USERINFO});
    private static final Collection RequestConstants = Arrays.asList(new String[]{
            Constants.PORTLET_REQUEST,
            Constants.PORTLET_RESPONSE});
    private PortletMode portletMode;
    private WindowState windowState;
    private PortletPreferences portletPreferences;
    private PortletSession portletSession;
    private PortalContext portalContext;
    private String responseContentType;
    private ArrayList responseContentTypes;
    private Map properties;
    private Map pseudoAPIAttributes;
    private Authorization authorization;

    public PortletEnvironmentRenderRequest(PortletSession session, RenderRequest request, Configuration configuration, Authorization auth) {
        portletSession = session;
        authorization = auth;
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
        secure = request.isSecure();
        contextPath = request.getContextPath();

        //ICE-2846: check for deprecated configuration parameter as well
        String customPortletAttributes = configuration.getAttribute("portlet.hiddenAttributes",
                configuration.getAttribute("hiddenPortletAttributes", ""));
        Collection hiddenCustomAttributeNames = Arrays.asList(customPortletAttributes.split(" "));

        attributes = Collections.synchronizedMap(new HashMap());
        populateMap(Collections.list(request.getAttributeNames()), attributes, request);
        //Some portal containers do not add the javax.servlet.include.*
        //attributes to the attribute names collection so they are not
        //copied into our own collection.  We do that here as necessary.
        populateMap(PersistentRequestConstants, attributes, request);
        //ICE-2247: Some javax.portlet.* constants to add as well as
        populateMap(RequestConstants, attributes, request);
        //add custom attributes
        populateMap(hiddenCustomAttributeNames, attributes, request);

        pseudoAPIAttributes = new HashMap();
        populateMap(PersistentRequestConstants, pseudoAPIAttributes, request);
        populateMap(hiddenCustomAttributeNames, pseudoAPIAttributes, request);

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
            List values = (List) properties.get(name);
            return (String) values.get(0);
        } else {
            return null;
        }
    }

    public Enumeration getProperties(String name) {
        if (properties.containsKey(name)) {
            List values = (List) properties.get(name);
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
        return authorization.isUserInRole(string);
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

    public abstract RequestAttributes requestAttributes();

    void repopulatePseudoAPIAttributes() {
        attributes.putAll(pseudoAPIAttributes);
    }

    private static void populateMap(Collection keys, Map map, RenderRequest request) {
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            final Object value = request.getAttribute(name);
            if (name != null && value != null) {
                map.put(name, value);
            }
        }
    }
}