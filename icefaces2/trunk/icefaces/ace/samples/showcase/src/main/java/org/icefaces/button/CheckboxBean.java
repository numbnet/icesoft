package org.icefaces.button;

import java.io.Serializable;


import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;


@ManagedBean (name="checkboxBean")
@ViewScoped
public class CheckboxBean implements Serializable {

    private boolean checked = false;

    public CheckboxBean(){
   }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean selected) {
        this.checked = selected;
    }

    public void actionListenerMethod(ActionEvent e) {
        checked = !checked;
    }
}