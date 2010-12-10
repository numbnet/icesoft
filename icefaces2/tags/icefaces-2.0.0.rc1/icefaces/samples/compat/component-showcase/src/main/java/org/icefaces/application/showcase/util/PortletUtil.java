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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.application.showcase.util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import java.util.Iterator;
import java.util.Map;

@ManagedBean(name = "portletUtil")
@RequestScoped
public class PortletUtil {
    public static final String PORTLET_CONFIG_KEY = "javax.portlet.faces.PortletConfig";
    public static final String VIEW_PATH_KEY = "org.icefaces.demo.viewPath";

    public PortletUtil() {
    }

    /**
     * Get the init-param value for 'org.icefaces.demo.viewPath' from portlet.xml
     * @return
     */
    public String getPortletViewPath() {
        return getInitParam(VIEW_PATH_KEY);
    }

    /**
     * Due to vagueness in the various specifications relating to both JSF and portlets,
     * init-param values are not easily accessible via normal JSF API mechanisms.  This
     * method uses the PortletFaces bridge to access the PortletConfig so that we can get
     * at the values we want.
     * 
     * @param key The key for the init-param
     * @return The value for the init-param
     */
    private static String getInitParam(String key){
        String initParam = null;
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Object objReq = ec.getRequest();
        if( objReq instanceof PortletRequest){
            PortletRequest pr = (PortletRequest)objReq;
            PortletConfig portletConfig = (PortletConfig) pr.getAttribute(PORTLET_CONFIG_KEY);
            initParam = portletConfig.getInitParameter(key);
        }
        return initParam;
    }

    private static void dumpMap(String msg, Map map){
        StringBuffer buff = new StringBuffer(msg);
        buff.append("\n");
        Iterator keys = map.keySet().iterator();
        while(keys.hasNext()){
            Object key = keys.next();
            buff.append("\n  ").append(key).append(" = ").append(map.get(key));
        }
        System.out.println(buff.toString());
    }
}
