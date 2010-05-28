package com.icefaces.project.memory.game.chat;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class GameChatMessage {
	private Date timestamp;
	private String sender;
	private String text;
	private String fontColor;
	
	private String cachedFormattedTimestamp = null;
	
	public GameChatMessage(String sender, String text) {
		this(sender, text, GameChat.DEFAULT_COLOR);
	}
	
	public GameChatMessage(String sender, String text, String fontColor) {
		this(Calendar.getInstance(TimeZone.getDefault()).getTime(),
			 sender, text, fontColor);
	}
	
	public GameChatMessage(Date timestamp, String sender, String text, String fontColor) {
		this.timestamp = timestamp;
		this.sender = sender;
		this.text = text;
		this.fontColor = fontColor;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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
	}

	public String getTimestampFormatted() {
		if (cachedFormattedTimestamp == null) {
			cachedFormattedTimestamp = GameChat.TIMESTAMP.format(timestamp);
		}
		
		return cachedFormattedTimestamp;
	}
	
	public String toString() {
		return sender + ": " + text;
	}
}
