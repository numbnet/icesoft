package com.icefaces.project.memory.user;

import com.icesoft.faces.async.render.SessionRenderer;
import com.icefaces.project.memory.game.GameManager;

/**
 * Class used to manage the different SessionRenderers and their groups
 * Basically we'll want to channel all Ajax Push requests through this class,
 *  so that we don't have manual calls in other classes
 * This allows us to abstract how the render is done, and also hide the exacty group
 *  names
 */
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
