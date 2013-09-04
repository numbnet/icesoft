/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

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
