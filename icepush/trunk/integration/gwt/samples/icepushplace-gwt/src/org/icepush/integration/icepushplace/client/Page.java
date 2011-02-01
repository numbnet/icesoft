/**
 *
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 *
*/
package org.icepush.integration.icepushplace.client;

import java.util.HashMap;
import java.util.List;

import org.icepush.gwt.client.GWTPushContext;
import org.icepush.gwt.client.PushEventListener;
import org.icepush.integration.icepushplace.client.model.User;
import org.icepush.integration.icepushplace.shared.ValidatorUtil;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * UI class to create and back our page level code
 * This will manage the login panel and the world panel, plus a local user once they login
 */
public class Page implements EntryPoint, ClosingHandler {
	public static final String PAGEID_HEADER = "headerLabel";
	public static final String PAGEID_PANEL = "panelContainer";
	
    private final WorldServiceAsync worldService = GWT.create(WorldService.class);
    
    private String[] regions;
    private User ourUser;
    
    private Widget inputSet;
    
    private Panel worldPanel;
    private HashMap<String,Panel> regionPanels;
    
    private Button loginButton;
    private Button updateButton;
    private Label errorLabel;
    
    private TextBox nameField;
    private TextBox mindField;
    private TextBox messageField;
    private ListBox moodList;
    private ListBox regionList;
    
    private PushEventListener pushListener;
    private String entryPushGroup;
    
    public Page() {
    	// Detect when the browser closes a tab or quits, so we can act on that
    	Window.addWindowClosingHandler(this);
    }
    
    /**
     * Simple method to check if two strings are not null and equal each other
     *
     * @param one string to check
     * @param two string to check
     * @return true if the strings match, false otherwise 
     */
	private boolean doStringsMatch(String one, String two) {
	    if ((one != null) && (two != null)) {
	        return one.equals(two);
	    }
	    else {
	        return one == two;
	    }
	}    
    
