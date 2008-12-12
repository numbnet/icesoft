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

import com.icesoft.applications.faces.webmail.WebmailBase;

import javax.faces.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;

/**
 * <p>The <code>LoginBeanFactory</code> class is responsible for storing creating
 * an appropriate login bean for the given login mode.  Login beans can
 * be registered witht his class vian the setRegisterLoginBean method</p>
 * </p>
 * <p>This factory contains a RegisteredLoginBean bean by default.  Addition
 * LoginBean must be added as need by users of the class.</p>
 */
public class LoginBeanFactory implements WebmailBase {

//    private static Log log = LogFactory.getLog(LoginBeanFactory.class);

    private HashMap registeredLoginBeans = new HashMap(2);

    /**
     * Login is of type anonymous and UserAccountPool will be used to get
     * a creat a anonymous account.
     */
    public static final String ANONYMOUS_MODE = "0";

    /**
     * Login is of type registered, the provided userName and userPassword
     * will be used for the login process.
     */
    public static final String REGISTERED_MODE = "1";

    // login type, either anonymous or registered.
    public String loginMode = REGISTERED_MODE;

    public String loginValidateOutcome = "";

    // initiated state
    private boolean isInit = false;


    public LoginBeanFactory() {
        // factory always has a registeredLoginBean created as a default.
        RegisteredLoginBean registeredLoginBean = new RegisteredLoginBean();
        registeredLoginBeans.put(registeredLoginBean.getLoginType(), registeredLoginBean);
    }

    /**
     * Gets the verified user object for this LoginBean if any.
     *
     * @return a verified user object, null if the LoginBean user name and password
     *         could not be verified.
     */
    public User getVerifiedUser() {
        LoginBean loginBean = (LoginBean) registeredLoginBeans.get(loginMode);

        if (loginBean != null) {
            return loginBean.getVerifiedUser();
        } else {
            return null;
        }
    }

    /**
     * Gets a LoginBean for the current login mode.
     *
     * @return LoginBean object associated witht the current login mode.
     */
    public LoginBean getLoginBean() {
        return (LoginBean) registeredLoginBeans.get(loginMode);
    }

    /**
     * Register a login bean with the factory.
     *
     * @param loginBean
     */
    public void setRegisterLoginBean(LoginBean loginBean) {
        registeredLoginBeans.put(loginBean.getLoginType(), loginBean);
    }

    /**
     * Sets the login mode.
     *
     * @param loginMode login mode assosciated with an individual LoginBean.  There
     *                  should be a 1-1 releation of login modes with LoginBeans.
     */
    public void setLoginMode(final String loginMode) {
        this.loginMode = loginMode;
    }

    /**
     * Gets the current login mode.
     *
     * @return login state associated with the factory.
     */
    public String getLoginMode() {
        return loginMode;
    }

    /**
     * Validates the LoginBean username and password.
     *
     * @param e
     */
    public void validateLoginBean(ActionEvent e) {
        // get bean from hash depending on loginMode.
        LoginBean loginBean = (LoginBean) registeredLoginBeans.get(loginMode);

        if (loginBean != null) {
            loginValidateOutcome = loginBean.login();
        } else {
            loginValidateOutcome = LoginBean.LOGIN_FAILURE;
        }
    }

    /**
     * Gets the outcome of the validateLoingBean method call.
     *
     * @return if success LOGIN_SUCCESS is returned; otherwise, LOGIN_FAILURE.
     */
    public String getLoginValidateOutcome() {
        return loginValidateOutcome;
    }

    /**
     * Initializes the LoingBean Factory.
     */
    public void init() {
        if (isInit) {
            return;
        }
        isInit = true;
    }

    /**
     * Disposes the login factory.  All registered LoginBean object's logout
     * method is called on dispose.
     */
    public void dispose() {
        // itereate through registered logins and call logout.
        if (registeredLoginBeans != null) {
            Iterator loginBeans = registeredLoginBeans.values().iterator();
            LoginBean loginBean;
            while (loginBeans.hasNext()) {
                loginBean = (LoginBean) loginBeans.next();
                if (loginBean != null) {
                    loginBean.logout();
                }
            }
        }

        isInit = false;
    }
}
