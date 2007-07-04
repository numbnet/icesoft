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
package com.icesoft.applications.faces.webmail.settings;

import com.icesoft.applications.faces.webmail.util.encryption.PasswordHash;
import com.icesoft.applications.faces.webmail.mail.MailAccount;
import com.icesoft.applications.faces.webmail.mail.MailAccountBean;
import com.icesoft.applications.faces.webmail.mail.MailManager;
import com.icesoft.applications.faces.webmail.mail.MailAccountTimerCache;
import com.icesoft.applications.faces.webmail.WebmailMediator;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * <p>The Settings Bean is responsible for handling updates to mail account settings.
 * The class interacts with the mail manager to add/remove and edit mail accounts
 * for a particular user.</p>
 *
 * @since 1.0
 */
public class SettingsBean {

    // mail manager pointer
    private MailManager mailManager;

    // list of tabs (one per mail account)
    private List tabList = new ArrayList();

    // back up the accounts in case users cancel the operation
    private List oldAccounts = new ArrayList();

    // controls the index of tab to be displayed
    private int selectedIndex = 0;

    // reference to to mediator
    private WebmailMediator mediator;

    /**
     * Constructs a new Settings Bean.
     * @param mediator reference to webamil mediator object.
     */
    public SettingsBean(WebmailMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * Gets the associated mail manager.
     * @return mail manager associated with this session.
     */
    public MailManager getMailManager() {
        return mailManager;
    }

    /**
     * Sets the mail manager callback.
     * @param mailManager mail manager associated with this session.
     */
    public void setMailManager(MailManager mailManager) {
        this.mailManager = mailManager;
    }

    /**
     * Gets the tab focus of the tabbed pane which displays the settings.
     * @return tabbed pane selected tab index.
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Sets the selected tab focus index.
     * @param i tab focus index, zero based.
     */
    public void setSelectedIndex(int i) {
        selectedIndex = i;
    }

    /**
     * Gets the mail accounts associated with this settings bean.  Each
     * account is represented by one tab.
     * @return list of mail accounts.
     */
    public List getTabList() {
        return tabList;
    }

    /**
     * Sets the tab list.
     * @see #tabList
     * @param tabList
     */
    public void setTabList(List tabList) {
        this.tabList = tabList;
    }

    /**
     * Gets a list of origional mail accounts associated with this user.
     * @return list of old mail accounts.
     */
    public List getOldAccounts() {
        return oldAccounts;
    }

    /**
     * Sets the old Accounts.
     * @param oldAccounts
     */
    public void setOldAccounts(List oldAccounts) {
        this.oldAccounts = oldAccounts;
    }


    /**
     * Init the setting when the applications is loaded
     */
    public void init() {

        List accounts = mailManager.getMailAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            tabList.add(accounts.get(i));
        }

        //back up the accounts
        backup();

    }

    /**
     * In case user cancels the operation, use this method to
     * restore the account info before changes
     */
    private void revert() {
        for (int i = 0; i < oldAccounts.size(); i++) {
            MailAccountBean ma = (MailAccountBean) tabList.get(i);
            //reference needs to be maintained in order to use
            //Hibernate
            MailAccount.copy((MailAccountBean) oldAccounts.get(i), ma);
            tabList.set(i, ma);
        }
        for (int i = oldAccounts.size(); i < tabList.size(); i++) {
            tabList.remove(i);
        }
    }

    /**
     * After each key action, the backup list should be updated too
     */
    private void backup() {
        oldAccounts = new ArrayList();
        for (int i = 0; i < tabList.size(); i++) {
            MailAccountBean ma = new MailAccountBean();
            MailAccount.copy((MailAccountBean) tabList.get(i), ma);
            oldAccounts.add(ma);
        }

    }

