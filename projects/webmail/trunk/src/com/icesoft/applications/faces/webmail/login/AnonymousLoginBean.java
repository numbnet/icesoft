/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
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
