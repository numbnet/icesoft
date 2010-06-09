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

import com.icefaces.project.memory.game.GameInstance;
import com.icefaces.project.memory.game.GameManager;
import com.icefaces.project.memory.game.card.GameCard;
import com.icefaces.project.memory.user.UserModel;
import com.icefaces.project.memory.util.Randomizer;

/**
 * Class used to handle generating and naming computer controlled players (aka "bots")
 */
public class BotManager {
	public static final long BOT_THINK_DELAY_BASE = 900l;
	public static final int BOT_THINK_DELAY_VARIATION = 500;
	public static final long BOT_MOVE_DELAY = 700l;
	public static final int BOT_MOVE_DELAY_VARIATION = 600;	
	
	/**
	 * Get a list of available computer names
	 */
	public static List<String> getAvailableNames() {
		ArrayList<String> toReturn = new ArrayList<String>(6);
		
		toReturn.add("Peter");
		toReturn.add("John");
		toReturn.add("Doug");
		toReturn.add("Sarah");
		toReturn.add("Karen");
		toReturn.add("Anna");
		
		return toReturn;
	}
	
	/**
	 * Method to generate a series of random computer sessions that can be added to a game
	 * This will always provide uniquely named bots
	 *  However if the number requested exceeds the available names, generic incrementing numbers
	 *   will be used for names instead
	 */
	public static BotSession[] generateComputers(BotDifficulty difficulty, int number,
												 GameManager gameManager, GameInstance currentGame) {
		BotSession[] toReturn = new BotSession[number];
		List<String> names = getAvailableNames();
		String currentName = null;
		
		// Generate the desired series of computer sessions
		for (int i = 0; i < number; i++) {
			// Default to an incremented number as the name
			currentName = String.valueOf(System.currentTimeMillis());
			
			// If possible pull a unique name from our instance of available names
			if (names.size() > 0) {
				currentName = names.remove(Randomizer.getInstance().nextInt(names.size()));
			}
			
			toReturn[i] = generateComputer(currentName, difficulty, gameManager, currentGame);
		}
		
		return toReturn;
	}
	
	/**
	 * Method to generate a single named computer session
	 * This will handle all the proper toggling of flags and values to ensure the session is valid and
	 *  can be directly added to a GameInstance
	 */
	public static BotSession generateComputer(String baseName, BotDifficulty difficulty,
										      GameManager gameManager, GameInstance currentGame) {
		// Create a new BotSession and return it
		return new BotSession(baseName,
							  difficulty.generateNewLevel(), difficulty.generateNewErrorChance(),
							  difficulty.getMoveDelayModifier(),
							  gameManager, currentGame);
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
	public static void performComputerTurn(final GameInstance game) {
		game.getTurns().getThreadPool().execute(new Runnable() {
			public void run() {
				// First we'll want to sleep for a bit, to give the illusion of being human
				try {
					Thread.sleep(BOT_THINK_DELAY_BASE +
								 Randomizer.getInstance().nextInt(BOT_THINK_DELAY_VARIATION));
				}catch (InterruptedException interrupted) {
					return;
				}
				
				// Then we'll want to make sure the cards have reflipped and we
				//  are in a game state that is ready to accept new moves
				// We'll check the thread status 5 times, and break and continue
				//  with the turn as soon as the thread is done
				for (int i = 0; i < 5; i++) {
					if (game.getTurns().getThreadRunning()) {
						try {
							// Don't sleep for the full reflipDelay each time
							// This is because if we miss the thread stopping by a few milliseconds we
							//  don't want to have to wait through the entire reflipDelay again
							Thread.sleep(game.getReflipDelay()/3);
						}catch (InterruptedException ignored) { }
					}
					else {
						break;
					}
				}
				
				// Double check the started status, since with all these threads
				//  it could have been reset after our sleep calls
				if (game.getIsStarted()) {
					boolean scored = false;
					
					// Get our computer player we're moving for
					BotSession bot = (BotSession)game.getTurns().getCurrentTurnUser();
					
					// Determine our set of moves
					GameCard[] moves = bot.determineMoves(game.getBoard());
					
					// Run through the actual moves
					game.performTurn(moves[0]);
					try {
						Thread.sleep(BOT_MOVE_DELAY +
									 Randomizer.getInstance().nextInt(BOT_MOVE_DELAY_VARIATION) -
									 bot.getMoveDelayModifier());
					}catch (InterruptedException interrupted) {
						return;
					}
					scored = game.performTurn(moves[1]);
					
					// If we scored a point we need to act again, so restart our computer turn
					if ((scored) && (game.getIsStarted())) {
						performComputerTurn(game);
					}
					return;
				}
				// If the game is not running we don't want to act, but we also still haven't completed our turn
				// So we'll sleep a bit and try again
				else {
					try {
						Thread.sleep(BOT_THINK_DELAY_BASE*2);
					}catch (InterruptedException ignored) { }
					
					performComputerTurn(game);
					return;
				}
			}
		});
	}
	
	/**
	 * Method to loop through the passed users and delegate memorizing the
	 *  passed GameCard for every user session that is a bot
	 * Memorizing a card will allow a bot to recall it in the future when they are
	 *  trying to make a match, which makes them seem a bit more human
	 */
	public static void memorizeCard(List<UserModel> users, GameCard toMemorize) {
		for (UserModel loopUser : users) {
			if (loopUser.getIsComputer()) {
				((BotSession)loopUser).memorizeCard(toMemorize);
			}
		}
	}
	
	public static void clearMemorizedCards(List<UserModel> users) {
		for (UserModel loopUser : users) {
			if (loopUser.getIsComputer()) {
				((BotSession)loopUser).clearAllMemorizedCards();
			}
		}
	}
}
