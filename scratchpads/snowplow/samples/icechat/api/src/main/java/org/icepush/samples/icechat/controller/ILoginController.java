package org.icepush.samples.icechat.controller;

import org.icepush.samples.icechat.IPushRequestContext;
import org.icepush.samples.icechat.controller.model.ICredentialsBean;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.service.exception.LoginFailedException;

public interface ILoginController {
	
	public ICredentialsBean getCredentialsBean();

	public void setCredentialsBean(ICredentialsBean credentialsBean);

	public IPushRequestContext getPushRequestContext() ;

	public void setPushRequestContext(IPushRequestContext pushRequestContext) ;

	public void setChatService(IChatService chatService) ;

	public User getCurrentUser();
	
	public void login() throws LoginFailedException;
	
	public void login(String userName, String password) throws LoginFailedException;

    public void register();
    
    public void register(String userName, String nickName, String password);
    
    public void logout();


}
