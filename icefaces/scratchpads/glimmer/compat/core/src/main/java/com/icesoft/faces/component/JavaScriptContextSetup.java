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

package com.icesoft.faces.component;

import javax.faces.context.FacesContext;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.icefaces.util.EnvUtils;
import com.icesoft.faces.context.effects.JavascriptContext;

public class JavaScriptContextSetup implements SystemEventListener {

    public JavaScriptContextSetup() {
    }

    public boolean isListenerForSource(Object source) {
        return true;
    }

    public void processEvent(SystemEvent event)  {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        
        if (!EnvUtils.isICEfacesView(facesContext)) {
            return;
        }

        UIViewRoot root = facesContext.getViewRoot();
        UIOutput jsContextOutput = new UIOutput() {
            public Object getValue() {
                String scriptPre = "<div id=\"dynamic-code\" style=\"visibility: hidden; display: none;\">" +
                        "<script type=\"text/javascript\">";
                String scriptPost = "</script></div>";
                String scriptContents = "";
                if (!facesContext.getPartialViewContext().isPartialRequest()) {
                    scriptContents = JavascriptContext.getJavascriptCalls(facesContext);
                }
                return scriptPre + scriptContents + scriptPost;
            }
        };
        jsContextOutput.setTransient(true);
        jsContextOutput.getAttributes().put("escape", "false");
        
        root.addComponentResource(facesContext, jsContextOutput, "body");

    }
}
