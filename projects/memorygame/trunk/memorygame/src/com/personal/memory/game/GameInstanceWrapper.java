package com.personal.memory.game;


/**
 * Class that wraps a Game model with UI specific variables, like row selected.
 */
public class GameInstanceWrapper {
	private GameInstance wrapped;
	
	private boolean selected = false;
	
	public GameInstanceWrapper() {
	}
	
	public GameInstanceWrapper(GameInstance parent) {
		this.wrapped = parent;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public GameInstance getWrapped() {
		return wrapped;
	}

	public void setWrapped(GameInstance wrapped) {
		this.wrapped = wrapped;
	}
}
