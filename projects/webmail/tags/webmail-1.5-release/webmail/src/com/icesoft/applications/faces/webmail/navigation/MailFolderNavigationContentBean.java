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
package com.icesoft.applications.faces.webmail.navigation;

import com.icesoft.applications.faces.webmail.WebmailMediator;
import com.icesoft.applications.faces.webmail.mail.*;
import com.icesoft.faces.component.dragdrop.DndEvent;
import com.icesoft.faces.component.dragdrop.DropEvent;
import com.icesoft.faces.context.effects.Effect;

import javax.faces.event.ActionEvent;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageChangedEvent;



/**
 * This is just a wrapper for a Folder bean so that it can be managed
 * in the context of the NavigationManager for displaying content.
 *
 * @since 1.0
 */
public class MailFolderNavigationContentBean
        extends NavigationContent
        implements MessageCountListener, MessageChangedListener {

    // stores a pointer to mailFolder associated with this navigation bean.
    private MailFolderBean mailFolder;

    // reference to mediator used to assess the render manager. 
    private WebmailMediator webmailMediator;

    // unread message count
    private int unreadMessageCount = 0;

    /**
     * Default constructor, set default leaf icon to an inactive envelop. 
     */
    public MailFolderNavigationContentBean() {
        super();
        setLeaf(true);
        setLeafIcon("images/envelop_inactive.gif");
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
     * Gets the mail folder associated with this navigation bean.
     *
     * @return mail forder if assing; otherwise, null.
     */
    public MailFolderBean getMailFolder() {
        return mailFolder;
    }

    /**
     * Disposes this  mail folder content bean.  All folder messag listeners
     * are removed at this point.
     */
    public void dispose(){
        // we don't want multiple listners, so we do a clean up if needed
        if (this.mailFolder != null) {
            this.mailFolder.getFolder().removeMessageCountListener(this);
            this.mailFolder.getFolder().removeMessageChangedListener(this);
        }
    }

    /**
     * Sets the mail folder associated with this navigaiton bean.
     *
     * @param mailFolder folder to assign.
     */
    public void setMailFolder(MailFolderBean mailFolder) {
        // we don't want multiple listners, so we do a clean up if needed
        if (this.mailFolder != null) {
            this.mailFolder.getFolder().removeMessageCountListener(this);
            this.mailFolder.getFolder().removeMessageChangedListener(this);
        }
        // assign the new folder
        this.mailFolder = mailFolder;

        // get the message count data
        updateUnreadMessageCount();

        // lastly add the new listeners
        if (this.mailFolder != null) {
            this.mailFolder.getFolder().addMessageCountListener(this);
            this.mailFolder.getFolder().addMessageChangedListener(this);
        }
    }

    /**
     * Gets the label for this menu item.  New message count information maybe
     * incorporated in this release.
     * @return name of mail folder.
     */
    public String getMenuDisplayText() {

        if (mailFolder != null) {

            // Finially return the correct label.
            if (unreadMessageCount == 0) {
                return super.getMenuDisplayText();
            }
            else {
                return super.getMenuDisplayText() +
                        " <b>(" + unreadMessageCount + ")</b>";
            }
        }
        else {
            return super.getMenuDisplayText();
        }
    }

    /**
     * Sets the navigationSelctionBeans selected state to this NavigationContent
     */
    public void contentVisibleAction(ActionEvent event) {
        // refresh mail message list
        mailFolder.init();

        // we want to set the MailManger selectedAccount state.
        MailAccountBean mailAccount = mailFolder.getMailAccount();
        MailManager mailManager = mailAccount.getMailManager();

        // set selected fold and bean.
        mailManager.setSelectedMailAccount(mailAccount);
        mailAccount.setSelectedMailFolder(mailFolder);

        // tickel the mail account to make sure the view is accurate
        mailAccount.getMailControl().tickleMailAccount(mailFolder);

        // lastly remove any exponged message
        mailFolder.removeExpunged();

        // Hilight this panel as selected by using a "brighter" icon
        setBranchContractedIcon("images/folderclosed.gif");
        setBranchExpandedIcon("images/folderopen.gif");
        setLeafIcon("images/envelop_active.gif");

        // set navigation selected panel
        super.contentVisibleAction(event);
    }

    /**
     * Restores the default icons for this ICEUserNod
     */
    public void restoreDefaultIcons() {
        setBranchContractedIcon("images/folderclosed.gif");
        setBranchExpandedIcon("images/folderopen.gif");
        setLeafIcon("images/envelop_inactive.gif");
    }


    /**
     * Drop action that moves the message around
     */
    public void navigationDropAction(DropEvent event) {
        if (mailFolder != null &&
                 // get objects from drop event
                event.getEventType() == DndEvent.DROPPED &&
                event.getTargetDragValue() != null) {

            Object targetType = event.getTargetDragValue();
            // make sure we are getting a message bean
            if (targetType instanceof MessageBean){
                // get move handlers
                MailAccountBean mailAccount = mailFolder.getMailAccount();
                MailAccountControl mailControl = mailAccount.getMailControl();

                // make the move official.
                MessageBean droppedMsg = (MessageBean) targetType;

                // check to make sure the email is from this account, if not
                // we don't want to initiate a move.
                if (!mailAccount.equals(droppedMsg.getParentAccount())){
                    currentEffect = dropFailureEffect;
                    currentEffect.setFired(false);
                    return;
                }

                boolean moveSuccess = mailControl.moveMessages(new Object[]{droppedMsg},
                        droppedMsg.getParentFolder(), mailFolder);

                if (moveSuccess){
                    // assign effect
                    currentEffect = dropSuccessEffect;
                }
                else{
                    currentEffect = dropFailureEffect;
                }
            }
            // show the error effect otherwise.
            else{
                currentEffect = dropFailureEffect;
            }
            currentEffect.setFired(false);
        }
    }

    /**
     * Message has been added to folder.  All we want to do here is update the
     * folder label as the unread message count may have been updated.
     *
     * @param messageCountEvent
     */
    public void messagesAdded(MessageCountEvent messageCountEvent) {
        // update unread message count
        updateUnreadMessageCount();

        // Ask the RenderManager for a render
        webmailMediator.requestOnDemandRender();
    }

    /**
     * Updates the new message count on the label of this folder Item.
     * @param messageCountEvent
     */
    public void messagesRemoved(MessageCountEvent messageCountEvent) {
        // update unread message count
        updateUnreadMessageCount();

        // Ask the RenderManager for a render
        webmailMediator.requestOnDemandRender();
    }

    /**
     *
     * @param messageChangedEvent
     */
    public void messageChanged(MessageChangedEvent messageChangedEvent) {

        // update unread message count
        if (messageChangedEvent.getMessageChangeType() ==
                        MessageChangedEvent.FLAGS_CHANGED){
            updateUnreadMessageCount();
        }
        else if (messageChangedEvent.getMessageChangeType() ==
                        MessageChangedEvent.ENVELOPE_CHANGED){
//            updateUnreadMessageCount();
        }
        // Ask the RenderManager for a render
        webmailMediator.requestOnDemandRender();
    }

    /**
     * Utility method for updating the unread message count.
     */
    private void updateUnreadMessageCount(){
        // Update the unreadMessageCount, this is expensive so we only want
        // to do it when we have to as it is expensive.
        if (mailFolder != null && mailFolder.getFolder() != null){
            int count = mailFolder.getUnreadMessageCount();
            if (count > 0){
                unreadMessageCount = count;
            }
            else{
                unreadMessageCount = 0;
            }
        }
    }

    /**
     * Gets the drop effect associated with this navigation node.
     * @return effect if any, null otherwise.
     */
    public Effect getDropEffect(){
        return currentEffect;
    }
}
