package org.icefaces.UIDataText;

import org.icefaces.component.tab.TabSet;

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
