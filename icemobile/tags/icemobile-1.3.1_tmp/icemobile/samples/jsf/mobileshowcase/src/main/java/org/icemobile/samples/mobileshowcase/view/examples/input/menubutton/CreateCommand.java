/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
package org.icemobile.samples.mobileshowcase.view.examples.input.menubutton;

import org.icemobile.samples.mobileshowcase.util.FacesUtils;

import javax.faces.event.ActionEvent;

/**
 * Simple Create Command that grabs an instance of Bean and mutates it.
 */
public class CreateCommand implements Command{
    
    public static final String CREATE_COMMAND_NAME = "Create Command";
    
    public void execute(ActionEvent event) {
        MenuButtonBean menuButtonBean = (MenuButtonBean)
                FacesUtils.getManagedBean(MenuButtonBean.BEAN_NAME);
        menuButtonBean.setExecutedCommand(CREATE_COMMAND_NAME);
    }
}
