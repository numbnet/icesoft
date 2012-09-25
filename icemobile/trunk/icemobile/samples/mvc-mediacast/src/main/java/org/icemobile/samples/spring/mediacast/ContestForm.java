package org.icemobile.samples.spring.mediacast;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class ContestForm {
	
	@NotEmpty @Email 
	private String email;
	
	@Size(max = 164)
	private String description;
	
	private String layout;
	
	private String photoId;
	
	private String form;
	
	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	@Override
	public String toString() {
		return "ContestForm [email=" + email + ", description=" + description
				+ ", layout=" + layout + ", photoId=" + photoId + ", form="
				+ form  + "]";
	}
	
	public boolean isEmpty(){
		return this.email == null || this.email.length() == 0;
	}
	

}
