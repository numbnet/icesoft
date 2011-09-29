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
package com.icefaces.project.memory.game;

import java.util.List;
import java.util.Vector;

import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icefaces.application.PushRenderer;

import com.icefaces.project.memory.bean.RedirectBean;
import com.icefaces.project.memory.bot.BotDifficulty;
import com.icefaces.project.memory.bot.BotDifficultyManager;
import com.icefaces.project.memory.bot.BotManager;
import com.icefaces.project.memory.exception.FailedJoinException;
import com.icefaces.project.memory.game.card.GameCardSetManager;
import com.icefaces.project.memory.user.UserModel;
import com.icefaces.project.memory.util.ValidatorUtil;

/**
 * Game class that maintains a list of all available GameInstances
 * This also handles the management of card sets by delegating it to a GameCardSetManager
 */
@ManagedBean(name="gameManager")
@ApplicationScoped
public class GameManager {
	public static final int DEFAULT_SIZE = 4;
	public static final int DEFAULT_MAX_USERS = 2;
	public static final int DEFAULT_MAX_FLIP = 2;
	public static final long DEFAULT_REFLIP_DELAY = 1200l;
	public static final long WIN_SCREEN_DISPLAY_TIME = 7500l;
	public static final String RENDER_GROUP_LOBBY = "render-lobby";
	public static final String TEST_MODE_GAME_NAME = "Activate Test Mode";
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private GameCardSetManager cardSetManager;
	private List<GameInstance> gameList = new Vector<GameInstance>(0);
	
	public GameManager() {
	}
	
	private void renderLobby() {
		PushRenderer.render(RENDER_GROUP_LOBBY);
	}
	
	public GameCardSetManager getCardSetManager() {
		if (cardSetManager == null) {
			cardSetManager = new GameCardSetManager();
		}
		
		return cardSetManager;
	}

	public void setCardSetManager(GameCardSetManager cardSetManager) {
		this.cardSetManager = cardSetManager;
	}

	public List<GameInstance> getGameList() {
		return gameList;
	}

	public void setGameList(List<GameInstance> gameList) {
		this.gameList = gameList;
	}
	
	public boolean getHasGame(String name) {
		for (GameInstance loopGame : gameList) {
			if (name.equalsIgnoreCase(loopGame.getName())) {
				return true;
			}
		}
		
		return false;
	}
	
	public GameInstance getGameAt(int index) {
		return gameList.get(index);
	}
	
	public boolean getHasGameList() {
		return ValidatorUtil.isValidList(gameList);
	}
	
	public int getGameCount() {
		if (getHasGameList()) {
			return gameList.size();
		}
		
		return 0;
	}
	
	public static boolean checkTestMode(String name) {
		return TEST_MODE_GAME_NAME.equalsIgnoreCase(name);
	}
	
	public boolean createGame(UserModel user, GameInstance newGame) {
		if (log.isInfoEnabled()) {
			log.info("User " + user.getName() + " created a new " + newGame.getMaxUsers() +
					 " player game called '" + newGame.getName() + "' with the card set " +
					 newGame.getBoard().getCardSet().getName() + " and back image " + newGame.getBoard().getCardSet().getBackImage() + ".");
		}
		
		// Add the creating user to their own game
		newGame.addUser(user);
		
		// Add the new game to our game list
		gameList.add(newGame);
		
		// Render everyone in the lobby so they see the change
		renderLobby();
		
		return true;
	}
	
	public boolean joinGame(UserModel user, GameInstance game, String password) throws FailedJoinException {
		if (log.isInfoEnabled()) {
			log.info("User " + user.getName() + " is attempting to join the game called '" + game.getName() + "'.");
		}
		
		// If the user has already joined the game then just redirect them to the board
		// This can happen if they manually go back to the lobby page and then try to
		//  rejoin the same game
		if (game.getHasUser(user)) {
			RedirectBean.redirectToBoard();
			
			return true;
		}
		// Otherwise proceed with normal game joining
		else {
			// Ensure there is a slot available
			if (game.getHasSpace()) {
				if (game.getHasPassword()) {
					if (!password.equals(game.getPassword())) {
						throw new FailedJoinException("Invalid password entered for game '" + game.getName() + "'.");
					}
				}
				
				// All looks good, so add the user
				game.addUser(user);
				
				// Attempt to start the game
				// This will naturally check whether it is full
				game.startGame();
				
				// Render everyone in the lobby so they see the change
				renderLobby();
				
				return true;
			}
			else {
				throw new FailedJoinException("Game '" + game.getName() + "' is full.");
			}
		}
	}
	
	/**
	 * Method to fill the remaining open slots of a game with computer controlled sessions
	 */
	public boolean addComputersToGame(BotDifficulty difficulty, GameInstance game) {
		if (difficulty == null) {
			difficulty = BotDifficultyManager.getDefaultDifficulty();
		}
		
		if (log.isInfoEnabled()) {
			log.info("Attempting to add " + difficulty.getName() + " computers to the game called '" + game.getName() + "'.");
		}
		
		if (game.getHasSpace()) {
			// Generate a bunch of new computer controlled sessions and fill the game with them
			UserModel[] computers = BotManager.generateComputers(difficulty, game.getEmptySpace(),
															     this, game);
			for (UserModel computer : computers) {
				game.addUser(computer);
			}
			
			// Start the game now that we are full of computer players
			game.startGame();
			
			// Render everyone in the lobby so they see the change
			renderLobby();
			
			return true;
		}
		
		return false;
	}
	
	public void leaveGame(UserModel user, GameInstance game) {
		if (log.isInfoEnabled()) {
			log.info("User " + user.getName() + " is attempting to leave the game called '" + game.getName() + "'.");
		}
		
		if (game.getHasUser(user)) {
			game.stopGame();
			game.removeUser(user); // This will cause a render
			
			// Check if we only have computer players left
			// If we do we'll want to remove them as they can't play amongst themselves
			// This will then cause the game to be shutdown because there are no users left
			if (!game.getHasNonComputerUsers()) {
				game.getUsers().clear();
			}
			
			// Remove the game from the list if no one is in it
			// This will stop empty games from lingering around
			if (!game.getHasUsers()) {
				if (log.isInfoEnabled()) {
					log.info("Shutting down empty game called '" + game.getName() + "'.");
				}
				
				game.shutdownGame();
				
				gameList.remove(game);
				
				// Dereference the game instance
				game = null;
			}
			
			renderLobby();
		}
	}

	/**
	 * Method to perform some cleanup when this bean is going to get disposed
	 * In this case we want to cancel the TimerTask that checks for Card Set related
	 *  filesystem changes every so often
	 */
	@PreDestroy
	public void dispose() {
		if (getCardSetManager() != null) {
			cardSetManager.cancelCheckSetChanges();
		}
	}
}
