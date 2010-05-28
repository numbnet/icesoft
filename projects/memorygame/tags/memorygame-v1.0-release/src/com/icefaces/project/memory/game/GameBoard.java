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
import java.util.Random;

import com.icefaces.project.memory.game.card.GameCard;
import com.icefaces.project.memory.game.card.GameCardSet;

/**
 * Game class to handle the board
 * We'll store all clickable GameCards, the size, and how many matches we have so far
 */
public class GameBoard {
	private Random randomizer;
	private int matchCount = 0; // When this is >= half the cells, game is over
	private int size = GameManager.DEFAULT_SIZE;
	private GameCardSet cardSet;
	private List<GameCard> data;
	
	public GameBoard(int size, GameCardSet cardSet) {
		this.size = size;
		this.cardSet = cardSet;
		
		init();
	}
	
	protected void init() {
		randomizer = new Random(System.currentTimeMillis());
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public GameCardSet getCardSet() {
		return cardSet;
	}

	public void setCardSet(GameCardSet cardSet) {
		this.cardSet = cardSet;
	}

	public List<GameCard> getData() {
		return data;
	}

	public void setData(List<GameCard> data) {
		this.data = data;
	}
	
	/**
	 * Method to generate the board based on our current card set
	 * We'll basically get a valid GameCard for every slot in our board grid
	 */
	public void generateData() {
		int numberOfCells = getCellCount();
		int halfLifeCells = getHalfCellCount();
		
		// Setup our list to be filled and used
		data = new ArrayList<GameCard>(numberOfCells);
		
		// Generate a list of matching cards
		// Then we'll loop through and assign their position randomly
		List<GameCard> availableData = new ArrayList<GameCard>(numberOfCells);
		
		for (int i = 0; i < numberOfCells; i++) {
			if (i < halfLifeCells) {
				availableData.add(cardSet.getCardInstance(i));
			}
			else {
				availableData.add(cardSet.getCardInstance(i-halfLifeCells));
			}
		}
		
		for (int i = 0; i < numberOfCells; i++) {
			data.add(availableData.remove(
				randomizer.nextInt(availableData.size())));
		}
		
		// Reset our full list since it has been emptied
		availableData = null;
	}
	
	public void resetBoard() {
		matchCount = 0;
	}
	
	public boolean isGameDone() {
		matchCount++;
		
		return matchCount >= getHalfCellCount();
	}
	
	public int getCellCount() {
		return size*size;
	}
	
	public int getHalfCellCount() {
		return getCellCount()/2;
	}
	
	public int getPixelWidth() {
		return size * (GameCard.PIXEL_WIDTH+2);
	}
	
	public int getPixelHeight() {
		return size * (GameCard.PIXEL_HEIGHT+2);
	}
}
