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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.icefaces.application.PushRenderer;

import com.icefaces.project.memory.exception.ThreadRunningException;
import com.icefaces.project.memory.game.card.GameCard;
import com.icefaces.project.memory.user.UserModel;
import com.icefaces.project.memory.util.Randomizer;

/**
 * Game class to handle the flipping of cards, determing of scoring, turn tracking, etc. 
 */
public class GameTurns {
	private GameInstance parentGame;
	private long reflipDelay;
	private List<UserModel> users;
	private UserModel currentTurnUser;
	private List<GameCard> listOfMoves = new ArrayList<GameCard>(GameManager.DEFAULT_MAX_FLIP);
	private int flipCount = 0;
	
	private ExecutorService threadPool; // delicious thread pools
	private boolean flipThreadRunning = false;
	
	public GameTurns(GameInstance parentGame, long reflipDelay, List<UserModel> users) {
		this.parentGame = parentGame;
		this.reflipDelay = reflipDelay;
		this.users = users;
		
		init();
	}
	
	protected void init() {
		threadPool = Executors.newCachedThreadPool();
	}
	
	public UserModel getCurrentTurnUser() {
		return currentTurnUser;
	}
	
	public void setCurrentTurnUser(UserModel currentTurnUser) {
		this.currentTurnUser = currentTurnUser;
	}

	public void changeCurrentTurnUser(UserModel currentTurnUser) {
		// Remove turn status from the previous turn holder
		if (this.currentTurnUser != null) {
			this.currentTurnUser.setIsTurn(false);
		}
		
		// Set the new turn holder status and object
		// If the user was a computer, we'll want to perform a turn for them
		if (currentTurnUser != null) {
			currentTurnUser.setIsTurn(true);
			setCurrentTurnUser(currentTurnUser);
			
			if ((this.currentTurnUser.getIsComputer()) &&
			    (parentGame != null)) {
				parentGame.performComputerTurn();
			}
		}
		else {
			setCurrentTurnUser(null);
		}
	}
	
	public ExecutorService getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ExecutorService threadPool) {
		this.threadPool = threadPool;
	}
	
	public boolean getThreadRunning() {
		return flipThreadRunning;
	}

	/**
	 * Method to determine who the next user should be
	 * If there is no clear next user, we'll randomize it
	 */
	public void nextTurnUser() {
		if (currentTurnUser != null) {
			int newUserIndex = -1;
			UserModel loopUser = null;
			
			// Try to find the index of our current user, since we'll be
			//  using a round-robin to go to the next person
			for (int i = 0; i < users.size(); i++) {
				loopUser = users.get(i);
				
				if (currentTurnUser == loopUser) {
					newUserIndex = (i+1);
					
					break;
				}
			}
			
			// Set the current user based on the decided index
			if (newUserIndex != -1) {
				if (newUserIndex >= users.size()) {
					newUserIndex = 0;
				}
				
				changeCurrentTurnUser(users.get(newUserIndex));
			}
		}
		else {
			determineTurns();
		}
	}
	
	public void determineTurns() {
		determineTurns(false);
	}
	
	public void determineTurns(boolean testMode) {
		int userIndex = Randomizer.getInstance().nextInt(users.size());
		
		// Reset to a hardcoded index to let the first user go if we're in test mode 
		if (testMode) {
			userIndex = 0;
		}
		
		changeCurrentTurnUser(users.get(userIndex));
		
		parentGame.getChat().addFirstTurnMessage(currentTurnUser.getName());
	}
	
	public void stopThreadPool() {
		if ((threadPool != null) &&
		    (!threadPool.isShutdown())) {
			threadPool.shutdown();
		}
	}
	
	public void resetTurns() {
		changeCurrentTurnUser(null);
		
		resetTrackingVariables();
	}
	
	private void resetTrackingVariables() {
		listOfMoves.clear();
		flipCount = 0;
	}
	
	/**
	 * Method called when a card is flipped
	 * This will check whether we have a match, and whether we need to reflip cards, etc. 
	 */
	public UserModel handleTurn(GameCard toFlip) throws ThreadRunningException {
		if (flipThreadRunning) {
			throw new ThreadRunningException("Ignoring actions while waiting for cards to reflip.");
		}
		
		// Record and perform the move
		listOfMoves.add(flipCount, toFlip);
		flipCount++;
		toFlip.flip();
		
		// If we have performed enough flips it's time to check scoring
		if (flipCount >= GameManager.DEFAULT_MAX_FLIP) {
			final int matchType = checkForMatch();
			
			// Determine whether we have a match
			if (matchType != -1) {
				// Reset our move list and flip count after a turn is done
				resetTrackingVariables();
				
				// Return the matching user so they can have their score incremented
				return currentTurnUser;
			}
			else {
				// Execute a thread that will reflip unmatching cards after a delay
				// This allows all users to see the failed match before the cards are reflipped
				threadPool.execute(new Runnable() {
					public void run() {
						flipThreadRunning = true;
						
						try{
							Thread.sleep(reflipDelay);
							
							// Act if the game is still running
							if (parentGame.getIsStarted()) {
								// Reflip all cards on a failed match
								for (GameCard toReflip : listOfMoves) {
									toReflip.unflip();
								}
								
								// Move to the next user, unless the previous one got a match
								nextTurnUser();
								
								// Reset our move list and flip count after a turn is done
								resetTrackingVariables();
							}
						}catch (InterruptedException ignored) { }
						
						if (parentGame != null) {
							PushRenderer.render(parentGame.getName());
						}
						
						flipThreadRunning = false;
					}
				});
			}
		}
		
		return null;
	}
	
	private int checkForMatch() {
		int currentIndexType = -1;
		
		for (GameCard loopCard : listOfMoves) {
			if ((currentIndexType != -1) &&
			    (currentIndexType == loopCard.getIndexType())) {
				return currentIndexType;
			}
			currentIndexType = loopCard.getIndexType();
		}
		
		return -1;
	}
}
