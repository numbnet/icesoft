/*
 * Version: MPL 1.1
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
 */

package org.icefaces.util;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.component.UIViewRoot;
import javax.faces.application.Resource;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnvUtils {

    private static Logger log = Logger.getLogger(EnvUtils.class.getName());

    //The key used to store the current configuration in the application map.
    public static String ICEFACES_ENV_CONFIG = "org.icefaces.env.config";

    //Parameters that are configurable using application init parameters
    public static String ICEFACES_AUTO = "org.icefaces.render.auto";
    public static String ICEFACES_AUTOID = "org.icefaces.autoid";
    public static String ICEFACES_RENDER = "org.icefaces.render";
    public static String ARIA_ENABLED = "org.icefaces.aria.enabled";
    public static String BLOCK_UI_ON_SUBMIT = "org.icefaces.blockUIOnSubmit";
    public static String COMPRESS_DOM = "org.icefaces.compressDOM";
    public static String COMPRESS_RESOURCES = "org.icefaces.compressResources";
    public static String CONNECTION_LOST_REDIRECT_URI = "org.icefaces.connectionLostRedirectURI";
    public static String DELTA_SUBMT = "org.icefaces.deltaSubmit";
    public static String SESSION_EXPIRED_REDIRECT_URI = "org.icefaces.sessionExpiredRedirectURI";
    public static String STANDARD_FORM_SERIALIZATION = "org.icefaces.standardFormSerialization";
    public static String WINDOW_SCOPE_EXPIRATION = "org.icefaces.windowScopeExpiration";

    //Other parameters used internally by ICEfaces framework.
    public static final String HEAD_DETECTED = "org.icefaces.headDetected";
    public static final String BODY_DETECTED = "org.icefaces.bodyDetected";
    private static String RESOURCE_PREFIX = "/javax.faces.resource/";
    private static String PATH_TEMPLATE = "org.icefaces.resource.pathTemplate";
    private static String DUMMY_RESOURCE = "bridge.js";
    private static String[] DEFAULT_TEMPLATE = new String[] {RESOURCE_PREFIX, ".jsf"};

    //Use reflection to identify if the Portlet classes are available.
    private static Class PortletSessionClass;
    private static Class PortletRequestClass;
    static {
        try {
            PortletSessionClass = Class.forName("javax.portlet.PortletSession");
            PortletRequestClass = Class.forName("javax.portlet.PortletRequest");
        } catch (Throwable t) {
            log.log(Level.FINE, "Portlet classes not available: ", t);
        }
    }

    //Use reflection to identify if ICEpush is available.
    private static boolean icepushPresent;
    static {
        try {
            Class.forName("org.icepush.PushContext");
            icepushPresent = true;
        } catch (ClassNotFoundException e) {
            icepushPresent = false;
        }
    }

    //Use reflection to identify if JSF implementation is Mojarra.
    private static boolean mojarraPresent = false;
    static {
        try {
            Class.forName("com.sun.faces.context.FacesContextImpl");
            mojarraPresent = true;
        } catch (Throwable t) {
            mojarraPresent = false;
        }
    }

    public static boolean isAriaEnabled(FacesContext facesContext) {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Map viewMap = viewRoot.getViewMap();
        Object ariaEnabled = viewMap.get(ARIA_ENABLED);
        if (null == ariaEnabled) {
            return EnvConfig.getEnvConfig(facesContext).ariaEnabled;
        }
        return (Boolean.TRUE.equals(ariaEnabled));
    }

    public static boolean isAutoId(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).autoId;
    }

    public static boolean isAutoRender(FacesContext facesContext)  {
        return EnvConfig.getEnvConfig(facesContext).autoRender;
    }

    public static boolean isBlockUIOnSubmit(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).blockUIOnSubmit;
    }

    public static boolean isCompressDOM(FacesContext facesContext) {
        //consider making this a per-view setting
        return EnvConfig.getEnvConfig(facesContext).compressDOM;
    }

    public static boolean isCompressResources(FacesContext facesContext)  {
        return EnvConfig.getEnvConfig(facesContext).compressResources;
    }

    public static String getConnectionLostRedirectURI(FacesContext facesContext)  {
        return EnvConfig.getEnvConfig(facesContext).connectionLostRedirectURI;
    }

    public static boolean isDeltaSubmit(FacesContext facesContext)  {
        return EnvConfig.getEnvConfig(facesContext).deltaSubmit;
    }

    public static String getSessionExpiredRedirectURI(FacesContext facesContext)  {
        return EnvConfig.getEnvConfig(facesContext).connectionLostRedirectURI;
    }

    public static boolean isStandardFormSerialization(FacesContext facesContext)  {
        return EnvConfig.getEnvConfig(facesContext).standardFormSerialization;
    }

    public static long getWindowScopeExpiration(FacesContext facesContext)  {
        return EnvConfig.getEnvConfig(facesContext).windowScopeExpiration;
    }

    public static boolean isICEfacesView(FacesContext facesContext) {
        //Check to see if the view is configured to use ICEfaces (default is to enable ICEfaces).
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Map viewMap = viewRoot.getViewMap();

        Object icefacesRender = viewMap.get(ICEFACES_RENDER);
        if (null == icefacesRender) {
            icefacesRender = EnvConfig.getEnvConfig(facesContext).autoRender;
            viewMap.put(ICEFACES_RENDER, icefacesRender);
        }
        //using .equals on Boolean to obtain boolean robustly
        return (Boolean.TRUE.equals(icefacesRender));
    }

    public static boolean isICEpushPresent() {
        return icepushPresent;
    }

    public static boolean hasHeadAndBodyComponents(FacesContext facesContext){
        //ICE-5613: ICEfaces must have h:head and h:body tags to render resources into
        //Without these components, ICEfaces is disabled.
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Map viewMap = viewRoot.getViewMap();
        if (!viewMap.containsKey(HEAD_DETECTED) || !viewMap.containsKey(BODY_DETECTED) ) {
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "ICEfaces disabled for view " + viewRoot.getViewId() +
                        "\n  h:head tag available: " + viewMap.containsKey(HEAD_DETECTED) +
                        "\n  h:body tag available: " + viewMap.containsKey(BODY_DETECTED));
            }
            return false;
        }
        return true;
    }


    //remove this once multi-form ViewState is addressed
    public static boolean needViewStateHack() {
        return mojarraPresent;
    }

    public static String[] getPathTemplate()  {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map applicationMap = facesContext.getExternalContext()
                .getApplicationMap();
        String[] pathTemplate = (String[]) applicationMap.get(PATH_TEMPLATE);
        if (null != pathTemplate)  {
            return pathTemplate;
        }
        Resource dummyResource = facesContext.getApplication()
                .getResourceHandler().createResource(DUMMY_RESOURCE);
        if (null != dummyResource)  {
            String dummyPath = dummyResource.getRequestPath();
            pathTemplate = extractPathTemplate(dummyPath);
        }
        if (null == pathTemplate)  {
            return DEFAULT_TEMPLATE;
        }
        applicationMap.put(PATH_TEMPLATE, pathTemplate);
        return pathTemplate;
    }

    private static String[] extractPathTemplate(String path)  {
        int start = path.indexOf(DUMMY_RESOURCE);
        String pre = path.substring(0, start);
        String post = path.substring(start + DUMMY_RESOURCE.length());
        return new String[] {pre, post};
    }

    public static boolean instanceofPortletSession(Object session) {
        return PortletSessionClass != null && PortletSessionClass.isInstance(session);
    }

    public static boolean instanceofPortletRequest(Object request) {
        return PortletRequestClass != null && PortletRequestClass.isInstance(request);
    }
}

