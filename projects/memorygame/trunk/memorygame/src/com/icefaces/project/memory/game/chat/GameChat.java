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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icefaces.application.PushRenderer;

import com.icefaces.project.memory.bean.color.ColorBean;

/**
 * Game class to manage chat
 * The main purpose is to store a list of GameChatMessages which can be
 *  accessed by players in the game
 */
public class GameChat {
	public static final String SYSTEM_NAME = "System";
	public static final String DEFAULT_COLOR = ColorBean.DEFAULT_COLOR_HEX;
	public static final DateFormat TIMESTAMP = new SimpleDateFormat("h:mm:ssa");
	public static final int TABLE_SIZE = 15;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private String gameName;
	private List<GameChatMessage> messages;
	
	public GameChat(String gameName) {
		this.gameName = gameName;
		
		init();
	}
	
	protected void init() {
		messages = new Vector<GameChatMessage>(TABLE_SIZE);
	}
	
	public int getTableSize() {
		return TABLE_SIZE;
	}
	
	public List<GameChatMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<GameChatMessage> messages) {
		this.messages = messages;
	}
	
	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	public void addMessage(GameChatMessage toAdd) {
		addMessage(toAdd, true);
	}

	public void addMessage(GameChatMessage toAdd, boolean renderAfter) {
		if (log.isInfoEnabled()) {
			log.info("CHAT> " + toAdd);
		}
		
		messages.add(0, toAdd);
		
		// Trim any extra messages to keep the size of the chat log small
		if (messages.size() > TABLE_SIZE) {
			messages.remove(TABLE_SIZE);
		}
		
		if ((renderAfter) && (gameName != null)) {
			PushRenderer.render(gameName);
		}
	}
	
	public void addEnterMessage(String name) {
		addMessage(new GameChatMessage(SYSTEM_NAME, name + " entered the game."), false);
	}
	
	public void addExitMessage(String name) {
		addMessage(new GameChatMessage(SYSTEM_NAME, name + " exited the game."), false);
	}
	
	public void addShakeMessage(String name) {
		addMessage(new GameChatMessage(SYSTEM_NAME, name + " shook the game."), false);
	}
	
	public void addFirstTurnMessage(String name) {
		addMessage(new GameChatMessage(SYSTEM_NAME, name + " has the first turn."), false);
	}
	
	public void addWinnerMessage(String message) {
		addMessage(new GameChatMessage(SYSTEM_NAME, message), false);
	}
	
	public void addTestModeMessage() {
		addMessage(new GameChatMessage(SYSTEM_NAME, "Activated test mode. Random aspects have been removed."), false);
	}
}
