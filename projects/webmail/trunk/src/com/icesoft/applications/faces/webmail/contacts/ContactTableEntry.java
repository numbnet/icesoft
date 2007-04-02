/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.contacts;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * <p>Contact table entry represents each table entry in
 * the contacts' view table <p>
 *
 * @since 1.0
 */
public class ContactTableEntry {

    private ContactModel model;
    //selection flag
    private boolean flag;


    public ContactTableEntry(ContactModel m, boolean f) {
        model = m;
        flag = f;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public ContactModel getModel() {
        return model;
    }

    public void setModel(ContactModel model) {
        this.model = model;
    }

    /**
     * selectThisContact method set the contact bean of contact list bean
     * to the contact model of this table entry
     *
     * @param event
     */
    public void selectThisContact(ActionEvent event) {
        ContactListBean contactListBean = ((ContactManager) FacesContext
                .getCurrentInstance()
                .getApplication()
                .createValueBinding("#{contactManager}")
                .getValue(FacesContext.getCurrentInstance())).getContactListBean();
        if(contactListBean.getContactBean()!= null)
        {
			contactListBean.setContactBean(null);
		}
        contactListBean.setContactBean((ContactBean) this.getModel());
        contactListBean.filter();
    }
}