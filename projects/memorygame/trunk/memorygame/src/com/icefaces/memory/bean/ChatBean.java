package com.personal.memory.bean;

import javax.faces.event.ActionEvent;

import com.icesoft.faces.context.effects.Highlight;
import com.personal.memory.game.chat.GameChat;
import com.personal.memory.game.chat.GameChatMessage;
import com.personal.memory.util.ValidatorUtil;

public class ChatBean {
	private GameChat chatRoom;
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
