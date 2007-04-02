/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.login;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


/**
 * <p>The <code>UserAccountPool</code> object is responsible for managing the
 * user accounts pool.  When a user successfully logs into the system a user
 * account is locked in the user pool until the user logs out or the session
 * expires.</p>
 * <p/>
 * <p>There is a limited number of user accounts available, if no user counts
 * are available a user must wait until one becomes available.  To check if a
 * mail account is available a call to <link>#getAnonUserAccount</link> must be called.
 * When a user logs off the system a call to <link>#releaseAnonUserAccount</link>
 * must be called, otherwise the account will be locked until the session expires.
 * </p>
 * <p/>
 * <p>The <code>UserAccountPool</code> should be defined in applications scope
 * to insure that only one instance of the pool exisits</p>
 *
 * @since 1.0
 */
public class UserAccountPool {

    /**
     * List of isEmailAddressValid username and passwords for the available mail accounts.
     * The list is hard coded to 8, but a future release may allow for customized
     * settings for new mails accounts.
     */
    private static UserBean[] anonUserBeanPool = {
            new UserBean("anon1", "anon1"),
            new UserBean("anon2", "anon2"),
            new UserBean("anon3", "anon3"),
            new UserBean("anon4", "anon4"),
            new UserBean("anon5", "anon5"),
            new UserBean("anon6", "anon6"),
            new UserBean("anon7", "anon7"),
            new UserBean("anon8", "anon8"),
            new UserBean("anon9", "anon9"),
            new UserBean("anon10", "anon10"),
            new UserBean("anon11", "anon11"),
            new UserBean("anon12", "anon12"),
            new UserBean("anon13", "anon13"),
            new UserBean("anon14", "anon14"),
            new UserBean("anon15", "anon15"),
            new UserBean("anon16", "anon16"),
            new UserBean("anon17", "anon17"),
            new UserBean("anon18", "anon18"),
            new UserBean("anon19", "anon19"),
            new UserBean("anon20", "anon20"),
            new UserBean("anon21", "anon21"),
            new UserBean("anon22", "anon22"),
            new UserBean("anon23", "anon23"),
            new UserBean("anon24", "anon24"),
            new UserBean("anon25", "anon25"),
            new UserBean("anon26", "anon26"),
            new UserBean("anon27", "anon27"),
            new UserBean("anon28", "anon28"),
            new UserBean("anon29", "anon29"),
            new UserBean("anon30", "anon30")
    };

    // isnure singleton.
    private UserAccountPool(){

    }

    /**
     * Gets an available mail account from the user account pool which can be used
     * to anonymousLogin to the mail server when available.  If there are no user accounts
     * available null is returned
     *
     * @return a isEmailAddressValid user information for a mail account if available, null otherwise.
     */
    public static synchronized UserBean getAnonUserAccount() {
        HttpSession poolSession;
        HttpSession httpSession = (HttpSession)
                FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        // return current session if user is already logged in.
        for (int i = 0, max = anonUserBeanPool.length; i < max; i++) {
            poolSession = anonUserBeanPool[i].getHttpSession();
            if (poolSession != null &&
                    poolSession.equals(httpSession)) {
                anonUserBeanPool[i].setInUse(true);
                // mark session as already in use
                return anonUserBeanPool[i];
            }
        }
        // otherwise login with a new account.
        for (int i = 0, max = anonUserBeanPool.length; i < max; i++) {
            if (!anonUserBeanPool[i].isInUse()) {
                anonUserBeanPool[i].setInUse(true);
                anonUserBeanPool[i].setHttpSession(httpSession);
                return anonUserBeanPool[i];
            }
        }
        return null;
    }

    /**
     * Release the mail account associated with the specified session id.  It
     * must be assumed that account was logged off from the mail server before
     * this method is called.
     *
     * @param user user
     */
    public static void releaseAnonUserAccount(User user) {
        // find and mark the httpSession as not being used
        if (user != null) {
            for (int i = 0; i < anonUserBeanPool.length; i++) {
                if (user.getUserName().equals(anonUserBeanPool[i].getUserName())){
                    anonUserBeanPool[i].setInUse(false);
                    anonUserBeanPool[i].setHttpSession(null);
                    break;
                }
            }
        }
    }

    /**
     * <p>Release the mail account associated with the specified session id.
     * This method should be called when a session is expired to release the
     * appropriate anonymous account.</p>
     *
     * @param session HTTP session associated with a anonymous user account who
     *                  should be released and put back in the user pool.
     */
    public static void releaseAnonUserAccount(HttpSession session) {
        if (session != null) {
            for (int i = 0; i < anonUserBeanPool.length; i++) {
                if (anonUserBeanPool[i].getHttpSession() != null &&
                    session.getId().equals(anonUserBeanPool[i].getHttpSession().getId())){
                    anonUserBeanPool[i].setInUse(false);
                    anonUserBeanPool[i].setHttpSession(null);
                    break;
                }
            }
        }
    }

}

