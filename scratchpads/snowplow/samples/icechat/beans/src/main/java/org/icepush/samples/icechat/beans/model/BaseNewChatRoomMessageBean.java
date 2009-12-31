package org.icepush.samples.icechat.beans.model;

import java.io.Serializable;

import org.icepush.samples.icechat.controller.model.INewChatRoomMessageBean;

public abstract class BaseNewChatRoomMessageBean implements Serializable, INewChatRoomMessageBean{
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
