package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("push")
public interface PushService extends RemoteService{
	public String getPushId();
}