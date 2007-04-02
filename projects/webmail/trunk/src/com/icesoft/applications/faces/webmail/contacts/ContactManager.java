/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.contacts;


import com.icesoft.applications.faces.webmail.WebmailBase;
import com.icesoft.applications.faces.webmail.WebmailMediator;
import com.icesoft.applications.faces.webmail.login.User;
import com.icesoft.applications.faces.webmail.mail.MailManager;
import com.icesoft.applications.faces.webmail.navigation.NavigationContent;
import com.icesoft.applications.faces.webmail.navigation.NavigationSelectionBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.List;


/**
 * The <code>ContactManager</code> class is responsible for managing the main
 * UI actions such as edit, add, delete and search.
 *
 * @since 1.0
 */
public class ContactManager implements WebmailBase {

    private static Log log = LogFactory.getLog(ContactManager.class);

    // link to mediator for applications mediation
    private WebmailMediator mediator;

    // Contacts to manage
    private ContactListBean contactListBean;
    private boolean isInit;

    private static NavigationContent contactEditNavigation;
    private static NavigationContent contactNavigation;


    /**
     * add a new contact if it's not in the contact list
     *
     * @param contact
     */
    public void addContact(ContactModel contact) {
        if (!contains(contact)) {
            ContactModelUtility.saveContact(contact);
            ContactModelUtility.resetListModel(contactListBean.getValidUser(), contactListBean);
        }
    }


    /**
     * check whether the contact list contains this contact
     *
     * @param contact
     * @return true if it's contained in the list false otherwise
     */
    private boolean contains(ContactModel contact) {
        for (int i = 0; i < contactListBean.getContactList().size(); i++) {
            ContactModel tmpContact = (ContactModel) contactListBean.getContactList().get(i);
            if (tmpContact.getPrimaryEmail().equalsIgnoreCase(contact.getPrimaryEmail()))
                return true;
        }
        return false;
    }

    /**
     * setter of mediator
     *
     * @param mediator
     */
    public void setMediator(WebmailMediator mediator) {
        this.mediator = mediator;
        this.mediator.setContactManager(this);
    }

    /**
     * default constructor
     */
    public ContactManager() {
    }

    /**
     * @return contactEditNavigation
     */
    public static NavigationContent getContactEditNavigation() {
        return contactEditNavigation;
    }

    /**
     * Navigate to search result view
     *
     * @return null
     */
    public String navigateToSearchList() {

        //if only one record is returned, go to the edit view
        //of this contact else it stays at the same view
        if (contactListBean.isUniqueSearchResult())
            this.navigateToEditContact();

        return null;
    }

    /**
     * Navigate to contact edit view
     *
     * @return null
     */
    public String navigateToEditContact() {
        NavigationSelectionBean navigationSelectionBean =
                mediator.getNavigationManager().getNavigationSelectionBean();
        navigationSelectionBean.setSelectedPanel(contactEditNavigation);
        return null;
    }

    /**
     * Navigate from search list to contact list
     *
     * @return null
     */
    public String navigateFromSearchList() {

        //reset the model to update the change
        ContactModelUtility.resetListModel(contactListBean.getValidUser(), contactListBean);

        NavigationSelectionBean navigationSelectionBean =
                mediator.getNavigationManager().getNavigationSelectionBean();
        navigationSelectionBean.setSelectedPanel(contactNavigation);
        return null;
    }

    /**
     * Navigate from the contact edit view from one of following views:
     * 1.contact list
     * 2.contact search list
     * 3.message view
     * And it should go back the same view after update the current contact
     *
     * @return null
     */
    public String navigateToContactList() {

        //not from message view
        if (!contactListBean.isFromMail()) {

            //from search list view
            if (contactListBean.isSearchMode()) {
                //update the search list
                List searchResult = ContactModelUtility.find(
                        contactListBean.getValidUser().getUserName(),
                        contactListBean.getSearchType(),
                        ("%" + contactListBean.getSearchString() + "%")
                );
                contactListBean.setContactList(searchResult);
                contactListBean.setContactTable(ContactViewUtility.toViewTable(contactListBean.getContactList(), false));
                contactListBean.setCheckCount(0);

            }

            //from contact list view
            else
                ContactModelUtility.resetListModel(contactListBean.getValidUser(), contactListBean);

            //set the view panel
            NavigationSelectionBean navigationSelectionBean =
                    mediator.getNavigationManager().getNavigationSelectionBean();
            navigationSelectionBean.setSelectedPanel(contactNavigation);
        }

        //from message view
        else {
            ContactModelUtility.resetListModel(contactListBean.getValidUser(), contactListBean);
            NavigationSelectionBean navigationSelectionBean =
                    mediator.getNavigationManager().getNavigationSelectionBean();
            navigationSelectionBean.setSelectedPanel(MailManager.getMailViewNavigation());

        }
        return null;
    }

    /**
     * Initialization of contact manager
     */
    public void init() {

        if (isInit) {
            return;
        }
        isInit = true;

        contactEditNavigation = new NavigationContent();
        contactEditNavigation.setNavigationSelection(
                mediator.getNavigationManager().getNavigationSelectionBean());
        contactEditNavigation.setTemplateName("contactsEditViewPanel");
        contactEditNavigation.setMenuContentTitle("Contact-Edit");

        contactNavigation = new NavigationContent();
        contactNavigation.setNavigationSelection(
                mediator.getNavigationManager().getNavigationSelectionBean());
        contactNavigation.setTemplateName("contactsViewPanel");
        contactNavigation.setMenuContentTitle("Contacts");

        // do a little clean up incase the api is used incorrectly.
        if (contactListBean != null) {
            contactListBean.dispose();
            contactListBean = null;
        }


        User validUser = mediator.getLoginManager().getLoginBeanFactory().getVerifiedUser();
        contactListBean = new ContactListBean();
        contactListBean.setValidUser(validUser);

        //init contactListBean that it owns
        contactListBean.init();
    }

    /**
     * dipose manager
     */
    public void dispose() {
        isInit = false;

        if (log.isDebugEnabled()) {
            log.debug(" Disposing ContactManager");
        }

        if (contactListBean != null) {
            contactListBean.dispose();
        }
    }


    /**
     * getter of contact list
     *
     * @return list of contacts
     */
    public ContactListBean getContactListBean() {
        return contactListBean;
    }

    /**
     * setter of contact list
     *
     * @param contactListBean
     */
    public void setContactListBean(ContactListBean contactListBean) {
        this.contactListBean = contactListBean;
    }

    /**
     * send an email to selected contact
     */
     public void sendEmailWithContact(ActionEvent event){
		MailManager mm = mediator.getMailManager();
		mm.createNewMessage(null);
		mm.getSelectedMailAccount()
		.getSelectedMessageBean().populateEmails(contactListBean.getSelectedEmails());
	    contactListBean.deselectAll(null);
	 }

}

