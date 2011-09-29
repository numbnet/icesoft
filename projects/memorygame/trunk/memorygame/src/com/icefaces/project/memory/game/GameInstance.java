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

import org.icefaces.application.PushRenderer;

import com.icefaces.project.memory.bot.BotChatManager;
import com.icefaces.project.memory.bot.BotManager;
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
	protected volatile boolean isStarted = false; // can be toggled from the main thread or the bot player thread or the winning screen thread
	protected String winnerMessage;
	private GameBoard board;
	private GameChat chat;
	private GameTurns turns;
	private boolean testMode = false;
	
	public GameInstance() {
		this(null, null, GameManager.DEFAULT_SIZE,
			 new GameCardSet(null, 0),
			 new Vector<UserModel>(0),
			 GameManager.DEFAULT_MAX_USERS,
			 GameManager.DEFAULT_REFLIP_DELAY,
			 false, false);
	}
	
	public GameInstance(GameInstance clone) {
		this(clone.getName(),
		     clone.getPassword(),
		     clone.getBoard().getSize(),
		     clone.getBoard().getCardSet(),
		     clone.getUsers(),
		     clone.getMaxUsers(),
		     clone.getReflipDelay(),
		     clone.getIsStarted(),
		     clone.getTestMode());
	}
	
	public GameInstance(String name, String password, int size,
			GameCardSet cardSet, List<UserModel> users,
			int maxUsers, long reflipDelay, boolean isStarted,
			boolean testMode) {
		this.name = name;
		this.password = password;
		this.users = users;
		this.maxUsers = maxUsers;
		this.reflipDelay = reflipDelay;
		this.isStarted = isStarted;
		
		this.board = new GameBoard(size, cardSet);
		this.chat = new GameChat(this.name);
		this.turns = new GameTurns(this,
			   				       this.reflipDelay,
							       this.users);
		
		// Determine if we're in test mode, and notify the user accordingly
		this.testMode = GameManager.checkTestMode(this.name);
		if (this.testMode) {
			this.chat.addTestModeMessage();
		}
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
	public boolean getTestMode() {
		return testMode;
	}
	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
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
	
	public void recacheUsersByScore() {
		setUsersByScore(null);
	}

	public void renderGame() {
		if (name != null) {
			PushRenderer.render(name);
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
			for (UserModel loopUser : users) {
				if (!loopUser.getIsComputer()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean getHasComputerUsers() {
		if (getUserCount() > 0) {
			for (UserModel loopUser : users) {
				if (loopUser.getIsComputer()) {
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
		
		// Greet the other players if we're a computer
		if (user.getIsComputer()) {
			BotChatManager.botChatJoinGreeting(user);
		}
		
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
			// Tell the BotManager to remember what card was flipped
			// This will help the computer players on their future turns
			// Note this won't have any effect if no computer users are present
			BotManager.memorizeCard(users, toFlip);
			
			// Check whether we scored a point
			UserModel matcher = turns.handleTurn(toFlip);
			
			if (matcher != null) {
				matcher.incrementScore();
				
				// Reset the cached user score list so it gets resorted
				recacheUsersByScore();
				
				// Check if this last match won the round
				if (board.isGameDone()) {
					someoneWon();
				}
				else {
					// If the user was not a computer then tell the bots to congratulate the player
					if (!matcher.getIsComputer()) {
						BotChatManager.botChatCongratulateScore(users, matcher.getName());
					}
					// Otherwise have the bots taunt the other players
					else {
						BotChatManager.botChatTauntOnScore(matcher);
					}
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
	 * Method called to perform a computer controlled turn in place of a normal
	 *  user turn.
	 * This will simply delegate the request to the BotManager
	 */
	public void performComputerTurn() {
		BotManager.performComputerTurn(this);
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
		
		// Set these messages as needed and then stop the game as it's over
		chat.addWinnerMessage(message);
		setWinnerMessage(message);
		
		// Make the bots congratulate the winner
		BotChatManager.botChatCongratulateWin(users, winningUser.getName(), winningUser.getIsComputer());
		
		// Stop the game so we have a chance to display the win screen
		setIsStarted(false);
		
		// Start a new thread from the existing pool to close the winning screen and restart the game
		turns.getThreadPool().execute(new Runnable() {
			public void run() {
				try {
					Thread.sleep(GameManager.WIN_SCREEN_DISPLAY_TIME);
				}catch (InterruptedException ignored) { }
				
				// Clear the victory message after our sleep
				setWinnerMessage(null);
				
				// Check whether the game has been started while we slept
				// If it has we don't need to restart anything, otherwise we
				//  should perform that task
				if (!isStarted) {
					// Restart the game
					stopGame();
					startGame();
				}
			}
		});
	}
	
	public void startGame() {
		if (getIsFull()) {
			setWinnerMessage(null);
			setIsStarted(true);
			
			// Reset the scoreboard
			recacheUsersByScore();
			
			// Clear any bots of memorized cards
			BotManager.clearMemorizedCards(users);
			
			// Generate a new board
			board.generateData(testMode);
			
			// Determine the first turn
			turns.determineTurns(testMode);
		}
		
		renderGame();
	}
	
	public void stopGame() {
		setIsStarted(false);
		
		this.resetScores();
		board.resetBoard();
		turns.resetTurns();
	}
	
	public void shutdownGame() {
		setWinnerMessage(null);
		stopGame();
		
		turns.stopThreadPool();
	}
}
