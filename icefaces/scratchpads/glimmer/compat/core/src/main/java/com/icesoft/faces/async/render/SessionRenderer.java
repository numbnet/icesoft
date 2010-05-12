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

package com.icesoft.faces.async.render;

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.logging.Logger;

public class SessionRenderer extends PushRenderer {
    private static Logger log = Logger.getLogger(SessionRenderer.class.getName());
    //avoid referencing PortableRenderer class in static context so that application still works when ICEpush not present
    private static Object portableRenderer;

    public static void render(String groupName) {
        if (portableRenderer != null) {
            if (FacesContext.getCurrentInstance() == null) {
                ((PortableRenderer) portableRenderer).render(groupName);
            } else {
                PushRenderer.render(groupName);
            }
        }
    }

    public static class StartupListener implements SystemEventListener {
        public void processEvent(SystemEvent event) throws AbortProcessingException {
            try {
                portableRenderer = PushRenderer.getPortableRenderer(FacesContext.getCurrentInstance());
            } catch (NoClassDefFoundError e) {
                log.info("ICEpush library missing. Cannot enable push functionality.");
            }
        }

        public boolean isListenerForSource(Object source) {
            return true;
        }
    }
}
