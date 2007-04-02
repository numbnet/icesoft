/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
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
