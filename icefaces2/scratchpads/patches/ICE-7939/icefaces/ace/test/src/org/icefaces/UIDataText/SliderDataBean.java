package org.icefaces.UIDataText;

import org.icefaces.component.sliderentry.SliderEntry;

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