class EnvConfig {
    private static Logger log = Logger.getLogger(EnvConfig.class.getName());

    boolean autoRender;
    boolean autoId;
    boolean ariaEnabled;
    boolean blockUIOnSubmit;
    boolean compressDOM;
    boolean compressResources;
    String  connectionLostRedirectURI;
    boolean deltaSubmit;
    String  sessionExpiredRedirectURI;
    boolean standardFormSerialization;
    long    windowScopeExpiration;

    public EnvConfig(Map initMap)  {
        init(initMap);
    }

    public void init(Map initMap)  {
        StringBuilder info = new StringBuilder();

        autoRender = decodeBoolean(initMap, EnvUtils.ICEFACES_AUTO, true, info);
        autoId = decodeBoolean(initMap, EnvUtils.ICEFACES_AUTOID, true, info);
        ariaEnabled = decodeBoolean(initMap, EnvUtils.ARIA_ENABLED, true, info);
        blockUIOnSubmit = decodeBoolean(initMap, EnvUtils.BLOCK_UI_ON_SUBMIT, false, info);
        compressDOM = decodeBoolean(initMap, EnvUtils.COMPRESS_DOM, false, info);
        compressResources = decodeBoolean(initMap, EnvUtils.COMPRESS_RESOURCES, true, info);
        connectionLostRedirectURI = decodeString(initMap, EnvUtils.CONNECTION_LOST_REDIRECT_URI, "null", info);
        deltaSubmit = decodeBoolean(initMap, EnvUtils.DELTA_SUBMT, false, info);
        sessionExpiredRedirectURI = decodeString(initMap, EnvUtils.SESSION_EXPIRED_REDIRECT_URI, "null", info);
        deltaSubmit = decodeBoolean(initMap, EnvUtils.DELTA_SUBMT, false, info);
        windowScopeExpiration = decodeLong(initMap,EnvUtils.WINDOW_SCOPE_EXPIRATION, 1000, info);
        
        log.info("ICEfaces Configuration: \n" + info);
    }

