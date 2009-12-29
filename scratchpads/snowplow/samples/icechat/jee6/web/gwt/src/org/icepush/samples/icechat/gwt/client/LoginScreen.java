package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;


public class LoginScreen extends AbstractScreen{


	public LoginScreen(){
		DialogBox panel = new DialogBox();
		panel.setText("Please Login");
		
		VerticalPanel root = new VerticalPanel();
		root.setPixelSize(250, 150);

		HorizontalPanel usernamePanel= new HorizontalPanel();
		
		Label usernameLabel = new Label("Username:");
		usernamePanel.add(usernameLabel);
		
		TextBox usernameText = new TextBox();
		usernamePanel.add(usernameText);
		root.add(usernamePanel);
		
		HorizontalPanel passwordPanel = new HorizontalPanel();
		
		Label passwordLabel = new Label("Password:");
		passwordPanel.add(passwordLabel);
		
		PasswordTextBox passwordText = new PasswordTextBox();
		passwordPanel.add(passwordText);
		root.add(passwordPanel);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonPanel.setWidth("100%");
		
		Button loginButton =  new Button();
		loginButton.setText("Login");
		loginButton.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				Window.alert("Attempting Login");
			}
		});
		buttonPanel.add(loginButton);
		root.add(buttonPanel);
		
		HorizontalPanel regLinkPanel = new HorizontalPanel();
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonPanel.setWidth("100%");
		
		Label notAMemberLabel = new Label("Not a member? ");
		regLinkPanel.add(notAMemberLabel);
		
		Hyperlink regLink = new Hyperlink("Register","reg");
		regLinkPanel.add(regLink);
		root.add(regLinkPanel);
		
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
