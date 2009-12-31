package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;


import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IceChatModule implements EntryPoint {
 
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    
    //setup ICEpush engine.
    
   
    
    PushServiceAsync service = (PushServiceAsync) GWT.create(PushService.class);
    
    	AsyncCallback<String> callback = new AsyncCallback<String>(){
			public void onSuccess(String result){
				
					GWTPushContext context = GWTPushContext.getInstance();
    
   
    				context.addPushEventListener(new PushEventListener(){
    					public void onPushEvent(){
    						Window.alert("hurray");
    					}
    				},
    				new String[]{"chatRoom1"},
    				result
    				);
			}	
						
			public void onFailure(Throwable t){
				Window.alert("Failed to create a new ChatRoom.  Please wait a minute and try again.");
			}
		};
		service.getPushId(callback);
	
    
    //initialize the main page view.
    DockPanel mainPanel = new DockPanel();

    HorizontalPanel topPanel = new HorizontalPanel();
    
   
    topPanel.add(new CredentialsPanel());

    mainPanel.add(topPanel, DockPanel.NORTH);

	mainPanel.add(new ChatRoomListPanel(), DockPanel.EAST);

    RootPanel.get().add(mainPanel);
    
    //setup history support.
    
    History.addHistoryListener(new ApplicationHistoryListener());
    
    History.fireCurrentHistoryState();

   
  }
}
