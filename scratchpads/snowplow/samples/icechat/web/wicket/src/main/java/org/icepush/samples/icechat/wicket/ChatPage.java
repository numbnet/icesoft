/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.wicket;
import java.util.ArrayList;
import javax.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.icepush.samples.icechat.cdi.model.NewChatRoomMessageBean;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.UserChatSession;

/**
 *
 * @author bkroeger
 */
public final class ChatPage extends AppBasePage {

    private final ListView messagesListView;
    private final ListView usersListView;

    NewChatRoomMessageBean composingMessage = new NewChatRoomMessageBean();

    String messageInput = "";

    @Inject
    ChatManagerViewControllerSessionBean chatManagerVC;
    CompoundPropertyModel compoundChatManagerVC;

    public ChatPage() {
        super ();
        compoundChatManagerVC = new CompoundPropertyModel(chatManagerVC);

        // ICEpush code
        getPushRequestContext();
        chatManagerVC.setPushRequestContext(pushRequestContext);
        Label pushJavascript = new Label("pushJavascript", new Model("ice.push.register('" + pushRequestContext.getCurrentPushId() + "',function(){window.location.reload();});"));
        pushJavascript.setEscapeModelStrings(false);
        add(pushJavascript);

        
        final Form chatRoomForm = new Form("chatRoomForm",compoundChatManagerVC);
        chatRoomForm.setOutputMarkupId(true);
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
                            chatManagerVC.setNewChatRoomMessageBean(composingMessage);
                            chatManagerVC.sendNewMessage();
                            messagesListView.modelChanged();
                            target.addComponent(chatRoomForm);
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
