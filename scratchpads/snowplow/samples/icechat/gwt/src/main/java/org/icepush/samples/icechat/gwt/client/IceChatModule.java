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
