package org.icemobile.samples.springbasic;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * This is a sample backing bean for the MVC supported state
 * The properties should be the same
 */
@SessionAttributes("ContactBean")
public class ContactBean {

    private String rawContact;
    private String name;
    private String phone;
    private String email;

    @ModelAttribute("contactBean")
    public org.icemobile.samples.springbasic.ContactBean createBean() {
        return new org.icemobile.samples.springbasic.ContactBean();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRawContact() {
        return rawContact;
    }

    public void setRawContact(String rawContact) {
        this.rawContact = rawContact;
    }
    
    public void reset(){
        this.rawContact = null;
        this.email = null;
        this.phone = null;
        this.name = null;
    }
    
    
}
