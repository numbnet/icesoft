package org.icepush.samples.icechat.gwt.client.service;

import org.icepush.samples.icechat.gwt.client.Credentials;
import org.icepush.samples.icechat.gwt.client.User;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface UserServiceAsync{
	void register(User name, AsyncCallback<Credentials> callback);
    void login(String username, String password, AsyncCallback<Credentials> callback);
}