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

import com.icefaces.project.memory.user.UserModel;
import com.icefaces.project.memory.util.Randomizer;

/**
 * Class used to handle the automated chat messages of bots
 * All chat requests for a bot should be routed through here, instead of
 *  accessing the botChat method directly on a BotSession
 * What we'll do is check the chat chance for each topic for each bot,
 *  and then randomly choose a message if needed and tell the bot to say it
 */
public class BotChatManager {
	private static final int CONGRATULATE_SCORE_CHANCE = 35;
	private static final int CONGRATULATE_WIN_CHANCE = 85;
	private static final int CONGRATULATE_WELCOME_CHANCE = 70;
	private static final int CONGRATULATE_TAUNT_CHANCE = 25;
	
	// Messages to say when a human scores a point
	private static final String[] congratulateScoreMessages = {
		"Nice move %s",
		"Nice point %s",
		"Nice match %s",
		"Smooth move %s",
		"Good memory %s",
		"Beginner's luck %s!",
		"Big score for %s!",
		"Ah I missed that one %s, good job",
		"Excellent move",
		"Excellent match",
		"Good match %s",
		"Good match!",
		"Pretty good for a human",
		"Pretty good for a human %s"
	};
	// Messages to say when a human wins a game
	private static final String[] congratulateWinHumanMessages = {
		"Good game",
		"Woot good game",
		"Great game",
		"Fun game",
		"Nice game",
		"That was a good game",
		"Nice win %s",
		"%s, you're good",
		"%s, you're smart",
		"Aw %s beat me",
		"Beaten by %s :(",
		"Good game %s",
		"Nice game %s",
		"Good job %s you won",
		"Nice job %s",
		"I'll get you next time %s",
		"We'll see if you can win again %s",
		"Beginner's luck %s!",
		"Good win %s",
		"Nice victory %s",
		"Pretty good for a human",
		"Not bad for a human, %s",
		"%s wins for humanity!"
	};
	// Messages to say when a bot wins a game
	private static final String[] congratulateWinBotMessages = {
		"Good game",
		"Woot good game",
		"Great game",
		"Fun game",
		"Nice game",
		"That was a good game",
		"Nice win %s",
		"%s, you're good",
		"%s, you're smart",
		"Good game %s",
		"Nice game %s",
		"Nice job %s",
		"Good win %s",
		"Nice victory %s",
		"Computer players rule!",
		"I knew %s would win",
		"%s won for all computer kind",
		"Machines beat humans!",
		"Hooray %s is unstoppable"
	};
	// Messages to say when a bot joins the game
	private static final String[] welcomeMessages = {
		"Hey",
		"Hey all",
		"Hey everybody",
		"Hey everyone",
		"Hi",
		"Hi all",
		"Hi everybody",
		"Hi everyone",
		"Hello",
		"Hello all",
		"Hello everybody",
		"Hello everyone",
		"Howdy",
		"Yo",
		"Sup",
		"Greetings",
		"I'm ready to play!",
		"This is going to be fun",
		"Let's rock",
		"Time to play!"
	};
	// Messages to say when a bot scores a point
	private static final String[] tauntScoreMessages = {
		"Oh yeah who scored?",
		"Woot check out that move",
		"Check out that move",
		"Like that move",
		"Nice I'm on the scoreboard",
		"I'm pretty awesome",
		"Oh snap what a move",
		"My memory is awesome",
		"My skills are awesome",
		"I'm going to win I hope!",
		"Perfect move if I say so myself",
		"Match for me!",
		"Point for me!",
		"Woot that's a match",
		"Woot that's a point",
		"Hooray that's a match",
		"Hooray that's a point",
		"Yes I scored!",
		"I'm on a roll!"
	};
	
	/**
	 * Wrapper method for main botChatGeneric
	 */
	private static void botChatGeneric(List<UserModel> users,
									   int talkChance,
									   String[] messages) {
		botChatGeneric(users, talkChance, messages, null);
	}

	/**
	 * Generic method to potentially have each bot say a message
	 * We'll loop through the passed users, and for each UserModel that is
	 *  a computer we'll randomize a percent chance against the passed talkChance
	 * If that passes we'll take a random message from the passed messages and
	 *  make the current bot say that message
	 */
	private static void botChatGeneric(List<UserModel> users,
									   int talkChance,
									   String[] messages, String name) {
		String chatText = null;
		
		// Loop through the users looking for computers
		for (UserModel loopUser : users) {
			if (loopUser.getIsComputer()) {
				// See if the computer will make a comment, which is based on the passed talk chance percent
				if (Randomizer.getInstance().nextInt(100) <= talkChance) {
					// Choose a random message from the avaiable ones
					chatText = messages[Randomizer.getInstance().nextInt(messages.length)];
					
					// If we have a passed name, substitute it into the message
					if (name != null) {
						chatText = String.format(chatText, name);
					}
					
					// Make the bot chat with our desired text and render flag
					((BotSession)loopUser).botChat(chatText);
				}
			}
		}
	}
	
	/**
	 * Method to call when a human scores a point
	 */
	public static void botChatCongratulateScore(List<UserModel> users, String scorerName) {
		botChatGeneric(users, CONGRATULATE_SCORE_CHANCE, congratulateScoreMessages, scorerName);
	}
	
	/**
	 * Method to call when someone won the game
	 * This will act differently depending on whether it was a human or a bot that won the game
	 */
	public static void botChatCongratulateWin(List<UserModel> users, String winnerName, boolean winnerIsComputer) {
		if (!winnerIsComputer) {
			botChatGeneric(users, CONGRATULATE_WIN_CHANCE, congratulateWinHumanMessages, winnerName);
		}
		else {
			// Ensure we have at least 2 people in the game before congratulating,
			//  since otherwise it's just 1 bot and 1 human, and we don't need the
			//  bot congratulating themself
			if (users.size() > 2) {
				botChatGeneric(users, CONGRATULATE_WIN_CHANCE, congratulateWinBotMessages, winnerName);
			}
		}
	}
	
	/**
	 * Method to call when a bot joins the game and wishes to greet the other players
	 */
	public static void botChatJoinGreeting(UserModel joiner) {
		List<UserModel> users = new ArrayList<UserModel>(1);
		users.add(joiner);
		
		botChatGeneric(users, CONGRATULATE_WELCOME_CHANCE, welcomeMessages);
	}
	
	/**
	 * Method to call when a bot scores a point and wishes to taunt the other players
	 */
	public static void botChatTauntOnScore(UserModel scorer) {
		List<UserModel> users = new ArrayList<UserModel>(1);
		users.add(scorer);
		
		botChatGeneric(users, CONGRATULATE_TAUNT_CHANCE, tauntScoreMessages);
	}
}
