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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
package org.icefaces.application.showcase.view.bean.examples.layoutPanel.panelTabset;

import com.icesoft.faces.component.paneltabset.PanelTabSet;
import com.icesoft.faces.component.paneltabset.TabChangeEvent;
import com.icesoft.faces.component.paneltabset.TabChangeListener;
import org.icefaces.application.showcase.util.MessageBundleLoader;

import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;

/**
 * The StaticTabSetBean class is a backing bean for the TabbedPane showcase
 * demonstration and is used to store the various states of the the
 * ice:panelTabSet component.  These states are visibility, tab selection and
 * tab placement.
 *
 * @since 0.3.0
 */
public class StaticTabSetBean implements TabChangeListener {

    /**
     * The demo contains three tabs and thus we need three variables to store
     * their respective rendered states.
     */
    private boolean tabbedPane1Visible;
    private boolean tabbedPane2Visible;
    private boolean tabbedPane3Visible;
    private HtmlSelectOneRadio selectedTabObject;

    /**
     * Tabbed placement, possible values are "top" and "bottom", the default is
     * "bottom".
     */
    private String tabPlacement =
            MessageBundleLoader.getMessage("page.panelTabSet.top");

    // default tab focus.
    private String selectedTabFocus = "1";

    /**
     * Binding used by example to listen
     */
    private PanelTabSet tabSet;


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

    public PanelTabSet getTabSet() {
        return tabSet;
    }

    public void setTabSet(PanelTabSet tabSet) {
        this.tabSet = tabSet;
    }

    /**
     * Called when the tab pane focus is to be changed.
     *
     * @param event new value is the new selected tab index.
     */
    public void selectTabFocus(ValueChangeEvent event) {
        int index = Integer.parseInt((String) event.getNewValue());
        tabSet.setSelectedIndex(index);
    }

    /**
     * Called when a tab is selected.
     *
     * @param event value is the selected tab.
     */
    public void selectTab(ValueChangeEvent event) {
        UIInput component = (UIInput) event.getComponent();
        int index = 1;
        try {
            index = Integer.parseInt(component.getValue().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        tabSet.setSelectedIndex(index);
    }

    public String getTabPlacement() {
        return tabPlacement;
    }

    public void setTabPlacement(String tabPlacement) {
        this.tabPlacement = tabPlacement;
    }

    /**
     * Method is called when there has been a request to change the tab
     * placement.
     *
     * @param event contains new tab placement data.
     */
    public void selectTabPlacement(ValueChangeEvent event) {
        tabPlacement = (String) event.getNewValue();
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
        setSelectedTabFocus(String.valueOf(tabChangeEvent.getNewTabIndex()));
        if (selectedTabObject != null) {
            selectedTabObject.setSubmittedValue(selectedTabFocus);
        }
    }

    public String getSelectedTabFocus() {
        return selectedTabFocus;
    }

    public void setSelectedTabFocus(String selectedTabFocus) {
        this.selectedTabFocus = selectedTabFocus;
    }

    public HtmlSelectOneRadio getBindSelectedTabObject() {
        return selectedTabObject;
    }

    public void setBindSelectedTabObject(HtmlSelectOneRadio selectedTabObject) {
        this.selectedTabObject = selectedTabObject;
    }

}
