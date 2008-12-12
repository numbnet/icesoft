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
package com.icesoft.applications.faces.webmail.mail;

import com.icesoft.applications.faces.webmail.contacts.ContactManager;
import com.icesoft.applications.faces.webmail.contacts.ContactModel;
import com.icesoft.faces.component.selectinputtext.SelectInputText;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *This class represents each entry of the recipient table on emailEditView page
 */
public class TableEntry {

    //type of recipients: To, Cc and Bcc
    String type;
    //email address of this recipient
    String value;
    //used by autoComplete fields representing matched email accounts in contacts
    List matchList;

    //configure the algo. for generating match list
    //0 - bidirectional from critical point
    //1 - unidirectional from critical point(ascending)
    //2 - unidirectional from critical point(decending)
    final int ACALGO = 0;

    //comparator used to sort and search the contact list by
    //their display names
    final Comparator NAME_COMPARATOR = new Comparator() {
        String s1,s2,e1,e2;

        public int compare(Object o1, Object o2) {
			e1 = ((ContactModel) o1).getPrimaryEmail();
            e2 = ((ContactModel) o2).getPrimaryEmail();
            s1 = ((ContactModel) o1).getDisplayName();
            s2 = ((ContactModel) o2).getDisplayName();


            return s1.compareToIgnoreCase(s2)+e1.compareToIgnoreCase(e2);
        }
    };

    //comparator used to sort and search the contact list by
    //their email addresses
    final Comparator EMAIL_COMPARATOR = new Comparator() {
        String s1
                ,
                s2;

        public int compare(Object o1, Object o2) {
            s1 = ((ContactModel) o1).getPrimaryEmail();
            s2 = ((ContactModel) o2).getPrimaryEmail();
            return s1.compareToIgnoreCase(s2);
        }
    };

    /**
     * return the matched list for autoComplete
     */
    public List getMatchList() {
        return matchList;
    }

    /**
     * set the mathced list
     *
     * @param ls
     */
    public void setMatchList(List ls) {
        this.matchList = ls;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }


    public TableEntry(String m, String i) {
        type = m;
        value = i;
    }

    /**
     * Utility function to get the contact list
     * based on search string
     */
    public void updateContactList(ValueChangeEvent event) {

        //control whether it's email list or display name list
        boolean isEmail = false;
        List result = new ArrayList();
        //maximum results to show
        int maxMatches = ((SelectInputText) event.getComponent()).getRows();

        //get the contact list
        List dictionary = (((ContactManager) FacesContext
                .getCurrentInstance()
                .getApplication()
                .createValueBinding("#{contactManager}")
                .getValue(FacesContext.getCurrentInstance()))
                .getContactListBean())
                .getContactList();

        Collections.sort(dictionary, NAME_COMPARATOR);

        //get the search string
        String searchString = (String) event.getNewValue();

        //create the key object for the search
        //default is to search display name
        ContactModel key = new ContactModel();
        key.setDisplayName(searchString);
        key.setPrimaryEmail(searchString);


        int positionName = Collections.binarySearch(dictionary, key, NAME_COMPARATOR);
        int insertName;
        int positionEmail = -1;

        //if not find a match in display name list
        //then try to find a match in email list
        //otherwise use partial match for display name list
        if (positionName < 0) {
            insertName = Math.abs(positionName) - 1;
            //key.setPrimaryEmail(key.getDisplayName());
            //key.setDisplayName(null);
            //Collections.sort(dictionary, EMAIL_COMPARATOR);
            //positionEmail = Collections.binarySearch(dictionary, key, EMAIL_COMPARATOR);
            //if (positionEmail >= 0)
            //    isEmail = true;
            //else
            //	Collections.sort(dictionary, NAME_COMPARATOR);

        } else {
            insertName = positionName;
        }


        if(ACALGO == 0){
	        int half = 0;
			if (maxMatches%2==0)
				half = maxMatches/2;
			else
				half = (maxMatches-1)/2;

            if((insertName < dictionary.size()) && (insertName >= 0))
				result.add(
					new SelectItem(dictionary.get(insertName),
					((ContactModel) dictionary.get(insertName)).getPrimaryEmail()
                        ));

			for(int i=1;i<half;i++){
			    if((insertName - i) < 0)
			    	break;
			    result.add(
			    		new SelectItem(dictionary.get(insertName - i),
			          ((ContactModel) dictionary.get(insertName - i)).getPrimaryEmail()
                        ));
			}


            for (int i = 1; i < maxMatches-half-1; i++) {
                if ((insertName + i) >= dictionary.size())
                    break;
                result.add(
                        new SelectItem(dictionary.get(insertName + i),
                                ((ContactModel) dictionary.get(insertName + i)).getPrimaryEmail()
                        ));
            }

	    }
	    else if(ACALGO == 1){
            for (int i = 0; i < maxMatches; i++) {
                if ((insertName + i) >= dictionary.size())
                    break;
                result.add(
                        new SelectItem(dictionary.get(insertName + i),
                                ((ContactModel) dictionary.get(insertName + i)).getPrimaryEmail()
                        ));
            }
		}
		else if(ACALGO == 2){
            for (int i = 0; i < maxMatches; i++) {
                if ((insertName - i) < 0)
                    break;
                result.add(
                        new SelectItem(dictionary.get(insertName - i),
                                ((ContactModel) dictionary.get(insertName - i)).getPrimaryEmail()
                        ));
            }

		}

		//assign a new match list
        if (this.matchList != null){
		            this.matchList.clear();
		            this.matchList = null;
        }
        //set the match list to be returned
        this.matchList = result;

    }
}