    /**
     * Add a empty account into the list
     *
     * @param event
     */
    public void addNewAccount(ActionEvent event) {


        MailAccountBean selectedAccount = new MailAccountBean();
        selectedAccount.setHost("");
        selectedAccount.setIncomingHost("");
        selectedAccount.setIncomingPort(23);
        selectedAccount.setIncomingSsl(false);
        selectedAccount.setMailUsername("");
        selectedAccount.setOutgoingHost("");
        selectedAccount.setOutgoingPort(23);
        selectedAccount.setPassword("");
        selectedAccount.setOutgoingVerification(false);
        selectedAccount.setProtocol("imap");
        selectedAccount.setSelected(false);
        selectedAccount.setUserName(this.getMailManager()
                .getMediator().getLoginManager().getLoginBeanFactory().getLoginBean()
                .getVerifiedUser().getUserName());

        this.tabList.add(selectedAccount);

        //set focus on the newly added account
        selectedIndex = tabList.size() - 1;
    }


    /**
     * Delete all the accounts that has delete flag set
     */
    private void deleteAccount() {

        List newList = new ArrayList();
        List deletedIndex = new ArrayList();

        for (int i = 0; i < tabList.size(); i++) {
            if (((MailAccountBean) tabList.get(i)).isSelected()) {
                deletedIndex.add(new Integer(i));
            } else {
                newList.add(tabList.get(i));
            }
        }

        for (int i = 0; i < deletedIndex.size(); i++) {
            int ind = ((Integer) deletedIndex.get(i)).intValue();
            ((MailAccountBean) tabList.get(ind)).delete();
        }

        tabList = newList;

    }


    /**
     * Update the account information
     *
     */
    public String update() {

        String errorMessage = "";

        // start by deleting accounts has delete flag set
        deleteAccount();

        // remove all mail account nodes from the tree
        mailManager.getMediator().getNavigationManager().removeAllMailAccountNode();

        // remove all timers for this session
        // add a pointer to the timer task to the cache for clean up
        // purposes.
        HttpSession httpSession = null;
        FacesContext facesContext =  FacesContext.getCurrentInstance();
        // make sure we have a valid facesContext
        if (facesContext != null){
            httpSession = (HttpSession) facesContext.getExternalContext()
                    .getSession(false);
        }
        // finally remove the timers for this session.
        if (httpSession != null){
            MailAccountTimerCache.releaseMailAccountTimers(httpSession);
        }

        // insert the current set of nodes into the tree
        for (int i = 0; i < tabList.size(); i++) {

            // get a mail account
            MailAccountBean mailAccountBean = (MailAccountBean) tabList.get(i);
            // stop its timer
            mailAccountBean.cancelTickelTimerTask();

            mailAccountBean.setPassword(PasswordHash.encrypt(mailAccountBean.getPassword()));
            mailAccountBean.save();
            mailAccountBean.setPassword(PasswordHash.decrypt(mailAccountBean.getPassword()));

            // try to connect to the mail account
            if (mailAccountBean.getMailControl().pingMailAccount()) {

                mailAccountBean.setMailManager(this.getMailManager());

                mailAccountBean.init();
                mailAccountBean.refreshFolderList();

                // register the account with hash.
                this.getMailManager().addMailAccount(mailAccountBean);

                // schedule a timer task to get account folder updates
                TimerTask timerMailTickelTask =
                        mailAccountBean.createTimerTask(MailManager.threadPool, mailAccountBean);
                // create new timers for the changed accounts. 
                mediator.sheduleTickelTimer(timerMailTickelTask, MailManager.CHECK_MAIL_FREQUENCY);
            }
            //otherwise add this account to error message
            else {
                errorMessage += " " + mailAccountBean.getEmail();
            }
        }

        if (!("").equals(errorMessage)) {
            WebmailMediator.addMessage(
                    "smp:settingsForm",
                    "errorMessage",
                    "webmail.settings.invalidLogin", errorMessage);
        } else {
            //back up the list
            backup();
            //set focus on the first tab
            selectedIndex = 0;
        }
        return null;
    }

    /**
     * User change is cancelled
     *
     * @param event
     */
    public void cancel(ActionEvent event) {
        //revert the changes
        revert();
        selectedIndex = 0;
    }


}