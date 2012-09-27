/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.impl.event;

import org.icefaces.impl.application.WindowScopeManager;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class WindowAndViewIDSetup implements SystemEventListener {
    private static final Logger Log = Logger.getLogger(WindowAndViewIDSetup.class.getName());
    private static final String ID_SUFFIX = "_windowviewid";
    private boolean partialStateSaving;

    public WindowAndViewIDSetup() {
        partialStateSaving = EnvUtils.isPartialStateSaving(
                FacesContext.getCurrentInstance());
    }

    public void processEvent(final SystemEvent event) throws AbortProcessingException {
        UIForm form = (UIForm) ((ComponentSystemEvent) event).getComponent();
        String componentId = form.getId() + ID_SUFFIX;

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
        if (!(source instanceof UIForm)) {
            return false;
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(facesContext)) {
            return false;
        }
        UIForm htmlForm = (UIForm) source;
        String componentId = htmlForm.getId() + ID_SUFFIX;
        if (!partialStateSaving) {
            for (UIComponent child : htmlForm.getChildren()) {
                String id = child.getId();
                if ((null != id) && id.endsWith(ID_SUFFIX)) {
                    return false;
                }
            }
        }
        // Guard against duplicates within the same JSF lifecycle
        for (UIComponent comp : htmlForm.getChildren()) {
            if (componentId.equals(comp.getId())) {
                return false;
            }
        }
        return true;
    }
}
