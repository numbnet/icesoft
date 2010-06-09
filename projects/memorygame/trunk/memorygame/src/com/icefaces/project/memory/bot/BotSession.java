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
package com.icefaces.project.memory.bot;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.icefaces.project.memory.game.GameBoard;
import com.icefaces.project.memory.game.GameInstance;
import com.icefaces.project.memory.game.GameManager;
import com.icefaces.project.memory.game.card.GameCard;
import com.icefaces.project.memory.game.chat.GameChatMessage;
import com.icefaces.project.memory.user.UserRenderer;
import com.icefaces.project.memory.user.UserSession;
import com.icefaces.project.memory.util.Randomizer;

public class BotSession extends UserSession {
	private int level;
	private int errorChance;
	private int moveDelayModifier;
	private List<GameCard> memorizedCards;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public BotSession(String name, int level, int errorChance, int moveDelayModifier,
		              GameManager gameManager, GameInstance currentGame) {
		super(gameManager, currentGame, new UserRenderer());
		
		this.isComputer = true;
		this.level = level;
		this.errorChance = errorChance;
		this.moveDelayModifier = moveDelayModifier;
		this.name = "Bot " + name;
		this.memorizedCards = new ArrayList<GameCard>(level);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getErrorChance() {
		return errorChance;
	}

	public void setErrorChance(int errorChance) {
		this.errorChance = errorChance;
	}
	
	public int getMoveDelayModifier() {
		return moveDelayModifier;
	}

	public void setMoveDelayModifier(int moveDelayModifier) {
		this.moveDelayModifier = moveDelayModifier;
	}

	public List<GameCard> getMemorizedCards() {
		return memorizedCards;
	}

	public void setMemorizedCards(List<GameCard> memorizedCards) {
		this.memorizedCards = memorizedCards;
	}
	
	public boolean getIsIntelligent() {
		return level > GameManager.DEFAULT_MAX_FLIP;
	}

	/**
	 * Method to memorize a card, if we haven't already
	 * By memorizing cards we can "recall" them later and simulate how
	 *  a human would play the game, instead of just cheating and looping
	 *  through the game board in an unflipped state
	 */
	public boolean memorizeCard(GameCard toMemorize) {
		// Ensure we haven't already memorized the card
		for (GameCard loopCard : memorizedCards) {
			if (loopCard == toMemorize) {
				return false;
			}
		}
		
		// Memorize the new card
		memorizedCards.add(0, toMemorize);
		
		// Trim any old memorized cards based on the intelligence level
		if (memorizedCards.size() > level) {
			memorizedCards.remove(level);
		}
		
		return true;
	}
	
	/**
	 * Clear our current memorized cards
	 * Useful between rounds in the same game
	 */
	public void clearAllMemorizedCards() {
		memorizedCards.clear();
	}
	
	/**
	 * Method to clear all flipped cards from our memory, so we don't bother checking them
	 */
	public void clearFlippedMemorizedCards() {
		List<GameCard> flippedToRemove = null;
		for (GameCard loopCard : memorizedCards) {
			// Remove any flipped cards
			if (loopCard.getIsFlipped()) {
				if (flippedToRemove == null) {
					flippedToRemove = new ArrayList<GameCard>(1);
				}
				
				flippedToRemove.add(loopCard);
			}
		}
		
		// Clean up any flipped cards
		if (flippedToRemove != null) {
			memorizedCards.removeAll(flippedToRemove);
		}
	}
	
	/**
	 * Ensure that we are intelligent enough to perform a non-random move,
	 *  and also that our errorChance didn't fire this check
	 */
	private boolean canDetermineMoves() {
		return ((getIsIntelligent()) &&
			    (memorizedCards.size() > 0) &&
			    (Randomizer.getInstance().nextInt(100) <= errorChance));
	}
	
	/**
	 * Method to determine what cards we want to flip for our turn
	 * We'll first check if any cards we've memorized would match each other
	 * Otherwise we'll intelligently randomize our moves
	 * The end result is a returned set of GameCard objects to flip
	 */
	public GameCard[] determineMoves(GameBoard board) {
		GameCard[] toReturn = new GameCard[GameManager.DEFAULT_MAX_FLIP];
		
		// Remove any flipped cards before we try to find matches
		clearFlippedMemorizedCards();
		
		if (canDetermineMoves()) {
			// Loop through every card and check it against every other card to try to find a match
			GameCard resultCard = null;
			for (GameCard cardToCheck : memorizedCards) {
				resultCard = compareCardToMemory(cardToCheck);
				
				if (resultCard != null) {
					toReturn[0] = resultCard;
					toReturn[1] = cardToCheck;
					
					if (log.isDebugEnabled()) {
						log.debug("Computer " + name + " (Level " + level + ", " + errorChance + "%) remembered a matching move.");
					}
					
					return toReturn;
				}
			}
		}
		
		return randomlyDetermineMoves(board);
	}
	
	/**
	 * Method to intelligently determine a random move
	 * We'll try to randomize our first card, then check our memory to see if
	 *  we remember something it matches against, otherwise we'll randomize our
	 *  second card too
	 * The pair of moves are then returned
	 */
	private GameCard[] randomlyDetermineMoves(GameBoard board) {
		GameCard[] toReturn = new GameCard[GameManager.DEFAULT_MAX_FLIP];
		
		toReturn[0] = board.getRandomUnflippedCard();
		
		// Check if our random first choice matches a memorized card
		GameCard resultCard = compareCardToMemory(toReturn[0]);
		
		// If we remember a match, let's use it
		if (resultCard != null) {
			toReturn[1] = resultCard;
			
			if (log.isDebugEnabled()) {
				log.debug("Computer " + name + " (Level " + level + ", " + errorChance + "%) randomly chose an inital move then remembered a match.");
			}
		}
		// Otherwise we'll try to intelligently randomize our second choice
		else {
			toReturn[1] = board.getRandomUnflippedCard(toReturn[0]);
			
			if (log.isDebugEnabled()) {
				log.debug("Computer " + name + " (Level " + level + ", " + errorChance + "%) randomly chose a move.");
			}
		}
		
		return toReturn;
	}
	
	/**
	 * Method to loop through our memorized cards and compare them to the passed card
	 * We'll return the first match, or null if no match was found
	 */
	private GameCard compareCardToMemory(GameCard toCompare) {
		for (GameCard loopCard : memorizedCards) {
			// Check against ONLY other cards, and look for a match
			if ((toCompare != loopCard) &&
			    (toCompare.getIndexType() == loopCard.getIndexType())) {
				return loopCard;
			}
		}
		
		return null;
	}
	
	/**
	 * Convenience method to add a chat message from this bot, based on the passed text
	 */
	public void botChat(String text) {
		if (currentGame != null) {
			currentGame.getChat().addMessage(new GameChatMessage(name, text));
		}
	}
}
