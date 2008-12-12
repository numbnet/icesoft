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
import com.icesoft.applications.faces.webmail.WebmailBase;
import com.icesoft.applications.faces.webmail.WebmailMediator;
import com.icesoft.applications.faces.webmail.contacts.ContactBean;
import com.icesoft.applications.faces.webmail.contacts.ContactManager;
import com.icesoft.applications.faces.webmail.contacts.ContactModelUtility;
import com.icesoft.applications.faces.webmail.login.User;
import com.icesoft.applications.faces.webmail.navigation.NavigationContent;
import com.icesoft.applications.faces.webmail.navigation.NavigationManager;
import com.icesoft.applications.faces.webmail.navigation.NavigationSelectionBean;
import com.icesoft.applications.faces.webmail.settings.SettingsBean;
import edu.emory.mathcs.backport.java.util.concurrent.ExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.context.FacesContext;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Flags;
import javax.mail.internet.MimeMessage;
import javax.mail.Address;
import javax.mail.search.BodyTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.io.IOException;
import javax.faces.event.ActionEvent;


/**
 * <p>The <code>MailManager</code>class is responsible for storing
 * all mail accounts associated with a user and their respective controls.  This
 * class also work closing with the navigation Manager, when a new mail account
 * is added with a call to {@link #addMailAccount(MailAccountBean)} the
 * navigation manager is called and the necessary tree nodes are added.  The same
 * goes for {@link #removeMailAccount(MailAccountBean)}</p>
 * <p/>
 * <p>As mentioned earlier a user can have more then one mail account associated
 * wither their userName.  These allows users to add/edit/remove their own
 * mail accounts as they see fit.</p>
 *
 * @since 1.0
 */
public class MailManager implements WebmailBase {
    
    private static Log log = LogFactory.getLog(MailManager.class);
    
    // We use a thread pool which probs the mail accounts every x frequency
    // to see if messages have been added or removed.
    public static final int CHECK_MAIL_FREQUENCY = 30000;
    public static final ExecutorService threadPool = Executors.newCachedThreadPool();
    
    // search criteria
    private static final String SEARCH_FROM = "to";
    private static final String SEARCH_SUBJECT = "subject";
//    private static final String SEARCH_DATE = "date";
//    private static final String SEARCH_FLAGS = "flags";
    private static final String SEARCH_BODY = "body";
    
    // list of search criteria, initilized in init method
    private static SelectItem[] searchList;
    
    // folder where message are stored, make sure to dispose.
    private MailFolderBean searchResultsFolder;
    
    // default search type
    private String searchType = SEARCH_SUBJECT;
    // search string
    private String searchTerm = "";
    
    // settings bean for mail account management
    private SettingsBean settingsBean;
    
    // Navigation rules related to mail manipulation
    private static NavigationContent messageEditNavigation;
    private static NavigationContent messageViewNavigation;
    private static NavigationContent messageListNavigation;
    private static NavigationContent messageSearchListNavigation;
    private static NavigationContent settingsNavigation;
    
    // Draft folder decloration, not sure how this should be handed for internationalization
    public static final String DRAFT_FOLDER_NAME = "Drafts";
    
    // Trash folder
    public static final String TRASH_FOLDER_NAME = "Trash";
    
    // Sent Items folder decloration
    public static final String SENT_FOLDER_NAME = "Sent Items";
    
    // Sent Items folder decloration
    public static final String INBOX_FOLDER_NAME = "INBOX";
    
    // a list of the current mail accounts associated with this manager.
    private ArrayList mailAccountBeans;
    
    // selected mail account.
    private MailAccountBean selectedMailAccount;
    
    // mediator callback
    private WebmailMediator mediator;
    
    // is initiated flag
    private boolean isInit;
    
    // process used to record the intialization process of webmail
    protected int process = 0;
    
    // showModalPanel used to control the modal panel's on/off during the login process
    private boolean showModalPanel;
    
    // processText used to output text description based on the intialization
    // process of webmail
    private String processText = "";
    
    
    /**
     * Create a new instance of MailManager.  A call to init() is needed to
     * initialize the classe.  The dispose() method should be called when
     * the class is no longer needed.
     */
    public MailManager() {
    }
    
    /**
     * Gets the mediator class associated with this manager.
     *
     * @return singleton webmail mediator.
     */
    public WebmailMediator getMediator() {
        return mediator;
    }
    
    /**
     * Gets the naviation rule for navigating to the mail folder view.
     *
     * @return navigation content rule.
     */
    public static NavigationContent getMailViewNavigation() {
        return messageViewNavigation;
    }
    
    /**
     * Sets the Mediator callback.
     *
     * @param mediator initialized mediator.
     */
    public void setMediator(WebmailMediator mediator) {
        this.mediator = mediator;
        this.mediator.setMailManager(this);
    }
    
