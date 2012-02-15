package org.icemobile.samples.mobileshowcase.view.examples.input.menubutton;

import org.icemobile.samples.mobileshowcase.util.FacesUtils;

import javax.faces.event.ActionEvent;

/**
 * Simple Delete Command that grabs an instance of Bean and mutates it.
 */
public class DeleteCommand implements Command{

    public static final String DELETE_COMMAND_NAME = "Delete Command";

    public void execute(ActionEvent event) {
        MenuButtonBean menuButtonBean = (MenuButtonBean)
                FacesUtils.getManagedBean(MenuButtonBean.BEAN_NAME);
        menuButtonBean.setExecutedCommand(DELETE_COMMAND_NAME);
    }
}
