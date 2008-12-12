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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.*;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>The <code>Message</code> class is the data model for a the view
 * <code>MessageBean</code>.  This class is responsible for wrapping a MimeMessage
 * class and providing methods which make interacting with the message object
 * easier.</p>
 *
 * @since 1.0
 */
public class Message {

    protected static Log log = LogFactory.getLog(Message.class);

    // HTML line break, need for display of message
    protected static final String HTML_LINE_BREAK = "<br/>";

    // &nbsp; , space attribute
    protected static final String HTML_SPACE = "&nbsp;";

    // search patterns, used for stripping html body tags from content
    protected static final Pattern BODY_PATTERN = Pattern.compile(
            "<\\s*body[^>]*>(.*)<\\s*/\\s*body\\s*>",
            Pattern.CASE_INSENSITIVE + Pattern.DOTALL);

    // search pattern, common incountered eamil tags to remove
    protected static final Pattern TAG_PATTERN = Pattern.compile(
            "</?\\w+((\\s+\\w+(\\s*=\\s*(?:\".*?\"|'.*?'|[^'\">\\s]+))?)+\\s*|\\s*)/?>",
            Pattern.CASE_INSENSITIVE + Pattern.DOTALL);

    protected static final Pattern TEXT_LINE_BREAK_PATTERN = Pattern.compile(
            "\n", Pattern.CASE_INSENSITIVE + Pattern.DOTALL);

    protected static final Pattern HTML_LINE_BREAK_PATTERN = Pattern.compile(
            HTML_LINE_BREAK, Pattern.CASE_INSENSITIVE + Pattern.DOTALL);

    // wrapped message object
    protected MimeMessage message;

    // Wrapped message flags.  It cost a bit to get this data, so we can cache
    // it and listen for MessageChangeEvents and update the values then.
    private Flags previousMessageFlag;
    private Flags currentMessageFlag;

    // wrapped message object, if the message has been saved
    protected MimeMessage savedMessage;

    /**
     * Gets the MimeMessage that is wrapped by this class.
     *
     * @return a mime message.
     */
    public MimeMessage getMessage() {
        return message;
    }

    /**
     * Sets the MimeMessage that this class wrapps.
     *
     * @param message
     */
    public void setMessage(MimeMessage message) {
        // update flags.
        try {
            previousMessageFlag = message.getFlags();
            currentMessageFlag = message.getFlags();
        } catch (MessagingException e) {
            log.error("Error getting message fags");
        }

        this.message = message;
    }

    /**
     * Sets the cached message flags. This is a cached value and does
     * not actually set the message for the value.
     * @param messageFlags new message flags.
     */
    public void setMessageFlag(Flags messageFlags){
        previousMessageFlag = currentMessageFlag;
        currentMessageFlag = messageFlags;
    }

    /**
     * Gets the previously set  message flags. This is a cached value and does
     * not actually check the message for the value.
     * @return previously set message flags
     */
    public Flags getPreviousMessageFlag() {
        return previousMessageFlag;
    }

    /**
     * Gets the currently set  message flags. This is a cached value and does
     * not actually check the message for the value.
     * @return previously set message flags
     */
    public Flags getCurrentMessageFlag() {
        return currentMessageFlag;
    }

    /**
     * Get the personal name. If the name is encoded as per RFC 2047, it is
     * decoded and converted into Unicode. If the decoding or conversion fails,
     * the raw data is returned as is.
     * @param sender string making up a name encoded as per RFC 2047.
     * @return the personal value of the sender string if found; otherwie an
     * empty string.
     */
    public static String getPersonal(String sender) {

        String personal = "";
        try {
            InternetAddress addr = InternetAddress.parse(sender)[0];
            if (addr.getPersonal() != null)
                personal = addr.getPersonal();
        } catch (AddressException e) {
            log.error("Invalid sender");
        }
        catch (IndexOutOfBoundsException e) {
            log.error("Invalid sender, could not par internet address");
        }

        return personal;

    }

