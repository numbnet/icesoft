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

import com.icesoft.applications.faces.webmail.WebmailMediator;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Flags;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * <p>The MessageBean object is a wrapper for a Message class.  This wrapper
 * class handles some of the work needed to convert html form data to valid
 * message field and back again.</p>
 * <p/>
 * <p>This class also assumes that partial submits can be used to verify
 * address information for to, cc, bcc and subject.  A message is associated
 * with bean so that error messages can be sent to the applications.</p>
 *
 * @since 1.0
 */
public class MessageBean extends Message {

    // indicates the the message has been selected in the folder view
    protected boolean selected;

    // flag to indicate if header details should be rendered
    protected static boolean renderHeaderDetails = false;

    // callback to parent folder of message
    protected MailFolderBean parentFolder;

    // callback to account6 associated with messages.
    protected MailAccountBean parentAccount;

    //image source indicating if the header details is showed. Default is hidden
    protected String imageSource = "images/plus_green.gif";

    // date format
    protected static DateFormat dateFormat;

    // Constants used to indicate wrapper type for html formating.
    protected static final int HTML_SENDER = 0;
    protected static final int HTML_SUBJECT = 1;
    protected static final int HTML_DATE = 2;
    protected static final int HTML_TO = 3;

    private static final String TO_RECIPIENT = "To:";
    private static final String CC_RECIPIENT = "Cc:";
    private static final String BCC_RECIPIENT = "Bcc:";

    private static final String EMPTY = "";

    protected final SelectItem[] recipientTypes = new SelectItem[]{
            new SelectItem(TO_RECIPIENT),
            new SelectItem(CC_RECIPIENT),
            new SelectItem(BCC_RECIPIENT)
    };

    //protected HtmlDataTable recipients;
    protected List recipientTable;


    public MessageBean() {
        if (dateFormat == null) {
            Locale locale = null;
            if (FacesContext.getCurrentInstance() != null &&
                    FacesContext.getCurrentInstance().getApplication() != null)
            {
                locale = FacesContext.getCurrentInstance().
                        getApplication().getDefaultLocale();
            }
            // make sure the local is not null, as it will cause an error
            // when creating the dateFormat object.
            if (locale == null) {
                locale = Locale.getDefault();
            }
            // set the new date formate
            dateFormat = DateFormat.getDateTimeInstance(
                    DateFormat.SHORT,
                    DateFormat.SHORT,
                    locale);
        }

        //assign default recipient table
        recipientTable = new ArrayList(3);
        recipientTable.add(new TableEntry(TO_RECIPIENT, ""));
        recipientTable.add(new TableEntry(CC_RECIPIENT, ""));
        recipientTable.add(new TableEntry(BCC_RECIPIENT, ""));
    }

    /**
     * Add more table entries to recipient table
     */
    public void addMoreRecipients() {
        recipientTable.add(new TableEntry(TO_RECIPIENT, ""));
        recipientTable.add(new TableEntry(TO_RECIPIENT, ""));
        recipientTable.add(new TableEntry(TO_RECIPIENT, ""));
    }


    public List getRecipientTable() {
        return recipientTable;
    }


    public void setRecipientTable(List recipientTable) {
        this.recipientTable = recipientTable;
    }


    public SelectItem[] getRecipientTypes() {
        return recipientTypes;
    }


    public int getTableSize() {
        return this.recipientTable.size();
    }

    public String getImageSource(){
		if(renderHeaderDetails)
			return "images/minus_green.gif";
		else
        	return "images/plus_green.gif";

	}

    /**
     * Utility method for populating the recipient fields from recipientTable
     */
    public void populateRecipients() {
        String toString = "";
        String ccString = "";
        String bccString = "";

        for (int i = 0; i < recipientTable.size(); i++) {
            TableEntry row = (TableEntry) recipientTable.get(i);
            String rowValue = row.value;
            String rowType = row.type;
            if (TO_RECIPIENT.equals(rowType)) {
                if (!(EMPTY.equals(rowValue)))
                    toString += ( rowValue) + ",";
            } else if (CC_RECIPIENT.equals(rowType)) {
                if (!(EMPTY.equals(rowValue)))
                    ccString += ( rowValue) + ",";
            } else if (BCC_RECIPIENT.equals(rowType)) {
                if (!(EMPTY.equals(rowValue)))
                    bccString += ( rowValue) + ",";
            }
        }

        if (!toString.equals(EMPTY))
            this.setRecipientsTo(toString.substring(0, toString.length() - 1));
        if (!ccString.equals(EMPTY))
            this.setRecipientsCc(ccString.substring(0, ccString.length() - 1));
        if (!bccString.equals(EMPTY))
            this.setRecipientsBcc(bccString.substring(0, bccString.length() - 1));

    }


