package org.icefaces.button;

import java.io.Serializable;


import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;


@ManagedBean (name="linkBean")
@ViewScoped
public class LinkButtonBean implements Serializable {

    private boolean checked = false;
	private String label = "ActionListener";
	private boolean rendered = false;
    private String linkValue="Link with actionListener only (Text by ValueExpression)";


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
    	//should return another page for redirection?
    	rendered =! rendered;
    	return " ";
    }
    public void actionListenerMethod(ActionEvent e) {
        rendered = !rendered;
    }

     public void singleSubmitListenerMethod(ActionEvent e) {
        rendered=!rendered;
    }


    public void hrefAndActionMethod(ActionEvent e) {
        rendered=!rendered;
    }

    public boolean isRendered(){
    	return this.rendered;
    }

    public String getLinkValue() {
        return linkValue;
    }

    public void setLinkValue(String linkValue) {
        this.linkValue = linkValue;
    }
}