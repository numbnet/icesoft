package org.icemobile.samples.springbasic;

import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * This is a sample backing bean for the MVC supported state
 * By convention, he name of the properties should be the same as the ids of
 * the jsp tags.
 */
@SessionAttributes("accordionBean")
public class AccordionBean {

    private String accordionOne = "tab2";

    public String getAccordionOne() {
        return accordionOne;
    }

    public void setAccordionOne(String accordionOne) {
        this.accordionOne = accordionOne;
    }
}