    /**
     * Toggles the Header details render state.
     *
     * @return null indicates that action event should not cause a navigation.
     */
    public String toggleHeaderDetails() {
        renderHeaderDetails = !renderHeaderDetails;

        return null;
    }

    /**
     * Gets the state of the renderHeaderDetials flag.
     *
     * @return true if the header should be rendered; otherwise, false.
     */
    public boolean isRenderHeaderDetails() {
        return renderHeaderDetails;
    }

    /**
     * Sets the flag that indicates if the header details should be rendered.
     *
     * @param renderHeaderDetails true to render header; otherwise, false.
     */
    public void setRenderHeaderDetails(boolean renderHeaderDetails) {
        MessageBean.renderHeaderDetails = renderHeaderDetails;
    }

    /**
     * Gets the parent folder which contains this message.
     *
     * @return parent folder if one is associated.  A parent is not guarenteed
     *         new messages do not have parent folders, null is returned in this case.
     */
    public MailFolderBean getParentFolder() {
        return parentFolder;
    }

    /**
     * Sets the parent folder callback for this message. This state should be
     * update if a message is moved to a different folder.
     *
     * @param parentFolder parent folder contains by this message.
     */
    public void setParentFolder(MailFolderBean parentFolder) {
        this.parentFolder = parentFolder;
    }

    /**
     * Gets the parent mail account associated with this message.  The parent
     * maill account should always be set during message creation
     *
     * @return parent mail account if one; otherwise, null.
     * @see MailAccountControl#createNewMessage()
     */
    public MailAccountBean getParentAccount() {
        return parentAccount;
    }

    /**
     * Set the parent mail account callback. The parent mail account is used
     * as the context in which the message can be manipulated.
     *
     * @param parentAccount mail account to associate with this message.
     */
    public void setParentAccount(MailAccountBean parentAccount) {
        this.parentAccount = parentAccount;
    }

    /**
     * Sets the folder callback.
     *
     * @param parentFolder parent fold bean.
     */
    public void setFolderCallback(MailFolderBean parentFolder) {
        this.parentFolder = parentFolder;
    }

    /**
     * Sets the message sets the selected message of the parent folder bean
     * to this message.  This should be done before viewing a message.
     *
     * @param event
     */
    public void changeSelectedMessage(ActionEvent event) {
        if (parentAccount != null) {
            // deselete all other messages except this one, view specific.
            parentFolder.deselecteMessageFlags(this);
            // set this been as the selected, model specific
            parentAccount.setSelectedMessageBean(this);
        }
    }

    /**
     * Gets if the messages has been selected in the view.
     *
     * @return true if the message is selected; otherwise, false.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the selected message state.
     *
     * @param selected true to select message; otherwise, false.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Sets the message content as plain/text from the given form text area. A
     * multipart message is created so that further development can add attachements
     * and html more easily.
     *
     * @param plainText content String to set as message content.
     */
     public void setPlainTextContent(String plainText) {
        try {
            message.setText(plainText);
        } catch (MessagingException e) {
            log.error("Error writing message content");
        }

    }

    /**
     * Sets the message content as plain/text from the given form text area. A
     * multipart message is created so that further development can add attachements
     * and html more easily.
     *
     * @return plain text of message content.
     */
    public String getPlainTextContent() {
        // after assemble contnet is call, we have a string with line breaks
        // this good for sending plain text messages.
        String content = assembleMimeMesageContent(message);
        content = removeHTMLTags(new StringBuffer(content)).toString();
        return content;
    }

    /**
     * Gets the messages send date formated to SHORT date and time.
     *
     * @return message send date in string format
     */
    public String getSendDateString() {
        Date date = getSendDate();
        if (date != null) {
            return dateFormat.format(date);
        } else {
            return "";
        }
    }

    /**
     * Gets the sender of this message.
     *
     * @return sender information in String form and in RFC822 format.
     */
    public String getSender() {
        StringBuffer senders = new StringBuffer();
        Address[] addresses = getSenders();
        if (addresses != null &&
                addresses.length > 0) {
            InternetAddress tmpAddress;
            for (int i = 0; i < addresses.length; i++) {
                tmpAddress = (InternetAddress) addresses[i];
                senders.append(tmpAddress.toString());
            }
            return senders.toString();
        }
        return "";
    }

