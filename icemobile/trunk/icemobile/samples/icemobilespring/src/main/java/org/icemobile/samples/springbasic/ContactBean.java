package org.icemobile.samples.springbasic;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This is a sample backing bean for the MVC supported state
 * The properties should be the same
 */
@SessionAttributes("ContactBean")
public class ContactBean {

    private String contactOne;

    @ModelAttribute("contactBean")
    public org.icemobile.samples.springbasic.ContactBean createBean() {
        return new org.icemobile.samples.springbasic.ContactBean();
    }

    public String getContactOne() {
        return contactOne;
    }

    public void setContactOne(String contactOne) {
        this.contactOne = contactOne;
    }
}
