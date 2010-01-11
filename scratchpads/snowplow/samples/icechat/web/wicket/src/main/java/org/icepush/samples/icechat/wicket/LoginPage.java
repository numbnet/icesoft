/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.wicket;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.icepush.samples.icechat.service.exception.LoginFailedException;

/**
 *
 * @author bkroeger
 */
public final class LoginPage extends AppBasePage {
    public LoginPage() {
        super ();
        final Form loginForm = new Form("login",compoundCredentialsBean);

        loginForm.add(new RequiredTextField<String>("userName"));
        loginForm.add(new TextField<String>("nickName"));
        loginForm.add(new PasswordTextField("password"));
        loginForm.add(new AjaxButton("login") {
                protected void onSubmit(AjaxRequestTarget target, Form form) {
                        try {
                            loginController.login(credentialsBean.getUserName(),
                                            credentialsBean.getPassword());
                            loginForm.setVisible(false);
                        } catch (LoginFailedException e) {
                                this.warn(e.getMessage());
                        }
                        setResponsePage(new ChatPage(compoundCredentialsBean));
                }
        });
        loginForm.add(new AjaxButton("register") {
                protected void onSubmit(AjaxRequestTarget target, Form form) {

                    loginController.register(credentialsBean.getUserName(),
                                             credentialsBean.getNickName(),
                                             credentialsBean.getPassword());
                    loginForm.setVisible(false);
                    setResponsePage(new ChatPage(compoundCredentialsBean));
                }
        });
        add(loginForm);
        loginForm.add(new FeedbackPanel("loginMessages")
                        .setOutputMarkupId(true));
    }

}

