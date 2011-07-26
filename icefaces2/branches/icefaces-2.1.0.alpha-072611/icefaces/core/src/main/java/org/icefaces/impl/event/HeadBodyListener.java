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

import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The aim of the class is to listen for HtmlHead and HtmlBody components as we
 * won't attempt to add any ICEfaces scripts and resources unless these components
 * are present in the view.  This class is currently not optimized in how it detects
 * the components but this might be related to a bug in JSF.
 */
public class HeadBodyListener implements SystemEventListener {
    private final static Logger log = Logger.getLogger("org.icefaces.impl.event.HeadBodyListener");

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = context.getViewRoot();
        Map viewMap = viewRoot.getViewMap();

        //TODO: improve detection of HtmlHead and HtmlBody components
        // Since the current Sun JSF implementation returns HtmlHead and HtmlBody
        // as UIOutput component instances, we can't rely on that to for detection.
        // Instead, we need to check the rendererType which is potentially fragile
        // as it can be overridden.

        if (!viewMap.containsKey(EnvUtils.HEAD_DETECTED)) {
            List<UIComponent> children = viewRoot.getChildren();
            for (UIComponent c : children) {
                String rendererType = c.getRendererType();
                if ("javax.faces.Head".equals(rendererType)) {
                    viewMap.put(EnvUtils.HEAD_DETECTED, EnvUtils.HEAD_DETECTED);
                    if (log.isLoggable(Level.FINER)) {
                        log.log(Level.FINER, "head detected");
                    }
                }
            }
        }

        if (!viewMap.containsKey(EnvUtils.BODY_DETECTED)) {
            List<UIComponent> children = viewRoot.getChildren();
            for (UIComponent c : children) {
                String rendererType = c.getRendererType();
                if ("javax.faces.Body".equals(rendererType)) {
                    viewMap.put(EnvUtils.BODY_DETECTED, EnvUtils.BODY_DETECTED);
                    if (log.isLoggable(Level.FINER)) {
                        log.log(Level.FINER, "body detected");
                    }
                }
            }
        }
    }

    public boolean isListenerForSource(Object source) {
        return source instanceof UIViewRoot;
    }
}
