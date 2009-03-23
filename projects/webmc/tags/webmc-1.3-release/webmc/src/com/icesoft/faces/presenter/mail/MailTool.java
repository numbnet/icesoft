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

import java.util.Properties;

/**
 * Convenience class used to send automated generic messages from the presenter
 * The mail settings are generated from the contents of PROPERTIES_FILE, which
 * should be setup by the user if they want mail functionality
 */
public class MailTool {
    private static final String PROPERTIES_FILE = "com/icesoft/faces/presenter/mail/mailaccount.prop";

    private static MailAccount defaultAccount;
    private static Log log = LogFactory.getLog(MailTool.class);
    private static boolean mailAvailable = false;

    public MailTool() {
    }

    static {
        defaultAccount = generateMailAccount();
    }

    /**
     * Method to send a message from the default presenter mail account to the
     * desired recipient
     *
     * @param subject   to use
     * @param content   of the message
     * @param recipient of the message
     * @return true on success
     */
    public static boolean sendMessage(String subject, String content,
                                      String recipient) {
        if (!mailAvailable) {
            return false;
        }

        MailAccountControl control = new MailAccountControl(defaultAccount);
        boolean status = control.sendMessage(
                control.createNewMessage(subject, content, recipient));
        control.disconnect();
        return status;
    }

    /**
     * Method to generate a MailAccount object that is read from the PROPERTIES_FILE
     * If the properties file does not exist, the functionality for emailing a chat
     * log to a user will fail
     *
     * @return the generated and setup account
     */
    private static MailAccount generateMailAccount() {
        try{
            // Load the properties file, which should be in the same directory
            // as this class
            Properties mailFile = new Properties();
            ClassLoader classLoader = MailTool.class.getClassLoader();
            mailFile.load(classLoader.getResourceAsStream(PROPERTIES_FILE));

            // Build the mail object based on the properties file
            MailAccount toReturn =  new MailAccount(
                    mailFile.getProperty("protocol", "imap"),
                    mailFile.getProperty("host", "unknown"),
                    mailFile.getProperty("incomingHost", "unknown"),
                    getIntegerProperty(mailFile.getProperty("incomingPort", "143")),
                    getBooleanProperty(mailFile.getProperty("incomingSSL", "false")),
                    getBooleanProperty(mailFile.getProperty("outgoingVerification", "false")),
                    mailFile.getProperty("outgoingHost", "unknown"),
                    getIntegerProperty(mailFile.getProperty("outgoingPort", "25")),
                    getBooleanProperty(mailFile.getProperty("outgoingSSL", "false")),
                    mailFile.getProperty("user", "mail"),
                    mailFile.getProperty("password", "")
                    );

            mailAvailable = true;
            return toReturn;
        }catch (Exception failedProperties) {
            if (log.isErrorEnabled()) {
                failedProperties.printStackTrace();
                log.error("Mail account failed during setup, so no email will be available");
                log.error("Please make sure " + PROPERTIES_FILE + " exists and is configured");
            }
        }

        mailAvailable = false;
        return new MailAccount();
    }

    /**
     * Convenience method to convert a String value from a Properties file into
     * an integer
     *
     * @param base to convert
     * @return the integer value, or 0 if something went wrong
     */
    private static int getIntegerProperty(String base) {
        if (base != null) {
            try{
                return Integer.parseInt(base);
            }catch (NumberFormatException nfe) {
                /* Intentionally ignored - if this fails it means the base was not an int */
            }
        }

        return 0;
    }

    /**
     * Convenience method to convert a String value from a Properties file into
     * a boolean
     *
     * @param base to convert
     * @return the boolean value
     */
    private static boolean getBooleanProperty(String base) {
        return Boolean.valueOf(base).booleanValue();
    }
}