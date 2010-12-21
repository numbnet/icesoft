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
 */

package org.icefaces.util;

import javax.faces.component.NamingContainer;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utility API for managing the element focus in the browser.
 */
public class FocusController {
    private static final Pattern ClientIdPattern = Pattern.compile("^(([\\w\\_]*)\\" + NamingContainer.SEPARATOR_CHAR + "([\\w\\_]*))*$");

    /**
     * Acquire the ID of the currently focused element in the browser.
     *
     * @param context the FacesContext
     * @return the element ID
     */
    public static String getReceivedFocus(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        Map map = externalContext.getRequestParameterMap();
        String focusedElement = (String) map.get("ice.focus");
        return focusedElement != null && ClientIdPattern.matcher(focusedElement).matches() && !focusedElement.equals("null") && !focusedElement.equals("undefined") ? focusedElement : null;
    }

    /**
     * Set the element ID that should received focus during next update.
     *
     * @param context the FacesContext
     * @param id      the element ID
     */
    public static void setFocus(FacesContext context, String id) {
        if (id != null && !"".equals(id)) {
            context.getExternalContext().getRequestMap().put(FocusController.class.getName(), id);
        }
    }

    /**
     * Get the element ID the will receive focus during next update.
     *
     * @param context the FacesContext
     * @return the element ID
     */
    public static String getFocus(FacesContext context) {
        return (String) context.getExternalContext().getRequestMap().get(FocusController.class.getName());
    }

    /**
     * Test if focus is defined for a certain element.
     *
     * @param context the FacesContext
     * @return return true if focus is defined
     */
    public static boolean isFocusSet(FacesContext context) {
        return context.getExternalContext().getRequestMap().containsKey(FocusController.class.getName());
    }
}
