package org.icemobile.samples.mobileshowcase.view.examples.input.menubutton;

import javax.faces.event.ActionEvent;

/**
 * Command pattern interface.
 */
public interface Command {
    public void execute(ActionEvent event);
}
