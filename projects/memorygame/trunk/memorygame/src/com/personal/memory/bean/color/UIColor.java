package com.personal.memory.bean.color;

public class UIColor {
	private String displayName;
	private String imageName;
	private String htmlHex;
	
	public UIColor(String displayName,
				   String htmlHex) {
		this.displayName = displayName;
		this.htmlHex = htmlHex;
		
		this.imageName = displayName.toLowerCase();
	}

	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getHtmlHex() {
		return htmlHex;
	}
	public void setHtmlHex(String htmlHex) {
		this.htmlHex = htmlHex;
	}
}
