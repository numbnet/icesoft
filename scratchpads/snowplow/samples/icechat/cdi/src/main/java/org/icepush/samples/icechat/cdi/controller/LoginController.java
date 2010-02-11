package org.icepush.samples.icechat.cdi.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.icepush.samples.icechat.IPushRequestContext;
import org.icepush.samples.icechat.beans.controller.BaseLoginControllerBean;
import org.icepush.samples.icechat.controller.model.ICredentialsBean;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.cdi.qualifier.RemoveAmbiguity;


@Named
@SessionScoped
public class LoginController extends BaseLoginControllerBean implements Serializable{
	
	@Inject
	@Override
	public void setChatService(IChatService chatService){
		super.setChatService(chatService);
	}
	
	@Inject
	@Override
	public void setCredentialsBean(ICredentialsBean credentialsBean){
		super.setCredentialsBean(credentialsBean);
	}
	
	@Inject
	@Override
	public void setPushRequestContext(IPushRequestContext pushRequestContext){
		super.setPushRequestContext(pushRequestContext);
	}

}
