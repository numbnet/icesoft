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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Properties;

/**
 * Class used to handle the various aspects of emailing, such as connecting,
 * sending messages, etc.
 */
public class MailAccountControl {
    private static Log log = LogFactory.getLog(MailAccountControl.class);

    // Mail account associated with this control
    private MailAccount mailAccount;

    // JavaMail session object
    private Session mailSession;

    // Stores and retrieves messages
    private Store mailStore;

    // If the server is running Sun JDK 1.4 then we can use suns incomingSsl provider,
    // com.sun.net.incomingSsl.internal.incomingSsl.Provider.  However when using IBM webSphere
    // the com.sun classes are not available so we must make sure that IBM's
    // security provider is set, com.ibm.jsse.IBMJSSEProvider.
    static {
        // The first step in this process is to try and load sun provider
        // with the class loader
        boolean loadedSecurityProvider = false;
        try {
            Class sunSecurityProviderClass =
                    Class.forName("com.sun.net.ssl.internal.ssl.Provider");
            Constructor sunSecurityConstructor =
                    sunSecurityProviderClass.getConstructor((java.lang.Class[]) null);
            java.security.Provider sunSecurityProvider =
                    (java.security.Provider) sunSecurityConstructor
                            .newInstance((Object[]) null);
            java.security.Security.addProvider(sunSecurityProvider);
            loadedSecurityProvider = true;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        // If the sun Security provider could not be loaded then we can assume that we are using
        // an IBM JVM and thus must load there provider.
        if (!loadedSecurityProvider) {
            try {
                Class sunSecurityProviderClass =
                        Class.forName("com.ibm.jsse.IBMJSSEProvider");
                Constructor sunSecurityConstructor =
                        sunSecurityProviderClass.getConstructor((java.lang.Class[]) null);
                java.security.Provider sunSecurityProvider =
                        (java.security.Provider) sunSecurityConstructor
                                .newInstance((Object[]) null);
                java.security.Security.addProvider(sunSecurityProvider);
                loadedSecurityProvider = true;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        // Lastly if we get this far there is no SSL provider and thus the applications will fail
        if (!loadedSecurityProvider) {
            if (log.isErrorEnabled()) {
                log.error("No security provider");
            }
        }
    }

    /**
     * Creates a new <code>MailAccountControl</code>
     *
     * @param mailAccount to control
     */
    public MailAccountControl(MailAccount mailAccount) {
        this.mailAccount = mailAccount;
    }

    /**
     * Is this service currently connected?
     *
     * @return true if the service is connected, false if it is not connected
     */
    public boolean isConnected() {
        return mailStore != null && mailStore.isConnected();
    }

    /**
     * Connects the incoming mail server information specified by the
     * MailAccount class.  This method will return true if the connection has
     * already made or if the connection process was successful.
     *
     * @return true if the connection succeeded; otherwise, false.
     */
    private synchronized boolean connect() {
        try {
            // Make sure we are not already connected.
            if (mailStore != null && mailStore.isConnected()) {
                return true;
            } else {
                Properties mailProperties = System.getProperties();

                // Setup SSL connection factory
                if (mailAccount.isIncomingSsl()) {
                    mailProperties.setProperty("mail.imap.socketFactory.class",
                                               "javax.net.ssl.SSLSocketFactory");
                    mailProperties.setProperty(
                            "mail.imap.socketFactory.fallback", "false");
                    mailProperties.setProperty("mail.imap.port", String.valueOf(
                            mailAccount.getIncomingPort()));
                    mailProperties.setProperty("mail.imap.socketFactory.port",
                                               String.valueOf(
                                                       mailAccount.getIncomingPort()));
                }
                // Otherwise log on using http, avoid using incomingSsl properties as
                // it will botch the connection
                else {
                    mailProperties.remove("mail.imap.socketFactory.class");
                    mailProperties.remove("mail.imap.socketFactory.fallback");
                    mailProperties.remove("mail.imap.port");
                    mailProperties.remove("mail.imap.socketFactory.port");
                }

                // Connection url
                URLName urlName =
                        new URLName(mailAccount.getProtocol() + "://" +
                                    mailAccount.getMailUsername() + "@" +
                                    mailAccount.getHost());

                // Password authenticator
                PasswordAuthentication pwdAuth = new PasswordAuthentication(
                        mailAccount.getMailUsername(),
                        mailAccount.getPassword());
                mailSession = Session.getInstance(mailProperties);
                mailSession.setPasswordAuthentication(urlName, pwdAuth);

                // Finally try to connect
                mailStore = mailSession.getStore(urlName);
                mailStore.connect();

                return true;
            }
        } catch (NoSuchProviderException e) {
            if (log.isErrorEnabled()) {
                log.error("Connection Error - No such provider for " +
                          mailAccount.toString(), e);
            }
            return false;
        } catch (AuthenticationFailedException e) {
            if (log.isErrorEnabled()) {
                log.error("Connection Error - Authentication error " +
                          mailAccount.toString(), e);
            }
            return false;
        } catch (MessagingException e) {
            if (log.isErrorEnabled()) {
                log.error("Connection Error - Messaging exception " +
                          mailAccount.toString(), e);
            }
            return false;
        } catch (Throwable e) {
            if (log.isErrorEnabled()) {
                log.error("Connection Error - Other exception " +
                          mailAccount.toString(), e);
            }
            return false;
        }
    }

    /**
     * Closes the connection incoming mail server
     */
    public void disconnect() {
        // Make sure we are even connected
        if (mailStore != null && mailStore.isConnected()) {
            try {
                mailStore.close();
            } catch (MessagingException e) {
                if (log.isErrorEnabled()) {
                    log.error("Messaging exception on close method: " +
                              mailAccount.toString());
                }
            }
        }
    }

    /**
     * Create a new message bean which contains a MimeMessage package
     *
     * @param subject    of the message
     * @param content    of the message
     * @param recipients of the message
     * @return a new MessageBean
     */
    public MimeMessage createNewMessage(String subject, String content,
                                        String recipients) {
        // Make sure we are connected
        connect();
        if (mailSession != null) {
            MimeMessage message = new MimeMessage(mailSession);
            try {
                message.setFrom(InternetAddress
                        .parse(mailAccount.getEmail(), false)[0]);
                message.setSubject(subject);
                message.setText(content);
                message.addRecipients(Message.RecipientType.TO,
                                      InternetAddress.parse(recipients, false));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return message;
        } else {
            return null;
        }
    }

    /**
     * Sends the given MessageBean using this controllers mail account
     *
     * @param message message to send
     * @return true if the message was send with out errors; otherwise, false.
     */
    public boolean sendMessage(MimeMessage message) {
        if (message != null) {
            try {
                if (!connect()) {
                    return false;
                }
                Transport transport;
                // Check if SSL is needed
                if (mailAccount.isOutgoingSsl()) {
                    transport = mailSession.getTransport("smtps");
                } else {
                    transport = mailSession.getTransport("smtp");
                }

                // Check if username and password authentication is needed
                if (mailAccount.isOutgoingVerification()) {
                    transport.connect(mailAccount.getOutgoingHost(),
                                      mailAccount.getOutgoingPort(),
                                      mailAccount.getMailUsername(),
                                      mailAccount.getPassword());
                } else {
                    transport.connect(mailAccount.getOutgoingHost(),
                                      mailAccount.getOutgoingPort(), null,
                                      null);
                }

                // Try and send the message
                if (transport.isConnected() && message.getContent() != null) {
                    // Add a time stamp
                    message.setSentDate(new Date());

                    // Send the message.
                    transport.sendMessage(message, message.getAllRecipients());
                }
            } catch (SendFailedException e) {
                e.printStackTrace();
                return false;
            } catch (MessagingException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}