package org.icepush.integration.icepushplace.client;

import java.util.List;

import org.icepush.integration.icepushplace.client.model.User;
import org.icepush.integration.icepushplace.shared.ValidatorUtil;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * UI class to create and back our page level code
 * This will manage the login panel and the world panel, plus a local user once they login
 */
public class Page implements EntryPoint, ClosingHandler {
    private final WorldServiceAsync worldService = GWT.create(WorldService.class);
    
    private User ourUser;
    private Panel loginPanel;
    private Panel worldPanel;
    
    public Page() {
    	// Detect when the browser closes a tab or quits, so we can act on that
    	Window.addWindowClosingHandler(this);
    }
    
	/**
	 * Entry point method to generate our necessary UI code
	 * This will default to using the login panel
	 * Our approach is to switch between two panels, the loginPanel and worldPanel, depending on
	 *  what state the application is in
	 */
	public void onModuleLoad() {
		generateLoginPanel();
		generateWorldPanel();
		
		useLoginPanel();
	}
	
	/**
	 * Method to create, setup, and customize the login panel
	 * This will have a few fields a user can fill in to enter the main application
	 */
	private void generateLoginPanel() {
		// Generate the name field
		final TextBox nameField = new TextBox();
		nameField.setText("New User");
		nameField.setFocus(true);
		
		// Generate the mood list
		final ListBox moodList = new ListBox();
		for (int i = 0; i < WorldService.MOODS.length; i++) {
			moodList.addItem(WorldService.MOODS[i]);
		}
		
		// Generate the 'on your mind' area
		final TextArea mindArea = new TextArea();
		mindArea.setCharacterWidth(25);
		mindArea.setVisibleLines(3);
		
		// Generate the region list
		final ListBox regionList = new ListBox();
		for (int i =0 ;i < WorldService.REGIONS.length; i++) {
			regionList.addItem(WorldService.REGIONS[i]);
		}
		
		// Generate the login button
		Button loginButton = new Button("Login");
		
		// Generate the error label
		final Label errorLabel = new Label();
		
		// Add all components to the loginPanel
		loginPanel = new VerticalPanel();
		loginPanel.add(makeField("Nickname:", nameField));
		loginPanel.add(makeField("What mood are you in?:", moodList));
		loginPanel.add(makeField("What's on your mind?:", mindArea));
		loginPanel.add(makeField("Change your region:", regionList));
	    loginPanel.add(loginButton);
	    loginPanel.add(errorLabel);
	    
		// Create a handler for the button action
		class LoginButtonHandler implements ClickHandler, KeyUpHandler {
			public void onClick(ClickEvent event) {
				loginHandler();
			}

			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					loginHandler();
				}
			}

			private void loginHandler() {
				// Attempt to add this user to the world via our remote service
				worldService.addUser(nameField.getText(),
									 moodList.getValue(moodList.getSelectedIndex()),
									 mindArea.getText(),
									 regionList.getValue(regionList.getSelectedIndex()),
					 new AsyncCallback<User>() {
							public void onFailure(Throwable caught) {
								errorLabel.setText(caught.getMessage());
							}

							public void onSuccess(User result) {
								// Store our local user
								ourUser = result;
								
								// Switch to the world panel
								useWorldPanel();
							}
					   });
			}
		}
		
		// Add button action handlers
		LoginButtonHandler handler = new LoginButtonHandler();
		loginButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
	
	/**
	 * Method to create, setup, and customize the world panel
	 * This will have a list of regions / continents and show the different users found in each
	 */
	private void generateWorldPanel() {
		// Create a handler for the Refresh button action
		class RefreshButtonHandler implements ClickHandler, KeyUpHandler {
			public void onClick(ClickEvent event) {
				// Regenerate our world panel (mainly so new users in each region will be added)
				generateWorldPanel();
				useWorldPanel();
			}
			
			public void onKeyUp(KeyUpEvent event) {
			}
		}
		
		// Generate the refresh button
		Button refreshButton = new Button("Refresh");
		refreshButton.addClickHandler(new RefreshButtonHandler());
		
		// Add all the components to the worldPanel
		worldPanel = new VerticalPanel();
		worldPanel.add(refreshButton);
		
		// Add each region as a new panel
		for (String currentRegion : WorldService.REGIONS) {
			worldPanel.add(makeRegionPanel(currentRegion));
		}
	}
	
	/**
	 * Method to make a UI Panel for a single named region
	 * This will look up all users in the region and display their mood image, name, and thoughts
	 * 
	 * @param region to make a Panel for
	 * @return the constructed Panel
	 */
	private Panel makeRegionPanel(String region) {
		final Panel toReturn = new VerticalPanel();
		toReturn.add(makeBoldLabel(region));
		
		// Get a list of users for this region
		worldService.getUsersByRegion(region, new AsyncCallback<List<User>>() {
			public void onFailure(Throwable caught) {
			}
			
			public void onSuccess(List<User> result) {
				// If we retrieved a list of users properly we'll want to add them to our panel
				if (ValidatorUtil.isValidList(result)) {
					Panel userPanel = null;
					
					// For each user we'll add the mood image, name, and thoughts
					for (User currentUser : result) {
						userPanel = new HorizontalPanel();
						userPanel.add(makeMoodImage(currentUser.getMood()));
						userPanel.add(new Label(currentUser.getName()));
						userPanel.add(new Label("'" + currentUser.getMind() + "'"));
						
						toReturn.add(userPanel);
					}
				}
			}
		});
		
		return toReturn;
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
	 * Method to associate a label with a generic widget
	 * This is useful for a lot of form inputs, like "Nickname: [_______]"
	 * 
	 * @param text to use in the label
	 * @param widget to associate
	 * @return the constructed Panel with the label/value pair
	 */
	private Panel makeField(String text, Widget widget) {
		Panel toReturn = new HorizontalPanel();
		
		toReturn.add(new Label(text));
		toReturn.add(widget);
		
		return toReturn;
	}
	
	/**
	 * Method to create a generic bold label
	 * 
	 * @param text to use in the label
	 * @return the constructed bold Label
	 */
	private Label makeBoldLabel(String text) {
		Label toReturn = new Label(text);
		toReturn.setStyleName("boldLabel");
		
		return toReturn;
	}	
	
	/**
	 * Method to switch the page view to the loginPanel
	 */
	private void useLoginPanel() {
		switchPanel("Enter ICEpush Place", loginPanel);
	}
	
	/**
	 * Method to switch the page view to the worldPanel
	 */
	private void useWorldPanel() {
		switchPanel("Welcome to the World", worldPanel);
	}
	
	/**
	 * Generic method to switch the main page view panel
	 * This will clear the headerLabel and panelContainer of child components
	 *  then add the new passed components
	 *  
	 * @param title text to use in the header label
	 * @param panel to switch to
	 */
	private void switchPanel(String title, Panel panel) {
		// Reset the header
		RootPanel.get("headerLabel").clear();
		RootPanel.get("headerLabel").add(new Label(title));
		
		// Reset the main panel
		RootPanel.get("panelContainer").clear();
		RootPanel.get("panelContainer").add(panel);
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
				ourUser = null;
			}
		});
	}
}
