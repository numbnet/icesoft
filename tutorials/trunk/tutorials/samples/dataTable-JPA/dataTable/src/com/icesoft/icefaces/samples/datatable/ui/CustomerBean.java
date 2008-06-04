package org.icefaces.ui;


import javax.faces.event.ActionEvent;

import org.icefaces.jpa.Customer;

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
	
	public CustomerBean (Customer customer, SessionBean sessionBean){
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
	public void toggleSelected(ActionEvent e){
		tempcontactfirstname = customer.getContactfirstname();
		tempcontactlastname = customer.getContactlastname();
		expanded = !expanded;
	}	

	/**
	 * <p>Bound to commandButton actionListener in the ui that commits Customer 
	 * changes to the database.</p>
	 */
	public void commit(ActionEvent e){
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
	public void cancel(ActionEvent e){
		tempcontactfirstname = customer.getContactfirstname();
		tempcontactlastname = customer.getContactlastname();
		expanded = !expanded;
	}

}
