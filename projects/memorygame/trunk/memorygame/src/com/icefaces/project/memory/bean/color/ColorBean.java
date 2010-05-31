package com.icefaces.project.memory.bean.color;

public class ColorBean {
	public static final String DEFAULT_COLOR_HEX = "#000044";
	
	private static final UIColor[] FONT_COLORS = new UIColor[] {
		new UIColor("Default", DEFAULT_COLOR_HEX),
		new UIColor("Black", "#000000"),
		new UIColor("Red", "#FF3300"),
		new UIColor("Blue", "#3333FF"),
		new UIColor("Green", "#33FF33"),
		new UIColor("Teal", "#00FFFF"),
		new UIColor("Orange", "#FF9933"),
		new UIColor("Brown", "#996633"),
		new UIColor("Purple", "#9933FF"),
		new UIColor("Pink", "#FF3399")
	};
	
	public ColorBean() {
	}
	
	public UIColor[] getFontColors() {
		return FONT_COLORS;
	}
}
