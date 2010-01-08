package org.icepush.samples.icechat.gwt.client.screens;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import org.icepush.samples.icechat.gwt.client.Credentials;
import org.icepush.samples.icechat.gwt.client.User;
import org.icepush.samples.icechat.gwt.client.service.UserService;
import org.icepush.samples.icechat.gwt.client.service.UserServiceAsync;
import org.icepush.samples.icechat.gwt.client.UserSession;


public class RegisterScreen extends AbstractScreen{

	private Label errorLabel;
	private TextBox usernameText;
	private TextBox nicknameText;
	private PasswordTextBox passwordText;
	private PasswordTextBox confirmPasswordText;

	public RegisterScreen(){
		DialogBox panel = new DialogBox();
		panel.setText("Please Provide Some Additional Information");
		panel.setGlassEnabled(true);
		VerticalPanel root = new VerticalPanel();
		
		errorLabel = new Label();
		errorLabel.setVisible(false);
		root.add(errorLabel);
		
		Grid fieldsGrid = new Grid(4,2);
	
		fieldsGrid.setText(0,0,"Username:");
		fieldsGrid.setText(1,0,"Nickname:");
		fieldsGrid.setText(2,0,"Password:");
		fieldsGrid.setText(3,0,"Confirm Password:");
		
		usernameText = new TextBox();
		nicknameText = new TextBox();
		passwordText = new PasswordTextBox();
		confirmPasswordText = new PasswordTextBox();
		
		fieldsGrid.setWidget(0,1,usernameText);
		fieldsGrid.setWidget(1,1,nicknameText);
		fieldsGrid.setWidget(2,1,passwordText);
		fieldsGrid.setWidget(3,1,confirmPasswordText);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonPanel.setWidth("100%");
		
		Button registerButton = new Button("Register");
		registerButton.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				if(!RegisterScreen.this.passwordText.getText().equals(RegisterScreen.this.confirmPasswordText.getText())){
					RegisterScreen.this.errorLabel.setText("Error: passwords do not match...");
					RegisterScreen.this.errorLabel.setVisible(true);
				}else{
					RegisterScreen.this.errorLabel.setVisible(false);
					
					UserServiceAsync service = (UserServiceAsync) GWT.create(UserService.class);
					AsyncCallback<Credentials> callback = new AsyncCallback<Credentials>(){
						public void onSuccess(Credentials result){
							UserSession.getInstance().setCredentials(result);
                                                        PopupRegistry.getInstance().hideScreen("reg");
                                                        MainPanelRegistry.getInstance().getCredentialsPanel().setUsername(result.getUserName());

						}
						
						public void onFailure(Throwable t){
							Window.alert("RPC failed: " + t.getMessage());
						}
					};
					
					User user = new User();
					user.setUsername(RegisterScreen.this.usernameText.getText());
                                        user.setNickname(RegisterScreen.this.nicknameText.getText());
                                        user.setPassword(RegisterScreen.this.passwordText.getText());
					service.register(user, callback);
				}
			}
		});
		
		buttonPanel.add(registerButton);
		
		root.add(fieldsGrid);
		root.add(buttonPanel);
		
		panel.setWidget(root);
		panel.center();
		
		this.initScreen(panel);
	}
	
	@Override
	public void show(){
		((DialogBox)this.root).show();
	}
	
	@Override
	public void hide(){
		((DialogBox)this.root).hide();
	}
	
}