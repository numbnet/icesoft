/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.login;

import com.icesoft.applications.faces.webmail.util.db.HibernateUtil;
import com.icesoft.applications.faces.webmail.util.encryption.PasswordHash;
import com.icesoft.applications.faces.webmail.WebmailMediator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;


/**
 * <p>The LoginBean is the Login objects view which interacts with the login.jspx
 * JSF view. This class is responsible for dipslaying the appropriate error
 * messages when login errors occure, or there is no more anonymous accounts
 * available.</p>
 * <p/>
 * There are two distinct modes; anonymous login and registered login with a
 * valid username and password.
 *
 * @since 1.0
 */
public class LoginBean extends Login {

    private static Log log = LogFactory.getLog(LoginBean.class);
    /**
     * Faces Navigation String used to inidicate a successful login
     */
    public static final String LOGIN_SUCCESS = "loginSuccess";

    /**
     * Faces Navigation String used to inidicate that a login failure
     */
    public static final String LOGIN_FAILURE = null;

    /**
     * Faces Navigation String used to inidicate a successful logout
     */
    public static final String LOGOUT_SUCCESS = "logoutSuccess";

    // login type, either anonymous or registered.
    protected String loginType;

    protected User verifiedUser;
    protected User clone;
    protected User newUser;
    protected boolean newMode = false;

    /**
     * Called to do dictate the login navigation.
     *
     * @return LOGIN_SUCCESS if the login was succesfull; otherwise, LOGIN_FAILURE.
     */
    public String login() {
        return LOGIN_FAILURE;
    }

    /**
     * Called to do dictate any work that needs to be done on logout.
     */
    public void logout() {
        // mark the session as logged out, set to logged in in the verify user
        // method.
        setSessionLoginParam(false);
    }

    public String getLoginType() {
        return loginType;
    }


    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    /**
     * Gets the verified user object.
     *
     * @return verified user object, null if the verification process failed
     */
    public User getVerifiedUser() {
        return verifiedUser;
    }

    /**
     * Checks users table to see if userBean is valid
     *
     * @return navigation string used by the faces config.
     */
    protected String verifyUserBean(User userBean) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        session.beginTransaction();

        try {
            verifiedUser = (User) session.get(User.class, userBean.getUserName());
        }
        catch (HibernateException e) {
            log.error("Failed execute user lookup - SQL error", e);
        }

        session.getTransaction().commit();

