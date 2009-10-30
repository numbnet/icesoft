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

import javax.faces.context.FacesContext;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.icefaces.render.DOMRenderKit;

public class EnvUtils {
    private static Logger log = Logger.getLogger("org.icefaces.util.EnvUtils");
    private static Class PortletSessionClass;

    static {
        try {
            PortletSessionClass = Class.forName("javax.portlet.PortletSession");
        } catch (Throwable t)  {
            log.log(Level.FINE, "PortletSession class not available: ", t);
        }
    }

    public static boolean instanceofPortletSession(Object session)  {
        if (null != PortletSessionClass)  {
            return PortletSessionClass.isInstance(session);
        }
        return false;
    }

    public static boolean isICEfacesView(FacesContext facesContext)  {
        if (facesContext.getRenderKit() instanceof DOMRenderKit)  {
            return true;
        }
        return false;
    }
}
