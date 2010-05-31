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

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.List;

import com.icesoft.faces.async.render.SessionRenderer;
import com.icefaces.project.memory.comparator.UserByScoreComparator;
import com.icefaces.project.memory.exception.ThreadRunningException;
import com.icefaces.project.memory.game.card.GameCard;
import com.icefaces.project.memory.game.card.GameCardSet;
import com.icefaces.project.memory.game.chat.GameChat;
import com.icefaces.project.memory.user.UserModel;
import com.icefaces.project.memory.user.UserSession;
import com.icefaces.project.memory.util.ValidatorUtil;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Primary game class representing an actual game
 * This has all the attributes set when the game was created, like the name, max players, etc.
 * We'll also track who is currently in the game, and maintain references to our supporting
 *  classes like chat and board management
 */
public class GameInstance {
	protected String name;
	protected String password;
	protected List<UserModel> users;
	protected List<UserModel> usersByScore; // list of players sorted by highest score
	protected int maxUsers = GameManager.DEFAULT_MAX_USERS;
	protected long reflipDelay = GameManager.DEFAULT_REFLIP_DELAY;
	protected boolean isStarted = false;
	private String winnerMessage;
	private GameBoard board;
	private GameChat chat;
	private GameTurns turns;
	private Random randomizer;
	
	public GameInstance() {
		this(null, null, GameManager.DEFAULT_SIZE,
			 new GameCardSet(null, 0),
			 new ArrayList<UserModel>(0),
			 GameManager.DEFAULT_MAX_USERS,
			 GameManager.DEFAULT_REFLIP_DELAY,
			 false);
	}
	
	public GameInstance(GameInstance clone) {
		this(clone.getName(),
		     clone.getPassword(),
		     clone.getBoard().getSize(),
		     clone.getBoard().getCardSet(),
		     clone.getUsers(),
		     clone.getMaxUsers(),
		     clone.getReflipDelay(),
		     clone.getIsStarted());
	}
	
	public GameInstance(String name, String password, int size,
			GameCardSet cardSet, List<UserModel> users,
			int maxUsers, long reflipDelay, boolean isStarted) {
		this.name = name;
		this.password = password;
		this.users = users;
		this.maxUsers = maxUsers;
		this.reflipDelay = reflipDelay;
		this.isStarted = isStarted;
		
		board = new GameBoard(size, cardSet);
		chat = new GameChat(this.name);
		turns = new GameTurns(this,
							  this.reflipDelay,
							  this.users);
		
		init();
	}
	
