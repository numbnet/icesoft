package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Window;

public abstract class AbstractScreen {

	protected Widget root;
	public void initScreen(Widget widget){
		this.root= widget;
	}
	
	/**
		register the screen on a parent (if applicable).
		@param root the parent to use.  If null the screen is left orphaned
	*/
	public void registerScreen(Panel root){
		if(root == null){
			//assume this widget does not need to be added to a parent (ie: a popup)
			return;
		}
		root.add(this.root);
	}
	
	/**
		hides this screen
	*/
	public void hide(){
		this.root.setVisible(false);
	}
	
	/**
		show the screen
	*/
	public void show(){
		this.root.setVisible(true);
	}
	

}
