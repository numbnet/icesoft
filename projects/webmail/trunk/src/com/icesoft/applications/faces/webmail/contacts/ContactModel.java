/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.contacts;

import java.util.List;

/**
 * Contact model class represents each individual contact.
 *
 * @since 1.0
 */
public class ContactModel {

    protected List addresses;
    protected List emails;
    protected List webpages;
    protected List phones;

    protected long id;
    protected String initials;
    protected String last;
    protected String first;
    protected String middle;
    protected String displayName;
    protected String nickName;
    protected String note;
    protected String primaryEmail;
    protected String primaryPhone;
    //name of user that owns this contact
    protected String userName;


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPrimaryPhone() {
        return primaryPhone;
    }


    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }


    public String getPrimaryEmail() {
        return primaryEmail;
    }


    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initial) {
        this.initials = initial;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List getAddresses() {
        return addresses;
    }

    public void setAddresses(List addresses) {
        this.addresses = addresses;
    }

    public List getEmails() {
        return emails;
    }

    public void setEmails(List emails) {
        this.emails = emails;
    }

    public List getPhones() {
        return phones;
    }

    public void setPhones(List phones) {
        this.phones = phones;
    }

    public List getWebpages() {
        return webpages;
    }

    public void setWebpages(List webpages) {
        this.webpages = webpages;
    }


}
