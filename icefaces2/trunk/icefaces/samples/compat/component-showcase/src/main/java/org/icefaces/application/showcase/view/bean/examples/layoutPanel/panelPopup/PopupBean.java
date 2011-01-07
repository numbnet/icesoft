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

package org.icefaces.application.showcase.view.bean.examples.layoutPanel.panelPopup;

import org.icefaces.application.showcase.util.MessageBundleLoader;


import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

/**
 * <p>The PopupBean class is the backing bean that manages the Popup Panel
 * state.</p>
 * <p>This includes the modal and draggable user configurable message, as well
 * as the rendered and visibility state.</p>
 */
@ManagedBean(name = "popup")
@ViewScoped
public class PopupBean implements Serializable {
    // user entered messages for both dialogs
    private String draggableMessage = MessageBundleLoader.getMessage("page.panelPopup.defaultDraggableMessage");
    private String modalMessage = MessageBundleLoader.getMessage("page.panelPopup.defaultModalMessage");
    // render flags for both dialogs
    private boolean draggableRendered = false;
    private boolean modalRendered = false;
    // if we should use the auto centre attribute on the draggable dialog
    private boolean autoCentre = false;

    
    
    public String getDraggableMessage() {
        return draggableMessage;
    }

    public void setDraggableMessage(String draggableMessage) {
        this.draggableMessage = draggableMessage;
    }

    public String getModalMessage() {
        return modalMessage;
    }

    public void setModalMessage(String modalMessage) {
        this.modalMessage = modalMessage;
    }

    public boolean isDraggableRendered() {
        return draggableRendered;
    }

    public void setDraggableRendered(boolean draggableRendered) {
        this.draggableRendered = draggableRendered;
    }

    public boolean getModalRendered() {
        return modalRendered;
    }

    public void setModalRendered(boolean modalRendered) {
        this.modalRendered = modalRendered;
    }
    
    public boolean getAutoCentre() {
        return autoCentre;
    }

    public void setAutoCentre(boolean autoCentre) {
        this.autoCentre = autoCentre;
    }

    public String getDetermineDraggableButtonText() {
        return MessageBundleLoader.getMessage("page.panelPopup.show."
                + draggableRendered);
    }

    public String getDetermineModalButtonText() {
        return MessageBundleLoader.getMessage("page.panelPopup.show."
                + modalRendered);
    }

    public void toggleDraggable(ActionEvent event) {
        draggableRendered = !draggableRendered;
    }

    public void toggleTest(){
    	this.modalRendered= !this.modalRendered;
    }
    
    public void toggleModal(ActionEvent event) {
        modalRendered = !modalRendered;
    }
}
