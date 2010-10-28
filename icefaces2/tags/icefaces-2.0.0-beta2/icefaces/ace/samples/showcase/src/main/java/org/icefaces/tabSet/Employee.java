package org.icefaces.tabSet;

import java.awt.event.ActionEvent;
import java.io.Serializable;


public class Employee implements Serializable {
	
	private String firstName;
	private String lastName;
	private String address;
	private String emailAddress;
    private boolean rendered= true;
 
	
	public Employee(String firstName, String lastName, String address,
			        String emailAddress) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.emailAddress = emailAddress;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public void showTab(ActionEvent event){
		this.setRendered(true);
		System.out.println("showing Tab as rendered="+rendered);
	}
	public void hideTab(ActionEvent event){
		this.setRendered(false);
		System.out.println("showing Tab as NOT rendered="+rendered);
	}
   
}
