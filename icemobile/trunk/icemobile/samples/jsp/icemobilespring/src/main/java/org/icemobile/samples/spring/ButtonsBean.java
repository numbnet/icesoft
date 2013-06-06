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

import java.lang.String;

/**
 * This is a sample backing bean for the MVC supported state
 * The properties should be the same
 */
@SessionAttributes("buttonsBean")
public class ButtonsBean {
    private String selectedHorizontal = null;
    private String selectedVertical = null;
    private String selectedType = null;

    @ModelAttribute("buttonsBean")
    public ButtonsBean createBean() {
        return new ButtonsBean();
    }

    public String getSelectedHorizontal() {
        return selectedHorizontal;
    }

    public void setSelectedHorizontal(String selectedHorizontal) {
        this.selectedHorizontal = selectedHorizontal;
    }

    public String getSelectedVertical() {
        return selectedVertical;
    }

    public void setSelectedVertical(String selectedVertical) {
        this.selectedVertical = selectedVertical;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

}
