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
package com.icefaces.project.memory.game.card;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.icefaces.project.memory.filter.CardImageFilter;
import com.icefaces.project.memory.filter.DirectoryFilter;
import com.icefaces.project.memory.util.FacesUtil;
import com.icefaces.project.memory.util.ValidatorUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class used to manage the various GameCardSets
 * This includes generating GameCardSet objects from the filesystem images we have available
 * This class will also use a Timer to check the filesystem image directory for changes
 *  and reload the available card sets when necessary. So a folder with new card set images
 *  can be added to the filesystem directory and will be available to the lobby without a restart
 */
public class GameCardSetManager {
	private static final long CONTENT_CHECK_RATE = 5 * 60 * 1000l;
	public static final String BASE_PATH_FRONT = "./css/images/cardset-front";
	public static final String BASE_PATH_BACK = "./css/images/cardset-back";
	public static final String DEFAULT_BACK_IMAGE = "default-back.png";
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private TimerTask setChangeTask;
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
	
	/**
	 * Method to generate all available card sets, including the back images
	 * We'll determine where our webapp is deployed to, and work from there
	 * Any valid folders in the BASE_PATH_FRONT directory will be converted
	 *  into GameCardSet objects, named the same as the directory, and with one card
	 *  for every image in that folder
	 * A similar approach is used for the back images, except any image at all in
	 *  BASE_PATH_BACK will be added as a choosable back image
	 */
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
				
				if (log.isInfoEnabled()) {
					log.info("Initialized " + availableSets.size() + " card sets and " + availableBackImages.size() + " back images.");
				}
				
				return;
			}
		}catch (Exception failed) {
			failed.printStackTrace();
		}
		
		availableSets = new Vector<GameCardSet>(0);
	}
	
	/**
	 * Method to take a valid card set directory on the filesystem and
	 *  generate a GameCardSet object from the contents
	 */
	private GameCardSet initCardSet(File setDir) {
		String setName = setDir.getName();
		File[] cardImages = setDir.listFiles(new CardImageFilter());
		
		GameCardSet toReturn = new GameCardSet(setName, cardImages.length);
		
		// Add a GameCard for each image in the directory
		for (int i = 0; i < cardImages.length; i++) {
			toReturn.addCard(new GameCard(i, cardImages[i].getName(),
										  wrapFrontImagePath(setName, cardImages[i].getName()),
										  wrapBackImagePath(DEFAULT_BACK_IMAGE)));
		}
		
		return toReturn;
	}
	
	/**
	 * Method to schedule the checking and possible reloading of card sets and back images
	 * This check looks at the modification date to see if it has changed from the stored
	 *  value, in which case initAvailableSets will be called again
	 */
	private void scheduleCheckSetChanges() {
        // Schedule a task to check for card set directory changes every 5 minutes
        setChangeTask = new TimerTask() {
            public void run() {
            	// If the directory content of available sets has changed
            	//  we'll want to reinitialize the list
                if (checkSetChanges()) {
                	if (log.isInfoEnabled()) {
                		log.info("Card set changes detected by the scheduled task, reinitializing list...");
                	}
                	initAvailableSets();
                }
            }
        };
        
        new Timer("setchanges", true).scheduleAtFixedRate(setChangeTask,
											CONTENT_CHECK_RATE,
											CONTENT_CHECK_RATE);
	}
	
	/**
	 * Method to cancel the schedule checking and possible reloading of card sets and back images
	 */
	public void cancelCheckSetChanges() {
		if (setChangeTask != null) {
			if (log.isInfoEnabled()) {
				log.info("Cancelling the scheduled task to check for card set changes.");
			}
			
			setChangeTask.cancel();
		}
	}
	
	/**
	 * Determine whether the card set folders have been modified
	 */
	private boolean checkSetChanges() {
		// Ensure we have a valid set directory
		if (ValidatorUtil.isValidDirectory(setDir)) {
			// Check whether the modification date has changed
			if (lastModDate != setDir.lastModified()) {
				// Store the recent modification and return 'true' for changes
				lastModDate = setDir.lastModified();
				
				return true;
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
