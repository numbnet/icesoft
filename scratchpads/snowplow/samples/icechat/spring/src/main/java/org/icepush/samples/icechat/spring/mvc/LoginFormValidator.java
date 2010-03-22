/*
 * Version: MPL 1.1
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
 * The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icepush.samples.icechat.spring.mvc;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoginFormValidator implements Validator {
    
    protected final Log logger = LogFactory.getLog(getClass());

    public boolean supports(Class clazz) {
        return LoginFormData.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
        LoginFormData form = (LoginFormData)obj;

        if (form == null) {
            errors.rejectValue("userName", "error.not-specified", null, "Values required.");
        }
        else {
            if (!isValidString(form.getUserName())) {
                errors.rejectValue("userName", "error.no-username", null, "Value required.");
            }
        }
    }

    protected boolean isValidString(String test) {
        return (test != null) && (test.trim().length() > 0);        
    }
}