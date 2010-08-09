package org.icefaces.commandLink;

import org.icefaces.component.commandlink.CommandLink;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.component.UIComponent;


@ManagedBean (name="linkBean")
@ViewScoped
public class LinkButtonBean implements Serializable {

    private boolean checked = false;
	private String label = "ActionListener";
	private boolean rendered = false;


  /** @PostConstruct
   public void init(){
       System.out.println("creating RadioButtonBean version="+this);
   } **/

    public LinkButtonBean(){
   }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean selected) {
        this.checked = selected;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String methodAction(){
    	System.out.println("--> methodAction() callback for link fired");
    	//should return another page for redirection?
    	rendered =! rendered;
    	return " ";
    }
    public void actionListenerMethod(ActionEvent e) {
        System.out.println("_____  Full submit actionListener ");
        rendered = !rendered;
    }

     public void singleSubmitListenerMethod(ActionEvent e) {
        System.out.println("___----- SingleSubmit actionListener ");
        rendered=!rendered;
    }


    public void hrefAndActionMethod(ActionEvent e) {
        System.out.println("-----> hrefAndActionMethod called");
        rendered=!rendered;
    }

    public boolean isRendered(){
    	return this.rendered;
    }
}