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
