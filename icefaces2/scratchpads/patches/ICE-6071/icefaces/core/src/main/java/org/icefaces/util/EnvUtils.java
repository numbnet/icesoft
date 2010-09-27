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
import javax.faces.component.UIViewRoot;
import javax.faces.application.Resource;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnvUtils {
    private static Logger log = Logger.getLogger(EnvUtils.class.getName());
    private static Class PortletSessionClass;
    private static Class PortletRequestClass;
    //internal flags
    public static String ICEFACES_CONFIG_LOADED = "org.icefaces.config.loaded";
    public static String ICEFACES_RENDER = "org.icefaces.render";
    public static String ARIA_ENABLED = "org.icefaces.aria.enabled";
    public static String COMPRESS_DOM = "org.icefaces.compressDOM";

    public static final String HEAD_DETECTED = "org.icefaces.headDetected";
    public static final String BODY_DETECTED = "org.icefaces.bodyDetected";

    private static String RESOURCE_PREFIX = "/javax.faces.resource/";
    private static String PATH_TEMPLATE = "org.icefaces.resource.pathTemplate";
    private static String DUMMY_RESOURCE = "bridge.js";
    private static String[] DEFAULT_TEMPLATE = new String[] {RESOURCE_PREFIX, ".jsf"};

    static {
        try {
            PortletSessionClass = Class.forName("javax.portlet.PortletSession");
            PortletRequestClass = Class.forName("javax.portlet.PortletRequest");
        } catch (Throwable t) {
            log.log(Level.FINE, "Portlet classes not available: ", t);
        }
    }

    public static boolean instanceofPortletSession(Object session) {
        return PortletSessionClass == null ? false : PortletSessionClass.isInstance(session);
    }

    public static boolean instanceofPortletRequest(Object request) {
        return PortletRequestClass == null ? false : PortletRequestClass.isInstance(request);
    }

    public static boolean isICEfacesView(FacesContext facesContext) {
        //Check to see if the view is configured to use ICEfaces (default is to enable ICEfaces).
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Map viewMap = viewRoot.getViewMap();

        Object icefacesRender = viewMap.get(ICEFACES_RENDER);
        if (null == icefacesRender) {
            icefacesRender = new Boolean(
                    EnvConfig.getEnvConfig(facesContext).getAutoRender());
            viewMap.put(ICEFACES_RENDER, icefacesRender);
        }
        //using .equals on Boolean to obtain boolean robustly
        return (Boolean.TRUE.equals(icefacesRender));
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

    public static boolean isAutoId(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).getAutoId();
    }

    public static boolean isAriaEnabled(FacesContext facesContext) {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Map viewMap = viewRoot.getViewMap();
        Object ariaEnabled = viewMap.get(ARIA_ENABLED);
        if (null == ariaEnabled) {
            return EnvConfig.getEnvConfig(facesContext).isAriaEnabled();
        }
        return (Boolean.TRUE.equals(ariaEnabled));
    }

    public static boolean isCompressDOM(FacesContext facesContext) {
        //consider making this a per-view setting
        return EnvConfig.getEnvConfig(facesContext).getCompressDOM();
    }

    private static boolean icepushPresent;

    static {
        try {
            Class.forName("org.icepush.PushContext");
            icepushPresent = true;
        } catch (ClassNotFoundException e) {
            icepushPresent = false;
        }
    }

    public static boolean isICEpushPresent() {
        return icepushPresent;
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

}
