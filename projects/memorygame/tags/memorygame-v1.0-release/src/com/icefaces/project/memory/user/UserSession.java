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
