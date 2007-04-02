/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.contacts;

import java.util.List;

/**
 * Contact list model represents the list of contact models
 *
 * @since 1.0
 */
public class ContactListModel {

    //list of contacts
    protected List contactList;

    /**
     * constructor
     *
     * @param list
     */
    public ContactListModel(List list) {
        contactList = list;
    }

    /**
     * default constructor
     */
    public ContactListModel() {
    }

    /**
     * getter of contact list
     *
     * @return list of contacts
     */
    public List getContactList() {
        return contactList;
    }

    /**
     * setter of contact list
     *
     * @param contactList
     */
    public void setContactList(List contactList) {
        this.contactList = contactList;
	}



}