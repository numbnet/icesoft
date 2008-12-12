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
package com.icesoft.applications.faces.webmail.contacts;


import com.icesoft.applications.faces.webmail.WebmailBase;
import com.icesoft.applications.faces.webmail.login.User;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * wrapper class of contact list model and this class will
 * directly interact with the page
 *
 * @since 1.0
 */
public class ContactListBean extends ContactListModel implements WebmailBase {

    //predefined search types
    private SelectItem[] searchTypeItems = new SelectItem[]{
            new SelectItem("Display Name"),
            new SelectItem("Primary Phone"),
            new SelectItem("Primary Email"),
            new SelectItem("Last Name"),
            new SelectItem("First Name")
    };

    //default search type
    private String searchType = "Display Name";
    //default search string
    private String searchString = "";

    //flag of unique search result
    private boolean uniqueSearchResult;
    //flag of currently in search mode
    private boolean searchMode = false;
    //flag of empty result set
    private boolean noResult;
    //flag of add contact from mail view
    private boolean fromMail;

    //contactTable is a wrapper of contactList
    //It adds a column of checkboxes for view purpose
    private ContactTableEntry[] contactTable;

    //name of column by which the table is sorted
    private String sort;
    //flag of ascending order of sorted table
    private boolean ascending;
    //selected contact
    private ContactBean contactBean;
    //current user
    private User validUser;

    //keep track number of records selected
    private long checkCount = 0;

    /**
      * Field for enabling/disabling multiple contact selection in contact list table.
      * Placeholder for future enhancement which allows user to make choice between 
      * one or multiple contact selections
      */	
    public boolean multipleSelection = true;

    /**
     * getter of contact bean
     *
     * @return selected contact bean
     */
    public ContactBean getContactBean() {
        return contactBean;
    }

    /**
     * setter of contact bean
     *
     * @param contactBean
     */
    public void setContactBean(ContactBean contactBean) {
        this.contactBean = contactBean;
    }

    /**
     * getter of fromMail flag
     *
     * @return fromMail
     */
    public boolean isFromMail() {
        return fromMail;
    }

    /**
     * setter of fromMail flag
     *
     * @param fromMail
     */
    public void setFromMail(boolean fromMail) {
        this.fromMail = fromMail;
    }


    /**
     * getter of checkCount
     *
     * @return checkCount
     */
    public long getCheckCount() {
        return checkCount;
    }

    /**
     * setter of checkCount
     *
     * @param checkCount
     */
    public void setCheckCount(long checkCount) {
        this.checkCount = checkCount;
    }

    /**
     * getter of current user
     *
     * @return validUser
     */
    public User getValidUser() {
        return validUser;
    }

    /**
     * setter of current user
     *
     * @param user
     */
    public void setValidUser(User user) {
        validUser = user;
    }

    /**
     * getter of current view table
     *
     * @return view table
     */
    public ContactTableEntry[] getContactTable() {

        //sort before returned
        sort(getSort(), isAscending());
        return contactTable;
    }

    /**
     * setter of view table
     *
     * @param contactTable
     */
    public void setContactTable(ContactTableEntry[] contactTable) {
        this.contactTable = contactTable;
    }


