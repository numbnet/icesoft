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
import com.icesoft.applications.faces.webmail.util.encryption.PasswordHash;
import edu.emory.mathcs.backport.java.util.concurrent.ExecutorService;

import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import javax.faces.application.Application;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.List;
import java.util.Iterator;

/**
 * <p>The MailAccountBean is the corresponding view object for the underlying
 * MailAccount model.</p>
 *
 * @since 1.0
 */
public class MailAccountBean extends MailAccount
        implements WebmailBase, Runnable {

    // mail manager callback
    protected MailManager mailManager;

    // mail controller
    protected MailAccountControl mailControl;

    protected ArrayList mailFolders;

    // selected mail account bean
    private MailFolderBean selectedMailFolder;
    private MailFolderBean previousSelectedMailFolder;

    // selected message
    private MessageBean selectedMessageBean;

    // timer used to tickel mail account
    private TimerTask tickelTimerTask;

    //
    FacesContext facesContext;

    /**
     * Create a new instance of a MailAccountBean.
     */
    public MailAccountBean() {
        super();
        mailControl = new MailAccountControl(this);
        facesContext = FacesContext.getCurrentInstance();
    }

    public MailManager getMailManager() {
        return mailManager;
    }

    public void setMailManager(MailManager mailManager) {
        this.mailManager = mailManager;
    }

    public MailAccountControl getMailControl() {
        return mailControl;
    }

    /**
     * Set the selected account of parent manager
     *
     * @param event
     */
    public void changeSelectedAccount(ActionEvent event) {
        this.getMailManager().setSelectedMailAccount(this);
        MailFolderBean mailFolderBean = this.getMailControl().findFolder("INBOX");
        if (mailFolderBean != null) {
            mailFolderBean.init();
            this.setSelectedMailFolder(mailFolderBean);

        }
    }

    /**
     * Gets the selected mail account.  This only occurs when a user selects
     * a different mail folder in the navigation tree.
     *
     * @return selected mail folder.
     */
    public MailFolderBean getSelectedMailFolder() {
        return selectedMailFolder;
    }

    public ArrayList getMailFolders() {
        return mailFolders;
    }

    public void setMailFolders(ArrayList mailFolders) {
        this.mailFolders = mailFolders;
    }

    /**
     * Sets the selected mail folder.
     *
     * @param selectedMailFolder
     */
    public void setSelectedMailFolder(MailFolderBean selectedMailFolder) {
        previousSelectedMailFolder = this.selectedMailFolder;
        this.selectedMailFolder = selectedMailFolder;
    }

    /**
     * Gets the previously selected mail folder.
     */
    public MailFolderBean getPreviouslySelectedMailFolder() {
        return previousSelectedMailFolder;
    }

    /**
     * Gets the selected message bean. This message can be either associateded
     * with a mail folder or it might be new message yet to have been saved
     * to the draft folder or sent and thus sent to the sent items folder.
     *
     * @return the current message bean, null if now is selected.
     */
    public MessageBean getSelectedMessageBean() {
        return selectedMessageBean;
    }

    /**
     * Sets the selected message bean to the specified message.
     *
     * @param selectedMessageBean new message to set as selected, null is a valid
     *                            setter value.
     */
    public void setSelectedMessageBean(MessageBean selectedMessageBean) {
        this.selectedMessageBean = selectedMessageBean;
    }


    /**
     * Get the email address associated with this account.
     *
     * @return email address of the user account.
     */
    public String getEmail() {
        return mailUsername + "@" + host;
    }

    /**
     * Create a MailAccountBean object out of a MailAccount model.  The MailAccounts
     * data is copied to a new instance of a MailAccountBean object.
     *
     * @param mailAccount mail account data to copy.
     * @return MailAccount bean object with the same data as the origional
     *         MailAccount object.
     */
    public static MailAccountBean createMailAccountBean(MailAccount mailAccount) {
        MailAccountBean mailAccountBean = new MailAccountBean();
        mailAccountBean.id = mailAccount.id;
        mailAccountBean.setUserName(mailAccount.getUserName());
        mailAccountBean.setHost(mailAccount.getHost());
        mailAccountBean.setMailUsername(mailAccount.getMailUsername());
        mailAccountBean.setPassword(PasswordHash.decrypt(mailAccount.getPassword()));
        mailAccountBean.setProtocol(mailAccount.getProtocol());

        mailAccountBean.setIncomingPort(mailAccount.getIncomingPort());
        mailAccountBean.setIncomingHost(mailAccount.getIncomingHost());
        mailAccountBean.setIncomingSsl(mailAccount.isIncomingSsl());

        mailAccountBean.setOutgoingPort(mailAccount.getOutgoingPort());
        mailAccountBean.setOutgoingHost(mailAccount.getOutgoingHost());
        mailAccountBean.setOutgoingSsl(mailAccount.isOutgoingSsl());

        mailAccountBean.setOutgoingVerification(mailAccount.isOutgoingVerification());


        mailAccountBean.setIncomingSsl(mailAccount.isIncomingSsl());
        return mailAccountBean;
    }

    /**
     * Initiats the mail class.
     */
    public void init() {

    }

    /**
     * Refreshes internal list of folders in this account.
     */
    public void refreshFolderList() {
        // get  fresh list of mail folders
        if (mailFolders != null) {
            mailFolders.clear();
        }
        mailFolders = mailControl.getFolderList();
    }

    /**
     * Dispose this Mail acocunts resources
     */
    public void dispose() {

        // cancel the Timer Task used to tickle account
        if (tickelTimerTask != null){
            tickelTimerTask.cancel();
            tickelTimerTask = null;
        }

        // disconnect from server
        if (mailControl != null) {
            mailControl.disconnect();
        }

        // free up resouces in vector;
        if (mailFolders != null) {
            MailFolderBean mailFolder;
            // dispose resources for a mailfolder.
            for (int i = mailFolders.size() - 1; i >= 0; i--) {
                mailFolder = (MailFolderBean) mailFolders.get(i);
                mailFolder.dispose();
            }
            mailFolders.clear();
        }
    }

    /**
     * <p>When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.<p>
     * <p/>
     * <p>This  particular run method will try and call the mails folders
     * getMethodCount which forces the sever to send EXISTS notifications.
     * and sends any MessageCountEvents to to MessageCountListeners.</p>
     *
     * @see Thread#run()
     */
    public void run() {

        if (FacesContext.getCurrentInstance() != null){
            Application application =
                    FacesContext.getCurrentInstance().getApplication();

            WebmailMediator webmailMediator =
                    ((WebmailMediator) application.createValueBinding("#{webMailMediator}").
                            getValue(FacesContext.getCurrentInstance()));
            // first listern will win on destroying timer.
            if (webmailMediator != null){
                MailManager mailManager = webmailMediator.getMailManager();
                if (mailManager != null){
                    List mailAccounts = mailManager.getMailAccounts();
                    if (mailAccounts != null && !mailAccounts.isEmpty()){
                        Iterator accountIterator  = mailAccounts.iterator();
                        MailAccountBean account;
                        while (accountIterator.hasNext()){
                            account =
                                    (MailAccountBean)accountIterator.next();
                            account.tickleMailAccount();
                        }
                    }
                }
            }
        }
    }

    /***
     * Utility mehtod which tickeles each mail account in this account to make
     * sure that the the mail api will fire events when the folder contents
     * have changed.
     */
    public void tickleMailAccount(){
        // loop through account folders and call the get messageCount methodl.
        if (mailFolders != null) {
            if (log.isDebugEnabled()) {
                log.debug("Refreshing mail account " + getEmail());
            }

            // IMAP folders need to be tickled to send message counter events
            for (int i = mailFolders.size() - 1; i >= 0; i--) {
                mailControl.tickleMailAccount((MailFolderBean) mailFolders.get(i));
            }
        }
    }

    /**
     * Creates a new TimerTask where this MailAccountBean is added to the
     * thread pool and excuted.  Basically every timer_duration the run method
     * of MailAccountBean is called and the folders contents are tickled so
     * that MessageCountListeners events can be fired.
     *
     * @param executorService thread pool
     * @param mailAccountBean mail account bean to run.
     * @return a new Timer task which will execute the execute the mail checking
     *         routine via the thread pool.
     */
    public TimerTask createTimerTask(
            final ExecutorService executorService,
            final MailAccountBean mailAccountBean) {

        tickelTimerTask = new TimerTask() {
            public void run() {
                executorService.execute(mailAccountBean);
            }
        };

        return tickelTimerTask;
    }

    /**
     * Cancels the timer task associated with this mail account.  The timer
     * can not be restarted, createTimerTask() must be called to re-initialize.
     */
    public void cancelTickelTimerTask(){
        if (tickelTimerTask != null){
            tickelTimerTask.cancel();
            tickelTimerTask = null;
        }
    }
}
