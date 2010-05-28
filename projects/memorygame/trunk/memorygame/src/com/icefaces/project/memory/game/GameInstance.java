package com.icefaces.project.memory.game;

import java.util.ArrayList;
import java.util.Vector;
import java.util.List;

import com.icesoft.faces.async.render.SessionRenderer;
import com.icefaces.project.memory.comparator.UserByScoreComparator;
import com.icefaces.project.memory.exception.ThreadRunningException;
import com.icefaces.project.memory.game.card.GameCard;
import com.icefaces.project.memory.game.card.GameCardSet;
import com.icefaces.project.memory.game.chat.GameChat;
import com.icefaces.project.memory.user.UserModel;
import com.icefaces.project.memory.user.UserSession;
import com.icefaces.project.memory.util.ValidatorUtil;

import edu.emory.mathcs.backport.java.util.Collections;

public class GameInstance {
	protected String name;
	protected String password;
	protected List<UserModel> users;
	protected List<UserModel> usersByScore;
	protected int maxUsers = GameManager.DEFAULT_MAX_USERS;
	protected long reflipDelay = GameManager.DEFAULT_REFLIP_DELAY;
	protected boolean isStarted = false;
	private String winnerMessage;
	private GameBoard board;
	private GameChat chat;
	private GameTurns turns;
	
	public GameInstance() {
		this(null, null, GameManager.DEFAULT_SIZE,
			 new GameCardSet(null, 0),
			 new ArrayList<UserModel>(0),
			 GameManager.DEFAULT_MAX_USERS,
			 GameManager.DEFAULT_REFLIP_DELAY,
			 false);
	}
	
	public GameInstance(GameInstance clone) {
		this(clone.getName(),
		     clone.getPassword(),
		     clone.getBoard().getSize(),
		     clone.getBoard().getCardSet(),
		     clone.getUsers(),
		     clone.getMaxUsers(),
		     clone.getReflipDelay(),
		     clone.getIsStarted());
	}
	
