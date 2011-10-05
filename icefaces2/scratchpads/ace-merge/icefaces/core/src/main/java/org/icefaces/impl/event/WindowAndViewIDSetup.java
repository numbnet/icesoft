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

package org.icefaces.impl.event;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;

import org.icefaces.impl.application.WindowScopeManager;
import org.icefaces.util.EnvUtils;

public class WindowAndViewIDSetup implements SystemEventListener {
    private static final Logger Log = Logger.getLogger(WindowAndViewIDSetup.class.getName());
    private static final String ID_SUFFIX = "_windowviewid";
    private boolean partialStateSaving;

    public WindowAndViewIDSetup()  {
        partialStateSaving = EnvUtils.isPartialStateSaving(
            FacesContext.getCurrentInstance() );
    }

    public void processEvent(final SystemEvent event) throws AbortProcessingException {
        final FacesContext context = FacesContext.getCurrentInstance();
        HtmlForm form = (HtmlForm) ((PostAddToViewEvent) event).getComponent();
        String componentId = form.getId() + ID_SUFFIX;
        context.getAttributes().put(componentId, componentId);

        UIOutput output = new UIOutputWriter() {
            public void encode(ResponseWriter writer, FacesContext context) throws IOException {
                Map requestMap = context.getExternalContext().getRequestMap();

                if (WindowScopeManager.lookupAssociatedWindowID(requestMap) == null) {
                    Log.severe("Missing window ID attribute. Request map cleared prematurely.");
                    return;
                }
                String viewId = BridgeSetup.getViewID(context.getExternalContext());
                if (viewId == null) {
                    Log.severe("Missing view ID attribute. Request map cleared prematurely.");
                    return;
                }
                writer.startElement("input", this);
                writer.writeAttribute("type", "hidden", null);
                writer.writeAttribute("name", "ice.window", null);
                writer.writeAttribute("value", WindowScopeManager.lookupWindowScope(context).getId(), null);
                writer.endElement("input");

                writer.startElement("input", this);
                writer.writeAttribute("type", "hidden", null);
                writer.writeAttribute("name", "ice.view", null);
                writer.writeAttribute("value", viewId, null);
                writer.endElement("input");
            }
        };

        output.setTransient(true);
        output.setId(componentId);
        form.getChildren().add(0, output);
    }

    public boolean isListenerForSource(final Object source) {
        if (!(source instanceof HtmlForm)) {
            return false;
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(facesContext)) {
            return false;
        }
        HtmlForm htmlForm = (HtmlForm)source;
        String componentId = htmlForm.getId() + ID_SUFFIX;
        if (!partialStateSaving)  {
            for (UIComponent child : htmlForm.getChildren())  {
                String id = child.getId();
                if ((null != id) && id.endsWith(ID_SUFFIX))  {
                    return false;
                }
            }
        }
        // Guard against duplicates within the same JSF lifecycle
        if (null != facesContext.getAttributes().get(componentId)) {
            return false;
        }
        return true;
    }
}
