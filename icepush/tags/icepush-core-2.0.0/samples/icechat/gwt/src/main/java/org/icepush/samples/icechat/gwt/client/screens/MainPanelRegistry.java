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

/**
 *
 * @author icesoft
 */
public class MainPanelRegistry {

    private CredentialsPanel credentialsPanel;
    private ChatRoomListPanel chatRoomListPanel;
    private ChatScreen chatScreen;

    public CredentialsPanel getCredentialsPanel() {
        return credentialsPanel;
    }

    public void registerCredentialsPanel(CredentialsPanel credentialsPanel) {
        this.credentialsPanel = credentialsPanel;
    }
    
    public void registerChatRoomListPanel(ChatRoomListPanel panel){
    	this.chatRoomListPanel = panel;
    }
    
    public ChatRoomListPanel getChatRoomListPanel(){
    	return this.chatRoomListPanel;
    }

    public ChatScreen getChatScreen() {
        return chatScreen;
    }

    public void registerChatScreen(ChatScreen chatScreen) {
        this.chatScreen = chatScreen;
    }

    
    
    
	/** private constructor for this singleton registry*/
    
    private MainPanelRegistry(){

    }
    /* singleton fields and methods */

    private static MainPanelRegistry instance;
    
    public static MainPanelRegistry getInstance(){
        if(instance == null){
            instance = new MainPanelRegistry();
        }

        return instance;
    }
}
