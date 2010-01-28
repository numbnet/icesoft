package org.icepush.gwt.samples.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class MessagesPopup extends PopupPanel {
	private HideTimer timer = new HideTimer(this);
	public MessagesPopup(){
		Label messagesLabel = new Label("Someone Clicked Send");
		messagesLabel.setStylePrimaryName("messagesLabel");

		this.add(messagesLabel);
		this.setStylePrimaryName("messagesPanel");
		this.setAnimationEnabled(true);
		this.hide();
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
