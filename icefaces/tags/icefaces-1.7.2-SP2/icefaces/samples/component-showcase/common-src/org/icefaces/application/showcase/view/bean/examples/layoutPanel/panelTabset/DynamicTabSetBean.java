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

import org.icefaces.application.showcase.util.MessageBundleLoader;

import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * The DynamicTabSetBean is the backing bean for the TabbedPane showcase
 * demonstration.  It is used to dynamically add and remove tabs used in
 * conjunction with the ice:panelTabSet component.
 *
 * @since 0.3.0
 */
public class DynamicTabSetBean implements Serializable {

    private int tabIndex;
    private String newTabLabel;
    private String newTabContent;
    private List tabs = new ArrayList();

    public DynamicTabSetBean() {

        // pre-defined two tabs into the panelTabSet
        Tab newTab1 = new Tab();
        newTab1.setLabel(MessageBundleLoader.getMessage("bean.panelTabSet.dynamic.default.label1"));
        newTab1.setContent(MessageBundleLoader.getMessage("bean.panelTabSet.dynamic.default.content1"));
        tabs.add(newTab1);

        Tab newTab2 = new Tab();
        newTab2.setLabel(MessageBundleLoader.getMessage("bean.panelTabSet.dynamic.default.label2"));
        newTab2.setContent(MessageBundleLoader.getMessage("bean.panelTabSet.dynamic.default.content2"));
        tabs.add(newTab2);
    }

    /**
     * remove a tab from panelTabSet.
     *
     * @param event remove button click.
     */
    public void removeTab(ActionEvent event) {
        // remove the specified tab index if possible.
        if (tabs != null && tabs.size() > tabIndex) {
            tabs.remove(tabIndex);
            // try and fine a valid index
            if (tabIndex > 0) {
                tabIndex--;
            }
        }
    }

    /**
     * add a new tab to the panelTabSet.
     *
     * @param event add button click.
     */
    public void addTab(ActionEvent event) {

        // assign default label if it's blank
        if (newTabLabel.equals("")) {
            newTabLabel = MessageBundleLoader.getMessage(
                    "bean.panelTabSet.dynamic.labelString") + (tabIndex + 1);
            newTabContent = MessageBundleLoader.getMessage(
                    "bean.panelTabSet.dynamic.contentString") + (tabIndex + 1);
        }

        // set the new tab from the input
        Tab newTab = new Tab();
        newTab.setContent(newTabContent);
        newTab.setLabel(newTabLabel);

        // add to both tabs and select options of selectRadiobox
        tabs.add(newTab);

        // clean up input field
        newTabLabel = "";
        newTabContent = "";
    }

    public String getNewTabLabel() {
        return newTabLabel;
    }

    public void setNewTabLabel(String newTabLabel) {
        this.newTabLabel = newTabLabel;
    }

    public String getNewTabContent() {
        return newTabContent;
    }

    public void setNewTabContent(String newTabContent) {
        this.newTabContent = newTabContent;
    }

    public List getTabs() {
        return tabs;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public int getTabsSize(){
        return tabs.size();
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
     * Inner class that represents a tab object with a label, content, and an
     * index.
     */
    public class Tab {
        String label;
        String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

}
