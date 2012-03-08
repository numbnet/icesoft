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

package org.icefaces.component.fileentry;

import javax.portlet.PortletRequest;
import javax.portlet.filter.PortletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

public class FileUploadPortletRequestWrapper extends PortletRequestWrapper {

    private Map<String, String[]> parameterMap;

    public FileUploadPortletRequestWrapper(Object request,
                                           Map<String, String[]> parameterMap) {
        super((PortletRequest)request);
        this.parameterMap = parameterMap;
    }

    // Returns a java.util.Map of the parameters of this request.
    public Map<String, String[]> getParameterMap() {
        if (parameterMap != null) {
            return Collections.unmodifiableMap(parameterMap);
        }
        return super.getParameterMap();
    }

    // Returns an Enumeration of String objects containing the names of
    // the parameters contained in this request.
    public Enumeration<String> getParameterNames() {
        if (parameterMap != null) {
            Vector<String> keyVec = new Vector<String>(parameterMap.keySet());
            return keyVec.elements();
        }
        return super.getParameterNames();
    }

    // Returns the value of a request parameter as a String, or null if
    // the parameter does not exist.
    public String getParameter(String name) {
        if (parameterMap != null) {
            if (!parameterMap.containsKey(name)) {
                return null;
            }
            String[] values = parameterMap.get(name);
            if (values != null && values.length >= 1) {
                return values[0];
            }
            return null; // Or "", since the key does exist?
        }
        return super.getParameter(name);
    }

    // Returns an array of String objects containing all of the values the
    // given request parameter has, or null if the parameter does not exist.
    public String[] getParameterValues(String name) {
        if (parameterMap != null) {
            if (!parameterMap.containsKey(name)) {
                return null;
            }
            return parameterMap.get(name);
        }
        return super.getParameterValues(name);
    }

    public String getContentType() {
        return FileEntryPhaseListener.APPLICATION_FORM_URLENCODED;
    }

}