    /**
     * Set the RFC 822 "From" header field. Any existing values are replaced
     * with the given address. If address is null, this header is removed.
     *
     * @param from - the address in RFC822 format.
     */
    public void setSender(String from) {
        try {
            InternetAddress address = InternetAddress.parse(from, true)[0];
            setSender(address);
        } catch (javax.mail.MessagingException e) {
            log.error("Could decode from string, maynot be in RFC822 format");
            WebmailMediator.addMessage(
                    "eevp:emailEditForm",
                    "errorMessage",
                    "webmail.email.edit.error.sender",null);
        }
        catch (IndexOutOfBoundsException e) {
            log.error("Invalid sender, could not par internet address");
            WebmailMediator.addMessage(                
                    "eevp:emailEditForm",
                    "errorMessage",
                    "webmail.email.edit.error.sender",null);
        }
    }

    /**
     * Set the "TO" recipient type to the given addresses. If the address
     * parameter is null, the corresponding recipient field is removed.
     *
     * @param recipients string made up of valid email addresses.
     */
    public void setRecipientsTo(String recipients) {
        try {
            InternetAddress[] addresses =
                    InternetAddress.parse(recipients, true);
            setRecipients(addresses, MimeMessage.RecipientType.TO);
        } catch (AddressException e) {
            if (log.isDebugEnabled())
                log.debug("Could not parse To internet addreses");
            WebmailMediator.addMessage(
                    "eevp:emailEditForm",
                    "errorMessage",
                    "webmail.email.edit.error.to", null);
        }
    }

    /**
     * Set the "CC" recipient type to the given addresses. If the address
     * parameter is null, the corresponding recipient field is removed.
     *
     * @param recipients string made up of valid email addresses.
     */
    public void setRecipientsCc(String recipients) {
        try {
            InternetAddress[] addresses =
                    InternetAddress.parse(recipients, true);
            setRecipients(addresses, MimeMessage.RecipientType.CC);
        } catch (AddressException e) {
            if (log.isDebugEnabled())
                log.debug("Could not parse CC internet addreses");
            WebmailMediator.addMessage(
                    "eevp:emailEditForm",
                    "errorMessage",
                    "webmail.email.edit.error.cc", null);
        }
    }

    /**
     * Set the "BCC" recipient type to the given addresses. If the address
     * parameter is null, the corresponding recipient field is removed.
     *
     * @param recipients string made up of valid email addresses.
     */
    public void setRecipientsBcc(String recipients) {
        try {
            InternetAddress[] addresses =
                    InternetAddress.parse(recipients, true);
            setRecipients(addresses, MimeMessage.RecipientType.BCC);
        } catch (AddressException e) {
            if (log.isDebugEnabled())
                log.debug("Could not parse BCC internet addreses");
            WebmailMediator.addMessage(
                    "eevp:emailEditForm",
                    "errorMessage",
                    "webmail.email.edit.error.bcc", null);
        }
    }

    /**
     * Below methods are part of changes an emails sender account, not fully
     * implemented yet.
     */

    /**
     * Sets the message sender account.  It is possible for there to be more
     * then one mail account for the users session and thus the mail should
     * be configureable.
     *
     * @param senderAccount
     */
    public void setMessageSenderAccount(MailAccountBean senderAccount) {
        if (senderAccount != null) {
            setSender(senderAccount.getEmail());
        }
    }

    /**
     * Gets the message sender account associated with this email.
     *
     * @return MailAccountBean associated
     */
    public MailAccountBean getMessageSenderAccount() {

        if (message == null || parentAccount == null) {
            return null;
        }

        try {
            // get mail account manager so that we can
            MailManager mailManager = parentAccount.getMailManager();
            MailAccountBean foundAccount = null;
            MailAccountBean tmpAccount;

            InternetAddress tmpAddress = (InternetAddress) message.getSender();
            if (tmpAddress != null) {
                for (int i = 0; i < mailManager.getMailAccounts().size(); i++) {
                    tmpAccount = (MailAccountBean) mailManager.getMailAccounts().get(i);

                    if (tmpAccount.getEmail().equals(tmpAddress.getAddress())) {
                        return tmpAccount;
                    }
                }
                return foundAccount;
            }
        } catch (MessagingException e) {
            log.error("Error retreiving mesage sender");
        }
        return null;
    }


