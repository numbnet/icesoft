package org.icepush.samples.icechat.beans.model;

import java.io.Serializable;

import org.icepush.samples.icechat.controller.model.INewChatRoomBean;

public abstract class BaseNewChatRoomBean implements Serializable, INewChatRoomBean{
	
	private static final long serialVersionUID = 6407306352321882490L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
