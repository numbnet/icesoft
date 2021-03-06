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

package org.icefaces.impl.event;

import org.icefaces.util.EnvUtils;
import org.icefaces.util.JavaScriptRunner;

import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;

public class JavaScriptRunnerSetup implements SystemEventListener {

    public JavaScriptRunnerSetup() {
    }

    public boolean isListenerForSource(Object source) {
        return true;
    }

    public void processEvent(SystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(facesContext)) {
            return;
        }

        UIOutput jsOutput = new UIOutputWriter() {
            public void encode(ResponseWriter writer, FacesContext context) throws IOException {
                writer.startElement("span", this);
                writer.writeAttribute("id", "dynamic-code", null);
                if (!context.getPartialViewContext().isPartialRequest()) {
                    writer.startElement("script", this);
                    writer.writeAttribute("type", "text/javascript", null);
                    writer.write(JavaScriptRunner.collateScripts(context));
                    writer.endElement("script");
                }
                writer.endElement("span");
            }
        };

        UIViewRoot root = facesContext.getViewRoot();
        jsOutput.setTransient(true);
        root.addComponentResource(facesContext, jsOutput, "body");
    }
}
