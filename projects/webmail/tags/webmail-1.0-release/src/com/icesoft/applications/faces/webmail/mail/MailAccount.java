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
package com.icesoft.applications.faces.webmail.mail;

import com.icesoft.applications.faces.webmail.util.db.HibernateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.faces.event.ActionEvent;


/**
 * <p>The <code>MailAccount</code> model's purpose is to mananage mail
 * account settings.</p>
 * <p/>
 * <p><b>Note: </b><br />
 * This class makes to attempt to keep the user password encrypted. This
 * will be corrected in a future release</p>
 *
 * @since 1.0
 */
public class MailAccount {

    protected static Log log = LogFactory.getLog(MailAccount.class);

    protected long id;

    //whether this account is selected in account management view
    protected boolean selected = false;

    // UserAccount name of user
    protected String userName;

    // mail protocol imap, smtp, pop3, etc.
    // todo: we should be using constants here or a reference table in the DB
    protected String protocol;

    // DNS host name of email server.
    protected String host;

    // incoming host name
    protected String incomingHost;

    // Connection incomingPort on the host
    protected int incomingPort;

    // account needs incomingSsl connection
    protected boolean incomingSsl;

    // verifcaction flag for outgoing server.
    protected boolean outgoingVerification;

    // incoming host name
    protected String outgoingHost;

    // Connection incomingPort on the host
    protected int outgoingPort;

    // account needs incomingSsl connection
    protected boolean outgoingSsl;

    // email user namer, or full email with mailUsername@host
    protected String mailUsername;

    // UserAccount password
    protected String password;


    public void save() {


        try {
            // save the changes to the database
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.saveOrUpdate(this);
            // finish the transaction
            session.getTransaction().commit();
        }
        catch (HibernateException e) {
            log.error("Failed to cancel mail account changes - SQL error", e);
        }
    }

    public void delete() {
        try {
            // save the changes to the database
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.delete(this);
            // finish the transaction
            session.getTransaction().commit();
        }
        catch (HibernateException e) {
            log.error("Failed to cancel mail account changes - SQL error", e);
        }


    }

    /**
     * Is current account selected in the management view
     *
     * @return true if selected false otherwise
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Set whether this account is selected in the management view
     *
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Gets the unique id associated with this mail account object.
     *
     * @return unique id.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique id.  This value should only be copied and never manipulated
     * outside of the database.
     *
     * @param id
     */
    private void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the user name of the user account.
     *
     * @return user name of the user account.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets the password of the user account.
     *
     * @return encrypted password of the user account.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user account password.
     *
     * @param password new password for account.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the user name of the account.
     *
     * @param username new user name to assign to the account.
     */
    public void setUserName(String username) {
        this.userName = username;
    }

    /**
     * Gest the mail account connection protocol.
     *
     * @return mail account connection protocol.
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the mail account connection protocol.
     *
     * @param protocol new account protocol
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * Gets the host name of the mail server.
     *
     * @return host name of mail server.
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host name of the mail server.
     *
     * @param host DNS host name of mail server.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets the incomingPort number used to connect to the mail server.
     *
     * @return incomingPort number used to connect to mail server.
     */
    public int getIncomingPort() {
        return incomingPort;
    }

    /**
     * Sets the incomingPort number used to connect ot the mail server.
     *
     * @param incomingPort connection incomingPort number.
     */
    public void setIncomingPort(int incomingPort) {
        this.incomingPort = incomingPort;
    }

    /**
     * Gets the account SSL connection property.
     *
     * @return true indicates that a SSL connection is required; otherwise, false.
     */
    public boolean isIncomingSsl() {
        return incomingSsl;
    }

    /**
     * Sets the account incomingSsl connection property.
     *
     * @param incomingSsl true indicates that a incomingSsl connection is required; otherwise, false.
     */
    public void setIncomingSsl(boolean incomingSsl) {
        this.incomingSsl = incomingSsl;
    }

    /**
     * Gets mail user name of the connection account.  Mail user name is the
     * start of the email address, ie. mailUsername@host.
     *
     * @return mail user name.
     */
    public String getMailUsername() {
        return mailUsername;
    }

