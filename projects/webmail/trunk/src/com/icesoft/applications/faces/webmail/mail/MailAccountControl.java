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


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.internet.MimeMessage;
import javax.mail.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.List;


/**
 * <p>The <code>MailAccountControl</code> acts as a control for JavaMail related
 * tasks. The control works with a mail account and resposible for handling everything
 * from connection to the <code>MailAccount</code> to sending messages.</p>
 *
 * @since 1.0
 */
public class MailAccountControl {

    private static Log log = LogFactory.getLog(MailAccountControl.class);

    // mail account associated with this control
    private MailAccountBean mailAccount;

    // JavaMail session object.
    private Session mailSession;

    // Stores and retreives messages.
    private Store mailStore;

    // If the server is running Sun JDK 1.4 then we can use suns incomingSsl provider,
    // com.sun.net.incomingSsl.internal.incomingSsl.Provider.  However when using IBM webSphere
    // the com.sun classes are not available so we must make sure that IBM's
    // security provider is set, com.ibm.jsse.IBMJSSEProvider.
    static {
        // The first steop in this process is to try and load sun provider
        // with the class loader
        boolean loadedSecurityProvider = false;
        try {
            Class sunSecurityProviderClass =
                    Class.forName("com.sun.net.ssl.internal.ssl.Provider");
            Constructor sunSecurityConstructor =
                    sunSecurityProviderClass.getConstructor((Class[])null);
            java.security.Provider sunSecurityProvider =
                    (java.security.Provider) sunSecurityConstructor.newInstance((Object[])null);
            java.security.Security.addProvider(sunSecurityProvider);
            loadedSecurityProvider = true;
        }
        catch (Throwable e) {
            if (log.isDebugEnabled())
                log.error("Could not class load com.sun.net.ssl.internal.ssl.Provider ");
        }
        // if the sun Security provider could not be loaded then we can assume that we are using
        // an IBM JVM and thus must load there provider.
        if (!loadedSecurityProvider) {
            try {
                Class sunSecurityProviderClass =
                        Class.forName("com.ibm.jsse.IBMJSSEProvider");
                Constructor sunSecurityConstructor =
                        sunSecurityProviderClass.getConstructor((Class[])null);
                java.security.Provider sunSecurityProvider =
                        (java.security.Provider) sunSecurityConstructor.newInstance((Object[])null);
                java.security.Security.addProvider(sunSecurityProvider);
                loadedSecurityProvider = true;
            }
            catch (Throwable e) {
                if (log.isDebugEnabled())
                    log.error("Could not class load com.ibm.jsse.IBMJSSEProvider ");
            }
        }
        // lastly if we get this far there is no SSL provider and thus the applications will fail
        if (!loadedSecurityProvider) {
            log.fatal("Could not load a security provider..  ");
        }
    }

    /**
     * Creates a new <code>MailAccountControl</code>
     *
     * @param mailAccount
     */
    public MailAccountControl(MailAccountBean mailAccount) {
        this.mailAccount = mailAccount;
    }

    /**
     * Utility method to force the mail api to throw message changed and count
     * events.  A call to the folders isOpen() method.
     * @param mailFolderBean folder to tickle
     */
    public void tickleMailAccount(MailFolderBean mailFolderBean){
        // we need to call these methods to tickle the mail folder
        // to throw a MessageCountEvent
        if (mailFolderBean != null){
            if (mailFolderBean.getFolder() != null){
                mailFolderBean.getMessageCount();
            }
        }
    }

    /**
     * Test the mail account connection.  Failures could be do to login failure
     * or an invalid mail address.
     *
     * @return true if the pinging of mail account was sucessful; otherwise, false.
     */
    public boolean pingMailAccount() {
        // disconnect if already connected, then try to connect
        disconnect();
        // reconnect and report success
        return connect();
    }

    /**
     * Is this service currently connected?
     * @return true if the service is connected, false if it is not connected
     */
    public boolean isConnected(){
        return mailStore != null && mailStore.isConnected();
    }

