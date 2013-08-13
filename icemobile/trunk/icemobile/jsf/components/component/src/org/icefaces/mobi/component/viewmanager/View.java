/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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
package org.icefaces.mobi.component.viewmanager;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.MethodExpression;
import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.mobi.api.ContentPaneController;
import org.icefaces.mobi.component.tabset.TabSet;
import org.icefaces.mobi.component.tabset.TabSetBase;
import org.icefaces.mobi.component.tabset.TabSet.IdIndex;
import org.icefaces.mobi.component.tabset.TabSet.OrientationType;
import org.icefaces.mobi.renderkit.InlineScriptEventListener;
import org.icefaces.mobi.utils.JSFUtils;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.component.IPagePanel;
import org.icemobile.component.ITabSet;
import org.icemobile.util.ClientDescriptor;

public class View extends ViewBase {
 
    @Override
    public void broadcast(FacesEvent event)
            throws AbortProcessingException {
        if (event instanceof ValueChangeEvent) {
            ValueChangeEvent vce = (ValueChangeEvent) event;
            MethodExpression method = getTabChangeListener();
            if (method != null) {
                method.invoke(getFacesContext().getELContext(), new Object[]{event});
            }
        } else {
            super.broadcast(event);
        }
    }

    @Override
    public void queueEvent(FacesEvent event) {
        if (event.getComponent() == this) {
            boolean isImmediate = isImmediate();
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("invoked event for immediate " + isImmediate);
            }
            if (isImmediate) {
                event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            } else {
                event.setPhaseId(PhaseId.INVOKE_APPLICATION);
            }
        }
        super.queueEvent(event);
    }


}
