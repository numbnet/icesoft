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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.impl.application;

import org.icefaces.impl.event.DeltaSubmitPhaseListener;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.*;
import java.util.logging.Logger;

public class DeltaSubmitParametersPropagation extends ConfigurableNavigationHandler {
    private final static Logger log = Logger.getLogger(DeltaSubmitParametersPropagation.class.getName());
    private final ConfigurableNavigationHandler handler;

    public DeltaSubmitParametersPropagation(final NavigationHandler handler) {
        this.handler = handler instanceof ConfigurableNavigationHandler ? (ConfigurableNavigationHandler) handler : new NoopConfigurableNavigationHandler(handler);
    }

    public NavigationCase getNavigationCase(FacesContext context, String fromAction, String outcome) {
        return handler.getNavigationCase(context, fromAction, outcome);
    }

    public Map<String, Set<NavigationCase>> getNavigationCases() {
        return handler.getNavigationCases();
    }

    public void handleNavigation(FacesContext context, String fromAction, String outcome) {
        NavigationCase navigationCase = getNavigationCase(context, fromAction, outcome);
        if (navigationCase.isRedirect()) {
            handler.handleNavigation(context, fromAction, outcome);
        } else {
            UIViewRoot viewRoot = context.getViewRoot();
            Collection<UIForm> forms = findUIForms(viewRoot);
            Map<String, Object> viewAttributes = viewRoot.getViewMap();
            HashMap idToPreviousParametersMapping = (HashMap) viewAttributes.get(DeltaSubmitPhaseListener.PreviousParameters);
            if (idToPreviousParametersMapping == null) {
                idToPreviousParametersMapping = new HashMap();
                viewAttributes.put(DeltaSubmitPhaseListener.PreviousParameters, idToPreviousParametersMapping);
            }
            for (UIForm form: forms) {
                idToPreviousParametersMapping.put(form.getId(), new HashMap((Map) form.getAttributes().get(DeltaSubmitPhaseListener.PreviousParameters)));
            }
            handler.handleNavigation(context, fromAction, outcome);
            //propagate previously calculated submit parameters
            context.getViewRoot().getViewMap().put(DeltaSubmitPhaseListener.PreviousParameters, idToPreviousParametersMapping);
        }
    }

    private Collection<UIForm> findUIForms(UIViewRoot viewRoot) {
        LinkedList queue = new LinkedList();
        ArrayList result = new ArrayList<UIForm>();
        queue.add(viewRoot);
        //breadth depth search ... it just makes sense
        while (!queue.isEmpty()) {
            UIComponent cursor = (UIComponent) queue.removeFirst();
            if (cursor instanceof UIForm) {
                result.add(cursor);
                //stop searching through form's children since forms cannot be nested
            } else {
                queue.addAll(cursor.getChildren());
            }
        }
        return result;
    }


    private static class NoopConfigurableNavigationHandler extends ConfigurableNavigationHandler {
        private final NavigationHandler handler;

        public NoopConfigurableNavigationHandler(NavigationHandler handler) {
            this.handler = handler;
        }

        public NavigationCase getNavigationCase(FacesContext context, String fromAction, String outcome) {
            log.warning(handler + " is not a ConfigurableNavigationHandler");
            return null;
        }

        public Map<String, Set<NavigationCase>> getNavigationCases() {
            log.warning(handler + " is not a ConfigurableNavigationHandler");
            return null;
        }

        public void handleNavigation(FacesContext context, String fromAction, String outcome) {
            handler.handleNavigation(context, fromAction, outcome);
        }
    }
}
