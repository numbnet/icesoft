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
package com.icefaces.project.memory.user;

import org.icefaces.application.PushRenderer;

import com.icefaces.project.memory.game.GameManager;

/**
 * Class used to manage the different PushRenderers and their groups
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
		PushRenderer.addCurrentSession(GameManager.RENDER_GROUP_LOBBY);
		
		setInLobby(true);
	}
	
	public void leaveLobby() {
		PushRenderer.removeCurrentSession(GameManager.RENDER_GROUP_LOBBY);
		
		setInLobby(false);
	}
	
	public void joinGame(String name) {
		PushRenderer.addCurrentSession(name);
		
		setInGame(true);
	}
	
	public void leaveGame(String name) {
		PushRenderer.removeCurrentSession(name);
		
		setInGame(false);
	}
	
	public String getLobbyCall() {
		if (!inLobby) {
			joinLobby();
		}
		
		return null;
	}
}
