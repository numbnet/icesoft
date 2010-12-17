package org.icefaces.component.animation;

 
public class Fade extends Effect{

	private float from = 1;
	private float to = 0;
	
	public Fade() {
		
	}
 
	
	
	public float getFrom() {
		return from;
	}
	public void setFrom(float from) {
		this.from = from;
 
	}
	public float getTo() {
		return to;
	}
	public void setTo(float to) {
		this.to = to;
		 	
	}
}
