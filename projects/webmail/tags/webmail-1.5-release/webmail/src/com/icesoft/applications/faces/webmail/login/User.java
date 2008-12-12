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


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.icesoft.applications.faces.webmail.util.db.HibernateUtil;


/**
 * <p>The <code>User</code> model's purpose is to mananage the userName and
 * password information of a user.  This class is bound to a database table
 * users. </p>
 *
 * @since 1.0
 */
public class User {

    private static Log log = LogFactory.getLog(User.class);

    // basic properties of a user.
    protected String userName;
    protected String userPassword;
    protected String firstName;
    protected String lastName;
    protected String company;

    private String oldPassword = "";
    private String newPassword = "";
    private String verifiedPassword = "";

    /**
     * same as copy(User), return nothing
     *
     * @param s
     * @param d
     */
    public static void copy(User s, User d) {
        d.setCompany(s.getCompany());
        d.setFirstName(s.getFirstName());
        d.setLastName(s.getLastName());
        d.setUserName(s.getUserName());
        d.setUserPassword(s.getUserPassword());
    }

    /**
     * Default user bean constructor
     */
    public User() {

    }

    /**
     * Creates a new instance of User with the specified username and password
     *
     * @param userName
     * @param password
     */
    public User(String userName, String password) {
        this.userName = userName;
        this.userPassword = password;
    }

    /**
     * Gets the username associated with this user.
     *
     * @return user name of user.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the username of the User
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the username of the user.
     *
     * @return unser name of user.
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Set the userPassword of user.
     *
     * @param userPassword new user password.
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }


    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getVerifiedPassword() {
        return verifiedPassword;
    }

    public void setVerifiedPassword(String verifiedPassword) {
        this.verifiedPassword = verifiedPassword;
    }

    /**
     * Save user data to database.   Hibernate is used to persist the user. 
     */
    public void save(){

    	 try {
             // save the changes to the database
             Session session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             session.saveOrUpdate(this);
             // finish the transaction
             session.getTransaction().commit();
         }
         catch (HibernateException e) {
             log.error("Failed to cancel User changes - SQL error", e);
         }


    }
}