    public static EnvConfig getEnvConfig(FacesContext facesContext)  {
        ExternalContext externalContext = facesContext.getExternalContext();
        Map appMap = externalContext.getApplicationMap();
        EnvConfig envConfig = (EnvConfig) appMap.get(EnvUtils.ICEFACES_ENV_CONFIG);
        if (null == envConfig) {
            envConfig = new EnvConfig(externalContext.getInitParameterMap());
            appMap.put(EnvUtils.ICEFACES_ENV_CONFIG, envConfig);
        }
        return envConfig;
    }


    boolean decodeBoolean(Map map, String name, boolean defaultValue, StringBuilder info)  {
        String paramValue = (String) map.get(name);
        if (null == paramValue)  {
            info.append(name).append(": ").append(defaultValue).append(" [default]\n");
            return defaultValue;
        }
        if ("true".equalsIgnoreCase(paramValue)) {
            info.append(name).append(": true\n");
            return true;
        }
        if ("false".equalsIgnoreCase(paramValue)) {
            info.append(name).append(": false\n");
            return false;
        }
        info.append(name).append(": ").append(defaultValue).append(" [default replacing malformed non-boolean value: ").append(paramValue).append("]\n");
        return defaultValue;
    }

    String decodeString(Map map, String name, String defaultValue, StringBuilder info)  {
        String paramValue = (String) map.get(name);
        if (null == paramValue)  {
            info.append(name).append(": ").append(defaultValue).append(" [default]\n");
            return defaultValue;
        }
        info.append(name).append(": ").append(paramValue).append("\n");
        return paramValue;
    }

    long decodeLong(Map map, String name, long defaultValue, StringBuilder info)  {
        String paramValue = (String) map.get(name);
        if (null == paramValue)  {
            info.append(name).append(" = ").append(defaultValue).append(" [default]  ");
            return defaultValue;
        }
        try{
            return Long.parseLong(paramValue);
        } catch (Exception e){
            info.append(name).append(": ").append(defaultValue).append(" [default replacing malformed long value: ").append(paramValue).append("]\n");
        }
        return defaultValue;
    }

}
