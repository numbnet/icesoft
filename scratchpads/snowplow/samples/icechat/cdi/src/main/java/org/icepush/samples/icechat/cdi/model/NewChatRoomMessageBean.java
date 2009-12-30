package org.icepush.samples.icechat.cdi.model;

import javax.enterprise.inject.Model;

import org.icepush.samples.icechat.beans.model.BaseNewChatRoomMessageBean;
import org.icepush.samples.icechat.controller.model.INewChatRoomMessageBean;

@Model
public class NewChatRoomMessageBean extends
		BaseNewChatRoomMessageBean
		implements INewChatRoomMessageBean {}
