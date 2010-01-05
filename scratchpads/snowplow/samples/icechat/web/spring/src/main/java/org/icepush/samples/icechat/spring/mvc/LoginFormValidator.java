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
            if (!isValidString(form.getNickName())) {
                errors.rejectValue("nickName", "error.no-nickname", null, "Value required.");
            }
            if (!isValidString(form.getPassword())) {
                errors.rejectValue("password", "error.no-password", null, "Value required.");
            }
        }
    }

    protected boolean isValidString(String test) {
        return (test != null) && (test.trim().length() > 0);        
    }
}