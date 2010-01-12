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
		return currentPushId;
	}

	public PushContext getPushContext() {
		return pushContext;
	}

	protected void intializePushContext(HttpServletRequest request,
			HttpServletResponse response) throws IllegalArgumentException {
		if (request == null)
			throw new IllegalArgumentException("HttpServletRequest is null");
		if (response == null)
			throw new IllegalArgumentException("HttpServletResponse is null");
		pushContext = PushContext.getInstance(request.getSession()
				.getServletContext());
		currentPushId = pushContext.createPushId(request, response);
	}


}
