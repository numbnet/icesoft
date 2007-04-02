/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.login;

import javax.servlet.http.HttpSession;

/**
 * <p>The UserBean is the User objects view which interacts with the login.jspx
 * JSF view. This class also stores its own persistence state thus insuring that
 * only one User can be associated with one session.</p>
 * <p/>
 * <p>The notion of a user being "inUse" comes from the fact that a user
 * can log on to the system with a limited number of anonymous users.  We
 * only want to assign a user that has already been assigned to someone else. </p>
 *
 * @since 1.0
 */
public class UserBean extends User {

    // seesion associated with user
    private HttpSession httpSession;
    // flag indicated if user is in use
    private boolean inUse;

    /**
     * Constructs a new UserBean with the specified username and password.
     *
     * @param userName
     * @param password
     */
    public UserBean(String userName, String password) {
        super(userName, password);
    }

    /**
     * Gest the HttpSession associated with object if any.
     *
     * @return httpSession object if assinged, null otherwise.
     */
    public HttpSession getHttpSession() {
        return httpSession;
    }

    /**
     * Set the httpSession with the specified value.
     *
     * @param httpSession
     */
    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    /**
     * Indicates that the current user is being used in a session.
     *
     * @return true if in use, false otherwise.
     */
    public boolean isInUse() {
        return inUse;
    }

    /**
     * Sets the inUse state.
     *
     * @param inUse true if in use, false otherwise.
     */
    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
}
