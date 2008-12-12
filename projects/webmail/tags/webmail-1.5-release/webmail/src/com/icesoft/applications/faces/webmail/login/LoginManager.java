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
