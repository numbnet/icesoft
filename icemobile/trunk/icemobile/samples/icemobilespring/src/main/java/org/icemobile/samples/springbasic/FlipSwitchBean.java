package org.icemobile.samples.springbasic;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * This is a sample backing bean for the MVC supported state
 * The properties should be the same
 */
@SessionAttributes("flipSwitchBean")
public class FlipSwitchBean {

    // One property for each switch on the page
    private String yesNoFlipSwitch;
    private String onOffFlipSwitch;
    private String trueFalseFlipSwitch;


    @ModelAttribute("flipSwitchBean")
    public FlipSwitchBean createBean() {
        return new FlipSwitchBean();
    }

    public String getYesNoFlipSwitch() {
        return yesNoFlipSwitch;
    }

    public void setYesNoFlipSwitch(String yesNoFlipSwitch) {
        this.yesNoFlipSwitch = yesNoFlipSwitch;
    }


    public String getTrueFalseFlipSwitch() {
        return trueFalseFlipSwitch;
    }

    public void setTrueFalseFlipSwitch(String trueFalseFlipSwitch) {
        this.trueFalseFlipSwitch = trueFalseFlipSwitch;
    }

    public String getOnOffFlipSwitch() {
        return onOffFlipSwitch;
    }

    public void setOnOffFlipSwitch(String onOffFlipSwitch) {
        this.onOffFlipSwitch = onOffFlipSwitch;
    }
}
