/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.contacts;


import javax.faces.event.ActionEvent;

/**
 * Wrapper class of contact model
 *
 * @since 1.0
 */
public class ContactBean extends ContactModel {

	private int selectedIndex = 0;
	
	
    public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}


	/**
     * save a new contact or update an old contact
     *
     * @param event
     */
    public void saveContact(ActionEvent event) {

        ContactModelUtility.saveContact(this);
        selectedIndex = 0;
    }
    
    /**
     * when user cancels the change, set the focus on the first tab
     * @param event
     */
    public void cancel(ActionEvent event){
    	selectedIndex = 0;
    }


}