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
package com.icesoft.faces.presenter.validator;

import com.icesoft.faces.presenter.presentation.PresentationManager;
import com.icesoft.faces.presenter.presentation.PresentationManagerBean;
import com.icesoft.faces.presenter.util.ValidationMessages;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class PasswordValidator implements Validator {
    public void validate(FacesContext context, UIComponent component,
                         Object value) {
        String inputPassword = value.toString().trim();
        String componentID = component.getId();

        // View password is invalid if one has not been created.
        if (componentID.startsWith("view")) {
            String inputPresentationName =
                    ((UIParameter) component.getChildren().get(0)).getValue()
                            .toString();
            if ((inputPresentationName != null) && (!inputPresentationName.equals("")) &&
                    (!inputPresentationName.equals(PresentationManagerBean.DEFAULT_PRESENTATION))) {
                if (!PresentationManager.getInstance()
                        .passwordExists(inputPassword) || !PresentationManager
                        .getInstance().isPasswordAndPresentationMatch(
                        inputPassword, inputPresentationName)) {
                    FacesMessage message = ValidationMessages.getMessage(
                            ValidationMessages.MESSAGE_RESOURCES,
                            "invalidViewPassword", null);
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(message);
                }
            }
        }
        // Password set by moderator is invalid if it's less than 6 characters
        else if (componentID.startsWith("moderator")) {
            if (inputPassword.length() < 6) {
                FacesMessage message = ValidationMessages.getMessage(
                        ValidationMessages.MESSAGE_RESOURCES, "invalidPassword",
                        null);
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(message);
            }
        }
    }
}
