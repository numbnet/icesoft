/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
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
import org.icepush.samples.icechat.cdi.model.CredentialsBean;
import org.icepush.samples.icechat.controller.ILoginController;
import org.icepush.samples.icechat.service.exception.LoginFailedException;

/**
 *
 */
public final class LoginPage extends AppBasePage {

    @Inject
    ILoginController loginController;
    CompoundPropertyModel compoundLoginController = new CompoundPropertyModel(loginController);

    CredentialsBean credentialsBean = new CredentialsBean();

    public LoginPage() {
        super ();

        loginController.setChatService(chatService);
        loginController.setCredentialsBean(credentialsBean);

        final Form loginForm = new Form("login",compoundLoginController);
        final FeedbackPanel feedbackPanel = new FeedbackPanel("loginMessages");
        feedbackPanel.setOutputMarkupId(true);

        loginForm.add(new RequiredTextField<String>("credentialsBean.userName"));
        loginForm.add(new TextField<String>("credentialsBean.nickName"));
        loginForm.add(new PasswordTextField("credentialsBean.password"));
        loginForm.add(new AjaxButton("loginButton") {
                protected void onSubmit(AjaxRequestTarget target, Form form) {
                        try {
                            loginController.login(credentialsBean.getUserName(),
                                            credentialsBean.getPassword());
                            setResponsePage(new ChatPage());
                        } catch (LoginFailedException e) {
                            this.warn(e.getMessage());
                            target.addComponent(feedbackPanel);
                        }
                }
        });
        loginForm.add(new AjaxButton("registerButton") {
                protected void onSubmit(AjaxRequestTarget target, Form form) {

                    loginController.register(credentialsBean.getUserName(),
                                             credentialsBean.getNickName(),
                                             credentialsBean.getPassword());
                    setResponsePage(new ChatPage());
                }
        });

        loginForm.add(feedbackPanel);

        add(loginForm);        
    }

}

