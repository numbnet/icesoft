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

import com.icefaces.project.memory.util.Randomizer;

/**
 * Class representing the difficulty of a bot
 * Some factors that make a bot more challenging to play against include
 *  how long their memorized card list will be, what their error chance will be, etc.
 */
public class BotDifficulty {
	private String name;
	private String description;
	private int minLevel;
	private int rangeLevel;
	private int minErrorChance;
	private int rangeErrorChance;
	private int moveDelayModifier;
	
	public BotDifficulty(String name, String description,
						 int minLevel, int rangeLevel,
						 int minErrorChance, int rangeErrorChance,
						 int moveDelayModifier) {
		this.name = name;
		this.description = description;
		this.minLevel = minLevel;
		this.rangeLevel = rangeLevel;
		this.minErrorChance = minErrorChance;
		this.rangeErrorChance = rangeErrorChance;
		this.moveDelayModifier = moveDelayModifier;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMinLevel() {
		return minLevel;
	}
	
	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}
	
	public int getRangeLevel() {
		return rangeLevel;
	}
	
	public void setRangeLevel(int rangeLevel) {
		this.rangeLevel = rangeLevel;
	}
	
	public int getMinErrorChance() {
		return minErrorChance;
	}
	
	public void setMinErrorChance(int minErrorChance) {
		this.minErrorChance = minErrorChance;
	}
	
	public int getRangeErrorChance() {
		return rangeErrorChance;
	}
	
	public void setRangeErrorChance(int rangeErrorChance) {
		this.rangeErrorChance = rangeErrorChance;
	}
	
	public int getMoveDelayModifier() {
		return moveDelayModifier;
	}

	public void setMoveDelayModifier(int moveDelayModifier) {
		this.moveDelayModifier = moveDelayModifier;
	}

	/**
	 * Method to generate a random level for this difficulty based on minLevel and rangeLevel
	 */
	public int generateNewLevel() {
		if (rangeLevel > 0) {
			return minLevel + Randomizer.getInstance().nextInt(rangeLevel);
		}
		
		return minLevel;
	}
	
	/**
	 * Method to generate a random error chance for this difficulty based on minErrorChance and rangeErrorChance
	 */
	public int generateNewErrorChance() {
		if (rangeErrorChance > 0) {
			return minErrorChance + Randomizer.getInstance().nextInt(rangeErrorChance);
		}
		
		return minErrorChance;
	}
}