        if (verifiedUser != null) {
            if (verifiedUser.getUserPassword().equals(
                    PasswordHash.encrypt(userBean.getUserPassword()))) {
                clone = new User();
                // create clone of verified user
                User.copy(verifiedUser, clone);

                // mark the session as logged in, set to logged out in the
                // logout method
                setSessionLoginParam(true);
                return LOGIN_SUCCESS;
            } else {
                return LOGIN_FAILURE;
            }
        } else {
            log.info("setting login error message ");
            WebmailMediator.addMessage(
                    "loginForm",
                    "errorMessage",
                    "webmail.login.invalidLogin", null);
            return LOGIN_FAILURE;
        }
    }

    /**
     * Update the user information
     *
     * @param event
     */
    public void updateUser(ActionEvent event) {

        String oldPassword = verifiedUser.getOldPassword();
        String userPassword = verifiedUser.getUserPassword();
        String verifiedPassword = verifiedUser.getVerifiedPassword();
        String newPassword = verifiedUser.getNewPassword();
        String errorMessage = "";

        // check for old password
        if (!oldPassword.equals("")) {

            //check whether it's the same as the password
            if ((encrypt(oldPassword)).equals(userPassword)) {
                // new password must not be empty
                if (!newPassword.equals("")) {
                    // new password and verified password must be matched
                    if (!newPassword.equals(verifiedPassword)) {
                        errorMessage = "webmail.settings.error.password.nomatch";
                    } else {
                        verifiedUser.setUserPassword(encrypt(newPassword));
                    }
                } else {
                    errorMessage = "webmail.settings.error.password.empty";

                }
            } else {
                errorMessage = "webmail.settings.error.password.old.incorrect";

            }
        }
        //Old password is needed to change the password
        else if ((!newPassword.equals("")) || (!verifiedPassword.equals(""))) {
                errorMessage = "webmail.settings.error.password.old.empty";
        }

        //no error
        if (errorMessage.equals("")) {

            verifiedUser.save();
            User.copy(verifiedUser, clone);
        }

        //display error message
        else {

            WebmailMediator.addMessage(
                    "settingsForm",
                    "errorMessage",
                    errorMessage, null );
        }
    }


    /**
     * Utility function to encrypt the plain string
     *
     * @param s string to be encrypted
     * @return string after encryption
     */
    private String encrypt(String s) {
        return PasswordHash.encrypt(s);
    }


    /**
     * Called when user change is cancelled
     *
     * @param event
     */
    public void cancelUser(ActionEvent event) {
        verifiedUser.setOldPassword("");
        verifiedUser.setNewPassword("");
        verifiedUser.setVerifiedPassword("");
        User.copy(clone, verifiedUser);
    }

    /**
     * Indicate whether it's the mode to add a new user
     *
     * @return true if it's the mode to add a new user false otherwise
     */
    public boolean isNewMode() {
        return newMode;
    }


    /**
     * Set whether it's the mode to add a new user
     *
     * @param newMode
     */
    public void setNewMode(boolean newMode) {
        this.newMode = newMode;
    }


    /**
     * Populate a new user with empty string(for Hibernate)
     * and switch to add new user mode
     *
     * @param event
     */
    public void newUser(ActionEvent event) {
        newUser = new User();
        newUser.setFirstName("");
        newUser.setLastName("");
        newUser.setCompany("");
        newUser.setUserPassword("");
        newUser.setNewPassword("");
        newUser.setVerifiedPassword("");
        newMode = true;
    }

    /**
     * Store the new user into the database
     */
    public void addUser(ActionEvent event) {
        String error = "";

        //password cannot be empty
        if (newUser.getNewPassword().equals("")) {
            error = "Password cannot be empty";
        } else {
            //passwords must match
            if (!newUser.getNewPassword().equals(newUser.getVerifiedPassword()))
                error = "New password and retype password don't match";
        }

        if (error.equals("")) {
            //encrypt the password
            newUser.setUserPassword(encrypt(newUser.getNewPassword()));
            //save in the database
            newUser.save();
            //switch to normal mode
            newMode = false;
        } else {

            WebmailMediator.addMessage(
                    "settingsForm",
                    "errorMessage",
                    null, error);
        }
    }

    /**
     * Add new user action is cancelled
     *
     * @param event
     */
    public void cancelAddUser(ActionEvent event) {
        newMode = false;
    }

    /**
     * Indicate whether it's autorized to add a new user
     * @return true if current user's name is specified as adminName and currently
     * not in add new mode false otherwise
     */
    public boolean isAuthorized() {
        String adminName = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("adminName");
        return (verifiedUser.getUserName().equalsIgnoreCase(adminName) && !newMode);
    }

    /**
     * Sets the session attribute "LoggedIn".  This varaiable is used by a JSP
     * jump page to bypass the login screen if a session has already been started
     * and the user is "LoggedIn".
     * @param isLoggedIn tree if logged in, false otherwise.
     */
    private void setSessionLoginParam(boolean isLoggedIn){
        HttpSession httpSession = null;
        FacesContext facesContext =  FacesContext.getCurrentInstance();

        // make sure we have a valid facesContext
        if (facesContext != null){
            httpSession =
                    (HttpSession) facesContext.getExternalContext()
                            .getSession(false);
        }

        // finally make sure the session is not null before trying to set the
        // login
        if (httpSession != null){
            httpSession.setAttribute("LoggedIn", Boolean.toString(isLoggedIn) );
        }
    }
}
