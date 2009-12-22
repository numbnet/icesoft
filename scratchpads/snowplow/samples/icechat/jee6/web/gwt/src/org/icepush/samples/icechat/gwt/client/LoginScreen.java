package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;


public class LoginScreen extends AbstractScreen{


	public LoginScreen(){
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
		this.initScreen(root);
	}
}
