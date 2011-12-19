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

import org.icefaces.ace.component.sliderentry.SliderEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIColumn;


/**
 * Slider data backing bean for dynamic changing values
 * @since 1.5
 */
@ManagedBean (name="sliderDataBean")
@ViewScoped
public class SliderDataBean implements Serializable {


    List sliderValues = new ArrayList();

    private String blueClass = "blue_box";
    private String redClass = "red_box";
    private String greenClass = "green_box";


    public SliderDataBean(){
        for (int idx = 0; idx < 3; idx ++ ) {
            sliderValues.add( new SliderHolder() );
        }
    }

    public void setToBlue(ActionEvent ae) {

        UIComponent comp = ae.getComponent();
        while ((comp = comp.getParent()) != null) {
            if (comp instanceof UIColumn) {
                UIColumn uic = (UIColumn) comp;
                List<UIComponent> children = comp.getChildren();
                for (UIComponent child: children) {
                    if (child instanceof SliderEntry) {
                        SliderEntry se = (SliderEntry) child;
                        se.setStyleClass( blueClass );
                        return;
                    }
                }
            } 
        }
    }

     public void setToRed(ActionEvent ae) {

        UIComponent comp = ae.getComponent();
        while ((comp = comp.getParent()) != null) {
            if (comp instanceof UIColumn) {
                UIColumn uic = (UIColumn) comp;
                List<UIComponent> children = comp.getChildren();
                for (UIComponent child: children) {
                    if (child instanceof SliderEntry) {
                        SliderEntry se = (SliderEntry) child;
                        se.setStyleClass( redClass );
                        return;
                    }
                }
            }
        }
    }

    public void setToGreen(ActionEvent ae) {

        UIComponent comp = ae.getComponent();
        while ((comp = comp.getParent()) != null) {
            if (comp instanceof UIColumn) {
                UIColumn uic = (UIColumn) comp;
                List<UIComponent> children = comp.getChildren();
                for (UIComponent child: children) {
                    if (child instanceof SliderEntry) {
                        SliderEntry se = (SliderEntry) child;
                        se.setStyleClass( greenClass );
                        return;
                    }
                }
            }
        }
    }


    public List getSliderValues() {
        return sliderValues;
    }

}