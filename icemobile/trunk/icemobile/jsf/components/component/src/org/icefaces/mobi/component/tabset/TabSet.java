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
package org.icefaces.mobi.component.tabset;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.mobi.api.ContentPaneController;
import org.icefaces.mobi.utils.JSFUtils;


public class TabSet extends TabSetBase implements ContentPaneController {
    private static Logger logger = Logger.getLogger(TabSet.class.getName());
    public static final StringBuilder TABSET_CONTAINER_CLASS = new StringBuilder("mobi-tabset ");
    public static final StringBuilder TABSET_CONTAINER_BOTTOM_CLASS = new StringBuilder("mobi-tabset-tabs-bottom ");
    public static final StringBuilder TABSET_CONTAINER_BOTTOM_FOOTER_CLASS = new StringBuilder("mobi-tabset-tabs-bottom-footer ");
    public static final StringBuilder TABSET_CONTAINER_TOP_CLASS = new StringBuilder("mobi-tabset-tabs-top ");
    public static final StringBuilder TABSET_CONTAINER_TOP_HEADER_CLASS = new StringBuilder("mobi-tabset-tabs-top-header ");
    public static final StringBuilder TABSET_TABS_CLASS = new StringBuilder("mobi-tabset-tabs ui-bar-b");
    /* activeTab is now done with javascript so tabs are not rendered every request if not changed */
  //  public static final StringBuilder TABSET_ACTIVETAB_CLASS = new StringBuilder("activeTab ");
    public static final StringBuilder TABSET_CONTENT_CLASS = new StringBuilder("mobi-tabset-content ");
    public static final StringBuilder TABSET_HIDDEN_PAGECLASS = new StringBuilder("mobi-tabpage-hidden");
    public static final String TABSET_ACTIVE_CONTENT_CLASS= "mobi-tabpage";

    private boolean updatePropScriptTag = false;

    public static enum orientation {top, bottom}

    public enum OrientationType {
        top, bottom;
        public static final OrientationType DEFAULT = OrientationType.bottom;

        public boolean equals(String deviceName) {
            return this.name().equals(deviceName);
        }
    }

    /**
     * method is required by ContentPaneController interface no error checking as
     * component is not in the tree
     */
    public String getSelectedId() {
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
        String currentId = getCurrentId();
        int tabIndex = 0;
        // no current id then default to defaultId if present or as a last
        // resort to the first contentPane which is index zero.
        if (currentId == null || currentId.length() == 0) {
            String defaultId = getDefaultId();
            if (defaultId != null && defaultId.length() > 0) {
                // load the default id
                currentId = defaultId;
            }
        }
        // resolve the current id.
        UIComponent comp = JSFUtils.getChildById(this, currentId);
        // found a component, now grap the id of it for later use.
        if (comp != null) {
            tabIndex = getChildren().indexOf(comp);
        }
        // fall back to the first index if currentId didn't resolve to a comp.
        else {
            comp = getChildren().get(tabIndex);
            currentId = comp.getId();
        }

        // store the id and index of the selected pane
        return new IdIndex(currentId, tabIndex);
    }

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

    public void setUpdatePropScriptTag(boolean update) {
        this.updatePropScriptTag = update;
    }

    public boolean isUpdatePropScriptTag() {
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
