/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.gwt.client;

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
