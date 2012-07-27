package org.icemobile.samples.springbasic;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * This is a sample backing bean for the MVC supported state
 * The properties should be the same
 */
@SessionAttributes("buttonsBean")
public class ButtonsBean {

    @ModelAttribute("buttonsBean")
    public ButtonsBean createBean() {
        return new ButtonsBean();
    }

}
