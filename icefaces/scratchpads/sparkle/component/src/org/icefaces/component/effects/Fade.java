package org.icefaces.component.effects;

import javax.faces.component.behavior.FacesBehavior;

@FacesBehavior("org.icefaces.effects.Fade")
public class Fade extends EffectBehavior{

	private float from = 1;
	private float to = 0;
	
	public Fade() {
		
	}
	public Fade(float from, float to) {
		super();
		setFrom(from);
		setTo(to);
	}
	
	
	public float getFrom() {
		return from;
	}
	public void setFrom(float from) {
		this.from = from;
		setUsingStyleClass(false);
	}
	public float getTo() {
		return to;
	}
	public void setTo(float to) {
		this.to = to;
		setUsingStyleClass(false);		
	}
 
	
	
}