    /**
     * Sets the mail user name.
     *
     * @param mailUsername mail user name.
     */
    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }

    /**
     * Gets the outgoing mail host name of the mail server.
     *
     * @return host name of outgoing mail account
     */
    public String getOutgoingHost() {
        return outgoingHost;
    }

    /**
     * Sets the Outgoing Host name of the mail server.
     *
     * @param outgoingHost outgoing host name
     */
    public void setOutgoingHost(String outgoingHost) {
        this.outgoingHost = outgoingHost;
    }

    /**
     * Gets the outgoing mail server port number.  The default for smtp is port
     * 25 and smpts is port 465.
     *
     * @return the outgoing mails server port number
     */
    public int getOutgoingPort() {
        return outgoingPort;
    }

    /**
     * Sets the outgoing mail server port number.  The default for smtp is port
     * 25 and smpts is port 465.
     *
     * @param outgoingPort the outgoing mails server port number
     */
    public void setOutgoingPort(int outgoingPort) {
        this.outgoingPort = outgoingPort;
    }

    /**
     * Get the email address associated with this account.
     *
     * @return email address of the user account.
     */
    public String getEmail() {
        return mailUsername + "@" + host;
    }


    /**
     * Indicates if the outgoing mail server should be using SSL for the socket.
     *
     * @return true if SSL is enabled; otherwise, false.
     */
    public boolean isOutgoingSsl() {
        return outgoingSsl;
    }

    /**
     * Sets if the outgoing mail server connection should use SSL
     *
     * @param outgoingSsl ture to enable SSL; otherwise, false.
     */
    public void setOutgoingSsl(boolean outgoingSsl) {
        this.outgoingSsl = outgoingSsl;
    }

    /**
     * Indicates if the outgoing mail server needs to username and password
     * authentication to make a connection.
     *
     * @return true indicates that authentication is  needed; otherwise, false.
     */
    public boolean isOutgoingVerification() {
        return outgoingVerification;
    }

    /**
     * Sets the outgoing verification flag.
     *
     * @param outgoingVerification true indicates that authentication is
     *                             needed; otherwise, false.
     */
    public void setOutgoingVerification(boolean outgoingVerification) {
        this.outgoingVerification = outgoingVerification;
    }

    /**
     * Gets the incoming mail server host name.
     *
     * @return the incoming mail server host name.
     */
    public String getIncomingHost() {
        return incomingHost;
    }

    /**
     * Sets the incoming mail server host name.
     *
     * @param incomingHost incoming mail server host name.
     */
    public void setIncomingHost(String incomingHost) {
        this.incomingHost = incomingHost;
    }

    public String toString() {
        return new StringBuffer().append("id: ").append(id)
                .append("\n host: ").append(host)
                .append("\n username:      ").append(mailUsername)
                .append("\n incoming host: ").append(incomingHost)
                .append("\n incoming ssl:  ").append(incomingSsl)
                .append("\n incoming port: ").append(incomingPort)
                .append("\n outgoing host: ").append(outgoingHost)
                .append("\n outgoing ssl:  ").append(outgoingSsl)
                .append("\n outgoing port: ").append(outgoingPort)
                .append("\n outoing verif: ").append(outgoingVerification)
                .append("\n protocol: ").append(protocol).toString();
    }



    public static void copy(MailAccount source, MailAccount destination) {
        destination.setHost(source.getHost());
        destination.setIncomingHost(source.getIncomingHost());
        destination.setIncomingSsl(source.isIncomingSsl());
        destination.setMailUsername(source.getMailUsername());
        destination.setOutgoingHost(source.getOutgoingHost());
        destination.setOutgoingPort(source.getOutgoingPort());
        destination.setOutgoingSsl(source.isOutgoingSsl());
        destination.setOutgoingVerification(source.isOutgoingVerification());
        destination.setPassword(source.getPassword());
        destination.setProtocol(source.getProtocol());
        destination.setUserName(source.getUserName());
        destination.setSelected(source.isSelected());
    }

}