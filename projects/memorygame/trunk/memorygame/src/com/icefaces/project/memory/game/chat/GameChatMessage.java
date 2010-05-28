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
