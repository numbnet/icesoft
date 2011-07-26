package org.icefaces.tutorial.easyajaxpush.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.tutorial.easyajaxpush.model.TextModel;

@ManagedBean(name="messageBean")
@ApplicationScoped
public class MessageBean implements Serializable {
	private static final int MAX_SIZE = 25;
	
	private List<TextModel> textList = new ArrayList<TextModel>(0);

	public MessageBean() {
	}
	
	public List<TextModel> getTextList() {
		return textList;
	}

	public void setTextList(List<TextModel> textList) {
		this.textList = textList;
	}
	
	public void addToList(String sessionId, String color) {
		textList.add(makeTextModel(sessionId, color));
		
		if (textList.size() > MAX_SIZE) {
			textList.clear();
		}
	}
	
	private TextModel makeTextModel(String sessionId, String color) {
		return new TextModel("User with session ID of " + sessionId + " selected color \"" + color + "\".",
						     color);
	}
}