    /**
     * <p>This event listener is called when a mail folder is to be searched.
     * The search uses the current values of <code>searchType</code> and
     * <code>searchTerm</code> to generate the search term object used by
     * JavaMail.</p>
     * <p/>
     * <p>The message returned from the search, if any are added to a new folder
     * bean which is in tern set as the current folder.  This enables the
     * emailFolderviewPanel.jspx template to view the found messages. When a
     * user clicks on a normal mail folder the search results are removed from
     * the folder view.</p>
     *
     * @param event
     */
    public void searchFolder(ActionEvent event) {
        
        if (log.isDebugEnabled()) {
            log.debug("Search Folder Action");
        }
        
        // reset selected message count
        selectedMailAccount.getSelectedMailFolder().deselecteMessageFlags(null);
        
        // get the current folder from the selected mail account
        Folder selectedMailFolder =
                selectedMailAccount.getSelectedMailFolder().getFolder();
        
        // This should mean that we are doing a second search and need to
        // set the selected folder back to our current folder
        if (selectedMailFolder == null) {
            selectedMailFolder =
                    selectedMailAccount.getPreviouslySelectedMailFolder().getFolder();
            // update the selected folder, to reset the state for the next seach
            selectedMailAccount.setSelectedMailFolder(
                    selectedMailAccount.getPreviouslySelectedMailFolder());
        }
        
        if (selectedMailFolder != null && !"".equals(searchTerm) ) {
            try {
                // setup the search terms
                SearchTerm term = null;
                if (searchType.equals(SEARCH_FROM)) {
                    term = new FromStringTerm(searchTerm);
                } else if (searchType.equals(SEARCH_SUBJECT)) {
                    term = new SubjectTerm(searchTerm);
                } else if (searchType.equals(SEARCH_BODY)) {
                    term = new BodyTerm(searchTerm);
                }
                
                // Get results of search
                if (term != null) {
                    javax.mail.Message[] searchResultMessages =
                            selectedMailFolder.search(term);
                    // no check if there are any results
                    if (searchResultMessages != null &&
                            searchResultMessages.length > 0) {
                        // if we have results we want to create a new
                        // MailFolderbean which will be made the selected
                        // folder
                        if (searchResultsFolder != null) {
                            searchResultsFolder.dispose();
                        }
                        // set the artificial name of the search results folder.
                        searchResultsFolder =
                                new MailFolderBean(
                                WebmailMediator.getMessageBundle().
                                getString("webmail.searchFolder.title"));
                        searchResultsFolder.setWebmailMediator(mediator);
                        searchResultsFolder.setMailAccount(selectedMailAccount);
                        searchResultsFolder.addMessage(searchResultMessages);
                        
                        // set search results folder
                        selectedMailAccount.setSelectedMailFolder(searchResultsFolder);
                        
                    }
                    // display an message that new results where found
                    else {
                        WebmailMediator.addMessage(
                                "efvp:emailSearchForm",
                                "errorMessage",
                                "webmail.email.folder.view.search.error", null);
                    }
                }
            } catch (MessagingException e) {
                log.error("Error access mail folder");
            }
        } else if (log.isDebugEnabled()) {
            log.debug("Selected Folder is null, could not perform folder search");
        }
    }
    
    /**
     * Get the serach type used for next search.
     *
     * @return search type used, SEARCH_BODY, SEARCH_FROM, SEARCH_SUBJECT
     */
    public String getSearchType() {
        return searchType;
    }
    
    /**
     * Get the settings bean used by the mail manager
     *
     * @return SettingsBean
     */
    public SettingsBean getSettingsBean() {
        return settingsBean;
    }
    
    /**
     * Set the settings bean to the passed parameter
     *
     * @param settingsBean to use
     */
    public void setSettingsBean(SettingsBean settingsBean) {
        this.settingsBean = settingsBean;
    }
    
    /**
     * Sets the search type.
     *
     * @param searchType search type, valid values are SEARCH_BODY,
     *                   SEARCH_FROM, SEARCH_SUBJECT
     */
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
    
    /**
     * Gets the list of SelectedItems used by the jspx view.
     *
     * @return list of SelectItems need for the selectOneMenu component
     */
    public SelectItem[] getSearchList() {
        return searchList;
    }
    
    /**
     * Gets the search term that is currently set for this folders search. Search
     * term is a string value which will be used as the search criteria.
     *
     * @return search term
     */
    public String getSearchTerm() {
        return searchTerm;
    }
    
    /**
     * Sets the search term for this folders search.
     *
     * @param searchTerm string used as search criteria.
     */
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    /**
     * Gets a list of mail accounts assosiated this manager.
     *
     * @return list of mail accounts.
     */
    public SelectItem[] getMailAccountSelectedItems() {
        if (mailAccountBeans != null) {
            SelectItem[] mailAccounts = new SelectItem[mailAccountBeans.size()];
            MailAccountBean tmpMailAccount;
            for (int i = 0; i < mailAccountBeans.size(); i++) {
                tmpMailAccount = (MailAccountBean) mailAccountBeans.get(i);
                mailAccounts[i] =
                        new SelectItem(tmpMailAccount, tmpMailAccount.getEmail());
            }
            return mailAccounts;
        } else {
            return new SelectItem[]{new SelectItem(null, "No valid Account")};
        }
    }
    
