package org.icemobile.samples.mobileshowcase.view.examples.input.menubutton;

import org.icemobile.samples.mobileshowcase.util.FacesUtils;

import javax.faces.event.ActionEvent;

/**
 * Simple Updates Command that grabs an instance of Bean and mutates it.
 */
public class UpdateCommand implements Command{

    public static final String UPDATE_COMMAND_NAME = "Update Command";

    public void execute(ActionEvent event) {
        MenuButtonBean menuButtonBean = (MenuButtonBean)
                FacesUtils.getManagedBean(MenuButtonBean.BEAN_NAME);
        menuButtonBean.setExecutedCommand(UPDATE_COMMAND_NAME);
    }
}