    /**
     * Get the email address. If the email is encoded as per RFC 2047, it is
     * decoded and converted into Unicode. If the decoding or conversion fails,
     * the raw data is returned as is.
     * @param sender string making up a email encoded as per RFC 2047.
     * @return the personal value of the email string if found; otherwie an
     * empty string.
     */
    public static String getEmail(String sender) {
        String email = "";
        try {
            InternetAddress addr = InternetAddress.parse(sender)[0];
            if (addr.getAddress() != null)
                email = addr.getAddress();
        } catch (AddressException e) {
            log.error("Invalid sender");
        }
        catch (IndexOutOfBoundsException e) {
            log.error("Invalid sender, could not par internet address");
        }
        return email;
    }

    /**
     * Gets the saved message bean.  The saved message bean will be null
     * unless a user has saved a message to the "draft" folder.  Once saved
     * mime message are read-only and as a result we must save a reference to
     * the old message so that if a user edits a message and wants to save the new
     * copy, we also want ot remove the old saved copy.
     *
     * @return saved mime message state if any; otherwise, null.
     */
    public MimeMessage getSavedMessage() {
        return savedMessage;
    }

    /**
     * Sets the saved messaged state.  The saved message state should only be
     * set if a message is saved to a folder.  This will ensure that we can
     * delete the savedMessage reference if the user decides to re-save a messages.
     *
     * @param savedMessage old message state.
     */
    public void setSavedMessage(MimeMessage savedMessage) {
        this.savedMessage = savedMessage;
    }

    /**
     * Gets the attachement state status of the message.
     *
     * @return true if the message has an attachement; otherwise, false.
     *         <p/>
     *         <p><b>NOTE:</b>This class has not been implemented.
     */
    public boolean hasAttachement() {
        //todo implement, MimeMessage Object Hierarchy as needed
        try {
            if (message.getContentType().equals("multipart/mixed")) {
                Object content = message.getContent();
                if (content instanceof Multipart) {

                }
            }
            return false;
        } catch (MessagingException e) {
            log.error("Error determining if message has attachement ", e);
        } catch (IOException e) {
            log.error(e);
        }
        return false;
    }

    /**
     * Gets the send date of this message.  If the message has not yest been send
     * then the current date is returned.
     *
     * @return messages sent date if set, todays date if the message has no date
     *         value.
     */
    public Date getSendDate() {
        try {
            return message.getSentDate();
        } catch (MessagingException e) {
            log.error("Error getting message send date");
        }
        // return current date if no date, not great but better then null
        // todo: convert a formated date string, reformat to return null
        // and add string specific methods to the view (MessageBean).
        return new Date();
    }

    /**
     * Gets the message subject.
     *
     * @return message subject.  If null, an empty string is returned.
     */
    public String getSubject() {
        try {
            return message.getSubject();
        } catch (MessagingException e) {
            log.error("Error getting message subject");
        }
        return "";
    }

    /**
     * Set the subject of the wrapped message.
     *
     * @param subject new subject content for message.
     */
    public void setSubject(String subject) {
        try {
            message.setSubject(subject);
        } catch (MessagingException e) {
            log.error("Error setting subject");
        }
    }

    /**
     * Get message sender addresses.  There is usually just one but there
     * is can be more then one by according to the specification
     *
     * @return array of Address object for each associated sender.
     */
    public Address[] getSenders() {
        try {
            return message.getFrom();
        } catch (MessagingException e) {
            log.error("Error getting message subject");
        }
        return null;
    }


    /**
     * Set the RFC 822 "From" header field. Any existing values are replaced
     * with the given address. If address is null, this header is removed.
     *
     * @param from - the address in RFC822 format.
     */
    public void setSender(Address from) {
        if (message != null) {
            try {
                message.setFrom(from);
            } catch (IllegalWriteException e) {
                log.error("Could not set from address, read only message");
            } catch (javax.mail.MessagingException e) {
                log.error("Could decode from string, maynot be in RFC822 format");
                WebmailMediator.addMessage(
                        "eevp:emailEditForm",
                        "errorMessage",
                        "webmail.email.edit.error.sender", null);
            }
        } else if (log.isDebugEnabled()) {
            log.debug("Null message could not set sender address");
        }
    }

    /**
     * Gets the recipients specifiedy by the "TO" header.
     *
     * @return String representing all the addresses that make up the "To" header
     */
    public String getRecipientsTo() {
        return getRecipient(MimeMessage.RecipientType.TO);
    }

