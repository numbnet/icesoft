package org.icepush.integration.icepushplace.client;

import java.util.List;

import org.icepush.integration.icepushplace.client.model.User;
import org.icepush.integration.icepushplace.shared.UIUtil;
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

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Page implements EntryPoint, ClosingHandler {
    private final WorldServiceAsync worldService = GWT.create(WorldService.class);
    
    private String ourName;
    private Panel loginPanel;
    private Panel worldPanel;
    
    public Page() {
    	Window.addWindowClosingHandler(this);
    }
    
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		generateLoginPanel();
		generateWorldPanel();
		
		useLoginPanel();
	}
	
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
		loginPanel.add(UIUtil.makeField("Nickname:", nameField));
		loginPanel.add(UIUtil.makeField("What mood are you in?:", moodList));
		loginPanel.add(UIUtil.makeField("What's on your mind?:", mindArea));
		loginPanel.add(UIUtil.makeField("Change your region:", regionList));
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
				worldService.addUser(
					 new User(nameField.getText(),
							  moodList.getValue(moodList.getSelectedIndex()),
							  mindArea.getText(),
							  regionList.getValue(regionList.getSelectedIndex())),
					 new AsyncCallback<Boolean>() {
							public void onFailure(Throwable caught) {
								errorLabel.setText(caught.getMessage());
							}

							public void onSuccess(Boolean result) {
								ourName = nameField.getText();
								
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
	
	private void generateWorldPanel() {
		// Create a handler for the Refresh button action
		class RefreshButtonHandler implements ClickHandler, KeyUpHandler {
			public void onClick(ClickEvent event) {
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
	
	private Panel makeRegionPanel(String region) {
		final Panel toReturn = new VerticalPanel();
		toReturn.add(UIUtil.makeBoldLabel(region));
		
		worldService.getUsersByRegion(region, new AsyncCallback<List<User>>() {
			public void onFailure(Throwable caught) {
			}
			
			public void onSuccess(List<User> result) {
				if (ValidatorUtil.isValidList(result)) {
					Panel userPanel = null;
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
	
	private Image makeMoodImage(String mood) {
		Image toReturn = new Image(WorldService.MOOD_IMAGE_PREFIX +
								   mood +
								   WorldService.MOOD_IMAGE_SUFFIX);
		toReturn.setPixelSize(26, 29);
		
		return toReturn;
	}
	
	private void useLoginPanel() {
		switchPanel("Enter ICEpush Place", loginPanel);
	}
	
	private void useWorldPanel() {
		switchPanel("Welcome to the World", worldPanel);
	}
	
	private void switchPanel(String title, Panel panel) {
		RootPanel.get("headerLabel").clear();
		RootPanel.get("headerLabel").add(new Label(title));
		
		RootPanel.get("panelContainer").clear();
		RootPanel.get("panelContainer").add(panel);
	}

	@Override
	public void onWindowClosing(ClosingEvent event) {
		worldService.removeUser(ourName, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
			}
			
			public void onSuccess(Boolean result) {
				ourName = null;
			}
		});
	}
}
