
package org.icefaces.demo.ajax;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "ajaxBean")
@ViewScoped
public class AjaxBean implements Serializable {
    Boolean visible = false;
    int toggleCount = 0;

    public Boolean getVisible() {
        return visible;
    }

    public void toggle(ActionEvent ae) {
        toggleCount++;
        visible = !visible;
    }

    public String getResponseWriter(){
        FacesContext fc = FacesContext.getCurrentInstance();
        ResponseWriter rw = fc.getResponseWriter();
        return rw.getClass().getName();
    }

    public int getToggleCount(){
        return toggleCount;
    }

}