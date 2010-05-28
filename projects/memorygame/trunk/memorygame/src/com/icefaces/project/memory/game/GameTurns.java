package com.icefaces.project.memory.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.icesoft.faces.async.render.SessionRenderer;
import com.icefaces.project.memory.exception.ThreadRunningException;
import com.icefaces.project.memory.game.card.GameCard;
import com.icefaces.project.memory.user.UserModel;

/**
 * Game class to handle the flipping of cards, determing of scoring, turn tracking, etc. 
 */
public class GameTurns {
	private Random randomizer;
	private String gameName;
	private long reflipDelay;
	private List<UserModel> users;
	private UserModel currentTurnUser;
	private List<GameCard> listOfMoves = new ArrayList<GameCard>(GameManager.DEFAULT_MAX_FLIP);
	private int flipCount = 0;
	
	private ExecutorService threadPool; // delicious thread pools
	private boolean flipThreadRunning = false;
	
	public GameTurns(String gameName, long reflipDelay, List<UserModel> users) {
		this.gameName = gameName;
		this.reflipDelay = reflipDelay;
		this.users = users;
		
		init();
	}
	
	protected void init() {
		randomizer = new Random(System.currentTimeMillis());
		threadPool = Executors.newCachedThreadPool();
	}
	
	public UserModel getCurrentTurnUser() {
		return currentTurnUser;
	}

	public void setCurrentTurnUser(UserModel currentTurnUser) {
		if (this.currentTurnUser != null) {
			this.currentTurnUser.setIsTurn(false);
		}
		if (currentTurnUser != null) {
			currentTurnUser.setIsTurn(true);
		}
		
		this.currentTurnUser = currentTurnUser;
	}
	
	public ExecutorService getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ExecutorService threadPool) {
		this.threadPool = threadPool;
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
				
				setCurrentTurnUser(users.get(newUserIndex));
			}
		}
		else {
			determineTurns();
		}
	}
	
	public void determineTurns() {
		setCurrentTurnUser(users.get(randomizer.nextInt(users.size())));
	}
	
	public void stopThreadPool() {
		if ((threadPool != null) &&
		    (!threadPool.isShutdown())) {
			threadPool.shutdown();
		}
	}
	
	public void resetTurns() {
		setCurrentTurnUser(null);
		
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
							
							// Reflip all cards on a failed match
							for (GameCard toReflip : listOfMoves) {
								toReflip.unflip();
							}
							
							// Move to the next user, unless the previous one got a match
							nextTurnUser();
							
							// Reset our move list and flip count after a turn is done
							resetTrackingVariables();
						}catch (InterruptedException ignored) { }
						
						if (gameName != null) {
							SessionRenderer.render(gameName);
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
