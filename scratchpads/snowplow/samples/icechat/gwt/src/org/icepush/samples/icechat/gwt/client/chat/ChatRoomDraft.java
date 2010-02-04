package org.icepush.samples.icechat.gwt.client.chat;

import java.io.Serializable;

public class ChatRoomDraft implements Serializable {

	private String userSessionToken;
	private String text;
	
	public ChatRoomDraft(){}
	
	public ChatRoomDraft(String text, String userSessionToken){
		this.text = text;
		this.userSessionToken = userSessionToken;
		
	}

	public String getUserSessionToken() {
		return userSessionToken;
	}

	public void setUserSessionToken(String userSessionToken) {
		this.userSessionToken = userSessionToken;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
	
	
}