    /**
     * Connects the incoming mail server information specified by the MailAccount
     * class.  This method will return true if the connection has already made
     * or if the connection process was successful.
     *
     * @return true if the connection succeded; otherwise, false.
     */
    public synchronized boolean connect() {
        try {
            // make sure we are not allready connected.
            if (mailStore != null && mailStore.isConnected()) {
                return true;
            } else {
                Properties mailProperties = System.getProperties();

                // setup SSL connection factory
                if (mailAccount.isIncomingSsl()) {
                    mailProperties.setProperty("mail.imap.socketFactory.class",
                            "javax.net.ssl.SSLSocketFactory");
                    mailProperties.setProperty("mail.imap.socketFactory.fallback",
                            "false");
                    mailProperties.setProperty("mail.imap.port",
                            String.valueOf(mailAccount.getIncomingPort()));
                    mailProperties.setProperty("mail.imap.socketFactory.port",
                            String.valueOf(mailAccount.getIncomingPort()));
                }
                // otherwise log on using http, avoid using incomingSsl properties as
                // it will botch the connection .
                else {
                    mailProperties.remove("mail.imap.socketFactory.class");
                    mailProperties.remove("mail.imap.socketFactory.fallback");
                    mailProperties.remove("mail.imap.port");
                    mailProperties.remove("mail.imap.socketFactory.port");
                }

                // connection url
                URLName urlName = new URLName(mailAccount.getProtocol() + "://" +
                        mailAccount.getMailUsername() + "@" + mailAccount.getHost());

                // password authenticator
                PasswordAuthentication pwdAuth =
                        new PasswordAuthentication(mailAccount.getMailUsername(),
                                mailAccount.getPassword());

                mailSession = Session.getInstance(mailProperties);
                mailSession.setPasswordAuthentication(urlName, pwdAuth);

                // finally try to connect
                mailStore = mailSession.getStore(urlName);
                mailStore.connect();
                return true;
            }
        }
        catch (NoSuchProviderException e) {
            log.error("Connection Error - No such provider for " + mailAccount.toString());
            return false;
        }catch(AuthenticationFailedException e){
            log.error("Connection Error - Authentication error " + mailAccount.toString());
            return false;
        }catch (MessagingException e) {
            log.error("Connection Error - Messaging Exception " + mailAccount.toString());
            return false;
        }catch (Throwable e) {
            log.error("Connection Error - Misc. Exception " + mailAccount.toString());
            return false;
        }
    }

    /**
     * Closes the connection incoming mail server.
     */
    public void disconnect() {
        // make sure we are not allready connected.
        if (mailStore != null && mailStore.isConnected()) {
            try {
                mailStore.close();
            } catch (MessagingException e) {
                log.error("Messaging Exception on close method " + mailAccount.toString());
            }
        }
    }

    /**
     * Utility method to look for the given folder name, if not found then the
     * folder is created.
     *
     * @param folderName folder name to create if not found.
     */
    public void createMailFolder(final String folderName){

        if (log.isDebugEnabled()) {
            log.debug("Looking for folder : " + folderName);
        }

        try {
            // make sure we are connected
            connect();

            if (mailStore != null){
                Folder folder = mailStore.getFolder(folderName);

                // if the folder does not exist then we must create it.
                if (!folder.exists()){
                    if (log.isDebugEnabled()) {
                        log.debug("Creating folder : " + folderName);
                    }
                    folder.create(Folder.HOLDS_MESSAGES);
                }
                else{
                    if (log.isDebugEnabled()) {
                        log.debug("Found folder : " + folderName);
                    }
                }
            }
        } catch (MessagingException e) {
            log.error("Creating new folder failed " + folderName);
        }
    }

