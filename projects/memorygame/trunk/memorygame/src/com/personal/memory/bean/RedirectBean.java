package com.personal.memory.bean;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.personal.memory.exception.FailedJoinException;
import com.personal.memory.game.GameInstance;
import com.personal.memory.user.UserSession;
import com.personal.memory.util.FacesUtil;
import com.personal.memory.util.ValidatorUtil;

public class RedirectBean {
	public static final String BASE_URL_PARAM = "invite.url.base";
	public static final String GAME_NAME_PARAM = "game";
	public static final String GAME_PASS_PARAM = "password";
	public static final String USER_NAME_PARAM = "user";
	private static final String REDIRECT_URI = "redirect.iface";
	private static final String BOARD_URI = "board.iface";
	private static final String LOBBY_URI = "lobby.iface";
	
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

	private boolean checkForRedirect() {
		String gameName = getGameNameParam();
		String gamePass = getGamePassParam();
		String userName = getUserNameParam();
		
		if (ValidatorUtil.isValidString(gameName)) {
			try{
				if (joinGame(gameName, gamePass, userName)) {
					RedirectBean.forceLoadURI("./" + BOARD_URI);
					
					return true;
				}
			}catch (FailedJoinException failedJoin) {
				failedJoin.printStackTrace();
			}
		}
		
		// Fallback to redirecting to the lobby
		RedirectBean.forceLoadURI("./" + LOBBY_URI);
		
		return false;
	}
	
	private boolean joinGame(String gameName, String gamePass, String userName) throws FailedJoinException {
		if ((userSession == null) || (userSession.getGameManager() == null)) {
			return false;
		}
		
		List<GameInstance> gameList = userSession.getGameManager().getGameList();
		
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
	
	public static String generateInviteURL(String gameName, String gamePass, String userName) {
		String toReturn = FacesUtil.getContextParameter(BASE_URL_PARAM) + REDIRECT_URI +
   		 				  "?" + GAME_NAME_PARAM + "=" + gameName +
   		 				  "&" + USER_NAME_PARAM + "=" + userName;

		if (ValidatorUtil.isValidString(gamePass)) {
			toReturn += "&" + RedirectBean.GAME_PASS_PARAM + "=" + gamePass;
		}
		
		return toReturn;
	}
	
    public static void forceLoadURI(String url) {
    	HttpServletResponse response = FacesUtil.getCurrentResponse();
    	
        if (response != null) {
            response.setHeader("Refresh", "0; URL=" + response.encodeRedirectURL(url));
        }
    }
}