    /**
     * Initiates the MailManager class.  The loginManager's verifiedUser object
     * is used to get a list of mail accounts associated with it. These
     * accounts are registered with the manager and the NavigationManager
     * is notified of need for the respective navigation nodes.
     */
    public void init() {
        
        // only init once
        if (isInit) {
            return;
        }
        isInit = true;
        
        // setup internationalization
        ResourceBundle tmp = WebmailMediator.getMessageBundle();
        searchList = new SelectItem[]{
            new SelectItem(SEARCH_SUBJECT,
                    tmp.getString("webmail.email.folder.view.search.subject")),
            new SelectItem(SEARCH_BODY,
                    tmp.getString("webmail.email.folder.view.search.body")),
            new SelectItem(SEARCH_FROM,
                    tmp.getString("webmail.email.folder.view.search.from"))
        };
        
        // setup default navigation rules
        messageEditNavigation = new NavigationContent();
        messageEditNavigation.setNavigationSelection(
                mediator.getNavigationManager().getNavigationSelectionBean());
        messageEditNavigation.setTemplateName("./inc/contentPanels/emailEditViewPanel.jspx");
        messageEditNavigation.setMenuContentTitle("Mail Message Edit");
        
        messageListNavigation = new NavigationContent();
        messageListNavigation.setNavigationSelection(
                mediator.getNavigationManager().getNavigationSelectionBean());
        messageListNavigation.setTemplateName("./inc/contentPanels/emailFolderViewPanel.jspx");
        messageListNavigation.setMenuContentTitle("Mail Folder");
        
        messageViewNavigation = new NavigationContent();
        messageViewNavigation.setNavigationSelection(
                mediator.getNavigationManager().getNavigationSelectionBean());
        messageViewNavigation.setTemplateName("./inc/contentPanels/emailViewPanel.jspx");
        messageViewNavigation.setMenuContentTitle("Mail Message View");
        
        messageSearchListNavigation = new NavigationContent();
        messageSearchListNavigation.setNavigationSelection(
                mediator.getNavigationManager().getNavigationSelectionBean());
        messageSearchListNavigation.setTemplateName("./inc/contentPanels/emailFolderViewPanel.jspx");
        messageSearchListNavigation.setMenuContentTitle("Search Results");
        
        settingsNavigation = new NavigationContent();
        settingsNavigation.setNavigationSelection(
                mediator.getNavigationManager().getNavigationSelectionBean());
        settingsNavigation.setTemplateName("./inc/contentPanels/settingsManagePanel.jspx");
        settingsNavigation.setMenuContentTitle("Settings");
        
        // Query the DB to find out how many mail accounts are associated with
        // the user
        User validUser =
                mediator.getLoginManager().getLoginBeanFactory().getVerifiedUser();
        
        // get a list of Mail accounts from DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        session.beginTransaction();
        Query mailAccountQuery = session.createQuery(
                "from MailAccountBean where username = :verifiedUser ");
        mailAccountQuery.setParameter("verifiedUser", validUser.getUserName());
        // get accounts associated with user.
        List mailAccounts = mailAccountQuery.list();
        // finish the transaction
        session.getTransaction().commit();
        
        if (log.isDebugEnabled()) {
            log.debug("Loading mail accounts, found: " + mailAccounts.size());
        }
        
        // if we have any accounts then we want to register them with the
        // mail control and build the tree nodes.
        if (mailAccounts.size() > 0) {
            mailAccountBeans = new ArrayList(mailAccounts.size());
            //log.info("mailAccounts size is "+mailAccounts.size());
            // for each mail account we want to register the account
            MailAccountBean mailAccountBean;
            for (int i = 0; i < mailAccounts.size(); i++) {
                
                // copy the mailAccount to a mailAccountBean
                mailAccountBean =
                        MailAccountBean.createMailAccountBean(
                        (MailAccount) mailAccounts.get(i));
                
                // set manager callback
                mailAccountBean.setMailManager(this);
                
                // add the mail account to mail account list
                mailAccountBeans.add(mailAccountBean);
                
                // initiate the account
                if (mailAccountBean.getMailControl().pingMailAccount()) {
                    // initiate mail account
                    mailAccountBean.init();
                    
                    // check to make sure the need folders are available, if not
                    // try creating them.
                    if (mailAccountBean.getMailControl() != null){
                        //update the intialization process and label for login webmail
                        mailAccountBean.getMailControl().createMailFolder(
                                INBOX_FOLDER_NAME);
                        this.setProcess(80);
                        this.setProcessText("Inbox Folder Initializing");
                        mediator.renderHelper();
                        mailAccountBean.getMailControl().createMailFolder(
                                TRASH_FOLDER_NAME);
                        this.setProcess(90);
                        this.setProcessText("Trash Folder Initializing");
                        mediator.renderHelper();
                        mailAccountBean.getMailControl().createMailFolder(
                                SENT_FOLDER_NAME);
                        this.setProcess(95);
                        this.setProcessText("Sent Folder Initializing");
                        mediator.renderHelper();
                        mailAccountBean.getMailControl().createMailFolder(
                                DRAFT_FOLDER_NAME);
                        this.setProcess(99);
                        this.setProcessText("Draft Folder Initializing");
                        mediator.renderHelper();
                    }
                    
                    // refresh folder list in Bean, this must be done before
                    // addMailAccount is called.
                    mailAccountBean.refreshFolderList();
                    
                    // Register mail account with the this MailManager as well
                    // as with the NavigationManager.  The NavigationManager adds
                    // all mail account folders to the navigation tree.
                    addMailAccount(mailAccountBean);
                    
                    if (log.isDebugEnabled()) {
                        log.debug("Loading Mail Account: " + mailAccountBean.toString());
                    }
                    
                    // add unique senders of all the emails in inbox folder of all
                    // mail accounts into contacts
//                    mailAccountBean.setSelectedMailFolder(
//                            mailAccountBean.getMailControl().findFolder(INBOX_FOLDER_NAME));
//                    mailAccountBean.getSelectedMailFolder().init();
                    
//                    List msgList = mailAccountBean.getSelectedMailFolder().getMessageList();
//
//                    for (int k = 0; k < msgList.size(); k++) {
//                        MessageBean msg = (MessageBean) msgList.get(k);
//                        String sender = msg.getSender();
//                        ContactBean tmpContact = new ContactBean();
//                        tmpContact.setDisplayName(Message.getPersonal(sender));
//                        tmpContact.setPrimaryEmail(Message.getEmail(sender));
//                        tmpContact.setFirst("");
//                        tmpContact.setLast("");
//                        tmpContact.setInitials("");
//                        tmpContact.setMiddle("");
//                        tmpContact.setNickName("");
//                        tmpContact.setNote("");
//                        tmpContact.setPrimaryPhone("");
//                        tmpContact.setUserName("");
//
//                        //create predefined collections and assign them
//                        //to the new bean
//                        tmpContact.setAddresses(ContactModelUtility.defaultAddress(tmpContact));
//                        tmpContact.setEmails(ContactModelUtility.defaultEmail(tmpContact));
//                        tmpContact.setPhones(ContactModelUtility.defaultPhone(tmpContact));
//                        tmpContact.setWebpages(ContactModelUtility.defaultWebpage(tmpContact));
//
//                        // add all contact information from inbox emails.
//                        mediator.getContactManager().addContact(tmpContact);
//
//                    }
                    
                }
                
                // schedule a timer task to get account folder updates
                TimerTask timerMailTickelTask =
                        mailAccountBean.createTimerTask(threadPool, mailAccountBean);
                
                mediator.sheduleTickelTimer(timerMailTickelTask,CHECK_MAIL_FREQUENCY);
                
            }
        } else{
            mailAccountBeans = new ArrayList();
        }
        
        // create the settings bean and all manager callback support
        settingsBean = new SettingsBean(mediator);
        settingsBean.setMailManager(this);
        settingsBean.init();
    }
    
    /**
     * Disposes all resources related to this class.  All mail account
     * controls and their respective mail accounts are reset.
     */
    public void dispose() {
        // mark as unitialized
        isInit = false;
        
        if (log.isDebugEnabled()) {
            log.debug(" Disposing MailManager");
        }
        
        // release all associated mail accounts
        if (mailAccountBeans != null) {
            
            // clear search folder, no memory leaks here
            if (searchResultsFolder != null) {
                searchResultsFolder.dispose();
            }
            
            // dispose of the mails account and thus their respective folders
            Iterator mailAccounts = mailAccountBeans.iterator();
            while (mailAccounts.hasNext()) {
                ((MailAccountBean)mailAccounts.next()).dispose();
            }
            mailAccountBeans.clear();
            mailAccountBeans = null;
        }
        
    }
    
