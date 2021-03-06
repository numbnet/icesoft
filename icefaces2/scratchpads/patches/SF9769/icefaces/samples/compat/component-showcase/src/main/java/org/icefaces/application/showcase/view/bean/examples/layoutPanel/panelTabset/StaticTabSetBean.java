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
package org.icefaces.application.showcase.view.bean.examples.layoutPanel.panelTabset;

import com.icesoft.faces.component.paneltabset.TabChangeEvent;
import com.icesoft.faces.component.paneltabset.TabChangeListener;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;
import java.io.Serializable;

/**
 * The StaticTabSetBean class is a backing bean for the TabbedPane showcase
 * demonstration and is used to store the various states of the the
 * ice:panelTabSet component.  These states are visibility, tab selection and
 * tab placement.
 *
 * @since 0.3.0
 */
 @ManagedBean(name = "staticTabbedPaneExample")
 @ViewScoped
public class StaticTabSetBean implements TabChangeListener, Serializable {

    /**
     * The demo contains three tabs and thus we need three variables to store
     * their respective rendered states.
     */
    private boolean tabbedPane1Visible=true;
    private boolean tabbedPane2Visible=true;
    private boolean tabbedPane3Visible=true;

    // selected tab index
    private String selectedIndex = "0";

    /**
     * Tabbed placement, possible values are "top" and "bottom", the default is
     * "bottom".
     */
    private String tabPlacement = "top";

    public boolean isTabbedPane1Visible() {
        return tabbedPane1Visible;
    }

    public void setTabbedPane1Visible(boolean tabbedPane1Visible) {
        this.tabbedPane1Visible = tabbedPane1Visible;
    }

    public boolean isTabbedPane2Visible() {
        return tabbedPane2Visible;
    }

    public void setTabbedPane2Visible(boolean tabbedPane2Visible) {
        this.tabbedPane2Visible = tabbedPane2Visible;
    }

    public boolean isTabbedPane3Visible() {
        return tabbedPane3Visible;
    }

    public void setTabbedPane3Visible(boolean tabbedPane3Visible) {
        this.tabbedPane3Visible = tabbedPane3Visible;
    }

    public String getTabPlacement() {
        return tabPlacement;
    }

    public void setTabPlacement(String tabPlacement) {
        this.tabPlacement = tabPlacement;
    }

    public String getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(String selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = String.valueOf(selectedIndex);
    }

    public int getFocusIndex() {
    	int focusIndex = 0;
    	try{
    		focusIndex = Integer.parseInt(selectedIndex);
    	}
    	catch(NumberFormatException nfe){
    		//do nothing
    	}
        return focusIndex;
    }

    public void setFocusIndex(int index){
        selectedIndex = String.valueOf(index);
    }

    /**
     * Called when the table binding's tab focus changes.
     *
     * @param tabChangeEvent used to set the tab focus.
     * @throws AbortProcessingException An exception that may be thrown by event
     *                                  listeners to terminate the processing of the current event.
     */
    public void processTabChange(TabChangeEvent tabChangeEvent)
            throws AbortProcessingException {
        // only used to show TabChangeListener usage.
    }
}
