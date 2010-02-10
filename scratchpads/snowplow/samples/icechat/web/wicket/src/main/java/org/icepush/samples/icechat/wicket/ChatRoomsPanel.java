/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.wicket;

import javax.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.icepush.samples.icechat.cdi.model.NewChatRoomBean;
import org.icepush.samples.icechat.model.ChatRoom;

/**
 *
 * @author bkroeger
 */
public final class ChatRoomsPanel extends Panel{

    @Inject
    ChatManagerViewControllerSessionBean chatManagerVC;
    
    NewChatRoomBean newChatRoomBean = new NewChatRoomBean();
    CompoundPropertyModel compoundNewChatRoomBean = new CompoundPropertyModel(newChatRoomBean);

    final ListView chatRoomsListView;

    public ChatRoomsPanel(String id,IModel compoundChatManagerFacadeBean) {
        super (id);

        final Form chatRooms = new Form("chatRoomsForm",compoundChatManagerFacadeBean);
        chatRooms.setOutputMarkupId(true);
        chatRooms.add(chatRoomsListView = new ListView("chatRooms"){
            public void populateItem(final ListItem listItem){
                final ChatRoom chatRoom = (ChatRoom)listItem.getModelObject();
                listItem.add(new AjaxButton("name",new Model(chatRoom.getName())) {
                    protected void onSubmit(AjaxRequestTarget target, Form form) {
                        chatManagerVC.openChatSession(chatRoom.getName());
                        setResponsePage(getPage());
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
