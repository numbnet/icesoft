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
package com.icesoft.faces.presenter.mail;

/**
 * Class to act as a mail account for sending chat log emails from the
 * presenter
 */
public class MailAccount {
    // mail protocol imap, smtp, pop3, etc.
    private String protocol;

    // DNS host name of email server.
    private String host;

    // Incoming host name
    private String incomingHost;

    // Connection incomingPort on the host
    private int incomingPort;

    // Account needs incomingSsl connection
    private boolean incomingSsl;

    // Verification flag for outgoing server.
    private boolean outgoingVerification;

    // Incoming host name
    private String outgoingHost;

    // Connection incomingPort on the host
    private int outgoingPort;

    // Account needs incomingSsl connection
    private boolean outgoingSsl;

    // Email user namer, or full email with mailUsername@host
    private String mailUsername;

    // UserAccount password
    private String password;

    public MailAccount() {
    }

    public MailAccount(String pro, String ho, String incHost, int incPort,
                       boolean incSSL,
                       boolean outVer, String outHost, int outPort,
                       boolean outSSL, String name, String pwd) {
        protocol = pro;
        host = ho;
        incomingHost = incHost;
        incomingPort = incPort;
        incomingSsl = incSSL;
        outgoingVerification = outVer;
        outgoingHost = outHost;
        outgoingPort = outPort;
        outgoingSsl = outSSL;
        mailUsername = name;
        password = pwd;
    }

    /**
     * Gets the password of the user account
     *
     * @return encrypted password of the user account
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user account password
     *
     * @param password new password for account
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the mail account connection protocol
     *
     * @return mail account connection protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the mail account connection protocol
     *
     * @param protocol new account protocol
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * Gets the host name of the mail server
     *
     * @return host name of mail server
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host name of the mail server
     *
     * @param host DNS host name of mail server
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets the incomingPort number used to connect to the mail server
     *
     * @return incomingPort number used to connect to mail server
     */
    public int getIncomingPort() {
        return incomingPort;
    }

    /**
     * Sets the incomingPort number used to connect ot the mail server
     *
     * @param incomingPort connection incomingPort number
     */
    public void setIncomingPort(int incomingPort) {
        this.incomingPort = incomingPort;
    }

    /**
     * Gets the account SSL connection property
     *
     * @return true indicates that a SSL connection is required; otherwise,
     *         false
     */
    public boolean isIncomingSsl() {
        return incomingSsl;
    }

    /**
     * Sets the account incomingSsl connection property
     *
     * @param incomingSsl true indicates that a incomingSsl connection is
     *                    required; otherwise, false
     */
    public void setIncomingSsl(boolean incomingSsl) {
        this.incomingSsl = incomingSsl;
    }

    /**
     * Gets mail user name of the connection account.  Mail user name is the
     * start of the email address, ie. mailUsername@host
     *
     * @return mail user name
     */
    public String getMailUsername() {
        return mailUsername;
    }

    /**
     * Sets the mail user name
     *
     * @param mailUsername mail user name
     */
    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }

    /**
     * Gets the email address in a formatted manner, ie. user@host
     *
     * @return formatted email address
     */
    public String getEmail() {
        return mailUsername + "@" + host;
    }

    /**
     * Gets the outgoing mail host name of the mail server
     *
     * @return host name of outgoing mail account
     */
    public String getOutgoingHost() {
        return outgoingHost;
    }

    /**
     * Sets the Outgoing Host name of the mail server
     *
     * @param outgoingHost outgoing host name
     */
    public void setOutgoingHost(String outgoingHost) {
        this.outgoingHost = outgoingHost;
    }

    /**
     * Gets the outgoing mail server port number.  The default for smtp is port
     * 25 and smtps is port 465
     *
     * @return the outgoing mails server port number
     */
    public int getOutgoingPort() {
        return outgoingPort;
    }

    /**
     * Sets the outgoing mail server port number.  The default for smtp is port
     * 25 and smtps is port 465
     *
     * @param outgoingPort the outgoing mails server port number
     */
    public void setOutgoingPort(int outgoingPort) {
        this.outgoingPort = outgoingPort;
    }

    /**
     * Indicates if the outgoing mail server should be using SSL for the socket
     *
     * @return true if SSL is enabled; otherwise, false
     */
    public boolean isOutgoingSsl() {
        return outgoingSsl;
    }

    /**
     * Sets if the outgoing mail server connection should use SSL
     *
     * @param outgoingSsl true to enable SSL; otherwise, false
     */
    public void setOutgoingSsl(boolean outgoingSsl) {
        this.outgoingSsl = outgoingSsl;
    }

    /**
     * Indicates if the outgoing mail server needs to username and password
     * authentication to make a connection
     *
     * @return true indicates that authentication is  needed; otherwise, false
     */
    public boolean isOutgoingVerification() {
        return outgoingVerification;
    }

    /**
     * Sets the outgoing verification flag
     *
     * @param outgoingVerification true indicates that authentication is needed;
     *                             otherwise, false
     */
    public void setOutgoingVerification(boolean outgoingVerification) {
        this.outgoingVerification = outgoingVerification;
    }

    /**
     * Gets the incoming mail server host name
     *
     * @return the incoming mail server host name
     */
    public String getIncomingHost() {
        return incomingHost;
    }

    /**
     * Sets the incoming mail server host name
     *
     * @param incomingHost incoming mail server host name
     */
    public void setIncomingHost(String incomingHost) {
        this.incomingHost = incomingHost;
    }
}