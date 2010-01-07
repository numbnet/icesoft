package org.icepush.samples.icechat.spring.mvc;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icepush.samples.icechat.spring.impl.BaseLoginController;
import org.icepush.samples.icechat.spring.impl.BasePushRequestContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoginFormController extends SimpleFormController {

    protected final Log logger = LogFactory.getLog(getClass());

    private PushRequestManager pushRequestManager;
    private LoginFormData loginFormData;
    private BaseLoginController baseLoginController;

    public LoginFormController() {
        super();
    }

    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
                                 Object command, BindException errors)
                                 throws Exception {
        if ((errors != null) && (errors.getErrorCount() == 0)) {
            try{
                if (baseLoginController.getPushRequestContext() == null) {
                    pushRequestManager.setPushRequestContext(new BasePushRequestContext(request, response));
                    
                    baseLoginController.setPushRequestContext(pushRequestManager.getPushRequestContext());
                }

                baseLoginController.register();
                baseLoginController.login();

                return new ModelAndView(new RedirectView(getSuccessView()));
            }catch (Exception failedLogin) {
                logger.error("Failed to attempt to log a user in", failedLogin);
            }
        }

        return new ModelAndView(new RedirectView("login.htm"));
    }

    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        return loginFormData;
    }

    public PushRequestManager getPushRequestManager() { return pushRequestManager; }
    public LoginFormData getLoginFormData() { return loginFormData; }
    public BaseLoginController getBaseLoginController() { return baseLoginController; }

    public void setPushRequestManager(PushRequestManager pushRequestManager) { this.pushRequestManager = pushRequestManager; }
    public void setLoginFormData(LoginFormData loginFormData) { this.loginFormData = loginFormData; }
    public void setBaseLoginController(BaseLoginController baseLoginController) { this.baseLoginController = baseLoginController; }
}