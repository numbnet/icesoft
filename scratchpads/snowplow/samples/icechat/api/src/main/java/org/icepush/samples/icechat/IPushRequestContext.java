package org.icepush.samples.icechat;

import org.icepush.PushContext;

public interface IPushRequestContext {
	
	public String getCurrentPushId();
	
	public PushContext getPushContext();

}
