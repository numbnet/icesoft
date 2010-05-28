package com.personal.memory.bean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.personal.memory.bean.sort.SortBean;
import com.personal.memory.exception.FailedJoinException;
import com.personal.memory.game.GameInstance;
import com.personal.memory.game.GameInstanceWrapper;
import com.personal.memory.game.card.GameCardSet;
import com.personal.memory.user.UserSession;
import com.personal.memory.util.FacesUtil;
import com.personal.memory.util.ValidatorUtil;

import edu.emory.mathcs.backport.java.util.Collections;

public class LobbyBean extends SortBean {
	private static final String RANDOM_CARD_VALUE = "RANDOMIZE";
	
	private Random randomizer;
	
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
		
		init();
	}
	
	protected void init() {
		randomizer = new Random(System.currentTimeMillis());
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
		if (RANDOM_CARD_VALUE.equals(selectedCardSet)) {
			this.selectedCardSet =
				getAvailableSetsAsItems()[randomizer.nextInt(availableSetsAsItems.length)].getValue().toString(); 
		}
		else {
			this.selectedCardSet = selectedCardSet;
		}		
	}
	
	public String getSelectedCardBack() {
		return selectedCardBack;
	}

	public void setSelectedCardBack(String selectedCardBack) {
		if (RANDOM_CARD_VALUE.equals(selectedCardBack)) {
			this.selectedCardBack =
				getAvailableBackImagesAsItems()[randomizer.nextInt(availableBackImagesAsItems.length)].getValue().toString(); 
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
	
	public void gameSelectionListener(RowSelectorEvent rse) {
		GameInstanceWrapper selectedGame = localGameList.get(rse.getRow());
		
		setPasswordPromptRendered(false);
		setPassword(null);
		
		if (selectedGame.isSelected()) {
			setPasswordPromptRendered(selectedGame.getWrapped().getHasPassword());
		}
	}
	
	public void openCardPreview(ActionEvent event) {
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
	
	public void refreshGameList(ActionEvent event) {
		cacheGameList();
	}

	private void cacheGameList() {
		int gameCount = userSession.getGameManager().getGameList().size();
		localGameList = new ArrayList<GameInstanceWrapper>(gameCount);
		
		for (int i = 0; i < gameCount; i++) {
			localGameList.add(new GameInstanceWrapper(
					userSession.getGameManager().getGameAt(i)));
		}
	}
	
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
	
	public String createGame() {
		// Translate the selected card set into a useful object
		if (!ValidatorUtil.isValidString(selectedCardSet)) {
			return null;
		}
		
		GameCardSet cardSet =
			userSession.getGameManager().getCardSetManager().createCardSet(selectedCardSet,
																		   selectedCardBack);
		
		if (cardSet == null) {
			return null;
		}
		
		createdGame.getBoard().setCardSet(cardSet);
		GameInstance newGame = new GameInstance(createdGame);
		
		userSession.getGameManager().createGame(userSession, newGame);
		userSession.setCurrentGame(newGame);
		
		createdGame = null;
		
		return redirectToGame();
	}
	
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
