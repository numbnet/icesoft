/*
 *
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 *
 */
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
