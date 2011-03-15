package org.icefaces.tutorial.easyajaxpush.model;

import java.io.Serializable;

public class TextModel implements Serializable {
	private String text;
	private String color;
	
	public TextModel() {
	}
	
	public TextModel(String text, String color) {
		this.text = text;
		this.color = color;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String toString() {
		return text;
	}
}
