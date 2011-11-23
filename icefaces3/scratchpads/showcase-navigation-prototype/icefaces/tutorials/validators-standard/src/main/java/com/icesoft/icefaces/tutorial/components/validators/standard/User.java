/*
 * User.java
 *
 * Created on March 27, 2007, 9:31 AM
 *
 * Backing Bean for Validatioin tutorial
 */

package com.icesoft.icefaces.tutorial.components.validators.standard;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dnorthcott
 */
public class User {

    private long age =0;
    private long displayAge = 0;
    private String name;
    private String displayName;
    private String phoneNumber;
    private String email;
    private String displayEmail;

    private String displayPhoneNumber;


    /** Creates a new instance of User */
    public User() {
    }

    public void setAge(long age){
        this.age = age;
    }

    public long getAge(){
        return age;
    }

    public long getDisplayAge(){
        return displayAge;
    }
    public void setDisplayAge(long ds){
        this.displayAge = ds;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setDisplayName(String ds){
        this.displayName = ds;
    }
    public String getDisplayName(){
        return displayName;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setDisplayPhoneNumber(String dpn){
        this.displayPhoneNumber = dpn;
    }
    public String getDisplayPhoneNumber(){
        return displayPhoneNumber;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }
    public void setDisplayEmail(String de){
        this.displayEmail = de;
    }
    public String getDisplayEmail(){
        return displayEmail;
    }

    public void submitAction(ActionEvent event){
        setDisplayAge(age);
    }

    /** register method to validate the registered name
     *@param ActionEvent event
     */
    public void register(ActionEvent event){

        setDisplayName(name);
        FacesContext context = FacesContext.getCurrentInstance();
        if(StringUtils.isEmpty(getName())){
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary("Name Field is Blank");
            message.setDetail("Name Field is Blank..");
            context.addMessage("tutorialForm:name",message);

        }

    }
    /** method to verify the phone number
     *@param ActionEvent event
     */
    public void submitActionPhone(ActionEvent event){
        setDisplayPhoneNumber(phoneNumber);
    }

    /** method to validate the email address
     *@param ActionEvent event
     */
    public void submitActionEmail(ActionEvent event){
        setDisplayEmail(email);
    }

    /** method to validate the email field
     *@param FacesContext context, UIComponent validate, Object value
     */
    public void validateEmail(FacesContext context, UIComponent validate, Object value){
        String email = (String)value;

        if(email.indexOf('@')==-1){
            ((UIInput)validate).setValid(false);
            FacesMessage msg = new FacesMessage("Invalid Email");
            context.addMessage(validate.getClientId(context), msg);
        }
    }

}
