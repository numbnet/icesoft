/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * 2004-2008 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

package org.icefaces.event;


import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class DetectNavigationPhaseListener implements PhaseListener {
    public static String RESTORE_VIEW_ID = 
            DetectNavigationPhaseListener.class.getName() + "RESTORE_VIEW_ID";
    public static String RENDER_VIEW_ID = 
            DetectNavigationPhaseListener.class.getName() + "RENDER_VIEW_ID";
    public static String NAVIGATED = 
            DetectNavigationPhaseListener.class.getName() + "NAVIGATED";

    public void afterPhase(PhaseEvent phaseEvent) {
        FacesContext facesContext = phaseEvent.getFacesContext();
        if (PhaseId.RESTORE_VIEW == phaseEvent.getPhaseId()) {
            facesContext.getAttributes().put( RESTORE_VIEW_ID, 
                    facesContext.getViewRoot().getViewId() );
        }
    }

    public void beforePhase(PhaseEvent phaseEvent) {
        FacesContext facesContext = phaseEvent.getFacesContext();
        if (PhaseId.RENDER_RESPONSE == phaseEvent.getPhaseId()) {
            Object restoreViewId = facesContext.getAttributes()
                    .get(RESTORE_VIEW_ID);
            if (!facesContext.getViewRoot().getViewId().equals(restoreViewId))  {
                facesContext.getAttributes().put( NAVIGATED, Boolean.TRUE);
            }
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

}
