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


import com.icesoft.applications.faces.webmail.util.db.HibernateUtil;
import com.icesoft.applications.faces.webmail.login.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;


/**
 * Utility class responsible for communicating with the database
 *
 * @since 1.0
 */

public class ContactModelUtility {

    protected static Log log = LogFactory.getLog(ContactModelUtility.class);

    /**
     * Retrieve the contactList from database using
     * given user name
     */
    public static List selectList(User validUser) {

        List contactList = null;

        if (validUser != null) {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            session.beginTransaction();
            Query mailAccountQuery = session.createQuery(
                    "from ContactModel where user_name = :verifiedUser");
            mailAccountQuery.setParameter("verifiedUser", validUser.getUserName());

            // get accounts associated with user.
            try {
                contactList = mailAccountQuery.list();
            }
            catch (HibernateException e) {
                log.error("error retriving contact list from database");

            }

            // finish the transaction
            session.getTransaction().commit();

        }

        return contactList;

    }


    /**
     * Refresh the list model
     */
    public static void resetListModel(User user, ContactListBean bean) {

        bean.setContactList(selectList(user));
        bean.setContactTable(ContactViewUtility.toViewTable(bean.getContactList(), false));
        bean.setCheckCount(0);
        bean.setNoResult(false);
        bean.setUniqueSearchResult(false);
        bean.setSearchMode(false);
        bean.setFromMail(false);
        bean.setSearchType("Display Name");
        bean.setSearchString("");
    }


    /**
     * Retrieve the contact record from database using
     * given id
     */
    public static ContactModel select(long id) {

        ContactModel contactModel = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        session.beginTransaction();
        Query selectIdQuery = session.createQuery(
                "from ContactModel where id = :selectedId");
        selectIdQuery.setLong("selectedId", id);

        try {
            contactModel = (ContactBean) selectIdQuery.uniqueResult();
        }
        catch (HibernateException e) {
            log.error("error retrieving contact from database with given id");
        }

        // finish the transaction
        session.getTransaction().commit();
        return contactModel;

    }

    /**
     * Delete the records that in the selected list
     * from database
     */
    public static void delete(List selectedList) {

        try {
            // save the changes to the database
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            for (int i = 0; i < selectedList.size(); i++)
                session.delete(selectedList.get(i));

            session.getTransaction().commit();
        }
        catch (HibernateException e) {
            log.error("error deleting selected contact");

        }
    }


    /**
     * Insert a new contact into or update a record in the database
     */
    public static void saveContact(ContactModel contactModel) {

        //set the user name of the new contact record
        //so that it can be retrieved next time
        if ((contactModel.getUserName() == null) || (contactModel.getUserName().equals("")))
            contactModel.setUserName(
                    ((ContactManager) FacesContext
                            .getCurrentInstance()
                            .getApplication()
                            .createValueBinding("#{contactManager}")
                            .getValue(FacesContext.getCurrentInstance()))
                            .getContactListBean()
                            .getValidUser()
                            .getUserName()
            );

		if((contactModel.getDisplayName() == null)||contactModel.getDisplayName().equals(""))
		{
			if(contactModel.getPrimaryEmail() != null)
				contactModel.setDisplayName(contactModel.getPrimaryEmail());
		}

        try {
            // save the changes to the database
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.saveOrUpdate(contactModel);
            session.getTransaction().commit();
        }
        catch (HibernateException e) {
            log.error("error updating/inserting the contact");
        }

    }

    /**
     * search for the contact that contains the search string in the search type
     * and matches the username
     *
     * @param userName
     * @param searchType
     * @param searchString
     * @return list of matched record or null if no matched record found
     */
    public static List find(String userName, String searchType, String searchString) {

        List contactList;

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        session.beginTransaction();
        //set criteria to match the search string, type and user name
        contactList = session.createCriteria(ContactBean.class)
                .add(Restrictions.ilike(searchType, searchString))
                .add(Restrictions.eq("userName", userName))
                .list();
        session.getTransaction().commit();


        return contactList;
    }


    /**
     * Generate default list of address models and link it to
     * the contact model
     *
     * @param cb
     * @return default list of addresses
     */
    public static List defaultAddress(ContactModel cb) {
        List addressList = new ArrayList();
        AddressModel temp1 = new AddressModel("Office");
        temp1.setCity("");
        temp1.setCountry("");
        temp1.setPostcode("");
        temp1.setProvince("");
        temp1.setStreet("");
        AddressModel temp2 = new AddressModel("Home");
        temp2.setCity("");
        temp2.setCountry("");
        temp2.setPostcode("");
        temp2.setProvince("");
        temp2.setStreet("");
        temp1.setContact(cb);
        temp2.setContact(cb);
        addressList.add(temp1);
        addressList.add(temp2);

        return addressList;
    }

    /**
     * Generate default list of email models and link it to
     * the contact model
     *
     * @param cb
     * @return default list of emails
     */
    public static List defaultEmail(ContactModel cb) {
        List emailList = new ArrayList();
        EmailModel temp1 = new EmailModel("Work");
        EmailModel temp2 = new EmailModel("Personal");
        temp1.setValue("");
        temp1.setContact(cb);
        temp2.setValue("");
        temp2.setContact(cb);
        emailList.add(temp1);
        emailList.add(temp2);

        return emailList;
    }

    /**
     * Generate default list of phone models and link it to
     * the contact model
     *
     * @param cb
     * @return default list of phones
     */
    public static List defaultPhone(ContactModel cb) {
        List phoneList = new ArrayList();
        PhoneModel temp1 = new PhoneModel("Business");
        PhoneModel temp2 = new PhoneModel("Personal");
        temp1.setValue("");
        temp2.setValue("");
        temp1.setContact(cb);
        temp2.setContact(cb);
        phoneList.add(temp1);
        phoneList.add(temp2);

        return phoneList;
    }

    /**
     * Generate the default list of webpage models and link it
     * to the contact model
     *
     * @param cb
     * @return default list of webpages
     */
    public static List defaultWebpage(ContactModel cb) {
        List webpageList = new ArrayList();

        WebpageModel temp1 = new WebpageModel("Company");
        WebpageModel temp2 = new WebpageModel("Personal");
        temp1.setValue("");
        temp2.setValue("");
        temp1.setContact(cb);
        temp2.setContact(cb);
        webpageList.add(temp1);
        webpageList.add(temp2);

        return webpageList;
    }

}