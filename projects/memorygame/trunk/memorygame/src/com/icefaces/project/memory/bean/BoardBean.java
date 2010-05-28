package com.icefaces.project.memory.bean;

import javax.faces.event.ActionEvent;

import com.icesoft.faces.context.effects.Shake;
import com.icefaces.project.memory.game.card.GameCard;
import com.icefaces.project.memory.user.UserSession;
import com.icefaces.project.memory.util.FacesUtil;

public class BoardBean {
	public static final String DEFAULT_LAYOUT = "vertical";
	
	private UserSession userSession;
	private boolean invitePopup = false;
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
	
	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}
	
	public Shake getShakeEffect() {
		if (userSession.getIsShaking()) {
			shakeEffect.setFired(false);
			userSession.setIsShaking(false);
		}
		
		return shakeEffect;
	}

	public void setShakeEffect(Shake shakeEffect) {
		this.shakeEffect = shakeEffect;
	}

	public String leaveGame() {
		userSession.getGameManager().leaveGame(userSession, userSession.getCurrentGame());
		
		userSession.getRenderer().leaveGame(userSession.getCurrentGame().getName());
		userSession.getRenderer().joinLobby();
		
		userSession.setCurrentGame(null);
		
		return "lobby";
	}
	
	public void shakeWindow(ActionEvent event) {
		userSession.getCurrentGame().requestShake(userSession.getName());
	}
	
	public void openInvite(ActionEvent event) {
		setInvitePopup(true);
	}
	
	public void closeInvite(ActionEvent event) {
		setInvitePopup(false);
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
	
	public void flipCard(ActionEvent event) {
		// Flip the clicked card
		GameCard clickedCard = (GameCard)FacesUtil.getFAttribute(event, "clickedCard");
		
		if (!clickedCard.getIsFlipped()) {
			userSession.getCurrentGame().performTurn(clickedCard);
		}
	}
}
