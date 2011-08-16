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

import com.sun.faces.facelets.tag.ui.UIDebug;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  As per ICE-5717, the <ui:debug> tag was causing full page refreshes so we wrap it with
 *  a panelGroup in order to contain the updated region and prevent it from searching farther
 *  up the tree.  Because this is a Mojarra-specific issue and uses internal Mojarra classes,
 *  we adopt a mini-factory approach and delegate the handling of the issue to a class
 *  specifically designed for it.  We need to do this in order to allow us to register the
 *  SystemEventListener in a way that's independent of the JSF implementation.
 **/
public class MojarraDebugTagListener implements SystemEventListener {
    private static final Logger LOGGER = Logger.getLogger(MojarraDebugTagListener.class.getName());

    public boolean isListenerForSource(Object source) {
        return true;
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(context)) {
            return;
        }

        if (event instanceof PostAddToViewEvent) {
            final UIDebug debugTag = (UIDebug) ((PostAddToViewEvent) event).getComponent();
            String debugId = debugTag.getId();
            UIComponent parent = (UIComponent) debugTag.getParent();
            //We can pretend the following hack is legitimate because the
            //UIDebug component should be transient and is not useful during
            //restore view
            if (context.getCurrentPhaseId().equals(PhaseId.RESTORE_VIEW)) {
                parent.getChildren().remove(debugTag);
                return;
            }

            if (parent instanceof javax.faces.component.html.HtmlPanelGroup)
                return; //do nothing as it's already contained in panelGroup
            else {
                //find debug in the list of children to know where to re-insert
                Iterator<UIComponent> kids = parent.getChildren().iterator();
                int debugLocation = 0;
                int counter = 0;
                while (kids.hasNext()) {
                    if (kids.next().getId().equals(debugId)) {
                        debugLocation = counter;
                    }
                    counter++;
                }
                parent.getChildren().remove(debugTag);
                //create a panelGroup to enclose the debug tag
                UIComponent enclosingPanel = new javax.faces.component.html.HtmlPanelGroup();
                enclosingPanel.setId("debugPanel");
                enclosingPanel.setInView(true);
                enclosingPanel.setTransient(true);
                //set parent of debug to this enclosingPanel
                enclosingPanel.getChildren().add(debugTag);
                parent.getChildren().add(debugLocation, enclosingPanel);
            }

        }
    }
}
