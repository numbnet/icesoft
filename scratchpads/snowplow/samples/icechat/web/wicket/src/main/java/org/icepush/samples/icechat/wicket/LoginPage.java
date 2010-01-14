/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.wicket;
import javax.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.icepush.samples.icechat.cdi.controller.LoginController;
import org.icepush.samples.icechat.cdi.model.CredentialsBean;
import org.icepush.samples.icechat.cdi.qualifier.RemoveAmbiguity;
import org.icepush.samples.icechat.service.exception.LoginFailedException;

/**
 *
 * @author bkroeger
 */
public final class LoginPage extends AppBasePage {

    @Inject
    @RemoveAmbiguity
    LoginController loginController;

    @Inject
    CredentialsBean credentialsBean;

    CompoundPropertyModel compoundLoginController = new CompoundPropertyModel(loginController);

    public LoginPage() {
        super ();
        System.out.println("CONSTRUCTOR: LOGIN PAGE !!");
        loginController.setChatService(chatService);
        loginController.setCredentialsBean(credentialsBean);
        final Form loginForm = new Form("login",compoundLoginController);

        loginForm.add(new RequiredTextField<String>("credentialsBean.userName"));
        loginForm.add(new TextField<String>("credentialsBean.nickName"));
        loginForm.add(new PasswordTextField("credentialsBean.password"));
        loginForm.add(new AjaxButton("loginButton") {
                protected void onSubmit(AjaxRequestTarget target, Form form) {
                        try {
                            loginController.login(credentialsBean.getUserName(),
                                            credentialsBean.getPassword());
                            //loginForm.setVisible(false);
                        } catch (LoginFailedException e) {
                                this.warn(e.getMessage());
                        }
                        setResponsePage(new ChatRoomsPage());
                }
        });
        loginForm.add(new AjaxButton("registerButton") {
                protected void onSubmit(AjaxRequestTarget target, Form form) {

                    loginController.register(credentialsBean.getUserName(),
                                             credentialsBean.getNickName(),
                                             credentialsBean.getPassword());
                    //loginForm.setVisible(false);
                    setResponsePage(new ChatRoomsPage());
                }
        });
        add(loginForm);
        loginForm.add(new FeedbackPanel("loginMessages")
                        .setOutputMarkupId(true));
    }

}

