package org.icepush.samples.icechat.gwt.client.screens;


import java.util.HashMap;
import java.util.Iterator;

public class PopupRegistry{
	
	private static PopupRegistry instance = null;
	
	public HashMap<String,AbstractScreen> screens = new HashMap<String,AbstractScreen>();
	
	private PopupRegistry(){
		screens.put("login",new LoginScreen());
		screens.put("reg", new RegisterScreen());
	}
	
	public void displayOnlyThisScreen(String id){
		Iterator<String> keys = this.screens.keySet().iterator();
		
		while(keys.hasNext()){
			String key = keys.next();
			hideScreen(key);
		}
		
		showScreen(id);
	}
	
	public void hideScreen(String id){
		this.screens.get(id).hide();
	}
	
	public void showScreen(String id){
		this.screens.get(id).show();
	}
	
	public static PopupRegistry getInstance(){
		if(instance == null){
			instance = new PopupRegistry();
		}
		
		return instance;
	}
	
	
}