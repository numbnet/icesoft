/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.contacts;


/**
 * GenericContactAttribute represents the generic attribute of the contact
 * all contact attributes must extends this class.
 *
 * @since 1.0
 */
public class GenericContactAttribute {

    //id of this entry
    private long id;
    //id of parent contact
    private long contactId;
    //link for database purpose
    private ContactModel contact;


    public ContactModel getContact() {
        return contact;
    }


    public void setContact(ContactModel contact) {
        this.contact = contact;
    }


    public GenericContactAttribute() {

    }


    public long getContactId() {
        return contactId;
    }


    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}