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
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/
package com.icefaces.project.memory.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import com.icesoft.faces.context.effects.Highlight;
import com.icefaces.project.memory.game.chat.GameChat;
import com.icefaces.project.memory.game.chat.GameChatMessage;
import com.icefaces.project.memory.util.ValidatorUtil;

/**
 * Page bean for the functionality of chatting, both sending and displaying messages
 */
@ManagedBean(name="chatBean")
@ViewScoped
public class ChatBean {
	@ManagedProperty(value = "#{userSession.currentGame.chat}")
	private GameChat chatRoom;
	
	@ManagedProperty(value = "#{userSession.name}")
	private String sender;
	private String text;
	private String fontColor = GameChat.DEFAULT_COLOR;
	private String tableRows = String.valueOf(GameChat.TABLE_SIZE);
	private Highlight effect = new Highlight(GameChat.DEFAULT_COLOR);
	
	public ChatBean() {
		init();
	}
	
	protected void init() {
		effect.setDuration(0.5f);
		effect.setFired(true);
	}
	
	public GameChat getChatRoom() {
		return chatRoom;
	}

	public void setChatRoom(GameChat chatRoom) {
		this.chatRoom = chatRoom;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
		
		// Reset the highlight color to match the font
		effect.setStartColor(this.fontColor);
	}
	
	public String getTableRows() {
		return tableRows;
	}

	public void setTableRows(String tableRows) {
		this.tableRows = tableRows;
	}

	public Highlight getEffect() {
		return effect;
	}

	public void setEffect(Highlight effect) {
		this.effect = effect;
	}
	
	public void submitMessage(ActionEvent event) {
		if (ValidatorUtil.isValidString(text)) {
			chatRoom.addMessage(new GameChatMessage(sender, text, fontColor));
			
			setText(null);
			
			effect.setFired(false);
		}
	}
}
