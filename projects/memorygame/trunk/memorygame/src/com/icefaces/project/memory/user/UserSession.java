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

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.icefaces.project.memory.game.GameInstance;
import com.icefaces.project.memory.game.GameManager;

@ManagedBean(name="userSession")
@SessionScoped
public class UserSession extends UserModel {
	protected Log log = LogFactory.getLog(this.getClass());
	
	@ManagedProperty(value = "#{gameManager}")
	protected GameManager gameManager;
	protected GameInstance currentGame;
	protected UserRenderer renderer = new UserRenderer();
	protected boolean isShaking = false;
	
	public UserSession() {
		if (log.isInfoEnabled()) {
			log.info("New session created with default name '" + name + "'.");
		}
	}
	
	public UserSession(GameManager gameManager, GameInstance currentGame, UserRenderer renderer) {
		super();
		
		this.gameManager = gameManager;
		this.currentGame = currentGame;
		this.renderer = renderer;
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
	
	/**
	 * Method to attempt to leave the current game, if one is valid
	 * 
	 * @return true if a game was left
	 */
	public boolean leaveCurrentGame() {
		if ((gameManager != null) && (currentGame != null)) {
			gameManager.leaveGame(this, currentGame);
			renderer.leaveGame(currentGame.getName());
			setCurrentGame(null);
			
			return true;
		}
		
		return false;
	}
}
