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

package com.icesoft.icefaces.samples.datatable.ui;


import com.icesoft.icefaces.samples.datatable.jpa.Customer;

import javax.faces.event.ActionEvent;

/**
 * <p>This class wraps the Customer object with view specific methods and
 * properties.</p>
 */
public class CustomerBean {

    private Customer customer;
    private SessionBean sessionBean;
    private boolean expanded = false;

    private String tempcontactfirstname;
    private String tempcontactlastname;

    public CustomerBean(Customer customer, SessionBean sessionBean) {
        this.customer = customer;
        this.sessionBean = sessionBean;
        tempcontactfirstname = customer.getContactfirstname();
        tempcontactlastname = customer.getContactlastname();
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getTempcontactfirstname() {
        return tempcontactfirstname;
    }

    public void setTempcontactfirstname(String tempcontactfirstname) {
        this.tempcontactfirstname = tempcontactfirstname;
    }

    public String getTempcontactlastname() {
        return tempcontactlastname;
    }

    public void setTempcontactlastname(String tempcontactlastname) {
        this.tempcontactlastname = tempcontactlastname;
    }

    /**
     * <p>Bound to commandLink actionListener in the ui that renders/unrenders
     * the Customer details for editing.</p>
     */
    public void toggleSelected(ActionEvent e) {
        tempcontactfirstname = customer.getContactfirstname();
        tempcontactlastname = customer.getContactlastname();
        expanded = !expanded;
    }

    /**
     * <p>Bound to commandButton actionListener in the ui that commits Customer
     * changes to the database.</p>
     */
    public void commit(ActionEvent e) {
        customer.setContactfirstname(tempcontactfirstname);
        customer.setContactlastname(tempcontactlastname);
        sessionBean.commit(customer);
        expanded = !expanded;
    }

    /**
     * <p>Bound to commandButton actionListener in the ui that cancels potential
     * Customer changes to the database and unrenders the editable Customer
     * details.</p>
     */
    public void cancel(ActionEvent e) {
        tempcontactfirstname = customer.getContactfirstname();
        tempcontactlastname = customer.getContactlastname();
        expanded = !expanded;
    }

    /**
     * <p>Bound to commandButton actionListener in the ui that cancels potential
     * Customer changes to the database and unrenders the editable Customer
     * details.</p>
     */
    public void delete(ActionEvent e) {
        sessionBean.delete(customer);
        expanded = !expanded;
    }

}
