package org.icepush.samples.icechat.gwt.client.screens;

import org.icepush.samples.icechat.gwt.client.Credentials;
import org.icepush.samples.icechat.gwt.client.UserSession;
import org.icepush.samples.icechat.gwt.client.service.UserService;
import org.icepush.samples.icechat.gwt.client.service.UserServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;

public class StartScreen extends DialogBox{
	
	@UiField
	TextBox displayNameText;
	
	private Binder binder = GWT.create(Binder.class);
	
	public StartScreen(){
		
		this.setText("Start Chatting!!");
		this.center();
		this.setModal(true);
		this.setGlassEnabled(true);
		this.setWidget(binder.createAndBindUi(this));
	}
	
	@UiHandler({"submitButton"})
	public void start(ClickEvent ev){
		UserServiceAsync service = GWT.create(UserService.class);
		
		AsyncCallback<Credentials> callback = new AsyncCallback<Credentials>() {


			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				
			}

			public void onSuccess(Credentials credentials) {
				UserSession.getInstance().setCredentials(credentials);
				MainPanelRegistry.getInstance().getCredentialsPanel().setUsername(credentials.getUserName());
				StartScreen.this.hide();
			}
		};
		
		service.register(displayNameText.getText(), callback);
	}
	
	public interface Binder extends UiBinder<Panel, StartScreen>{}
}
