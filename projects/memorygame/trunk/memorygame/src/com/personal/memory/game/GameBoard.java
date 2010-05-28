package com.personal.memory.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.personal.memory.game.card.GameCard;
import com.personal.memory.game.card.GameCardSet;

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