    /**
     * Gets a list of folders associated with the MailAccount.
     *
     * @return list of mail account folders.  Null if there are no folders or
     *         if no connection is made
     */
    public ArrayList getFolderList() {
        ArrayList iceMailFolders = null;
        Folder rootFolder;
        try {
            // make sure we are connected;
            connect();

            rootFolder = mailStore.getDefaultFolder();
            if ((rootFolder.getType() & Folder.HOLDS_FOLDERS) != 0) {
                Folder[] mailFolders = rootFolder.list();

                // create our new list of MailFolderBean
                iceMailFolders = new ArrayList(mailFolders.length);
                MailFolderBean tmpMailFolder;
                for (int i = 0; i < mailFolders.length; i++) {
                    tmpMailFolder = new MailFolderBean();
                    tmpMailFolder.setWebmailMediator(
                            mailAccount.getMailManager().getMediator());
                    tmpMailFolder.setFolder(mailFolders[i]);
                    tmpMailFolder.setMailAccount(mailAccount);
                    iceMailFolders.add(tmpMailFolder);
                }
            }
        } catch (IllegalStateException e) {
            log.error("IllegalStateException on list method " + mailAccount.toString());
        } catch (NoSuchProviderException e) {
            log.error("NoSuchProviderException on list method " + mailAccount.toString());
        } catch (AuthenticationFailedException e) {
            log.error("AuthenticationFailedException on list method " + mailAccount.toString());
        } catch (MessagingException e) {
            log.error("Messaging Exception on list method " + mailAccount.toString());
        }
        return iceMailFolders;
    }

    /**
     * Create a new message bean which contains a MimeMessage package.
     *
     * @return a new MessageBean
     */
    public MessageBean createNewMessage() {
        // make sure we are connected
        connect();
        if (mailSession != null) {
            MessageBean message = new MessageBean();
            // set message first
            message.setMessage(new MimeMessage(mailSession));
            // apply a default sender and subject
            message.setSender(mailAccount.getEmail());
            message.setSubject("");
            message.setParentAccount(mailAccount);
            return message;
        } else {
            return null;
        }
    }

    /**
     * Create a new message from the specified message containing the origonal
     * message content are ready formated in a forwarded message format.
     *
     * @param message message to be used to create a pre-formated forward message
     */
    public MessageBean createForwardMessage(MessageBean message) {

        // create the new forward message
        MessageBean forwardMessage = new MessageBean();
        forwardMessage.setMessage(new MimeMessage(mailSession));

        // set sender
        forwardMessage.setSender(mailAccount.getEmail());

        // copy subject
        forwardMessage.setSubject("Fwd: " + message.getSubject());

        // setup message content
        StringBuffer localBody = new StringBuffer();
        localBody.append("\n\n\n---------- Forwarded message ----------");
        localBody.append("\nFrom: ").append(message.getSender());
        localBody.append("\nDate: ").append(message.getSendDateString());
        localBody.append("\nSubject: ").append(message.getSubject());
        localBody.append("\n\n\n");

        // strip <br>'s from html view
        String bodyText = message.getPlainTextContent();
        bodyText = bodyText.replaceAll("<br/>", "\n");

        // finally assemble message and set new message content
        localBody.append(bodyText);
        forwardMessage.setPlainTextContent(localBody.toString());

        return forwardMessage;
    }

    /**
     * Create a new message from the specified message containing the origonal
     * message content are ready formated in a reply message format.
     *
     * @param message message to be used to create a pre-formated reply message
     */
    public MessageBean createReplyMessage(MessageBean message) {

        // create the new forward message
        MessageBean replyMessageBean = new MessageBean();

        // Get an formated reply message.
        try {
            // create simple reply
            MimeMessage replyMessage = (MimeMessage) message.getMessage().reply(false);
            replyMessageBean.setMessage(replyMessage);
            replyMessageBean.setSender(mailAccount.getEmail());

        } catch (MessagingException e) {
            if (log.isDebugEnabled())
                log.debug("reply message, could not set recipents. ");
        }

        // setup message content
        StringBuffer localBody = new StringBuffer();
        localBody.append("\n\n\n---------- Original message ----------");
        localBody.append("\nFrom: ").append(message.getSender());
        localBody.append("\nDate: ").append(message.getSendDateString());
        localBody.append("\nSubject: ").append(message.getSubject());
        localBody.append("\n\n\n");
        // strip <br>'s from html view
        String bodyText = message.getPlainTextContent();
        bodyText = bodyText.replaceAll("<br/>", "\n");

        // finally assemble message and set new message content
        localBody.append(bodyText);
        replyMessageBean.setPlainTextContent(localBody.toString());


        replyMessageBean.updateRecipients(true);

        return replyMessageBean;
    }

