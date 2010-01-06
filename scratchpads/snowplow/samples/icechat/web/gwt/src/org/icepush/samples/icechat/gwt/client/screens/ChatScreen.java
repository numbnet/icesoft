package org.icepush.samples.icechat.gwt.client.screens;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.icepush.samples.icechat.gwt.client.GWTPushContext;
import org.icepush.samples.icechat.gwt.client.PushEventListener;
import org.icepush.samples.icechat.gwt.client.UserSession;
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


    private ChatServiceAsync chatService = GWT.create(ChatService.class);

    private HashMap<String, PushEventListener> characterListeners = new HashMap<String,PushEventListener>();

    PushEventListener currentMessagesPushListener;
    PushEventListener currentParticipantsPushListener;

    public ChatScreen() {
        Binder binder = GWT.create(Binder.class);
        this.initWidget(binder.createAndBindUi(this));

        MainPanelRegistry.getInstance().registerChatScreen(this);

        horizontalLayoutPanel.setCellWidth(chatUsersPanel, "250px");
        horizontalLayoutPanel.setCellHeight(chatUsersPanel, "100%");
        chatControlsPanel.setCellWidth(newMessageButton, "60px");


    }


    @UiHandler(value={"newMessageTextbox"})
    public void onKeyUp(KeyUpEvent ev){
        if(ev.getNativeKeyCode() == 13)
            sendChatMessage();
        else if(ev.getNativeKeyCode() == 32){
            sendCharacterNotification();
        }
    }



    @UiHandler(value = {"newMessageButton"})
    public void onButtonPressed(ClickEvent ev){
        sendChatMessage();
    }

    public void sendChatMessage() {
        AsyncCallback<Void> sendMessageCallback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(Void result) {
                
            }
        };

        chatService.sendMessage(this.newMessageTextbox.getText(), UserSession.getInstance().getCredentials().getUserName(), currentChatRoom, sendMessageCallback);
        ChatScreen.this.newMessageTextbox.setText("");
    }

    public void sendCharacterNotification(){
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(Void result) {

            }
        };
        chatService.sendCharacterNotification( UserSession.getInstance().getCredentials().getUserName(), currentChatRoom, this.newMessageTextbox.getText(), callback);

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


        //clear out all previous character listeners.
        Iterator<String> keys = this.characterListeners.keySet().iterator();

        while(keys.hasNext()){
            String key = keys.next();
            Window.alert("Removing: [" + key + "]");
            GWTPushContext.getInstance().removePushEventListener(this.characterListeners.get(key));
        }

        this.characterListeners.clear();

        JoinChatRoomCallback joinCallback = new JoinChatRoomCallback(handle);

        chatService.joinChatRoom(handle, UserSession.getInstance().getCredentials().getUserName(), joinCallback);

        //first stop listening to push events for the old chat room.
        if(this.currentMessagesPushListener != null)
            GWTPushContext.getInstance().removePushEventListener(this.currentMessagesPushListener);

        if(this.currentParticipantsPushListener != null)
            GWTPushContext.getInstance().removePushEventListener(this.currentParticipantsPushListener);

        this.currentMessagesPushListener = new PushEventListener() {

            public void onPushEvent() {
                //on a push notification, fetch new messages.
                ChatScreen.this.refreshChatRoom(currentChatRoom);
            }
        };

        GWTPushContext.getInstance().addPushEventListener(this.currentMessagesPushListener, new String[]{handle.getName()});


        this.currentParticipantsPushListener = new PushEventListener() {

            public void onPushEvent() {
                //update the participants.
                ChatScreen.this.refreshParticipants(currentChatRoom);
            }
        };

        GWTPushContext.getInstance().addPushEventListener(this.currentParticipantsPushListener, new String[]{handle.getName() + "-participants"});


    }

    public void refreshChatRoom(ChatRoomHandle handle) {
      

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
                ChatScreen.this.currentChatRoom.setNextMessages(null);
                ChatScreen.this.messagesScrollingPanel.scrollToBottom();
            }
        };

        chatService.getMessages(handle, callback);


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
                    Label awarenessLabel = new Label();
                    HorizontalPanel scribePanel = new HorizontalPanel();

                    /* @TODO
                     * thi image should be branded to ICEsoft....
                     *
                     */
                    Image img = new Image("./common/img/pen.gif");
                    img.setVisible(false);
                    
                    scribePanel.add(img);
                    scribePanel.add(awarenessLabel);
                    awarenessLabel.getElement().setAttribute("style", "color: red; font-weight: normal;");
                    ChatScreen.this.roomUserList.add(particLabel);
                    ChatScreen.this.roomUserList.add(scribePanel);
                    if(!ChatScreen.this.characterListeners.containsKey(participant + ChatScreen.this.currentChatRoom.getName().replaceAll(" ", "_")) && !participant.equals(UserSession.getInstance().getCredentials().getUserName())){
                        //register a new listener for this participant. (if not already registered and not for the current user).
                        CharacterPushListener pushListener = new CharacterPushListener(participant, currentChatRoom, awarenessLabel, img);

                        GWTPushContext.getInstance().addPushEventListener(pushListener, new String[]{participant + ChatScreen.this.currentChatRoom.getName().replaceAll(" ", "_")});
                        ChatScreen.this.characterListeners.put(participant + ChatScreen.this.currentChatRoom.getName().replaceAll(" ", "_"), pushListener);
                        
                    }
                }


            }
        };

        
        chatService.getParticipants(handle, getParticipants);
    }

    public class CharacterPushListener extends PushEventListener{
        private Label awarenessLabel;
        private Image awarenessImage;
        private String username;
        private ChatRoomHandle handle;
        public CharacterPushListener(String username, ChatRoomHandle handle, Label awarenessLabel, Image awarenessImage){
            this.awarenessLabel = awarenessLabel;
            this.awarenessImage = awarenessImage;
            this.handle = handle;
            this.username = username;
        }

        @Override
        public void onPushEvent() {
            AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void onSuccess(String result) {
                    CharacterPushListener.this.awarenessLabel.setText(result);
                    awarenessImage.setVisible(!result.equals(""));
                   
                }
            };

           ChatScreen.this.chatService.getCurrentCharacters(username, handle, callback);
        }
    }
}
