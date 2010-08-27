package org.icefaces.component.effects;

public abstract class Effect{
	private boolean usingStyleClass;
	private EffectBehavior effectBehavior;
	
    EffectBehavior getEffectBehavior() {
		return effectBehavior;
	}

	void setEffectBehavior(EffectBehavior effectBehavior) {
		this.effectBehavior = effectBehavior;
	}

	public boolean isUsingStyleClass() {
		return usingStyleClass;
	}

	public void setUsingStyleClass(boolean usingStyleClass) {
		this.usingStyleClass = usingStyleClass;
	}

	public String getName() {
    	return this.getClass().getSimpleName();
    }
	
	
	public void run() {
		if(null != getEffectBehavior()) {
			effectBehavior.setRun(true);
		}
	}
}
