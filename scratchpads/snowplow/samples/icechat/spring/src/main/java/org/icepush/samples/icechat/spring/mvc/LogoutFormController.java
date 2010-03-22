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
            if (baseLoginController.getCurrentUser() != null) {
                baseLoginController.setCurrentUser(null);
            }

            // Request a logout
            baseLoginController.logout();

            // Destroy our session
            request.getSession(false).invalidate();
        }

        return new ModelAndView(new RedirectView("login.htm"));
    }

    public BaseLoginController getBaseLoginController() { return baseLoginController; }

    public void setBaseLoginController(BaseLoginController baseLoginController) { this.baseLoginController = baseLoginController; }
}