/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.navigation;

import com.icesoft.applications.faces.webmail.WebmailBase;
import com.icesoft.applications.faces.webmail.WebmailMediator;
import com.icesoft.applications.faces.webmail.mail.MailAccountBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;

/**
 * <p>The <code>NavigationManager</code>class is managing the construction
 * of the NavigationTree and to insure that the state of the NavigationSelectionBean
 * is kept consistant.<p>
 *
 * @since 1.0
 */
public class NavigationManager implements WebmailBase {

    private static Log log = LogFactory.getLog(NavigationManager.class);

    // link to mediator for applications mediation
    private WebmailMediator mediator;

    // stores currently selected content panel
    private NavigationSelectionBean navigationSelectionBean;

    // navigation tree, represents a tree component nodes which when selected
    // control the navigationSelectionBean's state.
    private NavigationTree navigationTree;

    // initiated state
    private boolean isInit = false;

    public void setMediator(WebmailMediator mediator) {
        this.mediator = mediator;
        this.mediator.setNavigationManager(this);
    }

    /**
     * Initilized the manager. An instance fo the navigationTree and
     * navigationSelectionBean is created.
     */
    public void init() {
        if (isInit) {
            return;
        }
        isInit = true;

        navigationSelectionBean = new NavigationSelectionBean();
        navigationTree = new NavigationTree(mediator, navigationSelectionBean);
        navigationTree.init();

    }

    /**
     * Clean up any resource associated with bean.
     */
    public void dispose() {
        isInit = false;

        if (log.isDebugEnabled()) {
            log.debug(" Disposing NavigationManager");
        }

        if (navigationTree != null) {
            navigationTree.dispose();
        }
    }

    /**
     * Gets the NavigationSelectionBean object. This object maintains which
     * content panel is visible.
     *
     * @return NavigationSelectionBean object.
     */
    public NavigationSelectionBean getNavigationSelectionBean() {
        return navigationSelectionBean;
    }

    /**
     * Gets the navigation tree instance associated with this manager.
     *
     * @return navigation tree.
     */
    public NavigationTree getNavigationTree() {
        return navigationTree;
    }

    /**
     * Adds the specfied mail account node to the navigation tree and leafs
     * for any folders contained in the mail account.
     *
     * @param mailAccount user object to be added to the navigation tree
     */
    public void addMailAccountNode(MailAccountBean mailAccount) {
        if (isInit && mailAccount != null) {

            // adds the specified mail account as a node to the tree
            // and its respective child nodes
            navigationTree.addMailAccountBranch(mailAccount);
        }
    }


    /**
     * Adds the specfied mail account node to the navigation tree and leafs
     * for any folders contained in the mail account.
     *
     * @param mailAccount user object to be added to the navigation tree
     */
    public void removeMailAccountNode(MailAccountBean mailAccount) {
        if (isInit && mailAccount != null) {

            navigationTree.removeMailAccountNode(mailAccount);

        }
    }

    /**
     * Removes all mail accounts from the navigation tree.
     */
    public void removeAllMailAccountNode() {
        if (isInit) {
            navigationTree.removeAllMailAccountNode();
        }
    }

}
