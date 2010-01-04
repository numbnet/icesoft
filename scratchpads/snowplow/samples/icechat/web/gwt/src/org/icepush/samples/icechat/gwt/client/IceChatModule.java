package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.RootPanel;


import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IceChatModule implements EntryPoint {

    private PushEventListener listener;
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    
    
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
