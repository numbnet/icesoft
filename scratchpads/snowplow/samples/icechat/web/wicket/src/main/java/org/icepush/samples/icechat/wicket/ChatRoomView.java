/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.wicket;
import java.util.Iterator;
import javax.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.icepush.samples.icechat.cdi.model.CurrentChatSessionHolderBean;
import org.icepush.samples.icechat.cdi.model.NewChatRoomMessageBean;
import org.icepush.samples.icechat.cdi.view.ChatManagerViewControllerBean;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.UserChatSession;

/**
 *
 * @author bkroeger
 */
public final class ChatRoomView extends Panel {

    @Inject
    ChatManagerViewControllerBean chatManagerVC;

    private final ListView messagesListView;

    private NewChatRoomMessageBean composingMessage = new NewChatRoomMessageBean();

    public ChatRoomView(String id,IModel model) {
        super (id,model);
        chatManagerVC.setNewChatRoomMessageBean(composingMessage);

        Form chatRoomForm = new Form("chatRoomForm",model);
        chatRoomForm.add(new Label("session.room.name"));

        RepeatingView userChatSessionsRepeater = new RepeatingView("userChatSessionsRepeater");
        Iterator chatSessionIterator = ((CurrentChatSessionHolderBean)model.getObject()).getSession().getRoom().getUserChatSessions().iterator();
        while(chatSessionIterator.hasNext()){
            final UserChatSession userChatSession = (UserChatSession)chatSessionIterator.next();
            userChatSessionsRepeater.add(new Label(userChatSession.getUser().getDisplayName()));
        }
        chatRoomForm.add(userChatSessionsRepeater);

        chatRoomForm.add(messagesListView = new ListView("messagesListView", ((CurrentChatSessionHolderBean)model.getObject()).getSession().getRoom().getMessages()){
            public void populateItem(final ListItem listItem){
                final Message message = (Message)listItem.getModelObject();
                listItem.add(new Label("created",message.getCreated().toString()));
                listItem.add(new Label("userChatSession.user.displayName",message.getUserChatSession().getUser().getDisplayName()));
                listItem.add(new Label("message",message.getMessage()));

            }
        });

        CompoundPropertyModel compoundComposingMessage = new CompoundPropertyModel(composingMessage);
        chatRoomForm.add(new TextField<String>("message",compoundComposingMessage));
	chatRoomForm.add(new AjaxButton("send") {
			protected void onSubmit(AjaxRequestTarget target, Form form) {
                            chatManagerVC.sendNewMessage();
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
