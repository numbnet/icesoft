package com.icefaces.project.memory.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.icefaces.project.memory.game.GameInstance;
import com.icefaces.project.memory.game.GameManager;
import com.icefaces.project.memory.user.UserRenderer;
import com.icefaces.project.memory.user.UserSession;

/**
 * Class used to handle generating and naming computer controlled players (aka "bots")
 */
public class BotManager {
	private static final Random randomizer = new Random(System.currentTimeMillis());
	
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
	public static UserSession[] generateComputers(int number, GameManager gameManager, GameInstance currentGame) {
		UserSession[] toReturn = new UserSession[number];
		List<String> names = getAvailableNames();
		String currentName = null;
		
		// Generate the desired series of computer sessions
		for (int i = 0; i < number; i++) {
			// Default to an incremented number as the name
			currentName = String.valueOf(System.currentTimeMillis());
			
			// If possible pull a unique name from our instance of available names
			if (names.size() > 0) {
				currentName = names.remove(randomizer.nextInt(names.size()));
			}
			
			toReturn[i] = generateComputer(currentName, gameManager, currentGame);
		}
		
		return toReturn;
	}
	
	/**
	 * Method to generate a single named computer session
	 * This will handle all the proper toggling of flags and values to ensure the session is valid and
	 *  can be directly added to a GameInstance
	 */
	public static UserSession generateComputer(String name, GameManager gameManager, GameInstance currentGame) {
		UserSession computer = new UserSession();
		
		computer.setName(name + " (Computer)");
		computer.setIsComputer(true);
		computer.setGameManager(gameManager);
		computer.setCurrentGame(currentGame);
		computer.setRenderer(new UserRenderer());
		
		return computer;
	}
}
