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

package org.icefaces.ace.component.list;

import org.icefaces.ace.event.ListSelectEvent;

import javax.el.MethodExpression;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.model.DataModel;
import java.util.*;

public class ACEList extends ListBase {
    @Override
    protected DataModel getDataModel() {
        return super.getDataModel();
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public Set<Object> getSelections() {
        Set<Object> set = super.getSelections();
        if (set == null) {
            set = new HashSet<Object>();
            setSelections(set);
        }
        return set;
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        FacesContext context = FacesContext.getCurrentInstance();
        String outcome = null;
        MethodExpression me = null;

        if (event instanceof ListSelectEvent) me = getSelectionListener();

        if (me != null) {
            if (!context.isValidationFailed()) {
                outcome = (String) me.invoke(context.getELContext(), new Object[] {event});
            }

            if (outcome != null) {
                NavigationHandler navHandler = context.getApplication().getNavigationHandler();
                navHandler.handleNavigation(context, null, outcome);
                context.renderResponse();
            }
        }
    }

    // References to immigrant objects are gathered immediately
    List<ImmigrationRecord> immigrants;

    public List<ImmigrationRecord> getImmigrants() {
        return immigrants;
    }

    public void setImmigrants(List<ImmigrationRecord> immigrants) {
        this.immigrants = immigrants;
    }

}
