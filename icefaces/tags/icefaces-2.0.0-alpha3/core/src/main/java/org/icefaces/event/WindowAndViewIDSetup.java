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

package org.icefaces.event;

import org.icefaces.application.WindowScopeManager;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;

public class WindowAndViewIDSetup implements SystemEventListener {

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(context)) {
            return;
        }

        HtmlForm form = (HtmlForm) ((PostAddToViewEvent) event).getComponent();
        UIOutput output = new UIOutputWriter() {
            public void encode(ResponseWriter writer, FacesContext context) throws IOException {
                writer.startElement("input", this);
                writer.writeAttribute("type", "hidden", null);
                writer.writeAttribute("name", "ice.window", null);
                writer.writeAttribute("value", WindowScopeManager.lookup(context).lookupWindowScope().getId(), null);
                writer.endElement("input");

                writer.startElement("input", this);
                writer.writeAttribute("type", "hidden", null);
                writer.writeAttribute("name", "ice.view", null);
                writer.writeAttribute("value", context.getExternalContext().getRequestMap().get(BridgeSetup.ViewState), null);
                writer.endElement("input");
            }
        };
        output.setTransient(true);
        output.setId(form.getId() + "_windowviewid");
        form.getChildren().add(0, output);
    }

    public boolean isListenerForSource(Object source) {
        return source instanceof HtmlForm;
    }
}
