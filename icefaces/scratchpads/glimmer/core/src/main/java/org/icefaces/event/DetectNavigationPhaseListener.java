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


import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.util.logging.Logger;
import java.util.logging.Level;

public class DetectNavigationPhaseListener implements PhaseListener {

    private static Logger log = Logger.getLogger("org.icefaces.event.DetectNavigationPhaseListener");

    public static String NAVIGATION_MARK = "org.icefaces.navigationMark";

    public void beforePhase(PhaseEvent phaseEvent) {
        FacesContext fc = phaseEvent.getFacesContext();
        UIViewRoot root = fc.getViewRoot();
        if( root == null ){
            return;
        }

        Object mark = root.getViewMap().get(NAVIGATION_MARK);
        if( mark == null ){
            root.getViewMap().put(NAVIGATION_MARK,NAVIGATION_MARK);
        }
    }

    public void afterPhase(PhaseEvent phaseEvent) {
        FacesContext fc = phaseEvent.getFacesContext();
        UIViewRoot root = fc.getViewRoot();
        if( root == null ){
            return;
        }

        Object mark = root.getViewMap().get(NAVIGATION_MARK);
        if( mark == null ){
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.INVOKE_APPLICATION;
    }

}