	public GameInstance(String name, String password, int size,
			GameCardSet cardSet, List<UserModel> users,
			int maxUsers, long reflipDelay, boolean isStarted) {
		this.name = name;
		this.password = password;
		this.users = users;
		this.maxUsers = maxUsers;
		this.reflipDelay = reflipDelay;
		this.isStarted = isStarted;
		
		board = new GameBoard(size, cardSet);
		chat = new GameChat(this.name);
		turns = new GameTurns(this.name,
							  this.reflipDelay,
							  this.users);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<UserModel> getUsers() {
		return users;
	}
	public void setUsers(List<UserModel> users) {
		this.users = users;
	}
	public int getMaxUsers() {
		return maxUsers;
	}
	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}
	public long getReflipDelay() {
		return reflipDelay;
	}
	public void setReflipDelay(long reflipDelay) {
		this.reflipDelay = reflipDelay;
	}
	public boolean getIsStarted() {
		return isStarted;
	}
	public void setIsStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}
	public boolean getHasWinner() {
		return getWinnerMessage() != null;
	}
	public String getWinnerMessage() {
		return winnerMessage;
	}
	public void setWinnerMessage(String winnerMessage) {
		this.winnerMessage = winnerMessage;
	}
	public GameBoard getBoard() {
		return board;
	}
	public void setBoard(GameBoard board) {
		this.board = board;
	}
	public GameChat getChat() {
		return chat;
	}
	public void setChat(GameChat chat) {
		this.chat = chat;
	}
	public GameTurns getTurns() {
		return turns;
	}
	public void setTurns(GameTurns turns) {
		this.turns = turns;
	}
	public List<UserModel> getUsersByScore() {
		// Don't bother comparing unless there is more than 1 user
		if (getUsers().size() <= 1) {
			return users;
		}
		
		if ((usersByScore == null) ||
			(usersByScore.size() != getUsers().size())) {
			usersByScore = new Vector<UserModel>(users.size());
			usersByScore.addAll(users);
 
			Collections.sort(usersByScore, new UserByScoreComparator());
		}
		
		return usersByScore;
	}
	public void setUsersByScore(List<UserModel> usersByScore) {
		this.usersByScore = usersByScore;
	}

	public void renderGame() {
		if (name != null) {
			SessionRenderer.render(name);
		}
	}
	
	public boolean getHasPassword() {
		return ValidatorUtil.isValidString(password);
	}
	
	public boolean getHasUsers() {
		return getUserCount() > 0;
	}
	
	public boolean getHasUser(UserModel user) {
		if (getHasUsers()) {
			for (UserModel loopUser : users) {
				if (loopUser == user) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean getIsFull() {
		return getUserCount() >= getMaxUsers();
	}
	
	public boolean getHasSpace() {
		return (getUserCount()+1) <= getMaxUsers();
	}
	
	public int getEmptySpace() {
		return getMaxUsers() - getUserCount();
	}
	
	public int getUserCount() {
		if (users == null) {
			return 0;
		}
		
		return users.size();
	}
	
	public void resetScores() {
		setAllScores(0);
	}
	
	public void setAllScores(int newScore) {
		if (ValidatorUtil.isValidList(users)) {
			for (UserModel loopUser : users) {
				loopUser.setScore(newScore);
			}
		}
	}
	
	public void requestShake(String fromWho) {
		for (UserModel loopUser : users) {
			((UserSession)loopUser).setIsShaking(true);
		}
		
		// Notify the other users who the shaker was
		chat.addShakeMessage(fromWho);
		
		// Update the game to apply the shake effect
		renderGame();
	}
	
	public void addUser(UserModel user) {
		users.add(user);
		
		// Put in a notification to the chat log
		chat.addEnterMessage(user.getName());
		
		// Update the game to let everyone know a new user joined
		renderGame();
	}
	
	public void removeUser(UserModel user) {
		users.remove(user);
		
		// Put in a notification to the chat log
		chat.addExitMessage(user.getName());
		
		// Update the game to let everyone know a user left
		renderGame();
	}
	
	public void performTurn(GameCard toFlip) {
		try{
			UserModel matcher = turns.handleTurn(toFlip);
			
			if (matcher != null) {
				matcher.incrementScore();
				
				// Reset the cached user score list so it gets resorted
				setUsersByScore(null);
				
				// Check if this last match won the round
				if (board.isGameDone()) {
					someoneWon();
				}
			}
			
			renderGame();
		}catch (ThreadRunningException ignoreAction) {
			// If we get this exception it means the user was trying to
			//  click around while we waited to reflip our cards
			// So basically ignore them
		}
	}
	
	private void someoneWon() {
		UserModel winningUser = getUsersByScore().get(0);
		
		String message = winningUser.getName() + " won the game with a score of " +
		                 winningUser.getScore() + ".";
		
		chat.addWinnerMessage(message);
		setWinnerMessage(message);
		
		restartGame();
		setIsStarted(false);
		
		turns.getThreadPool().execute(new Runnable() {
			public void run() {
				try {
					Thread.sleep(7500l);
				}catch (InterruptedException ignored) { }
				
				setWinnerMessage(null);
				
				if (getIsFull()) {
					setIsStarted(true);
				}
				
				renderGame();
			}
		});
	}
	
	public void restartGame() {
		stopGame();
		startGame(false);
	}
	
	public void startGame() {
		startGame(true);
	}
	
	public void startGame(boolean renderAfter) {
		setIsStarted(true);
		
		board.generateData();
		turns.determineTurns();
		
		if (renderAfter) {
			renderGame();
		}
	}
	
	public void stopGame() {
		setIsStarted(false);
		
		this.resetScores();
		board.resetBoard();
		turns.resetTurns();
	}
	
	public void shutdownGame() {
		stopGame();
		
		turns.stopThreadPool();
	}
}
