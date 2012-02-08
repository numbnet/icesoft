package org.icemobile.samples.mobileshowcase.view.examples.layout.panelconfirmation;

import org.icemobile.samples.mobileshowcase.util.FacesUtils;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

/**
 *
 */
public class AcceptAction implements ActionListener {
    public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
        PanelConfirmation panelConfirmation = (PanelConfirmation)
                FacesUtils.getManagedBean(PanelConfirmation.BEAN_NAME);
        panelConfirmation.toggleVisibility();
    }
}
