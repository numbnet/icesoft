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

import com.icesoft.faces.context.DisposableBean;
import com.icefaces.project.memory.game.GameInstance;
import com.icefaces.project.memory.game.GameManager;

public class UserSession extends UserModel implements DisposableBean {
	private GameManager gameManager;
	private GameInstance currentGame;
	private UserRenderer renderer = new UserRenderer();
	private boolean isShaking = false;
	
	public UserSession() {
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}
	
	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public GameInstance getCurrentGame() {
		return currentGame;
	}

	public void setCurrentGame(GameInstance currentGame) {
		this.currentGame = currentGame;
	}
	
	public UserRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(UserRenderer renderer) {
		this.renderer = renderer;
	}
	
	public boolean getIsShaking() {
		return isShaking;
	}

	public void setIsShaking(boolean isShaking) {
		this.isShaking = isShaking;
	}
	
	public boolean getIsInGame() {
		return currentGame != null;
	}
	
	public static UserSession generateComputerSession(GameManager gameManager, GameInstance currentGame) {
		UserSession computer = new UserSession();
		
		computer.setName("Skynet (Computer)");
		computer.setIsComputer(true);
		computer.setGameManager(gameManager);
		computer.setCurrentGame(currentGame);
		computer.setRenderer(new UserRenderer());
		
		return computer;
	}
	
	/**
	 * Method called when the bean is about to be destroyed
	 * This would normally happen when a user closes their browser and we want to do something
	 *  when their session times out
	 * In this case we'll try to make them leave their current game, so that we don't have
	 *  a bunch of idle users populating games and confusing people
	 */
	public void dispose() {
		if ((gameManager != null) &&
		    (currentGame != null) &&
		   (name != null)) {
			gameManager.leaveGame(this, currentGame);
		}
	}
}
