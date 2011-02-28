/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icepush.samples.icechat.gwt.client.screens;

import java.util.HashMap;
import java.util.List;

import org.icepush.samples.icechat.gwt.client.Credentials;
import org.icepush.samples.icechat.gwt.client.UserSession;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomDraft;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomHandle;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomMessage;
import org.icepush.samples.icechat.gwt.client.service.ChatService;
import org.icepush.samples.icechat.gwt.client.service.ChatServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.icepush.samples.icechat.gwt.client.GWTPushContext;
import org.icepush.samples.icechat.gwt.client.PushEventListener;

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
    
    private int characterIndex = 0;


    private ChatServiceAsync chatService = GWT.create(ChatService.class);

    private HashMap<String, CharacterPushListener> characterListeners = new HashMap<String,CharacterPushListener>();
    
    PushEventListener currentDraftListener;
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
    	
        if(ev.getNativeKeyCode() == 13){
            sendChatMessage();
            characterIndex = 0;
            return;
        }else{
           characterIndex ++;
        }
        
        if(characterIndex % 2 == 0){ //send every second key
        	sendCharacterNotification();
        }
//        else if(ev.getNativeKeyCode() == 32){
//            sendCharacterNotification();
//        }
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

        chatService.sendMessage(this.newMessageTextbox.getText(), UserSession.getInstance().getCredentials().getSessionToken(), currentChatRoom, sendMessageCallback);
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
        chatService.sendCharacterNotification( UserSession.getInstance().getCredentials().getSessionToken(), currentChatRoom, this.newMessageTextbox.getText(), callback);

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

        JoinChatRoomCallback joinCallback = new JoinChatRoomCallback(handle);

        //first stop listening to push events for the old chat room.
        /*
        if(this.currentMessagesPushListener != null)
            GWTPushContext.getInstance().removePushEventListener(this.currentMessagesPushListener);

        if(this.currentParticipantsPushListener != null)
            GWTPushContext.getInstance().removePushEventListener(this.currentParticipantsPushListener);

        if(this.currentDraftListener != null){
        	GWTPushContext.getInstance().removePushEventListener(this.currentDraftListener);
        }*/
        
        this.currentMessagesPushListener = new PushEventListener() {

            public void onPushEvent() {
                //on a push notification, fetch new messages.
                ChatScreen.this.refreshChatRoom(currentChatRoom);
            }
        };

        GWTPushContext.getInstance().addPushEventListener(this.currentMessagesPushListener, handle.getName().replaceAll(" ", "_"));


        this.currentParticipantsPushListener = new PushEventListener() {

            public void onPushEvent() {
                //update the participants.
                ChatScreen.this.refreshParticipants(currentChatRoom);
            }
        };

        GWTPushContext.getInstance().addPushEventListener(this.currentParticipantsPushListener, handle.getName() + "-participants");

        this.currentDraftListener = new PushEventListener() {
			
			@Override
			public void onPushEvent() {
				ChatScreen.this.updateDraft();
				
			}
		};
		
		GWTPushContext.getInstance().addPushEventListener(this.currentDraftListener, handle.getName() + "-draft");
		
		/*TODO remove this hack.  This is required as a workaround to PUSH-22*/
		
		chatService.endLongPoll(new AsyncCallback<Void>() {


			public void onFailure(Throwable arg0) {
				
				
			}
			
			public void onSuccess(Void arg0) {
				//do nothing...
			}
		});
		/* end of hack */
		
		
		//now joing the chatroom
		
		chatService.joinChatRoom(handle, UserSession.getInstance().getCredentials().getSessionToken(), joinCallback);

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
        AsyncCallback<List<Credentials>> getParticipants = new AsyncCallback<List<Credentials>>() {

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(List<Credentials> result) {
                ChatScreen.this.roomUserList.clear();
                ChatScreen.this.characterListeners.clear();
                
                for (Credentials participant : result) {
                    Label particLabel = new Label(participant.getUserName());
                    HTML awarenessLabel = new HTML();
                    
                    
                    HorizontalPanel scribePanel = new HorizontalPanel();

                    /* @TODO
                     * thi image should be branded to ICEsoft....
                     *
                     */
                    Image img = new Image("./img/pen.gif");
                    img.setVisible(false);
                    
                    scribePanel.add(img);
                    scribePanel.add(awarenessLabel);
                    
                    
                    awarenessLabel.getElement().setAttribute("style", "color: red; font-weight: normal;");
                    ChatScreen.this.roomUserList.add(particLabel);
                    ChatScreen.this.roomUserList.add(scribePanel);
                    if(!ChatScreen.this.characterListeners.containsKey(participant.getSessionToken())){
                        //register a new listener for this participant. (if not already registered and not for the current user).
                        CharacterPushListener pushListener = new CharacterPushListener(awarenessLabel, img);
                        ChatScreen.this.characterListeners.put(participant.getSessionToken(), pushListener);
                        
                    }
                }


            }
        };

        
        chatService.getParticipants(handle, getParticipants);
    }
    
    private void updateDraft(){
    	AsyncCallback<List<ChatRoomDraft>> callback = new AsyncCallback<List<ChatRoomDraft>>() {

			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void onSuccess(List<ChatRoomDraft> drafts) {
				for(ChatRoomDraft draft: drafts)
					ChatScreen.this.characterListeners.get(draft.getUserSessionToken()).updateDraft(draft.getText());
			}
		};
		
		this.chatService.getNextDraftUpdate(UserSession.getInstance().getCredentials().getSessionToken(), currentChatRoom, callback);
    	
    }

    public class CharacterPushListener{
        private HTML awareness;
        private Image awarenessImage;
        public CharacterPushListener(HTML awareness, Image awarenessImage){
        	this.awareness = awareness;
            this.awarenessImage = awarenessImage;

        }

       
        public void updateDraft(String text) {
        	
        	int index = text.lastIndexOf(" ");
        	if(index >= 0){
        		StringBuilder html = new StringBuilder();
        		html.append(text.substring(0, index));
        		html.append(" <b>");
        		html.append(text.substring(index));
        		html.append("</b>");
        		
        		awareness.setHTML(html.toString());
        		
        	}else{
        		awareness.setHTML("<b>" + text + "</b>");
        	}
            
            awarenessImage.setVisible(!text.equals(""));
            
            
//            AsyncCallback<String> callback = new AsyncCallback<String>() {
//
//                public void onFailure(Throwable caught) {
//                    throw new UnsupportedOperationException("Not supported yet.");
//                }
//
//                public void onSuccess(String result) {
//                	
//                   
//                }
//            };
//            Chat
//           ChatScreen.this.chatService.getCurrentCharacters(username, handle, callback);
        }
    }
    
  
}
