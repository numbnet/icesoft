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

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import com.icefaces.project.memory.exception.FailedJoinException;
import com.icefaces.project.memory.game.GameInstance;
import com.icefaces.project.memory.user.UserSession;
import com.icefaces.project.memory.util.FacesUtil;
import com.icefaces.project.memory.util.ValidatorUtil;

/**
 * Bean used to check whether a specific game should be joined based
 *  on the redirection parameters
 * This allows users in a game to send a url invite to friends so
 *  the friends can instantly join their exact game without having
 *  to go through the lobby
 */
@ManagedBean(name="redirectBean")
@RequestScoped
public class RedirectBean {
	public static final String BASE_URL_PARAM = "invite.url.base";
	public static final String GAME_NAME_PARAM = "game";
	public static final String GAME_PASS_PARAM = "password";
	public static final String USER_NAME_PARAM = "user";
	private static final String REDIRECT_URI = "redirect.iface";
	private static final String BOARD_URI = "./board.iface";
	private static final String LOBBY_URI = "./lobby.iface";
	
	@ManagedProperty(value = "#{userSession}")
	private UserSession userSession;
	private boolean hasChecked = false;
	
	public RedirectBean() {
	}
	
	public String getCheck() {
		if (!hasChecked) {
			hasChecked = true;
			
			checkForRedirect();
		}
		
		return null;
	}
	
	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	public static String getGameNameParam() {
		return FacesUtil.getURLParameter(GAME_NAME_PARAM);
	}

	public static String getGamePassParam() {
		return FacesUtil.getURLParameter(GAME_PASS_PARAM);
	}

	public static String getUserNameParam() {
		return FacesUtil.getURLParameter(USER_NAME_PARAM);
	}

	/**
	 * Method to check whether we need to redirect to a game
	 * Basically we'll check the url parameters for validity, then join
	 *  the user into an existing game if possible, otherwise they go to the lobby  
	 */
	private boolean checkForRedirect() {
		String gameName = getGameNameParam();
		String gamePass = getGamePassParam();
		String userName = getUserNameParam();
		
		if (ValidatorUtil.isValidString(gameName)) {
			try{
				if (joinGame(gameName, gamePass, userName)) {
					redirectToBoard();
					
					return true;
				}
			}catch (FailedJoinException failedJoin) {
				failedJoin.printStackTrace();
			}
		}
		
		// Fallback to redirecting to the lobby
		redirectToLobby();
		
		return false;
	}
	
	/**
	 * Method to join an existing game directly, without entering the lobby
	 * This requires a bit of work to skip over some steps that would have been done
	 *  via the lobby, like setting a name and switching renderers, etc.
	 */
	private boolean joinGame(String gameName, String gamePass, String userName) throws FailedJoinException {
		if ((userSession == null) || (userSession.getGameManager() == null)) {
			return false;
		}
		
		List<GameInstance> gameList = userSession.getGameManager().getGameList();
		
		// Try to find a game that matches the one we're trying to join
		for (GameInstance loopGame : gameList) {
			if (gameName.equalsIgnoreCase(loopGame.getName())) {
				if (ValidatorUtil.isValidString(userName)) {
					userSession.setName(userName);
				}
				
				userSession.getGameManager().joinGame(
						userSession, loopGame, gamePass);
				userSession.setCurrentGame(loopGame);
				userSession.getRenderer().leaveLobby();
				userSession.getRenderer().joinGame(gameName);
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Method to generate the parameterized url that is sent to friends to join a game directly
	 */
	public static String generateInviteURL(String gameName, String gamePass, String userName) {
		String toReturn = FacesUtil.getContextParameter(BASE_URL_PARAM) + REDIRECT_URI +
   		 				  "?" + GAME_NAME_PARAM + "=" + gameName +
   		 				  "&" + USER_NAME_PARAM + "=" + userName;

		if (ValidatorUtil.isValidString(gamePass)) {
			toReturn += "&" + RedirectBean.GAME_PASS_PARAM + "=" + gamePass;
		}
		
		return toReturn;
	}
	
	public static void redirectToBoard() {
		FacesUtil.refreshBrowser(BOARD_URI);
	}
	
	public static void redirectToLobby() {
		FacesUtil.refreshBrowser(LOBBY_URI);
	}
}
