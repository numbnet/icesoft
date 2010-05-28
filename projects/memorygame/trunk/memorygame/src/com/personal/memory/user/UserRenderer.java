package com.personal.memory.user;

import com.icesoft.faces.async.render.SessionRenderer;
import com.personal.memory.game.GameManager;

public class UserRenderer {
	private boolean inLobby = false;
	private boolean inGame = false;
	
	public UserRenderer() {
	}

	public boolean isInLobby() {
		return inLobby;
	}

	public void setInLobby(boolean inLobby) {
		this.inLobby = inLobby;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
	
	public void joinLobby() {
		SessionRenderer.addCurrentSession(GameManager.RENDER_GROUP_LOBBY);
		
		setInLobby(true);
	}
	
	public void leaveLobby() {
		SessionRenderer.removeCurrentSession(GameManager.RENDER_GROUP_LOBBY);
		
		setInLobby(false);
	}
	
	public void joinGame(String name) {
		SessionRenderer.addCurrentSession(name);
		
		setInGame(true);
	}
	
	public void leaveGame(String name) {
		SessionRenderer.removeCurrentSession(name);
		
		setInGame(false);
	}
	
	public String getLobbyCall() {
		if (!inLobby) {
			joinLobby();
		}
		
		return null;
	}
}
