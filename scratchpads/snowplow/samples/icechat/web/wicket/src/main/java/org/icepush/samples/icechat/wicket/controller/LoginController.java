/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.wicket.controller;

import java.io.Serializable;

import org.icepush.samples.icechat.IPushRequestContext;
import org.icepush.samples.icechat.beans.controller.BaseLoginControllerBean;
import org.icepush.samples.icechat.controller.model.ICredentialsBean;
import org.icepush.samples.icechat.service.IChatService;


public class LoginController extends BaseLoginControllerBean implements Serializable{

	@Override
	public void setChatService(IChatService chatService){
		super.setChatService(chatService);
	}

	@Override
	public void setCredentialsBean(ICredentialsBean credentialsBean){
		super.setCredentialsBean(credentialsBean);
	}

	@Override
	public void setPushRequestContext(IPushRequestContext pushRequestContext){
		super.setPushRequestContext(pushRequestContext);
	}

}
