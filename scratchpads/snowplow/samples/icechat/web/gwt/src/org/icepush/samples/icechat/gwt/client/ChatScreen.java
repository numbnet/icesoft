package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import java.util.List;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomHandle;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomMessage;
import org.icepush.samples.icechat.gwt.client.service.ChatService;
import org.icepush.samples.icechat.gwt.client.service.ChatServiceAsync;

public class ChatScreen extends Composite {

    @UiField
    Label chatRoomNameLabel;
    @UiField
    VerticalPanel roomUserList;
    @UiField
    VerticalPanel messagesList;
    @UiField
    Panel chatUsersPanel;
    @UiField
    ScrollPanel messagesScrollingPanel;
    @UiField
    HorizontalPanel horizontalLayoutPanel;
    @UiField
    HorizontalPanel chatControlsPanel;
    @UiField
    Button newMessageButton;
    @UiField
    TextBox newMessageTextbox;
    private ChatRoomHandle currentChatRoom;



    public ChatScreen() {
        Binder binder = GWT.create(Binder.class);
        this.initWidget(binder.createAndBindUi(this));

        MainPanelRegistry.getInstance().registerChatScreen(this);

        horizontalLayoutPanel.setCellWidth(chatUsersPanel, "250px");
        horizontalLayoutPanel.setCellHeight(chatUsersPanel, "100%");
        chatControlsPanel.setCellWidth(newMessageButton, "60px");

//        newMessageTextbox.addKeyPressHandler(new KeyPressHandler() {
//
//            public void onKeyPress(KeyPressEvent event) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        })

        


    }


    @UiHandler(value={"newMessageTextbox"})
    public void onKeyPressed(KeyPressEvent ev){
        if(ev.getCharCode() == 13)
            sendChatMessage();
    }



    @UiHandler(value = {"newMessageButton"})
    public void onButtonPressed(ClickEvent ev){
        sendChatMessage();
    }

    public void sendChatMessage() {
        ChatServiceAsync service = GWT.create(ChatService.class);
        AsyncCallback<Void> sendMessageCallback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(Void result) {
                ChatScreen.this.newMessageTextbox.setText("");
            }
        };

        service.sendMessage(this.newMessageTextbox.getText(), UserSession.getInstance().getCredentials().getUserName(), currentChatRoom, sendMessageCallback);

    }

    public void show() {
        this.setVisible(true);

    }

    public void hide() {
        this.setVisible(false);
    }

    public void loadNewChatRoom(ChatRoomHandle handle) {
        this.chatRoomNameLabel.setText(handle.getName());
        this.messagesList.clear();

        this.show();

        ChatServiceAsync service = GWT.create(ChatService.class);

        JoinChatRoomCallback joinCallback = new JoinChatRoomCallback(handle);

        service.joinChatRoom(handle, UserSession.getInstance().getCredentials().getUserName(), joinCallback);

        GWTPushContext.getInstance().addPushEventListener(new PushEventListener() {

            public void onPushEvent() {
                //on a push notification, fetch new messages.
                ChatScreen.this.refreshChatRoom(currentChatRoom);
            }
        }, new String[]{handle.getName()});

        GWTPushContext.getInstance().addPushEventListener(new PushEventListener() {

            public void onPushEvent() {
                //on a push notification, fetch new messages.
                ChatScreen.this.refreshParticipants(currentChatRoom);
            }
        }, new String[]{handle.getName() + "-participants"});


    }

    public void refreshChatRoom(ChatRoomHandle handle) {
        ChatServiceAsync service = GWT.create(ChatService.class);

        AsyncCallback<ChatRoomHandle> callback = new AsyncCallback<ChatRoomHandle>() {

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(ChatRoomHandle result) {
                List<ChatRoomMessage> messages = result.getNextMessages();
                for (ChatRoomMessage message : messages) {
                    Label messageText = new Label(message.getText(), true);
                    HorizontalPanel authorPanel = new HorizontalPanel();

                    Label messageAuthor = new Label(message.getNickname());
                    HTMLPanel messageSays = new HTMLPanel("&nbsp; Says:");

                    messageAuthor.getElement().setAttribute("style", "font-weight: bold");

                    authorPanel.add(messageAuthor);
                    authorPanel.add(messageSays);


                    VerticalPanel messagePanel = new VerticalPanel();
                    messagePanel.add(authorPanel);
                    messagePanel.add(messageText);
                    messagePanel.add(new HTMLPanel("<br />"));

                    ChatScreen.this.messagesList.add(messagePanel);
                }
                ChatScreen.this.currentChatRoom = result;
                ChatScreen.this.messagesScrollingPanel.scrollToBottom();
            }
        };

        service.getMessages(handle, callback);


    }

    public interface Binder extends UiBinder<Panel, ChatScreen> {
    }

    public class JoinChatRoomCallback implements AsyncCallback<Void> {

        private ChatRoomHandle handle;

        public JoinChatRoomCallback(ChatRoomHandle handle) {
            this.handle = handle;
        }

        public void onFailure(Throwable caught) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void onSuccess(Void result) {
            ChatScreen.this.refreshParticipants(handle);
            currentChatRoom = handle;
            refreshChatRoom(handle);


        }
    }

    public void refreshParticipants(ChatRoomHandle handle) {
        AsyncCallback<List<String>> getParticipants = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(List<String> result) {
                ChatScreen.this.roomUserList.clear();
                for (String participant : result) {
                    Label particLabel = new Label(participant);
                    ChatScreen.this.roomUserList.add(particLabel);
                }


            }
        };

        ChatServiceAsync service = GWT.create(ChatService.class);
        service.getParticipants(handle, getParticipants);
    }
}
