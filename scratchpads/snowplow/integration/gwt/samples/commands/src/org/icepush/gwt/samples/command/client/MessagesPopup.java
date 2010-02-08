package org.icepush.gwt.samples.command.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class MessagesPopup extends PopupPanel {
	private HideTimer timer = new HideTimer(this);
	private Label messageLabel = new Label();
	public MessagesPopup(){
		
		messageLabel.setStylePrimaryName("messagesLabel");

		this.add(messageLabel);
		this.setStylePrimaryName("messagesPanel");
		this.setAnimationEnabled(true);
		this.hide();
	}
	
	public void setName(String name){
		this.messageLabel.setText(name + " has clicked send...");
	}
	/**
	 * we will override the show method to only display for 3 seconds.
	 */
	public void show(){
		super.show();
		this.timer.schedule(3000);
	}
	
	public class HideTimer extends Timer{
		private MessagesPopup target;
		public HideTimer(MessagesPopup target){
			this.target = target;
		}
		@Override
		public void run() {
			this.target.hide();
		}
		
	}
	
}
