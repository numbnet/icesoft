package org.icepush.samples.icechat;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.icepush.samples.icechat.beans.model.CredentialsBean;

public class LoginPage  extends WebPage{
	
	public LoginPage(){
		super();
		
		Form loginForm = new Form("login");
		loginForm.add(new RequiredTextField<String>("userName"));
		loginForm.add(new TextField<String>("nickName"));
		loginForm.add(new PasswordTextField("password"));
		loginForm.add(new Button("login"));
		loginForm.add(new Button("register"));
		add(loginForm);
		
		IModel loginModel = new Model(new CredentialsBean());
		
	}

}
