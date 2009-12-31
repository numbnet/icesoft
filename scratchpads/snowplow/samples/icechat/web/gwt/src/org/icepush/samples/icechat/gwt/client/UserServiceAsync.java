package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync{
	void register(User name, AsyncCallback<Credentials> callback);
}