package com.icefaces.project.memory.game;

import java.util.List;
import java.util.Vector;

import com.icesoft.faces.async.render.SessionRenderer;
import com.icefaces.project.memory.exception.FailedJoinException;
import com.icefaces.project.memory.game.card.GameCardSetManager;
import com.icefaces.project.memory.user.UserModel;
import com.icefaces.project.memory.util.ValidatorUtil;

public class GameManager {
	public static final int DEFAULT_SIZE = 4;
	public static final int DEFAULT_MAX_USERS = 2;
	public static final int DEFAULT_MAX_FLIP = 2;
	public static final long DEFAULT_REFLIP_DELAY = 1200l;
	
	public static final String RENDER_GROUP_LOBBY = "render-lobby";
	
	private GameCardSetManager cardSetManager;
	private List<GameInstance> gameList = new Vector<GameInstance>(0);
	
	public GameManager() {
	}
	
	private void renderLobby() {
		SessionRenderer.render(RENDER_GROUP_LOBBY);
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
	
	public boolean createGame(UserModel user, GameInstance newGame) {
		// Add the creating user to their own game
		newGame.addUser(user);
		
		// Add the new game to our game list
		gameList.add(newGame);
		
		// Render everyone in the lobby so they see the change
		renderLobby();
		
		return true;
	}
	
	public boolean joinGame(UserModel user, GameInstance game, String password) throws FailedJoinException {
		// Ensure the user hasn't already joined the game
		if (!game.getHasUser(user)) {
			// Ensure there is a slot available
			if (game.getHasSpace()) {
				if (game.getHasPassword()) {
					if (!password.equals(game.getPassword())) {
						throw new FailedJoinException("Invalid password entered for game '" + game.getName() + "'.");
					}
				}
				
				// All looks good, so add the user
				game.addUser(user);
				
				// Determine if the game should be started
				if (game.getIsFull()) {
					game.startGame();
				}
				
				// Render everyone in the lobby so they see the change
				renderLobby();
				
				return true;
			}
			else {
				throw new FailedJoinException("Game '" + game.getName() + "' is full.");
			}
		}
		else {
			throw new FailedJoinException("Game '" + game.getName() + "' already has a user with your name.");
		}
	}
	
	public void leaveGame(UserModel user, GameInstance game) {
		if (game.getHasUser(user)) {
			game.stopGame();
			game.removeUser(user); // This will cause a render
			
			// Remove the game from the list if no one is in it
			// This will stop empty games from lingering around
			if (!game.getHasUsers()) {
				game.shutdownGame();
				
				gameList.remove(game);
				
				// Dereference the game instance
				game = null;
			}
			
			renderLobby();
		}
	}
}
