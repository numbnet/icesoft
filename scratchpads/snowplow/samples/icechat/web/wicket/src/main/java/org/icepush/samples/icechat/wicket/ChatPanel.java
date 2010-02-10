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
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.icepush.samples.icechat.cdi.model.NewChatRoomMessageBean;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.UserChatSession;

/**
 *
 * @author bkroeger
 */
public final class ChatPanel extends PushPanel {

    private final ListView messagesListView;
    private final ListView usersListView;

    NewChatRoomMessageBean composingMessage = new NewChatRoomMessageBean();

    String messageInput = "";

    @Inject
    ChatManagerViewControllerSessionBean chatManagerVC;
    CompoundPropertyModel compoundChatManagerVC;

    Form chatRoomForm;

    public ChatPanel(String id) {
        super(id);
        compoundChatManagerVC = new CompoundPropertyModel(chatManagerVC);
        chatManagerVC.setPushRequestContext(pushRequestContext);

        chatRoomForm = new Form("chatRoomForm",compoundChatManagerVC);
        chatRoomForm.setOutputMarkupId(true);
        chatRoomForm.add(new Label("currentChatSessionHolder.session.room.name"));

        chatRoomForm.add(usersListView = new ListView("currentChatSessionHolder.session.room.userChatSessions"){
            public void populateItem(final ListItem listItem){
                final UserChatSession userChatSession = (UserChatSession)listItem.getModelObject();
                listItem.add(new Label("userName",userChatSession.getUser().getDisplayName()));

            }
        });

        chatRoomForm.add(messagesListView = new ListView("currentChatSessionHolder.session.room.messages"){
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
            }
        });

        add(chatRoomForm);
    }

    // PUSH CALLBACK
    protected void pushCallback(AjaxRequestTarget target) {
        usersListView.modelChanged();
        messagesListView.modelChanged();
        target.addComponent(chatRoomForm);
    }

    
    //These two methods will cause the component to render.
    @Override
     protected boolean callOnBeforeRenderIfNotVisible(){
        return true;
    }

    @Override
        protected void onBeforeRender () {
        super.onBeforeRender();
        if(chatManagerVC.getCurrentChatSessionHolder().getSession() !=null){
            usersListView.modelChanged();
            messagesListView.modelChanged();
            setVisible(true);
        }else{
            setVisible(false);
        }
    }
    
    /*This method will also cause the component to render, but is less efficient as this method is called many times in the lifecycle.
    @Override
        public boolean isVisible(){
        if(chatManagerVC.getCurrentChatSessionHolder().getSession() !=null){
            return true;
        }else{
            return false;
        }
     }*/
    
}