    public void mailAccountChange(ValueChangeEvent event) {
        log.debug("Mail Account change Event");
    }


    /**
     * Automatically populate the recipient table when reply/reply all a message
     *
     * @param onlyTo:boolean indicates whether it's a reply action(only sender)
     *               or reply all action(both sender and accounts on cc list)
     */
	public void updateRecipients(boolean onlyTo){
		recipientTable.clear();
		Address[] toList;

        try{
			//get the sender of original message
            toList = this.getMessage().getRecipients(MimeMessage.RecipientType.TO);
            //set sender as the recipient of this message automatically
            for(int i=0;i<toList.length;i++){
                recipientTable.add(new TableEntry("To:",toList[i].toString()));
            }

            //if reply all is intended
            if (!onlyTo) {
                Address[] allRecipients = this.getMessage().getAllRecipients();
                //check if it actually has any accounts on cc list
                if (allRecipients.length > toList.length) {
                    Address[] ccList = this.getMessage().getRecipients(MimeMessage.RecipientType.CC);
                    //set cc list as recipients as well
                    for (int i = 0; i < ccList.length; i++) {
                        recipientTable.add(new TableEntry("Cc:", ccList[i].toString()));
                    }
                    if(allRecipients.length>toList.length+ccList.length)
                    {
						Address[] bccList = this.getMessage().getRecipients(MimeMessage.RecipientType.BCC);
                        for (int i = 0; i < bccList.length; i++) {
                        	recipientTable.add(new TableEntry("Bcc:", bccList[i].toString()));
                        }
					}
                }
            }

	    }catch(MessagingException e){
			 log.error("Update Recipenent error", e);
		}

	}

    //Get wrapped sender property of this message bean
	public String getWrappedHtmlSender(){
		return getWrappedHtmlOutput(this,HTML_SENDER);
	}

    //Get wrapped subject property of this message bean
	public String getWrappedHtmlSubject(){
		return getWrappedHtmlOutput(this,HTML_SUBJECT);
	}

    //Get wrapped send date property of this message bean
	public String getWrappedHtmlSendDateString(){
		return getWrappedHtmlOutput(this,HTML_DATE);
	}

    //Get wrapped recipients(To:) property of this message bean
	public String getWrappedHtmlRecipientsTo(){
	    return getWrappedHtmlOutput(this,HTML_TO);
	}

    //Utility method to wrap a property of message bean with bold text in html
    //when this message bean represents an unread message
	private String getWrappedHtmlOutput(MessageBean msgBean, final int property){

		boolean isSeen = false;
		try{
            //check if msgBean represents an unread message
            isSeen = msgBean.getMessage().isSet(Flags.Flag.SEEN);
        }
        catch(MessagingException e){
            log.error("Error getting message flags.");
        }

        //if it's an old message, return unchanged properties
		if (isSeen){
			if(property == HTML_SENDER)
				return msgBean.getSender();
			else if(property == HTML_SUBJECT)
				return msgBean.getSubject();
			else if(property == HTML_DATE)
				return msgBean.getSendDateString();
			else if(property == HTML_TO)
				return msgBean.getRecipientsTo();
		}

		//if it's a new message, return wrapped properties
		else{
			if(property == HTML_SENDER)
				return ("<b>"+msgBean.getSender()+"</b>");
			else if(property == HTML_SUBJECT)
				return ("<b>"+msgBean.getSubject()+"</b>");
			else if(property == HTML_DATE)
				return ("<b>"+msgBean.getSendDateString()+"</b>");
			else if(property == HTML_TO)
				return ("<b>"+msgBean.getRecipientsTo()+"</b>");
		}

		return "";
    }

	/**
	 * Return whether cc field is rendered in header details
	 */
    public boolean isCcRendered(){
		return (!getRecipientsCc().equals(""));
	}

	/**
	 * Return whether bcc field is rendered in header details
	 */
    public boolean isBccRendered(){
	   return (!getRecipientsBcc().equals(""));
	}


    /**
     * Populate recipient table with specified email addresses
     */
     public void populateEmails(String[] emails){
	 	recipientTable.clear();
	 	for(int i=0;i<emails.length;i++){
			recipientTable.add(new TableEntry(TO_RECIPIENT, emails[i]));
		}
	 }
}