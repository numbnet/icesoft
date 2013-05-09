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
        panelConfirmation.toggleVisibility(actionEvent);
    }
}
