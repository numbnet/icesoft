package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.event.dom.client.ClickEvent;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;

import com.google.gwt.user.client.Window;

public class ChatRoomListPanel extends Composite{

	private static final ChatRoomListPanelLayoutBinder binder = GWT.create(ChatRoomListPanelLayoutBinder.class);
	
	@UiField
	VerticalPanel chatRoomList;
	
	public ChatRoomListPanel(){
		MainPanelRegistry.getInstance().registerChatRoomListPanel(this);
		initWidget(binder.createAndBindUi(this));
	}
	
	@UiHandler("createChatRoomButton")
	public void doCreateChatRoom(ClickEvent ev){
		 
    	ChatServiceAsync service = (ChatServiceAsync) GWT.create(ChatService.class);
    
    	AsyncCallback<Void> callback = new AsyncCallback<Void>(){
			public void onSuccess(Void result){
				
			}	
						
			public void onFailure(Throwable t){
				Window.alert("Failed to create a new ChatRoom.  Please wait a minute and try again.");
			}
		};
		service.createChatRoom("chatRoom1",callback);
	
	
	}

    @UiTemplate(value="ChatRoomListPanelLayout.ui.xml")
    public interface ChatRoomListPanelLayoutBinder extends UiBinder<Panel, ChatRoomListPanel>{};

}