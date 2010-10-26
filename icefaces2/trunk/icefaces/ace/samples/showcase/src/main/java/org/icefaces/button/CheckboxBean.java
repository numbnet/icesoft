package org.icefaces.button;

import java.io.Serializable;


import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;


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

    public void changeValue(ActionEvent e) {
        checked = !checked;
    }

    public String getImageName() {
        System.out.println(checked ? "images/checked.gif" : "images/unchecked.gif" );

        return checked ? "images/checked.gif" : "images/unchecked.gif"; 
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}