	/**
	 * Entry point method to generate our necessary UI code
	 * This will default to using the login panel
	 * Our approach is to switch between two panels, the loginPanel and worldPanel, depending on
	 *  what state the application is in
	 */
	public void onModuleLoad() {
		// Add a push listener to refresh when we hear an event
		pushListener = new PushEventListener() {
			public void onPushEvent() {
				refreshWorld();
		    }
	    };
	    entryPushGroup = "login" + Random.nextInt(); // Try to keep the group name unique
	    GWTPushContext.getInstance().addPushEventListener(pushListener, entryPushGroup);
		
		worldService.getAllRegions(new AsyncCallback<String[]>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(String[] result) {
				// Store our list of regions
				regions = result;
				
				// Create the map of region panels
				regionPanels = new HashMap<String,Panel>(regions.length);
				
				// Start with the login panel
				useLoginPanel();
			}
		});
	}
	
	private Widget generateInputSet() {
		// Generate the name field
		if (nameField == null) {
			nameField = new TextBox();
			nameField.setMaxLength(15);
			nameField.setStyleName("nameInput");
			nameField.setFocus(true);
			nameField.setText("New User");
		}
		
		// Generate the mood list
		if (moodList == null) {
			moodList = new ListBox();
			moodList.setStyleName("moodInput");
			
			for (int i = 0; i < WorldService.MOODS.length; i++) {
				moodList.addItem(WorldService.MOODS[i]);
			}
		}
		
		// Generate the 'on your mind' area
		if (mindField == null) {
			mindField = new TextBox();
			mindField.setMaxLength(150);
			mindField.setStyleName("thoughtInput");
		}
		
		// Generate the message field
		if (messageField == null) {
			messageField = new TextBox();
			messageField.setMaxLength(150);
			messageField.setStyleName("messageInput");
		}
		
		// Generate the region list
		if (regionList == null) {
			regionList = new ListBox();
			regionList.setStyleName("regionInput");
			
			for (int i = 0; i < regions.length; i++) {
				regionList.addItem(regions[i]);
			}
		}
		
		// Add all components to the returned grid
		Grid toReturn = new Grid(5, 2);
		toReturn.setStyleName("inputSet");
		toReturn.setWidget(0, 0, makeStyleLabel("Nickname:", "nameLabel"));		toReturn.setWidget(0, 1, nameField);
		toReturn.setWidget(1, 0, makeStyleLabel("Mood:", "moodLabel"));			toReturn.setWidget(1, 1, moodList);
		toReturn.setWidget(2, 0, makeStyleLabel("Thoughts:", "thoughtLabel"));	toReturn.setWidget(2, 1, mindField);
		toReturn.setWidget(3, 0, makeStyleLabel("Message:", "messageLabel"));	toReturn.setWidget(3, 1, messageField);
		toReturn.setWidget(4, 0, makeStyleLabel("Region:", "regionLabel"));		toReturn.setWidget(4, 1, regionList);
		
		return toReturn;
	}
	
	/**
	 * Method to make a new UI Panel for a single named region
	 * This will look up all users in the region and display their mood image, name, and thoughts
	 * 
	 * @param region to make a Panel for
	 * @return the constructed Panel
	 */
	private Panel makeRegionPanel(String region) {
		return makeRegionPanel(region, new VerticalPanel());
	}

	/**
	 * Method to clear and repopulate an existing UI Panel for a single named region
	 * This will look up all users in the region and display their mood image, name, and thoughts
	 * 
	 * @param region to make a Panel for
	 * @param base to rebuild the contents for
	 * @return the constructed Panel
	 */
	private Panel makeRegionPanel(String region, final Panel base) {
		base.clear();
		base.setWidth("100%");
		
		Panel labelPanel = new FlowPanel();
		labelPanel.setWidth("100%");
		labelPanel.setStyleName("regionPanel");
		
		labelPanel.add(makeStyleLabel(region, "regionHeader"));
		
		base.add(labelPanel);
		
		// Get a list of users for this region
		worldService.getUsersByRegion(region, new AsyncCallback<List<User>>() {
			public void onFailure(Throwable caught) {
			}
			
			public void onSuccess(List<User> result) {
				FlexTable userTable = null;
				
				// If we retrieved a list of users properly we'll want to add them to our panel
				if (ValidatorUtil.isValidList(result)) {
					
					// For each user we'll add the mood image, name, thought, and message
					for (User currentUser : result) {
						userTable = new FlexTable();
						userTable.setWidget(0, 0, makeMoodImage(currentUser.getMood()));
						userTable.setWidget(0, 1, new Label(currentUser.getName()));
						if ((currentUser.getTechnology() != null) && (currentUser.getTechnology().trim().length() > 0)) {
							userTable.setWidget(0, userTable.getCellCount(0),
												new Label("using " + currentUser.getTechnology()));
						}
						if ((currentUser.getMind() != null) && (currentUser.getMind().trim().length() > 0)) {
							userTable.setWidget(0, userTable.getCellCount(0),
											   new Label("thinks '" + currentUser.getMind() + "'"));
						}
						if ((currentUser.getMessage() != null) && (currentUser.getMessage().trim().length() > 0)) {
							if ((currentUser.getMind() != null) && (currentUser.getMind().trim().length() > 0)) {
								userTable.setWidget(0, userTable.getCellCount(0),
										           new Label("and"));
							}
							userTable.setWidget(0, userTable.getCellCount(0),
									           new Label("says '" + currentUser.getMessage() + "'"));
						}
						
						// Set all cells to have a vertical align of middle
						FlexCellFormatter format = userTable.getFlexCellFormatter();
						for (int i = 0, max = userTable.getCellCount(0); i < max; i++) {
						    format.setVerticalAlignment(0, i, HasVerticalAlignment.ALIGN_MIDDLE);
						}
						
						base.add(userTable);
					}
				}
				else {
					userTable = new FlexTable();
					userTable.setWidget(0, 0, new Label("No users on this continent."));
					
					base.add(userTable);
				}
			}
		});
		
		return base;
	}
	
	/**
	 * Method to take a user's mood and create a UI Image from it with the proper path and extension
	 * This assumes the mood matches an image available in the "images" folder
	 * And that the image name is similar to "mood-sad.png"
	 * 
	 * @param mood to make an image from
	 * @return the constructed Image
	 */
	private Image makeMoodImage(String mood) {
		Image toReturn = new Image(WorldService.MOOD_IMAGE_PREFIX +
								   mood +
								   WorldService.MOOD_IMAGE_SUFFIX);
		toReturn.setPixelSize(26, 29);
		
		return toReturn;
	}
	
	/**
	 * Method to associate the passed style class with a newly created label
	 * 
	 * @param text of the label
	 * @param style of the label
	 * @return created label
	 */
	private Label makeStyleLabel(String text, String style) {
		Label toReturn = new Label(text);
		toReturn.setStyleName(style);
		
		return toReturn;
	}
	
	/**
	 * Method to determine what index should be selected in a ListBox
	 * This will look at all available items and find one that matches the passed toSelect
	 * 
	 * @param component to select an index for
	 * @param toSelect to try to find in the component
	 */
	private void selectListBox(ListBox component, String toSelect) {
	    String currentText = null;
	    for (int i = 0; i < component.getItemCount(); i++) {
	    	currentText = component.getItemText(i);
	    	
	    	if (currentText.equals(toSelect)) {
	    		component.setSelectedIndex(i);
	    		return;
	    	}
	    }
	}
	
	/**
	 * Method to switch the page view to the loginPanel
	 */
	private void useLoginPanel() {
		if (loginButton == null) {
			loginButton = new Button("Login");
			loginButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					loginHandler();
				}
			});
		}
		
		switchPanel(WorldService.OUR_TECHNOLOGY + " - ICEpush Place Login", null, loginButton);
	}
	
	/**
	 * Method to switch the page view to the worldPanel
	 */
	private void useWorldPanel() {
		// Generate the world panel as needed
		if (worldPanel == null) {
			worldPanel = new VerticalPanel();
			worldPanel.setStyleName("worldPanel");
			
			// Add each region as a new panel
			for (String currentRegion : regions) {
				regionPanels.put(currentRegion, makeRegionPanel(currentRegion));
				
				worldPanel.add(regionPanels.get(currentRegion));
			}
		}
		
		// Generate the update button as needed
		if (updateButton == null) {
			updateButton = new Button("Update");
			updateButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					updateHandler();
				}
			});
		}
		
		switchPanel(WorldService.OUR_TECHNOLOGY + " - ICEpush Place View", worldPanel, updateButton);
	}
	
	/**
	 * Method to rebuild a single region panel, so that the users in it will be refreshed
	 * 
	 * @param region to refresh
	 */
	public void refreshRegion(String region) {
		Panel toRefresh = regionPanels.get(region);
		
		// Either rebuild the panel contents or the entire world
		if (toRefresh != null) {
			makeRegionPanel(region, toRefresh);
		}
		else {
			refreshWorld();
		}
	}
	
	/**
	 * Method to rebuild and redisplay the worldPanel
	 */
	public void refreshWorld() {
		// Nullify the worldPanel so it will be rebuilt via useWorldPanel
		worldPanel = null;
		
		useWorldPanel();
	}
	
	/**
	 * Generic method to switch the main page view panel
	 * This will clear the headerLabel and panelContainer of child components
	 *  then add the new passed components
	 *  
	 * @param title text to use in the header label
	 * @param panel to switch to
	 */
	private void switchPanel(String title, Panel panel, Button button) {
		// Reset the header
		RootPanel.get(PAGEID_HEADER).clear();
		RootPanel.get(PAGEID_HEADER).add(new Label(title));
		
		// Generate input and error components as needed
		if (inputSet == null) {
			inputSet = generateInputSet();
		}
		if (errorLabel == null) {
			errorLabel = new Label();
		}
		
		// Set the inputSet fields if we have an ourUser object
	    if (ourUser != null) {
		    nameField.setText(ourUser.getName());
		    mindField.setText(ourUser.getMind());
		    messageField.setText(ourUser.getMessage());
		    selectListBox(moodList, ourUser.getMood());
		    selectListBox(regionList, ourUser.getRegion());
	    }
		
		// Reset the main panel
		RootPanel.get(PAGEID_PANEL).clear();
		RootPanel.get(PAGEID_PANEL).add(inputSet);
		if (button != null) {
			RootPanel.get(PAGEID_PANEL).add(button);
		}
		RootPanel.get(PAGEID_PANEL).add(errorLabel);
		if (panel != null) {
			RootPanel.get(PAGEID_PANEL).add(panel);
		}
	}
	
	private void loginHandler() {
		// Attempt to add this user to the world via our remote service
		worldService.addUser(nameField.getText(),
							 moodList.getValue(moodList.getSelectedIndex()),
							 mindField.getText(),
							 regionList.getValue(regionList.getSelectedIndex()),
							 messageField.getText(),
			 new AsyncCallback<User>() {
				public void onFailure(Throwable caught) {
					errorLabel.setText(caught.getMessage());
				}

				public void onSuccess(User result) {
					// Store our local user
					ourUser = result;
					
					// Switch to the world panel
					useWorldPanel();
					
					// Push to update the page to show the world panel
					GWTPushContext.getInstance().push(entryPushGroup);
					
					// Add a push listener for every region
					for (int i = 0; i < regions.length; i++) {
						final String currentRegion = regions[i];
						
						// Make the push listener refresh just the region upon event capture
						pushListener = new PushEventListener() {
							public void onPushEvent() {
								refreshRegion(currentRegion);
						    }
					    };
					    GWTPushContext.getInstance().addPushEventListener(pushListener, currentRegion);
					}
				}
		});
	}
	
	private void updateHandler() {
		// Store our old region so we can update it during a move
		final String oldRegion = ourUser.getRegion();
		
		// Determine if we need to update the user (necessary when a field besides region changes)
		boolean needUpdate = false;
		
		/*
		 * Check the name, mood, mind, and message field for changes
		 * If any of the fields has changed, we'll need to toggle 'needUpdate'
		 * We'll also set the changed value into our user object
		 */
		if (!doStringsMatch(ourUser.getName(), nameField.getText())) {
		    ourUser.setName(nameField.getText());
		    if (!needUpdate) {
		        needUpdate = true;
		    }
		}
		
		if (!doStringsMatch(ourUser.getMood(), moodList.getValue(moodList.getSelectedIndex()))) {
		    ourUser.setMood(moodList.getValue(moodList.getSelectedIndex()));
		    if (!needUpdate) {
		        needUpdate = true;
		    }
		}
		
		if (!doStringsMatch(ourUser.getMind(), mindField.getText())) {
		    ourUser.setMind(mindField.getText());
		    if (!needUpdate) {
		        needUpdate = true;
		    }
		}
		
		if (!doStringsMatch(ourUser.getMessage(), messageField.getText())) {
		    ourUser.setMessage(messageField.getText());
		    if (!needUpdate) {
		        needUpdate = true;
            }
		}
		
        // Set the region, but we don't need an update on change, since we'll move the person anyways as needed
        ourUser.setRegion(regionList.getValue(regionList.getSelectedIndex()));
		
		// Call our service to try to move/update the user
		worldService.smartUpdateUser(needUpdate, oldRegion, ourUser, new AsyncCallback<User>() {
			public void onFailure(Throwable caught) {
				errorLabel.setText(caught.getMessage());
			}

			public void onSuccess(User result) {
				// Update our user if needed
				if (result != null) {
					ourUser = result;
				}
				
				// Update the region our user is currently in
				GWTPushContext.getInstance().push(ourUser.getRegion());
				
				// Also update our old region if we have a different one
				if (!ourUser.getRegion().equals(oldRegion)) {
					GWTPushContext.getInstance().push(oldRegion);
				}
			}
		});
	}
	
	/**
	 * Method called when the window is closing, such as a browser tab exiting
	 * Our goal here is to try to log "stale" users out of the application
	 *
	 *@param event of the close
	 */
	@Override
	public void onWindowClosing(ClosingEvent event) {
		// Attempt to remove our user from the world
		worldService.removeUser(ourUser, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
			}
			
			public void onSuccess(Boolean result) {
				if (pushListener != null) {
					// Try to remove our old listener for a different continent
					GWTPushContext.getInstance().removePushEventListenerFromGroup(pushListener, ourUser.getRegion());
				}
				
				ourUser = null;
			}
		});
	}
}