    /**
     * Gets the recipients specifiedy by the "CC" header.
     *
     * @return String representing all the addresses that make up the "CC" header
     */
    public String getRecipientsCc() {
        return getRecipient(MimeMessage.RecipientType.CC);
    }

    /**
     * Gets the recipients specifiedy by the "BCC" header.
     *
     * @return String representing all the addresses that make up the "BCC" header
     */
    public String getRecipientsBcc() {
        return getRecipient(MimeMessage.RecipientType.BCC);
    }

    /**
     * Utility method getting message recipient.
     *
     * @param type recipient type, valid values are constants in
     *             javax.mail.Message.RecipientType
     * @return String representing the specified recipient.  Empty string if
     *         no such reciepient is found.
     */
    private String getRecipient(final javax.mail.Message.RecipientType type) {
        StringBuffer recipients = new StringBuffer();
        try {
            Address[] addresses = message.getRecipients(type);
            if (addresses != null &&
                    addresses.length > 0) {
                // Write out the addres in the order they are found.
                for (int i = 0; i < addresses.length; i++) {
                    recipients.append(addresses[i].toString()+",");
                }
                return recipients.substring(0,recipients.length()-1).toString();
            }
        } catch (MessagingException e) {
            log.error("Error getting message recepients ");
        }
        return "";
    }

    /**
     * Set the "TO" recipient type to the given addresses. If the address
     * parameter is null, the corresponding recipient field is removed.
     *
     * @param recipient string made up of valid email addresses.
     */
    public void setRecipientsTo(Address[] recipient) {
        setRecipients(recipient, MimeMessage.RecipientType.TO);
    }

    /**
     * Set the "CC" recipient type to the given addresses. If the address
     * parameter is null, the corresponding recipient field is removed.
     *
     * @param recipient string made up of valid email addresses.
     */
    public void setRecipientsCc(Address[] recipient) {
        setRecipients(recipient, MimeMessage.RecipientType.CC);
    }

    /**
     * Set the "BCC" recipient type to the given addresses. If the address
     * parameter is null, the corresponding recipient field is removed.
     *
     * @param recipient string made up of valid email addresses.
     */
    public void setRecipientsBcc(Address[] recipient) {
        setRecipients(recipient, MimeMessage.RecipientType.BCC);
    }

    /**
     * Utility method getting message recipient.
     *
     * @param type recipient type, valid values are constants in
     *             javax.mail.Message.RecipientType
     */
    protected void setRecipients(Address[] address,
                                 final javax.mail.Message.RecipientType type) {
        try {
            message.setRecipients(type, address);

        } catch (IllegalWriteException e) {
            log.error("Could not set recipients address, read only message");
        } catch (MessagingException e) {
            log.error("Error setting message To recepients ");
        }
    }


    /**
     * Gets the content of the message.  The method will decide whether or not
     * will return html or plain text.
     *
     * @return emails content type
     */
    public String getContent() {
        Folder folder = message.getFolder();
        if (folder != null && !folder.isOpen()){
            try {
                folder.open(Folder.READ_WRITE);
            } catch (MessagingException e) {
                log.error("Error opening folder for message content");
            }
        }
        // after assemble contnet is call, we have a string with line breaks
        String pageContent = assembleMimeMesageContent(message);
        // for display purposes we want to sub in <br>'s
        return removeTextChariageBreaks(pageContent).toString();
    }

    /**
     * Sets the message content as plain/text from the given form text area. This
     * could be update for differnet content types.
     */
    public void setContent(MimeMultipart content) {
        try {
            message.setContent(content);
        } catch (MessagingException e) {
            log.error("Message content could not be set, readonly state", e);
        }

    }

