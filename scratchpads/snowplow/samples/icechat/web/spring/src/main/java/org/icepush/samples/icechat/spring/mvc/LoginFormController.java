package org.icepush.samples.icechat.spring.mvc;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class LoginFormController extends SimpleFormController {

    protected final Log logger = LogFactory.getLog(getClass());

    private LoginFormData loginFormData;

    public ModelAndView onSubmit(Object command) throws ServletException {
        LoginFormData currentForm = (LoginFormData)command;

        loginFormData.setUserName(currentForm.getUserName());
        
        return new ModelAndView(new RedirectView(getSuccessView()));
    }

    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        return loginFormData;
    }

    public LoginFormData getLoginFormData() { return loginFormData; }
    
    public void setLoginFormData(LoginFormData loginFormData) { this.loginFormData = loginFormData; }
}