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

import com.icesoft.applications.faces.webmail.common.SortableList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.Folder;
import javax.mail.FolderNotFoundException;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * <p>The <code>MailFolder</code> class is responsible for wrapping a JavaMail
 * folder object and storing extra data related to the <code>MailFolderBean</code>
 * view.  This object contains an interal list of <code>MessageBeans</code>
 * which represent each message that is contained in teh folder object.
 * </p>
 *
 * @since 1.0
 */
public class MailFolder extends SortableList {

    protected static Log log = LogFactory.getLog(MailFolder.class);

    // Mail folder that this class wraps
    protected Folder folder;

    protected MailAccountBean mailAccount;

    // list of messages contain in the folder
    protected ArrayList messageList = new ArrayList();

    // is this the sent folder
    protected boolean isSentFolder;

    // folder alternative name, mainly used for searching
    protected String folderName = "";

    public MailFolder() {
        super("dateColumn");
    }

    public MailFolder(String folderName) {
        super("dateColumn");
        this.folderName = folderName;
    }

    /**
     * Indicates that the mail folder contains child folders. Child forlders mean
     * that we must recursively decent to find a child messages.
     *
     * @return true if folder holds sub folders; otherwise, false .
     */
    public boolean holdsFolders() {
        boolean containsFolders = false;
        if (folder != null) {
            try {
                containsFolders = (folder.getType() & Folder.HOLDS_FOLDERS) != 0;
            } catch (MessagingException e) {
                log.error("Error getting folder type");
            }
        }
        return containsFolders;

    }

    /**
     * Is this folder a send item folder.
     * @return true if the folder names matche MailManager.SENT_FOLDER_NAME, false
     * otherwise;
     */
    public boolean isSentFolder() {
        return isSentFolder;
    }


    /**
     * Gets the child folder objects contained by this object.
     *
     * @return child folders.
     */
    public Folder[] getChildFolders() {

        Folder[] folders = null;
        try {
            folders = folder.list();

        } catch (MessagingException e) {
            log.error("Error getting child folders ");
        }
        return folders;
    }

    /**
     * Gets the <code>MailAccount</code> object associated with this folder.
     *
     * @return mail account associated with this folder.
     */
    public MailAccountBean getMailAccount() {
        return mailAccount;
    }

    /**
     * Sets the mail account associated with this folder.
     *
     * @param mailAccount mail account associated with this folder.
     */
    public void setMailAccount(MailAccountBean mailAccount) {
        this.mailAccount = mailAccount;
    }

    /**
     * Sets the folder wrapped by this object.
     *
     * @param folder MimeFolder object to be wrapped by this class.
     */
    public void setFolder(Folder folder) {
        this.folder = folder;
        isSentFolder =
        ((getFullName().equals(MailManager.SENT_FOLDER_NAME))||(getFullName().equals(MailManager.DRAFT_FOLDER_NAME)));
    }

    /**
     * Gets the folder wrapped by this object.
     *
     * @return folder object.
     */
    public Folder getFolder() {
        return folder;
    }

    /**
     * Get the total message count.
     *
     * @return total number of messages. -1 may be returned by certain
     *         implementations if this method is invoked on a closed folder.
     */
    public int getMessageCount() {
        if (folder != null) {
            try {
                return folder.getMessageCount();
            }
            catch (MessagingException e) {
                log.error("Error getting total number of messages");
            }
            return -1;
        } else {
            return -1;
        }
    }

    /**
     * Get the new message count.
     *
     * @return number of new messages. -1 may be returned by certain
     *         implementations if this method is invoked on a closed folder.
     */
    public int getNewMessageCount() {
        if (folder != null) {
            try {
                return folder.getNewMessageCount();
            }
            catch (MessagingException e) {
                log.error("Error getting total number of new messages");
            }
            return -1;
        } else {
            return -1;
        }
    }

    /**
     * Get the fullname of this folder.
     *
     * @return full name of the Folder
     */
    public String getFullName() {
        if (folder != null) {
            return folder.getFullName();
        } else {
            return folderName;
        }
    }

    /**
     * Open this folder in the given mode if the folder is not currently opne.
     * We can not currently close then open as folder as it will require
     * connection listeners to insure reliability.
     *
     * @param mode to open this folder with.
     */
    public synchronized void open(int mode) {
        if (folder != null) {
            try {
                // open the folder with the new permission
                if (!folder.isOpen()) {
                    folder.open(mode);
                }
            }
            catch (FolderNotFoundException e) {
                log.error("Folder not found, could not open ");
            }
            catch (MessagingException e) {
                log.error("Error opening folder ");
            }
        }
    }

