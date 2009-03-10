package com.icesoft.icefaces.security.beans;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.springframework.security.ui.AbstractProcessingFilter;

public class LoginBean {

	// properties
	private String userId;

	private String password;

	/**
	 * default empty constructor
	 */
	public LoginBean() {

		Exception ex = (Exception) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSessionMap()
				.get(AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY);

		if (ex != null)
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, ex
							.getMessage(), ex.getMessage()));

	}

	/**
	 * Method that is backed to a submit button of a form.
	 */
	public String send() {

		// do real logic
		return ("success");
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public void login(ActionEvent e) throws java.io.IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("/spring-authentication/j_spring_security_check.jsp?j_username="+userId+"&j_password="+password);
	}
}