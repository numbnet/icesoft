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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icepush.samples.icechat.controller.ILoginController;
import org.icepush.samples.icechat.spring.impl.BasePushRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@Scope("session")
public class LoginFormController{

    protected final Log logger = LogFactory.getLog(getClass());

    private PushRequestManager pushRequestManager;
    private LoginFormData loginFormData;
    private ILoginController loginController;

    @Autowired
    public LoginFormController(PushRequestManager pushRequestManager, LoginFormData loginFormData,
    		ILoginController loginController) {
        this.pushRequestManager = pushRequestManager;
        this.loginFormData = loginFormData;
        this.loginController = loginController;
    }

    public ModelAndView onSubmit(@ModelAttribute("loginFormData") LoginFormData loginFormData, BindingResult result, SessionStatus status,
    		HttpServletRequest request, HttpServletResponse response)
                                 throws Exception {
    	
    	new LoginFormValidator().validate(loginFormData, result);
    	
        if (!result.hasErrors()) {
            try{
                if (loginController.getPushRequestContext() == null) {
                    pushRequestManager.setPushRequestContext(new BasePushRequestContext(request, response));

                    loginController.setPushRequestContext(pushRequestManager.getPushRequestContext());
                }
                loginController.setUserName(loginFormData.getUserName());
                loginController.login();
                status.setComplete();
                return new ModelAndView(new RedirectView("/chat/rooms"));
            }catch (Exception failedLogin) {
                logger.error("Failed to attempt to log a user in", failedLogin);
            }
        }

        return new ModelAndView(new RedirectView("/chat/login"));
    }
    
   public String setupForm(Model model) {
		model.addAttribute("loginFormData", loginFormData);
		return "login";
	}

    
     protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        return loginFormData;
    }

    public PushRequestManager getPushRequestManager() {
        return pushRequestManager;
    }

    public LoginFormData getLoginFormData() {
        return loginFormData;
    }

    public ILoginController geLoginController() {
        return loginController;
    }
}