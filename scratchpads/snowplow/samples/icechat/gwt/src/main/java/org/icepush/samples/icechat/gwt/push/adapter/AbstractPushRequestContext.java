package org.icepush.samples.icechat.gwt.push.adapter;

import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icepush.PushContext;

public abstract class AbstractPushRequestContext implements Serializable{

	private static final long serialVersionUID = -6769963604096352169L;
	
	private String currentPushId;
	private PushContext pushContext;
	
	public String getCurrentPushId() {
		return currentPushId;
	}
	
	public PushContext getPushContext(){
		return pushContext;
	}

	protected void intializePushContext(ServletContext context, HttpServletRequest request, HttpServletResponse response){
		pushContext = PushContext.getInstance(context);
		currentPushId = pushContext.createPushId(request, response);
	}
	
}
