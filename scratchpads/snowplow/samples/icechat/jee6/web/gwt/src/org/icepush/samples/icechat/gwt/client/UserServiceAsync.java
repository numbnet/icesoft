package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.icepush.samples.icechat.gwt.client.User;

public interface UserServiceAsync{
	void register(User name, AsyncCallback<Void> callback);
}