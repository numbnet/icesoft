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
