package org.icefaces.demo.basic;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "basic")
@SessionScoped
public class Basic {
    Boolean visible = false;

    public Boolean getVisible() {
System.out.println("getvisible");
        return visible;
    }

    public void toggle(ActionEvent ae) {
        visible = !visible;
    }

}