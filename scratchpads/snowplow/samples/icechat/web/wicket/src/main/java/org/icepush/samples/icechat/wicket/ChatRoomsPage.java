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
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.icepush.samples.icechat.cdi.controller.LoginController;
import org.icepush.samples.icechat.cdi.facade.ChatManagerFacadeBean;
import org.icepush.samples.icechat.cdi.model.CurrentChatSessionHolderBean;
import org.icepush.samples.icechat.cdi.model.NewChatRoomBean;
import org.icepush.samples.icechat.cdi.qualifier.RemoveAmbiguity;
import org.icepush.samples.icechat.model.ChatRoom;

/**
 *
 * @author bkroeger
 */
public final class ChatRoomsPage extends AppBasePage {

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
    CompoundPropertyModel compoundChatManagerVC;

    NewChatRoomBean newChatRoomBean = new NewChatRoomBean();
    CompoundPropertyModel compoundNewChatRoomBean;

    CurrentChatSessionHolderBean currentChatSessionHolderBean = new CurrentChatSessionHolderBean();
    CompoundPropertyModel compoundCurrentChatSessionHolderBean;

    final ListView chatRoomsListView;

    public ChatRoomsPage() {
        super ();
        chatManagerVC.setChatService(chatService);
        chatManagerVC.setLoginController(loginController);
        chatManagerVC.setCurrentChatSessionHolder(currentChatSessionHolderBean);
        chatManagerFacadeBean.setChatService(chatService);

        compoundLoginController = new CompoundPropertyModel(loginController);
        compoundChatManagerFacadeBean = new CompoundPropertyModel(chatManagerFacadeBean);
        compoundChatManagerVC = new CompoundPropertyModel(chatManagerVC);
        compoundNewChatRoomBean = new CompoundPropertyModel(newChatRoomBean);
        compoundCurrentChatSessionHolderBean = new CompoundPropertyModel(currentChatSessionHolderBean);

        // ICEpush code
        getPushRequestContext();
        chatManagerVC.setPushRequestContext(pushRequestContext);
        Label pushJavascript = new Label("pushJavascript", new Model("ice.push.register('" + pushRequestContext.getCurrentPushId() + "',function(){window.location.reload();});"));
        pushJavascript.setEscapeModelStrings(false);
        add(pushJavascript);

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

        final Form chatRooms = new Form("chatRoomsForm",compoundChatManagerFacadeBean);
        chatRooms.setOutputMarkupId(true);
        
        chatRooms.add(chatRoomsListView = new ListView("chatRooms"){
            public void populateItem(final ListItem listItem){
                final ChatRoom chatRoom = (ChatRoom)listItem.getModelObject();
                listItem.add(new AjaxButton("name",new Model(chatRoom.getName())) {
                    protected void onSubmit(AjaxRequestTarget target, Form form) {
                        chatManagerVC.openChatSession(chatRoom.getName());
                        setResponsePage(new ChatPage());
                    }
	        });
                
            }
        });

        add(chatRooms);

        Form createNewChatRoom = new Form("createNewChatRoomForm",compoundNewChatRoomBean);
        createNewChatRoom.add(new RequiredTextField<String>("name"));
	createNewChatRoom.add(new AjaxButton("registerButton") {
			protected void onSubmit(AjaxRequestTarget target, Form form) {
                            chatManagerVC.setNewChatRoomBean(newChatRoomBean);
                            chatManagerVC.createNewChatRoom();
                            chatRoomsListView.modelChanged();
                            target.addComponent(chatRooms);
			}
		});
        add(createNewChatRoom);

    }

}