    /**
     * Adds more reciepent fields to the currently selected Message View.
     * @param event
     */
    public void addMoreRecipients(ActionEvent event) {
        this.getSelectedMailAccount().getSelectedMessageBean().addMoreRecipients();
        
    }
    
    /**
     * Refreshses all the mail accounts for this user.  This forces the mail
     * account subfolders to update their respective messages lists.
     */
    public void refreshMailAccounts(ActionEvent actionEvent) {
        try{
            MailAccountBean tmpAccount;
            MailAccountControl tmpControl;
            ArrayList tmpFolders;
            MailFolderBean tmpFolder;
            // loop through each account and refresh MailfoldersBeans
            for (int i = mailAccountBeans.size() - 1; i >= 0; i--) {
                
                if (mailAccountBeans.get(i) instanceof MailAccountBean) {
                    tmpAccount = (MailAccountBean) mailAccountBeans.get(i);
                    if(tmpAccount.getMailControl().isConnected()){
                        tmpControl = tmpAccount.getMailControl();
                        tmpFolders = tmpAccount.getMailFolders();
                        // refresh each folder
                        for (int j = tmpFolders.size() - 1; j >= 0; j--) {
                            tmpFolder = (MailFolderBean) tmpFolders.get(j);
                            tmpControl.tickleMailAccount(tmpFolder);
                        }
                    }
                }
                
            }
            WebmailMediator.addMessage("navInc:n",
                    "checkMessage",
                    null,
                    ("Finished checking "+mailAccountBeans.size()+" mail accounts"));
        } catch(Throwable ex){
            try{
                FacesContext facesContext =  FacesContext.getCurrentInstance();
                
                // make sure we have a valid facesContext
                if (facesContext != null){
                    
                    HttpSession httpSession =
                            (HttpSession) facesContext.getExternalContext()
                            .getSession(false);
                    if (log.isDebugEnabled()) {
                        log.debug("session 1 " + httpSession.getId());
                    }
                    httpSession.invalidate();
                    
                    facesContext =  FacesContext.getCurrentInstance();
                    httpSession =
                            (HttpSession) facesContext.getExternalContext()
                            .getSession(false);
                    if (log.isDebugEnabled()) {
                        log.debug("session 2 " + httpSession.getId());
                    }
                    facesContext.getExternalContext().redirect("./error.html");
                }
            }catch (IOException e){
                log.error("On error rederect error", e);
            }
        }
    }
    
    /**
     * Registers the specified mailAcount with the manager.  The navigationManager
     * is called on and a new navigation node is requested for the mailAccount
     * object.
     *
     * @param mailAccount mail account to register with manager
     */
    public void addMailAccount(MailAccountBean mailAccount) {
        if (mailAccountBeans != null) {
            // register the new mail account with the controller
            
            // get the NavigationManager
            NavigationManager navigationManager = mediator.getNavigationManager();
            
            // see if mail account is already in tree
            navigationManager.removeMailAccountNode(mailAccount);
            
            // add a new node to the navigation manager for this account
            navigationManager.addMailAccountNode(mailAccount);
            
        }
    }
    