	protected void init() {
		randomizer = new Random(System.currentTimeMillis());
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<UserModel> getUsers() {
		return users;
	}
	public void setUsers(List<UserModel> users) {
		this.users = users;
	}
	public int getMaxUsers() {
		return maxUsers;
	}
	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}
	public long getReflipDelay() {
		return reflipDelay;
	}
	public void setReflipDelay(long reflipDelay) {
		this.reflipDelay = reflipDelay;
	}
	public boolean getIsStarted() {
		return isStarted;
	}
	public void setIsStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}
	public boolean getHasWinner() {
		return getWinnerMessage() != null;
	}
	public String getWinnerMessage() {
		return winnerMessage;
	}
	public void setWinnerMessage(String winnerMessage) {
		this.winnerMessage = winnerMessage;
	}
	public GameBoard getBoard() {
		return board;
	}
	public void setBoard(GameBoard board) {
		this.board = board;
	}
	public GameChat getChat() {
		return chat;
	}
	public void setChat(GameChat chat) {
		this.chat = chat;
	}
	public GameTurns getTurns() {
		return turns;
	}
	public void setTurns(GameTurns turns) {
		this.turns = turns;
	}
	public List<UserModel> getUsersByScore() {
		// Don't bother comparing unless there is more than 1 user
		if (getUsers().size() <= 1) {
			return users;
		}
		
		if ((usersByScore == null) ||
			(usersByScore.size() != getUsers().size())) {
			usersByScore = new Vector<UserModel>(users.size());
			usersByScore.addAll(users);
 
			Collections.sort(usersByScore, new UserByScoreComparator());
		}
		
		return usersByScore;
	}
	public void setUsersByScore(List<UserModel> usersByScore) {
		this.usersByScore = usersByScore;
	}

	public void renderGame() {
		if (name != null) {
			SessionRenderer.render(name);
		}
	}
	
	public boolean getHasPassword() {
		return ValidatorUtil.isValidString(password);
	}
	
	public boolean getHasUsers() {
		return getUserCount() > 0;
	}
	
	public boolean getHasNonComputerUsers() {
		if (getUserCount() > 0) {
			for (UserModel currentUser : users) {
				if (!currentUser.getIsComputer()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean getHasUser(UserModel user) {
		if (getHasUsers()) {
			for (UserModel loopUser : users) {
				if (loopUser == user) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean getIsFull() {
		return getUserCount() >= getMaxUsers();
	}
	
	public boolean getHasSpace() {
		return (getUserCount()+1) <= getMaxUsers();
	}
	
	public int getEmptySpace() {
		return getMaxUsers() - getUserCount();
	}
	
	public int getUserCount() {
		if (users == null) {
			return 0;
		}
		
		return users.size();
	}
	
	public void resetScores() {
		setAllScores(0);
	}
	
	public void setAllScores(int newScore) {
		if (ValidatorUtil.isValidList(users)) {
			for (UserModel loopUser : users) {
				loopUser.setScore(newScore);
			}
		}
	}
	
	public void requestShake(String fromWho) {
		for (UserModel loopUser : users) {
			((UserSession)loopUser).setIsShaking(true);
		}
		
		// Notify the other users who the shaker was
		chat.addShakeMessage(fromWho);
		
		// Update the game to apply the shake effect
		renderGame();
	}
	
	public void addUser(UserModel user) {
		users.add(user);
		
		// Put in a notification to the chat log
		chat.addEnterMessage(user.getName());
		
		// Update the game to let everyone know a new user joined
		renderGame();
	}
	
	public void removeUser(UserModel user) {
		users.remove(user);
		
		// Put in a notification to the chat log
		chat.addExitMessage(user.getName());
		
		// Update the game to let everyone know a user left
		renderGame();
	}
	
	/**
	 * Method called to perform a turn
	 * Basically we'll try to flip the passed card, and if we have a match
	 *  the proper score will be incremented
	 *  
	 * @return true if the turn resulted in a score
	 */
	public boolean performTurn(GameCard toFlip) {
		try{
			UserModel matcher = turns.handleTurn(toFlip);
			
			if (matcher != null) {
				matcher.incrementScore();
				
				// Reset the cached user score list so it gets resorted
				setUsersByScore(null);
				
				// Check if this last match won the round
				if (board.isGameDone()) {
					someoneWon();
				}
				
				renderGame();
				
				return true;
			}
			
			renderGame();
		}catch (ThreadRunningException ignored) {
			// If we get this exception it means the user was trying to
			//  click around while we waited to reflip our cards
			// So basically ignore them
		}
		
		return false;
	}
	
	/**
	 * Method called to perform a computer turn in place of a normal user turn
	 * This will spawn a new thread
	 * First we'll delay a bit to represent the computer "thinking"
	 * Then we'll ensure the reflipping thread isn't still running (ie: the cards
	 *  are in a clickable state for the computer)
	 * Then, if the game is still going, we'll try to perform our moves
	 *  by randomly flipping cards
	 * If our flips result in a match we'll restart this method to fulfill our bonus turn
	 */
	public void performComputerTurn() {
		// Only bother to act if the game is actually running
		if (isStarted) {
			turns.getThreadPool().execute(new Runnable() {
				public void run() {
					// First we'll want to sleep for a bit, to give the illusion of being human
					try {
						Thread.sleep(GameManager.BOT_THINK_DELAY_BASE +
									 	randomizer.nextInt(GameManager.BOT_THINK_DELAY_VARIATION));
					}catch (InterruptedException interrupted) {
						return;
					}
					
					// Then we'll want to make sure the cards have reflipped and we
					//  are in a game state that is ready to accept new moves
					// We'll check the thread status 5 times, and break and continue
					//  with the turn as soon as the thread is done
					for (int i = 0; i < 5; i++) {
						if (turns.getThreadRunning()) {
							try {
								// Don't sleep for the full reflipDelay each time
								// This is because if we miss the thread stopping by a few milliseconds we
								//  don't want to have to wait through the entire reflipDelay again
								Thread.sleep(reflipDelay/3);
							}catch (InterruptedException ignored) { }
						}
						else {
							break;
						}
					}
					
					// Double check the started status, since with all these threads
					//  it could have been reset after our sleep calls
					if (isStarted) {
						boolean scored = false;
						GameCard toFlip = null;
						// Perform the computer moves
						// The cards to flip are chosen totally at random, so the computer
						//  is not exactly that smart
						for (int i = 0; i < GameManager.DEFAULT_MAX_FLIP; i++) {
							// Determine what to flip, then flip it and track whether we had a match
							toFlip = board.getRandomUnflippedCard();
							if (toFlip != null) {
								scored = performTurn(toFlip);
							}
							else {
								scored = false;
							}
							
							// Delay a bit between each move
							try {
								Thread.sleep(GameManager.BOT_MOVE_DELAY);
							}catch (InterruptedException interrupted) {
								return;
							}
						}
						
						// If we scored a point we need to act again, so restart our computer turn
						if ((scored) && (isStarted)) {
							performComputerTurn();
						}
					}
				}
			});
		}
	}
	
	/**
	 * Method called when someone has won the game
	 * We'll display the winning screen as necessary, before resetting the game
	 *  to a new round
	 */
	private void someoneWon() {
		// Grab the highest scoring user
		// This can cause some issue for ties, since whoever scored last becomes the winner
		UserModel winningUser = getUsersByScore().get(0);
		
		String message = winningUser.getName() + " won the game with a score of " +
		                 winningUser.getScore() + ".";
		
		chat.addWinnerMessage(message);
		setWinnerMessage(message);
		
		restartGame();
		setIsStarted(false);
		
		// Start a new thread from the existing pool to close the winning screen begin the game
		turns.getThreadPool().execute(new Runnable() {
			public void run() {
				try {
					Thread.sleep(7500l);
				}catch (InterruptedException ignored) { }
				
				setWinnerMessage(null);
				
				if (getIsFull()) {
					setIsStarted(true);
				}
				
				renderGame();
			}
		});
	}
	
	public void restartGame() {
		stopGame();
		startGame(false);
	}
	
	public void startGame() {
		startGame(true);
	}
	
	public void startGame(boolean renderAfter) {
		setIsStarted(true);
		
		board.generateData();
		turns.determineTurns();
		
		if (renderAfter) {
			renderGame();
		}
	}
	
	public void stopGame() {
		setIsStarted(false);
		
		this.resetScores();
		board.resetBoard();
		turns.resetTurns();
	}
	
	public void shutdownGame() {
		stopGame();
		
		turns.stopThreadPool();
	}
}
