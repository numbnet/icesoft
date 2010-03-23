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

import java.util.List;

import org.icepush.samples.icechat.gwt.client.UserSession;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomHandle;
import org.icepush.samples.icechat.gwt.client.service.ChatService;
import org.icepush.samples.icechat.gwt.client.service.ChatServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.icepush.samples.icechat.gwt.client.GWTPushContext;
import org.icepush.samples.icechat.gwt.client.PushEventListener;

public class ChatRoomListPanel extends Composite {

    private static final ChatRoomListPanelLayoutBinder binder = GWT.create(ChatRoomListPanelLayoutBinder.class);
    @UiField
    VerticalPanel chatRoomList;
    TextBox nameText;
    DialogBox namePopup;
    /**
     *
     * a listener for chat room index notifications.
     */
    PushEventListener chatListChangedListener = new PushEventListener() {

        public void onPushEvent() {
            ChatRoomListPanel.this.refreshChatList();
        }
    };



    public ChatRoomListPanel() {
        MainPanelRegistry.getInstance().registerChatRoomListPanel(this);
        initWidget(binder.createAndBindUi(this));

        //fetch latest list of chat rooms from the server.
        this.refreshChatList();
        GWTPushContext.getInstance().addPushEventListener(chatListChangedListener, "chatRoomIndex");


    }

    @UiHandler("createChatRoomButton")
    public void doCreateChatRoom(ClickEvent ev) {
        namePopup = new DialogBox();
        namePopup.setText("Please enter a name:");
        namePopup.setGlassEnabled(true);
        HorizontalPanel rootPanel = new HorizontalPanel();
        Label instructions = new Label("Please enter a name of the ChatRoom:");

        nameText = new TextBox();
        Button createButton = new Button("Create");
        createButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                ChatServiceAsync service = (ChatServiceAsync) GWT.create(ChatService.class);

                AsyncCallback<ChatRoomHandle> callback = new AsyncCallback<ChatRoomHandle>() {

                    public void onSuccess(ChatRoomHandle result) {
                        namePopup.hide();
                        refreshChatList();
                        MainPanelRegistry.getInstance().getChatScreen().loadNewChatRoom(result);
                        
                    }

                    public void onFailure(Throwable t) {
                        Window.alert("Failed to create a new ChatRoom.  Please wait a minute and try again.");
                    }
                };
                service.createChatRoom( nameText.getText(), UserSession.getInstance().getCredentials().getSessionToken(), callback);
            }
        });
        rootPanel.add(instructions);
        rootPanel.add(nameText);
        rootPanel.add(createButton);

        namePopup.add(rootPanel);
        namePopup.center();
        namePopup.show();


    }

    private void refreshChatList() {

        ChatServiceAsync service = GWT.create(ChatService.class);
        AsyncCallback<List<ChatRoomHandle>> callback = new AsyncCallback<List<ChatRoomHandle>>() {

            public void onFailure(Throwable caught) {
                Window.alert("error: " + caught.getMessage());
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(List<ChatRoomHandle> result) {
                ChatRoomListPanel.this.chatRoomList.clear();
                for (ChatRoomHandle handle : result) {
                    Button btn = new Button(handle.getName());
                    btn.addClickHandler(new LoadChatRoomHandler(handle));
                    btn.setSize("100%", "18pt");

                    ChatRoomListPanel.this.chatRoomList.add(btn);
                }
            }
        };

        service.getChatRooms(callback);


    }

    public class LoadChatRoomHandler implements ClickHandler {

        private ChatRoomHandle handle;

        public LoadChatRoomHandler(ChatRoomHandle handle) {
            this.handle = handle;
        }

        public void onClick(ClickEvent event) {
           
            MainPanelRegistry.getInstance().getChatScreen().loadNewChatRoom(LoadChatRoomHandler.this.handle);
        }
    }

    @UiTemplate(value = "ChatRoomListPanelLayout.ui.xml")
    public interface ChatRoomListPanelLayoutBinder extends UiBinder<Panel, ChatRoomListPanel> {
    };
}
