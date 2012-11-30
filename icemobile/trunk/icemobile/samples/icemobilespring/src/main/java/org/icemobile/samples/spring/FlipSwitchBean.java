package org.icemobile.samples.spring;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * This is a sample backing bean for the MVC supported state
 * The properties should be the same
 */
@SessionAttributes("flipSwitchBean")
public class FlipSwitchBean {

    // One property for each switch on the page
    private boolean yesNoFlipSwitch;
    private boolean onOffFlipSwitch = true;
    private boolean trueFalseFlipSwitch;


    @ModelAttribute("flipSwitchBean")
    public FlipSwitchBean createBean() {
        return new FlipSwitchBean();
    }

    public boolean getYesNoFlipSwitch() {
        return yesNoFlipSwitch;
    }

    public void setYesNoFlipSwitch(boolean yesNoFlipSwitch) {
        this.yesNoFlipSwitch = yesNoFlipSwitch;
    }


    public boolean getTrueFalseFlipSwitch() {
        return trueFalseFlipSwitch;
    }

    public void setTrueFalseFlipSwitch(boolean trueFalseFlipSwitch) {
        this.trueFalseFlipSwitch = trueFalseFlipSwitch;
    }

    public boolean getOnOffFlipSwitch() {
        return onOffFlipSwitch;
    }

    public void setOnOffFlipSwitch(boolean onOffFlipSwitch) {
        this.onOffFlipSwitch = onOffFlipSwitch;
    }
}
