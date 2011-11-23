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

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility API for evaluating Javascript code on the client.
 */
public class JavaScriptRunner {

    /**
     * Send immediately Javascript code to the client for evaluation and execution.
     *
     * @param context the current FacesContext
     * @param script  the Javascript code
     */
    public static void runScript(FacesContext context, String script) {
        Map requestMap = context.getExternalContext().getRequestMap();
        Collection scripts = (Collection) requestMap.get(JavaScriptRunner.class.getName());
        if (scripts == null) {
            scripts = new ArrayList();
            requestMap.put(JavaScriptRunner.class.getName(), scripts);
        }

        scripts.add(script);
    }

    public static String collateScripts(FacesContext context) {
        Map requestMap = context.getExternalContext().getRequestMap();
        Collection scripts = (Collection) requestMap.get(JavaScriptRunner.class.getName());
        if (scripts == null) {
            return "";
        } else {
            StringBuffer buffer = new StringBuffer();
            Iterator i = scripts.iterator();
            while (i.hasNext()) {
                String script = ((String) i.next()).trim();
                buffer.append(script);
                if (!script.endsWith(";")) {
                    buffer.append(";");
                }
            }

            return buffer.toString();
        }
    }
}
