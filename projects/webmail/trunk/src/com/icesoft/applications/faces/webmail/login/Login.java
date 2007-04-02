/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.login;

/**
 * <p>The <code>Login</code> model's purpose is to mananage the userName and
 * password information when loging into web mail.</p>
 *
 * @since 1.0
 */
public class Login {

    // user name to login to applications
    protected String userName;
    // password to login to applications
    protected String userPassword;

    /**
     * Gets the user name of the login.
     *
     * @return user name of login.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets the user name of the login.
     *
     * @param userName user name.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the login password.
     *
     * @return login password.
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Sets the user password.
     *
     * @param userPassword user password.
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

}
