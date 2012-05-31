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
package org.icefaces.mobi.component.tabset;

import org.icefaces.mobi.api.ContentPaneController;
import org.icefaces.mobi.utils.Utils;

import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELException;


public class TabSet extends TabSetBase implements ContentPaneController {
     private static Logger logger = Logger.getLogger(TabSet.class.getName());
     public static final StringBuilder TABSET_CONTAINER_CLASS = new StringBuilder("mobi-tabset ");
     public static final StringBuilder TABSET_TABS_CLASS = new StringBuilder("mobi-tabset-tabs ");
     public static final StringBuilder TABSET_ACTIVETAB_CLASS = new StringBuilder("activeTab ");
     public static final StringBuilder TABSET_CONTENT_CLASS = new StringBuilder("mobi-tabset-content ");
     public static final StringBuilder TABSET_HIDDEN_PAGECLASS = new StringBuilder("mobi-tabpage-hidden");

     private boolean updatePropScriptTag = false;

    /**
     * method is required by ContentPaneController interface no error checking as
     * component is not in the tree
     */
    public String getSelectedId(){
        return getCurrentId();
    }

    /**
     * The main difference between this and getSelectedId() is that this will
     * automatically handle defaulting to tab 0 if nothing has been specified,
     * or if what has been specified doesn't actually exist.
     * 
     * @return The id and index of the current tab
     */
    IdIndex resolveCurrentIdAndIndex() {
        String currId = getCurrentId();
        int tabIndex;
        if (currId == null || currId.length() == 0) {
            tabIndex = 0;
            UIComponent comp = getChildren().get(tabIndex);
            currId = comp.getId();
        } else {
            UIComponent comp = Utils.getChildById(this, currId);
            if (comp == null) {
                tabIndex = 0;
                comp = getChildren().get(tabIndex);
                currId = comp.getId();
            } else {
                tabIndex = getChildren().indexOf(comp);
            }
        }
        return new IdIndex(currId, tabIndex);
    }

      public void broadcast(FacesEvent event)
            throws AbortProcessingException {
        if (event instanceof ValueChangeEvent) {
            ValueChangeEvent vce = (ValueChangeEvent) event;
            setCurrentId((String)vce.getNewValue());
            MethodExpression method = getTabChangeListener();
            if (method != null) {
                method.invoke(getFacesContext().getELContext(), new Object[]{event});
            }
        } else {
            super.broadcast(event);
        }
    }
    public void queueEvent(FacesEvent event) {
        if (event.getComponent() == this) {
            boolean isImmediate = isImmediate();
            if (logger.isLoggable(Level.FINEST)){
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

    public void setUpdatePropScriptTag (boolean update){
        this.updatePropScriptTag = update;
    }
    public boolean isUpdatePropScriptTag(){
        return this.updatePropScriptTag;
    }


    public static class IdIndex {
        private String id;
        private int index;

        public IdIndex(String id, int index) {
            this.id = id;
            this.index = index;
        }

        public String getId() {
            return id;
        }

        public int getIndex() {
            return index;
        }
    }
}
