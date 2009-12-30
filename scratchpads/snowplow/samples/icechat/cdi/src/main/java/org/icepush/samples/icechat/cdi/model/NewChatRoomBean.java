package org.icepush.samples.icechat.cdi.model;

import javax.enterprise.inject.Model;

import org.icepush.samples.icechat.beans.model.BaseNewChatRoomBean;
import org.icepush.samples.icechat.controller.model.INewChatRoomBean;

@Model
public class NewChatRoomBean extends
		BaseNewChatRoomBean implements
		INewChatRoomBean {}
