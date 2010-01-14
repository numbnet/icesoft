package org.icepush.samples.icechat;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icepush.PushContext;
import org.icepush.samples.icechat.IPushRequestContext;

public abstract class AbstractPushRequestContext implements Serializable,
		IPushRequestContext {

	protected String currentPushId;
	private PushContext pushContext;

	public String getCurrentPushId() {
            System.out.println("gETTING PUSHID: " + currentPushId);
		return currentPushId;
	}

	public PushContext getPushContext() {
		return pushContext;
	}

	protected void intializePushContext(HttpServletRequest request,
			HttpServletResponse response) throws IllegalArgumentException {
                System.out.println("Initializing");
		if (request == null){
                    System.out.println("Request NULL");
                    throw new IllegalArgumentException("HttpServletRequest is null");
                }
                if (response == null){
                        System.out.println("Response NULL");
			throw new IllegalArgumentException("HttpServletResponse is null");
                }
                pushContext = PushContext.getInstance(request.getSession()
				.getServletContext());
		System.out.println("CREATING PUSH ID!!!!");
                currentPushId = pushContext.createPushId(request, response);
	}


}
