package org.icefaces.button;

import java.io.Serializable;


import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;


@ManagedBean (name="checkBean")
@ViewScoped
public class CheckboxBean implements Serializable {

    private boolean checked = false;
    private String imageName;

    public CheckboxBean(){

   }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean selected) {
        this.checked = selected;
    }


}