/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
package com.icesoft.faces.presenter.participant;

import com.icesoft.faces.presenter.presentation.AutoPresentation;
import com.icesoft.faces.async.render.OnDemandRenderer;
import com.icesoft.faces.async.render.DelayRenderer;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.ext.HtmlForm;

import javax.faces.event.ValueChangeEvent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class used to handle the login portion of a user's session This is done to
 * separate the main webmc functionality from the entire login process (which
 * uses a lot of temp variables and UI components to get the job done)
 */
public class LoginBean {
    private static Log log = LogFactory.getLog(LoginBean.class);

    private Participant parent;
    private String presentationName = "";
    private String presentationPassword;
    private String presentationMaxString = "-1";
    private boolean noSlotsLeft = false;
    private HtmlInputText firstNameField = null;
    private OnDemandRenderer loginPageRenderer;

    public LoginBean(Participant parent) {
        this.parent = parent;
    }

    /**
     * Method to get the parent owner of this bean
     *
     * @return parent participant
     */
    public Participant getParent() {
        return parent;
    }

    /**
     * Method to set the parent owner of this bean
     *
     * @param parent new owner
     */
    public void setParent(Participant parent) {
        this.parent = parent;
    }

    /**
     * Method to get the desired presentation name
     *
     * @return presentationName
     */
    public String getPresentationName() {
        return presentationName;
    }

    /**
     * Method to set the presentation name
     *
     * @param presentationName new
     */
    public void setPresentationName(String presentationName) {
        this.presentationName = presentationName;
    }

    /**
     * Method to get the desired presentation password
     *
     * @return presentationPassword
     */
    public String getPresentationPassword() {
        return presentationPassword;
    }

    /**
     * Method to get the default presentation names as a formatted String
     *
     * @return formatted defaultPresentationNames
     */
    public String getDefaultPresentationNames() {
        return AutoPresentation.getDefaultPresentationNamesList();
    }

    /**
     * Method to determine if default presentations are present
     * This is used for rendering a message on the login page
     *
     * @return true if default presentations are found
     */
    public boolean getHasDefaultPresentations() {
        return AutoPresentation.getDefaultPresentationNames() != null &&
               AutoPresentation.getDefaultPresentationNames().length > 0;
    }

    /**
     * Method to get the default password from AutoPresentation
     *
     * @return AutoPresentation.DEFAULT_PASSWORD
     */
    public String getDefaultPassword() {
        return AutoPresentation.DEFAULT_PASSWORD;
    }

    /**
     * Method to set the presentation password
     *
     * @param presentationPassword new
     */
    public void setPresentationPassword(String presentationPassword) {
        this.presentationPassword = presentationPassword;
    }

    /**
     * Method to get the maximum participants value from the front end page
     * dropdown list
     *
     * @return presentationMaxString
     */
    public String getPresentationMaxString() {
        return presentationMaxString;
    }

    /**
     * Method to set the maximum participants value
     *
     * @param presentationMaxString new
     */
    public void setPresentationMaxString(String presentationMaxString) {
        this.presentationMaxString = presentationMaxString;
    }

    /**
     * Method to determine if any room is left in an existing presentation
     *
     * @return noSlotsLeft
     */
    public boolean isNoSlotsLeft() {
        return noSlotsLeft;
    }

    /**
     * Convience method to set that slots are not available
     */
    public void setSlotsNone() {
        noSlotsLeft = true;
    }

    /**
     * Convience method to set that slots are still available
     */
    public void setSlotsAvailable() {
        noSlotsLeft = false;
    }

    /**
     * Method to get the first name field which is bound to a front end page
     * component, and is used for requesting focus
     *
     * @return firstNameField
     */
    public HtmlInputText getFirstNameField() {
        return firstNameField;
    }

    /**
     * Method to set the first name field
     *
     * @param firstNameField new
     */
    public void setFirstNameField(HtmlInputText firstNameField) {
        this.firstNameField = firstNameField;
    }

    /**
     * Method to get the ondemand login page renderer
     *
     * @return loginPageRenderer
     */
    public OnDemandRenderer getLoginPageRenderer() {
        return loginPageRenderer;
    }

    /**
     * Method to set the ondemand login page renderer
     *
     * @param loginPageRenderer new
     */
    public void setLoginPageRenderer(OnDemandRenderer loginPageRenderer) {
        this.loginPageRenderer = loginPageRenderer;
    }