    /**
     * Create a new message from the specified message containing the origonal
     * message content are ready formated in a reply all message format.
     *
     * @param message message to be used to create a pre-formated reply to all message
     */
    public MessageBean createReplyAllMessage(MessageBean message) {

        // create the new forward message
        MessageBean replyAllMessageBean = new MessageBean();

        // Get an formated reply message.
        try {
            // reply to all
            MimeMessage replyMessage = (MimeMessage) message.getMessage().reply(true);
            replyAllMessageBean.setMessage(replyMessage);
            replyAllMessageBean.setSender(mailAccount.getEmail());

        } catch (MessagingException e) {
            if (log.isDebugEnabled())
                log.debug("reply all message, could not set recipents. ");
        }

        // setup message content
        StringBuffer localBody = new StringBuffer();
        localBody.append("\n\n\n---------- Original message ----------");
        localBody.append("\nFrom: ").append(message.getSender());
        localBody.append("\nDate: ").append(message.getSendDateString());
        localBody.append("\nSubject: ").append(message.getSubject());
        localBody.append("\n\n\n");
        // strip <br>'s from html view
        String bodyText = message.getPlainTextContent();
        bodyText = bodyText.replaceAll("<br/>", "\n");

        // finally assemble message and set new message content
        localBody.append(bodyText);
        replyAllMessageBean.setPlainTextContent(localBody.toString());

        replyAllMessageBean.updateRecipients(false);
        return replyAllMessageBean;
    }

    /**
     * Deletes the messages specified by <code>messages</code>.  Deletion is
     * carried out by setting the message flag to DELETED.  When the folder is
     * closed the message will be exponged.
     *
     * @param messages messages to delete, must all be of type Message Bean
     *                 or of type javax.mail.Message.
     * @param exponge  true to imediatly exponse delete items; otherwise, false.
     * @return true if delete was successful; otherwise, false.
     */
    public boolean deleteMessage(Object[] messages, boolean exponge) {

        if (log.isDebugEnabled()) {
            log.debug("Delete Messages ");
        }

        MailFolderBean folder = mailAccount.getSelectedMailFolder();

        if (folder != null) {
            try {

                // first, remove the message from the view as changing the
                // flags is going to cause event to fire and thus possible renders
                folder.removeMessage(messages);

                // make sure that folder is in read mode
                if (!folder.isOpen()) {
                    folder.open(Folder.READ_WRITE);
                }
                // set messages flag to delete, if the folder is closed or exponged
                // the messages will be removed
                for (int i = messages.length - 1; i >= 0; i--) {
                    // set the delete flag
                    if (messages[i] != null &&
                            messages[i] instanceof MessageBean &&
                            !((MessageBean) messages[i]).getMessage().isExpunged()) {
                        ((MessageBean) messages[i]).getMessage().setFlag(
                                Flags.Flag.DELETED, true);
                    }
                    else if (messages[i] != null &&
                            messages[i] instanceof javax.mail.Message &&
                             !((javax.mail.Message) messages[i]).isExpunged()) {
                        ((javax.mail.Message) messages[i]).setFlag(
                                Flags.Flag.DELETED, true);
                    }
                }

                // remove delete messages imediately
                if (exponge) {
                    folder.expunge();
                }

            } catch (MessagingException e) {
                log.error("Error deleting messages ", e);
                return false;
            }
        } else {
            log.error("null selected folder");
        }
        return true;
    }

