package org.icepush.samples.icechat.spring.mvc;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icepush.samples.icechat.spring.impl.BaseLoginController;

import java.io.IOException;

public class LogoutFormController implements Controller {

    protected final Log logger = LogFactory.getLog(getClass());

    private BaseLoginController baseLoginController;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (baseLoginController != null) {
            if (baseLoginController.getCredentialsBean() != null) {
                ((LoginFormData) baseLoginController.getCredentialsBean()).clear();
            }

            baseLoginController.logout();
        }

        return new ModelAndView(new RedirectView("login.htm"));
    }

    public BaseLoginController getBaseLoginController() { return baseLoginController; }

    public void setBaseLoginController(BaseLoginController baseLoginController) { this.baseLoginController = baseLoginController; }
}