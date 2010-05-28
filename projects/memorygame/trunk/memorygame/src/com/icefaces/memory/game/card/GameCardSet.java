package com.personal.memory.game.card;

import java.util.ArrayList;
import java.util.List;

import com.personal.memory.util.ValidatorUtil;

/*
 * 2x2 = 2 cards
 * 4x4 = 8 cards
 * 6x6 = 18 cards
 * 8x8 = 32 cards
 * 10x10 = 50 cards
 * 12x12 = 72 cards
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
	
	public GameCard getCardInstance(int index) {
		if (index < cardList.size()) {
			return new GameCard(cardList.get(index));
		}
		
		return new GameCard(cardList.get(index % cardList.size()));
	}
	
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
