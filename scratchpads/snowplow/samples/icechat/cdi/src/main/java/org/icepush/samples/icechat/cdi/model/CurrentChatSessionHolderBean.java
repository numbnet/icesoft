package org.icepush.samples.icechat.cdi.model;

import javax.enterprise.inject.Model;

import org.icepush.samples.icechat.beans.model.BaseCurrentChatSessionHolderBean;
import org.icepush.samples.icechat.controller.model.ICurrentChatSessionHolderBean;

@Model
public class CurrentChatSessionHolderBean extends BaseCurrentChatSessionHolderBean
		implements ICurrentChatSessionHolderBean {}
