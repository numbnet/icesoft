/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.login;

import com.icesoft.applications.faces.webmail.WebmailBase;
import com.icesoft.applications.faces.webmail.WebmailMediator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;

/**
 * <p>The <code>MailManager</code>class is responsible for storing data and actions
 * associated with logining to the system.  The loginBean view is directly manipluated
 * by the login.jspx. </p>
 *
 * @since 1.0
 */
public class LoginManager implements WebmailBase {

    private static Log log = LogFactory.getLog(LoginManager.class);

    protected WebmailMediator mediator;

    private LoginBeanFactory loginBeanFactory;

    // initiated state
    private boolean isInit = false;

    /**
     * Create a new instance of a LoginManger.
     */
    public LoginManager() {
    }

    public void setMediator(WebmailMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * Gets the LoginBeanFactory assoicated with this manager.
     *
     * @return loginBean used to capture login data.
     */
    public LoginBeanFactory getLoginBeanFactory() {
        return loginBeanFactory;
    }

    public LoginBean getLoginBean() {
        return loginBeanFactory.getLoginBean();
    }

    /**
     * @param loginBeanFactory
     */
    public void setLoginBeanFactory(LoginBeanFactory loginBeanFactory) {
        this.loginBeanFactory = loginBeanFactory;
    }

    /**
     * initiate the class
     */
    public void init() {
        if (isInit) {
            return;
        }
        isInit = true;
    }

    /**
     * Dispose the class.
     */
    public void dispose() {

        if (log.isDebugEnabled()) {
            log.debug(" Disposing LoginManager");
        }

        // release login accoutns. 
        if (loginBeanFactory != null) {
            loginBeanFactory.dispose();
        }

        isInit = false;
    }


}
