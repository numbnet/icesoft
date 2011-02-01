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

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import java.io.Serializable;
import java.util.Map;
import org.icefaces.component.testComponent.TestComponent;

@ManagedBean(name="fParamBean")
@ViewScoped
public class fParamBean implements Serializable {

    private String firstParameterKey = "paramOne";
    private String secondParameterKey = "paramTwo";

    private String firstParameter;
    private String secondParameter;

    private boolean checkOneChecked;
    private boolean checkTwoChecked;

    private String junkParameter = " \\\\ // ]]>  '  \" \u7669 \u4e40 \u019e # $\t alert('hello'); ";

    private boolean showError;

    private int sliderValue = 50;

    private int intParam =99;
    private boolean boolParam = Boolean.TRUE;
    private Object objectParam = new Object();

    private String forParam = "undefined";
    private String forParameter = "Horton Hears a Who";

    public void linkActionMethod (ActionEvent ae) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Map requestParameters = ec.getRequestParameterMap();

        firstParameter = (String) requestParameters.get(firstParameterKey);
        secondParameter = (String) requestParameters.get(secondParameterKey);

        System.out.println("Hello I'm in the link method. firstParameter: " + firstParameter + ", secondParam: " + secondParameter);


    }

    public void buttonActionMethod (ActionEvent ae) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Map requestParameters = ec.getRequestParameterMap();

        firstParameter = (String) requestParameters.get(firstParameterKey);
        secondParameter = (String) requestParameters.get(secondParameterKey);
        System.out.println("Hello I'm in the button method. firstParameter: " + firstParameter + ", secondParam: " + secondParameter);
    }

    public void checkOneChanged (ValueChangeEvent vae) {
        System.out.println("Hello I'm in the check valuechange method. firstParameter: " + firstParameter + ", secondParam: " + secondParameter);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Map requestParameters = ec.getRequestParameterMap();

        firstParameter = (String) requestParameters.get(firstParameterKey);
        secondParameter = (String) requestParameters.get(secondParameterKey);
        System.out.println("Hello I'm in the check valuechange method. firstParameter: " + firstParameter + ", secondParam: " + secondParameter);
    }

     public void checkJunk (ActionEvent ae) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Map requestParameters = ec.getRequestParameterMap();

        firstParameter = (String) requestParameters.get(firstParameterKey);
        secondParameter = (String) requestParameters.get(secondParameterKey);

        showError = !( junkParameter.equals( firstParameter ));
         if (showError) {
             System.out.println("mismatch found, parameter=[" + firstParameter + "] junk: [" + junkParameter + "]");
         }
    }

    public void radioOneChanged (ValueChangeEvent vae) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Map requestParameters = ec.getRequestParameterMap();
        System.out.println("---> Value changed : " + vae.toString() );

        firstParameter = (String) requestParameters.get(firstParameterKey);
        secondParameter = (String) requestParameters.get(secondParameterKey);
        System.out.println("___----__--__  Hello I'm in the RADIO valuechange method. firstParameter: " + firstParameter + ", secondParam: " + secondParameter);
    }

     public void sliderChanged (ValueChangeEvent vae) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Map requestParameters = ec.getRequestParameterMap();
        System.out.println("---> Slider Value changed : " + vae.toString() );

        firstParameter = (String) requestParameters.get(firstParameterKey);
        secondParameter = (String) requestParameters.get(secondParameterKey);
        System.out.println("___----__--__  Hello I'm in the Slider valuechange method. firstParameter: " + firstParameter + ", secondParam: " + secondParameter);
    }

    public void forParameterAdjuster (ActionEvent ae) {

       
    }

    public void radioSubmitter (ActionEvent ae) {
        System.out.println("------ >> Radio button submitter fired");
    }


    public String getFirstParameter() {
        return firstParameter;
    }

    public void setFirstParameter(String firstParameter) {
        this.firstParameter = firstParameter;
    }

    public String getSecondParameter() {
        return secondParameter;
    }

    public void setSecondParameter(String secondParameter) {
        this.secondParameter = secondParameter;
    }

    public boolean isCheckOneChecked() {
        return checkOneChecked;
    }

    public void setCheckOneChecked(boolean checkOneChecked) {
        this.checkOneChecked = checkOneChecked;
    }

    public boolean isCheckTwoChecked() {
        return checkTwoChecked;
    }

    public void setCheckTwoChecked(boolean checkTwoChecked) {
        this.checkTwoChecked = checkTwoChecked;
    }

      public void menuItem1Action (ActionEvent ae) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Map requestParameters = ec.getRequestParameterMap();

        firstParameter = (String) requestParameters.get(firstParameterKey);
        secondParameter = (String) requestParameters.get(secondParameterKey);

        System.out.println("Hello I'm in the menu method. firstParameter: " + firstParameter + ", secondParam: " + secondParameter);

    }

      public void menuItem2Action (ActionEvent ae) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Map requestParameters = ec.getRequestParameterMap();

        firstParameter = (String) requestParameters.get(firstParameterKey);
        secondParameter = (String) requestParameters.get(secondParameterKey);

        System.out.println("Hello I'm in the menu method. firstParameter: " + firstParameter + ", secondParam: " + secondParameter);

    }

      public void menuItem3Action (ActionEvent ae) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Map requestParameters = ec.getRequestParameterMap();

        firstParameter = (String) requestParameters.get(firstParameterKey);
        secondParameter = (String) requestParameters.get(secondParameterKey);

        System.out.println("Hello I'm in the menu method. firstParameter: " + firstParameter + ", secondParam: " + secondParameter);

    }

    public String getJunkParameter() {
        return junkParameter;
    }

    public void setJunkParameter(String junkParameter) {
        this.junkParameter = junkParameter;
    }

    public boolean isShowError() {
        return showError;
    }

    public void setShowError(boolean showError) {
        this.showError = showError;
    }

    public int getSliderValue() {
        return sliderValue;
    }

    public void setSliderValue(int sliderValue) {
        this.sliderValue = sliderValue;
    }

    public int getIntParameter() {
        return intParam;
    }

    public void setIntParameter(int intProperty) {
        this.intParam = intProperty;
    }

    public boolean isBoolParam() {
        return boolParam;
    }

    public void setBoolParam(boolean boolParam) {
        this.boolParam = boolParam;
    }

    public Object getObjectParam() {
        return objectParam;
    }

    public void setObjectParam(Object objectParam) {
        this.objectParam = objectParam;
    }

    public String getForParam() {
        return forParam;
    }

    public void setForParam(String forParam) {
        this.forParam = forParam;
    }

    public String getForParameter() {
        return forParameter;
    }

    public void setForParameter(String forParameter) {
        this.forParameter = forParameter;
    }
}