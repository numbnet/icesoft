package org.icemobile.samples.springbasic;

import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * This is a sample backing bean for the MVC supported state
 * The properties should be the same
 */
@SessionAttributes("inputTextBean")
public class InputTextBean {

    // One property for each switch on the page
    private String textOne;

    public String getTextOne() {
        return textOne;
    }

    public void setTextOne(String textOne) {
        this.textOne = textOne;
    }
}