    /**
     * utility function to sort the view table
     */
    protected void sort(final String column, final boolean ascending) {
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                ContactTableEntry c1 = (ContactTableEntry) o1;
                ContactTableEntry c2 = (ContactTableEntry) o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("displayName")) {
                    return ascending ? c1.getModel().getDisplayName().compareTo(c2.getModel().getDisplayName()) : c2.getModel().getDisplayName().compareTo(c1.getModel().getDisplayName());
                } else if (column.equals("phoneNo")) {
                    return ascending ? c1.getModel().getPrimaryPhone().compareTo(c2.getModel().getPrimaryPhone()) : c2.getModel().getPrimaryPhone().compareTo(c1.getModel().getPrimaryPhone());
                } else if (column.equals("email")) {
                    return ascending ? c1.getModel().getPrimaryEmail().compareTo(c2.getModel().getPrimaryEmail()) : c2.getModel().getPrimaryEmail().compareTo(c1.getModel().getPrimaryEmail());
                } else return 0;
            }
        };

        Arrays.sort(contactTable, comparator);

    }

    /**
     * getter of ascending flag
     *
     * @return flag of ascending order
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * setter of ascending flag
     *
     * @param ascending
     */
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * getter of sorted column
     *
     * @return sorting column
     */
    public String getSort() {
        return sort;
    }

    /**
     * setter of sorted column
     *
     * @param sort
     */
    public void setSort(String sort) {
        this.sort = sort;
    }


    /**
     * Create a new contactBean and assign it the default values
     * IMPORTANT: string value must be initialized (not null)
     * otherwise the inputText component cannot
     * get updated
     */
    public void addContact(ActionEvent event) {

        if (contactBean != null)
            contactBean = null;

        contactBean = new ContactBean();


        contactBean.setDisplayName("");
        contactBean.setFirst("");
        contactBean.setLast("");
        contactBean.setInitials("");
        contactBean.setMiddle("");
        contactBean.setNickName("");
        contactBean.setNote("");
        contactBean.setPrimaryEmail("");
        contactBean.setPrimaryPhone("");
        contactBean.setUserName("");

        //create predefined collections and assign them
        //to the new bean
        contactBean.setAddresses(ContactModelUtility.defaultAddress(contactBean));
        contactBean.setEmails(ContactModelUtility.defaultEmail(contactBean));
        contactBean.setPhones(ContactModelUtility.defaultPhone(contactBean));
        contactBean.setWebpages(ContactModelUtility.defaultWebpage(contactBean));
        contactBean.setSelectedIndex(0);
    }

    /**
     * Create a new contactBean and populate it with the value
     * of the record retrieved from database
     */
    public void editContact(ActionEvent event) {

        if (contactBean != null)
            contactBean = null;

        contactBean = new ContactBean();
        //find the selected id
        long selectedId = getSelectedId();

        //retrieve the record from database
        contactBean = (ContactBean) ContactModelUtility.select(selectedId);

        //filter out the null records
        this.filter();
        contactBean.setSelectedIndex(0);
    }

    /**
     * The following filter out the null values of the record
     * TODO:find out why Hibernate retrieve more records than
     * the ones in the table and contain null records
     */
    public void filter() {


        List newAddresses = new ArrayList();
        List newEmails = new ArrayList();
        List newPhones = new ArrayList();
        List newWebpages = new ArrayList();
        for (int i = 0; i < contactBean.getAddresses().size(); i++) {
            if (contactBean.getAddresses().get(i) != null)
                newAddresses.add(contactBean.getAddresses().get(i));
        }
        for (int i = 0; i < contactBean.getEmails().size(); i++) {
            if (contactBean.getEmails().get(i) != null)
                newEmails.add(contactBean.getEmails().get(i));
        }
        for (int i = 0; i < contactBean.getPhones().size(); i++) {
            if (contactBean.getPhones().get(i) != null)
                newPhones.add(contactBean.getPhones().get(i));
        }
        for (int i = 0; i < contactBean.getWebpages().size(); i++) {
            if ( contactBean.getWebpages().get(i) != null)
                newWebpages.add(contactBean.getWebpages().get(i));
        }
        contactBean.setAddresses(newAddresses);
        contactBean.setEmails(newEmails);
        contactBean.setPhones(newPhones);
        contactBean.setWebpages(newWebpages);
    }

    /**
     * Delete the contact(s)
     */
    public void deleteContact(ActionEvent event) {

        ContactModelUtility.delete(getSelectedObjects());
    }


    /**
     * Search for the contact in the database
     *
     * @param event
     */
    public void findContact(ActionEvent event) {

        //set the search mode to true
        this.setSearchMode(true);

        //set the search type
        if (searchType.equals("Display Name"))
            searchType = "displayName";
        else if (searchType.equals("Primary Email"))
            searchType = "primaryEmail";
        else if (searchType.equals("Primary Phone"))
            searchType = "primaryPhone";
        else if (searchType.equals("Last Name"))
            searchType = "last";
        else
            searchType = "first";

        //search in the database to find a match
        List searchResult = ContactModelUtility.find(
                this.getValidUser().getUserName(),
                searchType,
                ("%" + searchString + "%")
        );

        //if a single record is returned set the uniqueResult flag
        //and set no result to false
        if (searchResult.size() == 1) {
            this.setUniqueSearchResult(true);
            this.setNoResult(false);
        }
        //set no result to true
        else if (searchResult.size() == 0) {
            this.setNoResult(true);
            this.setUniqueSearchResult(false);
        }
        //more than one records returned
        else {
            this.setUniqueSearchResult(false);
            this.setNoResult(false);
        }

        //if more than one records returned, return them as a list
        if (!this.isNoResult() && !this.isUniqueSearchResult()) {
            this.setContactList(searchResult);
            this.setContactTable(ContactViewUtility.toViewTable(this.getContactList(), false));
            this.setCheckCount(0);
        }

        //if only one record return, view its detail directly
        if (this.isUniqueSearchResult()) {
            if (this.contactBean != null)
                this.contactBean = null;
            this.setContactBean((ContactBean) searchResult.get(0));
            this.filter();
        }

    }


    /**
     * Return id of selected record, use as a complement of
     * getSelectedObjects
     */
    private long getSelectedId() {
        int selectedId = 0;
        for (; selectedId < contactTable.length; selectedId++) {
            if (contactTable[selectedId].isFlag())
                return contactTable[selectedId].getModel().getId();
        }
        return -1;
    }

    /**
     * Return the email address of selected contact
     */
    public String[] getSelectedEmails(){
		String[] emailArray = new String[getSelectedObjects().size()];
		for(int i=0;i<emailArray.length;i++){
			emailArray[i]=((ContactModel)getSelectedObjects().get(i)).getPrimaryEmail();
		}

		return emailArray;
	}

    /**
     * Return the actual objects that has been selected
     */
    private List getSelectedObjects() {

        List selectedList = new ArrayList();
        for (int selectedId = 0; selectedId < contactTable.length; selectedId++)
        {
            if (contactTable[selectedId].isFlag())
                selectedList.add(contactTable[selectedId].getModel());
        }
        return selectedList;
    }

    /**
     * Dynamically change the count of selected records
     * so that edit button or delete button can be disabled
     */
   public synchronized void rowSelection(RowSelectorEvent event) {
        // Maintain a count of selected messages
        if (event.isSelected()) {
            checkCount++;
        } else {
            checkCount--;
        }
    }

    /**
     * init the contactListBean
     */
    public void init() {
        ContactModelUtility.resetListModel(validUser, this);
        contactBean = new ContactBean();
        sort = "displayName";
        ascending = true;
    }

    /**
     * dispose the contactListBean
     */
    public void dispose() {
        if (contactList != null) {
            contactList.clear();
            contactList = null;
        }
        if (contactTable != null) {
            contactTable = null;
        }
        if (contactBean != null) {
            contactBean = null;
        }

    }

    /**
     * getter of searchTypeItems
     *
     * @return searchTypeItems
     */
    public SelectItem[] getSearchTypeItems() {
        return searchTypeItems;
    }

    /**
     * setter of searchTypeItems
     *
     * @param searchTypeItems
     */
    public void setSearchTypeItems(SelectItem[] searchTypeItems) {
        this.searchTypeItems = searchTypeItems;
    }

    /**
     * getter of search string
     *
     * @return search string
     */
    public String getSearchString() {
        return searchString;
    }

    /**
     * setter of search string
     *
     * @param searchString
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    /**
     * getter of searchType
     *
     * @return searchType
     */
    public String getSearchType() {
        return searchType;
    }

    /**
     * setter of search Type
     */
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    /**
     * getter of unique search result
     *
     * @return flag of unique result
     */
    public boolean isUniqueSearchResult() {
        return uniqueSearchResult;
    }

    /**
     * setter of unique search result flag
     *
     * @param isUniqueSearchResult
     */
    public void setUniqueSearchResult(boolean isUniqueSearchResult) {
        this.uniqueSearchResult = isUniqueSearchResult;
    }

    /**
     * getter of no result flag
     *
     * @return no result flag
     */
    public boolean isNoResult() {
        return noResult;
    }

    /**
     * setter of no result flag
     *
     * @param isNoResult
     */
    public void setNoResult(boolean isNoResult) {
        this.noResult = isNoResult;
    }

    /**
     * getter of search mode flag
     *
     * @return search mode flag
     */
    public boolean isSearchMode() {
        return searchMode;
    }

    /**
     * setter of search mode flag
     *
     * @param isSearchMode
     */
    public void setSearchMode(boolean isSearchMode) {
        this.searchMode = isSearchMode;
    }

    /**
     * set to true if no result is returned
     *
     * @return true if no matched record found false otherwise
     */
    public boolean isError() {
        return (this.searchMode && this.noResult);
    }

    /**
     * set to true if search returns some results
     */
    public boolean isResult() {
        return (this.searchMode && !this.noResult);
    }


    /**
     * select all the entries in the list
     * @param event
     */
    public void selectAll(ActionEvent event) {
        for (int i = 0; i < contactTable.length; i++) {
            if (!contactTable[i].isFlag()) {
                contactTable[i].setFlag(true);
                checkCount++;
            }
        }
    }


    /**
     * deselect all the entries in the list
     * @param event
     */
    public void deselectAll(ActionEvent event) {
        for (int i = 0; i < contactTable.length; i++) {
            if (contactTable[i].isFlag()) {
                contactTable[i].setFlag(false);
                checkCount--;
            }
        }
    }
    
   /**
     * Is multiple contact selection enabled in the contact list table?
     * @return true if multiple selection is enabled
     */

    public boolean isMultipleSelection() {
        return multipleSelection;
    }

    /**
     * Sets the state of multiple contact selection.
     *
     * @param multipleSelection
     */

    public void setMultipleSelection(boolean multipleSelection) {
        this.multipleSelection = multipleSelection;
    }

}