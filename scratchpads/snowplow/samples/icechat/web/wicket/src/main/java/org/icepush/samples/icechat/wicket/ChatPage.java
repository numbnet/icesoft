/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.wicket;
import java.util.ArrayList;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.wicket.model.NewChatRoomMessageBean;
import org.icepush.samples.icechat.wicket.view.ChatManagerViewControllerBean;

/**
 *
 * @author bkroeger
 */
public final class ChatPage extends AppBasePage {

    private final ListView messagesListView;
    private final ListView usersListView;

    NewChatRoomMessageBean composingMessage = new NewChatRoomMessageBean();

    String messageInput = "Default";

    ChatManagerViewControllerBean chatManagerVC;

    public ChatPage(IModel model) {
        super ();
        chatManagerVC = (ChatManagerViewControllerBean)model.getObject();
        chatManagerVC.setNewChatRoomMessageBean(composingMessage);

        Form chatRoomForm = new Form("chatRoomForm",model);
        chatRoomForm.add(new Label("currentChatSessionHolder.session.room.name"));

        chatRoomForm.add(usersListView = new ListView("usersListView", (ArrayList)chatManagerVC.getCurrentChatSessionHolder().getSession().getRoom().getUserChatSessions()){
            public void populateItem(final ListItem listItem){
                final UserChatSession userChatSession = (UserChatSession)listItem.getModelObject();
                listItem.add(new Label("userName",userChatSession.getUser().getDisplayName()));

            }
        });

        chatRoomForm.add(messagesListView = new ListView("messagesListView", chatManagerVC.getCurrentChatSessionHolder().getSession().getRoom().getMessages()){
            public void populateItem(final ListItem listItem){
                final Message message = (Message)listItem.getModelObject();
                listItem.add(new Label("created",message.getCreated().toString()));
                listItem.add(new Label("userChatSession.user.displayName",message.getUserChatSession().getUser().getDisplayName()));
                listItem.add(new Label("message",message.getMessage()));

            }
        });

        chatRoomForm.add(new TextField("messageInput",new PropertyModel(this,"messageInput")));
	chatRoomForm.add(new AjaxButton("send") {
			protected void onSubmit(AjaxRequestTarget target, Form form) {
                            composingMessage.setMessage(messageInput);
                            chatManagerVC.sendNewMessage();
                            messagesListView.modelChanged();
                            setResponsePage(getPage());
			}
		});

        add(chatRoomForm);
    }

    /*
    These two methods will cause the component to render.
    @Override
     protected boolean callOnBeforeRenderIfNotVisible(){
        return true;
    }

    @Override
        protected void onBeforeRender () {
        super.onBeforeRender();
        setVisible(true);
    }
    This method will also cause the component to render, but is less efficient as this method is called many times in the lifecycle.
    @Override
        public boolean isVisible(){
         return true;
     }
      */
}
