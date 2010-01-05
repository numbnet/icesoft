package org.icepush.samples.icechat.spring.mvc;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class LogoutFormController implements Controller {

    protected final Log logger = LogFactory.getLog(getClass());

    private LoginFormData loginFormData;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        loginFormData.clear();

        return new ModelAndView(new RedirectView("login.htm"));
    }

    public LoginFormData getLoginFormData() { return loginFormData; }

    public void setLoginFormData(LoginFormData loginFormData) { this.loginFormData = loginFormData; }
}