    /**
     * Sends the given MessageBean using this controllers mail account.
     *
     * @param message message to send
     * @return true if the message was send with out errors; otherwise, false.
     */
    public boolean sendMessage(MessageBean message) {
        if (log.isDebugEnabled())
            log.debug("Sending Message ");

        if (message.getMessage() != null) {
            try {
                connect();
                Transport transport;
                // check if SSL is needed
                if (mailAccount.isOutgoingSsl()) {
                    transport = mailSession.getTransport("smtps");
                } else {
                    transport = mailSession.getTransport("smtp");
                }
                // check if username and password authentication is needed
                if (mailAccount.isOutgoingVerification()) {
                    transport.connect(
                            mailAccount.getOutgoingHost(),
                            mailAccount.getOutgoingPort(),
                            mailAccount.getMailUsername(),
                            mailAccount.getPassword());
                } else {
                    transport.connect(
                            mailAccount.getOutgoingHost(),
                            mailAccount.getOutgoingPort(),
                            null, null);
                }

                // try and send the message
                if (transport.isConnected() &&
                        message.getSender() != null &&
                        message.getMessage() != null) {

                    // add a time stamp
                    message.getMessage().setSentDate(new Date());

                    // send the message.
                    transport.sendMessage(message.getMessage(),
                            message.getMessage().getAllRecipients());

                } else if (log.isDebugEnabled()) {
                    log.debug("Could not send message, null message");
                }

            } catch (SendFailedException e) {
                if (log.isDebugEnabled()){
                    log.debug("Message send failed " , e);
                    log.debug("To: " + message.getRecipientsTo());
                    log.debug("Bcc: " + message.getRecipientsBcc());
                    log.debug("Cc: " + message.getRecipientsCc());
                }
                return false;
            } catch (MessagingException e) {
                if (log.isDebugEnabled()){
                    log.debug("Message was not sent correctly ");
                }
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Saves the messsage specified by <code>message</code>.   Mime message
     * are read-only and thus a copy must be made to re-save a message.  An
     * applications should only limit this feature to the drafts folder for
     * read-only messages.
     *
     * @param message message to save contents of.
     * @return true if the save was succesful; otherwise, false.
     */
    public boolean saveMesssage(MessageBean message) {
        if (message.getMessage() != null) {
            try {
                // if the message does not have a folder then we know it is new
                // and that it can be saved.
                if (message.getMessage().getFolder() == null) {
                    message.getMessage().setFlag(Flags.Flag.DRAFT, true);
                    message.getMessage().setFlag(Flags.Flag.SEEN, true);
                    message.getMessage().saveChanges();
                    return true;
                }
                // otherwise we have a saved message and we will have to make
                // a copy in order to save it.
                else {
                    if (log.isDebugEnabled()) {
                        log.debug("Message could not be saved, read only Mime Message ");
                    }
                    return false;
                }

            } catch (IllegalWriteException e) {
                log.error("Message could not be saved, write exception ");
            } catch (MessagingException e) {
                log.error("Message could not be saved, messaging error");
            }
        }
        return false;
    }

    /**
     * Appends the given message to the the specified folder.  If the named
     * folder can not be found an error message is logged.
     * @param message
     * @param folderName
     */
    public void appendMessage(MessageBean message, String folderName){
        // find folder
        MailFolderBean destFolder = findFolder(folderName);

        if (destFolder != null){
            try {
                destFolder.getFolder().appendMessages(
                        new javax.mail.Message[]{message.getMessage()});
            }
            catch (MessagingException e) {
                log.error("Error appending message to folder " + folderName);
            }
            tickleMailAccount(destFolder);
        }
    }

    /**
     * Copies the specified messageBean returning a copy of the origional messageBean.
     * This should only be called when a users needed to edit a mime messageBean.
     *
     * @param messageBean messageBean to copy
     * @return new messageBean which contains the same data as the origional
     */
    public MessageBean copyMessage(MessageBean messageBean) {
        // create the new messageBean bean
        try {
            // copy the messageBean bundle
            MessageBean copyMessageBean = new MessageBean();
            MimeMessage copyMessage = new MimeMessage(messageBean.getMessage());
            copyMessageBean.setMessage(copyMessage);
            // copy extra view data
            copyMessageBean.setMessageSenderAccount(messageBean.getMessageSenderAccount());
            copyMessageBean.setParentFolder(messageBean.getParentFolder());
            copyMessageBean.setSavedMessage(messageBean.getSavedMessage());


            // copy recipients from MessageBean to the copied Message
            List recipients = messageBean.getRecipientTable();
            List copiedRecipients = new ArrayList(recipients.size());
            TableEntry tmpTableEntry;
            for (int i= 0, max = recipients.size(); i < max; i++){
                // copy recipients
                tmpTableEntry = (TableEntry)recipients.get(i);
                copiedRecipients.add(i,
                        new TableEntry(tmpTableEntry.getType(),
                                       tmpTableEntry.getValue() + ""));
            }
            copyMessageBean.setRecipientTable(copiedRecipients);

            return copyMessageBean;
        }
        catch (MessagingException e) {
            log.error("Error copying messageBean");
        }

        return null;
    }

    /**
     * Moves the specified messages from the from folder name to the destination
     * folder name.
     *
     * @param messages       messages to move
     * @param fromFolderName location of messages
     * @param destFolderName destination for messages
     * @return true if the move was successful; otherwise, false.
     */
    public boolean moveMessages(Object[] messages,
                                String fromFolderName,
                                String destFolderName) {
        MailFolderBean fromFolder = findFolder(fromFolderName);
        MailFolderBean destFolder = findFolder(destFolderName);

        return moveMessages(messages, fromFolder, destFolder);
    }

    /**
     * Move the specified messages from this folder, to the specified destination.
     *
     * @param messages   the array of message objects
     * @param fromFolder he folder to copy the messages from.
     * @param destFolder he folder to copy the messages to.
     */
    public boolean moveMessages(Object[] messages,
                                MailFolderBean fromFolder,
                                MailFolderBean destFolder) {

        try {
            // Return if no folder was found
            if ((fromFolder == null && destFolder == null) ||
                    (fromFolder != null && fromFolder.getFolder() != null &&
                            !fromFolder.getFolder().exists()) ||
                    (fromFolder != null && fromFolder.getFolder() != null &&
                            !fromFolder.getFolder().exists())) {
                if (log.isDebugEnabled()) {
                    log.debug("Can not move messages in their current state");
                }
                return false;
            }

            if (log.isDebugEnabled()) {
                if (fromFolder != null && destFolder != null) {
                    log.debug("Moving messages " + fromFolder.getFullName() +
                            " to " + destFolder.getFullName());
                }
            }

            // initiate the move, move then delete the old.
            if (destFolder != null && fromFolder != null) {

                // return a false move if the dest and from folder are the same.
                if (destFolder.equals(fromFolder)){
                    return false;
                }

                // Open the from folder if needed, destination is not needed by copyMessages
                if (!fromFolder.getFolder().isOpen())
                    fromFolder.open(Folder.READ_WRITE);

                // build messages array
                javax.mail.Message[] rawMessages = new javax.mail.Message[messages.length];
                for (int i = 0, max = rawMessages.length; i < max; i++) {
                    rawMessages[i] = ((MessageBean) messages[i]).getMessage();
                }

                // remove the message from the folder bean, the following mail api
                // calls are going to fire a lot of events which may mess up the
                // view temporarely
                fromFolder.removeMessage(rawMessages);

                // Copy MimeMessages into destination folder
                fromFolder.getFolder().copyMessages(rawMessages, destFolder.getFolder());

                // marke the current as deleteable.
                fromFolder.getFolder().setFlags(
                        rawMessages,
                        new Flags(Flags.Flag.DELETED), true);

                // exponge the folders
                fromFolder.expunge();

                // update parent folder callback
                for (int i = messages.length - 1; i >= 0; i--) {
                    ((MessageBean) messages[i]).setParentFolder(destFolder);
                }
            } else if (log.isDebugEnabled()) {
                log.debug("Can not move messages in their current state, null folder state ");
            }

            // trigger messages change events.
            tickleMailAccount(destFolder);
            tickleMailAccount(fromFolder);

            return true;

        } catch (MessagingException e) {
            log.error("Error Moving multiple messages failed to " + destFolder.getFullName());
        }

        return false;
    }


    /**
     * Moves the specified message to the specified folder.
     *
     * @param message        message to move
     * @param destFolderName folder to move message to.
     * @return true if move was successful; otherwise, false.
     */
    public boolean moveMessage(MessageBean message, String destFolderName) {
        // find draft folder and attempt a move
        MailFolderBean destfolder = findFolder(destFolderName);
        // move to message to the specified folder
        if (destfolder != null) {
            boolean success = moveMessage(message, destfolder);
            if (success) {
                message.setParentFolder(destfolder);
            }
            return success;
        }
        return false;
    }

    /**
     * Utility method to get the folder object for the specified name.
     *
     * @param folderName name of folder to find
     * @return mailfolder object corresponding to name; otherwise, null if no
     *         folder can be found.
     */
    public MailFolderBean findFolder(String folderName) {
        // find draft folder and attempt a move
        ArrayList folders = getFolderList();
        if (folders != null) {
            MailFolderBean destfolder = null;
            MailFolderBean tmpFolder;
            // find draft folder.
            for (int i = 0; i < folders.size(); i++) {
                tmpFolder = (MailFolderBean) folders.get(i);
                if (tmpFolder.getFolder().getName().equals(folderName)) {
                    destfolder = tmpFolder;
                    break;
                }
            }
            // move to message to the specified folder
            if (destfolder != null) {
                return destfolder;
            }
        }
        return null;
    }

    /**
     * Move the specified messages to the specified destination.
     *
     * @param message    an unsaved message object, a new message.
     * @param destfolder the folder wher the new message is to be saved.
     */
    public boolean moveMessage(MessageBean message, MailFolderBean destfolder) {

        // Return if no folder was found
        if (destfolder == null) {
            if (log.isDebugEnabled()) {
                log.debug("Move error - Null destination folder, now where to move");
            }
            return false;
        }

        try {
            // Open the folder if needed
            if (!destfolder.isOpen()) {
                destfolder.open(Folder.READ_WRITE);
            }

            Folder folder = message.getMessage().getFolder();
            // if there is a folder then we are copying then delete the message
            if (folder != null) {
                if (log.isDebugEnabled())
                    log.debug("Moving message with folder, copy, delete...");
                // Copy MimeMessages into destination folder
                folder.copyMessages(
                        new javax.mail.Message[]{message.getMessage()},
                        destfolder.getFolder());
                // marke the current as deleteable.
                folder.setFlags(
                        new javax.mail.Message[]{message.getMessage()},
                        new Flags(Flags.Flag.DELETED), true);

                // exponge the folder
                folder.expunge();

                // update parent folder callback
                message.setParentFolder(destfolder);
            }
            // if the message has no folder then we can append it to the destination
            else {
                if (log.isDebugEnabled())
                    log.debug("Moving message with null Folder, appending...");
                destfolder.getFolder().appendMessages(
                        new MimeMessage[]{message.getMessage()});
                // update parent folder callback
                message.setParentFolder(destfolder);
            }
            // trigger messages change events.
            tickleMailAccount(destfolder);

            return true;

        } catch (MessagingException e) {
            log.error("Moving single message failed ", e);
        }
        return false;
    }
}
