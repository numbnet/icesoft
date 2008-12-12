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

import com.icesoft.applications.faces.webmail.WebmailBase;
import com.icesoft.applications.faces.webmail.WebmailMediator;
import com.icesoft.applications.faces.webmail.mail.MailAccountBean;
import com.icesoft.applications.faces.webmail.mail.MailFolderBean;
import com.icesoft.faces.component.tree.Tree;

import javax.mail.Folder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * <p>The NavigationTree class is the backing bean for the showcase
 * navigation tree on the left hand side of the applicaiton. Each node in the
 * tree is made up a PageContent which is responsible for the navigation action
 * when a tree node is selected.</p>
 * <p/>
 * <p>When the Tree component binding takes place the tree nodes are initialized
 * and the tree is built.  Any addition to the tree navigation must be made
 * via this class.</p>
 *
 * @since 0.3.0
 */
public class NavigationTree implements WebmailBase {
    // binding to component
    private Tree treeComponent;

    // bound to components value attribute
    private DefaultTreeModel model;

    // root node of tree, for delaid construction
    private DefaultMutableTreeNode rootTreeNode;

    // Mail accounts root node. Allows support for the addition of multiple
    // mails accounts per user account.
    private DefaultMutableTreeNode mailAccountRootNode;

    // map of all navigation backing beans.
    private NavigationSelectionBean navigationBean;

    // link to mediator for applications mediation
    private WebmailMediator mediator;

    // initialization flag
    private boolean isInitiated;

    /**
     * Default constructor of the tree.  The root node of the tree is created
     * at this point.
     */
    public NavigationTree(WebmailMediator mediator,
                          NavigationSelectionBean navigationBean) {
        // build root node so that children can be attached
        NavigationContent rootObject = new NavigationContent();
        rootObject.setNavigationSelection(navigationBean);
        rootObject.setMenuDisplayText("webmail.navigation.rootNode.link");
        rootObject.setMenuContentTitle("webmail.navigation.rootNode.title");
        rootObject.setTemplateName("./inc/contentPanels/webmailSplashPanel.jspx");
        rootObject.setPageContent(true);

        rootTreeNode = new DefaultMutableTreeNode(rootObject);
        rootObject.setWrapper(rootTreeNode);

        // set model binding for bound component
        model = new DefaultTreeModel(rootTreeNode);

        this.navigationBean = navigationBean;
        this.mediator = mediator;
    }

    /**
     * Uitility method to build the entire navigation tree.
     */
    public void init() {
        if (isInitiated)
            return;

        // set init flag
        isInitiated = true;

        if (rootTreeNode != null) {

            // set this node as the default page to view
            navigationBean.setSelectedPanel((NavigationContent) rootTreeNode.getUserObject());

            /**
             * Generate the Mail Account node of the tree.  The Navigation
             * manager will privide an interface from where the Account
             * manager can add new nodes to the tree for each mail account
             * registered with the user.
             */
            NavigationContent mailAccountsFolder = new NavigationContent();
            mailAccountsFolder.setNavigationSelection(navigationBean);
            mailAccountsFolder.setMenuDisplayText("webmail.navigation.mailAccounts.link");
            mailAccountsFolder.setMenuContentTitle("webmail.navigation.mailAccounts.title");
            mailAccountsFolder.setTemplateName("./inc/contentPanels/mailAccountsSplashPanel.jspx");

            // create node and set callback
            mailAccountRootNode = new DefaultMutableTreeNode(mailAccountsFolder);
            mailAccountsFolder.setWrapper(mailAccountRootNode);

            // finally add the mail account branch to the
            rootTreeNode.add(mailAccountRootNode);

            /**
             * Build Tasks Node
             */
            TaskFolderNavigationContentBean taskFolder =
                    new TaskFolderNavigationContentBean();
            taskFolder.setNavigationSelection(navigationBean);
            taskFolder.setTasksManager(mediator.getTasksManager());
            taskFolder.setMenuDisplayText("webmail.navigation.tasks.link");
            taskFolder.setMenuContentTitle("webmail.navigation.tasks.title");
            taskFolder.setTemplateName("./inc/contentPanels/tasksViewPanel.jspx");
            taskFolder.setPageContent(true);

            // create node and set callback
            DefaultMutableTreeNode taskFolderNode =
                    new DefaultMutableTreeNode(taskFolder);
            taskFolder.setWrapper(taskFolderNode);

            //add the new node.
            rootTreeNode.add(taskFolderNode);

            /**
             * Build Contacts Node
             */
            ContactFolderNavigationContentBean contactFolder =
                    new ContactFolderNavigationContentBean();
            contactFolder.setNavigationSelection(navigationBean);
            contactFolder.setContactManager(mediator.getContactManager());
            contactFolder.setMenuDisplayText("webmail.navigation.contacts.link");
            contactFolder.setMenuContentTitle("webmail.navigation.contacts.title");
            contactFolder.setTemplateName("./inc/contentPanels/contactsViewPanel.jspx");
            contactFolder.setPageContent(true);

            // create node and set callback
            DefaultMutableTreeNode contactFolderNode =
                    new DefaultMutableTreeNode(contactFolder);
            contactFolder.setWrapper(contactFolderNode);

            // add the new neode
            rootTreeNode.add(contactFolderNode);
        }

    }

