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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.icefaces.project.memory.bot.BotDifficultyManager;
import com.icefaces.project.memory.game.card.GameCard;
import com.icefaces.project.memory.user.UserSession;
import com.icefaces.project.memory.util.FacesUtil;
import com.icesoft.faces.context.effects.Shake;

/**
 * Page bean for the functionality in board.xhtml
 * This will control a few popups, the shake effect, the current layout, etc.
 */
@ManagedBean(name="boardBean")
@ViewScoped
public class BoardBean {
	public static final String DEFAULT_LAYOUT = "vertical";
	
	private Log log = LogFactory.getLog(this.getClass());
	@ManagedProperty(value = "#{userSession}")
	private UserSession userSession;
	private boolean invitePopup = false;
	private boolean helpPopup = false;
	private String layout = DEFAULT_LAYOUT;
	private Shake shakeEffect = new Shake();
	private String cachedInviteLink = null;
	private String computerDifficulty = BotDifficultyManager.getDefaultDifficultyName();
	private SelectItem[] difficultiesAsItems;
	
	public BoardBean() {
		
	}
	
	public UserSession getUserSession() {
		return userSession;
	}

	@PostConstruct
	protected void init() {
		shakeEffect.setFired(true);
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
	
	public String getComputerDifficulty() {
		return computerDifficulty;
	}

	public void setComputerDifficulty(String computerDifficulty) {
		this.computerDifficulty = computerDifficulty;
	}

	public SelectItem[] getDifficultiesAsItems() {
		if (difficultiesAsItems == null) {
			difficultiesAsItems = BotDifficultyManager.getDifficultiesAsItems();
		}
		
		return difficultiesAsItems;
	}

	public void setDifficultiesAsItems(SelectItem[] difficultiesAsItems) {
		this.difficultiesAsItems = difficultiesAsItems;
	}

	/**
	 * Method called when a user wishes to leave the game
	 */
	public String leaveGame() {
		userSession.leaveCurrentGame();
		
		return "lobby";
	}
	
	public void requestComputerJoin(ActionEvent event) {
		userSession.getGameManager().addComputersToGame(BotDifficultyManager.getDifficultyByName(computerDifficulty),
												        userSession.getCurrentGame());
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

	/**
	 * Method called when the bean is about to be destroyed
	 * This would normally happen when a user closes their browser and we want to do something
	 * In the specific case of our BoardBean this would happen if they quit a game or close their browser
	 *  while inside a game
	 * In this case we'll try to make them leave their current game, so that we don't have
	 *  a bunch of idle users populating games and confusing people
	 */
	@PreDestroy
	public void dispose() {
		if (userSession != null) {
			if (userSession.getCurrentGame() != null) {
				
				if (log.isInfoEnabled()) {
					System.out.println("Disposing of BoardBean for '" + userSession.getName() + "' in game '" + userSession.getCurrentGame() + "'.");
				}
				// Kick the user from their current game
				userSession.leaveCurrentGame();
			}
		}
		else {
			log.warn("Disposing of BoardBean with a null user session.");
		}
	}
}
