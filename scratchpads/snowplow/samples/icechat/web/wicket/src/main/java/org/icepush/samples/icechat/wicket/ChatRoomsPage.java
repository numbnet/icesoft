/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.wicket;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.icepush.samples.icechat.beans.model.BaseNewChatRoomBean;
import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.wicket.controller.LoginController;
import org.icepush.samples.icechat.wicket.facade.ChatManagerFacadeBean;
import org.icepush.samples.icechat.wicket.model.CurrentChatSessionHolderBean;
import org.icepush.samples.icechat.wicket.view.ChatManagerViewControllerBean;

/**
 *
 * @author bkroeger
 */
public final class ChatRoomsPage extends AppBasePage {

    ChatManagerViewControllerBean chatManagerVC = new ChatManagerViewControllerBean();
    CompoundPropertyModel compoundChatManagerVC;

    ChatManagerFacadeBean chatManagerFacadeBean = new ChatManagerFacadeBean();
    CompoundPropertyModel compoundChatManagerFacadeBean;

    NewChatRoomBean newChatRoomBean = new NewChatRoomBean();
    class NewChatRoomBean extends BaseNewChatRoomBean{}
    CompoundPropertyModel compoundNewChatRoomBean;

    CurrentChatSessionHolderBean currentChatSessionHolderBean = new CurrentChatSessionHolderBean();
    CompoundPropertyModel compoundCurrentChatSessionHolderBean;

    final ListView chatRoomsListView;
    final LoginController loginController;

    public ChatRoomsPage(IModel model) {
        super ();

        loginController = (LoginController)model.getObject();
        chatManagerVC.setChatService(AppBasePage.chatService);
        chatManagerVC.setLoginController(loginController);
        chatManagerVC.setCurrentChatSessionHolder(currentChatSessionHolderBean);
        chatManagerFacadeBean.setChatService(AppBasePage.chatService);

        compoundChatManagerVC = new CompoundPropertyModel(chatManagerVC);
        compoundChatManagerFacadeBean = new CompoundPropertyModel(chatManagerFacadeBean);
        compoundNewChatRoomBean = new CompoundPropertyModel(newChatRoomBean);
        compoundCurrentChatSessionHolderBean = new CompoundPropertyModel(currentChatSessionHolderBean);

        Form chatSession = new Form("chatSession",model);
        chatSession.add(new Label("credentialsBean.userName"));
        chatSession.add(new Label("credentialsBean.nickName"));
	chatSession.add(new AjaxButton("logout") {
			protected void onSubmit(AjaxRequestTarget target, Form form) {
                            loginController.logout();
                            setResponsePage(LoginPage.class);
			}
		});
        add(chatSession);

        final Form chatRooms = new Form("chatRoomsForm",compoundChatManagerFacadeBean);
        //chatRooms.setOutputMarkupId(true);
        
        chatRooms.add(chatRoomsListView = new ListView("chatRooms"){
            public void populateItem(final ListItem listItem){
                final ChatRoom chatRoom = (ChatRoom)listItem.getModelObject();
                listItem.add(new AjaxButton("name",new Model(chatRoom.getName())) {
                    protected void onSubmit(AjaxRequestTarget target, Form form) {
                        chatManagerVC.openChatSession(chatRoom.getName());
                        setResponsePage(new ChatPage(compoundChatManagerVC));
                    }
	        });
                
            }
        });

        add(chatRooms);

        Form createNewChatRoom = new Form("createNewChatRoomForm",compoundNewChatRoomBean);
        createNewChatRoom.add(new RequiredTextField<String>("name"));
	createNewChatRoom.add(new AjaxButton("registerButton") {
			protected void onSubmit(AjaxRequestTarget target, Form form) {
                            // room was null when this call made outside of this scope
                            chatManagerVC.setNewChatRoomBean(newChatRoomBean);
                            System.out.println("LoginController " + loginController);
                                        System.out.println("username" + loginController.getCurrentUser().getUserName());
            System.out.println("password" + loginController.getCurrentUser().getPassword());
                            chatManagerVC.createNewChatRoom();
                            newChatRoomBean = new NewChatRoomBean();
                            chatRoomsListView.modelChanged();
                            //target.addComponent(chatRooms);
                            setResponsePage(getPage());
			}
		});
        add(createNewChatRoom);

    }

}

