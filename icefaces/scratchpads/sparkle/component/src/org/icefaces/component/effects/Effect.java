package org.icefaces.component.effects;

public abstract class Effect{
	private boolean usingStyleClass;
	
    public boolean isUsingStyleClass() {
		return usingStyleClass;
	}

	public void setUsingStyleClass(boolean usingStyleClass) {
		this.usingStyleClass = usingStyleClass;
	}

	public String getName() {
    	return this.getClass().getSimpleName();
    }	
}
