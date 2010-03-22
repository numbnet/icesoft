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
                baseLoginController.setUserName(loginFormData.getUserName());
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

    public PushRequestManager getPushRequestManager() {
        return pushRequestManager;
    }

    public void setPushRequestManager(PushRequestManager pushRequestManager) {
        this.pushRequestManager = pushRequestManager;
    }

    public LoginFormData getLoginFormData() {
        return loginFormData;
    }

    public void setLoginFormData(LoginFormData loginFormData) {
        this.loginFormData = loginFormData;
    }

    public BaseLoginController getBaseLoginController() {
        return baseLoginController;
    }

    public void setBaseLoginController(BaseLoginController baseLoginController) {
        this.baseLoginController = baseLoginController;
    }
}