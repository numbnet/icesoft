package org.icepush.samples.icechat.gwt.client;

import org.icepush.samples.icechat.gwt.client.screens.ChatRoomListPanel;
import org.icepush.samples.icechat.gwt.client.screens.ChatScreen;
import org.icepush.samples.icechat.gwt.client.screens.CredentialsPanel;
import org.icepush.samples.icechat.gwt.client.screens.MainPanelRegistry;
import org.icepush.samples.icechat.gwt.client.screens.StartScreen;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IceChatModule implements EntryPoint {

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


        mainPanel.add(new ChatScreen(), DockPanel.EAST);
        mainPanel.add(new ChatRoomListPanel(), DockPanel.WEST);

        //setup table widths...
        mainPanel.setCellWidth(MainPanelRegistry.getInstance().getChatRoomListPanel(), "250px");
        mainPanel.setSpacing(30);

        RootPanel.get("appPanel").add(mainPanel);

        StartScreen startPopup = new StartScreen();
        startPopup.show();

    }
}