    /**
     * Check whether this connection is really open.
     *
     * @return true if this Folder is in the 'open' state.
     */
    public boolean isOpen() {
        if (folder != null) {
            return folder.isOpen();
        } else {
            return false;
        }
    }

    /**
     * Close this folder.
     *
     * @param expunge expunges all deleted messages if this flag is true
     */
    public synchronized void close(boolean expunge) {
        if (folder != null) {
            try {
                if (folder.isOpen()) {
                    folder.close(expunge);
                }
            }
            catch (MessagingException e) {
                log.error("Could not close mail folder ", e);
            }
        }
    }

    /**
     * Expunge.
     */
    public void expunge() {
        if (folder != null) {
            try {
                // call expunge on wrapped folder and removed messages
                folder.expunge();
//                javax.mail.Message[] message = folder.expunge();
//                // removed expunged messages from messageList
//                MessageBean tmpMessageBean;
//                // have to check message size on each iteration
//                for (int i = 0; i < messageList.size(); i++) {
//                    tmpMessageBean = (MessageBean) messageList.get(i);
//                    // if we have a match remove the message bean as well.
//                    if (tmpMessageBean.getMessage().isExpunged()) {
//                        messageList.remove(i);
//                    }
//                }
            }
            catch (MessagingException e) {
                log.error("Error exponging folder " + folder.getName(), e);
            }

        }
    }

    /**
     * Gets the current ICEmessages list from the folder
     *
     * @return ICEmessage contained in this folder.
     */
    public ArrayList getMessageList() {
        // make sure the folder is open
        open(Folder.READ_WRITE);
        // sor the list before returning.
        if (!oldSort.equals(sort) ||
                oldAscending != ascending){
             sort(getSort(), isAscending());
             oldSort = sort;
             oldAscending = ascending;
        }
        return messageList;
    }

    /**
     * Sort the list.
     */
    protected void sort(final String column, final boolean ascending) {
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                MessageBean c1 = (MessageBean) o1;
                MessageBean c2 = (MessageBean) o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("attachmentColumn")) {
                    return ascending ?
                            (c1.hasAttachement() == c2.hasAttachement() ? 0 : 1) :
                            (c2.hasAttachement() == c1.hasAttachement() ? 0 : 1);
                } else if (column.equals("subjectColumn")) {
                    return ascending ?
                            c1.getSubject().compareTo(c2.getSubject()) :
                            c2.getSubject().compareTo(c1.getSubject());
                } else if (column.equals("senderColumn")) {
                    return ascending ?
                            c1.getSender().compareTo(c2.getSender()) :
                            c2.getSender().compareTo(c1.getSender());
                } else if (column.equals("toColumn")) {
				                    return ascending ?
				                            c1.getRecipientsTo().compareTo(c2.getRecipientsTo()) :
                            c2.getRecipientsTo().compareTo(c1.getRecipientsTo());
                } else if (column.equals("dateColumn")) {
                    if (c1.getSendDate() != null &&
                            c2.getSendDate() != null) {
                        return ascending ?
                                c1.getSendDate().compareTo(c2.getSendDate()) :
                                c2.getSendDate().compareTo(c1.getSendDate());
                    } else {
                        return 0;
                    }
                }
//                else if (column.equals("readColumn")) {
//                    return ascending ?
//                            c1.getEndDate().compareTo(c2.getEndDate()) :
//                            c2.getEndDate().compareTo(c1.getEndDate());
//                }
//                else if (column.equals("threadColumn")) {
//                    if (ascending){
//                        if (c1.getStatus() > c2.getStatus())
//                            return 1;
//                        else if (c1.getStatus() < c2.getStatus())
//                            return -1;
//                        else
//                            return 0;
//                    }
//                    else{
//                        if (c1.getStatus() > c2.getStatus())
//                            return -1;
//                        else if (c1.getStatus() < c2.getStatus())
//                            return 1;
//                        else
//                            return 0;
//                    }
//                }
                else if (column.equals("selectedColumn")) {
                    return ascending ?
                            (c1.isSelected() == c2.isSelected() ? 0 : 1) :
                            (c2.isSelected() == c1.isSelected() ? 0 : 1);
                } else
                    return 0;
            }
        };
        if (messageList != null) {
            Collections.sort(messageList, comparator);
        }
    }

    /**
     * Is the default sort direction for the given column "ascending" ?
     */
    protected boolean isDefaultAscending(String sortColumn) {
        return isAscending();
    }


}
