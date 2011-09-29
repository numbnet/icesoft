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
package com.icefaces.project.memory.bean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.icefaces.project.memory.bean.sort.SortBean;
import com.icefaces.project.memory.exception.FailedJoinException;
import com.icefaces.project.memory.game.GameInstance;
import com.icefaces.project.memory.game.GameInstanceWrapper;
import com.icefaces.project.memory.game.card.GameCardSet;
import com.icefaces.project.memory.user.UserSession;
import com.icefaces.project.memory.util.FacesUtil;
import com.icefaces.project.memory.util.Randomizer;
import com.icefaces.project.memory.util.ValidatorUtil;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Page bean for the functionality in lobby.xhtml
 * We'll store all the backing information for the various dropdowns, manage a few render flags, etc.
 */
@ManagedBean(name="lobbyBean")
@ViewScoped
public class LobbyBean extends SortBean {
	private static final String RANDOM_CARD_VALUE = "RANDOMIZE";
	
	@ManagedProperty(value = "#{userSession}")
	private UserSession userSession;
	private List<GameInstanceWrapper> localGameList;
	private SelectItem[] availableSetsAsItems;
	private SelectItem[] availableBackImagesAsItems;
	private GameInstance createdGame = new GameInstance();
	private String selectedCardSet;
	private String selectedCardBack;
	private GameCardSet cardPreview;
	private boolean previewPopupRendered = false;
	private boolean advancedOptionsRendered = false;
	private boolean passwordPromptRendered = false;
	private String password;
	
