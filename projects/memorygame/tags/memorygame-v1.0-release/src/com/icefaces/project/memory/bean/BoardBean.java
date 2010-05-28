package com.icefaces.project.memory.bean;

import javax.faces.event.ActionEvent;

import com.icesoft.faces.context.effects.Shake;
import com.icefaces.project.memory.game.card.GameCard;
import com.icefaces.project.memory.user.UserSession;
import com.icefaces.project.memory.util.FacesUtil;

/**
 * Page bean for the functionality in board.xhtml
 * This will control a few popups, the shake effect, the current layout, etc.
 */
public class BoardBean {
	public static final String DEFAULT_LAYOUT = "vertical";
	
	private UserSession userSession;
	private boolean invitePopup = false;
	private boolean helpPopup = false;
	private String layout = DEFAULT_LAYOUT;
	private Shake shakeEffect = new Shake();
	private String cachedInviteLink = null;
	
	public BoardBean() {
		init();
	}
	
	protected void init() {
		shakeEffect.setFired(true);
	}
	
	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}
	
	public boolean isInvitePopup() {
		return invitePopup;
	}

	public void setInvitePopup(boolean invitePopup) {
		this.invitePopup = invitePopup;
	}
	
	public boolean isHelpPopup() {
		return helpPopup;
	}

	public void setHelpPopup(boolean helpPopup) {
		this.helpPopup = helpPopup;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}
	
	public Shake getShakeEffect() {
		// If we're supposed to be shaking then prep the effect to be fired
		if (userSession.getIsShaking()) {
			shakeEffect.setFired(false);
			userSession.setIsShaking(false);
		}
		
		return shakeEffect;
	}

	public void setShakeEffect(Shake shakeEffect) {
		this.shakeEffect = shakeEffect;
	}

	/**
	 * Method called when a user wishes to leave the game
	 * We'll reset some renderers, drop from the game itself, and rejoin the lobby
	 */
	public String leaveGame() {
		userSession.getGameManager().leaveGame(userSession, userSession.getCurrentGame());
		
		userSession.getRenderer().leaveGame(userSession.getCurrentGame().getName());
		userSession.getRenderer().joinLobby();
		
		userSession.setCurrentGame(null);
		
		return "lobby";
	}
	
	/**
	 * Method called when a user wishes to shake the window
	 */
	public void shakeWindow(ActionEvent event) {
		userSession.getCurrentGame().requestShake(userSession.getName());
	}
	
	public void openInvite(ActionEvent event) {
		setInvitePopup(true);
	}
	
	public void closeInvite(ActionEvent event) {
		setInvitePopup(false);
	}
	
	public void openHelp(ActionEvent event) {
		setHelpPopup(true);
	}
	
	public void closeHelp(ActionEvent event) {
		setHelpPopup(false);
	}	
	
	public String getInviteLink() {
		if (cachedInviteLink == null) {
			cachedInviteLink =
				RedirectBean.generateInviteURL(userSession.getCurrentGame().getName(),
				  						       userSession.getCurrentGame().getPassword(),
											   "Remote User");
		}
		
		return cachedInviteLink;
	}
	
	/**
	 * Method called when a user clicks a card to flip it
	 * We'll basically get what card was clicked based on the f:attribute, and then pass this
	 *  on to the user session to handle for us 
	 */
	public void flipCard(ActionEvent event) {
		// Flip the clicked card
		GameCard clickedCard = (GameCard)FacesUtil.getFAttribute(event, "clickedCard");
		
		if (!clickedCard.getIsFlipped()) {
			userSession.getCurrentGame().performTurn(clickedCard);
		}
	}
}
