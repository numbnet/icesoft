/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.util;

import org.icefaces.impl.push.servlet.ICEpushResourceHandler;
import org.icefaces.impl.push.servlet.ProxyHttpServletRequest;
import org.icefaces.impl.push.servlet.ProxyHttpServletResponse;

import javax.faces.application.Resource;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnvUtils {

    private static Logger log = Logger.getLogger(EnvUtils.class.getName());

    //The key used to store the current configuration in the application map.
    public static String ICEFACES_ENV_CONFIG = "org.icefaces.env.config";

    //Parameters configurable using context parameters
    public static String ICEFACES_AUTO = "org.icefaces.render.auto";
    public static String ICEFACES_AUTOID = "org.icefaces.autoid";
    public static String COMPRESS_DOM = "org.icefaces.compressDOM";
    public static String COMPRESS_RESOURCES = "org.icefaces.compressResources";
    public static String DELTA_SUBMT = "org.icefaces.deltaSubmit";
    public static String LAZY_PUSH = "org.icefaces.lazyPush";
    public static String STANDARD_FORM_SERIALIZATION = "org.icefaces.standardFormSerialization";
    public static String STRICT_SESSION_TIMEOUT = "org.icefaces.strictSessionTimeout";
    public static String WINDOW_SCOPE_EXPIRATION = "org.icefaces.windowScopeExpiration";
    public static String MANDATORY_RESOURCE_CONFIG = "org.icefaces.mandatoryResourceConfiguration";
    public static String UNIQUE_RESOURCE_URLS = "org.icefaces.uniqueResourceURLs";
    public static String LAZY_WINDOW_SCOPE = "org.icefaces.lazyWindowScope";
    public static String DISABLE_DEFAULT_ERROR_POPUPS = "org.icefaces.disableDefaultErrorPopups";


    //Parameters configurable using context parameters but only in compatibility mode
    public static String CONNECTION_LOST_REDIRECT_URI = "org.icefaces.connectionLostRedirectURI";
    public static String SESSION_EXPIRED_REDIRECT_URI = "org.icefaces.sessionExpiredRedirectURI";

    //Parameters configurable on a per page-basis as attributes of <ice:config/>
    public static String ICEFACES_RENDER = "org.icefaces.render";
    public static String ARIA_ENABLED = "org.icefaces.aria.enabled";
    public static String BLOCK_UI_ON_SUBMIT = "org.icefaces.blockUIOnSubmit";

    //Other parameters used internally by ICEfaces framework.
    public static final String HEAD_DETECTED = "org.icefaces.headDetected";
    public static final String BODY_DETECTED = "org.icefaces.bodyDetected";
    private static String RESOURCE_PREFIX = "/javax.faces.resource/";
    private static String PATH_TEMPLATE = "org.icefaces.resource.pathTemplate";
    private static String DUMMY_RESOURCE = "bridge.js";
    private static String[] DEFAULT_TEMPLATE = new String[]{RESOURCE_PREFIX, ".jsf"};

    //Use reflection to identify if the Portlet classes are available.
    private static Class PortletSessionClass;
    private static Class PortletRequestClass;
    private static Class PortletResponseClass;

    static {
        try {
            PortletSessionClass = Class.forName("javax.portlet.PortletSession");
            PortletRequestClass = Class.forName("javax.portlet.PortletRequest");
            PortletResponseClass = Class.forName("javax.portlet.PortletResponse");
        } catch (Throwable t) {
            log.log(Level.FINE, "Portlet classes not available: ", t);
        }
    }

    //Use reflection to identify if the Portlet classes are on a specific platform.
    private static Class LiferayClass;

    static {
        try {
            LiferayClass = Class.forName("com.liferay.portal.theme.ThemeDisplay");
        } catch (Throwable t) {
            log.log(Level.FINE, "Liferay class not available: ", t);
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

    /**
     * Returns the value of the context parameter org.icefaces.aria.enabled.  The default value is true and indicates
     * that views are ARIA (Accessible Rich Internet Applications) enabled.  This context parameter is application-wide
     * and works together with the 'ariaEnabled' attribute of the ICEfaces configuration tag <ice:config> so that ARIA support
     * can be turned on and off selectively on a per page basis.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.aria.enabled.  The default is true.
     */
    public static boolean isAriaEnabled(FacesContext facesContext) {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Map viewMap = viewRoot.getViewMap();
        Object ariaEnabled = viewMap.get(ARIA_ENABLED);
        if (null == ariaEnabled) {
            return EnvConfig.getEnvConfig(facesContext).ariaEnabled;
        }
        return (Boolean.TRUE.equals(ariaEnabled));
    }

    /**
     * Returns the value of the context parameter org.icefaces.autoid.  The default value is true and indicates
     * that the majority of standard JSF components will write their ids to the page markup.  This allows page updates
     * to be targetted as granularly as possible.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.autoid.  The default is true.
     */
    public static boolean isAutoId(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).autoId;
    }

    /**
     * Returns the value of the context parameter org.icefaces.render.auto.  The default value is true and indicates
     * that DOM changes will automatically be applied to each page.  This context parameter is application-wide and works
     * together with the render attribute of the ICEfaces configuration tag <ice:config> so that DOM updates can be turned on
     * and off selectively on a per page basis.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.render.auto.  The default is true.
     */
    public static boolean isAutoRender(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).autoRender;
    }

    /**
     * Returns the value of the context parameter org.icefaces.blockUIOnSubmit.  The default value is false and indicates
     * that the UI will not be blocked after a request has been submitted.  To help deal with the problems with double-submits,
     * this parameter can be set to true.
     * <p/>
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.blockUIOnSubmit.  The default is false.
     */
    public static boolean isBlockUIOnSubmit(FacesContext facesContext) {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Map viewMap = viewRoot.getViewMap();
        Object blockUIOnSubmit = viewMap.get(BLOCK_UI_ON_SUBMIT);
        if (null == blockUIOnSubmit) {
            return EnvConfig.getEnvConfig(facesContext).blockUIOnSubmit;
        }
        return (Boolean.TRUE.equals(blockUIOnSubmit));
    }

    /**
     * Returns the value of the context parameter org.icefaces.compressDOM.  The default value is false and indicates
     * that, between requests, the server-side DOM will be serialized and compressed to save memory.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.compressDOM.  The default is false.
     */
    public static boolean isCompressDOM(FacesContext facesContext) {
        //consider making this a per-view setting
        return EnvConfig.getEnvConfig(facesContext).compressDOM;
    }

    /**
     * Returns the value of the context parameter org.icefaces.compressResources.  The default value is true and indicates
     * that, for resource requests, certain resources should be automatically compressed via gzip before being sent.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.compressResources.  The default is true.
     */
    public static boolean isCompressResources(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).compressResources;
    }

    /**
     * Returns the value of the context parameter org.icefaces.connectionLostRedirectURI.  The default value is the String
     * "null" and indicates that no URI has been set and the default behaviour is taken when the Ajax Push connection is lost.
     * Setting a URI value tells ICEfaces to redirect to that view if the Ajax Push connection is lost.
     * <p/>
     * Note: This value is only relevant when running ICEfaces 2 with the compatible component suite:
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.connectionLostRedirectURI.  The default is the String "null".
     */
    public static String getConnectionLostRedirectURI(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).connectionLostRedirectURI;
    }

    /**
     * Returns the value of the context parameter org.icefaces.deltaSubmit.  The default value is false and indicates that
     * the delta submit features is not currently enabled.  When delta submit is enabled, form submission is done in a way that
     * minimizes what is sent across the wire.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.deltaSubmit.  The default is false.
     */
    public static boolean isDeltaSubmit(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).deltaSubmit;
    }

    /**
     * Returns the value of the context parameter org.icefaces.lazyPush.  The default value is true and indicates that
     * ICEpush will be initially lazily.  In other words, ICEpush will not activate and open a blocking connection
     * until the first push request is made.  By setting lazyPush to false, ICEpush will be automatically activated for
     * each ICEfaces page.
     * <p/>
     * This context parameter is application-wide and works together with the lazyPush attribute of the ICEfaces
     * configuration tag <ice:config> so that ICEpush can be set to activate lazily on a per-page basis.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.lazyPush.  The default is true.
     */
    public static boolean isLazyPush(FacesContext facesContext) {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Map viewMap = viewRoot.getViewMap();
        Object lazyPush = viewMap.get(LAZY_PUSH);
        if (null == lazyPush) {
            return EnvConfig.getEnvConfig(facesContext).lazyPush;
        }
        return (Boolean.TRUE.equals(lazyPush));
    }


    /**
     * Returns true if Liferay classes are detected via reflection.
     *
     * @return Returns true if Liferay classes are detected via reflection.
     */
    public static boolean isLiferay() {
        return LiferayClass != null;
    }


    /**
     * Returns true if JSF Partial State Saving is active.
     *
     * @param facesContext The current FacesContext instance.
     * @return Returns the current state of JSF Partial State Saving.  The default is true.
     */
    public static boolean isPartialStateSaving(FacesContext facesContext) {
        return !("false".equalsIgnoreCase(
                FacesContext.getCurrentInstance().getExternalContext()
                        .getInitParameter("javax.faces.PARTIAL_STATE_SAVING")));
    }

    /**
     * Returns the value of the context parameter org.icefaces.sessionExpiredRedirectURI.  The default value is the String
     * "null" and indicates that no URI has been set and the default behaviour is taken when the session expires.  Setting
     * a URI value tells ICEfaces to redirect to that view if the Ajax Push connection is lost.
     * <p/>
     * Note: This value is only relevant when running ICEfaces 2 with the compatible component suite:
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.sessionExpiredRedirectURI.  The default is the String "null".
     */
    public static String getSessionExpiredRedirectURI(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).sessionExpiredRedirectURI;
    }

    /**
     * Returns the value of the context parameter org.icefaces.standardFormSerialization.  The default value is false and indicates
     * that ICEfaces should do optimized for submission based on the submitting element.  Setting this value to true indicates that
     * ICEfaces should do a normal, full form submission.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.standardFormSerialization.  The default is false.
     */
    public static boolean isStandardFormSerialization(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).standardFormSerialization;
    }

    /**
     * Returns the value of the context parameter org.icefaces.strictSessionTimeout.  The default value is false and indicates
     * that ICEfaces should not interfere with container-managed session timeout.  Setting this value to true indicates that
     * ICEfaces should attempt to enforce the configured session timeout by ignoring intervening push activity.  Only
     * user events result in extending the session lifetime.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.strictSessionTimeout.  The default is false.
     */
    public static boolean isStrictSessionTimeout(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).strictSessionTimeout;
    }

    /**
     * Returns the value of the context parameter org.icefaces.windowScopeExpiration.  The default value is 1000 milliseconds
     * and indicates the length of time window-scoped values remain valid in the session after a reload or redirect occurs.
     * This allows for postbacks that might occur quickly after a reload or redirect to successfully retrieve the relevant
     * window-scoped values.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.windowScopeExpiration.  The default is 1000 milliseconds.
     */
    public static long getWindowScopeExpiration(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).windowScopeExpiration;
    }

    public static String getMandatoryResourceConfig(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).mandatoryResourceConfig;
    }

    public static boolean isUniqueResourceURLs(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).uniqueResourceURLs;
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

    public static boolean hasHeadAndBodyComponents(FacesContext facesContext) {
        //ICE-5613: ICEfaces must have h:head and h:body tags to render resources into
        //Without these components, ICEfaces is disabled.
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Map viewMap = viewRoot.getViewMap();
        if (!viewMap.containsKey(HEAD_DETECTED) || !viewMap.containsKey(BODY_DETECTED)) {
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

    public static String[] getPathTemplate() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map applicationMap = facesContext.getExternalContext()
                .getApplicationMap();
        String[] pathTemplate = (String[]) applicationMap.get(PATH_TEMPLATE);
        if (null != pathTemplate) {
            return pathTemplate;
        }
        Resource dummyResource = facesContext.getApplication()
                .getResourceHandler().createResource(DUMMY_RESOURCE);
        if (null != dummyResource) {
            String dummyPath = dummyResource.getRequestPath();
            pathTemplate = extractPathTemplate(dummyPath);
        }
        if (null == pathTemplate) {
            return DEFAULT_TEMPLATE;
        }
        applicationMap.put(PATH_TEMPLATE, pathTemplate);
        return pathTemplate;
    }

    private static String[] extractPathTemplate(String path) {
        int start = path.indexOf(DUMMY_RESOURCE);
        String pre = path.substring(0, start);
        String post = path.substring(start + DUMMY_RESOURCE.length());
        return new String[]{pre, post};
    }

    public static boolean instanceofPortletSession(Object session) {
        return PortletSessionClass != null && PortletSessionClass.isInstance(session);
    }

    public static boolean instanceofPortletRequest(Object request) {
        return PortletRequestClass != null && PortletRequestClass.isInstance(request);
    }

    public static boolean instanceofPortletResponse(Object response) {
        return PortletResponseClass != null && PortletResponseClass.isInstance(response);
    }

    public static HttpServletRequest getSafeRequest(FacesContext fc){
        ExternalContext ec = fc.getExternalContext();
        Object rawReq = ec.getRequest();
        if(instanceofPortletRequest(rawReq)){
            return new ProxyHttpServletRequest(fc);
        }
        return (HttpServletRequest)rawReq;
    }

    public static HttpServletResponse getSafeResponse(FacesContext fc){
        ExternalContext ec = fc.getExternalContext();
        Object rawRes = ec.getResponse();
        if(instanceofPortletResponse(rawRes)){
            return new ProxyHttpServletResponse(fc);
        }
        return (HttpServletResponse)rawRes;
    }

    public static boolean isPushRequest(FacesContext facesContext) {
        ExternalContext ec = facesContext.getExternalContext();
        String reqPath = ec.getRequestServletPath();
        String pathInfo = ec.getRequestPathInfo();
        String reqParam = ec.getRequestParameterMap().get("ice.submit.type");

        if (reqPath != null && reqPath.contains(ICEpushResourceHandler.BLOCKING_CONNECTION_RESOURCE_NAME) ||
                pathInfo != null && pathInfo.contains(ICEpushResourceHandler.BLOCKING_CONNECTION_RESOURCE_NAME) ||
                "ice.push".equals(reqParam)) {
            return true;
        }
        return false;
    }

    public static boolean isLazyWindowScope(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).lazyWindowScope;
    }

    public static boolean disableDefaultErrorPopups(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).disableDefaultErrorPopups;
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
    String connectionLostRedirectURI;
    boolean deltaSubmit;
    boolean lazyPush;
    boolean pushActive;
    String sessionExpiredRedirectURI;
    boolean standardFormSerialization;
    boolean strictSessionTimeout;
    long windowScopeExpiration;
    String mandatoryResourceConfig;
    boolean uniqueResourceURLs;
    boolean lazyWindowScope;
    public boolean disableDefaultErrorPopups;

    public EnvConfig(Map initMap) {
        init(initMap);
    }

    public void init(Map initMap) {
        StringBuilder info = new StringBuilder();

        autoRender = decodeBoolean(initMap, EnvUtils.ICEFACES_AUTO, true, info);
        autoId = decodeBoolean(initMap, EnvUtils.ICEFACES_AUTOID, true, info);
        ariaEnabled = decodeBoolean(initMap, EnvUtils.ARIA_ENABLED, true, info);
        blockUIOnSubmit = decodeBoolean(initMap, EnvUtils.BLOCK_UI_ON_SUBMIT, false, info);
        compressDOM = decodeBoolean(initMap, EnvUtils.COMPRESS_DOM, false, info);
        compressResources = decodeBoolean(initMap, EnvUtils.COMPRESS_RESOURCES, true, info);
        connectionLostRedirectURI = decodeString(initMap, EnvUtils.CONNECTION_LOST_REDIRECT_URI, null, info);
        deltaSubmit = decodeBoolean(initMap, EnvUtils.DELTA_SUBMT, false, info);
        lazyPush = decodeBoolean(initMap, EnvUtils.LAZY_PUSH, true, info);
        sessionExpiredRedirectURI = decodeString(initMap, EnvUtils.SESSION_EXPIRED_REDIRECT_URI, null, info);
        standardFormSerialization = decodeBoolean(initMap, EnvUtils.STANDARD_FORM_SERIALIZATION, false, info);
        strictSessionTimeout = decodeBoolean(initMap, EnvUtils.STRICT_SESSION_TIMEOUT, false, info);
        windowScopeExpiration = decodeLong(initMap, EnvUtils.WINDOW_SCOPE_EXPIRATION, 1000, info);
        mandatoryResourceConfig = decodeString(initMap, EnvUtils.MANDATORY_RESOURCE_CONFIG, null, info);
        uniqueResourceURLs = decodeBoolean(initMap, EnvUtils.UNIQUE_RESOURCE_URLS, true, info);
        lazyWindowScope = decodeBoolean(initMap, EnvUtils.LAZY_WINDOW_SCOPE, true, info);
        disableDefaultErrorPopups = decodeBoolean(initMap, EnvUtils.DISABLE_DEFAULT_ERROR_POPUPS, false, info);

        log.info("ICEfaces Configuration: \n" + info);
    }

    public static EnvConfig getEnvConfig(FacesContext facesContext) {
        ExternalContext externalContext = facesContext.getExternalContext();
        Map appMap = externalContext.getApplicationMap();
        EnvConfig envConfig = (EnvConfig) appMap.get(EnvUtils.ICEFACES_ENV_CONFIG);
        if (null == envConfig) {
            envConfig = new EnvConfig(externalContext.getInitParameterMap());
            appMap.put(EnvUtils.ICEFACES_ENV_CONFIG, envConfig);
        }
        return envConfig;
    }


    boolean decodeBoolean(Map map, String name, boolean defaultValue, StringBuilder info) {
        String paramValue = (String) map.get(name);
        if (null == paramValue) {
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

    String decodeString(Map map, String name, String defaultValue, StringBuilder info) {
        String paramValue = (String) map.get(name);
        if (null == paramValue) {
            info.append(name).append(": ").append(defaultValue).append(" [default]\n");
            return defaultValue;
        }
        info.append(name).append(": ").append(paramValue).append("\n");
        return paramValue;
    }

    long decodeLong(Map map, String name, long defaultValue, StringBuilder info) {
        String paramValue = (String) map.get(name);
        if (null == paramValue) {
            info.append(name).append(" = ").append(defaultValue).append(" [default]\n");
            return defaultValue;
        }
        try {
            return Long.parseLong(paramValue);
        } catch (Exception e) {
            info.append(name).append(": ").append(defaultValue).append(" [default replacing malformed long value: ").append(paramValue).append("]\n");
        }
        return defaultValue;
    }

}
