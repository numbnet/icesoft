/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.commandLink;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;


import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;


@ManagedBean (name="linkBean")
@ViewScoped
public class LinkButtonBean implements Serializable {

    private boolean checked = false;
	private String label = "ActionListener";
	private boolean rendered = false;
    private String linkValue="Link with actionListener only (Text by ValueExpression)";

    private String testingFor = "A valueExpression 'for' value";

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
         FacesContext fc = FacesContext.getCurrentInstance();
         ExternalContext ec = fc.getExternalContext();
         Map requestParameters = ec.getRequestParameterMap();
         Set keys = requestParameters.keySet();
         Iterator i = keys.iterator();
         while (i.hasNext() ) {
             Object key = i.next();
             System.out.println("Request parameter: " + key.toString() +
                                " value: " + requestParameters.get(key) );
         } 
    }

    public String singleSubmitAction() {
        System.out.println("___----- SingleSubmit ACTION ");
        rendered=!rendered;
        return "";
    }


    public void hrefAndActionMethod(ActionEvent e) {
        System.out.println("-----> hrefAndActionMethod called");
        rendered=!rendered;
    }

    public boolean isRendered(){
    	return this.rendered;
    }

    public String getLinkValue() {
        return linkValue;
    }

    public void setLinkValue(String linkValue) {
        //this.linkValue = linkValue;
    }

    public String getForValue() {
        return testingFor;
    }

    public void setForValue(String forValue) {
        testingFor = forValue;
    } 
}