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

import org.icefaces.application.PushRenderer;
import com.icesoft.faces.presenter.presentation.PresentationManager;
import com.icesoft.faces.presenter.presentation.PresentationManagerBean;
import com.icesoft.faces.component.ext.HtmlInputSecret;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.ext.HtmlForm;

import javax.faces.event.ValueChangeEvent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class used to handle the login portion of a user's session.  This is done to
 * separate the main webmc functionality from the entire login process.
 */
public class LoginBean {
    private static Log log = LogFactory.getLog(LoginBean.class);

    private Participant parent;
    private String presentationName = "";
    private String presentationPassword;
    private HtmlInputText firstNameField = null;
    private boolean invalidPassword = false;
    private boolean invalidPresentation = false;
    
    public LoginBean(Participant parent) {
        this.parent = parent;
    }

    public Participant getParent() {
        return parent;
    }

    public void setParent(Participant parent) {
        this.parent = parent;
    }

    public String getPresentationName() {
        return presentationName;
    }

    public void setPresentationName(String presentationName) {
        this.presentationName = presentationName;
    }

    public String getPresentationPassword() {
        return presentationPassword;
    }

    public void setPresentationPassword(String presentationPassword) {
        this.presentationPassword = presentationPassword;
    }

    public HtmlInputText getFirstNameField() {
        return firstNameField;
    }

    public void setFirstNameField(HtmlInputText firstNameField) {
        this.firstNameField = firstNameField;
    }

	public boolean isInvalidPassword() {
		return invalidPassword;
	}

	public void setInvalidPassword(boolean invalidPassword) {
		this.invalidPassword = invalidPassword;
	}

	public boolean isInvalidPresentation() {
		return invalidPresentation;
	}

	public void setInvalidPresentation(boolean invalidPresentation) {
		this.invalidPresentation = invalidPresentation;
	}

    /**
     * Method called when the "Viewer" tab is selected on the front end pages
     */
    public void toggleRoleTypeViewer(ActionEvent ae) {
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
    public void toggleRoleTypeModerator(ActionEvent ae) {
        parent.setRole(ParticipantInfo.ROLE_MODERATOR);

        presentationName = "";
        presentationPassword = "";

        if (firstNameField != null) {
            firstNameField.requestFocus();
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
        invalidPassword = false;
        invalidPresentation = false;
        clearSingleField(parentForm, "newPresentationName");
        clearSingleField(parentForm, "moderatorPassword");
        
        // Clear the select boxes
        clearSingleField(parentForm, "existingPresentationName");

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
     * Convenience method to reset the value of a UIInput component
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
     * Method to reset the login fields
     */
    public void clearFields() {
        presentationName = "";
        presentationPassword = null;
    }
	
    public void validatePassword(ActionEvent ae){
    	if (ae.getSource() instanceof HtmlInputSecret) {
    	    HtmlInputSecret component = (HtmlInputSecret)ae.getSource();
    	    validatePassword((String)component.getValue());
    	}
    }
    
    public boolean validatePassword(String inputPassword){
    	setInvalidPassword(false);
        if ((presentationName != null) && (!presentationName.equals("")) &&
                (!presentationName.equals(PresentationManagerBean.DEFAULT_PRESENTATION))) {
        	// View password is invalid if it does not match the selected presentation.
        	if (!PresentationManager
                    .getInstance().isPasswordAndPresentationMatch(
                    inputPassword, getPresentationName())) {
        		setInvalidPassword(true);
                return false;
            }
        }
        return true;
    }
    
    public String getSelectedPresentationPassword(){
        if(PresentationManager.getInstance().getPresentation(presentationName)==null){
        	return null;
        }else{
        	 return PresentationManager.getInstance().getPresentation(presentationName).getPassword();
        }
    }
    
    public boolean validatePresentation(){
    	setInvalidPresentation(false);
        if ((presentationName == null) || (presentationName.equals("")) ||
                (presentationName.equals(PresentationManagerBean.DEFAULT_PRESENTATION))) {
        		setInvalidPresentation(true);
                return false;
        }
        return true;
    }

}
