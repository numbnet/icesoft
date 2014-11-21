/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
import java.util.List;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.mobi.api.ContentPaneController;
import org.icefaces.mobi.utils.JSFUtils;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.component.ITabSet;
import org.icemobile.util.ClientDescriptor;


public class TabSet extends TabSetBase implements ContentPaneController, ITabSet {
    private static Logger logger = Logger.getLogger(TabSet.class.getName());

    private boolean isTop = false;
    public static enum orientation {top, bottom}
    private int index = 0;

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
 /*   public String getSelectedId() {
        return selectedId;
    }  */

    /**
     * The main difference between this and getSelectedId() is that this will
     * automatically handle defaulting to tab 0 if nothing has been specified,
     * or if what has been specified doesn't actually exist.
     *
     * @return The id and index of the current tab
     */
    IdIndex resolveCurrentIdAndIndex() {
        String currentId = getSelectedId();
        int tabIndex = 0;
        // no current id then default to defaultId if present or as a last
        // resort to the first contentPane which is index zero.
        if (!isRendered() || currentId == null || currentId.length() == 0) {
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
        this.index = tabIndex;
        // store the id and index of the selected pane
    //    logger.info("Storing new index="+tabIndex+" id="+currentId);
        return new IdIndex(currentId, tabIndex);
    }

    @Override
    public void broadcast(FacesEvent event)
            throws AbortProcessingException {
        if (event instanceof ValueChangeEvent) {
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

    public boolean setIsTop(String orientation){
        if (orientation != null && orientation.length() > 0) {
            if (orientation.equals(TabSet.OrientationType.bottom.name())) {
                this.isTop =false;
            } else  {
                this.isTop = true;
            }
        }
        return this.isTop;
    }
    public boolean getIsTop(){
        return this.isTop;
    }

    /*
        see if pagePanel is present as ancestor and if the header for pagePanel is as well
        should this be done from PagePanelp and see if tabset is one of it's children
        also, if this can't be changed dynamically, only have to do this once.
     */
    public void setParentHeaderFooter(){
        UIComponent parent = getParent();
        do  {
            if (parent instanceof org.icefaces.mobi.component.pagepanel.PagePanel) {
                setHeaderFooter(parent);
                return;
            }
            parent =  parent.getParent();
        }
        while (parent !=null);
    }

    private void setHeaderFooter(UIComponent parent) {
        if (parent.getFacetCount() > 0) {
            if (null != parent.getFacet("header")) {
                this.setParentHeader(true);
            } else {
                this.setParentHeader(false);
            }
            if (null != parent.getFacet("footer")){
                this.setParentFooter(true);
            }else {
                this.setParentFooter(false);
            }
        }
    }

    public ClientDescriptor getClient() {
        return MobiJSFUtils.getClientDescriptor();
    }

    public int getIndex() {
        return this.index;
    }

    public int getRenderedChildCount(){
        int numberRenderedChildren= 0;
        List<UIComponent> children = getChildren();
        for (UIComponent c : children) {
             if (c.isRendered())numberRenderedChildren++;
        }
  System.out.println(" number of rendered Children="+numberRenderedChildren);
        return numberRenderedChildren;
    }

}
