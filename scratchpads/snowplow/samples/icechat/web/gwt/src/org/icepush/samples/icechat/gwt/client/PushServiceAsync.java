package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PushServiceAsync {
	void getPushId(AsyncCallback<String> callback);

}