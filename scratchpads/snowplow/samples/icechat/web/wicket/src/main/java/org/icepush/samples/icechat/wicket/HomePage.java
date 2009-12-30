package org.icepush.samples.icechat.wicket;

import java.io.Serializable;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.icepush.samples.icechat.AbstractPushRequestContext;
import org.icepush.samples.icechat.IPushRequestContext;
import org.icepush.samples.icechat.beans.model.BaseCredentialsBean;
import org.icepush.samples.icechat.cdi.controller.LoginController;
import org.icepush.samples.icechat.service.exception.LoginFailedException;

public class HomePage extends WebPage {

	@Inject
	LoginController loginController;
	
	//@EJB
    //private ChatServiceLocal chatService;
	
	private IPushRequestContext pushRequestContext;

	private CredentialsBean credentialsBean = new CredentialsBean();
	
	class CredentialsBean extends BaseCredentialsBean{}

	public HomePage() {
		super();
		final Form loginForm = new Form("login");
		loginForm.add(new RequiredTextField<String>("userName", new Model() {
			public Serializable getObject() {
				return credentialsBean.getUserName();
			}

			public void setObject(Object object) {
				credentialsBean.setUserName((String) object);
			}
		}));
		loginForm.add(new TextField<String>("nickName", new Model() {
			public Serializable getObject() {
				return credentialsBean.getNickName();
			}

			public void setObject(Object object) {
				credentialsBean.setNickName((String) object);
			}
		}));
		loginForm.add(new PasswordTextField("password", new Model() {
			public Serializable getObject() {
				return credentialsBean.getPassword();
			}

			public void setObject(Object object) {
				credentialsBean.setPassword((String) object);
			}
		}));
		loginForm.add(new AjaxButton("login") {
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				try {
					/*
					chatService.login(credentialsBean.getUserName(),
							credentialsBean.getPassword());
					PushContext pushContext = getPushRequestContext().getPushContext();
			        pushContext.addGroupMember(PushConstants.CHAT_USERS.name(), 
							pushRequestContext.getCurrentPushId());
			        pushContext.push(PushConstants.CHAT_USERS.name());
			        */
					loginController.login(credentialsBean.getUserName(),
							credentialsBean.getPassword());
					loginForm.setVisible(false);
				} catch (LoginFailedException e) {
					this.warn(e.getMessage());
				}
				target.addComponent(form);
			}
		});
		loginForm.add(new AjaxButton("register") {
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				/*
				System.out.println("chatService: " + chatService);
				chatService.register(credentialsBean.getUserName(), credentialsBean.getNickName(), 
						credentialsBean.getPassword());
				PushContext pushContext = getPushRequestContext().getPushContext();
		        pushContext.addGroupMember(PushConstants.CHAT_USERS.name(), 
						pushRequestContext.getCurrentPushId());
		        pushContext.push(PushConstants.CHAT_USERS.name());
		        */
				loginController.register(credentialsBean.getUserName(), credentialsBean.getNickName(), 
						credentialsBean.getPassword());
				loginForm.setVisible(false);
				target.addComponent(form);
			}
		});
		add(loginForm);
		loginForm.add(new FeedbackPanel("loginMessages")
				.setOutputMarkupId(true));

	}
	
	class WicketPushRequestContextAdapter extends AbstractPushRequestContext{
		public WicketPushRequestContextAdapter(){
			
			//TODO tied to servlet, not portlet
			WebRequest webRequest = (WebRequest)getWebRequestCycle().getRequest();
			WebResponse webResponse = (WebResponse)getWebRequestCycle().getResponse();
			
			intializePushContext((HttpServletRequest)webRequest.getHttpServletRequest(), 
					(HttpServletResponse)webResponse.getHttpServletResponse());
		}
	}
	
	public IPushRequestContext getPushRequestContext(){
		if( pushRequestContext == null ){
			pushRequestContext = new WicketPushRequestContextAdapter();
		}
		return pushRequestContext;
	}

}

