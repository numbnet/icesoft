package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractScreen {

	protected Widget root;
	public void initScreen(Widget widget){
		this.root= widget;
	}
	
	public void registerScreen(Panel root){
		root.add(this.root);
	}
	

}
