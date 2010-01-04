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
        mainPanel.setStylePrimaryName("body_container");
        mainPanel.setWidth("100%");
        HorizontalPanel topPanel = new HorizontalPanel();
        CredentialsPanel credPanel = new CredentialsPanel();
        credPanel.setVisible(false);
        topPanel.add(credPanel);


        RootPanel.get("credentials").add(topPanel);

        mainPanel.add(new ChatRoomListPanel(), DockPanel.EAST);

        RootPanel.get("appPanel").add(mainPanel);

        //setup history support.

        History.addHistoryListener(new ApplicationHistoryListener());

        History.fireCurrentHistoryState();


    }
}
