package org.icepush.samples.icechat.beans.model;

import java.io.Serializable;

import javax.enterprise.inject.Model;

@Model
public class NewChatRoomBean implements Serializable{
	
	private static final long serialVersionUID = 6407306352321882490L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