    /**
     * Convience method to add the parent participant to the login renderer
     */
    public void addRenderable() {
        if (loginPageRenderer != null) {
            loginPageRenderer.add(parent);
        }
    }

    /**
     * Convience method to remove the parent participant from the login
     * renderer
     */
    public void removeRenderable() {
        if (loginPageRenderer != null) {
            loginPageRenderer.remove(parent);
        }
    }

    /**
     * Method called when a presentation is selected in the front end page level
     * dropdown list
     *
     * @param event of the selection
     */
    public void selectPresentation(ValueChangeEvent event) {
        if (parent.isModerator()) {
            presentationName = "";
            return;
        }
        noSlotsLeft = false;
    }

    /**
     * Method called when the "no room left" notification popup is closed
     *
     * @return "closeNoSlotsDialog" string for faces-config navigation
     */
    public String closeNoSlotsDialog() {
        noSlotsLeft = false;

        return "closeNoSlotsDialog";
    }

    /**
     * Method called when the "Viewer" tab is selected on the front end pages
     */
    public void toggleRoleTypeViewer() {
        parent.setRole(ParticipantInfo.ROLE_VIEWER);

        presentationPassword = "";

        if (firstNameField != null) {
            firstNameField.requestFocus();
        }
    }

    /**
     * Method called when the "Moderator" tab is selected on the front end
     * pages
     */
    public void toggleRoleTypeModerator() {
        parent.setRole(ParticipantInfo.ROLE_MODERATOR);

        presentationName = "";
        presentationPassword = "";

        if (firstNameField != null) {
            firstNameField.requestFocus();
        }
    }

    /**
     * Method to reset the login fields
     */
    public void clearFields() {
        presentationName = "";
        presentationPassword = null;
        presentationMaxString = "-1";
    }

    /**
     * Method called when the user wishes to reset the fields on the login page
     * This will clear the UIComponent values, as well as variables
     *
     * @param ae of the reset
     */
    public void resetButton(ActionEvent ae) {
        UIComponent base = ae.getComponent();
        UIComponent parentForm = base.getParent();

        while (!(parentForm instanceof HtmlForm)) {
            parentForm = parentForm.getParent();
        }

        // Clear the various fields
        clearSingleField(parentForm, "firstName");
        clearSingleField(parentForm, "lastName");
        clearSingleField(parentForm, "email");
        clearSingleField(parentForm, "viewPassword");
        clearSingleField(parentForm, "newPresentationName");
        clearSingleField(parentForm, "moderatorPassword");

        // Reset available variables
        clearFields();
        if (parent != null) {
            parent.clearFields();
        }

        // Request default focus
        if (firstNameField != null) {
            firstNameField.requestFocus();
        }
    }

    /**
     * Convience method to reset the value of a component
     *
     * @param parent of the component to reset
     * @param id of the component to reset
     */
    private void clearSingleField(UIComponent parent, String id) {
        try {
            UIInput toClear = (UIInput)parent.findComponent(id);
            toClear.setSubmittedValue("");
        }catch (Exception ignored) {
            /* Intentionally ignored - if this fails the one value won't be cleared */
        }
    }

    /**
     * Convience method to start a safety net rendering cycle. This is done on
     * the transition from logging in to actually being in a presentation.
     * Sometimes the loginPageRenderer has queued render requests, which are
     * fired when the user is on the index page. But when the old request is
     * fired, the login page will briefly be shown. Without this method, there is
     * a high chance the user will be stuck in a partial logged in state, and
     * basically have to rejoin the presentation.
     * The workaround is to create a 1, 3, and 5 second DelayRenderer, so that
     * the user is safely transitioned to the index page
     *
     * @param manager to get the DelayRenderer from
     * @param name of the renderer
     */
    public void startTransitionRender(RenderManager manager, String name) {
        // Get the renderer and add the Renderable participant object
        DelayRenderer delayer = manager.getDelayRenderer(name);
        delayer.add(parent);

        // Create and start the 1, 3, and 5 second renderers
        delayer.setDelay(1000);
        delayer.requestRender();

        delayer.setDelay(3000);
        delayer.requestRender();

        delayer.setDelay(5000);
        delayer.requestRender();
    }
}