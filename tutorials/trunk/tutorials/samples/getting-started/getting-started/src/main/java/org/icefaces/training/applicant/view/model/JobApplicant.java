package org.icefaces.training.applicant.view.model;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Job Applicant model
 */
@ManagedBean
@ViewScoped
public class JobApplicant implements Serializable{

	private String firstName;
	private String lastName;
	
	private Integer title;
	
	private String email;
	
	@Override
    public String toString(){
        return "jobApplicant " + super.toString();
    }
	
    @PostConstruct
	public void clear(){
		setEmail("");
		setFirstName("");
		setLastName("");
		setTitle(null);		
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

	public void setTitle(Integer title) {
		this.title = title;
	}

	public Integer getTitle() {
		return title;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
	
}
