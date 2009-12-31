package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.Window;

public class ApplicationHistoryListener implements HistoryListener{

	public void onHistoryChanged(String token){

		
		if(token == null || token.isEmpty()){
			token = "login";
		}
				
		PopupRegistry.getInstance().displayOnlyThisScreen(token);
	}
}