    /**
     * Removes the mail control specified by the mail account.  If no control
     * is removed false is return; otherwise, true.
     *
     * @param mailAccount
     * @return true if control has been removed, false otherwise.
     */
    protected boolean removeMailAccount(MailAccountBean mailAccount) {
        
        // find mailAccount in arralist
        if (mailAccountBeans.contains(mailAccount)) {
            
            // remove the internal reference
            mailAccountBeans.remove(mailAccount);
            
            // update the navigation manager
            NavigationManager navigationManager = mediator.getNavigationManager();
            // remove the node
            navigationManager.removeMailAccountNode(mailAccount);
            
            // confirm the removal
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Gets all registered mail accounts associated with this manager.
     *
     * @return all register mail accounts
     */
    public List getMailAccounts() {
        return mailAccountBeans;
    }
    
    /**
     * Gets the number of mail accounts associated with this manager.
     *
     * @return number of mail accounts.
     */
    public int getMailAccountSize() {
        if (mailAccountBeans != null) {
            return mailAccountBeans.size();
        } else {
            return 0;
        }
    }
    
    /**
     * Gets the selected mail account.  The selected mail account is used
     * by the emailFolderviewPanel and emailEditFolderViewPanel views.
     *
     * @return selected mail account.
     */
    public MailAccountBean getSelectedMailAccount() {
        return selectedMailAccount;
    }
    
    /**
     * Sets the selectedMailAccount.  Under normal cercomstances this only happens
     * when a user selects a node on the navigation tree mail account branch.
     *
     * @param selectedMailAccount
     */
    public void setSelectedMailAccount(MailAccountBean selectedMailAccount) {
        this.selectedMailAccount = selectedMailAccount;
    }
    
    /**
     * Utility method for finding the first selected message in a the current
     * selected folder.
     *
     * @return first selected messages in folder
     */
    private MessageBean findSelectedMessage() {
        // get the selected folder
        MailFolderBean folder = selectedMailAccount.getSelectedMailFolder();
        if (folder != null) {
            // find the select messages and set it to the new current message
            ArrayList messages = folder.getMessageList();
            MessageBean tmpMessage;
            for (int i = messages.size() - 1; i >= 0; i--) {
                tmpMessage = (MessageBean) messages.get(i);
                if (tmpMessage.isSelected()) {
                    selectedMailAccount.setSelectedMessageBean(tmpMessage);
                }
            }
        }
        return selectedMailAccount.getSelectedMessageBean();
    }
    
    
    /**
     * Creates a new message.  A new mail message can only be created if there
     * is at least one valid mail account associated with the manager.
     *
     * @param actionEvent
     */
    public void createNewMessage(ActionEvent actionEvent) {
        
        if (log.isDebugEnabled()) {
            log.debug("create new mail message");
        }
        
        // if now selected mail account get the first
        if (selectedMailAccount == null &&
                mailAccountBeans != null &&
                mailAccountBeans.size() > 0) {
            selectedMailAccount = (MailAccountBean) mailAccountBeans.get(0);
        }
        
        if (selectedMailAccount != null &&
                selectedMailAccount.getSelectedMailFolder() == null) {
            
            ArrayList mailFolders = selectedMailAccount.getMailFolders();
            if (mailFolders != null && mailFolders.size() > 0) {
                selectedMailAccount.setSelectedMailFolder(
                        (MailFolderBean) mailFolders.get(0));
            } else if (log.isDebugEnabled()) {
                log.debug("Error creating a new message, no mails accounts. ");
            }
        }
        
        // if we have a selected mail account create the new message
        if (selectedMailAccount != null) {
            
            MailAccountControl mailControl = selectedMailAccount.getMailControl();
            MessageBean newMessage = mailControl.createNewMessage();
            selectedMailAccount.setSelectedMessageBean(newMessage);
        } else {
            log.error("Could no create a new message, no valid mail accounts");
        }
        
    }
    
    /**
     * Views the selected message in a folder view. This method should only be
     * called if there is more then one selected message.
     *
     * @param actionEvent
     */
    public void viewSelectedMessage(ActionEvent actionEvent) {
        if (log.isDebugEnabled()) {
            log.debug("View message ");
        }
        // finds the first selected mail item adn sets it as the selected message
        findSelectedMessage();
        
        // reset selected message count
        selectedMailAccount.getSelectedMailFolder().deselecteMessageFlags(null);
    }
    
    /**
     * Create a new forward formated message from a message selected in the
     * mail folder email view.
     *
     * @param actionEvent
     */
    public void forwardSelectedMessage(ActionEvent actionEvent) {
        if (log.isDebugEnabled()) {
            log.debug("Forward message");
        }
        
        // find the visually seleced message from the view
        MessageBean tmpMessage = findSelectedMessage();
        
        // reset selected message count
        selectedMailAccount.getSelectedMailFolder().deselecteMessageFlags(null);
        
        // get the mail control and set the selected message as the new
        // selected message which will be used in the edit email view.
        MailAccountControl mailControl = selectedMailAccount.getMailControl();
        // create forward message
        tmpMessage = mailControl.createForwardMessage(tmpMessage);
        selectedMailAccount.setSelectedMessageBean(tmpMessage);
    }
    
    /**
     * Create a new reply to formated message from a message selected in the
     * mail folder email view.
     *
     * @param actionEvent
     */
    public void replyToSelectedMessage(ActionEvent actionEvent) {
        if (log.isDebugEnabled()) {
            log.debug("Reply message");
        }
        
        // find the visually selected message from the view
        MessageBean tmpMessage = findSelectedMessage();
        
        // reset selected message count
        selectedMailAccount.getSelectedMailFolder().deselecteMessageFlags(null);
        
        // get the mail control and set the selected message as the new
        // selected message which will be used in the edit email view.
        MailAccountControl mailControl = selectedMailAccount.getMailControl();
        // create reply message
        tmpMessage = mailControl.createReplyMessage(tmpMessage);
        selectedMailAccount.setSelectedMessageBean(tmpMessage);
    }
    
    /**
     * Create a new reply to all formated message from a message selected in the
     * mail folder email view.
     *
     * @param actionEvent
     */
    public void replyToAllSelectedMessage(ActionEvent actionEvent) {
        if (log.isDebugEnabled()) {
            log.debug("Reply all message");
        }
        // find the visually selected message from the view
        MessageBean tmpMessage = findSelectedMessage();
        
        // reset selected message count
        selectedMailAccount.getSelectedMailFolder().deselecteMessageFlags(null);
        
        // get the mail control and set the selected message as the new
        // selected message which will be used in the edit email view.
        MailAccountControl mailControl = selectedMailAccount.getMailControl();
        // create reply to all message
        tmpMessage = mailControl.createReplyAllMessage(tmpMessage);
        selectedMailAccount.setSelectedMessageBean(tmpMessage);
    }
    
    /**
     * Sends the a message specified by the selected mail accounts selected
     * message model.  Any errors during the send process will be written to the
     * log.
     *
     */
    public String sendSelectedMessage() {
        if (log.isDebugEnabled()) {
            log.debug("Sending message");
        }
        
        MailAccountControl mailControl = selectedMailAccount.getMailControl();
        
        // refresh selected message
        MessageBean tmpMessage = selectedMailAccount.getSelectedMessageBean();
        
        //populate the recipients
        tmpMessage.populateRecipients();
        
        // do some easy error checking before sending message
        if ( ("").equals(tmpMessage.getRecipientsTo())){
            WebmailMediator.addMessage("eevp:emailEditForm",
                    "errorMessage",
                    "webmail.email.edit.error.recipients",null);
            return null;
        }
        
        if ( ("").equals(tmpMessage.getSubject())){
            tmpMessage.setSubject("(no subject)");
            WebmailMediator.addMessage("eevp:emailEditForm",
                    "errorMessage",
                    "webmail.email.edit.error.subject", null);
            return null;
        }
        
        // send the selected message
        boolean messageSendSuccess = mailControl.sendMessage(tmpMessage);
        
        if (messageSendSuccess){
            // refresh selected message
            tmpMessage = selectedMailAccount.getSelectedMessageBean();
            
            // change flag on message
            try {
                tmpMessage.getMessage().setFlag(Flags.Flag.SEEN, true);
            } catch (MessagingException e) {
                log.error("Error setting message flag to seen");
            }
            
            // move the sent message to the sent messages folder.
            mailControl.moveMessage(tmpMessage, SENT_FOLDER_NAME);
            
            // add recipient to the contact list if it's not yet in it
            addRecipient(tmpMessage,true,true,false);
            
            // finally change to view to the selecte folder list
            return navigateToMessageList();
        } else{
            WebmailMediator.addMessage("eevp:emailEditForm",
                    "errorMessage",
                    "webmail.email.edit.error",null);
            return null;
        }
    }
    
    /**
     * Utility method silently add the recipients of a message to contact list
     */
    private void addRecipient(MessageBean msg, boolean addTo, boolean addCc, boolean addBcc){
        
        
        if(addTo){
            Address[] toList=null;
            try{
                toList = msg.getMessage().getRecipients(MimeMessage.RecipientType.TO);
            }catch(MessagingException e){log.error("Get recipient error",e);}
            
            if(toList != null){
                for(int i=0;i<toList.length;i++){
                    addPerson(Message.getPersonal(toList[i].toString()),Message.getEmail(toList[i].toString()));
                }
            }
        }
        
        if(addCc){
            Address[] ccList=null;
            try{
                ccList = msg.getMessage().getRecipients(MimeMessage.RecipientType.CC);
            }catch(MessagingException e){log.error("Get recipient error",e);}
            
            if(ccList != null){
                for(int i=0;i<ccList.length;i++){
                    addPerson(Message.getPersonal(ccList[i].toString()),Message.getEmail(ccList[i].toString()));
                }
            }
        }
        
        if(addBcc){
            Address[] bccList=null;
            try{
                bccList = msg.getMessage().getRecipients(MimeMessage.RecipientType.BCC);
            }catch(MessagingException e){log.error("Get recipient error",e);}
            
            if(bccList != null){
                for(int i=0;i<bccList.length;i++){
                    addPerson(Message.getPersonal(bccList[i].toString()),Message.getEmail(bccList[i].toString()));
                }
            }
        }
        
    }
    
    
    /**
     * Utility method silently add a person to contact list if it's not yet in it
     */
    private void addPerson(String name, String email){
        
        ContactBean tmpContact = new ContactBean();
        tmpContact.setDisplayName(name);
        tmpContact.setPrimaryEmail(email);
        tmpContact.setFirst("");
        tmpContact.setLast("");
        tmpContact.setInitials("");
        tmpContact.setMiddle("");
        tmpContact.setNickName("");
        tmpContact.setNote("");
        tmpContact.setPrimaryPhone("");
        tmpContact.setUserName("");
        
        //create predefined collections and assign them
        //to the new bean
        tmpContact.setAddresses(ContactModelUtility.defaultAddress(tmpContact));
        tmpContact.setEmails(ContactModelUtility.defaultEmail(tmpContact));
        tmpContact.setPhones(ContactModelUtility.defaultPhone(tmpContact));
        tmpContact.setWebpages(ContactModelUtility.defaultWebpage(tmpContact));
        
        //add all contact information from inbox emails.
        mediator.getContactManager().addContact(tmpContact);
        
        
        
    }
    
    
    
    
    /**
     * Prepares the selectedMessage bean for editing.  In most cases this method
     * is only called on a message that is flaged as draft in the draft folder.
     * This method willl detect if the message is readonly, if it is then we
     * make a copy and assign a reference to the saved message.  If the message
     * is writeable then we do nothing as it can be edited.
     *
     * @param actionEvent
     */
    public void editSelectedMessage(ActionEvent actionEvent) {
        if (log.isDebugEnabled()) {
            log.debug("Editing Selected message");
        }
        readyEditMessage();
        
        // reset selected message count
        selectedMailAccount.getSelectedMailFolder().deselecteMessageFlags(null);
    }
    
    
    /**
     * Saves the selected message state. If the message is new it is saved
     * to the DRAFT_FOLDER_NAME. If the message has no savedMessage attached
     * to it we can assume it is new and we save a copy to the DRAFT_FOLDER_NAME.
     * If the message has a savedMessage attached we must first save the current
     * state, remove the savedMessage reference, make a copy of the message and
     * save it to the draft folder, and finally assing the new saved folder
     * copy to the current editiable message.
     *
     * @param actionEvent
     * @see MailAccountControl#saveMesssage(MessageBean);
     */
    public void saveSelectedMessage(ActionEvent actionEvent) {
        if (log.isDebugEnabled()) {
            log.debug("Save Selected message");
        }
        
        // save the current message
        readySavedMessage();
        // ready the current message for edity
        readyEditMessage();
    }
    
    /**
     * Saves the currently selected mail message to the draft folder.  When the
     * message is saved it's flag is set to DRAFT which allows a user to re-edit
     * the message at a later time.
     * @return null
     */
    public String saveDraftMessage(){
        if (log.isDebugEnabled()) {
            log.debug("Save Draft message");
        }
        
        // save the current message
        readySavedMessage();
        
        // ready the current message for edity
        // readyEditMessage();
        
        // try and force an view update after the append, as the javaMail append
        // methos is not reliably sending a MessageCountEvent.
        MailAccountControl mailControl = selectedMailAccount.getMailControl();
        mailControl.tickleMailAccount(selectedMailAccount.getSelectedMailFolder());
        
        return null;
    }
    
    /**
     * <p>Utility method for saving a message.  The selected message bean is used as
     * the message to save, if the message has no parent folder then it is simply
     * saved to the draft folder, otherwise the message is copied and saved to the
     * draft folder and the origional message is deleted.<p>
     * <p/>
     * <p>Why do we have to copy-delete?  Mime message content is emuteable. </p>
     */
    private void readySavedMessage(){
        if (log.isDebugEnabled()) {
            log.debug("Ready Save Message");
        }
        
        try {
            // get the selected account and message.
            MailAccountControl mailControl = selectedMailAccount.getMailControl();
            MessageBean message = selectedMailAccount.getSelectedMessageBean();
            
            message.populateRecipients();
            // new message save handling
            if (message.getParentFolder() == null){
                // saves state of message, does not save it to a folder
                mailControl.saveMesssage(message);
                
                // append the message to the draft folder
                mailControl.appendMessage(message, DRAFT_FOLDER_NAME);
            }
            // existing message save handling, message already is saved to a folder
            // so we want to remove the old and append the new.
            else{
                mailControl.saveMesssage(message);
                
                // delete the saved copy
                mailControl.deleteMessage(
                        new javax.mail.Message[]{message.getSavedMessage()},
                        true);
                
                // append the current message to the draft folder
                mailControl.appendMessage(message, DRAFT_FOLDER_NAME);
                
            }
            
        } catch (Throwable e) {
            log.error("Error saving message", e);
        }
        
    }
    
    /**
     * Utility method for editing a message.  The selected MessageBean is copied
     * so that it can be modified.  The new copy is set as the selected message
     * and a reference to the emuteable copy is stored so that it can be deleted
     * if the message is saved again.
     */
    private void readyEditMessage(){
        if (log.isDebugEnabled()) {
            log.debug("Ready Edit Message");
        }
        try {
            // get the selected account and message.
            MailAccountControl mailControl = selectedMailAccount.getMailControl();
            MessageBean message = selectedMailAccount.getSelectedMessageBean();
            
            // copy message
            MessageBean copiedMessage = mailControl.copyMessage(message);
            
            // make sure recipientList in sync
            copiedMessage.updateRecipients(false);
            
            // set saved link
            copiedMessage.setSavedMessage(message.getMessage());
            
            // set the copy as the new selected message
            selectedMailAccount.setSelectedMessageBean(copiedMessage);
            
        } catch (Throwable e) {
            log.error("Error editing message", e);
        }
    }
    
    /**
     * Deletes the current message.  If the message is not already in trash folder
     * it is moved to the trash folder.  If a message is in the trash folder it
     * is deleted.
     * @param actionEvent
     */
    public void deleteCurrentMessage(ActionEvent actionEvent) {
        if (log.isDebugEnabled()) {
            log.debug("Delete current message");
        }
        
        // delete all the selected emails in the given folder.
        MailFolderBean folder = selectedMailAccount.getSelectedMailFolder();
        if (folder != null) {
            
            MailAccountControl mailControl = selectedMailAccount.getMailControl();
            
            // If we are not in the "Trash" folder we just want to mark and move
            // the messages.
            if (!folder.getFullName().equals(TRASH_FOLDER_NAME)) {
                mailControl.moveMessages(new MessageBean[]
                {selectedMailAccount.getSelectedMessageBean()},
                        folder.getFullName(),
                        TRASH_FOLDER_NAME);
            }
            // otherwise we want exponge the folder
            else {
                // mark selected messages as deleted.
                mailControl.deleteMessage(new MessageBean[]
                {selectedMailAccount.getSelectedMessageBean()}, true);
            }
            
            // tickle the mail account.
            mailControl.tickleMailAccount(selectedMailAccount.getSelectedMailFolder());
            
        }
        // we'll save the exponge for when the user logs off
        // the email will be marked as delete via css.
        
        // message bean to null
        selectedMailAccount.setSelectedMessageBean(null);
        
    }
    
    /**
     * Deletes the a message specified by the selected mail accounts selected
     * message model.
     *
     * @param actionEvent
     */
    public void deleteSelectedMessage(ActionEvent actionEvent) {
        
        // delete all the selected emails in the given folder.
        MailFolderBean folder = selectedMailAccount.getSelectedMailFolder();
        if (folder != null) {
            // find the select messages and set it to the new current message
            ArrayList messages = folder.getMessageList();
            ArrayList deleteMessageList = new ArrayList();
            MessageBean tmpMessage;
            for (int i = 0; i < messages.size(); i++) {
                tmpMessage = (MessageBean) messages.get(i);
                if (tmpMessage.isSelected()) {
                    deleteMessageList.add(tmpMessage);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Delete selected messages " + deleteMessageList.size());
            }
            
            MailAccountControl mailControl = selectedMailAccount.getMailControl();
            
            // If we are not in the "Trash" folder we just want to mark and move
            // the messages.
            if (!folder.getFullName().equals(TRASH_FOLDER_NAME)) {
                
                mailControl.moveMessages(deleteMessageList.toArray(),
                        folder.getFullName(),
                        TRASH_FOLDER_NAME);
            }
            // otherwise we want deponge the folder
            else {
                
                // mark selected messages as deleted.
                mailControl.deleteMessage(deleteMessageList.toArray(), true);
            }
            // reset selected message count
            folder.deselecteMessageFlags(null);
            
            // tickle the mail account.
            mailControl.tickleMailAccount(
                    selectedMailAccount.getSelectedMailFolder());
            mailControl.tickleMailAccount(
                    mailControl.findFolder(MailManager.TRASH_FOLDER_NAME));
            
        }
        
        // message bean to null
        selectedMailAccount.setSelectedMessageBean(null);
        
    }
    
    /**
     * Navigate to the editable Message panel
     */
    public String navigateToEditMessage() {
        
        // navigate to the tasksEditViewPanel
        NavigationSelectionBean navigationSelectionBean =
                mediator.getNavigationManager().getNavigationSelectionBean();
        
        // apply pre-defined navigation rue.
        navigationSelectionBean.setSelectedPanel(messageEditNavigation);
        
        return null;
    }
    
    /**
     * Navigate to the view Message search panel
     */
    public String navigateToViewMessageSearch() {
        
        // reset selected message count
        selectedMailAccount.getSelectedMailFolder().deselecteMessageFlags(null);
        
        // navigate to the tasksEditViewPanel
        NavigationSelectionBean navigationSelectionBean =
                mediator.getNavigationManager().getNavigationSelectionBean();
        
        // apply pre-defined navigation rue.
        navigationSelectionBean.setSelectedPanel(messageSearchListNavigation);
        
        return null;
    }
    
    /**
     * Navigate to the view Message panel
     */
    public String navigateToViewMessage() {
        
        // reset selected message count
        selectedMailAccount.getSelectedMailFolder().deselecteMessageFlags(null);
        
        // navigate to the tasksEditViewPanel
        NavigationSelectionBean navigationSelectionBean =
                mediator.getNavigationManager().getNavigationSelectionBean();
        
        // apply pre-defined navigation rue.
        navigationSelectionBean.setSelectedPanel(messageViewNavigation);
        
        return null;
    }
    
    /**
     * Navigate to the selected account mail box.
     */
    public String navigateToSelectedAccountInbox() {
        
        MailFolderBean mfb = selectedMailAccount.getMailControl().findFolder(INBOX_FOLDER_NAME);
        
        if(mfb != null){
            // set the inbox as the selected mail folder
            mfb.init();
            selectedMailAccount.setSelectedMailFolder(mfb);
            
            
        }
        // navigate to the message list, this shows current mail folder.
        navigateToMessageList();
        return null;
        
    }
    
    /**
     * Navigate to the task list panel
     */
    public String navigateToMessageList() {
        
        // navigate to the tasksEditViewPanel
        NavigationSelectionBean navigationSelectionBean =
                mediator.getNavigationManager().getNavigationSelectionBean();
        
        // apply pre-defined navigation rue.
        navigationSelectionBean.setSelectedPanel(messageListNavigation);
        
        // update view, just in case
        selectedMailAccount.getSelectedMailFolder().removeExpunged();
        
        return null;
    }
    
    /**
     * Navigate to contact view pane.
     *
     * @return null, no navigation via JSF.
     */
    public String navigateToContact() {
        
        // navigate to the tasksEditViewPanel
        NavigationSelectionBean navigationSelectionBean =
                mediator.getNavigationManager().getNavigationSelectionBean();
        
        // apply pre-defined navigation rue.
        navigationSelectionBean.setSelectedPanel(ContactManager.getContactEditNavigation());
        
        return null;
    }
    
    /**
     * Navigation command to show the settings panel.
     * @return null.
     */
    public String navigateToSettings() {
        // navigate to the settings panel
        NavigationSelectionBean navigationSelectionBean =
                mediator.getNavigationManager().getNavigationSelectionBean();
        
        // Update the selected mail account
        //settingsBean.setSelected(selectedMailAccount);
        
        // apply pre-defined navigation rule.
        navigationSelectionBean.setSelectedPanel(settingsNavigation);
        
        return null;
    }
    
    /**
     * Adds a email address specified in an email to the contact list if, it
     * is currently not in list.
     *
     * @param event
     */
    public void addSender(ActionEvent event) {
        
        // Get contanct manager.
        ContactManager contactManager = mediator.getContactManager();
        
        // navigation flag for setting back button on view.
        contactManager.getContactListBean().setFromMail(true);
        
        // setup a new Contact bean.
        
        ContactBean contactBean = new ContactBean();
        contactBean.setDisplayName(Message.getPersonal(this.findSelectedMessage().getSender()));
        contactBean.setFirst("");
        contactBean.setLast("");
        contactBean.setInitials("");
        contactBean.setMiddle("");
        contactBean.setNickName("");
        contactBean.setNote("");
        contactBean.setPrimaryEmail(Message.getEmail(this.findSelectedMessage().getSender()));
        contactBean.setPrimaryPhone("");
        contactBean.setUserName("");
        
        //create predefined collections and assign them
        //to the new bean
        contactBean.setAddresses(ContactModelUtility.defaultAddress(contactBean));
        contactBean.setEmails(ContactModelUtility.defaultEmail(contactBean));
        contactBean.setPhones(ContactModelUtility.defaultPhone(contactBean));
        contactBean.setWebpages(ContactModelUtility.defaultWebpage(contactBean));
        
        // finally add the new contact to the contact list.
        contactManager.getContactListBean().setContactBean(contactBean);
    }
    
    /**
     * Gets currently viewed message list navigation content.  This can be used
     * to change the view back to an origonal message list after a message edit.
     * @return currently visited message list, folder view.
     */
    public NavigationContent getMessageListNavigation() {
        return messageListNavigation;
    }
    
    /**
     * Sets the message list navigation.
     *
     * @see #getMessageListNavigation()
     * @param messageListNavigation
     */
    public void setMessageListNavigation(
            NavigationContent messageListNavigation) {
        MailManager.messageListNavigation = messageListNavigation;
    }
    
    /**
     * Deselect all selected messages.
     * @param event
     */
    public void cancelSelectedMessage(ActionEvent event){
        selectedMailAccount.getSelectedMailFolder().deselectAll(null);
    }
    
    
    /**
     * set the value of process for updating process bar
     * @param process, the amount of task completion for webmail
     * intialization process
     */
    public void setProcess(int process) {
        this.process = process;
    }
    
    /**
     * get the current value of process bar
     */
    public int getProcess() {
        return process;
    }
    
    /**
     * return the boolean value of showModalPanel
     * MoalPanel displays if showModalPanel value is true
     */
    public boolean isshowModalPanel() {
        return showModalPanel;
    }
    
    /**
     * set the boolean value of showModalPanel
     * @param showModalpanel, ModalPanel displays if
     * showModalPanel sets to true
     */
    public void setshowModalPanel(boolean showModalPanel) {
        this.showModalPanel = showModalPanel;
    }
    
    /**
     * Update the output text on the process bar to to reflect the
     * current intialization process of login webmail
     */
    public void setProcessText(String processText) {
        this.processText = processText;
    }
    
    /**
     * Get the output text on the process bar to reflect the
     * current intialization process of login webmail
     */
    public String getProcessText() {
        return processText;
    }
    
    /**
     * Listener method for closing the modal popup window
     */
    public void closeModalPopup(ActionEvent e) {
        showModalPanel = false;
    }
    
    /**
     *  Thread method that initializes all other manager classes  The progress bar
     * updates based on this task completion via requestRender() calls.
     */
    public static class MailManagersOperationRunner implements Runnable {
        
        WebmailMediator outputBean;
        public MailManagersOperationRunner(WebmailMediator outputBean) {
            this.outputBean = outputBean;
        }
        
        public void run() {
            try {
                // pause the thread
                Thread.sleep(1);
                
                // initialize contactManager and update process value
                if (outputBean.getContactManager() != null) {
                    outputBean.getContactManager().setMediator(outputBean);
                    outputBean.getContactManager().init();
                    outputBean.getMailManager().setProcess(30);
                    outputBean.getMailManager().setProcessText("Contact Manager Initializing");
                    outputBean.renderHelper();
                    
                }
                // init tasks manager from db and update process value.
                if (outputBean.getTasksManager() != null) {
                    outputBean.getTasksManager().setMediator(outputBean);
                    outputBean.getTasksManager().init();
                    outputBean.getMailManager().setProcess(50);
                    outputBean.getMailManager().setProcessText("Tasks Manager Initializing");
                    outputBean.renderHelper();
                }
                
                // init navigation manager, builds tree from difference manager data and update process value
                if (outputBean.getNavigationManager() != null) {
                    outputBean.getNavigationManager().setMediator(outputBean);
                    outputBean.getNavigationManager().init();
                    outputBean.getMailManager().setProcess(75);
                    outputBean.getMailManager().setProcessText("Navigation Manager Initializing");
                    outputBean.renderHelper();
                }
                
                // init mail manager, loads accounts from database and setup map
                if (outputBean.getMailManager() != null) {
                    outputBean.getMailManager().setMediator(outputBean);
                    outputBean.getMailManager().setProcessText("Mail Manager Initializing");
                    outputBean.getMailManager().init();
                    outputBean.renderHelper();
                }
            } catch (InterruptedException e) {
                log.error("mail managers intialization error", e);
            }
            // redirect to index.jsp page after all the intialization process finished and
            // close the modal popup window
            outputBean.getState().redirectTo("./index.jsp");
            outputBean.getMailManager().setshowModalPanel(false);
        }
        
    }
    
}