	public LobbyBean() {
		super("name");
	}
	
	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}
	
	public List<GameInstanceWrapper> getLocalGameList() {
		if ((localGameList == null) ||
		    (localGameList.size() != userSession.getGameManager().getGameList().size())) {
			cacheGameList();
			
			// Force a resort by swapping the oldAscending value to enter the if statement below
			oldAscending = !ascending;
		}
		
        // Using the status of SortBeam, determine if the list needs to be sorted or not
        if ((!oldSort.equals(sortColumnName)) ||
            (oldAscending != ascending)) {
             sort();
             oldSort = sortColumnName;
             oldAscending = ascending;
        }
		
		return localGameList;
	}

	public void setLocalGameList(List<GameInstanceWrapper> localGameList) {
		this.localGameList = localGameList;
	}
	
	/**
	 * Method to get a list of card sets and convert them into UI friendly SelectItems
	 */
	public SelectItem[] getAvailableSetsAsItems() {
		if (availableSetsAsItems == null) {
			List<GameCardSet> allSets = userSession.getGameManager().getCardSetManager().getAvailableSets();
			
			if (ValidatorUtil.isValidList(allSets)) {
				availableSetsAsItems = new SelectItem[allSets.size()];
				
				GameCardSet currentSet = null;
				for (int i = 0; i < allSets.size(); i++) {
					currentSet = allSets.get(i);
					
					availableSetsAsItems[i] =
						new SelectItem(currentSet.getName(),
									   currentSet.getName() + " (" +
									   currentSet.getCardCount() + ")");
				}
			}
		}
		
		return availableSetsAsItems;
	}
	
	public void setAvailableSetsAsItems(SelectItem[] availableSetsAsItems) {
		this.availableSetsAsItems = availableSetsAsItems;
	}
	
	/**
	 * Method to get a list of card back images and conver them into UI friendly SelectItems
	 */
	public SelectItem[] getAvailableBackImagesAsItems() {
		if (availableBackImagesAsItems == null) {
			List<String> backImages = userSession.getGameManager().getCardSetManager().getAvailableBackImages();
			
			if (ValidatorUtil.isValidList(backImages)) {
				availableBackImagesAsItems = new SelectItem[backImages.size()];
				
				for (int i = 0; i < backImages.size(); i++) {
					availableBackImagesAsItems[i] =
						new SelectItem(backImages.get(i));
				}
			}
		}
		
		return availableBackImagesAsItems;
	}
	
	public void setAvailableBackImagesAsItems(SelectItem[] availableBackImagesAsItems) {
		this.availableBackImagesAsItems = availableBackImagesAsItems;
	}

	public GameInstance getCreatedGame() {
		return createdGame;
	}

	public void setCreatedGame(GameInstance createdGame) {
		this.createdGame = createdGame;
	}
	
	public String getSelectedCardSet() {
		return selectedCardSet;
	}

	public void setSelectedCardSet(String selectedCardSet) {
		// Check whether we should attempt to randomize the selected card set, otherwise just set it
		if (RANDOM_CARD_VALUE.equals(selectedCardSet)) {
			this.selectedCardSet =
				getAvailableSetsAsItems()[Randomizer.getInstance().nextInt(availableSetsAsItems.length)].getValue().toString();
		}
		else {
			this.selectedCardSet = selectedCardSet;
		}		
	}
	
	public String getSelectedCardBack() {
		return selectedCardBack;
	}

	public void setSelectedCardBack(String selectedCardBack) {
		// Check whether we should attempt to randomize the selected card back, otherwise just set it
		if (RANDOM_CARD_VALUE.equals(selectedCardBack)) {
			this.selectedCardBack =
				getAvailableBackImagesAsItems()[Randomizer.getInstance().nextInt(availableBackImagesAsItems.length)].getValue().toString(); 
		}
		else {
			this.selectedCardBack = selectedCardBack;
		}
	}

	public GameCardSet getCardPreview() {
		return cardPreview;
	}

	public void setCardPreview(GameCardSet cardPreview) {
		this.cardPreview = cardPreview;
	}
	
	public boolean isPreviewPopupRendered() {
		return previewPopupRendered;
	}

	public void setPreviewPopupRendered(boolean previewPopupRendered) {
		this.previewPopupRendered = previewPopupRendered;
	}
	
	public boolean isAdvancedOptionsRendered() {
		return advancedOptionsRendered;
	}

	public void setAdvancedOptionsRendered(boolean advancedOptionsRendered) {
		this.advancedOptionsRendered = advancedOptionsRendered;
	}

	public boolean isPasswordPromptRendered() {
		return passwordPromptRendered;
	}

	public void setPasswordPromptRendered(boolean passwordPromptRendered) {
		this.passwordPromptRendered = passwordPromptRendered;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRandomCardValue() {
		return RANDOM_CARD_VALUE;
	}
	
	/**
	 * Method called when a game is clicked / selected to join
	 * We'll want to display a password prompt as needed
	 */
	public void gameSelectionListener(RowSelectorEvent rse) {
		GameInstanceWrapper selectedGame = localGameList.get(rse.getRow());
		
		setPasswordPromptRendered(false);
		setPassword(null);
		
		if (selectedGame.isSelected()) {
			setPasswordPromptRendered(selectedGame.getWrapped().getHasPassword());
		}
	}
	
	public void openCardPreview(ActionEvent event) {
		// Get the actual card set object based on the selected name
		cardPreview =
			userSession.getGameManager().getCardSetManager().getCardSetByName(selectedCardSet);
		
		if (cardPreview != null) {
			setPreviewPopupRendered(true);
		}
	}
	
	public void closeCardPreview(ActionEvent event) {
		setPreviewPopupRendered(false);
	}
	
	public void toggleAdvancedOptions(ActionEvent event) {
		advancedOptionsRendered = !advancedOptionsRendered;
	}
	
	public boolean getHasSelectedCardSet() {
		return ValidatorUtil.isValidString(selectedCardSet);
	}
	
	public boolean getHasSelectedBackImage() {
		return ValidatorUtil.isValidString(selectedCardBack);
	}
	
	private void cacheGameList() {
		int gameCount = userSession.getGameManager().getGameList().size();
		localGameList = new ArrayList<GameInstanceWrapper>(gameCount);
		
		for (int i = 0; i < gameCount; i++) {
			localGameList.add(new GameInstanceWrapper(
					userSession.getGameManager().getGameAt(i)));
		}
	}
	
	/**
	 * Method called to join a selected game
	 * First we'll deteremine what game was selected to join, then drop our user into it
	 *  and redirect from the lobby to the actual game page
	 */
	public String joinGame() {
		try{
			for (GameInstanceWrapper loopGame : localGameList) {
				if (loopGame.isSelected()) {
					userSession.getGameManager().joinGame(
							userSession, loopGame.getWrapped(), password);
					
					userSession.setCurrentGame(loopGame.getWrapped());
					
					return redirectToGame();
				}
			}
			
			FacesUtil.addGlobalMessage("No game was selected to join.");
		}catch (FailedJoinException failedJoin) {
			FacesUtil.addGlobalMessage(failedJoin.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Method called to create a new game from the various parameters we've input so far
	 */
	public String createGame() {
		if (!ValidatorUtil.isValidString(selectedCardSet)) {
			return null;
		}
		
		// Translate the selected card set into a useful object
		GameCardSet cardSet =
			userSession.getGameManager().getCardSetManager().createCardSet(selectedCardSet,
																		   selectedCardBack);
		
		if (cardSet == null) {
			return null;
		}
		
		// Set our card set and get a new instance of the game
		createdGame.getBoard().setCardSet(cardSet);
		GameInstance newGame = new GameInstance(createdGame);
		
		// Request creation of the game
		userSession.getGameManager().createGame(userSession, newGame);
		userSession.setCurrentGame(newGame);
		
		// Clear the old game instance
		createdGame = new GameInstance();
		
		return redirectToGame();
	}
	
	/**
	 * Method to perform some functionality before redirecting to the game page itself
	 * We'll want to do some minor cleanup of the lobby and then switch renderers 
	 */
	public String redirectToGame() {
		if (userSession.getCurrentGame() != null) {
			setPasswordPromptRendered(false);
			setPassword(null);
			
			userSession.getRenderer().leaveLobby();
			userSession.getRenderer().joinGame(userSession.getCurrentGame().getName());
			
			return "board";
		}
		
		return null;
	}

	@Override
	protected boolean isDefaultAscending(String defaultSortColumn) {
		return true;
	}

	@Override
	protected void sort() {
        Comparator<GameInstanceWrapper> gameComparator = new Comparator<GameInstanceWrapper>() {
            public int compare(GameInstanceWrapper game1Wrapper, GameInstanceWrapper game2Wrapper) {
                // If the column name is not set, return equal
                if (sortColumnName == null) {
                    return 0;
                }
                
                GameInstance game1 = game1Wrapper.getWrapped();
                GameInstance game2 = game2Wrapper.getWrapped();
                
                if (!ascending) {
                	game2 = game1Wrapper.getWrapped();
	            	game1 = game2Wrapper.getWrapped();
                }
                
        		if ("name".equals(sortColumnName)) {
					return game1.getName().compareTo(game2.getName());
        		}
        		else if ("password".equals(sortColumnName)) {
        			return ((Boolean)game1.getHasPassword()).compareTo(game2.getHasPassword());
        		}
        		else if ("gridSize".equals(sortColumnName)) {
        			return ((Integer)game1.getBoard().getSize()).compareTo(game2.getBoard().getSize());
        		}
        		else if ("cardSet".equals(sortColumnName)) {
        			return game1.getBoard().getCardSet().compareTo(game2.getBoard().getCardSet());
        		}
        		else if ("players".equals(sortColumnName)) {
        			return ((Integer)game1.getUserCount()).compareTo(game2.getUserCount());
        		}
        		
    			return 0;
            }
        };
		
		Collections.sort(localGameList, gameComparator);
	}
}
