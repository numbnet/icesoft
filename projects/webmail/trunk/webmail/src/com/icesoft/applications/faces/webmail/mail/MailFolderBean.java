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

import com.icesoft.applications.faces.webmail.WebmailBase;
import com.icesoft.applications.faces.webmail.WebmailMediator;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Flags;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageCountListener;
import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * <p>The MailFolderBean wraps a IMAPFolder and listens for MessageChanged and
 * message count events.  These events are very import as they will reflect any
 * changes to the state of the folder.</p>
 * <p/>
 * <p>Because MailFolderBean is a wrapper for IMAPFolder needed IMAPFolder methods
 * must be implemented as needed.  The urge to simply return a IMAPFolder should
 * be avoided as it could cause a potential state difference between the two message
 * lists.</p>
 *
 * @since 1.0
 */
public class MailFolderBean extends MailFolder
        implements WebmailBase, MessageChangedListener, MessageCountListener {

    // checked count, releated to folder view UI.
    private int checkedCount = 0;

    // reference to mediator used to assess the render manager.
    private WebmailMediator webmailMediator;

    // initilization flag
    private boolean isInit;

    /**
      * Field for enabling/disabling multiple message selection in email list table.
      * Placeholder for future enhancement which allows user to make choice between 
      * one or multiple message selections
      */	
    public boolean multipleSelection = true;
    
    /**
     * Default constructor for a new folder bean.
     * @param folderName alternative name for folder wrapper.  Will only be used
     * if the wrapped folder object is null.
     */
    public MailFolderBean(String folderName) {
        super(folderName);
    }

    /**
     * Default constructor for a folder bean.
     */
    public MailFolderBean(){
        super();
    }

    /**
     * Sets a WebmailMediator callback, so that this class can request a render
     * on message change events as well contact changes.
     *
     * @param webmailMediator
     */
    public void setWebmailMediator(WebmailMediator webmailMediator){
        this.webmailMediator = webmailMediator;
    }

    /**
     * Utility method used to initilize this folder.  That is the Folder
     * messages are read and stored in the messageList
     */
    public void init() {
        if (!isInit){
            isInit = true;
            // read messages for the first time,
            refreshIceMessageList();
        }
    }

    /**
     * Sets the folder in which this Folder bean wraps.
     *
     * @param folder JavaMail folder to wrap
     */
    public void setFolder(Folder folder) {
        // remove folder listeners
        if (this.folder != null){
            this.folder.removeMessageChangedListener(this);
            this.folder.removeMessageCountListener(this);
        }
        super.setFolder(folder);

        // add the new folder listener
        if (this.folder != null){
            this.folder.addMessageChangedListener(this);
            this.folder.addMessageCountListener(this);
        }
    }

    /**
     * Deselects the all MessageBeans in this folder except the excludedBean.
     *
     * @param excludedBean message to exculde from the deselction.
     */
    public void deselecteMessageFlags(MessageBean excludedBean) {
        // deselect messages
        MessageBean tmpMessage;
        checkedCount = 0;
        for (int i = 0; i < messageList.size(); i++) {
            tmpMessage = (MessageBean) messageList.get(i);
            if (!tmpMessage.equals(excludedBean)) {
                tmpMessage.setSelected(false);
            }
        }
    }

    /**
     * Adds the specified messages to the messageList.  The message are
     * wrapped by MessageBeans before they are added to the list.  This method
     * should only be called when handling MessageCountEvents.  In such a caces
     * the message are already part of the MimeForlder but have not yet been
     * wrapped by the MessageBean and thus can not be displayed.
     *
     * @param messages messages to at to this folder.
     */
    public void addMessage(javax.mail.Message[] messages) {
        if (messageList == null) {
            messageList = new ArrayList();
        }

        if (messages != null) {
            MimeMessage message;
            for (int i = messages.length - 1; i >= 0; i--) {
                message = (MimeMessage) messages[i];
                if (!message.isExpunged() &&
                        // make sure we didn't allready add it.
                        findMessage(message) == null){
                    // init a new MessageBean
                    MessageBean tmpMessage = new MessageBean();
                    tmpMessage.setFolderCallback(this);
                    tmpMessage.setParentAccount(mailAccount);
                    tmpMessage.setMessage(message);
                    // one more check for expunged
                    if (!message.isExpunged())
                        messageList.add(tmpMessage);
                }
            }
        }
    }

    /**
     * Get total number of messages in this Folder. Note that getting the
     * total message count can be an expensive operation involving actually
     * opening the folder.
     *
     * @return total number of messages. -1 may be returned if an Messaging
     *          Exception is encountered.
     */
    public int getUnreadMessageCount(){

        if (folder != null){
            // make sure the folder is open
            if(!isOpen()){
                open(Folder.READ_WRITE);
            }

            int unreadCount = 0;
            javax.mail.Message message;
            try{
                Message[] messages = folder.getMessages();
                for (int i = messages.length -1; i >= 0; i--){
                    message = messages[i];
                    if (message != null &&
                            !message.isExpunged() &&
                            !message.isSet(Flags.Flag.SEEN)){
                        unreadCount++;
                    }
                }
            }catch(MessagingException e){
                log.error("Error getting unread message count ", e);
            }
            return unreadCount;
        }
        return -1;
    }

    /**
     * Removes the specified messages from the messageList.  This method should
     * only be called by a MessageCountEvents handler and as a result the message
     * have already been removed from the MimeFolder, all we want to do is make
     * sure that the respective MessageBeans are removed as well.
     *
     * @param messages message that are to be removed.
     */
    public void removeMessage(Object[] messages) {
        if (messageList == null) {
            messageList = new ArrayList();
        }

        if (messages != null) {
            Message message;
            Object tmpMessage;
            MessageBean foundMessage;
            // loop threw message to delete.
            for (int i = messages.length - 1; i >= 0; i--) {
                tmpMessage = messages[i];
                if (tmpMessage instanceof javax.mail.Message){
                    message = (javax.mail.Message) tmpMessage;
                    // find the messagebean in the message list that wraps the
                    // delete mime message
                    foundMessage = findMessage(message);
                    if (foundMessage != null){
                        // find the correct index and then remove it if found.
                        int index = messageList.indexOf(foundMessage);
                        if (index >= 0){
                            messageList.remove(index);
                        }
                    }
                }
                else if (tmpMessage instanceof MessageBean){
                    int index = messageList.indexOf(tmpMessage);
                    if (index >= 0){
                        messageList.remove(index);
                    }
                }

            }
        }
    }

    /**
     * Utility method to removed any expunged message from message list.  This
     * ensures partially delete emssage get into the message list.
     */
    public void removeExpunged(){
        if (messageList != null){
            // make sure the folder is open
            if(!isOpen()){
                open(Folder.READ_WRITE);
            }

            MessageBean tmpMessage;
            for (int i = messageList.size() -1; i >= 0; i--){
                tmpMessage = (MessageBean) messageList.get(i);
                if (tmpMessage.getMessage() == null ||
                        tmpMessage.getMessage().isExpunged() ||
                        !tmpMessage.getParentFolder().equals(this)){
                    messageList.remove(i);
                }
            }
        }
    }

    /**
     * Refreshed the messageList content.
     */
    private void refreshIceMessageList() {

        try {
            // check for a null folder, this should only happen on searches
            // if it happens we don't want to do any refreshing, as the message
            // arn't actually in a folder
            if (folder == null) {
                return;
            }

            // open the folder in read write mode.
            if (!isOpen()) {
                open(Folder.READ_WRITE);
            }

            // create a new array list holding messages that can be sorted
            Message[] messages = folder.getMessages();

            // clear or create the message list
            if (messageList != null) {
                messageList.clear();
            } else {
                messageList = new ArrayList(messages.length);
            }

            // copy messages and update message folder stats.
            MessageBean tmpMessage;
            for (int i = 0; i < messages.length; i++) {
                if (messages[i] != null ) {  //&& !messages[i].isExpunged()
                    tmpMessage = new MessageBean();
                    tmpMessage.setFolderCallback(this);
                    tmpMessage.setParentAccount(mailAccount);
                    tmpMessage.setMessage((MimeMessage) messages[i]);
                    messageList.add(tmpMessage);
                }
            }

        } catch (MessagingException e) {
            log.error("Error reading messages ", e);
        }
    }

    /**
     * Disposed this Mail folder beans resources.  The folder is also exponged on
     * closure.
     */
    public void dispose() {

        // remove folder listeners
        if (this.folder != null){
            this.folder.removeMessageChangedListener(this);
            this.folder.removeMessageCountListener(this);
        }

        // close this folder, and exponges it.
        close(true);

        // clear the message list
        if (messageList != null) {
            messageList.clear();
            messageList = null;
        }
    }

      /**
     * Listener method called when a message on the page is selected
     * The method will update the current selected message count
     */
   public void rowSelection(RowSelectorEvent event) {
        // Maintain a count of selected messages
        if (event.isSelected()) {
            checkedCount++;
        } else {
            checkedCount--;
        }
    }

    /**
     * Selecte all message in the folder.
     * @param event
     */
    public void changeSelectAll(ValueChangeEvent event) {

        if (Boolean.valueOf(event.getNewValue().toString()).booleanValue()){
            selectAll(null);
        }
        else{
            deselectAll(null);
        }
    }

    /**
     * Gets the check count of the message in this folder.  This is used to
     * enable/disble the state of command buttons.
     *
     * @return number of check messages.
     */
    public int getCheckedCount() {
        return checkedCount;
    }

    /**
     * select all messages in the table
     *
     * @param event
     */
    public void selectAll(ActionEvent event) {
        checkedCount = 0;
        for (int i = 0; i < this.messageList.size(); i++) {
            if (!((MessageBean) messageList.get(i)).isSelected()) {
                ((MessageBean) messageList.get(i)).setSelected(true);
                checkedCount++;
            }
        }
    }

    /**
     * deselect all messages in the table
     * @param event
     */
    public void deselectAll(ActionEvent event){
    	checkedCount = 0;
        for (int i = 0; i < this.messageList.size(); i++) {
    		if (((MessageBean) messageList.get(i)).isSelected()) {
                ((MessageBean) messageList.get(i)).setSelected(false);
            }
    	}
    }

    /**
     * Utility mehtod to find a MessageBean object that wraps the specfied
     * message.
     * @param message message to search for.
     * @return found MessageBean object if any, null otherwise.
     */
    public MessageBean findMessage(Message message) {

        if (message == null)
            return null;

        if (!isOpen()) {
            open(Folder.READ_WRITE);
        }

        // search for the msesage.
        MessageBean tmpMessage;
        for (int i = this.messageList.size() - 1; i >= 0 ; i--) {
            tmpMessage = (MessageBean) messageList.get(i);
            // check for a matching message object
            if (message.equals(tmpMessage.getMessage())){
                return tmpMessage;
            }
        }
        return null;
    }

    /**
     * Invoked when a message is changed.  The messageChangedEvent holds
     * the message that has been changed, this method must update respective
     * message in the messageList.
     */
    public void messageChanged(MessageChangedEvent messageChangedEvent) {
        // message type references
        //     Flag.ANSWERED - usually an arrow on the message icon
        //     Flag.DELETED - change display text to strikeout
        //     Flag.SEEN - change display text to non-bold
        //     Flag.Draft - change display ice to have a pencil?

        if (messageChangedEvent != null) {

            // currnetly only concerned with Flag changes.
            if (messageChangedEvent.getMessageChangeType()
                    == MessageChangedEvent.FLAGS_CHANGED ){

                // get the message that's status has chaned.
                Message changedMessage = messageChangedEvent.getMessage();

                // find the MessageBean that wraps this mime message.
                MessageBean foundMessage = findMessage(changedMessage);

                // if we found the message we want to update the flags.
                if (foundMessage != null){
                    try {
                        foundMessage.setMessageFlag(changedMessage.getFlags());
                    } catch (MessagingException e) {
                        log.error("Error getting message flags");
                    }
                }
            }

            // Ask the RenderManager for a render
//            webmailMediator.requestOnDemandRender();
        }
    }

    /**
     * Invoked when messages are added into a folder.  The internal list of
     * ICEMessagesBeans must be updated to reflect this change.
     */
    public void messagesAdded(MessageCountEvent messageCountEvent) {
        // add new messages to this folder
        if (messageCountEvent != null &&
                messageCountEvent.getMessages() != null) {
            addMessage(messageCountEvent.getMessages());
            // resort list
            sort(getSort(), isAscending());
        }

        // The class MailFolderNavigationContentBean also listens for MessageCountEvents
        // we will let this class handle the render requests.

        // Ask the RenderManager for a render
//        webmailMediator.requestOnDemandRender();
    }

    /**
     * Invoked when messages are removed (expunged) from a folder. The internal
     * list of ICEMessagesBeans must be updated to reflect this change.
     */
    public void messagesRemoved(MessageCountEvent messageCountEvent) {
        // remove new message from this folder.
        if (messageCountEvent != null &&
                messageCountEvent.getMessages() != null) {
            removeMessage(messageCountEvent.getMessages());
            // resort list
            sort(getSort(), isAscending());
        }

        // The class MailFolderNavigationContentBean also listens for MessageCountEvents
        // we will let this class handle the render requests.

        // Ask the RenderManager for a render
//        webmailMediator.requestOnDemandRender();
    }

    /**
     * Is multiple message selection enabled in the email list table?
     * @return true if multiple selection is enabled
     */

    public boolean isMultipleSelection() {
        return multipleSelection;
    }

    /**
     * Sets the state of multiple message selection.
     *
     * @param multipleSelection
     */

    public void setMultipleSelection(boolean multipleSelection) {
        this.multipleSelection = multipleSelection;
    }

}
