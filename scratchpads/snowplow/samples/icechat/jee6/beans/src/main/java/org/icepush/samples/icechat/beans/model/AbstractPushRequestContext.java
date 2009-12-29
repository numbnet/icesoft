package org.icepush.samples.icechat.beans.model;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icepush.PushContext;

public abstract class AbstractPushRequestContext implements Serializable, PushRequestContext{

	private static final long serialVersionUID = -6769963604096352169L;
	
	private String currentPushId;
	private PushContext pushContext;
	
	public String getCurrentPushId() {
		return currentPushId;
	}
	
	public PushContext getPushContext(){
		return pushContext;
	}

	protected void intializePushContext(HttpServletRequest request, HttpServletResponse response){
		pushContext = PushContext.getInstance(request.getServletContext());
		currentPushId = pushContext.createPushId(request, response);
	}
	
}
