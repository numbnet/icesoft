package com.icefaces.project.memory.user;

public class UserModel {
	protected String name = "User " + System.currentTimeMillis();
	protected int score = 0;
	protected boolean isTurn = false;

	public UserModel() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public boolean getIsTurn() {
		return isTurn;
	}

	public void setIsTurn(boolean isTurn) {
		this.isTurn = isTurn;
	}

	public int incrementScore() {
		return score++;
	}
}
