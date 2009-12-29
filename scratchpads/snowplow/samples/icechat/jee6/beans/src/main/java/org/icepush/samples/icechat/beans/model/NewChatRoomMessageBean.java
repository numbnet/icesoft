package org.icepush.samples.icechat.beans.model;

import java.io.Serializable;

import javax.enterprise.inject.Model;

@Model
public class NewChatRoomMessageBean implements Serializable{
	
	private static final long serialVersionUID = -5918594742217174351L;
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
