package org.icepush.samples.icechat.beans.model;

import org.icepush.PushContext;

public interface PushRequestContext {
	
	public String getCurrentPushId();
	
	public PushContext getPushContext();

}
