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
import org.icepush.samples.icechat.wicket.facade.ChatManagerFacadeBean;
import org.icepush.samples.icechat.wicket.model.CurrentChatSessionHolderBean;
import org.icepush.samples.icechat.wicket.service.ChatServiceApplicationBean;
import org.icepush.samples.icechat.wicket.view.ChatManagerViewControllerBean;

/**
 *
 * @author bkroeger
 */
public final class ChatPage extends AppBasePage {

    ChatManagerViewControllerBean chatManagerVC;

    ChatManagerFacadeBean chatManagerFacadeBean;

    ChatServiceApplicationBean chatService;

    private NewChatRoomBean newChatRoomBean = new NewChatRoomBean();

    class NewChatRoomBean extends BaseNewChatRoomBean{}

    private CompoundPropertyModel compoundNewChatRoomBean = new CompoundPropertyModel(newChatRoomBean);

    private CurrentChatSessionHolderBean currentChatSessionHolderBean = new CurrentChatSessionHolderBean();

    private CompoundPropertyModel compoundCurrentChatSessionHolderBean = new CompoundPropertyModel(currentChatSessionHolderBean);

    private final ListView chatRoomsListView;

    public ChatPage(IModel model) {
        super ();
        chatManagerVC.setChatService(chatService);
        chatManagerVC.setLoginController(loginController);
        chatManagerVC.setCurrentChatSessionHolder(currentChatSessionHolderBean);

        Form chatSession = new Form("chatSession",model);
        chatSession.add(new Label("userName"));
        chatSession.add(new Label("nickName"));
	chatSession.add(new AjaxButton("logout") {
			protected void onSubmit(AjaxRequestTarget target, Form form) {
                            loginController.logout();
                            setResponsePage(LoginPage.class);
			}
		});
        add(chatSession);

        final Form chatRooms = new Form("chatRooms");
        //chatRooms.setOutputMarkupId(true);
        
        chatRooms.add(chatRoomsListView = new ListView("chatRoomsListView", chatManagerFacadeBean.getChatRooms()){
            public void populateItem(final ListItem listItem){
                final ChatRoom chatRoom = (ChatRoom)listItem.getModelObject();
                listItem.add(new AjaxButton("name",new Model(chatRoom.getName())) {
                    protected void onSubmit(AjaxRequestTarget target, Form form) {
                        chatManagerVC.openChatSession(chatRoom.getName());
                        setResponsePage(getPage().getClass());
                    }
	        });
                
            }
        });

        add(chatRooms);

        Form createNewChatRoom = new Form("createNewChatRoom",compoundNewChatRoomBean);
        createNewChatRoom.add(new RequiredTextField<String>("name"));
	createNewChatRoom.add(new AjaxButton("register") {
			protected void onSubmit(AjaxRequestTarget target, Form form) {
                            // room was null when this call made outside of this scope
                            chatManagerVC.setNewChatRoomBean(newChatRoomBean);
                            chatManagerVC.createNewChatRoom();
                            chatRoomsListView.modelChanged();
                            //target.addComponent(chatRooms);
                            setResponsePage(getPage().getClass());
			}
		});
        add(createNewChatRoom);

        if(currentChatSessionHolderBean.getSession()!= null){
           add(new ChatRoomView("chatRoomView",compoundCurrentChatSessionHolderBean));
        }else{
           add(new Label("chatRoomView","NA"));
        }

    }

}

