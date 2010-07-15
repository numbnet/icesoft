package org.icepush.integration.icepushplace.shared;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class UIUtil {
	public static Panel makeField(String text, Widget widget) {
		Panel toReturn = new HorizontalPanel();
		
		toReturn.add(new Label(text));
		toReturn.add(widget);
		
		return toReturn;
	}
	
	public static Label makeBoldLabel(String text) {
		Label toReturn = new Label(text);
		toReturn.setStyleName("boldLabel");
		
		return toReturn;
	}
}
