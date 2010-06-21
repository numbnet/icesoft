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
import org.icefaces.push.servlet.SessionExpiredException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WindowScopeSetup implements PhaseListener {
    private static Logger log = Logger.getLogger(WindowScopeSetup.class.getName());

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    public void beforePhase(PhaseEvent event) {
        final FacesContext context = FacesContext.getCurrentInstance();

        if (context.getExternalContext().getRequestMap().containsKey(SessionExpiredException.class.getName())) {
            throw new SessionExpiredException("User session has expired");
        }

        try {
            ExternalContext externalContext = context.getExternalContext();
            //ICE-5281:  We require that a session be available at this point and it may not have
            //           been created otherwise.
            Object session = externalContext.getSession(true);
            WindowScopeManager.determineWindowID(context);
        } catch (Exception e) {
            log.log(Level.WARNING, "Unable to set up WindowScope ", e);
        }
    }

    public void afterPhase(PhaseEvent event) {
    }
}
