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
package com.icesoft.applications.faces.webmail.login;

import com.icesoft.applications.faces.webmail.WebmailMediator;

/**
 * <p>The <code>RegisteredLoginBean</code> class is responsible for representing
 * users which are assumed to be registed in the webmail database.  </p>
 *
 * @since 1.0
 */
public class RegisteredLoginBean extends LoginBean {
//
//    private static Log log = LogFactory.getLog(RegisteredLoginBean.class);

    public RegisteredLoginBean() {
        loginType = LoginBeanFactory.REGISTERED_MODE;
    }

    /**
     * Verifies this object username and password with the users table in the
     * webmail database.
     *
     * @return LOGIN_FAILURE if either the username or password are incorrect.
     *         Otherwise LOGIN_SUCCEES is returned if the username and password
     *         are succesfully verified.
     */
    public String login() {
        String validated = verifyUserBean(new User(userName, userPassword));
        if (LOGIN_SUCCESS.equals(validated)){
            WebmailMediator.addMessage(
                    "loginForm",
                    "loadingMessage",
                    "webmail.login.status", null);
            return LOGIN_SUCCESS;
        }
        else{
            return LOGIN_FAILURE;
        }
    }
}
