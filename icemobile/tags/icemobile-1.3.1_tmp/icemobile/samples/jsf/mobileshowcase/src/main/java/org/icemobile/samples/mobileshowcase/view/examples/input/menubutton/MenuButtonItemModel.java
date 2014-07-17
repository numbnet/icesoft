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

import java.io.Serializable;

/**
 * Model to back the menuButton child element menuButtonItem.
 */
public class MenuButtonItemModel implements Serializable {

    private String value;
    private String label;
    private Command commandAction;

    public MenuButtonItemModel(String value, String label, Command commandAction) {
        this.value = value;
        this.label = label;
        this.commandAction = commandAction;
    }

    public MenuButtonItemModel(String label, Command commandAction) {
        this.label = label;
        this.commandAction = commandAction;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public Command getCommandAction() {
        return commandAction;
    }
}
