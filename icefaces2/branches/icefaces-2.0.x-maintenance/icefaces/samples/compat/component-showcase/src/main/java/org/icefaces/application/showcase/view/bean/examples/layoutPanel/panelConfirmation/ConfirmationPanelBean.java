/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.application.showcase.view.bean.examples.layoutPanel.panelConfirmation;

import org.icefaces.application.showcase.util.MessageBundleLoader;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ManagedBean(name = "confirmation")
@ViewScoped
public class ConfirmationPanelBean implements Serializable {
    private String dataIn = MessageBundleLoader.getMessage("page.panelConfirmation.dataIn");
    private String dataOut = MessageBundleLoader.getMessage("page.panelConfirmation.dataOut");
    private boolean withConfirmation = true;

    public void save(ActionEvent event) {
        dataOut = dataIn;
    }

    public void delete(ActionEvent event) {
        dataOut = null;
    }

    public String getDataOut() {
        return dataOut;
    }

    public String getDataIn() {
        return dataIn;
    }

    public void setDataIn(String dataIn) {
        this.dataIn = dataIn;
    }

    public boolean isWithConfirmation() {
        return withConfirmation;
    }

    public void setWithConfirmation(boolean withConfirmation) {
        this.withConfirmation = withConfirmation;
    }
}