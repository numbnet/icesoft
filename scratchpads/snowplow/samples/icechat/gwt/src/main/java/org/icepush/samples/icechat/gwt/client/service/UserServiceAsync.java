package org.icepush.samples.icechat.gwt.client.service;

import org.icepush.samples.icechat.gwt.client.Credentials;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface UserServiceAsync{
	void register(String name, AsyncCallback<Credentials> callback);
}