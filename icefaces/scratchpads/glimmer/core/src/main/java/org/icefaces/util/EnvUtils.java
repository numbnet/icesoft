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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icefaces.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnvUtils {
    private static Logger log = Logger.getLogger(EnvUtils.class.getName());
    private static Class PortletSessionClass;
    //internal flags
    public static String ICEFACES_CONFIG_LOADED = "org.icefaces.config.loaded";
    public static String ICEFACES_RENDER = "org.icefaces.render";
    public static String ARIA_ENABLED = "org.icefaces.aria.enabled";

    static {
        try {
            PortletSessionClass = Class.forName("javax.portlet.PortletSession");
        } catch (Throwable t) {
            log.log(Level.FINE, "PortletSession class not available: ", t);
        }
    }

    public static boolean instanceofPortletSession(Object session) {
        if (null != PortletSessionClass) {
            return PortletSessionClass.isInstance(session);
        }
        return false;
    }

    public static boolean isICEfacesView(FacesContext facesContext) {
        Map attributes = facesContext.getAttributes();
        Object icefacesRender = attributes.get(ICEFACES_RENDER);
        if (null == icefacesRender) {
            icefacesRender = new Boolean(
                    EnvConfig.getEnvConfig(facesContext).getAutoRender());
            attributes.put(ICEFACES_RENDER, icefacesRender);
        }
        //using .equals on Boolean to obtain boolean robustly
        return (Boolean.TRUE.equals(icefacesRender));
    }

    public static boolean isAutoId(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).getAutoId();
    }

    public static boolean isAriaEnabled(FacesContext facesContext) {
        Map attributes = facesContext.getAttributes();
        Object ariaEnabled = attributes.get(ARIA_ENABLED);
        if (null == ariaEnabled) {
            return EnvConfig.getEnvConfig(facesContext).isAriaEnabled();
        } 
        return (Boolean.TRUE.equals(ariaEnabled));
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
}
