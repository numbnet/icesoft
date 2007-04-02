/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.navigation;

import com.icesoft.applications.faces.webmail.mail.MailAccount;
import com.icesoft.applications.faces.webmail.mail.MailAccountBean;
import com.icesoft.applications.faces.webmail.mail.MailManager;
import com.icesoft.faces.component.dragdrop.DropEvent;

import javax.faces.event.ActionEvent;

/**
 * <p>This is just a wrapper for a MailAccountBean so that it can be managed
 * in the context of the NavigationManager for displaying content.<p>
 *
 * <p>The MailAccountBean represents the root node for a mail account and child
 * folder object (MailFolderNavigationContentBean) objects are hunge from it.<p>
 *
 * @since 1.0
 */
public class MailAccountNavigationContentBean extends NavigationContent {

    private MailAccountBean mailAccount;

    public MailAccountNavigationContentBean() {
        super();
        setLeafIcon("images/folderclosed.gif");
    }

    public MailAccount getMailAccount() {
        return mailAccount;
    }

    public void setMailAccount(MailAccountBean mailAccount) {
        this.mailAccount = mailAccount;
    }

    /**
     * Sets the navigationSelctionBeans selected state to this NavigationContent.
     * When selected the selected mail account value is set to this objects
     * associated MailAccountBean objec.t
     */
    public void contentVisibleAction(ActionEvent event) {
        // we want to set the MailManger selectedAccount state.
        MailManager mailManager = mailAccount.getMailManager();
        mailManager.setSelectedMailAccount(mailAccount);

        // if no connection try to connect to account
        mailAccount.getMailControl().connect();

        // set navigation selected panel
        super.contentVisibleAction(event);
    }

    /**
     * Called when an drop event occures on this navigation content.  Drag and
     * drop is not support at the folder level and as a result a dropFailureEffect
     * will always be fired. 
     *
     * @param event drop event.
     */
    public void navigationDropAction(DropEvent event) {
        currentEffect = dropFailureEffect;
        dropFailureEffect.setFired(false);
    }


}