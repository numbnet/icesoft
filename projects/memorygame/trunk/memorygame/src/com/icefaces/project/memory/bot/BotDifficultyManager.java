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

import javax.faces.model.SelectItem;

import com.icefaces.project.memory.util.ValidatorUtil;

/**
 * Class used to track the different Bot Difficulty levels, and provide
 *  a few simple access methods to the underlying list
 */
public class BotDifficultyManager {
	private static BotDifficulty[] difficulties = new BotDifficulty[] {
		new BotDifficulty("Simple", "Randomly guesses all moves.", 0, 0, 0, 0, 0),
		new BotDifficulty("Easy", "Can sometimes remember previous cards.", 1, 2, 10, 25, 50),
		new BotDifficulty("Normal", "Represents the usual memory of a person.", 3, 2, 5, 8, 100),
		new BotDifficulty("Hard", "Sharp memory ensures lots of matches.", 5, 3, 0, 7, 200),
		new BotDifficulty("Brutal", "Limitless memory without error.", 64, 0, 0, 0, 300)
	};
	private static BotDifficulty defaultDifficulty = difficulties[2]; // Default to "Normal"
	
	public static BotDifficulty getDefaultDifficulty() {
		return defaultDifficulty;
	}
	
	public static String getDefaultDifficultyName() {
		return defaultDifficulty.getName();
	}
	
	public static BotDifficulty[] getDifficulties() {
		return difficulties;
	}
	
	public static SelectItem[] getDifficultiesAsItems() {
		SelectItem[] toReturn = new SelectItem[difficulties.length];
		
		BotDifficulty currentDifficulty;
		for (int i = 0; i < difficulties.length; i++) {
			currentDifficulty = difficulties[i];
			toReturn[i] = new SelectItem(currentDifficulty.getName(),
										 currentDifficulty.getName() + " - " + currentDifficulty.getDescription());
		}
		
		return toReturn;
	}
	
	public static BotDifficulty getDifficultyByName(String name) {
		if (ValidatorUtil.isValidString(name)) {
			for (int i = 0; i < difficulties.length; i++) {
				if (name.equals(difficulties[i].getName())) {
					return difficulties[i];
				}
			}
		}
		
		return defaultDifficulty;
	}
}
