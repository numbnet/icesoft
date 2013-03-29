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
