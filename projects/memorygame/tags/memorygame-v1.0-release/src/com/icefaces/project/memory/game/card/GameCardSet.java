package com.icefaces.project.memory.game.card;

import java.util.ArrayList;
import java.util.List;

import com.icefaces.project.memory.util.ValidatorUtil;

/**
 * Class that represents a set of GameCards
 * Remember that some card sets are too small to be used for larger board
 *  sizes without duplication
 *  
 * Here are the details of how many cards are needed for each board size:
 *  > 2x2 = 2 cards
 *  > 4x4 = 8 cards
 *  > 6x6 = 18 cards
 *  > 8x8 = 32 cards
 *  > 10x10 = 50 cards
 *  > 12x12 = 72 cards
 */
public class GameCardSet implements Comparable<GameCardSet> {
	private String name;
	private String backImage;
	private List<GameCard> cardList;
	
	public GameCardSet(String name, int numberOfCards) {
		this(name,
		     GameCardSetManager.DEFAULT_BACK_IMAGE,
		     new ArrayList<GameCard>(numberOfCards));
	}
	
	public GameCardSet(GameCardSet clone, String backImage) {
		this(clone.getName(),
			 backImage,
		     clone.getCardList());
	}
	
	public GameCardSet(String name, String backImage, List<GameCard> cardList) {
		this.name = name;
		this.cardList = cardList;
		
		// Need to set the back image last
		// This is so the card list is already set, and the back image
		//  can be applied to all these cards
		setBackImage(backImage);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getBackImage() {
		return backImage;
	}

	public void setBackImage(String backImage) {
		this.backImage = backImage;
		
		// Update all children cards with the new back image
		applyBackImage();
	}

	public List<GameCard> getCardList() {
		return cardList;
	}

	public void setCardList(List<GameCard> cardList) {
		this.cardList = cardList;
	}
	
	public int getCardCount() {
		if (ValidatorUtil.isValidList(cardList)) {
			return cardList.size();
		}
		
		return 0;
	}
	
	public int getMaxGridSide() {
		return (int)Math.sqrt(getCardCount()*2.0);
	}
	
	public boolean addCard(GameCard toAdd) {
		return cardList.add(toAdd);
	}
	
	/**
	 * Method to return a GameCard at a specific index
	 * This is preferable to directly accessing the list
	 * This is also useful as we can still return a valid GameCard even if the index is
	 *  greater than the number of cards. In that case we just start wrapping around
	 *  and giving cards from the beginning of the list
	 */
	public GameCard getCardInstance(int index) {
		if (index < cardList.size()) {
			return new GameCard(cardList.get(index));
		}
		
		return new GameCard(cardList.get(index % cardList.size()));
	}
	
	/**
	 * Method to take the current backImage and set it to all children GameCard objects
	 */
	private void applyBackImage() {
		if ((ValidatorUtil.isValidList(cardList)) &&
		    (ValidatorUtil.isValidString(backImage))) {
			for (GameCard loopCard : cardList) {
				loopCard.setBackImage(GameCardSetManager.wrapBackImagePath(backImage));
			}
		}
	}
	
	public int compareTo(GameCardSet cardSet) {
		if ((cardSet != null) && (ValidatorUtil.isValidString(name))) {
			return name.compareTo(cardSet.getName());
		}
		
		return 0;
	}
}
