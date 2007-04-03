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