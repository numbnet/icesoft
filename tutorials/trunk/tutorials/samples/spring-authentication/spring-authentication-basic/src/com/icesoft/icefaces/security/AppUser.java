package com.icesoft.icefaces.security;

import java.io.Serializable;
import java.util.Set;

public class AppUser implements Serializable {
	private String firstName;

	private String lastName;

	private String login;

	private String password;

	private Set<String> roles;

	public AppUser() {
	}

	public AppUser(String login, String firstName, String lastName,
			String password, Set<String> roles) {
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.roles = roles;
		assert !roles.isEmpty();
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
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

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String toString() {
		return firstName + " " + lastName;
	}

	public String getFullName() {
		return toString();
	}

	public boolean isPasswordValid(String password) {
		return getPassword().equals(password);
	}

	boolean hasRole(String role) {
		return roles.contains(role);
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

}
