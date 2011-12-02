/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.icefaces.UIDataText;

import org.icefaces.ace.component.tabset.TabSet;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.component.UIComponent;

/**
 *
 */
@ManagedBean(name="tabBackingBean")
@ViewScoped
public class TabBackingBean {

    private String tabContents = "If the deviant structure of the universe is allowed to roam " +
                                 "wildly in-regurgio, it acts as a locality for munificent locus " +
                                 "events in a-prime over the surface of the oscillating Sphere. ";

    private String redClass="red_box";
    private String blueClass="blue_box";
    private String greenClass="green_box";


    public void setToBlue(ActionEvent ae) {

        UIComponent comp = ae.getComponent();
        while ((comp = comp.getParent()) != null) {
            if (comp instanceof TabSet) {
                TabSet tabComp = (TabSet) comp;
                tabComp.setStyleClass(blueClass);
            }
        }
    }

     public void setToRed(ActionEvent ae) {

        UIComponent comp = ae.getComponent();
        while ((comp = comp.getParent()) != null) {
            if (comp instanceof TabSet) {
                TabSet tabComp = (TabSet) comp;
                tabComp.setStyleClass(redClass);
            }
        }
    }

     public void setToGreen(ActionEvent ae) {

        UIComponent comp = ae.getComponent();
        while ((comp = comp.getParent()) != null) {
            if (comp instanceof TabSet) {
                TabSet tabComp = (TabSet) comp;
                tabComp.setStyleClass(greenClass);
            }
        }
    }

    public String getTabContents() {
        return tabContents;
    }

    public void setTabContents(String tabContents) {
        this.tabContents = tabContents;
    }
}