    /**
     * Method takes mime message and sets the String content for the Message.
     * If the message is a multipart, this method will be called recursively until
     * we set the content based on the specific Part we are looking for.
     */
    protected String assembleMimeMesageContent(Part part) {

        if (part == null) {
            if (log.isDebugEnabled()) {
                log.debug("Null message part when tgetting message content");
            }
            return HTML_LINE_BREAK;
        }

        // Get the data handler of the message part and get it's content
        Object partContent;
        try {
            partContent = part.getDataHandler().getContent();
        }
        catch (MessagingException e) {
            log.error("Problem Accessing Content from MIME message");
            return HTML_LINE_BREAK;
        }
        catch (IOException e) {
//            log.error("IO Problem Accessing Content from MIME message");
            return HTML_LINE_BREAK;
        }

        // get the multipart message content
        StringBuffer content = new StringBuffer();
        if (partContent instanceof MimeMultipart) {

            content.append(getMultipartContent((MimeMultipart) partContent));
        } else {
            // remove body tages for html message.
            try {
                if (part.isMimeType("text/html")) {
                    // replace CR with HTML line breaks for display purboses
                    content.append(extractBodyInnerHTML(
                            new StringBuffer(partContent.toString())));
                } else {
                    content.append(partContent.toString());
                }
            } catch (MessagingException e) {
                log.error("Problem Accessing MIME message content type");
                return content.toString();
            }
        }
        return content.toString();
    }

    /**
     * Gets the content of a multipart message.  This may require a recursive
     * decent into the message content.
     *
     * @param multipart multipart message part.
     * @return plain text representation of message content .
     */
    private StringBuffer getMultipartContent(MimeMultipart multipart) {
        StringBuffer content = new StringBuffer();
        try {
            // return if we havea  null message buton
            if (multipart.getCount() < 1)
                return content;
            //Loop through the content types and try to find the "best"
            //viewable content type for the message body. For now,
            //the "best" type is text/plain because of FF issues,
            //otherwise we use the first one we come across.
            BodyPart messageBody = null;
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                String disposition = part.getDisposition();
                if (disposition == null) {
                    if (messageBody == null)
                        messageBody = part;
                    //if this part is text/plain, we prefer it for now.
                    if (part.isMimeType("text/plain")) {
                        messageBody = part;
                        break;
                    }
                }
            }
            //If we didn't find anything we liked, just pick the first part.
            if (messageBody == null)
                messageBody = multipart.getBodyPart(0);
            //Set the content. This might recurse.
            content.append(assembleMimeMesageContent(messageBody));
        }
        catch (MessagingException e) {
            log.error("Problem reading mutlipart message");
            // return what we have found so far.
            content.append(HTML_LINE_BREAK);
        }
        return content;
    }

    /**
     * Utility method to extract content between the body tags of an HTML message.
     *
     * @param content content
     * @return message content between html tags.
     */
    private StringBuffer extractBodyInnerHTML(StringBuffer content) {
        StringBuffer match = new StringBuffer();
        try {
            Matcher bodyPatternMatcher = BODY_PATTERN.matcher(content);
            if (bodyPatternMatcher.find()) {
                match.append(bodyPatternMatcher.group(1));
            } else {
                return content;
            }
        } catch (IllegalStateException e) {
            // If no match has yet been attempted,
            // or if the previous match operation failed
            log.error("Problem removing html body tags from test/html message type");
            return content;
        }
        return match;
    }

    /**
     * Called to remove html tags from content of a text/html message to view in FF.
     *
     * @param content
     * @return content stripped of html tags.
     */
    protected StringBuffer removeHTMLTags(StringBuffer content) {
        StringBuffer match = new StringBuffer();
        try {
            Matcher tagMatcher = TAG_PATTERN.matcher(content);
            match.append(tagMatcher.replaceAll(HTML_SPACE));
        } catch (IllegalStateException e) {
            log.error("Problem replacing HTML tags");
            return content;
        }
        return match;
    }

    /**
     * Called to remove all text line breaks and replace with html breaks.
     *
     * @param content
     * @return content stripped of html tags.
     */
    private StringBuffer removeTextChariageBreaks(String content) {
        StringBuffer match = new StringBuffer();
        try {
            Matcher tagMatcher = TEXT_LINE_BREAK_PATTERN.matcher(content);
            match.append(tagMatcher.replaceAll(HTML_LINE_BREAK));
        } catch (IllegalStateException e) {
            log.error("Problem repacing text line breaks");
            return new StringBuffer(content);
        }
        return match;
    }
}
