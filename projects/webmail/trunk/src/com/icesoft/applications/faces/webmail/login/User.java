/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
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
