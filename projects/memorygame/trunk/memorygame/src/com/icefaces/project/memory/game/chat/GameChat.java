package com.icefaces.project.memory.game.chat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

import com.icesoft.faces.async.render.SessionRenderer;
import com.icefaces.project.memory.bean.color.ColorBean;

public class GameChat {
	public static final String SYSTEM_NAME = "System";
	public static final String DEFAULT_COLOR = ColorBean.DEFAULT_COLOR_HEX;
	public static final DateFormat TIMESTAMP = new SimpleDateFormat("h:mm:ssa");
	public static final int TABLE_SIZE = 15;
	
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
		messages.add(0, toAdd);
		
		// Trim any extra messages to keep the size of the chat log small
		if (messages.size() > TABLE_SIZE) {
			messages.remove(TABLE_SIZE);
		}
		
		if ((renderAfter) && (gameName != null)) {
			SessionRenderer.render(gameName);
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
	
	public void addWinnerMessage(String message) {
		addMessage(new GameChatMessage(SYSTEM_NAME, message), false);
	}
}
