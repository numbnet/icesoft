package org.demo.model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class Person implements Serializable, Cloneable {

	private static final long serialVersionUID = -3540661397516075104L;

	public Person(Integer id, String firstName, String lastName, String address) {
		this.setId(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}

	private Integer id;
	private String firstName;
	private String lastName;
	private String address;
	private Boolean selected;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return firstName + " " + lastName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	@Override
	/*
	 * Method to create a copy of this object.
	 * The super method does a copy of each member of this object into a new object
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Person) {
			Person compare = (Person) obj;
			if (compare.getId() == this.getId()) {
				return true;
			}
			else
				return false;
		}
		return super.equals(obj);
	}
}
