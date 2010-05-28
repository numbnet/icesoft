package com.personal.memory.game.card;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.personal.memory.filter.CardImageFilter;
import com.personal.memory.filter.DirectoryFilter;
import com.personal.memory.util.FacesUtil;
import com.personal.memory.util.ValidatorUtil;

import edu.emory.mathcs.backport.java.util.Arrays;

public class GameCardSetManager {
	private static final long CONTENT_CHECK_RATE = 5 * 60 * 1000l;
	public static final String BASE_PATH_FRONT = "./css/images/cardset-front";
	public static final String BASE_PATH_BACK = "./css/images/cardset-back";
	public static final String DEFAULT_BACK_IMAGE = "default-back.png";
	
	private File setDir;
	private File backImageDir;
	private long lastModDate = -1;
	
	private List<String> availableBackImages;
	private List<GameCardSet> availableSets;
	
	public GameCardSetManager() {
		init();
	}
	
	protected void init() {
		initAvailableSets();
		
		scheduleCheckSetChanges();		
	}
	
	@SuppressWarnings("unchecked")
	private void initAvailableSets() {
		try{
			if ((!ValidatorUtil.isValidDirectory(setDir)) ||
			    (!ValidatorUtil.isValidDirectory(backImageDir))) {
				String baseWebDir = FacesUtil.getBaseFilesystemDir();
				
				if (baseWebDir != null) {
					setDir = new File(baseWebDir, BASE_PATH_FRONT);
					backImageDir = new File(baseWebDir, BASE_PATH_BACK);
				}
			}
				
			// Try to generate the back images from a filtered file listing
			if (ValidatorUtil.isValidDirectory(backImageDir)) {
				availableBackImages =
					new ArrayList<String>(Arrays.asList(backImageDir.list(new CardImageFilter())));
			}
			
			// Just put in a default back image if we can't find any others
			if (!ValidatorUtil.isValidList(availableBackImages)) {
				availableBackImages = new ArrayList<String>(1);
				availableBackImages.add(DEFAULT_BACK_IMAGE);
			}
			else {
				// Sort our valid list otherwise
				Collections.sort(availableBackImages);
			}
			
			// Generate the card set items
			if (ValidatorUtil.isValidDirectory(setDir)) {
				File[] childDirs = setDir.listFiles(new DirectoryFilter());
				
				availableSets = new Vector<GameCardSet>(childDirs.length);
				
				File currentDir = null;
				for (int i = 0; i < childDirs.length; i++) {
					currentDir = childDirs[i];
					
					availableSets.add(initCardSet(currentDir));
				}
				
				// Sort the generated list of sets
				Collections.sort(availableSets);
				
				return;
			}
		}catch (Exception failed) {
			failed.printStackTrace();
		}
		
		availableSets = new Vector<GameCardSet>(0);
	}
	
	private GameCardSet initCardSet(File setDir) {
		String setName = setDir.getName();
		File[] cardImages = setDir.listFiles(new CardImageFilter());
		
		GameCardSet toReturn = new GameCardSet(setName, cardImages.length);
		
		for (int i = 0; i < cardImages.length; i++) {
			toReturn.addCard(new GameCard(i, cardImages[i].getName(),
										  wrapFrontImagePath(setName, cardImages[i].getName()),
										  wrapBackImagePath(DEFAULT_BACK_IMAGE)));
		}
		
		return toReturn;
	}
	
	private void scheduleCheckSetChanges() {
        // Schedule a task to check for card set directory changes every 5 minutes
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	// If the directory content of available sets has changed
            	//  we'll want to reinitialize the list
                if (checkSetChanges()) {
                	initAvailableSets();
                }
            }
        }, CONTENT_CHECK_RATE, CONTENT_CHECK_RATE);
	}
	
	private boolean checkSetChanges() {
		// Ensure we have a valid set directory
		if (ValidatorUtil.isValidDirectory(setDir)) {
			// Check whether the modification date has changed
			if (lastModDate != setDir.lastModified()) {
				if (ValidatorUtil.isValidList(availableSets)) {
					lastModDate = setDir.lastModified();
					
					// Check whether the current list size is different from the file system list
					return
						(availableSets.size() != setDir.listFiles(new DirectoryFilter()).length);
				}
			}
		}
		
		return false;
	}
	
	public static String wrapBackImagePath(String backImage) {
		return BASE_PATH_BACK + "/" + backImage;
	}	
	
	public static String wrapFrontImagePath(String setName, String cardName) {
		return BASE_PATH_FRONT + "/" + setName + "/" + cardName;
	}
	
	public String getBasePathBack() {
		return BASE_PATH_BACK;
	}
	
	public String getBasePathFront() {
		return BASE_PATH_FRONT;
	}
	
	public List<GameCardSet> getAvailableSets() {
		return availableSets;
	}

	public void setAvailableSets(List<GameCardSet> availableSets) {
		this.availableSets = availableSets;
	}
	
	public List<String> getAvailableBackImages() {
		return availableBackImages;
	}

	public void setAvailableBackImages(List<String> availableBackImages) {
		this.availableBackImages = availableBackImages;
	}

	public GameCardSet getCardSetByName(String setName) {
		for (GameCardSet loopSet : availableSets) {
			if (setName.equals(loopSet.getName())) {
				return loopSet;
			}
		}
		
		return null;
	}
	
	public GameCardSet createCardSet(String setName, String backImage) {
		GameCardSet baseSet = getCardSetByName(setName);
		
		if (baseSet != null) {
			return new GameCardSet(baseSet, backImage);
		}
		
		return null;
	}
}
