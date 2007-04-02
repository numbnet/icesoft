/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.navigation;

import com.icesoft.applications.faces.webmail.contacts.ContactManager;
import com.icesoft.applications.faces.webmail.contacts.ContactModelUtility;
import com.icesoft.faces.component.dragdrop.DropEvent;

import javax.faces.event.ActionEvent;

/**
 * This is just a wrapper for a ContactManagerBean so that it can be managed
 * in the context of the NavigationManager for displaying content.
 *
 * @since 1.0
 */
public class ContactFolderNavigationContentBean extends NavigationContent {

    protected ContactManager contactManager;

    /**
     * Creates a new ContactFolderNavigationContentBean.  The default icon for
     * this node is also set.
     */
    public ContactFolderNavigationContentBean() {
        super();
        setLeaf(true);
        setLeafIcon("images/contactfortree.gif");
    }

    /**
     * Gets the associated contact manager.
     * @return contact manager associated with this navigation object.
     */
    public ContactManager getContactManager() {
        return contactManager;
    }

    /**
     * Sets the contact manager associated with this object.
     * @param contactManager
     */
    public void setContactManager(ContactManager contactManager) {
        this.contactManager = contactManager;
    }

    /**
     * Called when this objects view is selected.  The contacts list view model
     * is reset to default values, clearing any previous search releated  view
     * settings.
     *
     * @param event
     */
    public void contentVisibleAction(ActionEvent event) {
        if ((contactManager != null) && (contactManager.getContactListBean() != null)){
            ContactModelUtility.resetListModel(
                    contactManager.getContactListBean().getValidUser(),
                    contactManager.getContactListBean());
        }
        super.contentVisibleAction(event);
    }

    /**
     * Called when an drop event occures on this navigation content.
     *
     * @param event drop event.
     */
    public void navigationDropAction(DropEvent event) {
        currentEffect = dropFailureEffect;
        dropFailureEffect.setFired(false);
    }


}