    /**
     * Free resouces associated with this class as well as clean up of post logout
     * states.
     */
    public void dispose() {
        // remove mediator pointer
        mediator = null;

        // clean up tree.
        if (rootTreeNode != null) {

            // call dispose on each tree node, removes all listeners.
            NavigationContent tmpNode;
            Enumeration treeNodes = rootTreeNode.depthFirstEnumeration();
            while (treeNodes!= null && treeNodes.hasMoreElements()){
                tmpNode = (NavigationContent)
                        ((DefaultMutableTreeNode)
                                treeNodes.nextElement()).getUserObject();
                tmpNode.dispose();
            }

            rootTreeNode.removeAllChildren();
            rootTreeNode = null;
        }

        isInitiated = false;
    }

    /**
     * Add the specified mailAccountUserObject to the mail accounts branch.  If
     * the account is allready present it will not be added to the tree again.
     *
     * @param mailAccount mail account object to add to tree.
     * @return default mutable tree mode representing webmail menu
     */
    public DefaultMutableTreeNode addMailAccountBranch(MailAccountBean mailAccount) {
        if (isInitiated &&
                mailAccount != null &&
                mailAccountRootNode != null) {

            // create a new node for the new mail account
            MailAccountNavigationContentBean userObject =
                    new MailAccountNavigationContentBean();
            userObject.setMailAccount(mailAccount);
            userObject.setNavigationSelection(navigationBean);

            // set the node link label to the account email address, this value
            // can never be null or empty.
            userObject.setMenuDisplayText(mailAccount.getEmail());

            // Set the content navigation title for this link
            userObject.setMenuContentTitle(
                    WebmailMediator.getMessageBundle().getString("webmail.mailAccount") +
                            " - " + mailAccount.getEmail());
            userObject.setTemplateName("./inc/contentPanels/mailAccountsSplashPanel.jspx");
            DefaultMutableTreeNode branchNode = new DefaultMutableTreeNode(userObject);
            userObject.setWrapper(branchNode);

            // Get the mail account folders and build the tree recursively
            ArrayList mailFolders = mailAccount.getMailFolders();
            if (mailFolders != null ){
                for (int i = 0; i < mailFolders.size(); i++) {
                    addFolderNode(branchNode, (MailFolderBean) mailFolders.get(i));
                }
            }

            // add the new account branch to the navigation tree
            mailAccountRootNode.add(branchNode);

            // return the node
            return branchNode;
        }
        return null;
    }

    /**
     * Recursive utility method for finding mail folder nodes.
     */
    private void addFolderNode(DefaultMutableTreeNode parentNode,
                               MailFolderBean mailFolder) {

        Folder folder = mailFolder.getFolder();

        MailFolderNavigationContentBean userObject = new MailFolderNavigationContentBean();
        userObject.setWebmailMediator(mediator);
        userObject.setNavigationSelection(navigationBean);
        userObject.setMenuDisplayText(folder.getName());
        userObject.setMailFolder(mailFolder);
        userObject.setEnableDnd("true");
        // set title when node is selected
        userObject.setMenuContentTitle(
                WebmailMediator.getMessageBundle().getString("webmail.mailAccount") +
                        " - " + mailFolder.getMailAccount().getEmail());
        userObject.setTemplateName("./inc/contentPanels/emailFolderViewPanel.jspx");

        DefaultMutableTreeNode folderNode = new DefaultMutableTreeNode(userObject);
        parentNode.add(folderNode);
        userObject.setWrapper(folderNode);

        // recursively descent untill we have checked for all subfolders.
        if (mailFolder.holdsFolders()) {
            Folder[] f = mailFolder.getChildFolders();
            MailFolderBean tmpMailFolder;
            for (int i = 0; i < f.length; i++) {
                tmpMailFolder = new MailFolderBean();
                tmpMailFolder.setWebmailMediator(mediator);
                tmpMailFolder.setFolder(f[i]);
                tmpMailFolder.setMailAccount(mailFolder.getMailAccount());
                addFolderNode(folderNode, tmpMailFolder);
            }
        }
    }

    /**
     * Removes the specified mailAccountUserObject from the mail account branch.
     *
     * @param mailAccount mail account object to remove from  tree.
     */
    public void removeMailAccountNode(MailAccountBean mailAccount) {
        if (isInitiated && mailAccountRootNode != null) {
            MailAccountNavigationContentBean tmpMailAccountContent;
            DefaultMutableTreeNode tmpNode;
            Enumeration childNodes = mailAccountRootNode.depthFirstEnumeration();
            while (childNodes.hasMoreElements()){
                tmpNode = (DefaultMutableTreeNode) childNodes.nextElement();
                if (tmpNode.getUserObject()
                        instanceof MailAccountNavigationContentBean){
                    tmpMailAccountContent =
                            (MailAccountNavigationContentBean)tmpNode.getUserObject();
                    // if we've found the node we can remove it.
                    if (tmpMailAccountContent.equals(mailAccount)){
                        mailAccountRootNode.remove(tmpNode);
                    }
                }
            }
        }
    }

    /**
     * Gets the default tree model.  This model is needed for the value attribute
     * of the tree component.
     *
     * @return default tree model used by the navigation tree.
     */
    public DefaultTreeModel getModel() {
        return model;
    }

    /**
     * Sets the default tree model.
     *
     * @param model new default three model.
     */
    public void setModel(DefaultTreeModel model) {
        this.model = model;
    }

    /**
     * Gets the tree component binding.
     *
     * @return tree component binding.
     */
    public Tree getTreeComponent() {
        return treeComponent;
    }

    /**
     * Sets the tree component binding.
     *
     * @param treeComponent tree component to bind to.
     */
    public void setTreeComponent(Tree treeComponent) {
        this.treeComponent = treeComponent;
        if (!isInitiated) {
            init();
        }
    }

    /**
     * remove all the mail account nodes from the tree
     */
    public void removeAllMailAccountNode() {
        mailAccountRootNode.removeAllChildren();
    }
}
