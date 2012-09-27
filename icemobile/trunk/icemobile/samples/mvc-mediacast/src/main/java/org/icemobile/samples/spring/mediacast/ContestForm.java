package org.icemobile.samples.spring.mediacast;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class ContestForm {
	
	public static final String DEFAULT_LAYOUT = "m";
	
	@NotEmpty @Email 
	private String email;
	
	@Size(max = 164)
	private String description;
	
	private String layout;
	
	private String photoId;
	
	private String form;
	
	private String voterId;
	
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
		this.photoId = cleanParam(photoId);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = cleanParam(email);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLayout() {
		if( layout == null || layout.length() == 0 ){
			layout = DEFAULT_LAYOUT;
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = cleanParam(layout);
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
	
	private String cleanParam(String param){
		if( param != null && param.indexOf(",") > 0 ){
			param = param.substring(0,param.indexOf(","));
		}
		return param;
	}

	public String getVoterId() {
		return voterId;
	}

	public void setVoterId(String voterId) {
		this.voterId = cleanParam(voterId);
	}

}
