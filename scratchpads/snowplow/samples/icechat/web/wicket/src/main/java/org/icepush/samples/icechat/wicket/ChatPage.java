/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.wicket;
import javax.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.icepush.samples.icechat.cdi.controller.LoginController;
import org.icepush.samples.icechat.cdi.facade.ChatManagerFacadeBean;
import org.icepush.samples.icechat.cdi.model.CurrentChatSessionHolderBean;
import org.icepush.samples.icechat.cdi.qualifier.RemoveAmbiguity;

/**
 *
 * @author bkroeger
 */
public final class ChatPage extends AppBasePage {

    // Session Scoped Beans
    @Inject
    @RemoveAmbiguity
    LoginController loginController;
    CompoundPropertyModel compoundLoginController;
    
    @Inject
    ChatManagerFacadeBean chatManagerFacadeBean;
    CompoundPropertyModel compoundChatManagerFacadeBean;
    
    @Inject
    ChatManagerViewControllerSessionBean chatManagerVC;

    CurrentChatSessionHolderBean currentChatSessionHolderBean = new CurrentChatSessionHolderBean();


    public ChatPage() {
        super ();
        // Initialize Model
        chatManagerVC.setChatService(chatService);
        chatManagerVC.setLoginController(loginController);
        chatManagerVC.setCurrentChatSessionHolder(currentChatSessionHolderBean);
        chatManagerFacadeBean.setChatService(chatService);

        compoundLoginController = new CompoundPropertyModel(loginController);
        compoundChatManagerFacadeBean = new CompoundPropertyModel(chatManagerFacadeBean);

        // Add Components to page
        Form chatSession = new Form("chatSession",compoundLoginController);
        chatSession.add(new Label("credentialsBean.userName"));
        chatSession.add(new Label("credentialsBean.nickName"));
	chatSession.add(new AjaxButton("logout") {
			protected void onSubmit(AjaxRequestTarget target, Form form) {
                            loginController.logout();
                            setResponsePage(LoginPage.class);
			}
		});
        add(chatSession);

        add(new ChatRoomsPanel("chatRoomsPanel",compoundChatManagerFacadeBean));
        
        add(new ChatPanel("chatPanel"));

    }

}

