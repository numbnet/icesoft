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
 * <p>The <code>AnonymousLoginBean</code> class is responsible for allowing
 * users of webmail-demo to logon with demo mail accounts specified in the
 * <code>UserAccountPool</code> class.</p>
 * <p/>
 * <p>This class should not be distributed with the non-demo version of webmail.
 * </p>
 *
 * @since 1.0
 *
 */
public class AnonymousLoginBean extends LoginBean {

//    private static Log log = LogFactory.getLog(AnonymousLoginBean.class);

    public AnonymousLoginBean() {
        loginType = LoginBeanFactory.ANONYMOUS_MODE;
    }

    /**
     * Gets an anonymouse user account from the userAccountPool.  If there are
     * no available accounts the login process will fail.
     *
     * @return LOGIN_FAILURE if there are no anonymous acocunts. or LOGIN_SUCCEES
     *         if an anymous account could be retreived.
     */
    public String login() {

        UserBean userBean = UserAccountPool.getAnonUserAccount();

        // if the account is null, the user can not log on anonymously
        if (userBean == null) {
            WebmailMediator.addMessage(
                    "loginForm",
                    "errorMessage",
                    "webmail.login.noMoreAnonAccounts", null);
            return LOGIN_FAILURE;
        }

        String validated = verifyUserBean(userBean);
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

    /**
     * Releases any locks in userAccountPool that may have been set during
     * login.
     */
    public void logout() {
        if (verifiedUser != null ) {
            UserAccountPool.releaseAnonUserAccount(verifiedUser);
        }
        // call super call to logout. 
        super.logout();
    }
}
